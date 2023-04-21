package org.openremote.web.console.domain;

import org.w3c.dom.Node;

/**
 * The super class of component, which include label, image and control component.
 * It contains component id and size.
 * 
 */
@SuppressWarnings("serial")
public class Component extends BusinessEntity {

    private int componentId;

    private int frameWidth;

    private int frameHeight;

    /**
    * Builds the component by parse component node.
    * 
    * @param node the node
    * 
    * @return the component
    */
    public static Component buildWithXML(Node node, PanelXmlEntity panelXmlEntity) {
        Component component = null;
        if (LABEL.equals(node.getNodeName())) {
            component = new Label(node);
            panelXmlEntity.getTmpLabels().put(component.getComponentId(), (Label) component);
        } else if (IMAGE.equals(node.getNodeName())) {
            component = new Image(node);
            panelXmlEntity.getTmpImages().add((Image) component);
        } else {
            return Control.buildWithXML(node);
        }
        return component;
    }

    public int getComponentId() {
        return componentId;
    }

    public void setComponentId(int componentId) {
        this.componentId = componentId;
    }

    public int getFrameWidth() {
        return frameWidth;
    }

    public void setFrameWidth(int frameWidth) {
        this.frameWidth = frameWidth;
    }

    public int getFrameHeight() {
        return frameHeight;
    }

    public void setFrameHeight(int frameHeight) {
        this.frameHeight = frameHeight;
    }
}
