package ca.petersens.gwt.databinding.client.converter;

import com.google.gwt.i18n.client.NumberFormat;

public class NumberFormatToString<T extends Number> implements ToString<T> {

    private final NumberFormat format;

    public NumberFormatToString(NumberFormat format) {
        if (format == null) {
            throw new NullPointerException("format must not be null");
        }
        this.format = format;
    }

    public final String toString(T value) {
        return (value == null ? null : format.format(value.doubleValue()));
    }

    protected final NumberFormat getFormat() {
        return format;
    }
}
