package jmax.widgets;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.lang.reflect.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

/**
  A Swing component that displays the properties of a Java Bean.

  <P>
  It is a JPanel with a GridBag layout, each row containing a JLabel 
  (the property name) and a JTextField (the property value).

  <P>
  Actually, the JTextField cannot be edited to change the property value.
  This will change in future versions...
  */
public class BeanPanel extends JPanel {

    /**
    Creates an empty <CODE>BeanPanel</CODE>
    */
    public BeanPanel() {
        layout = new GridBagLayout();
        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        displayedProperties = new Vector();
        setLayout(layout);
    }

    private class UnreadablePropertyException extends Exception {

        UnreadablePropertyException(String message) {
            super(message);
        }
    }

    private class PropertyUpdater {

        PropertyUpdater(Object beanInstance, PropertyDescriptor descriptor) {
            this.beanInstance = beanInstance;
            this.descriptor = descriptor;
            isCustom = false;
            if (descriptor.getPropertyType() != null) {
                PropertyEditor editor = PropertyEditorManager.findEditor(descriptor.getPropertyType());
                if (editor != null) {
                    try {
                        editor.setValue(getPropertyValue());
                    } catch (UnreadablePropertyException e) {
                    }
                    if (editor.supportsCustomEditor()) {
                        isCustom = true;
                        component = editor.getCustomEditor();
                        return;
                    }
                }
            }
            JTextField textField = new JTextField(getValueAsText());
            textField.setEditable(false);
            textField.setBackground(Color.white);
            component = textField;
        }

        Object getPropertyValue() throws UnreadablePropertyException {
            if (descriptor.getPropertyType() == null) throw new UnreadablePropertyException("");
            Method readMethod = descriptor.getReadMethod();
            try {
                if (readMethod != null && readMethod.getParameterTypes().length == 0) return readMethod.invoke(beanInstance, (Object[]) null);
            } catch (InvocationTargetException e) {
                throw new UnreadablePropertyException("");
            } catch (IllegalAccessException e) {
                throw new UnreadablePropertyException("");
            }
            throw new UnreadablePropertyException("");
        }

        String getValueAsText() {
            String value = "unknown";
            try {
                Object ret = getPropertyValue();
                if (ret != null) value = ret.toString(); else value = "null";
            } catch (UnreadablePropertyException e) {
            }
            return value;
        }

        void update() {
            if (!isCustom) ((JTextField) component).setText(getValueAsText());
        }

        protected Object beanInstance;

        protected PropertyDescriptor descriptor;

        protected boolean isCustom;

        protected Component component;
    }

    private void addOne(Object beanInstance, PropertyDescriptor descriptor, String labelName) {
        JLabel label = new JLabel(labelName);
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridwidth = 1;
        layout.setConstraints(label, constraints);
        add(label);
        PropertyUpdater updater = new PropertyUpdater(beanInstance, descriptor);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        layout.setConstraints(updater.component, constraints);
        add(updater.component);
        displayedProperties.addElement(updater);
    }

    /**
    Adds to the displayed properties all the properties (except "class")
    of the specified Bean instance, each property being labeled with its name
    @param beanInstance the Bean instance to display
    */
    public void display(Object beanInstance) {
        PropertyDescriptor descriptors[] = null;
        try {
            descriptors = Introspector.getBeanInfo(beanInstance.getClass()).getPropertyDescriptors();
        } catch (IntrospectionException e) {
            return;
        }
        for (int i = 0; i < descriptors.length; i++) if (!descriptors[i].getName().equals("class")) addOne(beanInstance, descriptors[i], descriptors[i].getName());
        update();
    }

    /**
    Adds to the displayed properties the specified property of the specified Bean instance,
    the property being labeled with its name
    @param beanInstance the Bean instance to display
    @param propertyName the name of the property to display
    */
    public void display(Object beanInstance, String propertyName) {
        display(beanInstance, propertyName, propertyName);
    }

    /**
    Adds to the displayed properties the specified property of the specified Bean instance,
    labeled with the specified display name
    @param beanInstance the Bean instance to be displayed
    @param propertyName the name of the property to display
    @param labelName the name that will appear in the label
    */
    public void display(Object beanInstance, String propertyName, String labelName) {
        PropertyDescriptor descriptors[] = null;
        try {
            descriptors = Introspector.getBeanInfo(beanInstance.getClass()).getPropertyDescriptors();
        } catch (IntrospectionException e) {
            return;
        }
        int i;
        for (i = 0; i < descriptors.length; i++) if (propertyName.equals(descriptors[i].getName())) break;
        if (i < descriptors.length) addOne(beanInstance, descriptors[i], labelName);
        update();
    }

    /**
    Update the display, by calling the read methods for all the properties
    that are currently displayed.
    */
    public void update() {
        for (Enumeration e = displayedProperties.elements(); e.hasMoreElements(); ) ((PropertyUpdater) e.nextElement()).update();
    }

    private GridBagLayout layout;

    private GridBagConstraints constraints;

    private Vector displayedProperties;

    public static void main(String args[]) {
        new ColorPropertyEditor();
        JFrame frame = new JFrame("BeanPanelTest");
        BeanPanel panel = new BeanPanel();
        Object instance = null;
        try {
            instance = Class.forName(args[0]).newInstance();
        } catch (java.lang.IllegalAccessException e) {
            System.err.println(e);
            System.exit(0);
        } catch (java.lang.InstantiationException e) {
            System.err.println(e);
            System.exit(0);
        } catch (java.lang.ClassNotFoundException e) {
            System.err.println(e);
            System.exit(0);
        }
        panel.display(instance);
        frame.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setSize(frame.getPreferredSize());
        frame.validate();
        frame.setVisible(true);
        JColorChooser.showDialog(frame, "Color Chooser", Color.white);
    }
}
