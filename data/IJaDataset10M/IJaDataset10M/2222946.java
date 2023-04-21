package org.akrogen.tkui.css.swt.properties;

import org.akrogen.tkui.css.core.dom.properties.ICSSPropertyHandler;
import org.akrogen.tkui.css.core.engine.CSSEngine;
import org.akrogen.tkui.css.swt.helpers.SWTElementHelpers;
import org.eclipse.swt.widgets.Control;
import org.w3c.dom.css.CSSValue;

/**
 * Abstract CSS Property SWT Handler to check if the <code>element</code>
 * coming from applyCSSProperty and retrieveCSSProperty methods is SWT Control.
 * 
 * @version 1.0.0
 * @author <a href="mailto:angelo.zerr@gmail.com">Angelo ZERR</a>
 * 
 */
public abstract class AbstractCSSPropertySWTHandler implements ICSSPropertyHandler {

    public boolean applyCSSProperty(Object element, String property, CSSValue value, String pseudo, CSSEngine engine) throws Exception {
        Control control = SWTElementHelpers.getControl(element);
        if (control != null) {
            this.applyCSSProperty(control, property, value, pseudo, engine);
            return true;
        }
        return false;
    }

    public String retrieveCSSProperty(Object element, String property, String pseudo, CSSEngine engine) throws Exception {
        Control control = SWTElementHelpers.getControl(element);
        if (control != null) {
            return retrieveCSSProperty(control, property, pseudo, engine);
        }
        return null;
    }

    /**
	 * Apply CSS Property <code>property</code> (ex : background-color) with
	 * CSSValue <code>value</code> (ex : red) into the SWT
	 * <code>control</code> (ex : SWT Text, SWT Label).
	 * 
	 * @param control
	 * @param property
	 * @param value
	 * @param pseudo
	 * @param engine
	 * @throws Exception
	 */
    protected abstract void applyCSSProperty(Control control, String property, CSSValue value, String pseudo, CSSEngine engine) throws Exception;

    /**
	 * Retrieve CSS value (ex : red) of CSS Property <code>property</code> (ex :
	 * background-color) from the SWT <code>control</code> (ex : SWT Text, SWT
	 * Label).
	 * 
	 * @param control
	 * @param property
	 * @param engine
	 * @return
	 * @throws Exception
	 */
    protected abstract String retrieveCSSProperty(Control control, String property, String pseudo, CSSEngine engine) throws Exception;
}
