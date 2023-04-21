package org.springframework.web.servlet.tags.form;

import java.beans.PropertyEditor;

/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public final class ValueFormatterWrapper {

    public static String getDisplayString(Object value, boolean htmlEscape) {
        return ValueFormatter.getDisplayString(value, htmlEscape);
    }

    public static String getDisplayString(Object value, PropertyEditor propertyEditor, boolean htmlEscape) {
        return ValueFormatter.getDisplayString(value, propertyEditor, htmlEscape);
    }
}
