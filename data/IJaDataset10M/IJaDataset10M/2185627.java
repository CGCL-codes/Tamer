package org.mobicents.slee.container.management.console.client.services;

import org.mobicents.slee.container.management.console.client.ServerCallback;
import org.mobicents.slee.container.management.console.client.ServerConnection;
import org.mobicents.slee.container.management.console.client.common.BrowseContainer;
import org.mobicents.slee.container.management.console.client.common.ComponentPropertiesPanel;
import org.mobicents.slee.container.management.console.client.common.ControlContainer;
import org.mobicents.slee.container.management.console.client.common.PropertiesPanel;
import org.mobicents.slee.container.management.console.client.components.ComponentNameLabel;
import org.mobicents.slee.container.management.console.client.components.ComponentsServiceAsync;
import org.mobicents.slee.container.management.console.client.components.info.SbbInfo;
import com.google.gwt.user.client.ui.Composite;

/**
 * @author Stefano Zappaterra
 * 
 */
public class SbbPanel extends Composite {

    private static final int ROW_COMPONENT_INFO = 0;

    private static final int ROW_SBB_INFO = 1;

    private ControlContainer rootPanel = new ControlContainer();

    private String sbbID;

    private BrowseContainer browseContainer;

    public SbbPanel(BrowseContainer browseContainer, String sbbID) {
        super();
        this.browseContainer = browseContainer;
        this.sbbID = sbbID;
        initWidget(rootPanel);
        refreshData();
    }

    private void refreshData() {
        ComponentsServiceAsync componentsService = ServerConnection.componentsService;
        ServerCallback callback = new ServerCallback(this) {

            public void onSuccess(Object result) {
                SbbInfo serviceInfo = (SbbInfo) result;
                refreshSbbPropertiesPanel(serviceInfo);
            }
        };
        componentsService.getComponentInfo(sbbID, callback);
    }

    private void refreshSbbPropertiesPanel(SbbInfo sbbInfo) {
        ComponentPropertiesPanel componentPropertiesPanel = new ComponentPropertiesPanel(browseContainer, sbbInfo);
        rootPanel.setWidget(ROW_COMPONENT_INFO, 0, componentPropertiesPanel);
        PropertiesPanel sbbPropertiesPanel = new PropertiesPanel();
        sbbPropertiesPanel.add("Event types", ComponentNameLabel.toArray(sbbInfo.getEventTypeIDs(), browseContainer));
        sbbPropertiesPanel.add("RA entity links", sbbInfo.getResourceAdaptorEntityLinks());
        sbbPropertiesPanel.add("Resource adaptors", ComponentNameLabel.toArray(sbbInfo.getResourceAdaptorTypeIDs()));
        sbbPropertiesPanel.add("SBBs", ComponentNameLabel.toArray(sbbInfo.getSbbIDs()));
        sbbPropertiesPanel.add("Address profile", new ComponentNameLabel(sbbInfo.getAddressProfileSpecificationID(), browseContainer));
        sbbPropertiesPanel.add("Profile specifications", ComponentNameLabel.toArray(sbbInfo.getProfileSpecificationIDs()));
        rootPanel.setWidget(ROW_SBB_INFO, 0, sbbPropertiesPanel);
    }
}
