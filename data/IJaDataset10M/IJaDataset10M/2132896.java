package com.qq.connect;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import com.qq.oauth.Config;
import com.qq.oauth.OAuth;
import com.qq.util.HttpClientUtils;

public class InfoToken {

    public String getInfo(String oauth_token, String oauth_token_secret, String openid, String format) throws IOException, InvalidKeyException, NoSuchAlgorithmException {
        String url = "http://openapi.qzone.qq.com/user/get_user_info";
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("format", format));
        parameters.add(new BasicNameValuePair(OAuth.OAUTH_CONSUMER_KEY, Config.APP_ID));
        parameters.add(new BasicNameValuePair(OAuth.OAUTH_NONCE, OAuth.getOauthNonce()));
        parameters.add(new BasicNameValuePair(OAuth.OAUTH_SIGNATURE_METHOD, OAuth.OAUTH_SIGNATURE_METHOD_VALUE));
        parameters.add(new BasicNameValuePair(OAuth.OAUTH_TIMESTAMP, OAuth.getOauthTimestamp()));
        parameters.add(new BasicNameValuePair(OAuth.OAUTH_TOKEN, oauth_token));
        parameters.add(new BasicNameValuePair(OAuth.OAUTH_VERSION, OAuth.OAUTH_VERSION_VALUE));
        parameters.add(new BasicNameValuePair(OAuth.OPENID, openid));
        parameters.add(new BasicNameValuePair(OAuth.OAUTH_SIGNATURE, OAuth.getOauthSignature("GET", url, parameters, oauth_token_secret)));
        url += "?" + OAuth.getSerialParameters(parameters, true);
        DefaultHttpClient httpclient = HttpClientUtils.getHttpClient();
        String html = HttpClientUtils.getHtml(httpclient, url, "UTF-8");
        return html;
    }
}
