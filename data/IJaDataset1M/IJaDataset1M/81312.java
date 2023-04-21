package org.mars_sim.msp.ui.swing.tool;

import org.mars_sim.msp.ui.swing.MainDesktopPane;
import javax.swing.*;

/** 
 * The ToolWindow class is an abstract UI window for a tool.
 * Particular tool windows should be derived from this.
 */
public abstract class ToolWindow extends JInternalFrame {

    protected String name;

    protected MainDesktopPane desktop;

    protected boolean opened;

    /** 
     * Constructor 
     *
     * @param name the name of the tool
     * @param desktop the main desktop.
     */
    public ToolWindow(String name, MainDesktopPane desktop) {
        super(name, true, true, false, false);
        this.name = name;
        this.desktop = desktop;
        opened = false;
        addInternalFrameListener(new ToolFrameListener());
    }

    /** 
     * Gets the tool name.
     *
     * @return tool name
     */
    public String getToolName() {
        return name;
    }

    /** 
     * Checks if the tool window has previously been opened.
     *
     * @return true if tool window has previously been opened.
     */
    public boolean wasOpened() {
        return opened;
    }

    /** 
     * Sets if the window has previously been opened.
     *
     * @param opened true if previously opened.
     */
    public void setWasOpened(boolean opened) {
        this.opened = opened;
    }

    /**
     * Update window.
     */
    public void update() {
    }

    /**
	 * Prepares tool window for deletion.
	 */
    public void destroy() {
    }
}
