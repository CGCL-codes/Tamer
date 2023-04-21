package org.jlense.uiworks.internal.window;

import org.jlense.uiworks.action.MenuBarManager;
import org.jlense.uiworks.action.StatusLineManager;
import org.jlense.uiworks.action.ToolBarManager;
import org.jlense.uiworks.workbench.IActionBars;
import org.jlense.uiworks.workbench.IPerspectiveDescriptor;
import org.jlense.uiworks.workbench.IWorkbenchPage;
import org.jlense.uiworks.workbench.IWorkbenchWindow;

/**
 * Defines methods common to internal implementation of the IWorkbenchWindow 
 * interface.
 * 
 * @see WorkbenchWindow
 * @see WorkbenchDialog
 * 
 * @author ted stockwell
 */
public interface IInternalWorkbenchWindow extends IWorkbenchWindow {

    /**
     * Closes the denoted page.
     * 
     * @param in the page to close.
     * @param save true if the page state should be serialized.
     */
    public boolean closePage(IWorkbenchPage in, boolean save);

    /**
     * Returns the action bars for this window.
     */
    public IActionBars getActionBars();

    /**
     * Returns the menu bar manager for this window (if it has one).
     *
     * @return the menu bar manager, or <code>null</code> if
     *   this window does not have a menu bar
     */
    public MenuBarManager getMenuBarManager();

    /**
     * Returns the status line manager for this window (if it has one).
     *
     *
     * @return the status line manager, or <code>null</code> if
     *   this window does not have a status line
     */
    public StatusLineManager getStatusLineManager();

    /**
     * Returns the tool bar manager for this window (if it has one).
     *
     * @return the tool bar manager, or <code>null</code> if
     *   this window does not have a tool bar
     */
    public ToolBarManager getToolBarManager();

    /**
     * Fires perspective reset event
     */
    public void firePerspectiveReset(IWorkbenchPage page, IPerspectiveDescriptor perspective);

    /**
     * Fires perspective changed event
     */
    public void firePerspectiveChanged(IWorkbenchPage page, IPerspectiveDescriptor perspective, String changeId);

    /**
     * Fires perspective activated
     */
    public void firePerspectiveActivated(IWorkbenchPage page, IPerspectiveDescriptor perspective);

    /**
     * Update the visible action sets. This method is typically called
     * from a page when the user changes the visible action sets
     * within the prespective.  
     */
    public void updateActionSets();

    /**
     * update the action bars.
     */
    public void updateActionBars();

    /**
     * update the shortcut bar.
     */
    public void updateShortcutBar();

    /**
     * Updates the window title.
     */
    public void updateTitle();
}
