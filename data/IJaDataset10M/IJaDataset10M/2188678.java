package weibo4andriod.http;

import weibo4andriod.WeiboException;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * representing unauthorized Request Token which is passed to the service provider when acquiring the authorized Access Token
 */
public class RequestToken extends OAuthToken {

    private HttpClient httpClient;

    private static final long serialVersionUID = -8214365845469757952L;

    RequestToken(Response res, HttpClient httpClient) throws WeiboException {
        super(res);
        this.httpClient = httpClient;
    }

    RequestToken(String token, String tokenSecret) {
        super(token, tokenSecret);
    }

    public String getAuthorizationURL() {
        return httpClient.getAuthorizationURL() + "?oauth_token=" + getToken();
    }

    /**
     * since Weibo4J 2.0.10
     */
    public String getAuthenticationURL() {
        return httpClient.getAuthenticationRL() + "?oauth_token=" + getToken();
    }

    public AccessToken getAccessToken(String pin) throws WeiboException {
        return httpClient.getOAuthAccessToken(this, pin);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RequestToken that = (RequestToken) o;
        if (httpClient != null ? !httpClient.equals(that.httpClient) : that.httpClient != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (httpClient != null ? httpClient.hashCode() : 0);
        return result;
    }
}
