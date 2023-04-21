package com.cfdrc.sbmlforge.gui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import com.cfdrc.sbmlforge.util.StringUtils;

/**
 * Creates a custom JDialog with a title, message, and indeterminate progress bar.
 * 
 * @author Zachary Groff
 */
public class ModalWaitDialog extends JDialog {

    /** ID for use in serialization. */
    private static final long serialVersionUID = 5637392396347584234L;

    /** Label displaying message in content area of dialog. */
    private JLabel lblMessage;

    /**
	 * Creates a non-modal dialog without a title and without a specified Frame owner.
	 *  No message is displayed.
	 */
    public ModalWaitDialog() {
        this(null);
    }

    /**
	 * Creates a non-modal dialog without a title and with the specified Frame as its owner.
	 *  No message is displayed.
	 *
	 * @param owner		the Frame from which the dialog is displayed
	 */
    public ModalWaitDialog(Frame owner) {
        this(owner, StringUtils.EMPTY);
    }

    /**
	 * Creates a modal or non-modal dialog without a title and with the specified owner Frame.
	 *  No message is displayed.
	 * 
	 * @param owner		the Frame from which the dialog is displayed
	 * @param modal		true for a modal dialog, false for one that allows others windows to be active at the same time
	 */
    public ModalWaitDialog(Frame owner, boolean modal) {
        this(owner, StringUtils.EMPTY, modal);
    }

    /**
	 * Creates a non-modal dialog with the specified title and with the specified owner frame.
	 *  If owner is null, a shared, hidden frame will be set as the owner of the dialog.
	 *  No message is displayed.
	 * 
	 * @param owner		the Frame from which the dialog is displayed
	 * @param title		the String to display in the dialog's title bar 
	 */
    public ModalWaitDialog(Frame owner, String title) {
        super(owner, title);
        init(StringUtils.EMPTY);
    }

    /**
	 * Creates a modal or non-modal dialog with the specified title and the specified owner Frame.
	 * If owner is null, a shared, hidden frame will be set as the owner of this dialog. 
	 * 
	 * @param owner		the Frame from which the dialog is displayed
	 * @param title		the String to display in the dialog's title bar 
	 * @param modal		true for a modal dialog, false for one that allows others windows to be active at the same time
	 */
    public ModalWaitDialog(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        init("Waiting for something to get done, should be soon...");
    }

    /**
	 * Creates a modal or non-modal dialog without a title and with the specified owner Frame.
	 * The given message is displayed on the screen.
	 * 
	 * @param owner		the Frame from which the dialog is displayed
	 * @param modal		true for a modal dialog, false for one that allows others windows to be active at the same time
	 * @param message	message to display to user in content area of dialog
	 */
    public ModalWaitDialog(Frame owner, boolean modal, String message) {
        this(owner, StringUtils.EMPTY, modal, message);
    }

    /**
	 * Creates a non-modal dialog with the specified title and with the specified owner frame.
	 *  If owner is null, a shared, hidden frame will be set as the owner of the dialog.
	 * The given message is displayed on the screen.
	 * 
	 * @param owner		the Frame from which the dialog is displayed
	 * @param title		the String to display in the dialog's title bar 
	 * @param message	message to display to user in content area of dialog.
	 */
    public ModalWaitDialog(Frame owner, String title, String message) {
        this(owner, title, false, message);
    }

    /**
	 * Creates a modal or non-modal dialog with the specified title and the specified owner Frame.
	 * If owner is null, a shared, hidden frame will be set as the owner of this dialog.
	 * The given message is displayed on the screen. 
	 * 
	 * @param owner		the Frame from which the dialog is displayed
	 * @param title		the String to display in the dialog's title bar 
	 * @param modal		true for a modal dialog, false for one that allows others windows to be active at the same time
	 * @param message	message to display to user in content area of dialog
	 */
    public ModalWaitDialog(Frame owner, String title, boolean modal, String message) {
        super(owner, title, modal);
        init(message);
    }

    /**
	 * Build GUI and setup display message.
	 * 	
	 * @param message	message to display to user in content area of dialog
	 */
    private void init(String message) {
        this.getContentPane().add(createPane(message));
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent we) {
            }
        });
        loadWindowPreferences();
    }

    /**
	 * Create inner panel making up dialog content area.
	 * 
	 * @param message	message to display to user in content area of dialog
	 * @return			JPanel containing display of content area
	 */
    private JPanel createPane(String message) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.NONE;
        constraints.ipadx = 0;
        constraints.ipady = 0;
        constraints.weightx = 0.0;
        constraints.weighty = 0.0;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(2, 2, 5, 2);
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        lblMessage = new JLabel(message);
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(lblMessage, constraints);
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setIndeterminate(true);
        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(progressBar, constraints);
        return panel;
    }

    /**
	 * Loads window preferences: Size, placement, display layer (always on top), and no title bar.
	 */
    private void loadWindowPreferences() {
        this.setPreferredSize(new Dimension(200, 100));
        int windowHeight = getPreferredSize().height;
        int windowWidth = getPreferredSize().width;
        setAlwaysOnTop(true);
        this.setUndecorated(false);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int X = (screen.width / 2) - (windowWidth / 2);
        int Y = (screen.height / 2) - (windowHeight / 2);
        setBounds(X, Y, windowWidth, windowHeight);
        pack();
    }

    /**
	 * Close this window. In doing so, the underlying frame will be released if there is one and this dialog is modal.
	 */
    public void close() {
        this.setVisible(false);
        this.dispose();
    }

    /**
	 * Set message displayed in dialog
	 * 
	 * @param message	Message to be displayed
	 */
    public void setMessage(String message) {
        this.lblMessage.setText(message);
    }
}
