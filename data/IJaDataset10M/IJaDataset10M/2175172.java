package jbuzzer.io.parser;

import jbuzzer.RemovableLogging;

/**
 * <p>
 * Logging facility for this package allowing to remove 
 * logging code by the flip of a switch (see {@link jbuzzer.RemovableLogging}). 
 * </p>
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann</a>
 *
 */
public final class Debug extends RemovableLogging {

    private static RemovableLogging instance = null;

    public static RemovableLogging getInstance() {
        if (instance == null) {
            instance = new Debug();
        }
        return instance;
    }

    private Debug() {
        super();
    }
}
