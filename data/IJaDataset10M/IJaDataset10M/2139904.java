package org.freebxml.omar.client.ui.web.client.browser;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.gwtext.client.core.Ext;
import com.gwtext.client.core.UpdateManager;
import com.gwtext.client.widgets.layout.ContentPanel;
import com.gwtext.client.widgets.layout.LayoutRegionConfig;

/**
 * A {@link ContextAction} implementation to show the repository item linked to
 * an extrinsic objects.
 * <p>
 * Comment: This class will probably disappear once we have support for editing registry items.
 * 
 * @author Andreas Veithen
 */
public class ViewRepositoryItemAction extends ContextAction {

    public ViewRepositoryItemAction() {
        super("View repository item");
    }

    public boolean appliesTo(RegistryObjectHandle registryObjectHandle) {
        return registryObjectHandle.hasFlag(RegistryObjectHandle.IS_EXTRINSIC_OBJECT);
    }

    public void execute(Browser browser, RegistryObjectHandle registryObjectHandle) {
        ContentPanel repositoryItemPanel = new ContentPanel(Ext.generateId());
        browser.addPanel(LayoutRegionConfig.CENTER, repositoryItemPanel);
        UpdateManager updateManager = repositoryItemPanel.getUpdateManager();
        updateManager.setDefaultUrl(GWT.getModuleBaseURL() + "rpc/getRepositoryItem?id=" + URL.encodeComponent(registryObjectHandle.getId()));
        updateManager.setLoadScripts(true);
        updateManager.refresh();
    }
}
