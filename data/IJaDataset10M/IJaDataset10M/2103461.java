package com.teletalk.jadmin.gui.overview;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;

/**
 * Dialog box class for viewing a vector property item.
 * 
 * @author Tobias L�fstrand
 * 
 * @since 1.0
 */
public final class VectorPropertyItemDialog extends JDialog implements ActionListener {

    /**
    * 
    */
    private static final long serialVersionUID = 1L;

    private final JButton done;

    private final JTextArea textDetails;

    /**
	 * Creates a new VectorPropertyItemDialog.
	 */
    public VectorPropertyItemDialog(Frame owner, String itemId, String itemString) {
        super(owner, itemId, true);
        JPanel mainPanel = (JPanel) this.getContentPane();
        mainPanel.setLayout(new BorderLayout(3, 3));
        textDetails = new JTextArea(itemString, 5, 25);
        textDetails.setLineWrap(true);
        textDetails.setWrapStyleWord(true);
        textDetails.setEditable(false);
        JScrollPane textScroll = new JScrollPane(textDetails);
        textScroll.setBorder(BorderFactory.createTitledBorder("Item description"));
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        southPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        done = new JButton("Done");
        done.addActionListener(this);
        southPanel.add(done);
        mainPanel.add(textScroll, BorderLayout.CENTER);
        mainPanel.add(southPanel, BorderLayout.SOUTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
    }

    /**
	 * Action event handler method.
	 */
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source == done) {
            dispose();
        }
    }
}
