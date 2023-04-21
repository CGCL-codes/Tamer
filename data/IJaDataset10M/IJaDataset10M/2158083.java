package org.jenia.faces.foogle.component;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

public class UIFoogleSearch extends UIFoogle {

    private String key;

    public String getKey() {
        if (null != this.key) {
            return this.key;
        }
        ValueExpression _vb = getValueExpression("key");
        if (_vb != null) {
            return (java.lang.String) _vb.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object saveState(FacesContext _context) {
        Object _values[] = new Object[2];
        _values[0] = super.saveState(_context);
        _values[1] = key;
        return _values;
    }

    public void restoreState(FacesContext _context, Object _state) {
        Object _values[] = (Object[]) _state;
        super.restoreState(_context, _values[0]);
        key = (String) _values[1];
    }
}
