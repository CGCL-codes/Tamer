package org.atricore.idbus.kernel.main.mediation.binding;

import org.atricore.idbus.kernel.main.mediation.Channel;
import org.atricore.idbus.kernel.main.mediation.provider.FederatedLocalProvider;

/**
 *
 * @author <a href="mailto:gbrigand@josso.org">Gianluca Brigandi</a>
 * @version $Id: BindingChannel.java 1331 2009-06-23 22:04:18Z sgonzalez $
 */
public interface BindingChannel extends Channel {

    FederatedLocalProvider getProvider();
}
