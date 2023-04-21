package net.sf.springlayout.web.controller.propertyeditor;

import java.beans.PropertyEditorSupport;
import org.springframework.util.StringUtils;

/**
 * A property editor that handles decimal round numbers.
 */
public final class NullableDecimalPropertyEditor extends PropertyEditorSupport {

    private Converter converter;

    public NullableDecimalPropertyEditor(Converter converter) {
        this.converter = converter;
    }

    public static final Converter BYTE_CONVERTER = new Converter() {

        public Object stringToNumber(String str) {
            return Byte.valueOf(str);
        }
    };

    public static final Converter SHORT_CONVERTER = new Converter() {

        public Object stringToNumber(String str) {
            return Short.valueOf(str);
        }
    };

    public static final Converter INTEGER_CONVERTER = new Converter() {

        public Object stringToNumber(String str) {
            return Integer.valueOf(str);
        }
    };

    public static final Converter LONG_CONVERTER = new Converter() {

        public Object stringToNumber(String str) {
            return Long.valueOf(str);
        }
    };

    public void setAsText(String text) throws IllegalArgumentException {
        if (!StringUtils.hasText(text)) {
            setValue(null);
        } else {
            setValue(this.converter.stringToNumber(text));
        }
    }

    public String getAsText() {
        Object value = getValue();
        if (value == null) {
            return "";
        }
        return value.toString();
    }

    private interface Converter {

        public Object stringToNumber(String str);
    }
}
