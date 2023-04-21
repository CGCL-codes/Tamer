package com.nexirius.framework.layout;

import javax.swing.*;
import java.lang.reflect.Method;

/**
 * This class is used to call a specific method of a SWING JComponent
 *
 * @author Marcel Baumann
 */
public class DefaultPropertyChanger implements PropertyChanger {

    String methodName;

    Object params[];

    public DefaultPropertyChanger(String methodName, Object params[]) {
        this.methodName = methodName;
        this.params = params;
    }

    /**
     * Set the property of the given component
     */
    public void change(JComponent comp) {
        try {
            if (comp instanceof JScrollPane) {
                comp = (JComponent) ((JScrollPane) comp).getViewport().getView();
            }
            Method method = null;
            if (params == null || params.length == 0) {
                method = comp.getClass().getMethod(methodName, null);
            } else {
                Method methods[] = comp.getClass().getMethods();
                for (int i = 0; i < methods.length; ++i) {
                    if (methods[i].getName().equals(methodName) && methods[i].getParameterTypes().length == params.length) {
                        method = methods[i];
                        break;
                    }
                }
            }
            if (method == null) {
                throw new Exception("No such method");
            }
            method.invoke(comp, params);
        } catch (Exception ex) {
            System.err.println("DefaultPropertyChanger is trying to invoke method " + methodName + " on " + comp.getClass().getName());
            ex.printStackTrace();
        }
    }
}
