package uk.org.windswept.treesize;

import java.awt.GridLayout;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.apache.log4j.Logger;
import uk.org.windswept.util.NumberMemoryFormatter;

public class TreePanel extends JPanel implements TreeModelListener {

    static Logger LOGGER = Logger.getLogger(TreePanel.class);

    private JTree tree;

    DefaultMutableTreeNode rootNode;

    DefaultTreeModel treeModel;

    public TreePanel() {
        super(new GridLayout(1, 0));
        rootNode = new DefaultMutableTreeNode("Root Node");
        treeModel = new DefaultTreeModel(rootNode);
        treeModel.addTreeModelListener(this);
        tree = new JTree(treeModel);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setShowsRootHandles(true);
        JScrollPane treeView = new JScrollPane(tree);
        add(treeView);
    }

    Map<File, DefaultMutableTreeNode> nodes = new HashMap<File, DefaultMutableTreeNode>();

    Map<DefaultMutableTreeNode, Long> dirSizes = new HashMap<DefaultMutableTreeNode, Long>();

    public void updateDirectoryNodeSize(File dir, long size) {
        LOGGER.debug("dir=" + dir + " size=" + size);
        DefaultMutableTreeNode node = nodes.get(dir);
        dirSizes.put(node, new Long(size));
        node.setUserObject(dir.getName() + " - " + NumberMemoryFormatter.formatAsMemorySize(size));
        MutableTreeNode parent = (MutableTreeNode) node.getParent();
        if (parent != null) {
            treeModel.removeNodeFromParent(node);
            LOGGER.debug("Parent has " + parent.getChildCount() + " child nodes");
            int index;
            for (index = 0; index < parent.getChildCount(); index++) {
                TreeNode otherNode = parent.getChildAt(index);
                long otherSize = dirSizes.get(otherNode).longValue();
                if (size > otherSize) break;
            }
            LOGGER.debug("Inserting node at index " + index);
            treeModel.insertNodeInto(node, parent, index);
            treeModel.nodeStructureChanged(parent);
        } else {
            treeModel.nodeChanged(node);
        }
    }

    public void addDirectoryNode(File dir) {
        if (!nodes.containsKey(dir)) {
            DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(dir.getName());
            nodes.put(dir, newNode);
            DefaultMutableTreeNode parentNode = nodes.get(dir.getParentFile());
            if (parentNode == null) parentNode = rootNode;
            treeModel.insertNodeInto(newNode, parentNode, parentNode.getChildCount());
            if (newNode.getLevel() < 3) {
                TreePath path = new TreePath(newNode.getPath());
                tree.expandPath(path);
                tree.scrollPathToVisible(path);
            }
        }
    }

    public void treeNodesChanged(TreeModelEvent arg0) {
    }

    public void treeNodesInserted(TreeModelEvent arg0) {
    }

    public void treeNodesRemoved(TreeModelEvent arg0) {
    }

    public void treeStructureChanged(TreeModelEvent arg0) {
    }
}
