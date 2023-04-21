package com.codemonster.nato.listener;

import com.codemonster.nato.NATOMain;
import com.codemonster.nato.model.TaskNode;
import com.codemonster.nato.util.TreeUtil;
import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class TreeTransferHandler extends TransferHandler {

    DataFlavor[] flavors = new DataFlavor[1];

    TaskNode[] nodesToRemove;

    TaskNode lastTargetNode = null;

    public TreeTransferHandler() {
        try {
            String mimeType = DataFlavor.javaJVMLocalObjectMimeType + ";class=\"" + TaskNode[].class.getName() + "\"";
            flavors[0] = new DataFlavor(mimeType);
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFound: " + e.getMessage());
        }
    }

    public boolean canImport(TransferHandler.TransferSupport support) {
        support.setShowDropLocation(true);
        if (!support.isDrop()) {
            return false;
        }
        if (!support.isDataFlavorSupported(flavors[0])) {
            return false;
        }
        JTree.DropLocation dropLocation = (JTree.DropLocation) support.getDropLocation();
        JTree tree = (JTree) support.getComponent();
        TreePath targetPath = dropLocation.getPath();
        TaskNode targetNode = (TaskNode) targetPath.getLastPathComponent();
        if (lastTargetNode != null && lastTargetNode != targetNode) {
            lastTargetNode.setBordered(false);
        }
        lastTargetNode = targetNode;
        targetNode.setBordered(true);
        tree.repaint();
        int dropRow = tree.getRowForPath(dropLocation.getPath());
        int[] selectionRows = tree.getSelectionRows();
        for (int i = 0; i < selectionRows.length; i++) {
            if (selectionRows[i] == dropRow) {
                return false;
            }
        }
        return true;
    }

    protected Transferable createTransferable(JComponent c) {
        JTree tree = (JTree) c;
        TreePath[] paths = tree.getSelectionPaths();
        if (paths != null) {
            List<TaskNode> copies = new ArrayList<TaskNode>();
            List<TaskNode> toRemove = new ArrayList<TaskNode>();
            TaskNode node = (TaskNode) paths[0].getLastPathComponent();
            TaskNode copy = node.clone();
            copies.add(copy);
            toRemove.add(node);
            for (int i = 1; i < paths.length; i++) {
                TaskNode next = (TaskNode) paths[i].getLastPathComponent();
                if (next.getLevel() < node.getLevel()) {
                    break;
                } else if (next.getLevel() > node.getLevel()) {
                    copy.add(next.clone());
                } else {
                    copies.add(next.clone());
                    toRemove.add(next);
                }
            }
            TaskNode[] nodes = copies.toArray(new TaskNode[copies.size()]);
            nodesToRemove = toRemove.toArray(new TaskNode[toRemove.size()]);
            return new NodesTransferable(nodes);
        }
        return null;
    }

    protected void exportDone(JComponent source, Transferable data, int action) {
        NATOMain.masterTree.clearSelection();
    }

    public int getSourceActions(JComponent c) {
        return MOVE;
    }

    public boolean importData(TransferHandler.TransferSupport support) {
        if (!canImport(support)) {
            return false;
        }
        TaskNode[] nodes = null;
        try {
            Transferable t = support.getTransferable();
            nodes = (TaskNode[]) t.getTransferData(flavors[0]);
        } catch (UnsupportedFlavorException ufe) {
            System.out.println("UnsupportedFlavor: " + ufe.getMessage());
        } catch (java.io.IOException ioe) {
            System.out.println("I/O error: " + ioe.getMessage());
        }
        JTree.DropLocation dl = (JTree.DropLocation) support.getDropLocation();
        int childIndex = dl.getChildIndex();
        TreePath dest = dl.getPath();
        TaskNode parent = (TaskNode) dest.getLastPathComponent();
        TreeUtil.cutTreeSelection(NATOMain.instance, NATOMain.masterTree, NATOMain.clipboard);
        DefaultTreeModel model = (DefaultTreeModel) NATOMain.masterTree.getModel();
        int index = childIndex;
        if (childIndex == -1) {
            index = parent.getChildCount();
        }
        for (int i = 0; i < nodes.length; i++) {
            model.insertNodeInto(nodes[i], parent, index++);
        }
        NATOMain.masterTree.setSelectionRow(0);
        TreeUtil.refreshTreeTaskState();
        return true;
    }

    public String toString() {
        return getClass().getName();
    }

    public class NodesTransferable implements Transferable {

        TaskNode[] nodes;

        public NodesTransferable(TaskNode[] nodes) {
            this.nodes = nodes;
        }

        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
            if (!isDataFlavorSupported(flavor)) throw new UnsupportedFlavorException(flavor);
            return nodes;
        }

        public DataFlavor[] getTransferDataFlavors() {
            return flavors;
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return flavors[0].equals(flavor);
        }
    }
}
