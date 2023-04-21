package social.hub.impl.scribe.util;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.OAuthConfig;
import org.scribe.utils.Preconditions;
import social.hub.util.Utils;

public class GenericScribe20API extends DefaultApi20 {

    private final social.hub.oauth.OAuthConfig config;

    public GenericScribe20API(social.hub.oauth.OAuthConfig config) {
        this.config = config;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return Utils.createUrlBuilder(config.getAccessTokenUrl(), config).toString();
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig scribeConfig) {
        Preconditions.checkValidUrl(config.getCallbackUrl(), "Must provide a valid url as callback. This service does not support OOB");
        return Utils.createUrlBuilder(config.getAuthorizeUrl(), config).toString();
    }
}
