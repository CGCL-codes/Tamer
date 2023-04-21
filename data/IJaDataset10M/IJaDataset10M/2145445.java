package org.jnm.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JButton;
import org.jnm.Node;

public class NodeEditorDialog extends JDialog {

    public NodeEditorDialog(Node node) {
        this.node = node;
        init();
    }

    public int showDialog() {
        setModal(true);
        show();
        return result;
    }

    public void save() {
        nodeEditorPanel.save();
    }

    private void init() {
        nodeEditorPanel = new NodeEditorPanel(node);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add("Center", nodeEditorPanel);
        getContentPane().add("South", createButtonPanel());
        setTitle("Node: " + node.getDisplayName());
        pack();
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                result = APPROVE_OPTION;
                save();
                dispose();
            }
        });
        panel.add(saveButton);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                result = CANCEL_OPTION;
                dispose();
            }
        });
        panel.add(cancelButton);
        return panel;
    }

    public static final int APPROVE_OPTION = 1;

    public static final int CANCEL_OPTION = 2;

    private Node node;

    private int result = CANCEL_OPTION;

    private NodeEditorPanel nodeEditorPanel;

    private JButton saveButton;

    private JButton cancelButton;
}
