package org.jprovocateur.serializer.converters.basic;

import org.jprovocateur.serializer.converters.SingleValueConverter;

/**
 * Base abstract implementation of  {@link org.jprovocateur.serializer.converters.SingleValueConverter}.
 * <p/>
 * <p>Subclasses should implement methods canConvert(Class) and fromString(String) for the conversion.</p>
 *
 * @author Joe Walnes
 * @author J&ouml;rg Schaible
 * @author Mauro Talevi
 * @see org.jprovocateur.serializer.converters.SingleValueConverter
 */
public abstract class AbstractSingleValueConverter implements SingleValueConverter {

    public abstract boolean canConvert(Class type);

    public String toString(Object obj) {
        return obj == null ? null : obj.toString();
    }

    public abstract Object fromString(String str);
}
