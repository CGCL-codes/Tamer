package com.sun.star.addon.sugarcrm.ui.tree;

import org.apache.log4j.Logger;
import com.sun.star.addon.sugarcrm.main.MainWindow;
import com.sun.star.addon.sugarcrm.soap.SugarInfo;
import com.sun.star.addon.sugarcrm.soap.SugarItem;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * UI to show CheckBox trees.
 * @author othman
 */
public class CheckTree extends JTree implements MouseListener {

    private static Logger _logger = Logger.getLogger(CheckTree.class);

    private ArrayList<CheckNode> selectedNodes = null;

    private CheckNode root;

    private TreeSelectionModel selectionModel_;

    private ArrayList<SugarItem> sugarItems;

    /**
     * constructor
     * @param root
     */
    public CheckTree(CheckNode root) {
        super(root);
        this.root = root;
        selectionModel_ = new DefaultTreeSelectionModel();
        selectionModel_.setSelectionMode(TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);
        setSelectionModel(selectionModel_);
        initTree();
    }

    /**
     * this method initialize the Tree with the nodes corresponding to sugar modules Array
     */
    private void initTree() {
        selectedNodes = new ArrayList<CheckNode>();
        if (((SugarInfo.getInstance().getModulesList() == null) || SugarInfo.getInstance().getModulesList().isEmpty())) {
            return;
        }
        for (String module : SugarInfo.getInstance().getModulesList()) {
            CheckNode node = new CheckNode(module);
            root.add(node);
        }
        setCellRenderer(new CheckRenderer());
        expandPath(getPathForRow(0));
        setRootVisible(false);
        addMouseListener(this);
    }

    /**
     * calls method addNodes(ArrayList sugarItems, DefaultMutableTreeNode parent)
     * @param sugarItems List of sugar items to insert as children nodes of Node <I>module</I>
     * @param module Tag of sugar module parent Node
     */
    public void addNodes(ArrayList sugarItems, String module) {
        for (int i = 0; i < root.getChildCount(); i++) {
            CheckNode node = (CheckNode) root.getChildAt(i);
            String value = (String) node.getUserObject();
            if ((value != null) && value.equals(module)) {
                addNodes(sugarItems, node);
                this.sugarItems = sugarItems;
                break;
            }
        }
        expandTree();
    }

    /**
     * this method adds SugarItem Nodes to their corresponding module Node in the Tree
     * @param sugarItems List of sugar items to insert as children nodes of Node <I>module</I>
     * @param parent sugar module parent Node
     */
    private void addNodes(ArrayList sugarItems, CheckNode parent) {
        DefaultTreeModel treeModel_ = (DefaultTreeModel) getModel();
        String module = (String) parent.getUserObject();
        for (int i = 0; i < sugarItems.size(); i++) {
            SugarItem it = (SugarItem) sugarItems.get(i);
            if (it.getItemType().equals(module)) {
                CheckNode node = new CheckNode(it.getName());
                parent.add(node);
                treeModel_.nodeChanged(node);
            }
        }
    }

    /**
     *    Finds SugarInfo Item in Tree corresponding to <I>tag</I> Tree node.
     *    @param tag node tag ofsugar item to find
     *    @return SugarItem object . in Tree it is node <I>tag</I>
     */
    public SugarItem findSugarItem(String tag) {
        if (sugarItems == null) {
            return null;
        }
        SugarItem sItem = null;
        for (int i = 0; i < sugarItems.size(); i++) {
            SugarItem it = (SugarItem) sugarItems.get(i);
            if (it.getName().equals(tag)) {
                sItem = it;
                break;
            }
        }
        return sItem;
    }

    /**
     * <PRE>this method empties the Tree</PRE>
     * <PRE>it removes all children nodes & keeps only the root nodes for sugar modules</PRE>
     */
    private void removeNodes() {
        DefaultTreeModel treeModel_ = (DefaultTreeModel) getModel();
        for (int i = 0; i < root.getChildCount(); i++) {
            CheckNode node = (CheckNode) root.getChildAt(i);
            node.removeAllChildren();
        }
        treeModel_.reload();
    }

    /**
     * adds node to selected nodes list
     * @param node
     */
    private void addSelectedNode(CheckNode node) {
        if (!selectedNodes.contains(node)) {
            selectedNodes.add(node);
        }
    }

    /**
     * removes node from selected nodes list
     * @param node
     * @return
     */
    private boolean removeSelectedNode(CheckNode node) {
        return selectedNodes.remove(node);
    }

    /**
     * returns the lsit of selected SugarInfo Items
     * @return
     */
    public ArrayList<SugarItem> getSelectedItems() {
        ArrayList<SugarItem> items = new ArrayList<SugarItem>();
        for (CheckNode node : selectedNodes) {
            SugarItem item = findSugarItem(node.getUserObject().toString());
            items.add(item);
        }
        return items;
    }

    /**
     * <PRE>this method empties the Tree</PRE>
     * <PRE>it removes all children nodes & keeps only the root nodes for sugar modules</PRE>
     */
    public void clearTree() {
        DefaultTreeModel treeModel_ = (DefaultTreeModel) getModel();
        for (int i = 0; i < root.getChildCount(); i++) {
            CheckNode node = (CheckNode) root.getChildAt(i);
            node.removeAllChildren();
        }
        treeModel_.reload();
    }

    /**
     * clear array lists to reset Tree data state
     */
    public void resetTree() {
        selectedNodes = new ArrayList<CheckNode>();
        sugarItems = new ArrayList<SugarItem>();
    }

    /**
     *
     */
    public void expandTree() {
        for (int i = 0; i < getRowCount(); i++) {
            expandRow(i);
        }
    }

    /**
     * handles Tree Mouse events
     * @param e
     */
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        int row = getRowForLocation(x, y);
        TreePath path = getPathForRow(row);
        if (path == null) {
            return;
        }
        Object o = path.getLastPathComponent();
        if (o instanceof CheckNode) {
            CheckNode node = (CheckNode) o;
            CheckNode parent = (CheckNode) node.getParent();
            if ((parent == null) || parent.equals(getModel().getRoot())) {
                return;
            }
            node.setSelected(!node.isSelected());
            _logger.debug(node + " Node Selected =" + node.isSelected());
            if (node.isSelected()) {
                addSelectedNode(node);
                MainWindow.getInstance().getSugarItemPanel().addItem(node);
            } else {
                removeSelectedNode(node);
                MainWindow.getInstance().getSugarItemPanel().removeItem(node);
            }
            ((DefaultTreeModel) getModel()).nodeChanged(node);
        }
    }

    /**
     *
     * @param arg0
     */
    public void mouseReleased(MouseEvent arg0) {
    }

    /**
     *
     * @param arg0
     */
    public void mouseEntered(MouseEvent arg0) {
    }

    /**
     *
     * @param arg0
     */
    public void mouseExited(MouseEvent arg0) {
    }

    /**
     *
     * @param arg0
     */
    public void mouseClicked(MouseEvent arg0) {
    }

    /**
     *
     * @return
     */
    public ArrayList<CheckNode> getSelectedNodes() {
        return selectedNodes;
    }

    /**
     *
     * @param selectedNodes
     */
    public void setSelectedNodes(ArrayList<CheckNode> selectedNodes) {
        this.selectedNodes = selectedNodes;
    }

    /**
     *
     * @return
     */
    public ArrayList getSugarItems() {
        return sugarItems;
    }

    /**
     *
     * @param sugarItems
     */
    public void setSugarItems(ArrayList sugarItems) {
        this.sugarItems = sugarItems;
    }
}
