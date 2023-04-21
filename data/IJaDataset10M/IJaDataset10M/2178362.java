package org.jboss.resteasy.auth.oauth;

import java.io.IOException;
import java.net.URISyntaxException;
import net.oauth.OAuth;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthException;
import net.oauth.OAuthMessage;
import net.oauth.OAuthProblemException;
import net.oauth.SimpleOAuthValidator;
import org.jboss.resteasy.logging.Logger;

/**
 * OAuth Validator implementation to check OAuth Messages
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class OAuthValidator extends SimpleOAuthValidator {

    private static final Logger logger = Logger.getLogger(OAuthValidator.class);

    private OAuthProvider provider;

    public OAuthValidator(OAuthProvider provider) {
        this.provider = provider;
    }

    /**
	 * Overridden to deprecate it since we cannot hide it. at least make sure we won't use it
	 */
    @Override
    @Deprecated
    public void validateMessage(OAuthMessage message, OAuthAccessor accessor) throws OAuthException, IOException, URISyntaxException {
        throw new RuntimeException("Do not use this method");
    }

    /**
	 * Overridden to validate the timestamp and nonces last since they have side-effects of storing
	 * data about the message, so we have to make sure the message is valid before we do that. 
	 */
    public void validateMessage(OAuthMessage message, OAuthAccessor accessor, OAuthToken requestToken) throws OAuthException, IOException, URISyntaxException {
        checkSingleParameters(message);
        validateVersion(message);
        validateSignature(message, accessor);
        validateTimestampAndNonce(message, requestToken);
    }

    /**
     * Throw an exception if the timestamp is out of range or the nonce has been
     * validated previously.
     */
    protected void validateTimestampAndNonce(OAuthMessage message, OAuthToken token) throws IOException, OAuthProblemException {
        message.requireParameters(OAuth.OAUTH_TIMESTAMP, OAuth.OAUTH_NONCE);
        long timestamp = Long.parseLong(message.getParameter(OAuth.OAUTH_TIMESTAMP));
        long now = currentTimeMsec();
        validateTimestamp(message, now, token);
        validateNonce(message, timestamp, now);
    }

    /**
	 * Overridden to delegate timestamp validation to the provider
	 */
    protected void validateTimestamp(OAuthMessage message, long timestamp, OAuthToken token) throws IOException, OAuthProblemException {
        if (token == null) return;
        try {
            provider.checkTimestamp(token, timestamp);
        } catch (org.jboss.resteasy.auth.oauth.OAuthException e) {
            logger.error("Invalid timestamp", e);
            throw new OAuthProblemException(OAuth.Problems.TIMESTAMP_REFUSED);
        }
    }
}
