package org.atricore.idbus.kernel.main.mediation.channel;

import org.atricore.idbus.kernel.main.session.SSOSessionManager;
import org.atricore.idbus.kernel.main.store.SSOIdentityManager;

/**
 *
 * @author <a href="mailto:gbrigand@josso.org">Gianluca Brigandi</a>
 * @version $Id: SPChannel.java 1320 2009-06-22 14:05:35Z sgonzalez $
 */
public interface IdPChannel extends FederationChannel {

    SSOSessionManager getSessionManager();

    SSOIdentityManager getIdentityManager();
}
