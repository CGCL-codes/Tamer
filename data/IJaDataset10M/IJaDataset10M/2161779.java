package org.gudy.azureus2.plugins.ui;

import org.gudy.azureus2.plugins.PluginInterface;

public interface UIInstanceFactory {

    /**
		 * Some UI instances need to understand which plugin they are associated with. This method
		 * gives the opportunity to customise the UIInstance returned to a plugin so that operations
		 * on it can take the appropriate actions
		 */
    public UIInstance getInstance(PluginInterface plugin_interface);

    /**
		 * This method will be called by the UI manager when detaching the UI to permit the action to be
		 * vetoed/any detach logic to occur. It should not be directly called by the plugin code 
		 */
    public void detach() throws UIException;
}
