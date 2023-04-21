package org.jdesktop.jdic.misc;

import javax.swing.JMenu;

/**
 *  <p>Displays a JMenu as a DockMenu, meaning the menu you get when you
 *  right click, control click, or press and hold on the application's
 *  Dock icon. The DockMenu is currently only supported on Mac OS X.
 *  <b>Note: the action listeners on the dock menu will not be called
 *  from the Swing event thread. If you want to manipulate Swing
 *  objects from your event handlers you must do so using
 *  <i>SwingUtilities.invokeLater().</i></b></p>
 *
 
 <p><b>Example:</b> To install a JMenu as the Dock menu:</p>
 
 <pre><code>
	JMenu dock_menu = new JMenu("Dock");
	dock_menu.add(new JMenuItem("item 1"));
	dock_menu.add(new JMenuItem("item 2"));
	DockMenu dm = DockMenu.newInstance();
	dm.setMenu(dock_menu);
 </code></pre>
 
 <p>Note that the {@link DockMenu#newInstance() newInstance()}method <i><b>must be called after at least one Swing/AWT window or frame has already been shown</b></i>, or else the AWT and Cocoa event threads may clash and cause your application to lock up.
 </p>
 
 
 * @author     Joshua Marinacci <a href="mailto:joshua@marinacci.org">joshua@marinacci.org</a>
 * @created    April 8, 2005
 */
public class DockMenu {

    private static DockMenu _dock_menu;

    /**
	 *  Constructor for the DockMenu object
	 */
    protected DockMenu() {
    }

    /**
	 *  Set the JMenu to be used as the Dock menu. Changes to the items
	 *  within this menu will be automatically reflected in the dock
	 *  menu. Sub menus are not currently supported.
	 *
	 * @param  menu  The new menu value
	 */
    public void setMenu(JMenu menu) {
        System.out.println("the dock menu is only supported on Mac OS X");
    }

    /**
	 *  Get a new instance of a DockMenu for the appropriate platform. If
	 *  an implementation is not available (only OSX is currently
	 *  supported) then it will return a dummy implemenation that does
	 *  nothing.
	 *
	 * @return    DockMenu for the current platform.
	 */
    public static DockMenu newInstance() {
        if (_dock_menu == null) {
            String os_name = System.getProperty("os.name");
            System.out.println("os name = " + os_name);
            if (os_name.toLowerCase().startsWith("mac os x")) {
                loadMac();
            } else {
                _dock_menu = new DockMenu();
            }
        }
        return _dock_menu;
    }

    /**
	 *  implementation detail
	 */
    private static void loadMac() {
        System.out.println("creating mac dock menu");
        try {
            _dock_menu = (DockMenu) DockMenu.class.forName("org.jdesktop.jdic.misc.impl.MacOSXDockMenu").newInstance();
        } catch (Throwable ex) {
            System.out.println("couldn't load the mac version");
            System.out.println("" + ex.getMessage());
            ex.printStackTrace();
            _dock_menu = new DockMenu();
        }
    }

    /** Returns true if the current platform supports Dock Menus */
    public boolean isDockMenuSupported() {
        return false;
    }
}
