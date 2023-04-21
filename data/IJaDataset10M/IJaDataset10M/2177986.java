package org.dmonix.oracledocgen.gui.menu;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.dmonix.oracledocgen.gui.MainFrame;
import org.dmonix.oracledocgen.gui.objectframes.UserFrame;
import org.dmonix.oracledocgen.session.PreferencesObject;
import org.dmonix.oracledocgen.session.SessionObject;
import org.dmonix.util.SwingWorker;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: dmonix.org
 * </p>
 * 
 * @author Peter Nerg
 * @version 1.0
 */
public class UserMenuItem extends ObjectMenuItem {

    /** The logger instance for this class */
    private static final Logger log = Logger.getLogger(UserMenuItem.class.getName());

    private String userName;

    private UserFrame userPanel;

    public UserMenuItem(String userName, String status) {
        super(userName);
        this.userName = userName;
        if (!status.toLowerCase().equals("open")) super.setIcon(PreferencesObject.getImageIcon("lock.gif"));
    }

    public String getUserName() {
        return this.userName;
    }

    protected void menuItemSelected() {
        try {
            SessionObject.getInstance().closeSchema();
        } catch (Exception ex) {
        }
        try {
            SessionObject.getInstance().openSchema(userName);
        } catch (Exception ex1) {
            MainFrame.getMainFrame().showErrorMessage("Failed to open schema", ex1.getMessage());
            log.log(Level.SEVERE, "Failed to open schema", ex1);
            return;
        }
        SwingWorker worker = new SwingWorker() {

            public Object construct() {
                MainFrame.getMainFrame().startRolling();
                MainFrame.getMainFrame().setAllObjectMenus();
                userPanel = new UserFrame();
                userPanel.setObjectName(userName);
                userPanel.setTitle("User " + userName);
                userPanel.setSize(MainFrame.getMainFrame().getDesktopSize());
                addFrameClosedListener(userPanel);
                MainFrame.getMainFrame().addInternalFrame(userPanel);
                setEnabled(false);
                MainFrame.getMainFrame().stopRolling();
                return null;
            }
        };
        worker.start();
        worker = null;
    }
}
