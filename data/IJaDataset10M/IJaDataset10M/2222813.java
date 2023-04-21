package com.rapig.mail.page;

import javax.swing.JPanel;
import java.awt.Frame;
import java.awt.BorderLayout;
import javax.swing.JDialog;
import java.awt.Dimension;

public class Setting extends JDialog {

    private static final long serialVersionUID = 1L;

    private JPanel jContentPane = null;

    /**
	 * @param owner
	 */
    public Setting(Frame owner) {
        super(owner);
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setSize(366, 244);
        this.setContentPane(getJContentPane());
    }

    /**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
        }
        return jContentPane;
    }
}
