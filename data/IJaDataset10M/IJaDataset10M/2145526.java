package com.icesoft.faces.webapp.parser;

import com.icesoft.faces.component.UIXhtmlComponent;
import org.xml.sax.Attributes;
import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentTag;
import javax.faces.el.ValueBinding;
import com.icesoft.util.pooling.ELPool;

/**
 * This class contains the tag processing logic for all XHTML tags.
 */
public class XhtmlTag extends UIComponentTag {

    private String tagName;

    private Attributes attributes;

    /**
     * RendererType getter.
     *
     * @return Renderer class name
     */
    public String getRendererType() {
        return "com.icesoft.faces.Xhtml";
    }

    /**
     * ComponentType getter.
     *
     * @return XHTML component type
     */
    public String getComponentType() {
        return TagToComponentMap.XHTML_COMPONENT_TYPE;
    }

    protected void setProperties(UIComponent comp) {
        super.setProperties(comp);
        UIXhtmlComponent component = (UIXhtmlComponent) comp;
        component.setTag(getTagName());
        Attributes attr = getAttributes();
        if (attr != null) {
            for (int i = 0; i < attr.getLength(); i++) {
                String value = (String) attr.getValue(i);
                if (isValueReference(value)) {
                    ValueBinding vb = getFacesContext().getApplication().createValueBinding(ELPool.get(value));
                    component.addValueBindingAttribute(attr.getQName(i), vb);
                } else {
                    component.addStandardAttribute(attr.getQName(i), value);
                }
            }
        }
    }

    /**
     * TagName setter.
     *
     * @param tag tag name.
     */
    public void setTagName(String tag) {
        tagName = tag;
    }

    /**
     * TagName getter.
     *
     * @return tag name
     */
    public String getTagName() {
        return tagName;
    }

    /**
     * Attributes setter.
     *
     * @param attrib Atrributes.
     */
    public void setAttributes(Attributes attrib) {
        attributes = attrib;
    }

    /**
     * Attributes getter.
     *
     * @return Attributes
     */
    public Attributes getAttributes() {
        return attributes;
    }
}
