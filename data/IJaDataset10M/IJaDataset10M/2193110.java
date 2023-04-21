package edu.uiuc.ncsa.security.delegation.token;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Interface for creating tokens. Issuers invoke this either either a map that includes the
 * values, from the servlet request or with a set of strings from which to make the tokens. No arguments in
 * the latter case means a completely new, unused token is created.
 * <p>Created by Jeff Gaynor<br>
 * on 4/10/12 at  11:10 AM
 */
public interface TokenForge {

    AuthorizationGrant getAuthorizationGrant(Map<String, String> parameters);

    AuthorizationGrant getAuthorizationGrant(HttpServletRequest request);

    AuthorizationGrant getAuthorizationGrant(String... tokens);

    AccessToken getAccessToken(Map<String, String> parameters);

    AccessToken getAccessToken(HttpServletRequest request);

    AccessToken getAccessToken(String... tokens);

    Verifier getVerifier(Map<String, String> parameters);

    Verifier getVerifier(HttpServletRequest request);

    Verifier getVerifier(String... tokens);
}
