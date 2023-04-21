package net.sf.irunninglog.canonical;

/**
 * Canonical object representing a <em>Runner</em>.  This class provides a
 * number of constants which define a loose contract as to what a
 * <em>Runner</em> is within the system.  This contract serves as a way to
 * allow different application layers to understand and interact with
 * generic data (e.g. data transfer objects).
 *
 * @author <a href="mailto:allan_e_lewis@yahoo.com">Allan Lewis</a>
 * @version $Revision: 1.2 $ $Date: 2005/12/18 18:58:20 $
 * @since iRunningLog 1.0
 */
public final class Runner extends Canonical {

    /** Canonical ID for Runner objects. */
    public static final String CANONICAL_ID = "Runner";

    /** Does not expose a constructor. */
    private Runner() {
    }

    /** The value used to identify the Runner's <em>name</em> field. */
    public static final String FIELD_USERNAME = "username";

    /** The value used to identify the Runner's <em>email</em> field. */
    public static final String FIELD_EMAIL = "email";

    /** The value used to identify the Runner's <em>units</em> field. */
    public static final String FIELD_UNITS = "units";

    /** The value used to identify the Runner's <em>start day</em> field. */
    public static final String FIELD_START_DAY = "startDay";
}
