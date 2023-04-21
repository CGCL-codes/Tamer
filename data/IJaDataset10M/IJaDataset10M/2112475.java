package de.sic_consult.forms.editor.swing;

import java.awt.Component;
import java.beans.PropertyDescriptor;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import de.sic_consult.forms.editor.EditString;

public class StringEditor extends DefaultPropertyEditor<String> {

    JTextComponent field;

    JScrollPane pane;

    public StringEditor() {
    }

    @Override
    public void setPropertie(PropertyDescriptor propertie) {
        EditString e = propertie.getReadMethod().getAnnotation(EditString.class);
        if (e != null && e.multiline()) {
            field = new JTextArea(e.lines(), e.columns());
            pane = new JScrollPane(field);
            pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        } else {
            if (e != null) {
                field = new JTextField(e.columns());
            } else {
                field = new JTextField(EditString.DEFAULTCOLS);
            }
        }
    }

    @Override
    public Component getCustomEditor() {
        return pane != null ? pane : field;
    }

    @Override
    public void setValue(Object value) {
        if (value != null) {
            field.setText(value.toString());
        } else {
            field.setText("");
        }
    }

    @Override
    public Object getValue() {
        return field.getText();
    }
}
