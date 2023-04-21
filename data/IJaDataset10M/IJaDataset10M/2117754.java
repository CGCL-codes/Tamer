package org.jsecurity.spring.remoting;

import org.aopalliance.intercept.MethodInvocation;
import org.jsecurity.session.Session;
import org.springframework.remoting.support.DefaultRemoteInvocationFactory;
import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationFactory;

/**
 * A {@link RemoteInvocationFactory} that passes the session ID to the server via a
 * {@link RemoteInvocation} {@link RemoteInvocation#getAttribute(String) attribute}.
 * This factory is the client-side part of
 * the JSecurity Spring remoting invocation.  A {@link SecureRemoteInvocationExecutor} should
 * be used to export the server-side remote services to ensure that the appropriate
 * Subject and Session are bound to the remote thread during execution.
 *
 * @author Jeremy Haile
 * @author Les Hazlewood
 * @since 0.1
 */
public class SecureRemoteInvocationFactory extends DefaultRemoteInvocationFactory {

    public static final String SESSION_ID_KEY = Session.class.getName() + "_ID_KEY";

    private static final String SESSION_ID_SYSTEM_PROPERTY_NAME = "jsecurity.session.id";

    /**
     * Creates a {@link RemoteInvocation} with the current session ID as an
     * {@link RemoteInvocation#getAttribute(String) attribute}.
     *
     * @param methodInvocation the method invocation that the remote invocation should
     *                         be based on.
     * @return a remote invocation object containing the current session ID as an attribute.
     */
    public RemoteInvocation createRemoteInvocation(MethodInvocation methodInvocation) {
        String sessionId = System.getProperty(SESSION_ID_SYSTEM_PROPERTY_NAME);
        if (sessionId == null) {
            throw new IllegalStateException("System property [" + SESSION_ID_SYSTEM_PROPERTY_NAME + "] is not set.  " + "This property must be set to the JSecurity session ID for remote calls to function.");
        }
        RemoteInvocation ri = new RemoteInvocation(methodInvocation);
        ri.addAttribute(SESSION_ID_KEY, sessionId);
        return ri;
    }
}
