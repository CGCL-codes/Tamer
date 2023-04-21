package org.eclipse.genforms.editor.model;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.jdom.Element;

public class EditElement extends org.eclipse.genforms.editor.model.Formelement {

    public IPropertyDescriptor[] getPropertyDescriptors() {
        if (propertyDescriptors == null) {
            propertyDescriptors = new IPropertyDescriptor[] { new TextPropertyDescriptor("name", "Name") };
        }
        return propertyDescriptors;
    }

    public Object getPropertyValue(Object id) {
        if ("name".equals(id)) {
            return getName();
        }
        return null;
    }

    public void setPropertyValue(Object id, Object value) {
        if ("name".equals(id)) {
            setName((String) value);
        }
    }

    @Override
    public Element getXMLElement() {
        Element element = new Element("text");
        return element;
    }
}
