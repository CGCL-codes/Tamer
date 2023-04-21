package javax.faces.component.html;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;

public class HtmlMessages extends javax.faces.component.UIMessages {

    public static final String COMPONENT_FAMILY = "javax.faces.Messages";

    public static final String COMPONENT_TYPE = "javax.faces.HtmlMessages";

    public HtmlMessages() {
        setRendererType("javax.faces.Messages");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getLayout() {
        return (String) getStateHelper().eval(PropertyKeys.layout, "list");
    }

    public void setLayout(String layout) {
        getStateHelper().put(PropertyKeys.layout, layout);
    }

    public String getStyle() {
        return (String) getStateHelper().eval(PropertyKeys.style);
    }

    public void setStyle(String style) {
        getStateHelper().put(PropertyKeys.style, style);
    }

    public String getStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.styleClass);
    }

    public void setStyleClass(String styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, styleClass);
    }

    public String getErrorClass() {
        return (String) getStateHelper().eval(PropertyKeys.errorClass);
    }

    public void setErrorClass(String errorClass) {
        getStateHelper().put(PropertyKeys.errorClass, errorClass);
    }

    public String getErrorStyle() {
        return (String) getStateHelper().eval(PropertyKeys.errorStyle);
    }

    public void setErrorStyle(String errorStyle) {
        getStateHelper().put(PropertyKeys.errorStyle, errorStyle);
    }

    public String getFatalClass() {
        return (String) getStateHelper().eval(PropertyKeys.fatalClass);
    }

    public void setFatalClass(String fatalClass) {
        getStateHelper().put(PropertyKeys.fatalClass, fatalClass);
    }

    public String getFatalStyle() {
        return (String) getStateHelper().eval(PropertyKeys.fatalStyle);
    }

    public void setFatalStyle(String fatalStyle) {
        getStateHelper().put(PropertyKeys.fatalStyle, fatalStyle);
    }

    public String getInfoClass() {
        return (String) getStateHelper().eval(PropertyKeys.infoClass);
    }

    public void setInfoClass(String infoClass) {
        getStateHelper().put(PropertyKeys.infoClass, infoClass);
    }

    public String getInfoStyle() {
        return (String) getStateHelper().eval(PropertyKeys.infoStyle);
    }

    public void setInfoStyle(String infoStyle) {
        getStateHelper().put(PropertyKeys.infoStyle, infoStyle);
    }

    public boolean isTooltip() {
        return (Boolean) getStateHelper().eval(PropertyKeys.tooltip, false);
    }

    public void setTooltip(boolean tooltip) {
        getStateHelper().put(PropertyKeys.tooltip, tooltip);
    }

    public String getWarnClass() {
        return (String) getStateHelper().eval(PropertyKeys.warnClass);
    }

    public void setWarnClass(String warnClass) {
        getStateHelper().put(PropertyKeys.warnClass, warnClass);
    }

    public String getWarnStyle() {
        return (String) getStateHelper().eval(PropertyKeys.warnStyle);
    }

    public void setWarnStyle(String warnStyle) {
        getStateHelper().put(PropertyKeys.warnStyle, warnStyle);
    }

    public String getDir() {
        return (String) getStateHelper().eval(PropertyKeys.dir);
    }

    public void setDir(String dir) {
        getStateHelper().put(PropertyKeys.dir, dir);
    }

    public String getLang() {
        return (String) getStateHelper().eval(PropertyKeys.lang);
    }

    public void setLang(String lang) {
        getStateHelper().put(PropertyKeys.lang, lang);
    }

    public String getTitle() {
        return (String) getStateHelper().eval(PropertyKeys.title);
    }

    public void setTitle(String title) {
        getStateHelper().put(PropertyKeys.title, title);
    }

    protected enum PropertyKeys {

        layout, style, styleClass, errorClass, errorStyle, fatalClass, fatalStyle, infoClass, infoStyle, tooltip, warnClass, warnStyle, dir, lang, title
    }
}
