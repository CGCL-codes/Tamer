package net.sourceforge.jpotpourri.jpotface.log4jlog;

/**
 * @author christoph_pickl@users.sourceforge.net
 */
public final class De {

    private static final boolean DEBUG_ENABLED = false;

    private De() {
    }

    public static void bug(final String msg) {
        if (DEBUG_ENABLED == true) {
            System.out.println("DEBUG: " + msg);
        }
    }
}
