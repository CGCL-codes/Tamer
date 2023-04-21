package spin.examples;

import javax.swing.SwingUtilities;

/**
 * Utility class to assert correct thread handling.
 */
public class Assert {

    /**
	 * No instance.
	 */
    private Assert() {
    }

    /**
	 * Must be executed on the EDT.
	 */
    public static void onEDT() {
        if (!SwingUtilities.isEventDispatchThread()) {
            throw new Error("assertion failed: on EDT");
        }
    }

    /**
	 * Must be executed off the EDT.
	 */
    public static void offEDT() {
        if (SwingUtilities.isEventDispatchThread()) {
            throw new Error("assertion failed: off EDT");
        }
    }
}
