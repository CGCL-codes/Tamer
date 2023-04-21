package fire.eagle.android;

import jfireeagle.*;
import android.app.Application;

public class AndroidApplication extends Application {

    private static AndroidApplication appInstance;

    public static Token getConsumerToken() {
        Token consumerToken = new Token();
        consumerToken.setPublicKey("aCk6Ghm7cUSI");
        consumerToken.setSecret("ZlD6KLFRt1whX44Ju5jRHMHGMrMRUi1h");
        return consumerToken;
    }

    public static FireEagleClient createFireEagleClient() {
        FireEagleClient client = new FireEagleClient();
        client.setCompressionEnabled(false);
        ClientSettings settings = new ClientSettings();
        settings.setOAuthCallbackUrl(Util.getCallbackUrl());
        settings.setUserSpecificToken(Preferences.getUserSpecificAccessToken());
        settings.setGeneralToken(Preferences.getGeneralAccessToken());
        settings.setConsumerToken(getConsumerToken());
        client.setClientSettings(settings);
        return client;
    }

    public void onCreate() {
        super.onCreate();
        appInstance = this;
        Preferences.reload(this);
    }

    public static AndroidApplication get() {
        return appInstance;
    }

    public void onTerminate() {
        super.onTerminate();
    }
}
