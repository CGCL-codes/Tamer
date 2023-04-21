package org.parosproxy.paros.core.scanner;

/**
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public abstract class AbstractHostPlugin extends AbstractPlugin {

    public void notifyPluginCompleted(HostProcess parent) {
        parent.pluginCompleted(this);
    }
}
