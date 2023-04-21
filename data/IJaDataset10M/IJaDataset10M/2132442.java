package org.cantaloop.tools.conversion;

/**
 * Converts a object of type <code>java.lang.String</code> into
 * a object of type <code>java.lang.Integer</code>. The converter
 * respects the locale-specific representation of a integer number.
 *
 * @todo <text>implement this converter!</text>
 * @created Thu Dec  6 22:09:21 2001
 *
 * @author <a href="mailto:stefan@cantaloop.org">Stefan Heimann</a>
 * @version @version@ ($Revision: 1.3 $)
 */
public class StringToLongConverter extends AbstractConverter {

    private static final StringToLongConverter s_instance = new StringToLongConverter();

    public static StringToLongConverter getInstance() {
        return s_instance;
    }

    public StringToLongConverter() {
        super(new Class[] { String.class }, new Class[] { Long.class });
    }

    public Object convert(Object o, Class type) throws ConversionException {
        checkTypes(o, type);
        if (o instanceof String) {
            return convert((String) o, type);
        }
        throw new org.cantaloop.tools.misc.BugException();
    }

    public Object convert(String s, Class type) throws ConversionException {
        try {
            return new Long(s);
        } catch (NumberFormatException e) {
            throw new ConversionException("Cannot convert String '" + s + "' to java.lang.Long");
        }
    }
}
