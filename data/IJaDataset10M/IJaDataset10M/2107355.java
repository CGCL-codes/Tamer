package de.cologneintelligence.fitgoodies.parsers;

import java.util.HashMap;

/**
 * Singleton class which manages registered parsers.
 *
 * @see SetupFixture SetupFixture
 * @author jwierum
 * @version $Id: ParserHelper.java 46 2011-09-04 14:59:16Z jochen_wierum $
 */
public final class ParserHelper {

    private final HashMap<Class<?>, Parser<?>> parsers = new HashMap<Class<?>, Parser<?>>();

    private static ParserHelper instance;

    private ParserHelper() {
        registerParser(new BigIntegerParser());
        registerParser(new BigDecimalParser());
        registerParser(new ObjectParser());
        registerParser(new ScientificDoubleParser());
    }

    /**
	 * Resets the singleton.
	 */
    public static void reset() {
        instance = null;
    }

    /**
	 * Returns an instance of <code>ParserHelper</code>.
	 * @return instance of <code>ParserHelper</code>
	 */
    public static ParserHelper instance() {
        if (instance == null) {
            instance = new ParserHelper();
        }
        return instance;
    }

    /**
	 * Registers a parser.
	 * @param <T> destination type which the parser processes
	 * @param type destination type which the parser processes
	 * @param parser parser which converts <code>String</code> to
	 * 		<code>&lt;T&gt;</code>
	 */
    public <T> void registerParser(final Class<T> type, final Parser<T> parser) {
        parsers.put(type, parser);
    }

    /**
	 * Parses <code>s</code> into an instance of a class of the type
	 * <code>type</code>. If no suitable parser could be found,
	 * <code>null</code> is returned.
	 * @param s the string to parse
	 * @param type the destination type
	 * @param parameter <code>String</code> which holds the column parameter
	 * @return parsed <code>s</code> as class of the type <code>type</code> or null
	 * @throws Exception exception which is thrown by a parser. Propagated to fit.
	 */
    public Object parse(final String s, final Class<?> type, final String parameter) throws Exception {
        Parser<?> p = parsers.get(type);
        if (p != null) {
            return p.parse(s, parameter);
        } else {
            return null;
        }
    }

    /**
	 * Registers a parser. The type is automatically determined by asking
	 * 		<code>parser</code> for its type.
	 * @param <T> destination type which the parser processes
	 * @param parser parser parser which converts <code>String</code> to
	 * 		<code>&lt;T&gt;</code>
	 */
    public <T> void registerParser(final Parser<T> parser) {
        registerParser(parser.getType(), parser);
    }
}
