package org.jenia.faces.foogle.component;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

public class UIWebSearchType extends UIFoogleSearchType {

    public static final String COMPONENT_TYPE = "org.jenia.faces.foogle.UIWebSearchType";

    private String siteRestriction;

    public String getSiteRestriction() {
        if (null != this.siteRestriction) {
            return this.siteRestriction;
        }
        ValueExpression _vb = getValueExpression("siteRestriction");
        if (_vb != null) {
            return (java.lang.String) _vb.getValue(getFacesContext().getELContext());
        } else {
            return null;
        }
    }

    public void setSiteRestriction(String siteRestriction) {
        this.siteRestriction = siteRestriction;
    }

    public Object saveState(FacesContext _context) {
        Object _values[] = new Object[2];
        _values[0] = super.saveState(_context);
        _values[1] = siteRestriction;
        return _values;
    }

    public void restoreState(FacesContext _context, Object _state) {
        Object _values[] = (Object[]) _state;
        super.restoreState(_context, _values[0]);
        siteRestriction = (String) _values[1];
    }
}
