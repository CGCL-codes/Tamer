package org.opensocial.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import net.oauth.OAuth;
import net.oauth.OAuthException;
import org.opensocial.client.OpenSocialClient;
import org.opensocial.client.OpenSocialProvider;
import org.opensocial.client.Token;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Any Android activity that wishes to use the OpenSocial apis should extend
 * this class. Call getOpenSocialClient to get the client library and then issue
 * getFriends or other calls.
 * 
 * Details: If the user is not already authed with this app, the
 * OpenSocialChooserActivity will be called. That activity displays a list of
 * all supported providers (passed in to this class). The user chooses a
 * provider and is redirected to the browser. After granting access, the oauth
 * redirect url calls the android scheme provided to this library.
 * 
 * @author Cassandra Doll
 */
public class OpenSocialActivity {

    public static final String ACCESS_TOKEN_PREF = "accessToken";

    public static final String ACCESS_TOKEN_SECRET_PREF = "accessTokenSecret";

    private OpenSocialProvider provider;

    private Context context;

    private SharedPreferences prefs;

    private Intent intent;

    private Map<OpenSocialProvider, Token> supportedProviders;

    private String androidScheme;

    public OpenSocialActivity(Activity context, Map<OpenSocialProvider, Token> supportedProviders, String androidScheme) {
        this.context = context;
        this.prefs = context.getSharedPreferences("default", Activity.MODE_PRIVATE);
        this.intent = context.getIntent();
        this.supportedProviders = supportedProviders;
        this.androidScheme = androidScheme;
    }

    public OpenSocialProvider getProvider() {
        return provider;
    }

    public OpenSocialClient getOpenSocialClient() {
        Token accessToken = loadAccessToken();
        String providerString = prefs.getString(OpenSocialChooserActivity.CURRENT_PROVIDER_PREF, null);
        if (accessToken.token == null && (intent.getData() == null || providerString == null)) {
            setupUsersOauthToken(supportedProviders, androidScheme);
            return null;
        }
        provider = OpenSocialProvider.valueOf(providerString.toUpperCase());
        OpenSocialClient client = getClient(provider, supportedProviders);
        if (intent.getData() != null) {
            try {
                accessToken = client.getAccessToken(provider, loadRequestToken());
            } catch (IOException e) {
                throw new RuntimeException("Error occured fetching access token", e);
            } catch (URISyntaxException e) {
                throw new RuntimeException("Error occured fetching access token", e);
            } catch (OAuthException e) {
                throw new RuntimeException("Error occured fetching access token", e);
            }
            persistAccessToken(accessToken);
        }
        client.setProperty(OpenSocialClient.Properties.ACCESS_TOKEN, accessToken.token);
        client.setProperty(OpenSocialClient.Properties.ACCESS_TOKEN_SECRET, accessToken.secret);
        client.setProperty(OpenSocialClient.Properties.REST_BASE_URI, provider.restEndpoint);
        client.setProperty(OpenSocialClient.Properties.RPC_ENDPOINT, provider.rpcEndpoint);
        return client;
    }

    private OpenSocialClient getClient(OpenSocialProvider provider, Map<OpenSocialProvider, Token> supportedProviders) {
        OpenSocialClient client = new OpenSocialClient();
        Token consumerToken = supportedProviders.get(provider);
        if (consumerToken != null) {
            client.setProperty(OpenSocialClient.Properties.CONSUMER_KEY, consumerToken.token);
            client.setProperty(OpenSocialClient.Properties.CONSUMER_SECRET, consumerToken.secret);
        }
        return client;
    }

    private void setupUsersOauthToken(Map<OpenSocialProvider, Token> supportedProviders, String browserScheme) {
        Intent oauthLibrary = new Intent(context, OpenSocialChooserActivity.class);
        ArrayList<String> providerStrings = new ArrayList<String>();
        for (Map.Entry<OpenSocialProvider, Token> entry : supportedProviders.entrySet()) {
            providerStrings.add(entry.getKey().providerName);
            addConsumerTokenExtra(oauthLibrary, entry.getKey(), entry.getValue());
        }
        oauthLibrary.putExtra(OpenSocialChooserActivity.ANDROID_SCHEME, browserScheme);
        oauthLibrary.putStringArrayListExtra(OpenSocialChooserActivity.PROVIDERS, providerStrings);
        context.startActivity(oauthLibrary);
    }

    private void addConsumerTokenExtra(Intent oauthLibrary, OpenSocialProvider provider, Token consumerToken) {
        if (consumerToken != null) {
            oauthLibrary.putExtra(provider.toString(), new String[] { consumerToken.token, consumerToken.secret });
        }
    }

    private void persistAccessToken(Token userSpecificAccessToken) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(ACCESS_TOKEN_PREF, userSpecificAccessToken.token);
        editor.putString(ACCESS_TOKEN_SECRET_PREF, userSpecificAccessToken.secret);
        editor.commit();
    }

    private Token loadAccessToken() {
        return new Token(prefs.getString(ACCESS_TOKEN_PREF, null), prefs.getString(ACCESS_TOKEN_SECRET_PREF, null));
    }

    public void clearSavedAuthentication() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(OpenSocialChooserActivity.CURRENT_PROVIDER_PREF);
        editor.remove(OpenSocialChooserActivity.REQUEST_TOKEN_PREF);
        editor.remove(OpenSocialChooserActivity.REQUEST_TOKEN_SECRET_PREF);
        editor.remove(ACCESS_TOKEN_PREF);
        editor.remove(ACCESS_TOKEN_SECRET_PREF);
        editor.remove(OpenSocialChooserActivity.CURRENT_PROVIDER_PREF);
        editor.commit();
    }

    private Token loadRequestToken() {
        String requestTokenPref = prefs.getString(OpenSocialChooserActivity.REQUEST_TOKEN_PREF, null);
        if (requestTokenPref != null) {
            String requestTokenSecretPref = prefs.getString(OpenSocialChooserActivity.REQUEST_TOKEN_SECRET_PREF, "");
            return new Token(requestTokenPref, requestTokenSecretPref);
        }
        Uri data = intent.getData();
        if (data != null) {
            int tokenIndex = data.toString().indexOf("oauth_token");
            if (tokenIndex != -1) {
                String token = data.toString().substring(tokenIndex + 12);
                return new Token(OAuth.decodePercent(token), "");
            }
        }
        return null;
    }
}
