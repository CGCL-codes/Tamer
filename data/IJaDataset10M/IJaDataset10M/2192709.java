package com.streamsicle.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import org.apache.log4j.Category;

/**
 * Super-class of screens in the configuration wizard.
 *
 * @author John Watkinson
 */
public class WizardDialog extends JDialog implements ActionListener {

    public static final String ACTION_CANCEL = "CANCEL";

    public static final String ACTION_NEXT = "NEXT";

    protected Object data;

    private JPanel contentPanel;

    static Category log = Category.getInstance(WizardDialog.class);

    public WizardDialog(Frame owner, String title, String description) {
        super(owner);
        setModal(true);
        setTitle(title);
        JTextArea descriptionLabel = new JTextArea(description);
        descriptionLabel.setEditable(false);
        descriptionLabel.setOpaque(false);
        descriptionLabel.setWrapStyleWord(true);
        descriptionLabel.setLineWrap(true);
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        JButton cancelButton = new JButton("Cancel");
        JButton nextButton = new JButton("Next...");
        cancelButton.setActionCommand(ACTION_CANCEL);
        nextButton.setActionCommand(ACTION_NEXT);
        cancelButton.addActionListener(this);
        nextButton.addActionListener(this);
        JPanel buttonPanel = new JPanel(new BorderLayout());
        contentPanel = new JPanel(new FlowLayout());
        buttonPanel.add(cancelButton, BorderLayout.WEST);
        buttonPanel.add(nextButton, BorderLayout.EAST);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(descriptionLabel, BorderLayout.NORTH);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        getContentPane().add(contentPanel, BorderLayout.CENTER);
    }

    /**
    * Adds specialized content to this wizard dialog.
    */
    public void setContent(Component comp) {
        contentPanel.add(comp);
    }

    /**
    * Called by the dialog to prepare the data for returning. Override this
    * to have the dialog return some data.
    */
    public void setData() {
        data = ACTION_NEXT;
    }

    /**
    * Displays the dialog box and returns some data, or <code>NULL</code> if
    * the <i>cancel</i> button was pressed.
    */
    public Object getData() {
        pack();
        setSize(400, 200);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        Dimension size = getSize();
        int x = (screenSize.width - size.width) / 2;
        int y = (screenSize.height - size.height) / 2;
        setLocation(x, y);
        setVisible(true);
        return data;
    }

    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        if (ACTION_CANCEL.equals(action)) {
            data = null;
        } else {
            setData();
        }
        setVisible(false);
    }

    /**
    * A trial WizardDialog
    */
    public static void main(String[] args) {
        JLabel testLabel = new JLabel("This is the extra content.");
        WizardDialog wd = new WizardDialog(null, args[0], args[1]);
        wd.setContent(testLabel);
        Object o = wd.getData();
        log.info("Result: " + o);
        System.exit(0);
    }
}
