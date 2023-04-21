package de.molimo.session.security;

import java.security.Principal;
import de.molimo.common.IParameterable;
import de.molimo.server.ThingLocal;

/**
 @author Marcus Schiesser
 */
public interface ISecurityChecker extends IParameterable {

    boolean canCall(Principal principal, ThingLocal thing);
}
