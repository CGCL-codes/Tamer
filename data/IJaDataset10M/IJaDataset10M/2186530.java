package com.rapidminer.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 * The stop dialog can be used by operators which allow to abort an iteration.
 * Instead of using a listener concept the operator should ask this dialog if it
 * should still perform its operation. Using this operator might be useful in
 * cases like evolutionary optimizing.
 * 
 * @author Ingo Mierswa
 */
public class StopDialog extends JDialog {

    private static final long serialVersionUID = -7090498773341030469L;

    private boolean stillRunning = true;

    public StopDialog(String title, String text) {
        super((Frame) null, title, false);
        getContentPane().setLayout(new BorderLayout());
        JLabel label = new JLabel(text);
        label.setBorder(BorderFactory.createEmptyBorder(7, 7, 7, 7));
        getContentPane().add(label, BorderLayout.CENTER);
        Icon informationIcon = UIManager.getIcon("OptionPane.informationIcon");
        if (informationIcon != null) {
            JLabel informationIconLabel = new JLabel(informationIcon);
            informationIconLabel.setBorder(BorderFactory.createEmptyBorder(7, 7, 7, 7));
            getContentPane().add(informationIconLabel, BorderLayout.WEST);
        }
        JPanel buttonPanel = new JPanel();
        JButton stopButton = new JButton("Stop");
        stopButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                stillRunning = false;
            }
        });
        buttonPanel.add(stopButton);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
    }

    public boolean isStillRunning() {
        return stillRunning;
    }
}
