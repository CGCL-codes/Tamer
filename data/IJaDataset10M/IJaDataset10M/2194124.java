package org.springframework.faces.ui;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * Component that uses the Dojo implementation of Spring JavaScript to decorate a child input component with client-side
 * currency validation behavior.
 * 
 * @author Jeremy Grelle
 * 
 */
public class DojoClientCurrencyValidator extends DojoDecoration {

    private static final String DOJO_COMPONENT_TYPE = "dijit.form.CurrencyTextBox";

    private static final String[] DOJO_ATTRS_INTERNAL = new String[] { "currency" };

    private static final String[] DOJO_ATTRS;

    static {
        DOJO_ATTRS = new String[DojoDecoration.DOJO_ATTRS.length + DOJO_ATTRS_INTERNAL.length];
        System.arraycopy(DojoDecoration.DOJO_ATTRS, 0, DOJO_ATTRS, 0, DojoDecoration.DOJO_ATTRS.length);
        System.arraycopy(DOJO_ATTRS_INTERNAL, 0, DOJO_ATTRS, DojoDecoration.DOJO_ATTRS.length, DOJO_ATTRS_INTERNAL.length);
    }

    private String currency;

    public String getCurrency() {
        if (currency != null) {
            return currency;
        }
        ValueBinding exp = getValueBinding("currency");
        return exp != null ? (String) exp.getValue(getFacesContext()) : null;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    protected String[] getDojoAttributes() {
        return DOJO_ATTRS;
    }

    public String getDojoComponentType() {
        return DOJO_COMPONENT_TYPE;
    }

    public Object saveState(FacesContext context) {
        Object[] values = new Object[2];
        values[0] = super.saveState(context);
        values[1] = this.currency;
        return values;
    }

    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        this.currency = (String) values[1];
    }
}
