package org.objectwiz.remote.container;

import java.security.Principal;
import java.util.Set;

/**
 * Abstraction for servlet containers.
 *
 * @author Vincent Laugier <vincent.laugier at helmet.fr>
 */
public interface ServletContainer {

    /**
     * Shall return the roles of the user corresponding to the given principal.
     */
    public Set<String> getRoles(Principal userPrincipal);
}
