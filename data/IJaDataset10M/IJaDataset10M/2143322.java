package com.googlecode.brui;

import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.swing.JComponent;
import javax.swing.JPanel;
import com.googlecode.brui.components.*;
import com.googlecode.brui.editors.BooleanEditor;
import com.googlecode.brui.editors.IntegerEditor;
import com.googlecode.brui.editors.StringEditor;

public class BeanComponentModel {

    private static final Object[] GETTER_ARGS = new Object[0];

    public JComponent getComponent(Class<?> bean, Object instance) throws IntrospectionException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        JComponent toReturn = null;
        if (bean == null || instance == null) throw new NullPointerException("You cannot construct a component " + "without arguments bean and instance.");
        PropertyDescriptor[] beanProperties = Introspector.getBeanInfo(bean.getClass()).getPropertyDescriptors();
        for (PropertyDescriptor p : beanProperties) {
            Method setter = p.getWriteMethod();
            Method getter = p.getReadMethod();
            if (setter != null && getter != null) {
                Class type = p.getPropertyType();
                if (type.equals(Integer.class)) {
                    new IntegerEditor(getter, setter, instance);
                } else if (type.equals(String.class)) {
                    new StringEditor(getter, setter, instance);
                } else if (type.equals(Boolean.class)) {
                    new BooleanEditor(getter, setter, instance);
                } else {
                }
            }
        }
        return toReturn;
    }

    private LayoutManager getDefaultLayout() {
        GridBagLayout toReturn = new GridBagLayout();
        return toReturn;
    }
}
