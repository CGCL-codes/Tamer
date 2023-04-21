package javax.help;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.util.*;
import java.text.*;

/**
 * Sort merge type
 *
 * @author Richard Gregor
 * @version	1.12	10/30/06
 */
public class SortMerge extends Merge {

    /**
     * Constructs SortMerge
     *
     * @param master The master NavigatorView
     * @param slave The slave NavigatorView
     */
    public SortMerge(NavigatorView master, NavigatorView slave) {
        super(master, slave);
    }

    /**
     * Processes sort merge 
     *
     * @param node The master node (This node must be sorted)
     * @return Merged master node
     */
    public TreeNode processMerge(TreeNode node) {
        debug("processMerge started");
        DefaultMutableTreeNode masterNode = (DefaultMutableTreeNode) node;
        if (masterNode.equals(slaveTopNode)) {
            return masterNode;
        }
        if (slaveTopNode.getChildCount() == 0) {
            return masterNode;
        }
        if (masterNode.getChildCount() == 0) {
            MergeHelpUtilities.mergeNodeChildren("javax.help.SortMerge", slaveTopNode);
            while (slaveTopNode.getChildCount() > 0) {
                masterNode.add((DefaultMutableTreeNode) slaveTopNode.getFirstChild());
            }
            return masterNode;
        }
        mergeNodes(masterNode, slaveTopNode);
        debug("process merge ended");
        return masterNode;
    }

    /**
     * Merge Nodes. Merge two nodes according to the Sort merging rules 
     *
     * @param masterNode The master node to merge with 
     * @param slaveNode The node to merge into the master
     */
    public static void mergeNodes(TreeNode master, TreeNode slave) {
        debug("mergeNodes started");
        DefaultMutableTreeNode masterNode = (DefaultMutableTreeNode) master;
        DefaultMutableTreeNode slaveNode = (DefaultMutableTreeNode) slave;
        sortNode(slaveNode, MergeHelpUtilities.getLocale(slaveNode));
        int masterCnt = masterNode.getChildCount();
        int m = 0;
        DefaultMutableTreeNode masterAtM = null;
        if (masterCnt > 0) {
            masterAtM = (DefaultMutableTreeNode) masterNode.getChildAt(m);
        }
        DefaultMutableTreeNode slaveNodeChild = null;
        while (slaveNode.getChildCount() > 0 && masterAtM != null) {
            slaveNodeChild = (DefaultMutableTreeNode) slaveNode.getFirstChild();
            int compareVal = MergeHelpUtilities.compareNames(masterAtM, slaveNodeChild);
            if (compareVal < 0) {
                ++m;
                if (m >= masterCnt) {
                    break;
                }
                masterAtM = (DefaultMutableTreeNode) masterNode.getChildAt(m);
                continue;
            } else if (compareVal > 0) {
                masterNode.add(slaveNodeChild);
                MergeHelpUtilities.mergeNodeChildren("javax.help.SortMerge", slaveNodeChild);
                continue;
            } else {
                if (MergeHelpUtilities.haveEqualID(masterAtM, slaveNodeChild)) {
                    MergeHelpUtilities.mergeNodes("javax.help.SortMerge", masterAtM, slaveNodeChild);
                    slaveNodeChild.removeFromParent();
                    slaveNodeChild = null;
                } else {
                    MergeHelpUtilities.markNodes(masterAtM, slaveNodeChild);
                    masterNode.add(slaveNodeChild);
                    MergeHelpUtilities.mergeNodeChildren("javax.help.SortMerge", slaveNodeChild);
                }
            }
        }
        while (slaveNode.getChildCount() > 0) {
            slaveNodeChild = (DefaultMutableTreeNode) slaveNode.getFirstChild();
            masterNode.add(slaveNodeChild);
            MergeHelpUtilities.mergeNodeChildren("javax.help.SortMerge", slaveNodeChild);
        }
        mergeNodeChildren(masterNode);
        debug("mergeNode ended");
    }

    /**
     * Merge Node Children. Merge the children of a node according to the
     * Sort merging rules.
     *
     * @param node The parent node from which the children are merged
     */
    public static void mergeNodeChildren(TreeNode node) {
        DefaultMutableTreeNode masterNode = (DefaultMutableTreeNode) node;
        debug("mergeNodeChildren master=" + MergeHelpUtilities.getNodeName(masterNode));
        sortNode(masterNode, MergeHelpUtilities.getLocale(masterNode));
        for (int i = 0; i < masterNode.getChildCount(); i++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) masterNode.getChildAt(i);
            if (!child.isLeaf()) {
                MergeHelpUtilities.mergeNodeChildren("javax.help.SortMerge", child);
            }
        }
    }

    /**
     * Sorts children of node using Array.sort 
     *
     * @param node The node to sort
     * @param locale The locale
     * @return Sorted node
     */
    public static void sortNode(DefaultMutableTreeNode node, Locale locale) {
        debug("sortNode");
        if (locale == null) {
            locale = Locale.getDefault();
        }
        CollationKey temp;
        int size = node.getChildCount();
        DefaultMutableTreeNode sortedNode = new DefaultMutableTreeNode();
        Collator collator = Collator.getInstance(locale);
        CollationKey[] keys = new CollationKey[size];
        for (int i = 0; i < size; i++) {
            String string = MergeHelpUtilities.getNodeName((DefaultMutableTreeNode) node.getChildAt(i));
            debug("String , i:" + string + " , " + i);
            keys[i] = collator.getCollationKey(string);
        }
        Arrays.sort(keys);
        for (int i = 0; i < size; i++) {
            DefaultMutableTreeNode child = MergeHelpUtilities.getChildWithName(node, keys[i].getSourceString());
            if (child != null) {
                sortedNode.add(child);
            }
        }
        while (sortedNode.getChildCount() > 0) {
            node.add((DefaultMutableTreeNode) sortedNode.getFirstChild());
        }
        debug("end sortNode");
    }

    private static boolean debug = false;

    private static void debug(String msg) {
        if (debug) {
            System.out.println("SortMerge :" + msg);
        }
    }
}
