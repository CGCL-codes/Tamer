package org.apache.commons.beanutils.converters;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

/**
 * <p>Standard {@link Converter} implementation that converts an incoming
 * String into a <code>java.lang.String</code> object, optionally using a
 * default value or throwing a {@link ConversionException} if a conversion
 * error occurs.</p>
 *
 * @author Craig R. McClanahan
 * @version $Revision: 1.5 $ $Date: 2004-02-28 05:18:37 -0800 (Sat, 28 Feb 2004) $
 * @since 1.3
 */
public final class StringConverter implements Converter {

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
            return ((String) null);
        } else {
            return (value.toString());
        }
    }
}
