package com.myspace.myspaceid.oauth;

/**
 * Class that encapsulates an OAuth token.
 */
public class OAuthToken {

    protected String key;

    protected String secret;

    public OAuthToken(String key, String secret) {
        this.key = key;
        this.secret = secret;
    }

    public OAuthToken(String str) {
        int i = str.indexOf("=");
        int j = str.indexOf("&", i + 1);
        key = str.substring(i + 1, j).trim();
        int k = str.indexOf("=", j + 1);
        int m = str.indexOf("&", k + 1);
        m = m == -1 ? str.length() : m;
        secret = str.substring(k + 1, m).trim();
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String toString() {
        return "OAuthToken: key = " + key + " , secret = " + secret;
    }
}
