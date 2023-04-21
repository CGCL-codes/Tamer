package org.cantaloop.tools.simplegui;

import javax.swing.JCheckBox;

public class BooleanFormElement extends AbstractSimpleFormElement {

    private JCheckBox m_checkbox;

    public BooleanFormElement() {
        super();
        m_checkbox = new JCheckBox();
        setJComponent(m_checkbox);
    }

    public Object getValue() {
        return m_checkbox.isSelected() ? Boolean.TRUE : Boolean.FALSE;
    }

    public void setValue(Object o) {
        m_checkbox.setSelected(((Boolean) o).booleanValue());
    }
}
