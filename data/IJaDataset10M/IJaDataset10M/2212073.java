package net.sf.antdoc;

/**
 */
public class AntdocLogger {

    /** the singleton instance */
    private static AntdocLogger _instance = null;

    /** Constructor. */
    private AntdocLogger() {
    }

    /** Constructor. */
    public static AntdocLogger getInstance() {
        if (_instance == null) {
            _instance = new AntdocLogger();
        }
        return _instance;
    }

    /**
     * Logs the given message using the current locale.
     */
    public void message(String rc) {
        System.out.println(System.currentTimeMillis() + ": " + rc);
    }
}
