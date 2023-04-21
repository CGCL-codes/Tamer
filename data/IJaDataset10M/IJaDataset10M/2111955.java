package de.sic_consult.forms.editor.swing;

import java.awt.Component;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditorSupport;
import javax.swing.JLabel;

public class DefaultPropertyEditor<T> extends PropertyEditorSupport {

    private JLabel label = new JLabel("Property nicht editierbar");

    private PropertyDescriptor propertie;

    public DefaultPropertyEditor() {
    }

    public PropertyDescriptor getPropertie() {
        return propertie;
    }

    public void setPropertie(PropertyDescriptor propertie) {
        this.propertie = propertie;
    }

    @Override
    public Component getCustomEditor() {
        return label;
    }

    public String getEditorValueName() {
        return "text";
    }
}
