package it.unical.inf.wsportal.client.view;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import it.unical.inf.wsportal.client.util.ComponentID;

/**
 *
 * @author Simone Spaccarotella {spa.simone@gmail.com}, Carmine Dodaro {carminedodaro@gmail.com}
 */
public class WsPortalViewport extends Viewport {

    private BorderLayout layout;

    private BorderLayoutData northConstraint;

    private BorderLayoutData westConstraint;

    private BorderLayoutData centerConstraint;

    private NorthSideContainer barPanel;

    private LeftSideContainer servicePanelContainer;

    private CenterSideContainer serviceInteractionContainer;

    /**
     * Creates a new custom viewport. It will be the
     * content panel of the portal.
     */
    public WsPortalViewport() {
        super();
        setID(ComponentID.WS_PORTAL_VIEWPORT);
        setLayout();
        setLayoutConstraints();
        setComponents();
    }

    /**
     * Sets the id and the item id.
     *
     * @param id a string which represent the component univocally.
     */
    private void setID(String id) {
        setId(id);
        setItemId(id);
    }

    /**
     * Sets the border layout.
     */
    private void setLayout() {
        layout = new BorderLayout();
        setLayout(layout);
    }

    /**
     * Set the constraints for each side of the
     * border layout.
     */
    private void setLayoutConstraints() {
        northConstraint = new BorderLayoutData(LayoutRegion.NORTH, 0.05F);
        westConstraint = new BorderLayoutData(LayoutRegion.WEST, 0.2F, 180, 600);
        westConstraint.setCollapsible(true);
        westConstraint.setSplit(true);
        centerConstraint = new BorderLayoutData(LayoutRegion.CENTER);
    }

    /**
     * Sets the components of this container.
     */
    private void setComponents() {
        barPanel = new NorthSideContainer();
        add(barPanel, northConstraint);
        servicePanelContainer = new LeftSideContainer();
        add(servicePanelContainer, westConstraint);
        serviceInteractionContainer = new CenterSideContainer();
        add(serviceInteractionContainer, centerConstraint);
    }
}
