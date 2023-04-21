package org.zkoss.jsf.zul.impl;

import java.io.IOException;
import java.io.StringWriter;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

/**
 * The Base implementation of Attribute.
 * This component should be declared nested under {@link org.zkoss.jsf.zul.Page}.
 * @author Dennis.Chen
 */
public class BaseAttribute extends AbstractComponent {

    private String _name = null;

    private BranchComponent _parentcomp;

    StringWriter fakeSw = null;

    ResponseWriter fakeOw = null;

    /**
	 * Override Method, write a body content to parent's attribute
	 * 
	 * @see  BranchComponent#addZULDynamicAttribute(String, Object)
	 */
    public void encodeEnd(FacesContext context) throws IOException {
        context.setResponseWriter(fakeOw);
        fakeSw.close();
        String content = fakeSw.toString();
        fakeSw = null;
        if (!isRendered() || !isEffective()) return;
        if (_name != null) {
            _parentcomp.addZULDynamicAttribute(_name, getBodyContent());
        }
        setBodyContent(null);
    }

    /** 
	 * Override method,
	 * We Construct ZUL JSF Component tree here.
	 * This method is called by JSF implementation, deriving class rarely need to invoke this method.
	 */
    public void encodeBegin(FacesContext context) throws IOException {
        fakeSw = new StringWriter();
        fakeOw = context.getResponseWriter();
        context.setResponseWriter(fakeOw.cloneWithWriter(fakeSw));
        if (!isRendered() || !isEffective()) return;
        super.encodeBegin(context);
        final AbstractComponent ac = (AbstractComponent) findAncestorWithClass(this, AbstractComponent.class);
        if (ac instanceof RootComponent) {
        } else if (ac instanceof BranchComponent) {
            _parentcomp = (BranchComponent) ac;
        } else {
            throw new IllegalStateException("Must be nested inside the page component: " + this);
        }
    }

    /**
	 * @return name of attribute.
	 */
    public String getName() {
        return _name;
    }

    /**
	 * set name of attribute.
	 * @param _name
	 */
    public void setName(String _name) {
        this._name = _name;
    }

    public Object saveState(FacesContext context) {
        Object values[] = new Object[2];
        values[0] = super.saveState(context);
        values[1] = _name;
        return (values);
    }

    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        _name = ((String) values[1]);
    }
}
