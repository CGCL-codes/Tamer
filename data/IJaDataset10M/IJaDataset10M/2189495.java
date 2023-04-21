package org.fpdev.apps.rtemaster.gui.dialogs;

import org.fpdev.util.gui.GUIFactory;
import java.awt.event.*;
import java.util.Enumeration;
import java.util.Iterator;
import javax.swing.*;
import javax.swing.border.*;
import org.fpdev.apps.rtemaster.gui.*;

public class SelectItemDialog extends JDialog implements ActionListener {

    private JButton okBtn_, cancelBtn_;

    private ButtonGroup rBtnGrp_;

    private int selIndex_;

    public SelectItemDialog(JFrame frame, Iterator<String> addrs) {
        super(frame, "Select Dialog", true);
        selIndex_ = -1;
        okBtn_ = new JButton("OK");
        okBtn_.addActionListener(this);
        cancelBtn_ = new JButton("Cancel");
        cancelBtn_.addActionListener(this);
        JPanel btnRow = new JPanel();
        btnRow.setLayout(new BoxLayout(btnRow, BoxLayout.X_AXIS));
        btnRow.add(okBtn_);
        btnRow.add(Box.createHorizontalStrut(5));
        btnRow.add(cancelBtn_);
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
        rBtnGrp_ = new ButtonGroup();
        int numItems = 0;
        while (addrs.hasNext()) {
            JRadioButton rBtn = new JRadioButton(addrs.next());
            rBtnGrp_.add(rBtn);
            rBtn.addActionListener(this);
            itemPanel.add(rBtn);
            numItems++;
        }
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(new JLabel("Please specify the specific address:"));
        topPanel.add(Box.createVerticalStrut(4));
        if (numItems <= 8) {
            topPanel.add(itemPanel);
        } else {
            JScrollPane scrollPane = new JScrollPane(itemPanel);
            GUIFactory.setSize(scrollPane, 300, 180);
            scrollPane.setAlignmentX(JComponent.LEFT_ALIGNMENT);
            topPanel.add(scrollPane);
        }
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        topPanel.setAlignmentX(CENTER_ALIGNMENT);
        mainPanel.add(topPanel);
        mainPanel.add(Box.createVerticalStrut(6));
        btnRow.setAlignmentX(CENTER_ALIGNMENT);
        mainPanel.add(btnRow);
        mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(mainPanel);
        this.pack();
        this.setLocation((frame.getWidth() / 2) - getWidth() / 2 + frame.getX(), (frame.getHeight() / 2) - getHeight() / 2 + frame.getY());
        show();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == okBtn_) {
            this.hide();
        } else if (e.getSource() == cancelBtn_) {
            selIndex_ = -1;
            this.hide();
        } else if (e.getSource() instanceof JRadioButton) {
            Enumeration btns = rBtnGrp_.getElements();
            int i = 0;
            while (btns.hasMoreElements()) {
                if (btns.nextElement() == e.getSource()) {
                    break;
                }
                i++;
            }
            selIndex_ = i;
        }
    }

    public int getSelectedIndex() {
        return selIndex_;
    }
}
