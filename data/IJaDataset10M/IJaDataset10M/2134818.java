package com.cube42.shapedcharge.viewer;

import java.awt.BorderLayout;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import com.cube42.util.gui.AppFoundation;
import com.cube42.util.gui.DialogUtils;

/**
 * Client used to play Shaped Charge
 *
 * @author  Matt Paulin
 * @version $Id: ShapedCharge.java,v 1.1 2003/03/14 01:37:15 zer0wing Exp $
 */
public class ShapedCharge extends AppFoundation {

    /**
	 * Tabbed panel used to track the different modes
	 */
    private ModePanel modePanel;

    /**
     * Constructs the ShapedCharge
     */
    public ShapedCharge() {
        super(true);
    }

    /**
	 * @see com.cube42.util.gui.AppFoundation#getAppName()
	 */
    public String getAppName() {
        return "Shaped Charge Client 1.0";
    }

    /**
	 * @see com.cube42.util.gui.AppFoundation#initApp()
	 */
    public void initApp() {
        JMenuBar menuBar = new JMenuBar();
        JMenuItem newAccountMenuItem = new JMenuItem("Create Account");
        JMenuItem manageMenuItem = new JMenuItem("Manage Troops");
        JMenuItem configureMenuItem = new JMenuItem("Configure Client");
        JMenu accountMenu = new JMenu("Account");
        accountMenu.add(newAccountMenuItem);
        accountMenu.add(manageMenuItem);
        JMenu clientMenu = new JMenu("Client");
        clientMenu.add(configureMenuItem);
        menuBar.add(accountMenu);
        menuBar.add(clientMenu);
        setJMenuBar(menuBar);
        modePanel = new ModePanel();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(modePanel);
        this.setSize(800, 600);
        DialogUtils.center(this);
    }

    /**
	 * @see com.cube42.util.gui.AppFoundation#shutdownApp()
	 */
    public void shutdownApp() {
    }

    /**
     * Starts the Propman
     */
    public static void main(String[] args) {
        ShapedCharge bps = new ShapedCharge();
    }
}
