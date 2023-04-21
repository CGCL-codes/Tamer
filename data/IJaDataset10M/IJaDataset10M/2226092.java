package org.jquery4jsf.custom.accordion;

import java.lang.String;
import org.jquery4jsf.taglib.html.ext.UIComponentTagBase;
import javax.faces.component.UIComponent;

public class AccordionPanelTag extends UIComponentTagBase {

    private String styleClass;

    private String style;

    private String cookie;

    private String active;

    private String animated;

    private String autoHeight;

    private String clearStyle;

    private String collapsible;

    private String event;

    private String fillSpace;

    private String iconsHeader;

    private String iconsHeaderSelected;

    private String navigation;

    private String navigationFilter;

    private String onchange;

    private String onchangestart;

    public void release() {
        super.release();
        this.styleClass = null;
        this.style = null;
        this.cookie = null;
        this.active = null;
        this.animated = null;
        this.autoHeight = null;
        this.clearStyle = null;
        this.collapsible = null;
        this.event = null;
        this.fillSpace = null;
        this.iconsHeader = null;
        this.iconsHeaderSelected = null;
        this.navigation = null;
        this.navigationFilter = null;
        this.onchange = null;
        this.onchangestart = null;
    }

    protected void setProperties(UIComponent comp) {
        super.setProperties(comp);
        org.jquery4jsf.custom.accordion.AccordionPanel component = null;
        try {
            component = (org.jquery4jsf.custom.accordion.AccordionPanel) comp;
        } catch (ClassCastException cce) {
            throw new IllegalStateException("Component " + component.toString() + " not expected type.");
        }
        setStringProperty(getFacesContext(), component, "styleClass", styleClass);
        setStringProperty(getFacesContext(), component, "style", style);
        setBooleanProperty(getFacesContext(), component, "cookie", cookie);
        setStringProperty(getFacesContext(), component, "active", active);
        setStringProperty(getFacesContext(), component, "animated", animated);
        setBooleanProperty(getFacesContext(), component, "autoHeight", autoHeight);
        setBooleanProperty(getFacesContext(), component, "clearStyle", clearStyle);
        setBooleanProperty(getFacesContext(), component, "collapsible", collapsible);
        setStringProperty(getFacesContext(), component, "event", event);
        setBooleanProperty(getFacesContext(), component, "fillSpace", fillSpace);
        setStringProperty(getFacesContext(), component, "iconsHeader", iconsHeader);
        setStringProperty(getFacesContext(), component, "iconsHeaderSelected", iconsHeaderSelected);
        setBooleanProperty(getFacesContext(), component, "navigation", navigation);
        setStringProperty(getFacesContext(), component, "navigationFilter", navigationFilter);
        setStringProperty(getFacesContext(), component, "onchange", onchange);
        setStringProperty(getFacesContext(), component, "onchangestart", onchangestart);
    }

    public String getComponentType() {
        return AccordionPanel.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return "org.jquery4jsf.AccordionPanelRenderer";
    }

    public void setStyleClass(String value) {
        this.styleClass = value;
    }

    public void setStyle(String value) {
        this.style = value;
    }

    public void setCookie(String value) {
        this.cookie = value;
    }

    public void setActive(String value) {
        this.active = value;
    }

    public void setAnimated(String value) {
        this.animated = value;
    }

    public void setAutoHeight(String value) {
        this.autoHeight = value;
    }

    public void setClearStyle(String value) {
        this.clearStyle = value;
    }

    public void setCollapsible(String value) {
        this.collapsible = value;
    }

    public void setEvent(String value) {
        this.event = value;
    }

    public void setFillSpace(String value) {
        this.fillSpace = value;
    }

    public void setIconsHeader(String value) {
        this.iconsHeader = value;
    }

    public void setIconsHeaderSelected(String value) {
        this.iconsHeaderSelected = value;
    }

    public void setNavigation(String value) {
        this.navigation = value;
    }

    public void setNavigationFilter(String value) {
        this.navigationFilter = value;
    }

    public void setOnchange(String value) {
        this.onchange = value;
    }

    public void setOnchangestart(String value) {
        this.onchangestart = value;
    }
}
