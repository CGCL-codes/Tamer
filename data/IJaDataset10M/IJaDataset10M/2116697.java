package org.jenia.faces.datatools.component;

import javax.el.ValueExpression;
import javax.faces.component.UIData;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import org.jenia.faces.util.Util;

public class UITotalRowsOfList extends UIParameter {

    public static final String COMPONENT_TYPE = "org.jenia.faces.datatools.UITotalRowsOfList";

    private String forId;

    public UITotalRowsOfList() {
        super();
        setRendererType(null);
    }

    public String getFor() {
        if (null != this.forId) {
            return this.forId;
        }
        ValueExpression _vb = getValueExpression("for");
        if (_vb != null) {
            return (java.lang.String) _vb.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    public void setFor(String forId) {
        this.forId = forId;
    }

    public Object saveState(FacesContext _context) {
        Object _values[] = new Object[2];
        _values[0] = super.saveState(_context);
        _values[1] = forId;
        return _values;
    }

    public void restoreState(FacesContext _context, Object _state) {
        Object _values[] = (Object[]) _state;
        super.restoreState(_context, _values[0]);
        forId = (String) _values[1];
    }

    public Object getValue() {
        UIData data = (UIData) Util.getForComponent(Util.getFacesContext(), getFor(), Util.getFacesContext().getViewRoot());
        return Integer.toString(UIDataTools.getValueSize(data));
    }
}
