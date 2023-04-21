package org.meta.shell.idebeans.propertysheet;

import java.awt.*;
import java.util.*;
import java.beans.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * A class representing UI for boolean type.
 *
 * @author  V.Ganesh
 * @version 2.0 (Part of MeTA v2.0)
 */
public class IDEBooleanPropertyUI extends JComboBox implements IDEPropertyUI {

    /**
     * Holds value of property property.
     */
    private IDEProperty property;

    /**
     * Holds value of property theInstance.
     */
    private Object theInstance;

    /**
     * Holds value of property propertyDescriptor.
     */
    private PropertyDescriptor propertyDescriptor;

    /** Creates a new instance of IDEBooleanPropertyUI */
    public IDEBooleanPropertyUI() {
        super();
    }

    /** 
     * Creates a new instance of IDEBooleanPropertyUI
     * 
     * @param property - the property that is represented by this field
     * @param theInstance - The instance of class that contains this property
     */
    public IDEBooleanPropertyUI(IDEProperty property, Object theInstance, PropertyDescriptor pd) throws Exception {
        this.property = property;
        this.theInstance = theInstance;
        this.propertyDescriptor = pd;
        addItem(Boolean.TRUE);
        addItem(Boolean.FALSE);
        setPropertyBoolean();
        setEnabled(property.isSetMethod());
    }

    /**
     * sets the initial property value
     */
    private void setPropertyBoolean() throws Exception {
        if (!property.getPropertyType().isAssignableFrom(boolean.class)) {
            throw new Exception("Invalid property type : " + property.getPropertyType());
        }
        setSelectedItem((Boolean) propertyDescriptor.getReadMethod().invoke(theInstance));
    }

    /**
     * Add change listener for this property, if property is writable
     */
    public void makeChangeUpdatable() {
        if (!property.isSetMethod()) return;
        addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                if (!property.isSetMethod()) return;
                try {
                    propertyDescriptor.getWriteMethod().invoke(theInstance, getSelectedItem());
                } catch (Exception e) {
                    System.err.println("Unable to set value : " + e.toString());
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(IDEBooleanPropertyUI.this, "Unable to set value : " + e.toString(), "Error setting value", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    /**
     * Getter for property property.
     * @return Value of property property.
     */
    public IDEProperty getProperty() {
        return this.property;
    }

    /**
     * Getter for property theInstance.
     * @return Value of property theInstance.
     */
    public Object getTheInstance() {
        return this.theInstance;
    }

    /**
     * Getter for property propertyDescriptor.
     * @return Value of property propertyDescriptor.
     */
    public PropertyDescriptor getPropertyDescriptor() {
        return this.propertyDescriptor;
    }
}
