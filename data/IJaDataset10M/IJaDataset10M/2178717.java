package org.hip.vif.search.internal;

import org.hip.vif.interfaces.IMessages;
import org.hip.vif.interfaces.IPartletElement;
import org.hip.vif.search.Activator;
import org.hip.vif.search.Constants;

/**
 * This class provides the service <code>org.hip.vif.interfaces.IPartletElement</code>.
 * It is part of the application of OSGi Declarative Services.
 * When the bundle becomes available, this partlet is registered to the application using this class.
 *
 * @author Luthiger
 * Created: 15.03.2009
 */
public class Partlet implements IPartletElement {

    public IMessages getMessages() {
        return Activator.getMessages();
    }

    public String getNamespaceID() {
        return Activator.getBundleName();
    }

    public String[] getPluggableExtensionIDs() {
        return new String[] { Constants.PLUGIN_ID_PARTLET };
    }

    public boolean isAdmin() {
        return false;
    }
}
