package fi.foyt.cs.oauth;

import java.util.concurrent.ConcurrentMap;
import org.openid4java.consumer.ConsumerException;
import org.openid4java.consumer.ConsumerManager;
import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.ext.oauth.provider.AuthorizationServerResource;
import org.restlet.ext.oauth.provider.ValidationServerResource;
import org.restlet.ext.oauth.provider.data.ClientStore;
import org.restlet.ext.oauth.provider.data.ClientStoreFactory;
import org.restlet.ext.openid.CallbackCacheFilter;
import org.restlet.ext.openid.SetCallbackFilter;
import org.restlet.resource.Finder;
import org.restlet.resource.ResourceException;
import org.restlet.routing.Filter;
import org.restlet.routing.Router;
import fi.foyt.cs.oauth.provider.data.GAEClientStore;

public class AuthorizationServer extends Application {

    @Override
    public synchronized Restlet createInboundRoot() {
        Context ctx = getContext();
        ConcurrentMap<String, Object> attribs = ctx.getAttributes();
        ClientStore clientStore = ClientStoreFactory.getInstance();
        attribs.put(ClientStore.class.getCanonicalName(), clientStore);
        Router router = new Router(ctx);
        router.attach("/authorize", AuthorizationServerResource.class);
        router.attach("/access_token", org.restlet.ext.oauth.provider.AccessTokenServerResource.class);
        router.attach("/access_token_g?client_id={client_id}&client_secret={client_secret}&code={code}&grant_type={grant_type}&redirect_uri={redirect_uri}", AccessTokenServerResource.class).setMatchingQuery(true);
        router.attach("/validate", ValidationServerResource.class);
        Filter loginFilter = new SetCallbackFilter();
        loginFilter.setNext(LoginResource.class);
        router.attach("/login", loginFilter);
        try {
            ConsumerManager consumerManager = new ConsumerManager();
            attribs.put("consumer_manager", consumerManager);
        } catch (ConsumerException e) {
            throw new ResourceException(e);
        }
        CallbackCacheFilter openIdAuthCache = new CallbackCacheFilter(getContext());
        UserCreateFilter openIduserCreateFilter = new UserCreateFilter(getContext());
        openIdAuthCache.setNext(openIduserCreateFilter);
        openIduserCreateFilter.setNext(new Finder(getContext(), OpenIdLogin.class));
        router.attach("/openid_login", openIdAuthCache);
        CallbackCacheFilter fbAuthCache = new CallbackCacheFilter(getContext());
        UserCreateFilter fbUserCreateFilter = new UserCreateFilter(getContext());
        fbAuthCache.setNext(fbUserCreateFilter);
        fbUserCreateFilter.setNext(new Finder(getContext(), FacebookLogin.class));
        router.attach("/facebook_login", fbAuthCache);
        router.attach("/login_page", LoginPageResource.class);
        attribs.put("oauth_auth_page", "auth/authorize.html");
        attribs.put("oauth_auth_skip_same_scope", "true");
        router.attach("/auth_page", new Finder(ctx, AuthPageServerResource.class));
        return router;
    }

    static {
        ClientStoreFactory.setClientStoreImpl(GAEClientStore.class, new Object[0]);
    }
}
