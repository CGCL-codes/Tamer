package org.opencdspowered.opencds.ui;

import org.opencdspowered.opencds.ui.util.Util;
import org.opencdspowered.opencds.ui.main.*;
import javax.swing.JFrame;
import javax.swing.JDialog;

/**
 * The class that initializes and starts our application. We do not have to
 *  set a Look And Feel (LAF) here as thats already done in the core initializer.
 *
 * @author  Lars 'Levia' Wesselius
*/
public class Initializer {

    public static void main(String[] args) {
        System.setProperty("substancelaf.heapStatusPanel", "");
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        Util.lock();
        final MainFrame mf = new MainFrame();
        mf.initialize();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                mf.setVisible(true);
            }
        });
    }
}
