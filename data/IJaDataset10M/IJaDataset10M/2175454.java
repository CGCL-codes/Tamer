package org.apache.shindig.gadgets.oauth;

import org.apache.shindig.auth.SecurityToken;
import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.gadgets.GadgetContext;
import org.apache.shindig.gadgets.GadgetException;
import org.apache.shindig.gadgets.GadgetSpecFactory;
import org.apache.shindig.gadgets.oauth.AccessorInfo.HttpMethod;
import org.apache.shindig.gadgets.oauth.AccessorInfo.OAuthParamLocation;
import org.apache.shindig.gadgets.oauth.OAuthResponseParams.OAuthRequestException;
import org.apache.shindig.gadgets.oauth.OAuthStore.ConsumerInfo;
import org.apache.shindig.gadgets.oauth.OAuthStore.TokenInfo;
import org.apache.shindig.gadgets.spec.GadgetSpec;
import org.apache.shindig.gadgets.spec.OAuthService;
import org.apache.shindig.gadgets.spec.OAuthSpec;
import org.apache.shindig.gadgets.spec.SpecParserException;
import org.apache.shindig.gadgets.spec.OAuthService.Location;
import org.apache.shindig.gadgets.spec.OAuthService.Method;
import com.google.inject.Inject;
import net.oauth.OAuthServiceProvider;
import org.apache.commons.lang.StringUtils;

/**
 * Higher-level interface that allows callers to store and retrieve
 * OAuth-related data directly from {@code GadgetSpec}s, {@code GadgetContext}s,
 * etc. See {@link OAuthStore} for a more detailed explanation of the OAuth
 * Data Store.
 */
public class GadgetOAuthTokenStore {

    private final OAuthStore store;

    private final GadgetSpecFactory specFactory;

    /**
   * Public constructor.
   *
   * @param store an {@link OAuthStore} that can store and retrieve OAuth
   *              tokens, as well as information about service providers.
   */
    @Inject
    public GadgetOAuthTokenStore(OAuthStore store, GadgetSpecFactory specFactory) {
        this.store = store;
        this.specFactory = specFactory;
    }

    /**
   * Retrieve an AccessorInfo and OAuthAccessor that are ready for signing OAuthMessages.  To do
   * this, we need to figure out:
   *
   * - what consumer key/secret to use for signing.
   * - if an access token should be used for the request, and if so what it is.   *
   * - the OAuth request/authorization/access URLs.
   * - what HTTP method to use for request token and access token requests
   * - where the OAuth parameters are located.
   *
   * Note that most of that work gets skipped for signed fetch, we just look up the consumer key
   * and secret for that.  Signed fetch always sticks the parameters in the query string.
   */
    public AccessorInfo getOAuthAccessor(SecurityToken securityToken, OAuthArguments arguments, OAuthClientState clientState, OAuthResponseParams responseParams) throws OAuthRequestException {
        AccessorInfoBuilder accessorBuilder = new AccessorInfoBuilder();
        OAuthServiceProvider provider = null;
        if (arguments.programmaticConfig()) {
            provider = loadProgrammaticConfig(arguments, accessorBuilder, responseParams);
        } else if (arguments.mayUseToken()) {
            provider = lookupSpecInfo(securityToken, arguments, accessorBuilder, responseParams);
        } else {
            accessorBuilder.setParameterLocation(AccessorInfo.OAuthParamLocation.URI_QUERY);
        }
        ConsumerInfo consumer;
        try {
            consumer = store.getConsumerKeyAndSecret(securityToken, arguments.getServiceName(), provider);
            accessorBuilder.setConsumer(consumer);
        } catch (GadgetException e) {
            throw responseParams.oauthRequestException(OAuthError.UNKNOWN_PROBLEM, "Unable to retrieve consumer key", e);
        }
        if (arguments.mayUseToken() && securityToken.getOwnerId() != null && securityToken.getOwnerId().equals(securityToken.getViewerId())) {
            lookupToken(securityToken, consumer, arguments, clientState, accessorBuilder, responseParams);
        }
        return accessorBuilder.create(responseParams);
    }

    /**
   * Lookup information contained in the gadget spec.
   */
    private OAuthServiceProvider lookupSpecInfo(SecurityToken securityToken, OAuthArguments arguments, AccessorInfoBuilder accessorBuilder, OAuthResponseParams responseParams) throws OAuthRequestException {
        GadgetSpec spec = findSpec(securityToken, arguments, responseParams);
        OAuthSpec oauthSpec = spec.getModulePrefs().getOAuthSpec();
        if (oauthSpec == null) {
            throw responseParams.oauthRequestException(OAuthError.BAD_OAUTH_CONFIGURATION, "Failed to retrieve OAuth URLs, spec for gadget " + securityToken.getAppUrl() + " does not contain OAuth element.");
        }
        OAuthService service = oauthSpec.getServices().get(arguments.getServiceName());
        if (service == null) {
            throw responseParams.oauthRequestException(OAuthError.BAD_OAUTH_CONFIGURATION, "Failed to retrieve OAuth URLs, spec for gadget does not contain OAuth service " + arguments.getServiceName() + ".  Known services: " + StringUtils.join(oauthSpec.getServices().keySet(), ',') + '.');
        }
        accessorBuilder.setParameterLocation(getStoreLocation(service.getRequestUrl().location, responseParams));
        accessorBuilder.setMethod(getStoreMethod(service.getRequestUrl().method, responseParams));
        return new OAuthServiceProvider(service.getRequestUrl().url.toJavaUri().toASCIIString(), service.getAuthorizationUrl().toJavaUri().toASCIIString(), service.getAccessUrl().url.toJavaUri().toASCIIString());
    }

    private OAuthServiceProvider loadProgrammaticConfig(OAuthArguments arguments, AccessorInfoBuilder accessorBuilder, OAuthResponseParams responseParams) throws OAuthRequestException {
        try {
            String paramLocationStr = arguments.getRequestOption(OAuthArguments.PARAM_LOCATION_PARAM, "");
            Location l = Location.parse(paramLocationStr);
            accessorBuilder.setParameterLocation(getStoreLocation(l, responseParams));
            String requestMethod = arguments.getRequestOption(OAuthArguments.REQUEST_METHOD_PARAM, "GET");
            Method m = Method.parse(requestMethod);
            accessorBuilder.setMethod(getStoreMethod(m, responseParams));
            String requestTokenUrl = arguments.getRequestOption(OAuthArguments.REQUEST_TOKEN_URL_PARAM);
            verifyUrl(requestTokenUrl, responseParams);
            String accessTokenUrl = arguments.getRequestOption(OAuthArguments.ACCESS_TOKEN_URL_PARAM);
            verifyUrl(accessTokenUrl, responseParams);
            String authorizationUrl = arguments.getRequestOption(OAuthArguments.AUTHORIZATION_URL_PARAM);
            verifyUrl(authorizationUrl, responseParams);
            return new OAuthServiceProvider(requestTokenUrl, authorizationUrl, accessTokenUrl);
        } catch (SpecParserException e) {
            throw responseParams.oauthRequestException(OAuthError.BAD_OAUTH_CONFIGURATION, e.getMessage());
        }
    }

    private void verifyUrl(String url, OAuthResponseParams responseParams) throws OAuthRequestException {
        if (url == null) {
            return;
        }
        Uri uri;
        try {
            uri = Uri.parse(url);
        } catch (Throwable t) {
            throw responseParams.oauthRequestException(OAuthError.BAD_OAUTH_CONFIGURATION, "Invalid url: " + url);
        }
        if (!uri.isAbsolute()) {
            throw responseParams.oauthRequestException(OAuthError.BAD_OAUTH_CONFIGURATION, "Invalid url: " + url);
        }
    }

    /**
   * Figure out the OAuth token that should be used with this request.  We check for this in three
   * places.  In order of priority:
   *
   * 1) From information we cached on the client.
   *    We encrypt the token and cache on the client for performance.
   *
   * 2) From information we have in our persistent state.
   *    We persist the token server-side so we can look it up if necessary.
   *
   * 3) From information the gadget developer tells us to use (a preapproved request token.)
   *    Gadgets can be initialized with preapproved request tokens.  If the user tells the service
   *    provider they want to add a gadget to a gadget container site, the service provider can
   *    create a preapproved request token for that site and pass it to the gadget as a user
   *    preference.
   */
    private void lookupToken(SecurityToken securityToken, ConsumerInfo consumerInfo, OAuthArguments arguments, OAuthClientState clientState, AccessorInfoBuilder accessorBuilder, OAuthResponseParams responseParams) throws OAuthRequestException {
        if (clientState.getRequestToken() != null) {
            accessorBuilder.setRequestToken(clientState.getRequestToken());
            accessorBuilder.setTokenSecret(clientState.getRequestTokenSecret());
        } else if (clientState.getAccessToken() != null) {
            accessorBuilder.setAccessToken(clientState.getAccessToken());
            accessorBuilder.setTokenSecret(clientState.getAccessTokenSecret());
            accessorBuilder.setSessionHandle(clientState.getSessionHandle());
            accessorBuilder.setTokenExpireMillis(clientState.getTokenExpireMillis());
        } else {
            TokenInfo tokenInfo;
            try {
                tokenInfo = store.getTokenInfo(securityToken, consumerInfo, arguments.getServiceName(), arguments.getTokenName());
            } catch (GadgetException e) {
                throw responseParams.oauthRequestException(OAuthError.UNKNOWN_PROBLEM, "Unable to retrieve access token", e);
            }
            if (tokenInfo != null && tokenInfo.getAccessToken() != null) {
                accessorBuilder.setAccessToken(tokenInfo.getAccessToken());
                accessorBuilder.setTokenSecret(tokenInfo.getTokenSecret());
                accessorBuilder.setSessionHandle(tokenInfo.getSessionHandle());
                accessorBuilder.setTokenExpireMillis(tokenInfo.getTokenExpireMillis());
            } else {
                accessorBuilder.setRequestToken(arguments.getRequestToken());
                accessorBuilder.setTokenSecret(arguments.getRequestTokenSecret());
            }
        }
    }

    private OAuthParamLocation getStoreLocation(Location location, OAuthResponseParams responseParams) throws OAuthRequestException {
        switch(location) {
            case HEADER:
                return OAuthParamLocation.AUTH_HEADER;
            case URL:
                return OAuthParamLocation.URI_QUERY;
            case BODY:
                return OAuthParamLocation.POST_BODY;
        }
        throw responseParams.oauthRequestException(OAuthError.INVALID_REQUEST, "Unknown parameter location " + location);
    }

    private HttpMethod getStoreMethod(Method method, OAuthResponseParams responseParams) throws OAuthRequestException {
        switch(method) {
            case GET:
                return HttpMethod.GET;
            case POST:
                return HttpMethod.POST;
        }
        throw responseParams.oauthRequestException(OAuthError.INVALID_REQUEST, "Unknown method " + method);
    }

    private GadgetSpec findSpec(final SecurityToken securityToken, final OAuthArguments arguments, OAuthResponseParams responseParams) throws OAuthRequestException {
        try {
            GadgetContext context = new OAuthGadgetContext(securityToken, arguments);
            return specFactory.getGadgetSpec(context);
        } catch (IllegalArgumentException e) {
            throw responseParams.oauthRequestException(OAuthError.UNKNOWN_PROBLEM, "Could not fetch gadget spec, gadget URI invalid.", e);
        } catch (GadgetException e) {
            throw responseParams.oauthRequestException(OAuthError.UNKNOWN_PROBLEM, "Could not fetch gadget spec", e);
        }
    }

    /**
   * Store an access token for the given user/gadget/service/token name
   */
    public void storeTokenKeyAndSecret(SecurityToken securityToken, ConsumerInfo consumerInfo, OAuthArguments arguments, TokenInfo tokenInfo, OAuthResponseParams responseParams) throws OAuthRequestException {
        try {
            store.setTokenInfo(securityToken, consumerInfo, arguments.getServiceName(), arguments.getTokenName(), tokenInfo);
        } catch (GadgetException e) {
            throw responseParams.oauthRequestException(OAuthError.UNKNOWN_PROBLEM, "Unable to store access token", e);
        }
    }

    /**
   * Remove an access token for the given user/gadget/service/token name
   */
    public void removeToken(SecurityToken securityToken, ConsumerInfo consumerInfo, OAuthArguments arguments, OAuthResponseParams responseParams) throws OAuthRequestException {
        try {
            store.removeToken(securityToken, consumerInfo, arguments.getServiceName(), arguments.getTokenName());
        } catch (GadgetException e) {
            throw responseParams.oauthRequestException(OAuthError.UNKNOWN_PROBLEM, "Unable to remove access token", e);
        }
    }
}
