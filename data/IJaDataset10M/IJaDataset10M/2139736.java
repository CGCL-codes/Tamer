package net.sf.clcl.conversion;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import net.sf.clcl.ConfigException;

public class BigIntegerConverter extends AbstractSingleConverter {

    private final NumberFormat format;

    public BigIntegerConverter() {
        this(NumberFormat.getInstance());
    }

    public BigIntegerConverter(NumberFormat format) {
        this.format = format;
    }

    protected boolean accepts(Object value, Class type) {
        return BigInteger.class.equals(type) && (value instanceof String);
    }

    protected Object doConvert(Object value, Class type) {
        try {
            Number converted = format.parse((String) value);
            if (converted instanceof BigInteger) {
                return converted;
            } else {
                return new BigInteger(converted.toString());
            }
        } catch (ParseException exc) {
            throw new ConfigException("Can't parse [" + value + "] with [" + format + "].", exc);
        }
    }

    public Converter withFormat(String pattern) {
        return new BigIntegerConverter(new DecimalFormat(pattern));
    }

    public String toString() {
        return "BigIntegerConverter{format=" + toString(format) + "}";
    }

    private static String toString(NumberFormat format) {
        if (format instanceof DecimalFormat) {
            return ((DecimalFormat) format).toPattern();
        } else {
            return format.toString();
        }
    }
}
