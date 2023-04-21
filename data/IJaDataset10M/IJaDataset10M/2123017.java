package ingenias.editor;

/**
 * To store preferences which cannot be modified from the GUI and that have no use for the
 * end-user
 * @author jjgs
 *
 */
public class StaticPreferences {

    private static boolean testing = false;

    public static void setTesting(boolean testing) {
        StaticPreferences.testing = testing;
    }

    public static boolean isTesting() {
        return testing;
    }
}
