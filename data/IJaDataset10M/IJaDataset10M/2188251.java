package geovista.jts.coordsys;

import geovista.jts.util.Blackboard;

/** 
 * Utility class.
 */
public class CoordinateSystemSupport {

    private static final String ENABLED_KEY = CoordinateSystemSupport.class.getName() + " - ENABLED";

    public static boolean isEnabled(Blackboard blackboard) {
        return blackboard.get(ENABLED_KEY, false);
    }

    public static void setEnabled(boolean enabled, Blackboard blackboard) {
        blackboard.put(ENABLED_KEY, enabled);
    }
}
