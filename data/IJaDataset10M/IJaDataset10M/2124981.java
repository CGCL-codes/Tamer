package org.mobicents.slee.container.management.console.client.components;

import org.mobicents.slee.container.management.console.client.ServerCallback;
import org.mobicents.slee.container.management.console.client.ServerConnection;
import org.mobicents.slee.container.management.console.client.common.BrowseContainer;
import org.mobicents.slee.container.management.console.client.common.ControlContainer;
import org.mobicents.slee.container.management.console.client.common.ListPanel;
import org.mobicents.slee.container.management.console.client.components.info.ComponentInfo;
import org.mobicents.slee.container.management.console.client.components.info.ComponentTypeInfo;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;

/**
 * @author Stefano Zappaterra
 * 
 */
public class ComponentTypeListPanel extends Composite {

    private ComponentsServiceAsync service = ServerConnection.componentsService;

    private BrowseContainer browseContainer;

    private ControlContainer rootPanel = new ControlContainer();

    private ComponentTypeInfo[] componentTypeInfos;

    public ComponentTypeListPanel(BrowseContainer browseContainer, ComponentTypeInfo[] componentTypeInfos) {
        super();
        this.browseContainer = browseContainer;
        this.componentTypeInfos = componentTypeInfos;
        initWidget(rootPanel);
        setData();
    }

    @SuppressWarnings("deprecation")
    private void setData() {
        ListPanel listPanel = new ListPanel();
        listPanel.setHeader(1, "Component Type");
        listPanel.setHeader(2, "Number");
        listPanel.setColumnWidth(1, "100%");
        for (int i = 0; i < componentTypeInfos.length; i++) {
            final ComponentTypeInfo componentTypeInfo = componentTypeInfos[i];
            listPanel.setCell(i, 0, new Image("images/components." + componentTypeInfo.getType().toLowerCase() + ".gif"));
            Hyperlink componentTypeLink = new Hyperlink(componentTypeInfo.getType(), componentTypeInfo.getType());
            componentTypeLink.addClickHandler(new ClickHandler() {

                public void onClick(ClickEvent event) {
                    onComponentType(componentTypeInfo);
                }
            });
            listPanel.setCell(i, 1, componentTypeLink);
            listPanel.setCellText(i, 2, "" + componentTypeInfo.getComponentNumber());
        }
        rootPanel.setWidget(0, 0, listPanel);
    }

    private void onComponentType(final ComponentTypeInfo componentTypeInfo) {
        ServerCallback callback = new ServerCallback(this) {

            public void onSuccess(Object result) {
                ComponentInfo[] componentInfos = (ComponentInfo[]) result;
                ComponentListPanel componentListPanel = new ComponentListPanel(browseContainer, componentInfos);
                browseContainer.add((componentTypeInfo.getType() + "s").replaceFirst("ys$", "ies"), componentListPanel);
            }
        };
        service.getComponentInfos(componentTypeInfo, callback);
    }
}
