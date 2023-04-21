package iwork.patchpanel.manager;

import java.lang.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.io.Serializable;
import java.awt.event.*;
import java.awt.*;
import iwork.patchpanel.*;
import iwork.eheap2.*;

public class GroupInspector extends NodeInspector {

    private JTextField name = new JTextField();

    private boolean initializing = false;

    public GroupInspector() {
        super();
        initInterface();
        initActionListeners();
    }

    private void initInterface() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout());
        this.add(mainPanel, BorderLayout.CENTER);
        Box mainBox = Box.createVerticalBox();
        mainPanel.add(mainBox);
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new GridLayout(1, 2));
        namePanel.add(new JLabel("Name ", SwingConstants.RIGHT));
        namePanel.add(name);
        mainBox.add(namePanel);
        mainBox.add(Box.createVerticalGlue());
    }

    private void initActionListeners() {
        name.addActionListener(new NameChangedAction());
    }

    public void setNode(PPNode node) {
        super.setNode(node);
        GroupNode groupNode = (GroupNode) node;
        initializing = true;
        if (groupNode != null) {
            name.setText(groupNode.groupName);
        }
        initializing = false;
    }

    private void updateName() {
        ((GroupNode) node).updateName();
        notifyListenersNameChanged(node);
    }

    private class NameChangedAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            if (!initializing) {
                ((GroupNode) node).setGroupName(name.getText(), true);
                notifyListenersDataChanged();
                updateName();
            }
        }
    }

    public String getTitle() {
        return "Group Inspector:";
    }
}
