package cookxml.core.converter;

import cookxml.core.DecodeEngine;
import cookxml.core.exception.ConverterException;
import cookxml.core.interfaces.Converter;

/**
 * This converter converts a string represenation into integer.
 * @see java.lang.Integer
 * @since CookXml 1.0
 * @author Heng Yuan
 */
public class IntConverter implements Converter {

    private static final Converter s_instance = new IntConverter();

    public static Converter getInstance() {
        return s_instance;
    }

    private IntConverter() {
    }

    public Object convert(String value, DecodeEngine decodeEngine) throws ConverterException {
        try {
            return Integer.valueOf(value);
        } catch (Exception ex) {
            throw new ConverterException(decodeEngine, ex, this, value);
        }
    }
}
