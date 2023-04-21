package net.oauth;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * An algorithm to determine whether a message has a valid signature, a correct
 * version number, a fresh timestamp, etc.
 *
 * @author Dirk Balfanz
 * @author John Kristian
 */
public interface OAuthValidator {

    /**
     * Check that the given message from the given accessor is valid.
     * 
     * @throws OAuthException
     *             the message doesn't conform to OAuth. The exception contains
     *             information that conforms to the OAuth <a
     *             href="http://wiki.oauth.net/ProblemReporting">Problem
     *             Reporting extension</a>.
     * @throws IOException
     *             the message couldn't be read.
     * @throws URISyntaxException
     *             the message URL is invalid.
     */
    public void validateMessage(OAuthMessage message, OAuthAccessor accessor) throws OAuthException, IOException, URISyntaxException;
}
