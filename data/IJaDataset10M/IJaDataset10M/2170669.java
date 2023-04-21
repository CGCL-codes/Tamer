package de.fhg.igd.earth.control.dialog;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import javax.swing.JDialog;

/**
 * This class creates a passive dialog to display something.
 * It's used to show the "please wait" window.
 *
 * Title        : Earth
 * Copyright    : Copyright (c) 2001
 * Organisation : IGD FhG
 * @author       : Werner Beutel
 * @version      : 1.0
 */
public class StatusDialog extends JDialog {

    /*************************************************************************
     * Creates an instance of this class.
     * @param frame Parent frame
     * @param title Dialog box title
     * @param modal Modal flag
     ************************************************************************/
    public StatusDialog(Frame frame, String title, boolean modal) {
        super(frame, title, modal);
        try {
            jbInit();
            pack();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(200, 25);
        Dimension frameSize = this.getSize();
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }

    /*************************************************************************
     * GUI Init
     ************************************************************************/
    void jbInit() throws Exception {
    }
}
