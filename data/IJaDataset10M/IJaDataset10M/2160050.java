package org.exolab.jms.net.connector;

import java.security.Principal;

/**
 * An <code>Authenticator</code> authenticates new connections
 *
 * @author <a href="mailto:tma@netspace.net.au">Tim Anderson</a>
 * @version $Revision: 1.1 $ $Date: 2004/11/26 01:51:03 $
 * @see ManagedConnectionAcceptor
 */
public interface Authenticator {

    /**
     * Determines if a principal has permissions to connect
     *
     * @param principal the principal to check
     * @return <code>true</code> if the principal has permissions to connect
     * @throws ResourceException if an error occurs
     */
    boolean authenticate(Principal principal) throws ResourceException;
}
