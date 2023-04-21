package com.compendium.ui.dialogs;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import com.compendium.ui.*;

/**
 * Dialog used to display a progress bar.
 *
 * @author ? / Michelle Bachler
 */
public class UIProgressDialog extends JDialog implements ActionListener {

    /** The pane to add the main content for this dialog to.*/
    private Container contentPane = null;

    /** The button to cancel this dialog.*/
    private JButton pbCancel = null;

    /** Indicates id thi dialog has been cancelled.*/
    private boolean cancelled = false;

    /** This label displays a message to the user.*/
    private JLabel statusLabel = new JLabel("");

    /** The progress bar held in this dialog.*/
    private JProgressBar progressBar;

    /** The current message being displayed to the user.*/
    private String sMessage = "";

    /**
	 * Constructor.
	 *
	 * @param parent, the parent frame of this dialog.
	 * @param title, the title for this dialog.
	 * @param message, the message to display in this dialog.
	 */
    public UIProgressDialog(JFrame parent, String title, String message) {
        super(parent, true);
        setTitle(title);
        sMessage = message;
        contentPane = getContentPane();
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    /**
	 * Constructor.
	 *
	 * @param parent, the parent frame of this dialog.
	 * @param title, the title for this dialog.
	 * @param message, the message to display in this dialog.
	 */
    public UIProgressDialog(JDialog parent, String title, String message) {
        super(parent, true);
        setTitle(title);
        sMessage = message;
        contentPane = getContentPane();
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    /**
	 * Draw the contents of this dialog.
	 * @param pb, the progress bar displayed in this dialog.
	 */
    public void showDialog(JProgressBar pb) {
        this.showDialog(pb, true);
    }

    /**
	 * Draw the contents of this dialog.
	 * @param pb, the progress bar displayed in this dialog.
	 * @param canCancel, indicates if this progress dialog can be cancelled (draw Cancel button or not?).
	 */
    public void showDialog(JProgressBar pb, boolean canCancel) {
        progressBar = pb;
        GridLayout grid = null;
        if (canCancel) grid = new GridLayout(3, 1); else grid = new GridLayout(2, 1);
        contentPane.setLayout(grid);
        setStatus(0);
        JPanel labelpanel = new JPanel();
        labelpanel.add(statusLabel);
        contentPane.add(labelpanel);
        JPanel progresspanel = new JPanel();
        progressBar.setPreferredSize(new Dimension(220, 20));
        progresspanel.add(progressBar);
        contentPane.add(progresspanel);
        if (canCancel) {
            JPanel panel = new JPanel();
            pbCancel = new UIButton("Cancel");
            getRootPane().setDefaultButton(pbCancel);
            panel.add(pbCancel);
            contentPane.add(panel);
            pbCancel.addActionListener(this);
        }
        pack();
        if (canCancel) setSize(350, 150); else setSize(350, 100);
        UIUtilities.centerComponent(this, getParent());
    }

    /**
	 * Checks the state of the progress dialog.
 	 * @param bCancelled, has this progress dialog been cancelled?
 	 * @param sType, the name of the type of use for this progress dialog (displayed in message).
	 */
    public boolean checkProgress(boolean bCancelled, String sType) {
        if (!bCancelled && isCancelled()) {
            int result = JOptionPane.showConfirmDialog(this, "Do you want to Cancel this " + sType + "?", "Cancel " + sType, JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                setVisible(false);
                return true;
            } else {
                setCancelled(false);
                return false;
            }
        }
        return false;
    }

    /**
	 * Process a cancel button push.
	 * @param ae, the ActionEvent associated.
	 */
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == pbCancel) {
            cancelled = true;
        }
    }

    /**
	 * Return whether this progress dialog has been cancelled or not.
	 * @return boolean, true if this progress dialog has been cancelled, else false.
	 */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
	 * Set whether this dialog has been cancelled or not.
	 * @param cancel, has this dialog been cancelled?
	 */
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    /**
	 * Set the message to display to the user.
	 * @param message, the message to diaply to the user.
	 */
    public void setMessage(String message) {
        sMessage = message;
    }

    /**
	 * Set the percentage complete for this progress dialog.
	 * @param num, the number of units completed out of the maximum stated.
	 */
    public void setStatus(int num) {
        if (progressBar != null) statusLabel.setText(sMessage + ": " + (num * 100 / progressBar.getMaximum()) + "%");
    }
}
