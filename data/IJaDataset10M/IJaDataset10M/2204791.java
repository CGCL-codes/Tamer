package com.jgoodies.animation.tutorial;

import java.awt.Component;
import java.awt.Dimension;

/**
 * Consists only of static convenience methods used throughout
 * the examples of the JGoodies Animation tutorial.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.1 $
 */
public final class TutorialUtils {

    private TutorialUtils() {
    }

    /**
     * Locates the given component on the screen's center.
     * 
     * @param component   the component to be centered
     */
    public static void locateOnOpticalScreenCenter(Component component) {
        Dimension paneSize = component.getSize();
        Dimension screenSize = component.getToolkit().getScreenSize();
        component.setLocation((screenSize.width - paneSize.width) / 2, (int) ((screenSize.height - paneSize.height) * 0.45));
    }
}
