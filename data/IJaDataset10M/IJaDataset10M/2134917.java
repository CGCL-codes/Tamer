package jwu2.gui;

import jwu2.core.Addon;

/**
 * The default behaviour for any GUI event listener is to do nothing at all
 * 
 * @author jbk
 */
public abstract class AbstractGUIEventListener implements GUIEventListener {

    public void onInstallAddonEvent(Addon addon) {
    }

    public void onRemoveAddonEvent(Addon addon) {
    }

    public void onBundleAddAddonEvent(Addon addon) {
    }

    public void onBundleRemoveAddonEvent(Addon addon) {
    }
}
