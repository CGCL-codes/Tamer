package org.apptools;

import org.apptools.plugin.ActivePlugIn;

/**An interface to be implemented by classes that wish to be notified 
 * of the life cycle events for ActivePlugIns. 
 * @author Johan Stenberg
 */
public interface ActivePlugInListener {

    /**Notified when a new plugin is opened*/
    public void plugInActivated(Platform p, ActivePlugIn pi);

    /**Notified when a plugin is about to be closed*/
    public void plugInDeactiviating(Platform p, ActivePlugIn pi);
}
