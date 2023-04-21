package fitgoodies.parsers;

/**
 * Parser which is able to convert a string into an <code>Object</code>.
 * This class provides a fallback mechanism. If a destination Object has just
 * the type object, the parser compares their <code>toString()</code> value.
 *
 * @author jwierum
 * @version $Id: ObjectParser.java 185 2009-08-17 13:47:24Z jwierum $
 */
public class ObjectParser implements Parser<Object> {

    /**
	 * Returns the destination class which is managed by this parser.
	 * @return Object.class
	 */
    @Override
    public final Class<Object> getType() {
        return Object.class;
    }

    /**
	 * Parses a string and converts it into an <code>object</code>.
	 * @param s <code>String</code> which will be converted
	 * @param parameter ignored
	 * @return <code>s.toString()</code>
	 */
    @Override
    public final Object parse(final String s, final String parameter) {
        return s;
    }
}
