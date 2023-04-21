package org.swirrel.installer;

import java.awt.Component;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import org.swirrel.listener.SwirrelInputMethodListener;
import org.swirrel.util.MethodCall;
import org.swirrel.annotation.CaretPositionChanged;
import org.swirrel.annotation.InputMethodTextChanged;
import java.awt.event.InputMethodListener;

/**
 *
 * @author Gronau
 */
public class InputMethodListenerInstaller extends AbstractListenerInstaller implements SwirrelListenerInstaller {

    public InputMethodListenerInstaller() {
        super(Arrays.<Class<? extends Annotation>>asList(CaretPositionChanged.class, InputMethodTextChanged.class), Arrays.<Class<?>>asList(Component.class));
    }

    public boolean isListenerAlreadySet(Object obj) {
        InputMethodListener[] inputMethodListeners = ((Component) obj).getInputMethodListeners();
        for (InputMethodListener iml : inputMethodListeners) {
            if (iml instanceof SwirrelInputMethodListener) {
                return true;
            }
        }
        return false;
    }

    public void registerListener(Field field, Component comp, java.util.List<MethodCall> methodCalls) throws IllegalAccessException {
        Object obj = field.get(comp);
        InputMethodListener iml = new SwirrelInputMethodListener(methodCalls.get(0), methodCalls.get(1));
        ((Component) obj).addInputMethodListener(iml);
    }
}
