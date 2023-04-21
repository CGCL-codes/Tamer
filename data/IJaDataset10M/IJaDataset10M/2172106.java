package com.icesoft.faces.component.ext;

import javax.faces.component.UIComponentBase;
import javax.faces.el.ValueBinding;
import javax.faces.context.FacesContext;

public class HtmlCheckbox extends UIComponentBase {

    private String _for = null;

    private Integer _index = null;

    public HtmlCheckbox() {
        super();
        setRendererType(HtmlSelectManyCheckbox.RENDERER_TYPE);
    }

    public String getFamily() {
        return "com.icesoft.faces.HtmlCheckbox";
    }

    public void setFor(String forValue) {
        _for = forValue;
    }

    public String getFor() {
        if (_for != null) return _for;
        ValueBinding vb = getValueBinding("for");
        if (vb == null) return null;
        Object value = vb.getValue(getFacesContext());
        if (value == null) return null;
        return value.toString();
    }

    public void setIndex(int index) {
        _index = new Integer(index);
    }

    public int getIndex() {
        if (_index != null) return _index.intValue();
        ValueBinding vb = getValueBinding("index");
        Number v = vb != null ? (Number) vb.getValue(getFacesContext()) : null;
        return v != null ? v.intValue() : Integer.MIN_VALUE;
    }

    public Object saveState(FacesContext context) {
        Object values[] = new Object[3];
        values[0] = super.saveState(context);
        values[1] = _for;
        values[2] = _index;
        return values;
    }

    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        _for = (String) values[1];
        _index = (Integer) values[2];
    }
}
