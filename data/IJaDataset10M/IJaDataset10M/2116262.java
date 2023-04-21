package org.vizzini.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.UIManager;
import org.junit.Before;
import org.junit.Test;

/**
 * Provides tests for the <code>ToggleMenuItem</code> class.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.4
 */
public class TestToggleMenuItem {

    /**
     * Perform the selected action.
     *
     * @param  event  Event.
     *
     * @since  v0.4
     */
    public void selected0ActionPerformed(ActionEvent event) {
        System.out.println("selected0ActionPerformed()");
    }

    /**
     * Perform the selected action.
     *
     * @param  event  Event.
     *
     * @since  v0.4
     */
    public void selected1ActionPerformed(ActionEvent event) {
        System.out.println("selected1ActionPerformed()");
    }

    /**
     * Perform the selected action.
     *
     * @param  event  Event.
     *
     * @since  v0.4
     */
    public void selected2ActionPerformed(ActionEvent event) {
        System.out.println("selected2ActionPerformed()");
    }

    /**
     * Perform the selected action.
     *
     * @param  event  Event.
     *
     * @since  v0.4
     */
    public void selected3ActionPerformed(ActionEvent event) {
        System.out.println("selected3ActionPerformed()");
    }

    /**
     * @since  v0.4
     */
    @Before
    public void setUp() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        ApplicationSupport.setStringBundleName("org.vizzini.ui.testResources");
        ApplicationSupport.setConfigBundleName("org.vizzini.ui.testConfig");
    }

    /**
     * Test the user interface.
     *
     * @throws  InterruptedException  if there is an interruption.
     *
     * @since   v0.4
     */
    @Test
    public void testUI() throws InterruptedException {
        boolean useIcon = false;
        boolean useText = true;
        boolean useMnemonic = true;
        boolean useAccelerator = true;
        ToggleMenuItem toggleMenuItem0 = new ToggleMenuItem(this, "unselected0", "selected0");
        ToggleMenuItem toggleMenuItem1 = new ToggleMenuItem(this, "unselected1", "selected1", useIcon, useText, useMnemonic, useAccelerator);
        Action actionUnselected2 = ActionManager.getAction(this, "unselected2");
        Action actionSelected2 = ActionManager.getAction(this, "selected2");
        ToggleMenuItem toggleMenuItem2 = new ToggleMenuItem(actionUnselected2, actionSelected2);
        Action actionUnselected3 = ActionManager.getAction(this, "unselected3", useIcon, useText, useMnemonic, useAccelerator);
        Action actionSelected3 = ActionManager.getAction(this, "selected3", useIcon, useText, useMnemonic, useAccelerator);
        ToggleMenuItem toggleMenuItem3 = new ToggleMenuItem(actionUnselected3, actionSelected3);
        JMenu menu = new JMenu("File");
        menu.add(toggleMenuItem0);
        menu.add(toggleMenuItem1);
        menu.add(toggleMenuItem2);
        menu.add(toggleMenuItem3);
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menu);
        JPanel panel = new JPanel();
        panel.add(menuBar);
        JFrame frame = new JFrame("MenuButton Test");
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.setSize(640, 480);
        frame.setVisible(true);
        synchronized (this) {
            wait();
        }
    }

    /**
     * Perform the unselected action.
     *
     * @param  event  Event.
     *
     * @since  v0.4
     */
    public void unselected0ActionPerformed(ActionEvent event) {
        System.out.println("unselected0ActionPerformed()");
    }

    /**
     * Perform the unselected action.
     *
     * @param  event  Event.
     *
     * @since  v0.4
     */
    public void unselected1ActionPerformed(ActionEvent event) {
        System.out.println("unselected1ActionPerformed()");
    }

    /**
     * Perform the unselected action.
     *
     * @param  event  Event.
     *
     * @since  v0.4
     */
    public void unselected2ActionPerformed(ActionEvent event) {
        System.out.println("unselected2ActionPerformed()");
    }

    /**
     * Perform the unselected action.
     *
     * @param  event  Event.
     *
     * @since  v0.4
     */
    public void unselected3ActionPerformed(ActionEvent event) {
        System.out.println("unselected3ActionPerformed()");
    }
}
