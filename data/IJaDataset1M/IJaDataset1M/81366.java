package org.mbari.swing;

import java.awt.BorderLayout;
import java.awt.Frame;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * <!-- Class Description -->
 *
 *
 * @version    $Id: ProgressDialog.java 332 2006-08-01 18:38:46Z hohonuuli $
 * @author     MBARI    
 */
public class ProgressDialog extends JDialog {

    /**
     * 
     */
    private static final long serialVersionUID = -5492015909318955975L;

    /**
	 * @uml.property  name="jContentPane"
	 * @uml.associationEnd  
	 */
    private JPanel jContentPane = null;

    /**
	 * @uml.property  name="descriptionLabel"
	 * @uml.associationEnd  
	 */
    private JLabel descriptionLabel = null;

    /**
	 * @uml.property  name="progressBar"
	 * @uml.associationEnd  
	 */
    private JProgressBar progressBar = null;

    /**
     * This is the default constructor
     */
    public ProgressDialog() {
        this(null);
    }

    /**
     * Constructs ...
     *
     *
     * @param owner
     */
    public ProgressDialog(Frame owner) {
        super(owner);
        initialize();
    }

    /**
	 * This method initializes jContentPane
	 * @return  javax.swing.JPanel
	 * @uml.property  name="jContentPane"
	 */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            descriptionLabel = new JLabel();
            descriptionLabel.setText("");
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(descriptionLabel, java.awt.BorderLayout.NORTH);
            jContentPane.add(getProgressBar(), java.awt.BorderLayout.SOUTH);
        }
        return jContentPane;
    }

    /**
	 * This method retrieves the progress bar used by this dialog.
	 * @return  javax.swing.JProgressBar
	 * @uml.property  name="progressBar"
	 */
    public JProgressBar getProgressBar() {
        if (progressBar == null) {
            progressBar = new JProgressBar();
        }
        return progressBar;
    }

    /**
     * This method initializes this
     *
     * @return void
     */
    private void initialize() {
        this.setContentPane(getJContentPane());
        this.setSize(250, 150);
    }

    public void setLabel(String label) {
        descriptionLabel.setText(label);
    }

    public void setVisible(boolean visible) {
        if (!visible) {
            setLabel("");
            getProgressBar().setString("");
            getProgressBar().setValue(0);
        }
        super.setVisible(visible);
    }
}
