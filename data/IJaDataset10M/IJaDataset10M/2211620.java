package edu.princeton.wordnet.wnscope.wizard;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * Utilities
 * 
 * @author <a href="mailto:bbou@ac-toulouse.fr">Bernard Bou</a>
 */
public class Utils {

    /**
	 * Center on screen
	 * 
	 * @param thisComponent
	 *            component to center
	 */
    public static void center(final Component thisComponent) {
        final Dimension thisScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final Dimension thisComponentSize = thisComponent.getSize();
        if (thisComponentSize.height > thisScreenSize.height) {
            thisComponentSize.height = thisScreenSize.height;
        }
        if (thisComponentSize.width > thisScreenSize.width) {
            thisComponentSize.width = thisScreenSize.width;
        }
        thisComponent.setLocation((thisScreenSize.width - thisComponentSize.width) / 2, (thisScreenSize.height - thisComponentSize.height) / 2);
    }
}
