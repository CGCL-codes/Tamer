package ar.com.ironsoft.javaopenauth.dto;

import java.net.URLEncoder;
import ar.com.ironsoft.javaopenauth.exceptions.JavaOpenAuthException;
import ar.com.ironsoft.javaopenauth.utils.OAuthUtils;

public class AuthorizationHeaderDTO {

    private String oauthCallback;

    private String oauthConsumerKey;

    private String oauthNonce;

    private String oauthSignatureMethod;

    private String oauthTimestamp;

    private String oauthVersion;

    private String oauthSignature;

    public String toString() {
        try {
            return "OAuth oauth_nonce=\"" + oauthNonce + "\", " + "oauth_callback=\"" + oauthCallback + "\", " + "oauth_signature_method=\"" + oauthSignatureMethod + "\", " + "oauth_timestamp=\"" + oauthTimestamp + "\", " + "oauth_consumer_key=\"" + oauthConsumerKey + "\", " + "oauth_signature=\"" + URLEncoder.encode(oauthSignature, OAuthUtils.ENCODING) + "\", " + "oauth_version=\"" + oauthVersion + "\"";
        } catch (Exception e) {
            throw new JavaOpenAuthException("Error encoding header parameters", e);
        }
    }

    public String getOauthCallback() {
        return oauthCallback;
    }

    public void setOauthCallback(String oauthCallback) {
        this.oauthCallback = oauthCallback;
    }

    public String getOauthConsumerKey() {
        return oauthConsumerKey;
    }

    public void setOauthConsumerKey(String oauthConsumerKey) {
        this.oauthConsumerKey = oauthConsumerKey;
    }

    public String getOauthNonce() {
        return oauthNonce;
    }

    public void setOauthNonce(String oauthNonce) {
        this.oauthNonce = oauthNonce;
    }

    public String getOauthSignatureMethod() {
        return oauthSignatureMethod;
    }

    public void setOauthSignatureMethod(String oauthSignatureMethod) {
        this.oauthSignatureMethod = oauthSignatureMethod;
    }

    public String getOauthTimestamp() {
        return oauthTimestamp;
    }

    public void setOauthTimestamp(String oauthTimestamp) {
        this.oauthTimestamp = oauthTimestamp;
    }

    public String getOauthVersion() {
        return oauthVersion;
    }

    public void setOauthVersion(String oauthVersion) {
        this.oauthVersion = oauthVersion;
    }

    public String getOauthSignature() {
        return oauthSignature;
    }

    public void setOauthSignature(String oauthSignature) {
        this.oauthSignature = oauthSignature;
    }
}
