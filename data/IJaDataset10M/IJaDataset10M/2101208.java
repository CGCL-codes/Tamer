package com.beanview.swing;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.text.JTextComponent;
import com.beanview.swing.SwingBeanViewPanel;
import javax.swing.JFrame;
import com.beanview.base.ConfigurationTestBase;
import com.beanview.test.ConfigurationTestObject;

public class SwingConfigurationTest extends ConfigurationTestBase {

    @SuppressWarnings("unchecked")
    protected void setUp() throws Exception {
        super.setUp();
        bean = new SwingBeanViewPanel();
        ConfigurationTestObject temp = new ConfigurationTestObject();
        bean.setDataObject(temp);
    }

    Component[] getComponents() {
        return ((SwingBeanViewPanel) bean).getComponents();
    }

    @Override
    public void testFirstNameLabel() {
        System.out.println("TEST: SWING testFirstNameLabel");
        Component[] components = getComponents();
        assertNotNull(components);
        boolean found = false;
        for (Component component : components) {
            if (component.getName().compareTo("firstName.label") == 0) {
                assertEquals("Familiar Name", ((JLabel) component).getText());
                found = true;
            }
        }
        assertTrue(found);
        assertTrue(((JTextComponent) bean.getPropertyComponent("firstName")).isEditable());
    }

    @Override
    public void testLastNameLabel() {
        if (!bean.getClass().equals(SwingBeanViewPanel.class)) return;
        System.out.println("TEST: testLastNameLabel");
        Component[] components = ((SwingBeanViewPanel) bean).getComponents();
        assertNotNull(components);
        boolean found = false;
        for (Component component : components) {
            if (component.getName().compareTo("lastName.label") == 0) {
                assertEquals("Last Name", ((JLabel) component).getText());
                found = true;
            }
        }
        assertTrue(found);
        assertTrue(((JTextComponent) bean.getPropertyComponent("lastName")).isEditable());
    }

    @Override
    public void testEditable() {
        System.out.println("TEST: testEditable");
        assertFalse(((JTextComponent) bean.getPropertyComponent("dontTouchThisField")).isEditable());
    }

    @Override
    public void testReadOnly() {
        System.out.println("TEST: Swing testReadOnly");
        assertFalse(((JTextComponent) bean.getPropertyComponent("readOnly")).isEditable());
    }

    public static void main(String[] argv) {
        JFrame frame = new JFrame();
        SwingBeanViewPanel bean = new SwingBeanViewPanel();
        bean = new SwingBeanViewPanel<ConfigurationTestObject>();
        ConfigurationTestObject temp = new ConfigurationTestObject();
        bean.setDataObject(temp);
        frame.add(bean);
        frame.pack();
        frame.setVisible(true);
    }
}
