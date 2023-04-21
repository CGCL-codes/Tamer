package cbdp.server.contextreasoner.impl.util;

public class DebugUtils {

    private static final String CBDP_DEBUG_KEY = "cbdp.contextreasoner.debug";

    private static final String CONTEXT_REASONER_DEBUG_KEY = "cbdp.contextreasoner.debug";

    private static final boolean inDebugMode;

    static {
        inDebugMode = System.getProperty(CBDP_DEBUG_KEY, "false").equals("true") || System.getProperty(CONTEXT_REASONER_DEBUG_KEY, "false").equals("true");
    }

    public static void printIfInDebugModel(final String text) {
        if (inDebugMode) {
            print(text);
        }
    }

    public static void printlnIfInDebugModel() {
        if (inDebugMode) {
            println();
        }
    }

    public static void printlnIfInDebugModel(final String text) {
        if (inDebugMode) {
            println(text);
        }
    }

    public static boolean isInDebugMode() {
        return inDebugMode;
    }

    public static void print(final String text) {
        System.out.print(text);
    }

    public static void println() {
        System.out.println();
    }

    public static void println(final String text) {
        System.out.println(text);
    }
}
