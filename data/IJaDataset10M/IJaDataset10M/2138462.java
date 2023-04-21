package com.volantis.synergetics.testtools.servletunit;

import com.meterware.httpunit.AuthorizationRequiredException;

/**
 * Thrown when servletunit wants to indicate that a resource is protected in an application using Basic Authorization.
 *
 * @author <a href="mailto:russgold@acm.org">Russell Gold</a>
 **/
class BasicAuthenticationRequiredException extends AuthorizationRequiredException {

    BasicAuthenticationRequiredException(String realmName) {
        super("Basic", "realm=" + realmName);
    }
}
