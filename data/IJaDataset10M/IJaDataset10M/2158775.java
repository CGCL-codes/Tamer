package org.jquery4jsf.custom.paragraph;

import java.lang.String;
import org.jquery4jsf.taglib.html.ext.UIComponentTagBase;
import javax.faces.component.UIComponent;

public class ParagraphTag extends UIComponentTagBase {

    private String value;

    private String converter;

    private String dir;

    private String lang;

    private String title;

    private String xmlLang;

    private String style;

    private String styleClass;

    public void release() {
        super.release();
        this.value = null;
        this.converter = null;
        this.dir = null;
        this.lang = null;
        this.title = null;
        this.xmlLang = null;
        this.style = null;
        this.styleClass = null;
    }

    protected void setProperties(UIComponent comp) {
        super.setProperties(comp);
        org.jquery4jsf.custom.paragraph.Paragraph component = null;
        try {
            component = (org.jquery4jsf.custom.paragraph.Paragraph) comp;
        } catch (ClassCastException cce) {
            throw new IllegalStateException("Component " + component.toString() + " not expected type.");
        }
        setValueProperty(getFacesContext(), component, "value", value);
        setConverterProperty(getFacesContext(), component, "converter", converter);
        setStringProperty(getFacesContext(), component, "dir", dir);
        setStringProperty(getFacesContext(), component, "lang", lang);
        setStringProperty(getFacesContext(), component, "title", title);
        setStringProperty(getFacesContext(), component, "xmlLang", xmlLang);
        setStringProperty(getFacesContext(), component, "style", style);
        setStringProperty(getFacesContext(), component, "styleClass", styleClass);
    }

    public String getComponentType() {
        return Paragraph.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return "org.jquery4jsf.ParagraphRenderer";
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setConverter(String value) {
        this.converter = value;
    }

    public void setDir(String value) {
        this.dir = value;
    }

    public void setLang(String value) {
        this.lang = value;
    }

    public void setTitle(String value) {
        this.title = value;
    }

    public void setXmlLang(String value) {
        this.xmlLang = value;
    }

    public void setStyle(String value) {
        this.style = value;
    }

    public void setStyleClass(String value) {
        this.styleClass = value;
    }
}
