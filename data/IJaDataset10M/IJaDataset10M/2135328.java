package org.neodatis.odb.gui;

import javax.swing.JFrame;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.neodatis.odb.OdbConfiguration;
import org.neodatis.odb.gui.tool.GuiUtil;

public class ODBExplorerMain {

    /**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
    private static void createAndShowGUI() {
        OdbConfiguration.setCheckModelCompatibility(false);
        OdbConfiguration.setAutomaticallyIncreaseCacheSize(true);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            System.out.println("Look&Feel = " + UIManager.getSystemLookAndFeelClassName());
            String className = "com.jgoodies.looks.plastic.PlasticLookAndFeel";
            UIManager.setLookAndFeel((LookAndFeel) Class.forName(className).newInstance());
        } catch (UnsupportedLookAndFeelException e) {
        } catch (ClassNotFoundException e) {
            System.out.println("JGoodies Looks&feels are not available");
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }
        GuiUtil.setDefaultFont();
        final ODBExplorerFrame frame = new ODBExplorerFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                createAndShowGUI();
            }
        });
    }
}
