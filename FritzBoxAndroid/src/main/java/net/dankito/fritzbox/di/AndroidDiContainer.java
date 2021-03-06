package net.dankito.fritzbox.di;

import android.content.Context;

import net.dankito.fritzbox.model.UserSettings;
import net.dankito.fritzbox.services.AlarmManagerCronService;
import net.dankito.fritzbox.services.AndroidFileStorageService;
import net.dankito.fritzbox.services.AndroidNetworkService;
import net.dankito.fritzbox.services.CallListObserver;
import net.dankito.fritzbox.services.CsvParser;
import net.dankito.fritzbox.services.DigestService;
import net.dankito.fritzbox.services.EncryptionService;
import net.dankito.fritzbox.services.FritzBoxClient;
import net.dankito.fritzbox.services.ICronService;
import net.dankito.fritzbox.services.ICsvParser;
import net.dankito.fritzbox.services.IDigestService;
import net.dankito.fritzbox.services.IEncryptionService;
import net.dankito.fritzbox.services.IFileStorageService;
import net.dankito.fritzbox.services.INetworkService;
import net.dankito.fritzbox.services.NotificationsService;
import net.dankito.fritzbox.services.UserSettingsManager;
import net.dankito.fritzbox.services.exceptions.EncryptionServiceException;
import net.dankito.fritzbox.utils.web.IWebClient;
import net.dankito.fritzbox.utils.web.OkHttpWebClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ganymed on 03/11/16.
 */
@Module
public class AndroidDiContainer {


  protected final Context context;


  public AndroidDiContainer(Context context) {
    this.context = context;
  }


  @Provides //scope is not necessary for parameters stored within the module
  public Context getContext() {
    return context;
  }


  @Provides
  @Singleton
  public UserSettingsManager provideUserSettingsManager(IFileStorageService fileStorageService, IEncryptionService encryptionService) {
    return new UserSettingsManager(fileStorageService, encryptionService);
  }

  @Provides
  @Singleton
  public UserSettings provideUserSettings(UserSettingsManager userSettingsManager) {
    return userSettingsManager.deserializeUserSettings();
  }

  @Provides
  @Singleton
  public IWebClient provideWebClient() {
    return new OkHttpWebClient();
  }

  @Provides
  @Singleton
  public IDigestService provideDigestService() {
    return new DigestService();
  }

  @Provides
  @Singleton
  public ICsvParser provideCsvParser() {
    return new CsvParser();
  }

  @Provides
  @Singleton
  public INetworkService provideNetworkService(Context context) {
    return new AndroidNetworkService(context);
  }

  @Provides
  @Singleton
  public FritzBoxClient provideFritzBoxClient(UserSettings userSettings, IWebClient webClient, IDigestService digestService, ICsvParser csvParser) {
    return new FritzBoxClient(userSettings, webClient, digestService, csvParser);
  }


  @Provides
  @Singleton
  public ICronService provideCronService() {
    return new AlarmManagerCronService(getContext());
  }

  @Provides
  @Singleton
  public NotificationsService provideNotificationsService(IFileStorageService fileStorageService) {
    return new NotificationsService(getContext(), fileStorageService);
  }

  @Provides
  @Singleton
  public IFileStorageService provideFileStorageService() {
    return new AndroidFileStorageService(getContext());
  }

  @Provides
  @Singleton
  public IEncryptionService provideEncryptionService() throws EncryptionServiceException {
    return new EncryptionService(getContext());
  }

  @Provides
  @Singleton
  public CallListObserver provideCallListObserver() {
    return new CallListObserver(getContext());
  }

}
