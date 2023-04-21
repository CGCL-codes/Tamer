package com.appspot.piment.api.sina;

import java.util.logging.Logger;
import weibo4j.Weibo;
import weibo4j.http.AccessToken;
import weibo4j.http.RequestToken;
import com.appspot.piment.Constants;
import com.appspot.piment.model.AuthToken;
import com.appspot.piment.model.WeiboSource;

public class SinaAuthApi extends ApiBase {

    private static final Logger log = Logger.getLogger(Constants.FQCN + SinaAuthApi.class.getName());

    private String authCallbackUrl;

    public SinaAuthApi() {
        super();
        this.subInit();
    }

    public SinaAuthApi(AuthToken authToken) {
        super(authToken);
        this.subInit();
    }

    protected void subInit() {
        this.authCallbackUrl = configMap.get("sina.oauth.callback");
        this.weibo = new Weibo();
    }

    public AuthToken requestToken() throws Exception {
        RequestToken requestToken;
        requestToken = weibo.getOAuthRequestToken(this.authCallbackUrl);
        log.info("Token:" + requestToken.getToken());
        log.info("TokenSecret:" + requestToken.getTokenSecret());
        log.info(requestToken.getAuthenticationURL());
        AuthToken authToken = new AuthToken(WeiboSource.Sina, requestToken.getToken(), requestToken.getTokenSecret());
        return authToken;
    }

    public AuthToken exchangeToken(String oauth_verifier) throws Exception {
        AccessToken accessToken = weibo.getOAuthAccessToken(this.authToken.getToken(), this.authToken.getTokenSecret(), oauth_verifier);
        log.info("new_oauth_token ---> " + accessToken.getToken());
        log.info("new_oauth_token_secret ---> " + accessToken.getTokenSecret());
        log.info("user_id ---> " + accessToken.getUserId());
        log.info("screenName ---> " + accessToken.getScreenName());
        authToken.setUserName(Long.toString(accessToken.getUserId()));
        authToken.setToken(accessToken.getToken());
        authToken.setTokenSecret(accessToken.getTokenSecret());
        return authToken;
    }
}
