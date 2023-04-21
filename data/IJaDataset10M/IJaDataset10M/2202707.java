package javax.faces.component.html;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.convert.Converter;
import javax.faces.el.MethodBinding;

public class HtmlInputSecret extends javax.faces.component.UIInput implements javax.faces.component.behavior.ClientBehaviorHolder {

    public static final String COMPONENT_FAMILY = "javax.faces.Input";

    public static final String COMPONENT_TYPE = "javax.faces.HtmlInputSecret";

    public HtmlInputSecret() {
        setRendererType("javax.faces.Secret");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    private static final java.util.Collection<String> CLIENT_EVENTS_LIST = java.util.Collections.unmodifiableCollection(java.util.Arrays.asList("blur", "focus", "change", "select", "click", "dblclick", "keydown", "keypress", "keyup", "mousedown", "mousemove", "mouseout", "mouseover", "mouseup", "valueChange"));

    public java.util.Collection<String> getEventNames() {
        return CLIENT_EVENTS_LIST;
    }

    public String getDefaultEventName() {
        return "valueChange";
    }

    public int getMaxlength() {
        return (Integer) getStateHelper().eval(PropertyKeys.maxlength, Integer.MIN_VALUE);
    }

    public void setMaxlength(int maxlength) {
        getStateHelper().put(PropertyKeys.maxlength, maxlength);
    }

    public boolean isRedisplay() {
        return (Boolean) getStateHelper().eval(PropertyKeys.redisplay, false);
    }

    public void setRedisplay(boolean redisplay) {
        getStateHelper().put(PropertyKeys.redisplay, redisplay);
    }

    public int getSize() {
        return (Integer) getStateHelper().eval(PropertyKeys.size, Integer.MIN_VALUE);
    }

    public void setSize(int size) {
        getStateHelper().put(PropertyKeys.size, size);
    }

    public String getAutocomplete() {
        return (String) getStateHelper().eval(PropertyKeys.autocomplete);
    }

    public void setAutocomplete(String autocomplete) {
        getStateHelper().put(PropertyKeys.autocomplete, autocomplete);
    }

    public String getLabel() {
        return (String) getStateHelper().eval(PropertyKeys.label);
    }

    public void setLabel(String label) {
        getStateHelper().put(PropertyKeys.label, label);
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

    public String getAlt() {
        return (String) getStateHelper().eval(PropertyKeys.alt);
    }

    public void setAlt(String alt) {
        getStateHelper().put(PropertyKeys.alt, alt);
    }

    public String getTabindex() {
        return (String) getStateHelper().eval(PropertyKeys.tabindex);
    }

    public void setTabindex(String tabindex) {
        getStateHelper().put(PropertyKeys.tabindex, tabindex);
    }

    public String getOnblur() {
        return (String) getStateHelper().eval(PropertyKeys.onblur);
    }

    public void setOnblur(String onblur) {
        getStateHelper().put(PropertyKeys.onblur, onblur);
    }

    public String getOnfocus() {
        return (String) getStateHelper().eval(PropertyKeys.onfocus);
    }

    public void setOnfocus(String onfocus) {
        getStateHelper().put(PropertyKeys.onfocus, onfocus);
    }

    public String getAccesskey() {
        return (String) getStateHelper().eval(PropertyKeys.accesskey);
    }

    public void setAccesskey(String accesskey) {
        getStateHelper().put(PropertyKeys.accesskey, accesskey);
    }

    public String getOnchange() {
        return (String) getStateHelper().eval(PropertyKeys.onchange);
    }

    public void setOnchange(String onchange) {
        getStateHelper().put(PropertyKeys.onchange, onchange);
    }

    public String getOnselect() {
        return (String) getStateHelper().eval(PropertyKeys.onselect);
    }

    public void setOnselect(String onselect) {
        getStateHelper().put(PropertyKeys.onselect, onselect);
    }

    public String getOnclick() {
        return (String) getStateHelper().eval(PropertyKeys.onclick);
    }

    public void setOnclick(String onclick) {
        getStateHelper().put(PropertyKeys.onclick, onclick);
    }

    public String getOndblclick() {
        return (String) getStateHelper().eval(PropertyKeys.ondblclick);
    }

    public void setOndblclick(String ondblclick) {
        getStateHelper().put(PropertyKeys.ondblclick, ondblclick);
    }

    public String getOnkeydown() {
        return (String) getStateHelper().eval(PropertyKeys.onkeydown);
    }

    public void setOnkeydown(String onkeydown) {
        getStateHelper().put(PropertyKeys.onkeydown, onkeydown);
    }

    public String getOnkeypress() {
        return (String) getStateHelper().eval(PropertyKeys.onkeypress);
    }

    public void setOnkeypress(String onkeypress) {
        getStateHelper().put(PropertyKeys.onkeypress, onkeypress);
    }

    public String getOnkeyup() {
        return (String) getStateHelper().eval(PropertyKeys.onkeyup);
    }

    public void setOnkeyup(String onkeyup) {
        getStateHelper().put(PropertyKeys.onkeyup, onkeyup);
    }

    public String getOnmousedown() {
        return (String) getStateHelper().eval(PropertyKeys.onmousedown);
    }

    public void setOnmousedown(String onmousedown) {
        getStateHelper().put(PropertyKeys.onmousedown, onmousedown);
    }

    public String getOnmousemove() {
        return (String) getStateHelper().eval(PropertyKeys.onmousemove);
    }

    public void setOnmousemove(String onmousemove) {
        getStateHelper().put(PropertyKeys.onmousemove, onmousemove);
    }

    public String getOnmouseout() {
        return (String) getStateHelper().eval(PropertyKeys.onmouseout);
    }

    public void setOnmouseout(String onmouseout) {
        getStateHelper().put(PropertyKeys.onmouseout, onmouseout);
    }

    public String getOnmouseover() {
        return (String) getStateHelper().eval(PropertyKeys.onmouseover);
    }

    public void setOnmouseover(String onmouseover) {
        getStateHelper().put(PropertyKeys.onmouseover, onmouseover);
    }

    public String getOnmouseup() {
        return (String) getStateHelper().eval(PropertyKeys.onmouseup);
    }

    public void setOnmouseup(String onmouseup) {
        getStateHelper().put(PropertyKeys.onmouseup, onmouseup);
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

    public boolean isDisabled() {
        return (Boolean) getStateHelper().eval(PropertyKeys.disabled, false);
    }

    public void setDisabled(boolean disabled) {
        getStateHelper().put(PropertyKeys.disabled, disabled);
    }

    public boolean isReadonly() {
        return (Boolean) getStateHelper().eval(PropertyKeys.readonly, false);
    }

    public void setReadonly(boolean readonly) {
        getStateHelper().put(PropertyKeys.readonly, readonly);
    }

    protected enum PropertyKeys {

        maxlength, redisplay, size, autocomplete, label, style, styleClass, alt, tabindex, onblur, onfocus, accesskey, onchange, onselect, onclick, ondblclick, onkeydown, onkeypress, onkeyup, onmousedown, onmousemove, onmouseout, onmouseover, onmouseup, dir, lang, title, disabled, readonly
    }
}
