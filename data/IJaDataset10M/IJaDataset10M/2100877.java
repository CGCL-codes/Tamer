package com.vividsolutions.jump.workbench.ui.addremove;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import com.vividsolutions.jump.workbench.ui.InputChangedFirer;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;

public class TreeAddRemoveList extends JPanel implements AddRemoveList {

    private BorderLayout borderLayout1 = new BorderLayout();

    private TreeAddRemoveListModel model = new TreeAddRemoveListModel(new JTree().getModel());

    private InputChangedFirer inputChangedFirer = new InputChangedFirer();

    private JTree tree = new JTree();

    private Border border1;

    public void add(MouseListener listener) {
        tree.addMouseListener(listener);
    }

    public TreeAddRemoveList() {
        tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent e) {
                inputChangedFirer.fire();
            }
        });
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setSelectedItems(Collection items) {
        throw new UnsupportedOperationException();
    }

    public void setModel(TreeAddRemoveListModel model) {
        this.model = model;
        tree.setModel(model.getTreeModel());
        inputChangedFirer.fire();
    }

    public JTree getTree() {
        return tree;
    }

    public void add(InputChangedListener listener) {
        inputChangedFirer.add(listener);
    }

    void jbInit() throws Exception {
        border1 = new EtchedBorder(EtchedBorder.RAISED, new Color(0, 0, 51), new Color(0, 0, 25));
        this.setLayout(borderLayout1);
        this.add(tree, BorderLayout.CENTER);
    }

    public AddRemoveListModel getModel() {
        return model;
    }

    public List getSelectedItems() {
        ArrayList selectedNodes = new ArrayList();
        TreePath[] selectionPaths = tree.getSelectionPaths();
        if (selectionPaths == null) {
            return selectedNodes;
        }
        for (int i = 0; i < selectionPaths.length; i++) {
            selectedNodes.add(selectionPaths[i].getLastPathComponent());
        }
        return selectedNodes;
    }
}
