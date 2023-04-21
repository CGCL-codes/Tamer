package net.ar.guia.own.adapters.visual;

import java.util.*;
import net.ar.guia.own.adapters.data.*;

public class SetToListValueHolder extends AbstractValueHolder implements ValueHolder {

    protected ValueHolder valueHolder;

    public SetToListValueHolder(ValueHolder aValueHolder) {
        valueHolder = aValueHolder;
    }

    public Object getValue() {
        return new Vector((Collection) valueHolder.getValue());
    }

    public void setValue(Object aValue) {
        valueHolder.setValue(new HashSet((Collection) aValue));
        fireValueChanged(aValue);
    }

    public Class getValueType() {
        return List.class;
    }

    public void setValueType(Class aClass) {
    }

    public void setNestedValueHolder(ValueHolder aValueHolder) {
    }

    public ValueHolder getNestedValueHolder() {
        return null;
    }
}
