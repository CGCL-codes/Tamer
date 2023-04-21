package org.jquery4jsf.custom.overflow;

import java.lang.String;
import org.jquery4jsf.taglib.html.ext.UIComponentTagBase;
import javax.faces.component.UIComponent;

public class OverflowTag extends UIComponentTagBase {

    private String _for;

    private String eventStart;

    private String eventStop;

    private String message;

    private String cssPadding;

    private String cssMargin;

    private String cssWidth;

    private String cssTop;

    private String cssLeft;

    private String cssTextAlign;

    private String cssColor;

    private String cssBorder;

    private String cssBackgroundColor;

    private String cssCursor;

    private String overlayCSSBackgroundColor;

    private String overlayCSSOpacity;

    private String iframeSrc;

    private String forceIframe;

    private String baseZ;

    private String centerX;

    private String centerY;

    private String allowBodyStretch;

    private String bindEvents;

    private String constrainTabKey;

    private String fadeIn;

    private String fadeOut;

    private String timeout;

    private String showOverlay;

    private String focusInput;

    private String applyPlatformOpacityRules;

    private String onUnblock;

    public void release() {
        super.release();
        this._for = null;
        this.eventStart = null;
        this.eventStop = null;
        this.message = null;
        this.cssPadding = null;
        this.cssMargin = null;
        this.cssWidth = null;
        this.cssTop = null;
        this.cssLeft = null;
        this.cssTextAlign = null;
        this.cssColor = null;
        this.cssBorder = null;
        this.cssBackgroundColor = null;
        this.cssCursor = null;
        this.overlayCSSBackgroundColor = null;
        this.overlayCSSOpacity = null;
        this.iframeSrc = null;
        this.forceIframe = null;
        this.baseZ = null;
        this.centerX = null;
        this.centerY = null;
        this.allowBodyStretch = null;
        this.bindEvents = null;
        this.constrainTabKey = null;
        this.fadeIn = null;
        this.fadeOut = null;
        this.timeout = null;
        this.showOverlay = null;
        this.focusInput = null;
        this.applyPlatformOpacityRules = null;
        this.onUnblock = null;
    }

    protected void setProperties(UIComponent comp) {
        super.setProperties(comp);
        org.jquery4jsf.custom.overflow.Overflow component = null;
        try {
            component = (org.jquery4jsf.custom.overflow.Overflow) comp;
        } catch (ClassCastException cce) {
            throw new IllegalStateException("Component " + component.toString() + " not expected type.");
        }
        setStringProperty(getFacesContext(), component, "for", _for);
        setStringProperty(getFacesContext(), component, "eventStart", eventStart);
        setStringProperty(getFacesContext(), component, "eventStop", eventStop);
        setStringProperty(getFacesContext(), component, "message", message);
        setStringProperty(getFacesContext(), component, "cssPadding", cssPadding);
        setStringProperty(getFacesContext(), component, "cssMargin", cssMargin);
        setStringProperty(getFacesContext(), component, "cssWidth", cssWidth);
        setStringProperty(getFacesContext(), component, "cssTop", cssTop);
        setStringProperty(getFacesContext(), component, "cssLeft", cssLeft);
        setStringProperty(getFacesContext(), component, "cssTextAlign", cssTextAlign);
        setStringProperty(getFacesContext(), component, "cssColor", cssColor);
        setStringProperty(getFacesContext(), component, "cssBorder", cssBorder);
        setStringProperty(getFacesContext(), component, "cssBackgroundColor", cssBackgroundColor);
        setStringProperty(getFacesContext(), component, "cssCursor", cssCursor);
        setStringProperty(getFacesContext(), component, "overlayCSSBackgroundColor", overlayCSSBackgroundColor);
        setStringProperty(getFacesContext(), component, "overlayCSSOpacity", overlayCSSOpacity);
        setStringProperty(getFacesContext(), component, "iframeSrc", iframeSrc);
        setBooleanProperty(getFacesContext(), component, "forceIframe", forceIframe);
        setIntegerProperty(getFacesContext(), component, "baseZ", baseZ);
        setBooleanProperty(getFacesContext(), component, "centerX", centerX);
        setBooleanProperty(getFacesContext(), component, "centerY", centerY);
        setBooleanProperty(getFacesContext(), component, "allowBodyStretch", allowBodyStretch);
        setBooleanProperty(getFacesContext(), component, "bindEvents", bindEvents);
        setBooleanProperty(getFacesContext(), component, "constrainTabKey", constrainTabKey);
        setIntegerProperty(getFacesContext(), component, "fadeIn", fadeIn);
        setIntegerProperty(getFacesContext(), component, "fadeOut", fadeOut);
        setIntegerProperty(getFacesContext(), component, "timeout", timeout);
        setBooleanProperty(getFacesContext(), component, "showOverlay", showOverlay);
        setBooleanProperty(getFacesContext(), component, "focusInput", focusInput);
        setBooleanProperty(getFacesContext(), component, "applyPlatformOpacityRules", applyPlatformOpacityRules);
        setStringProperty(getFacesContext(), component, "onUnblock", onUnblock);
    }

    public String getComponentType() {
        return Overflow.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return "org.jquery4jsf.OverflowRenderer";
    }

    public void setFor(String value) {
        this._for = value;
    }

    public void setEventStart(String value) {
        this.eventStart = value;
    }

    public void setEventStop(String value) {
        this.eventStop = value;
    }

    public void setMessage(String value) {
        this.message = value;
    }

    public void setCssPadding(String value) {
        this.cssPadding = value;
    }

    public void setCssMargin(String value) {
        this.cssMargin = value;
    }

    public void setCssWidth(String value) {
        this.cssWidth = value;
    }

    public void setCssTop(String value) {
        this.cssTop = value;
    }

    public void setCssLeft(String value) {
        this.cssLeft = value;
    }

    public void setCssTextAlign(String value) {
        this.cssTextAlign = value;
    }

    public void setCssColor(String value) {
        this.cssColor = value;
    }

    public void setCssBorder(String value) {
        this.cssBorder = value;
    }

    public void setCssBackgroundColor(String value) {
        this.cssBackgroundColor = value;
    }

    public void setCssCursor(String value) {
        this.cssCursor = value;
    }

    public void setOverlayCSSBackgroundColor(String value) {
        this.overlayCSSBackgroundColor = value;
    }

    public void setOverlayCSSOpacity(String value) {
        this.overlayCSSOpacity = value;
    }

    public void setIframeSrc(String value) {
        this.iframeSrc = value;
    }

    public void setForceIframe(String value) {
        this.forceIframe = value;
    }

    public void setBaseZ(String value) {
        this.baseZ = value;
    }

    public void setCenterX(String value) {
        this.centerX = value;
    }

    public void setCenterY(String value) {
        this.centerY = value;
    }

    public void setAllowBodyStretch(String value) {
        this.allowBodyStretch = value;
    }

    public void setBindEvents(String value) {
        this.bindEvents = value;
    }

    public void setConstrainTabKey(String value) {
        this.constrainTabKey = value;
    }

    public void setFadeIn(String value) {
        this.fadeIn = value;
    }

    public void setFadeOut(String value) {
        this.fadeOut = value;
    }

    public void setTimeout(String value) {
        this.timeout = value;
    }

    public void setShowOverlay(String value) {
        this.showOverlay = value;
    }

    public void setFocusInput(String value) {
        this.focusInput = value;
    }

    public void setApplyPlatformOpacityRules(String value) {
        this.applyPlatformOpacityRules = value;
    }

    public void setOnUnblock(String value) {
        this.onUnblock = value;
    }
}
