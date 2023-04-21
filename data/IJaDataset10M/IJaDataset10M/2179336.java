package net.sf.fjdbc;

import java.sql.SQLException;
import java.text.MessageFormat;

public class SQLConversionException extends SQLException {

    private static final String CANT_CONVERT = "Can''t convert {0} to {1}";

    public SQLConversionException(Object original, Class<?> target) {
        super(MessageFormat.format(CANT_CONVERT, original, target));
    }

    public SQLConversionException(Object original, Class<?> target, Throwable t) {
        this(original, target);
        initCause(t);
    }
}
