package org.jdesktop.jdic.misc.impl;

import com.apple.cocoa.application.*;
import com.apple.cocoa.foundation.*;
import com.apple.eawt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.jdesktop.jdic.misc.DockMenu;

/**
 *  The Mac OS X implementation of the Dock Menu. This is most likely
 *  the only implementation, as no other platform as the concept of a
 *  dock menu. In the future this may change.
 *
 * @author     joshua@marinacci.org
 * @created    April 8, 2005
 */
public class MacOSXDockMenu extends DockMenu {

    /**
	 *  Do not call. Use the DockMenu.newInstance() factory method
	 *  instead.
	 */
    public MacOSXDockMenu() {
    }

    /**
	 *  Set the JMenu you want exported to the dock menu. You can change
	 *  the menu items later and the changes will be reflected in this
	 *  dock menu.
	 *
	 * @param  menu  The JMenu to show on the dock.
	 */
    public void setMenu(JMenu menu) {
        Delegate delegate = new Delegate(menu);
        NSApplication app = NSApplication.sharedApplication();
        app.setDelegate(delegate);
    }

    /**
	 *  Implementation detail. Do not use!
	 *
	 * @author     joshua@marinacci.org
	 * @created    April 8, 2005
	 */
    class Delegate extends ApplicationAdapter {

        JMenu menu;

        /**
		 *  Constructor for the Delegate object
		 *
		 * @param  menu  Description of the Parameter
		 */
        public Delegate(JMenu menu) {
            this.menu = menu;
        }

        private final NSSelector actionSel = new NSSelector("doClick", new Class[] {});

        /**
		 *  Description of the Method
		 *
		 * @param  sender  Description of the Parameter
		 * @return         Description of the Return Value
		 */
        public NSMenu applicationDockMenu(NSApplication sender) {
            System.out.println("creating dock menu");
            NSMenu dockMenu = dockMenu = new NSMenu();
            for (int i = 0; i < menu.getMenuComponentCount(); i++) {
                JMenuItem item = (JMenuItem) menu.getMenuComponent(i);
                NSMenuItem nsitem = new NSMenuItem(item.getText(), actionSel, "");
                nsitem.setTarget(item);
                dockMenu.addItem(nsitem);
            }
            return dockMenu;
        }
    }
}
