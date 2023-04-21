package ti.plato.eclipse.log.analysis.u;

/**
 * 
 *
 * @author alex.k@ti.com
 */
public class AnalLoggerUtil {

    private static final String HEADER = AnalLoggerUtil.class.getSimpleName() + ": ";

    private static final String ERROR_HEADER = "ERROR " + HEADER;

    private static final String NYI_HEADER = "NYI " + HEADER;

    private static final String INFO_HEADER = "INFO " + HEADER;

    private AnalLoggerUtil() {
    }

    /**
	 * @param msg
	 */
    public static void logError(String msg) {
        logError(msg, null);
    }

    /**
	 * if t == null, do not print stack trace
	 * @param msg
	 * @param t
	 *
	 * @author alex.k@ti.com
	 */
    public static void logError(String msg, Throwable t) {
        System.err.println(ERROR_HEADER + msg);
        if (t == null) {
            return;
        }
        t.printStackTrace(System.err);
    }

    /**
	 * @author alex.k@ti.com
	 */
    public static void NYI() {
        Throwable t = new Throwable();
        StackTraceElement[] elements = t.getStackTrace();
        System.out.println(NYI_HEADER + elements[1]);
    }

    /**
	 * @param msg
	 *
	 * @author alex.k@ti.com
	 */
    public static void logInfo(String msg) {
        System.out.println(INFO_HEADER + msg);
    }

    /**
	 * @param msg
	 * @param isLogUsageInfo - if true, log EclipseLogAnalysisEngineOptions usage
	 * @param isLogStackTrace
	 */
    public static void fatalError(String msg, boolean isLogUsageInfo, boolean isLogStackTrace) {
        Throwable t = null;
        if (isLogStackTrace) {
            t = new Throwable(msg);
        }
        logError(msg, t);
        if (isLogUsageInfo) {
            logInfo(EclipseLogAnalysisEngineOptions.usage());
        }
        System.exit(1);
    }
}
