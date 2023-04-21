package org.xiaoniu.suafe.dialogs;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

/**
 * Parent class to all dialogs. Provides common functions useful in all
 * dialogs.
 * 
 * @author Shaun Johnson
 */
public final class DialogUtil {

    /**
	 * Center the specified component on the screen.
	 * 
	 * @param component Component to be centered.
	 */
    public static void center(Component component) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - component.getWidth()) / 2;
        int y = (screenSize.height - component.getHeight()) / 2;
        component.setLocation(x, y);
    }

    /**
	 * Center the specivied component on the parent.
	 * 
	 * @param parent Parent component
	 * @param child Child component
	 */
    public static void center(Component parent, Component child) {
        Dimension parentSize = parent.getSize();
        Point parentLocation = parent.getLocation();
        int x = (parentSize.width - child.getWidth()) / 2;
        int y = (parentSize.height - child.getHeight()) / 2;
        child.setLocation(parentLocation.x + x, parentLocation.y + y);
    }
}
