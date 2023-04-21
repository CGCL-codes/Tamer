package net.sf.rcpforms.modeladapter.converter;

import net.sf.rcpforms.modeladapter.util.Validate;
import org.eclipse.core.databinding.conversion.IConverter;

/**
 * Class IntegerToStringConverter converts an Integer value to non internationalized String
 * Default eclipse number converters works the following way:
 * Integer --> String: 1234 --> 1,234 for en-Locale
 * String --> Integer: 1234 --> 1234
 * 
 * This Converter does not take care of the current language setting:
 * Integer --> String: 1234 --> 1234
 * 
 * @author Remo Loetscher
 * @since v 1.1
 */
public class IntegerToStringConverter implements IConverter {

    /**
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.databinding.conversion.IConverter#convert(java.lang.Object)
     */
    public Object convert(Object fromObject) {
        if (fromObject == null) return "";
        Validate.isTrue(fromObject instanceof Integer, "Object fromObject must be of type Integer.class");
        String result = ((Integer) fromObject).toString();
        return result;
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.databinding.conversion.IConverter#getFromType()
     */
    public Object getFromType() {
        return Integer.class;
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.databinding.conversion.IConverter#getToType()
     */
    public Object getToType() {
        return String.class;
    }
}
