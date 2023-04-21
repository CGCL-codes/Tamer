package ti.chimera.service;

import ti.chimera.*;
import javax.swing.*;

/**
 * The window manager service provides a way for other parts of the system
 * to display dialogs/docks, toolbars, and menubar entries.
 * 
 * @author Rob Clark
 * @version 0.1
 */
public abstract class WindowManager extends ti.chimera.Service {

    /**
   * Class Constructor.
   */
    public WindowManager() {
        super("window manager");
    }

    /**
   * This exception type is used to abort closing a dialog, if it is not 
   * closable for whatever reason.  It can, for example, be thrown by the 
   * dialog's close-runnable (see {@link Dialog#addCloseRunnable}).
   */
    public static class DialogNotClosableException extends RuntimeException {
    }

    /**
   * Set the mode.  The mode implements the actual display of the
   * user interface to the user.
   * 
   * @param mode         the service implementing the mode which
   *    realizes the display of dialogs/toolbars/menubar
   */
    public abstract void setMode(WindowMode mode);

    /**
   * Set the Look & Feel.  Swing supports multiple look&feels, depending
   * on the platform.  (For example: Metal, Motif, Windows, MacOSX, plus
   * any 3rd party L&F that the user has installed.)
   * 
   * @param lnfName      the full name of the class implementing the L&F
   */
    public abstract void setLookAndFeel(String lnfName);

    /**
   * Get a dialog with the specified title.  This method should be used,
   * rather than creating a <code>JDialog</code>, because this will behave
   * properly if the window manager is in <code>DESKTOP_MODE</code>.
   * 
   * @param title        the title of the dialog
   * @return a dialog
   */
    public abstract Dialog getDialog(String title);

    public abstract void addDock(Dock dock);

    public abstract void removeDock(Dock dock);

    public abstract void dockUpdated(Dock dock);

    /**
   * Add a tool-bar.
   * 
   * @param toolBar      the tool-bar to add
   */
    public abstract void addToolBar(JToolBar toolBar);

    /**
   * Remove a tool-bar.
   * 
   * @param toolBar      the tool-bar to remove
   */
    public abstract void removeToolBar(JToolBar toolBar);

    /**
   * Add a menu bar item.  The path of the action is "/" seperated "path" of
   * the action, such as "/File/Open".  Multiple actions with the same path
   * and name can exist, in which case when the user selects that entry from
   * the pull-down menus, the <code>actionPerformed</code> methods will be
   * in the order that the actions where added.  If the action is null, a
   * separator will be added.
   * 
   * @param path         which sub-menu the item should go under
   * @param a            the menu bar action to add.
   * @see #removeMenuBarItem
   */
    public abstract void addMenuBarItem(String path, Action a);

    /**
   * Remove a menu bar item.
   * 
   * @param path         which sub-menu the item should go under
   * @param a            the menu bar action to remove.
   * @see #addMenuBarItem
   */
    public abstract void removeMenuBarItem(String path, Action a);

    /**
   * Show or hide the user interface.
   * 
   * @param b            <code>true</code> to show the user interface, or 
   *   <code>false</code> to hide it
   */
    public abstract void setVisible(boolean b);

    /**
   * Show or hide the user interface.
   * 
   * @param b            <code>true</code> to show the user interface, or 
   *   <code>false</code> to hide it
   */
    public abstract boolean isVisible();
}
