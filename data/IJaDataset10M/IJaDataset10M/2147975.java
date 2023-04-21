package org.akrogen.tkui.css.swing.properties;

import java.awt.Component;
import org.akrogen.tkui.css.core.engine.CSSEngine;
import org.w3c.dom.css.CSSValue;

public abstract class AbstractConvertedCSSPropertySwingHandler extends AbstractCSSPropertySwingHandler {

    protected void applyCSSProperty(Component component, String property, CSSValue value, String pseudo, CSSEngine engine) throws Exception {
        Object toType = getToType(value);
        if (toType != null) {
            Object newValue = engine.convert(value, toType, null);
            applyCSSPropertyValue(component, property, newValue, pseudo, engine);
        } else {
            applyCSSPropertyValue(component, property, value, pseudo, engine);
        }
    }

    protected String retrieveCSSProperty(Object value, String pseudo, CSSEngine engine) {
        Object toType = getToType(value);
        if (toType != null) {
            try {
                String newValue = engine.convert(value, toType, null);
                return newValue;
            } catch (Exception e) {
            }
        }
        return null;
    }

    protected abstract void applyCSSPropertyValue(Component component, String property, Object value, String pseudo, CSSEngine engine) throws Exception;

    protected abstract Object getToType(Object value);
}
