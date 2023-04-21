package edu.gatech.ealf;

import java.awt.Component;
import java.awt.Container;
import java.util.HashMap;
import java.util.Map;
import edu.gatech.ealf.magiclenseplaf.ZoomHelper;

public class GlassPaneManager {

    private static Map<Container, Map<Component, Boolean>> glassMap = new HashMap<Container, Map<Component, Boolean>>();

    private static Map<Component, ZoomHelper> glassComponentsHome = new HashMap<Component, ZoomHelper>();

    /**
     * Registers interest in a GlassPane for a particular component
     * @param glass
     * @param interestedComponent
     * @return
     */
    public static Map<Component, Boolean> register(Container glass, Component interestedComponent) {
        Map<Component, Boolean> glassRecord = glassMap.get(glass);
        if (glassRecord == null) {
            glassRecord = new HashMap<Component, Boolean>();
            glassMap.put(glass, glassRecord);
        }
        glassRecord.put(interestedComponent, false);
        return glassRecord;
    }

    /**
     * Unregisters interest in a GlassPane for a particular component
     * @param glass
     * @param interestedComponent
     */
    public static void unregister(Container glass, Component interestedComponent) {
        Map<Component, Boolean> glassRecord = glassMap.get(glass);
        if (glassRecord == null) {
            glassRecord = new HashMap<Component, Boolean>();
            glassMap.put(glass, glassRecord);
        }
        glassRecord.remove(interestedComponent);
    }

    /**
     * Show a glass pane and update the glass pane's interested
     * It also tells the glass to repaint itself
     * @param glass
     * @param interestedComponent
     */
    public static void show(Container glass, Component interestedComponent) {
        Map<Component, Boolean> glassRecord = glassMap.get(glass);
        if (glassRecord == null) {
            glassRecord = register(glass, interestedComponent);
        }
        glassRecord.put(interestedComponent, true);
        if (!glass.isVisible()) {
            glass.setVisible(true);
        }
        glass.repaint();
    }

    /**
     * Request the glass pane to be hidden.
     * It will only be hidden if all interested components want it hidden
     * It also tells the glass to repaint itself
     * @param glass
     * @param interestedComponent
     */
    public static void hide(Container glass, Component interestedComponent) {
        Map<Component, Boolean> glassRecord = glassMap.get(glass);
        if (glassRecord == null) {
            glassRecord = register(glass, interestedComponent);
        }
        glassRecord.put(interestedComponent, false);
        boolean oneTrue = false;
        for (Boolean b : glassRecord.values()) {
            oneTrue = oneTrue || b;
        }
        if (!oneTrue) {
            glass.setVisible(false);
        }
        glass.repaint();
    }

    public static void registerComponentHome(Component c, ZoomHelper home) {
        glassComponentsHome.put(c, home);
    }

    public static ZoomHelper unregisterComponentHome(Component c) {
        return glassComponentsHome.remove(c);
    }
}
