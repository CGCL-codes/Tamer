package org.apache.commons.beanutils.converters;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

/**
 * <p>Standard {@link Converter} implementation that converts an incoming
 * String into a <code>java.lang.Byte</code> object, optionally using a
 * default value or throwing a {@link ConversionException} if a conversion
 * error occurs.</p>
 *
 * @author Craig R. McClanahan
 * @version $Revision: 1.9 $ $Date: 2004-02-28 05:18:37 -0800 (Sat, 28 Feb 2004) $
 * @since 1.3
 */
public final class ByteConverter implements Converter {

    /**
     * Create a {@link Converter} that will throw a {@link ConversionException}
     * if a conversion error occurs.
     */
    public ByteConverter() {
        this.defaultValue = null;
        this.useDefault = false;
    }

    /**
     * Create a {@link Converter} that will return the specified default value
     * if a conversion error occurs.
     *
     * @param defaultValue The default value to be returned
     */
    public ByteConverter(Object defaultValue) {
        this.defaultValue = defaultValue;
        this.useDefault = true;
    }

    /**
     * The default value specified to our Constructor, if any.
     */
    private Object defaultValue = null;

    /**
     * Should we return the default value on conversion errors?
     */
    private boolean useDefault = true;

    /**
     * Convert the specified input object into an output object of the
     * specified type.
     *
     * @param type Data type to which this value should be converted
     * @param value The input value to be converted
     *
     * @exception ConversionException if conversion cannot be performed
     *  successfully
     */
    public Object convert(Class type, Object value) {
        if (value == null) {
            if (useDefault) {
                return (defaultValue);
            } else {
                throw new ConversionException("No value specified");
            }
        }
        if (value instanceof Byte) {
            return (value);
        } else if (value instanceof Number) {
            return new Byte(((Number) value).byteValue());
        }
        try {
            return (new Byte(value.toString()));
        } catch (Exception e) {
            if (useDefault) {
                return (defaultValue);
            } else {
                throw new ConversionException(e);
            }
        }
    }
}
