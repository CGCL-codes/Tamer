package org.apache.myfaces.trinidad.bean;

import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import java.io.Serializable;

public class TestValueBinding extends ValueBinding implements StateHolder, Serializable {

    public TestValueBinding() {
    }

    @Override
    public Object getValue(FacesContext context) {
        return _value;
    }

    @Override
    public void setValue(FacesContext context, Object value) {
        _value = value;
    }

    @Override
    public boolean isReadOnly(FacesContext context) {
        return false;
    }

    @Override
    public Class<?> getType(FacesContext context) {
        return null;
    }

    public Object saveState(FacesContext context) {
        return _value;
    }

    public void restoreState(FacesContext context, Object state) {
        _value = state;
    }

    public boolean isTransient() {
        return _transient;
    }

    public void setTransient(boolean newTransientValue) {
        _transient = newTransientValue;
    }

    private boolean _transient;

    private Object _value;
}
