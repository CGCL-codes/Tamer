package com.tenline.pinecone.platform.sdk.oauth;

import java.io.IOException;
import com.google.api.client.auth.oauth.OAuthAuthorizeTemporaryTokenUrl;
import com.google.api.client.auth.oauth.OAuthCallbackUrl;
import com.google.api.client.auth.oauth.OAuthGetAccessToken;
import com.google.api.client.auth.oauth.OAuthGetTemporaryToken;
import com.google.api.client.auth.oauth.OAuthHmacSigner;
import com.google.api.client.http.HttpMethod;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.UrlEncodedParser;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.tenline.pinecone.platform.sdk.development.APIResponse;
import com.tenline.pinecone.platform.sdk.development.AbstractAPI;

/**
 * @author Bill
 *
 */
public class AuthorizationAPI extends AbstractAPI {

    private HttpTransport transport;

    private HttpRequestFactory requestFactory;

    /**
	 * 
	 * @param host
	 * @param port
	 * @param context
	 */
    public AuthorizationAPI(String host, String port, String context) {
        super(host, port, context);
        url += "/oauth";
        transport = new NetHttpTransport();
        requestFactory = transport.createRequestFactory();
    }

    /**
	 * Request Temporary Token
	 * @param consumerKey
	 * @param consumerSecret
	 * @param callback
	 * @return
	 */
    public APIResponse requestToken(String consumerKey, String consumerSecret, String callback) {
        APIResponse response = new APIResponse();
        try {
            OAuthGetTemporaryToken temporaryToken = new OAuthGetTemporaryToken(url + "/requestToken");
            temporaryToken.transport = transport;
            OAuthHmacSigner signer = new OAuthHmacSigner();
            temporaryToken.signer = signer;
            temporaryToken.consumerKey = consumerKey;
            signer.clientSharedSecret = consumerSecret;
            temporaryToken.callback = callback;
            response.setDone(true);
            response.setMessage(temporaryToken.execute());
        } catch (IOException e) {
            response.setDone(false);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    /**
	 * Authorize Temporary Token
	 * @param token
	 * @return
	 */
    public APIResponse authorizeToken(String token) {
        APIResponse response = new APIResponse();
        try {
            OAuthAuthorizeTemporaryTokenUrl tokenUrl = new OAuthAuthorizeTemporaryTokenUrl(url + "/authorization" + "?oauth_token=" + token);
            response.setDone(true);
            response.setMessage(requestFactory.buildRequest(HttpMethod.GET, tokenUrl, null).execute().parseAsString());
        } catch (IOException e) {
            response.setDone(false);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    /**
	 * Confirm Authorization
	 * @param token
	 * @param decision
	 * @return
	 */
    public APIResponse confirmAuthorization(String token, String decision) {
        APIResponse response = new APIResponse();
        try {
            OAuthCallbackUrl callbackUrl = new OAuthCallbackUrl(url + "/authorization/confirm?oauth_token=" + token + "&xoauth_end_user_decision=" + decision);
            UrlEncodedParser.parse(requestFactory.buildRequest(HttpMethod.GET, callbackUrl, null).execute().parseAsString(), callbackUrl);
            response.setDone(true);
            response.setMessage(callbackUrl);
        } catch (IOException e) {
            response.setDone(false);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    /**
	 * Access Long-Lived Token
	 * @param consumerKey
	 * @param consumerSecret
	 * @param token
	 * @param tokenSecret
	 * @param verifier
	 * @return
	 */
    public APIResponse accessToken(String consumerKey, String consumerSecret, String token, String tokenSecret, String verifier) {
        APIResponse response = new APIResponse();
        try {
            OAuthGetAccessToken accessToken = new OAuthGetAccessToken(url + "/accessToken");
            accessToken.transport = transport;
            OAuthHmacSigner signer = new OAuthHmacSigner();
            accessToken.signer = signer;
            accessToken.consumerKey = consumerKey;
            accessToken.temporaryToken = token;
            signer.clientSharedSecret = consumerSecret;
            signer.tokenSharedSecret = tokenSecret;
            accessToken.verifier = verifier;
            response.setDone(true);
            response.setMessage(accessToken.execute());
        } catch (IOException e) {
            response.setDone(false);
            response.setMessage(e.getMessage());
        }
        return response;
    }
}
