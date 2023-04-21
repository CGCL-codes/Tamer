package com.google.code.stk.server.controller.oauth;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import twitter4j.Twitter;
import twitter4j.auth.RequestToken;
import com.google.code.stk.server.service.TwitterUtil;

public class ShowPinCodeController extends Controller {

    @Override
    public Navigation run() throws Exception {
        Twitter twitter = TwitterUtil.getNonAuthTwitter();
        RequestToken requestToken = twitter.getOAuthRequestToken();
        TwitterUtil.saveRequestToken(requestToken);
        return redirect(requestToken.getAuthorizationURL());
    }
}
