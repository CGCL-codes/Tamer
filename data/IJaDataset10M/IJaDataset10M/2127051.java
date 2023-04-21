package org.jquery4jsf.custom.panel;

import java.lang.String;
import org.jquery4jsf.taglib.html.ext.UIComponentTagBase;
import javax.faces.component.UIComponent;

public class PanelExTag extends UIComponentTagBase {

    private String style;

    private String styleClass;

    private String trueVerticalText;

    private String cookie;

    private String accordion;

    private String header;

    private String event;

    private String collapsible;

    private String collapseType;

    private String collapsed;

    private String collapseSpeed;

    private String controls;

    private String widgetClass;

    private String headerClass;

    private String contentClass;

    private String rightboxClass;

    private String controlsClass;

    private String titleClass;

    private String titleTextClass;

    private String iconClass;

    private String hoverClass;

    private String collapsePnlClass;

    private String headerIconClpsd;

    private String headerIcon;

    private String slideRIconClpsd;

    private String slideRIcon;

    private String slideLIconClpsd;

    private String slideLIcon;

    private String onunfold;

    private String onfold;

    public void release() {
        super.release();
        this.style = null;
        this.styleClass = null;
        this.trueVerticalText = null;
        this.cookie = null;
        this.accordion = null;
        this.header = null;
        this.event = null;
        this.collapsible = null;
        this.collapseType = null;
        this.collapsed = null;
        this.collapseSpeed = null;
        this.controls = null;
        this.widgetClass = null;
        this.headerClass = null;
        this.contentClass = null;
        this.rightboxClass = null;
        this.controlsClass = null;
        this.titleClass = null;
        this.titleTextClass = null;
        this.iconClass = null;
        this.hoverClass = null;
        this.collapsePnlClass = null;
        this.headerIconClpsd = null;
        this.headerIcon = null;
        this.slideRIconClpsd = null;
        this.slideRIcon = null;
        this.slideLIconClpsd = null;
        this.slideLIcon = null;
        this.onunfold = null;
        this.onfold = null;
    }

    protected void setProperties(UIComponent comp) {
        super.setProperties(comp);
        org.jquery4jsf.custom.panel.PanelEx component = null;
        try {
            component = (org.jquery4jsf.custom.panel.PanelEx) comp;
        } catch (ClassCastException cce) {
            throw new IllegalStateException("Component " + component.toString() + " not expected type.");
        }
        setStringProperty(getFacesContext(), component, "style", style);
        setStringProperty(getFacesContext(), component, "styleClass", styleClass);
        setBooleanProperty(getFacesContext(), component, "trueVerticalText", trueVerticalText);
        setStringProperty(getFacesContext(), component, "cookie", cookie);
        setStringProperty(getFacesContext(), component, "accordion", accordion);
        setStringProperty(getFacesContext(), component, "header", header);
        setStringProperty(getFacesContext(), component, "event", event);
        setBooleanProperty(getFacesContext(), component, "collapsible", collapsible);
        setStringProperty(getFacesContext(), component, "collapseType", collapseType);
        setBooleanProperty(getFacesContext(), component, "collapsed", collapsed);
        setStringProperty(getFacesContext(), component, "collapseSpeed", collapseSpeed);
        setBooleanProperty(getFacesContext(), component, "controls", controls);
        setStringProperty(getFacesContext(), component, "widgetClass", widgetClass);
        setStringProperty(getFacesContext(), component, "headerClass", headerClass);
        setStringProperty(getFacesContext(), component, "contentClass", contentClass);
        setStringProperty(getFacesContext(), component, "rightboxClass", rightboxClass);
        setStringProperty(getFacesContext(), component, "controlsClass", controlsClass);
        setStringProperty(getFacesContext(), component, "titleClass", titleClass);
        setStringProperty(getFacesContext(), component, "titleTextClass", titleTextClass);
        setStringProperty(getFacesContext(), component, "iconClass", iconClass);
        setStringProperty(getFacesContext(), component, "hoverClass", hoverClass);
        setStringProperty(getFacesContext(), component, "collapsePnlClass", collapsePnlClass);
        setStringProperty(getFacesContext(), component, "headerIconClpsd", headerIconClpsd);
        setStringProperty(getFacesContext(), component, "headerIcon", headerIcon);
        setStringProperty(getFacesContext(), component, "slideRIconClpsd", slideRIconClpsd);
        setStringProperty(getFacesContext(), component, "slideRIcon", slideRIcon);
        setStringProperty(getFacesContext(), component, "slideLIconClpsd", slideLIconClpsd);
        setStringProperty(getFacesContext(), component, "slideLIcon", slideLIcon);
        setStringProperty(getFacesContext(), component, "onunfold", onunfold);
        setStringProperty(getFacesContext(), component, "onfold", onfold);
    }

    public String getComponentType() {
        return PanelEx.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return "org.jquery4jsf.PanelExRenderer";
    }

    public void setStyle(String value) {
        this.style = value;
    }

    public void setStyleClass(String value) {
        this.styleClass = value;
    }

    public void setTrueVerticalText(String value) {
        this.trueVerticalText = value;
    }

    public void setCookie(String value) {
        this.cookie = value;
    }

    public void setAccordion(String value) {
        this.accordion = value;
    }

    public void setHeader(String value) {
        this.header = value;
    }

    public void setEvent(String value) {
        this.event = value;
    }

    public void setCollapsible(String value) {
        this.collapsible = value;
    }

    public void setCollapseType(String value) {
        this.collapseType = value;
    }

    public void setCollapsed(String value) {
        this.collapsed = value;
    }

    public void setCollapseSpeed(String value) {
        this.collapseSpeed = value;
    }

    public void setControls(String value) {
        this.controls = value;
    }

    public void setWidgetClass(String value) {
        this.widgetClass = value;
    }

    public void setHeaderClass(String value) {
        this.headerClass = value;
    }

    public void setContentClass(String value) {
        this.contentClass = value;
    }

    public void setRightboxClass(String value) {
        this.rightboxClass = value;
    }

    public void setControlsClass(String value) {
        this.controlsClass = value;
    }

    public void setTitleClass(String value) {
        this.titleClass = value;
    }

    public void setTitleTextClass(String value) {
        this.titleTextClass = value;
    }

    public void setIconClass(String value) {
        this.iconClass = value;
    }

    public void setHoverClass(String value) {
        this.hoverClass = value;
    }

    public void setCollapsePnlClass(String value) {
        this.collapsePnlClass = value;
    }

    public void setHeaderIconClpsd(String value) {
        this.headerIconClpsd = value;
    }

    public void setHeaderIcon(String value) {
        this.headerIcon = value;
    }

    public void setSlideRIconClpsd(String value) {
        this.slideRIconClpsd = value;
    }

    public void setSlideRIcon(String value) {
        this.slideRIcon = value;
    }

    public void setSlideLIconClpsd(String value) {
        this.slideLIconClpsd = value;
    }

    public void setSlideLIcon(String value) {
        this.slideLIcon = value;
    }

    public void setOnunfold(String value) {
        this.onunfold = value;
    }

    public void setOnfold(String value) {
        this.onfold = value;
    }
}
