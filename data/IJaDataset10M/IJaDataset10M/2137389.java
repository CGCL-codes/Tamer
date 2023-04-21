package de.schlund.pfixcore.auth;

import de.schlund.pfixcore.workflow.Context;

/**
 * @author mleidig@schlund.de
 */
public interface AuthorizationInterceptor {

    public void checkAuthorization(Context context) throws AuthorizationException;
}
