package com.cube42.echoverse.rmilogin;

import com.cube42.util.gui.SubSystemControlPanel;

/**
 * Main panel for controlling the RMILoginManager
 *
 * @author Matt Paulin
 * @version $Id: RMILoginManagerControlPanel.java,v 1.1.1.1 2002/10/15 00:42:50 zer0wing Exp $
 */
public class RMILoginManagerControlPanel extends SubSystemControlPanel {

    /**
     * Constructs the RMILoginManagerControlPanel
     */
    public RMILoginManagerControlPanel() {
        super(RMILoginManager.SUBSYSTEM_ID);
    }

    /**
     * Method called when the panel needs to initialize itself.  All GUI
     * setup should be done here
     */
    public void initialize() {
    }

    /**
     * Method called whenever the panel is reconnected to the subsystem
     */
    public void reconnected() {
    }

    /**
     * Method called whenever the panel needs to be updated with new information
     */
    public void update() {
    }

    /**
     * Method called when the panel is to be enabled
     */
    public void enablePanel() {
    }

    /**
     * Method called when the panel is to be disabled
     */
    public void disablePanel() {
    }
}
