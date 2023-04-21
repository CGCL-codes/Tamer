package net.sourceforge.liftoff.builder;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class BooleanEditor extends AttributeEditor implements ItemListener, PropertyChangeListener {

    JComboBox combo;

    private int loopGuard = 0;

    public BooleanEditor() {
        String[] values = { "no", "yes" };
        combo = new JComboBox(values);
        component = combo;
        combo.addItemListener(this);
    }

    public void setField(FieldDescr descr, String ident) {
        if (field != null) {
            StructDescr.removePropertyChangeListener(field.getPropertyName(ident), this);
        }
        super.setField(descr, ident);
        combo.setSelectedItem(getValue());
        StructDescr.addPropertyChangeListener(descr.getPropertyName(ident), this);
    }

    public void itemStateChanged(ItemEvent e) {
        loopGuard++;
        if (e.getStateChange() == ItemEvent.SELECTED) updateValue((String) (e.getItem()));
        loopGuard--;
    }

    public void propertyChange(PropertyChangeEvent e) {
        if (loopGuard > 0) return;
        combo.setSelectedItem(getValue());
    }
}
