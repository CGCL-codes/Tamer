package com.global360.sketchpadbpmn.propertiespanel;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JComponent;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class BooleanTableCell extends TableCell implements ActionListener {

    private static final long serialVersionUID = 1L;

    public static final String FALSE_STRING = Boolean.toString(false);

    public static final String TRUE_STRING = Boolean.toString(true);

    private JComboBox component = null;

    public BooleanTableCell() {
        this.component = new JComboBox();
        this.component.setOpaque(true);
        this.component.addItem(PropertyValue.NULL_OBJECT_STRING);
        this.component.addItem(FALSE_STRING);
        this.component.addItem(TRUE_STRING);
        this.component.addActionListener(this);
        initializeKeyEvents();
    }

    protected Component getComponent(Object value, boolean isEnabled) {
        if (value == null) {
            this.component.setSelectedItem(PropertyValue.NULL_OBJECT_STRING);
        } else {
            String stringValue = value.toString();
            this.component.getModel().setSelectedItem(stringValue);
        }
        initializeComponent(this.component, isEnabled);
        this.component.setBorder(null);
        return this.component;
    }

    protected JComponent getJComponent() {
        return this.component;
    }

    public Object getCellEditorValue() {
        return getCurrentValue();
    }

    public void actionPerformed(ActionEvent e) {
        this.stopCellEditing();
    }

    private BooleanProperty getCurrentValue() {
        BooleanProperty result = null;
        String currentValue = (String) this.component.getSelectedItem();
        if (currentValue != null) {
            if (currentValue.equalsIgnoreCase(PropertyValue.NULL_OBJECT_STRING)) result = null; else result = new BooleanProperty(new Boolean((String) currentValue));
        }
        return result;
    }
}
