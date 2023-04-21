package mobac.program;

import java.io.InputStream;
import java.util.Properties;
import mobac.Main;
import mobac.utilities.GUIExceptionHandler;
import mobac.utilities.Utilities;

public class ProgramInfo {

    public static String PROG_NAME = "Mobile Atlas Creator";

    public static String PROG_NAME_SHORT = "MOBAC";

    private static String VERSION = null;

    private static String SVN_REVISION = "unknown";

    private static String userAgent = "";

    /**
	 * Show or hide the detailed revision info in the main windows title
	 */
    private static boolean titleHideRevision = false;

    public static void initialize() {
        InputStream propIn = Main.class.getResourceAsStream("mobac.properties");
        try {
            Properties props = new Properties();
            props.load(propIn);
            VERSION = props.getProperty("mobac.version");
            titleHideRevision = Boolean.parseBoolean(props.getProperty("mobac.revision.hide", "false"));
            System.getProperties().putAll(props);
        } catch (Exception e) {
            String msg = "Error reading mobac.properties";
            GUIExceptionHandler.processFatalExceptionSimpleDialog(msg, e);
        } finally {
            Utilities.closeStream(propIn);
        }
        propIn = Main.class.getResourceAsStream("mobac-rev.properties");
        try {
            String rev;
            if (propIn != null) {
                Properties props = new Properties();
                props.load(propIn);
                rev = props.getProperty("mobac.revision");
                SVN_REVISION = Integer.toString(Utilities.parseSVNRevision(rev));
            } else {
                rev = System.getProperty("mobac.revision.fallback");
                SVN_REVISION = Integer.toString(Utilities.parseSVNRevision(rev)) + " exported";
            }
        } catch (Exception e) {
            Logging.LOG.error("Error reading mobac-rev.properties", e);
        } finally {
            Utilities.closeStream(propIn);
        }
        userAgent = PROG_NAME_SHORT + "/" + (getVersion().replaceAll(" ", "_"));
    }

    public static String getVersion() {
        if (VERSION != null) return VERSION; else return "UNKNOWN";
    }

    public static String getRevisionStr() {
        return SVN_REVISION;
    }

    public static String getVersionTitle() {
        String title = PROG_NAME;
        if (PROG_NAME_SHORT != null) title += " (" + PROG_NAME_SHORT + ") "; else title += " ";
        if (VERSION != null) {
            title += getVersion();
        } else title += "unknown version";
        return title;
    }

    public static String getCompleteTitle() {
        String title = getVersionTitle();
        if (!titleHideRevision) title += " (" + SVN_REVISION + ")";
        return title;
    }

    public static String getUserAgent() {
        return userAgent;
    }
}
