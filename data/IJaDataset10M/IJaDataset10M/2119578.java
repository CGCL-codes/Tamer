package org.orbeon.faces.components.demo.taglib;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.orbeon.faces.components.demo.components.PaneComponent;
import javax.faces.component.UIComponent;
import javax.faces.webapp.FacesTag;

/**
 * This class creates a <code>PaneComponent</code> instance
 * that represents a tab button control on the tab pane.
 */
public class PaneTabLabelTag extends FacesTag {

    private static Log log = LogFactory.getLog(PaneTabLabelTag.class);

    protected String label = null;

    protected String image = null;

    protected String commandName = null;

    public String getLabel() {
        return label;
    }

    public void setLabel(String newLabel) {
        label = newLabel;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String newImage) {
        image = newImage;
    }

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String newCommandName) {
        commandName = newCommandName;
    }

    public String getComponentType() {
        return ("Pane");
    }

    public String getRendererType() {
        return ("TabLabel");
    }

    public void release() {
        super.release();
        this.label = null;
        this.image = null;
        this.commandName = null;
    }

    protected void overrideProperties(UIComponent component) {
        super.overrideProperties(component);
        PaneComponent pane = (PaneComponent) component;
        if (null == pane.getAttribute("label")) {
            pane.setAttribute("label", getLabel());
        }
        if (null == pane.getAttribute("image")) {
            pane.setAttribute("image", getImage());
        }
        if (null == pane.getAttribute("commandName")) {
            pane.setAttribute("commandName", getCommandName());
        }
    }
}
