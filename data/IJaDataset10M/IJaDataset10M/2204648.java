package org.jhotdraw.draw;

/**
 * Options.
 *
 * @author  Werner Randelshofer
 * @version 1.0.1 2005-03-13 Fractional metrics and antialiased text turned on.
 *  <br>1.0 8. April 2004  Created.
 */
public class Options {

    /** Creates a new instance. */
    public Options() {
    }

    public static boolean isFractionalMetrics() {
        return true;
    }

    public static boolean isTextAntialiased() {
        return true;
    }
}
