package net.sourceforge.mazix.components.constants;

import static java.lang.System.getProperty;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableSet;
import static java.util.logging.Level.CONFIG;
import static java.util.logging.Level.FINE;
import static java.util.logging.Level.FINER;
import static java.util.logging.Level.FINEST;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;
import static net.sourceforge.mazix.components.constants.CharacterConstants.DOT_CHAR;
import java.io.File;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import net.sourceforge.mazix.components.utils.date.UnmodifiableDate;

/**
 * The class which stores all useful constants as Strings.
 *
 * @author Benjamin Croizet (graffity2199@yahoo.fr)
 * @since 0.7
 * @version 0.7
 */
public final class CommonConstants {

    /** The blank string. */
    public static final String BLANK_STRING = "";

    /** The current directory. */
    public static final File CURRENT_DIRECTORY = new File(DOT_CHAR);

    /** The double dots used for path. */
    public static final String DOUBLE_DOTS = DOT_CHAR + DOT_CHAR;

    /** The system file separator. */
    public static final String FILE_SEPARATOR = getProperty("file.separator");

    /** The first prime number used by Eclipse for implementing the hashcode() method. */
    public static final int HASHCODE_PRIME_NUMBER1 = 1231;

    /** The second prime number used by Eclipse for implementing the hashcode() method. */
    public static final int HASHCODE_PRIME_NUMBER2 = 1237;

    /** The system line separator. */
    public static final String LINE_SEPARATOR = getProperty("line.separator");

    /** The {@link Set} containing all log levels of the java logging API. */
    public static final Set<Level> LOG_LEVELS_SET = unmodifiableSet(new HashSet<Level>(asList(new Level[] { SEVERE, WARNING, INFO, CONFIG, FINE, FINER, FINEST })));

    /**
     * The -1 code which is often used when returning a method (for error, or end of file for
     * instance).
     */
    public static final int MINUS_ONE_RETURN_CODE = -1;

    /** The date which represents the current instant time. */
    public static final Date NOW = new UnmodifiableDate();

    /** The system path separator. */
    public static final String PATH_SEPARATOR = getProperty("path.separator");

    /** The number of millisecond in one second. */
    public static final int SECOND_MILLI = 1000;

    /**
     * Private constructor to prevent from instantiation.
     *
     * @since 0.7
     */
    private CommonConstants() {
        super();
    }
}
