package tufts.vue;

import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.awt.*;

public class EditLibraryDialog extends JDialog implements java.awt.event.ActionListener {

    JPanel editLibraryPanel = new JPanel();

    JPanel buttonPanel = new JPanel();

    JButton okButton = new JButton(VueResources.getString("button.ok.label"));

    java.awt.GridBagConstraints gbConstraints;

    public EditLibraryDialog() {
        super(VUE.getDialogParentAsFrame(), "EDIT LIBRARY", true);
        try {
            editLibraryPanel.setBackground(VueResources.getColor("White"));
            setBackground(VueResources.getColor("White"));
            java.awt.GridBagLayout gbLayout = new java.awt.GridBagLayout();
            gbConstraints = new java.awt.GridBagConstraints();
            gbConstraints.anchor = java.awt.GridBagConstraints.WEST;
            gbConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
            editLibraryPanel.setLayout(gbLayout);
            makePanel();
            okButton.addActionListener(this);
            buttonPanel.add(okButton);
            okButton.setBackground(VueResources.getColor("Orange"));
            getContentPane().add(editLibraryPanel, BorderLayout.CENTER);
            pack();
            setLocation(300, 300);
            setSize(new Dimension(480, 300));
        } catch (Throwable t) {
            t.printStackTrace();
        }
        setVisible(true);
    }

    private void makePanel() {
        try {
            gbConstraints.gridx = 0;
            gbConstraints.gridy = 0;
            editLibraryPanel.add(new JLabel(VueResources.getString("jlabel.underconstruction")), gbConstraints);
            gbConstraints.gridx = 0;
            gbConstraints.gridy = 1;
            editLibraryPanel.add(buttonPanel, gbConstraints);
            getRootPane().setDefaultButton(okButton);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void update(int check) {
        try {
            getContentPane().remove(editLibraryPanel);
            getContentPane().add(editLibraryPanel, BorderLayout.CENTER);
            getContentPane().repaint();
            getContentPane().validate();
            pack();
            setVisible(true);
            super.setVisible(true);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void actionPerformed(java.awt.event.ActionEvent ae) {
        if (ae.getActionCommand().equals("OK")) {
            setVisible(false);
        }
    }

    public String toString() {
        return "EditLibraryDialog";
    }
}
