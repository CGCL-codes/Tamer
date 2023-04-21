package com.jguigen.standard;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;

public class JxButtonGroup extends ButtonGroup {

    public JxButtonGroup() {
        super();
    }

    public void add(AbstractButton b) {
        super.add(b);
    }

    public java.util.Enumeration getElements() {
        return super.getElements();
    }

    public ButtonModel getSelection() {
        return super.getSelection();
    }

    public boolean isSelected(ButtonModel m) {
        return super.isSelected(m);
    }

    public void remove(AbstractButton b) {
        super.remove(b);
    }

    public void setSelected(ButtonModel m, boolean b) {
        super.setSelected(m, b);
    }
}
