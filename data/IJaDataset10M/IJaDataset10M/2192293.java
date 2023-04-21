package org.peaseplate.domain.conversion;

public class ConversionException extends Exception {

    private static final long serialVersionUID = 1L;

    private final Object value;

    private final Class<?> type;

    public ConversionException(Object value, Class<?> type, String message) {
        this(value, type, message, null);
    }

    public ConversionException(Object value, Class<?> type, String message, Throwable cause) {
        super(message, cause);
        this.value = value;
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public Class<?> getType() {
        return type;
    }
}
