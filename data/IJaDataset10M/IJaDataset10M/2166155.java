package org.bdgp.swing;

import javax.swing.*;
import java.awt.*;
import org.bdgp.util.*;

public class StringEditorComponent extends JTextArea implements GenericEditorComponent {

    protected String newStringValue;

    protected ListEditor editor;

    public StringEditorComponent(String newStringValue) {
        this.newStringValue = newStringValue;
    }

    public void store(Object in) {
        EditableString str = (EditableString) in;
        str.setValue(getText());
    }

    public void setMasterComponent(Component c) {
        if (c instanceof ListEditor) editor = (ListEditor) c;
    }

    public Object createNewValue() {
        return new EditableString(newStringValue);
    }

    public void load(Object in) {
        setText(in.toString());
    }

    public void setEditable(boolean in) {
        super.setEditable(in);
    }
}
