package org.nakedobjects.persistence.nhibernate.property;

import java.util.HashMap;
import org.nakedobjects.object.NakedObjectField;

/**
 * Factory to return a PropertyConverter for conversion between a specific 
 * NakedObjects value holder type and standard Java data type.
 * Additional Converters can be added by calling getInstance().add(..)
 */
public class ConverterFactory {

    private HashMap converters = new HashMap();

    private static ConverterFactory instance = new ConverterFactory();

    private ConverterFactory() {
    }

    /**
	 * Return a PropertyConverter which converts from a value holder
	 *  to a persistent class.
	 * @param nakedClass Class of the value holder to convert from
	 * 			eg WholeNumber.class
	 */
    public PropertyConverter getConverter(final Class nakedClass) {
        return getConverter(nakedClass.getName());
    }

    /**
	 * Return a PropertyConverter which converts from a value holder
	 *  to a persistent class.
	 * @param nakedClass name of the value holder to convert from
	 * @return the PropertyConverter, or null if none is registered
	 */
    public PropertyConverter getConverter(final String nakedClass) {
        return (PropertyConverter) converters.get(nakedClass);
    }

    public PropertyConverter getConverter(final NakedObjectField field) {
        return getConverter(field.getSpecification().getFullName());
    }

    /**
	 * Add a converter for the specified value type
	 */
    public void add(final Class valueType, final PropertyConverter converter) {
        if (!converters.containsKey(valueType.getName())) {
            converters.put(valueType.getName(), converter);
        }
    }

    /**
	 * Return the singleton ConverterFactory
	 */
    public static ConverterFactory getInstance() {
        return instance;
    }
}
