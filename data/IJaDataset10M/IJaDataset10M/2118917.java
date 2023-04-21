package org.apache.shindig.social.core.oauth;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.apache.shindig.auth.AnonymousAuthenticationHandler;
import org.apache.shindig.auth.AuthenticationHandler;
import org.apache.shindig.auth.UrlParameterAuthenticationHandler;
import java.util.List;

public class AuthenticationHandlerProvider implements Provider<List<AuthenticationHandler>> {

    protected List<AuthenticationHandler> handlers;

    @Inject
    public AuthenticationHandlerProvider(UrlParameterAuthenticationHandler urlParam, OAuthAuthenticationHandler threeLeggedOAuth, AnonymousAuthenticationHandler anonymous) {
        handlers = Lists.newArrayList(urlParam, threeLeggedOAuth, anonymous);
    }

    public List<AuthenticationHandler> get() {
        return handlers;
    }
}
