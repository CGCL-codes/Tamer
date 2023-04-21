package co.edu.uniquindio.utils.logger;

import org.apache.log4j.Level;

/**
 * The <code>FinestLevel</code> class defined nivel finest in logger
 * 
 * @author Daniel Pelaez
 * @author Hector Hurtado
 * @author Daniel Lopez
 * @version 1.0, 17/06/2010
 * @since 1.0
 * 
 */
public final class FinestLevel extends Level {

    /**
	 * Serial
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * Number value
	 */
    public static final int FINEST_INT = FineLevel.FINE_INT - 1000;

    /**
	 * String value
	 */
    private static String FINEST_STR = "FINE";

    /**
	 * Finest level
	 */
    public static final FinestLevel FINEST = new FinestLevel(FINEST_INT, FINEST_STR, 7);

    /**
	 * Creates a FinestLevel
	 * 
	 * @param level
	 *            Number level
	 * @param strLevel
	 *            String level
	 * @param syslogEquiv
	 */
    protected FinestLevel(int level, String strLevel, int syslogEquiv) {
        super(level, strLevel, syslogEquiv);
    }

    /**
	 * Convert the String argument to a level. If the conversion fails then this
	 * method returns {@link #FINE}.
	 */
    public static Level toLevel(String sArg) {
        return (Level) toLevel(sArg, FinestLevel.FINEST);
    }

    /**
	 * Convert the String argument to a level. If the conversion fails, return
	 * the level specified by the second argument, i.e. defaultValue.
	 */
    public static Level toLevel(String sArg, Level defaultValue) {
        if (sArg == null) {
            return defaultValue;
        }
        String stringVal = sArg.toUpperCase();
        if (stringVal.equals(FINEST_STR)) {
            return FinestLevel.FINEST;
        }
        return Level.toLevel(sArg, (Level) defaultValue);
    }

    /**
	 * Convert an integer passed as argument to a level. If the conversion
	 * fails, then this method returns {@link #FINEST}.
	 * */
    public static Level toLevel(int i) throws IllegalArgumentException {
        if (i == FINEST_INT) {
            return FinestLevel.FINEST;
        } else {
            return Level.toLevel(i);
        }
    }
}
