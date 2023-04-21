package com.pallas.unicore.client.dialogs;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Abstract dialog that places ok, apply, reset and cancel buttons and a status
 * bar in the south part of BorderLayout. You can choose different dialog types
 * with different buttons from the constructor.
 * 
 * @author Ralf Ratering
 * @version $Id: GenericDialog.java,v 1.1 2004/05/25 14:58:47 rmenday Exp $
 */
public abstract class GenericDialog extends JDialog {

    /**
	 * Listener class that reacts on button events and calls the appropriate
	 * abstract methods
	 */
    private class ButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Object src = e.getSource();
            if (src == okButton) {
                value = OK_OPTION;
                if (applyValues()) {
                    dispose();
                }
            } else if (src == applyButton) {
                value = APPLY_OPTION;
                applyValues();
            } else if (src == resetButton) {
                value = RESET_OPTION;
                resetValues();
            } else if (src == cancelButton) {
                value = CANCEL_OPTION;
                if (cancelValues()) {
                    dispose();
                }
            } else if (src == closeButton) {
                value = CLOSE_OPTION;
                dispose();
            } else if (src == previewButton) {
                value = APPLY_OPTION;
                applyValues();
            }
        }
    }

    /**
	 * Return value from class method if APPLY is chosen.
	 */
    public static final int APPLY_OPTION = 4;

    /**
	 * Return value from class method if CANCEL is chosen.
	 */
    public static final int CANCEL_OPTION = 1;

    /**
	 * Return value from class method if CLOSE is chosen.
	 */
    public static final int CLOSE_OPTION = 2;

    protected static Logger logger = Logger.getLogger("com.pallas.unicore.client.dialogs");

    /**
	 * define different modi
	 */
    protected static final int NONE = 0, OK = 1, OK_CANCEL = 2, APPLY_CLOSE = 3, APPLY_RESET_CLOSE = 4, OK_PREVIEW_CANCEL = 5;

    /**
	 * Return value from class method if OK is chosen.
	 */
    public static final int OK_OPTION = 3;

    /**
	 * Return value from class method if PREVIEW is chosen.
	 */
    public static final int PREVIEW_OPTION = 5;

    /**
	 * Return value from class method if RESET is chosen.
	 */
    public static final int RESET_OPTION = 0;

    protected ButtonListener buttonListener = new ButtonListener();

    /**
	 * different dialog buttons are protected to allow e.g. tooltip setting from
	 * sub classes
	 */
    protected JButton okButton = new JButton("Ok"), cancelButton = new JButton("Cancel"), applyButton = new JButton("Apply"), resetButton = new JButton("Reset"), closeButton = new JButton("Close"), previewButton = new JButton("Preview");

    private JLabel statusLabel = new JLabel(" ");

    private int type = OK_CANCEL;

    private int value;

    /**
	 * Constructor builds the components
	 * 
	 * @param title
	 *            dialog title
	 * @param parent
	 *            dialog parent - important for modal dialogs
	 * @param modal
	 *            true if dialog is modal
	 * @param type
	 *            NONE, OK, OK_CANCEL, OK_APPLY_CANCEL, OK_RESET_CANCEL or
	 *            OK_APPLY_RESET_CANCEL
	 */
    public GenericDialog(JFrame parent, String title, boolean modal, int type) {
        super(parent, title, modal);
        this.type = type;
        buildComponents();
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {

            /**
			 * Description of the Method
			 * 
			 * @param we
			 *            Description of Parameter
			 */
            public void windowClosing(WindowEvent we) {
                cancelValues();
                dispose();
            }
        });
    }

    /**
	 * Abstract method that is called each time the okButton is pressed, before
	 * the dialog is closed.
	 * 
	 * @return true if dialog can be disposed
	 */
    protected abstract boolean applyValues();

    /**
	 * Setup buttons and status bar
	 */
    private void buildComponents() {
        okButton.addActionListener(buttonListener);
        applyButton.addActionListener(buttonListener);
        resetButton.addActionListener(buttonListener);
        cancelButton.addActionListener(buttonListener);
        closeButton.addActionListener(buttonListener);
        previewButton.addActionListener(buttonListener);
        JPanel buttonSubPanel = new JPanel();
        switch(type) {
            case OK:
                buttonSubPanel.add(okButton);
                break;
            case OK_CANCEL:
                buttonSubPanel.setLayout(new GridLayout(1, 2, 10, 10));
                buttonSubPanel.add(okButton);
                buttonSubPanel.add(cancelButton);
                break;
            case OK_PREVIEW_CANCEL:
                buttonSubPanel.setLayout(new GridLayout(1, 3, 10, 10));
                buttonSubPanel.add(okButton);
                buttonSubPanel.add(previewButton);
                buttonSubPanel.add(cancelButton);
                break;
            case APPLY_CLOSE:
                buttonSubPanel.setLayout(new GridLayout(1, 2, 10, 10));
                buttonSubPanel.add(applyButton);
                buttonSubPanel.add(resetButton);
                break;
            case APPLY_RESET_CLOSE:
                buttonSubPanel.setLayout(new GridLayout(1, 3, 10, 10));
                buttonSubPanel.add(applyButton);
                buttonSubPanel.add(resetButton);
                buttonSubPanel.add(closeButton);
        }
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(buttonSubPanel);
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(buttonPanel, BorderLayout.CENTER);
        southPanel.add(statusLabel, BorderLayout.SOUTH);
        getContentPane().add(southPanel, BorderLayout.SOUTH);
    }

    /**
	 * Abstract method that is called each time the cancelButton is pressed, or
	 * the dialog is closed using the window handle.
	 * 
	 * @return true if dialog can be disposed
	 */
    protected abstract boolean cancelValues();

    /**
	 * Method that is called each time the resetButton is pressed, before the
	 * dialog is closed. This one is not declared as abstract, because only some
	 * dialogs use it.
	 */
    protected void resetValues() {
        if (type == APPLY_RESET_CLOSE) {
            logger.warning("resetValues method from GenericDialog needs implementation!");
        }
    }

    /**
	 * Modify text in status bar from outside
	 * 
	 * @param text
	 *            new status message
	 */
    public void setStatusMessage(String text) {
        logger.fine(text);
        statusLabel.setText(text);
        pack();
    }

    /**
	 * Overwrite show method to make okButton the default one and to position
	 * dialog relative to main frame.
	 */
    public void show() {
        getRootPane().setDefaultButton(okButton);
        setLocationRelativeTo(getParent());
        super.show();
    }

    /**
	 * Makes the Dialog visible.
	 * 
	 * @return an integer indicating the option chosen by the user
	 */
    public int showDialog() {
        this.show();
        return value;
    }
}
