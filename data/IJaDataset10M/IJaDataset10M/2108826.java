package org.hip.vif.groups;

import org.hip.vif.interfaces.IMessages;
import org.hip.vif.registry.AbstractMessages;

/**
 * Looks up localized messages in this bundle.
 *
 * @author Luthiger
 * Created: 18.02.2008
 */
public class Messages extends AbstractMessages implements IMessages {

    public Messages() {
        super("messages");
    }

    @Override
    protected ClassLoader getLoader() {
        return getClass().getClassLoader();
    }
}
