package org.akrogen.tkui.css.swt.properties.css2.lazy.border;

import org.akrogen.tkui.css.core.css2.CSSBorderPropertiesHelpers;
import org.akrogen.tkui.css.core.dom.properties.CSSBorderProperties;
import org.akrogen.tkui.css.core.dom.properties.ICSSPropertyHandler2;
import org.akrogen.tkui.css.core.dom.properties.ICSSPropertyHandler2Delegate;
import org.akrogen.tkui.css.core.engine.CSSEngine;
import org.akrogen.tkui.css.core.impl.dom.properties.CSSBorderPropertiesImpl;
import org.akrogen.tkui.css.swt.CSSSWTConstants;
import org.akrogen.tkui.css.swt.helpers.CSSSWTHelpers;
import org.akrogen.tkui.css.swt.properties.AbstractCSSPropertySWTHandler;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.w3c.dom.css.CSSValue;

public abstract class AbstractCSSPropertyBorderSWTHandler extends AbstractCSSPropertySWTHandler implements ICSSPropertyHandler2Delegate {

    public void applyCSSProperty(Control control, String property, CSSValue value, String pseudo, CSSEngine engine) throws Exception {
        Composite parent = control.getParent();
        if (parent == null) return;
        CSSBorderProperties border = (CSSBorderProperties) control.getData(CSSSWTConstants.CONTROL_CSS2BORDER_KEY);
        if (border == null) {
            border = new CSSBorderPropertiesImpl();
            control.setData(CSSSWTConstants.CONTROL_CSS2BORDER_KEY, border);
            parent.addPaintListener(CSSSWTHelpers.createBorderPaintListener(control, engine.getResourcesRegistry()));
        }
        super.applyCSSProperty(border, property, value, pseudo, engine);
    }

    public void applyCSSProperty(CSSBorderProperties border, String property, CSSValue value, String pseudo, CSSEngine engine) throws Exception {
        CSSBorderPropertiesHelpers.updateCSSProperty(border, property, value);
    }

    public ICSSPropertyHandler2 getCSSPropertyHandler2() {
        return CSSPropertyBorderSWTHandler2.INSTANCE;
    }
}
