package com.gwtent.client.uibinder;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class ValueChangedByBindingListenerCollection extends ArrayList<IValueChangedByBindingListener> {

    public boolean fireBeforeValueChange(Object instance, String property, Object value) {
        boolean result = true;
        for (IValueChangedByBindingListener l : this) {
            if (!l.beforeValueChange(instance, property, value)) result = false;
        }
        return result;
    }

    public void fireAfterValueChanged(Object instance, String property, Object value) {
        for (IValueChangedByBindingListener l : this) {
            l.afterValueChanged(instance, property, value);
        }
    }
}
