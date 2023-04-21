package tr.appl;

import java.awt.Color;
import java.awt.Dimension;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.openide.util.Utilities;

/**
 * Application constants.
 *
 * @author Jeremy Moore (jeremyimoore@yahoo.com.au)
 */
public class Constants {

    /** Application title */
    public static final String TITLE = "ThinkingRock";

    /** String value for the company. */
    public static final String COMPANY = "Avente Pty. Ltd.";

    /** String value for the web site address. */
    public static final String WEB_SITE = "http://www.thinkingrock.com.au";

    /**
     * Application version. For all versions that are not pre-release, the
     * format is: major.minor.revision with revision greater than 0,
     * e.g.  2.0.1
     * For all pre-release versions it is in format: major.minor.revision.type.n
     * where revision is 0,
     * e.g.  2.0.0.beta.1
     */
    public static final String VERSION = "2.0.1";

    /** String URL of the file on the web site that contains the latest version. */
    public static final String VERSION_FILE_URL = "http://www.thinkingrock.com.au/version.php";

    /** String value for the preference path. */
    public static final String PREFS_PATH = "ThinkingRock";

    /** String value of the minus sign unicode character. */
    public static final String STRING_MINUS = String.valueOf('−');

    /** String value of the plus sign unicode character. */
    public static final String STRING_PLUS = String.valueOf('+');

    /** Date format used. */
    public static final DateFormat DATE_FORMAT_SHORT = DateFormat.getDateInstance(DateFormat.SHORT);

    public static final DateFormat DATE_FORMAT_MEDIUM = DateFormat.getDateInstance(DateFormat.MEDIUM);

    public static final DateFormat DATE_FORMAT_LONG = DateFormat.getDateInstance(DateFormat.LONG);

    public static final DateFormat DATE_FORMAT_FULL = DateFormat.getDateInstance(DateFormat.FULL);

    public static final DateFormat DATE_FORMAT_FIXED = new SimpleDateFormat("EEE, d MMM yyyy");

    public static final DateFormat DATE_TIME_FORMAT_FIXED = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");

    /** Date format for timestamps. */
    public static final DateFormat DF_TIMESTAMP = new SimpleDateFormat("yyyyMMddHHmmss");

    /** Maximum number of rows to display for combo box lists */
    public static final int COMBO_MAX_ROWS = 30;

    /** The special ID number for the root project. */
    public static final int ID_ROOT_ALL = 0;

    /** The special ID number for the root project for projects. */
    public static final int ID_ROOT_PROJECTS = 1;

    /** The special ID number for the root project for single actions. */
    public static final int ID_ROOT_ACTIONS = 2;

    /** The special ID number for the root project for template projects. */
    public static final int ID_ROOT_TEMPLATES = 3;

    /** The special ID number for the root project for future projects. */
    public static final int ID_ROOT_FUTURES = 4;

    /** The special ID number for filter objects representing the All choice. */
    public static final int ID_FILTER_ALL = 5;

    /** The special ID number for filter objects representing the Multiple choice. */
    public static final int ID_FILTER_MULTIPLE = 6;

    /** The special ID number for filter objects representing the Edit Multiple choice. */
    public static final int ID_FILTER_MULTIPLE_EDIT = 7;

    /** The maximum special ID number. Normal ID numbers start after this number. */
    public static final int ID_MAX_SPECIAL = 100;

    /** Icon button type */
    public static final String BUTTON_TYPE_ICON = "icon";

    /** Text button type */
    public static final String BUTTON_TYPE_TEXT = "text";

    /** Toolbar button type */
    public static final String BUTTON_TYPE_TOOLBAR = "toolbar";

    /** Button type property **/
    public static final String BUTTON_TYPE = "JButton.buttonType";

    /** Icon button type value **/
    public static final String ICON = "icon";

    /** Button size for toolbar buttons. **/
    public static final Dimension TOOLBAR_BUTTON_SIZE = Utilities.isMac() ? new Dimension(28, 28) : new Dimension(24, 24);

    /** Colour of dates past*/
    public static final Color COLOR_DATE_PAST = Color.red;

    /** Colour of dates today */
    public static final Color COLOR_DATE_TODAY = Color.blue;

    /** Colour of dates future week */
    public static final Color COLOR_DATE_WEEK = new Color(0, 102, 0);
}
