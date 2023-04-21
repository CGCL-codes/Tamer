package net.sourceforge.rombrowser.gui;

import java.io.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import net.sourceforge.rombrowser.roms.*;
import net.sourceforge.rombrowser.gui.*;
import net.sourceforge.rombrowser.util.*;

public class ROMTreePanel extends JPanel {

    private JTree myTree;

    private ROMFileDisplay infoFrame = new ROMFileDisplay();

    public ROMTreePanel(TreeModel tm) {
        setLayout(new GridBagLayout());
        myTree = new JTree(tm);
        myTree.setRootVisible(false);
        myTree.setShowsRootHandles(true);
        myTree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        myTree.addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) myTree.getLastSelectedPathComponent();
                if (node == null) return;
                Object nodeInfo = node.getUserObject();
                if (node.isLeaf()) {
                    try {
                        ROMFile rf = (ROMFile) nodeInfo;
                        infoFrame.setMetaData(rf);
                    } catch (ClassCastException cce) {
                    }
                } else {
                }
            }
        });
        myTree.getInputMap().put(KeyStroke.getKeyStroke("BACK_SPACE"), "delete-row");
        myTree.getInputMap().put(KeyStroke.getKeyStroke("DELETE"), "delete-row");
        myTree.getActionMap().put("delete-row", new DeleteAction(myTree));
        myTree.setCellRenderer(new Renderer());
        JFrame jf = new JFrame();
        jf.setContentPane(infoFrame);
        jf.setVisible(true);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(new JScrollPane(myTree), c);
    }

    public void setTreeModel(TreeModel tm) {
        myTree.setModel(tm);
    }

    public TreeModel getTreeModel() {
        return myTree.getModel();
    }

    public JTree getJTree() {
        return myTree;
    }
}
