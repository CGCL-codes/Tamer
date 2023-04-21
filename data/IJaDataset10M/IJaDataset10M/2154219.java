package com.germinus.xpression.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author agonzalez
 */
public class PrintableTreeHelper {

    private PrintableTreeHelper() {
    }

    /**
     * Create a list with the nodes of a tree and information relative to
     * its layout to make easy its printing.
     * The root node element is not included in the list.
     * @param tree
     * @return
     */
    public static List<PrintableTreeNode> buildJSPrintableTreeNodes(Tree tree, Boolean printRoot) {
        List<PrintableTreeNode> nodes = new ArrayList<PrintableTreeNode>();
        if (tree == null) {
            return nodes;
        }
        Iterator it = tree.getChildren().iterator();
        boolean subFirst = true;
        if (printRoot) {
            nodes.addAll(buildPrintableTreeNodes(tree, subFirst, !it.hasNext(), 0));
            subFirst = false;
        } else {
            while (it.hasNext()) {
                Tree subTree = (Tree) it.next();
                nodes.addAll(buildPrintableTreeNodes(subTree, subFirst, !it.hasNext(), 0));
                subFirst = false;
            }
        }
        return nodes;
    }

    public static List<PrintableTreeNode> buildJSPrintableTreeNodes(Tree tree) {
        return buildJSPrintableTreeNodes(tree, Boolean.FALSE);
    }

    private static List<PrintableTreeNode> buildPrintableTreeNodes(Tree tree, boolean first, boolean last, int accumulatedLevels) {
        List<PrintableTreeNode> printableNodes = new ArrayList<PrintableTreeNode>();
        if (!last) {
            accumulatedLevels = 0;
        }
        if (tree.getChildren() != null && tree.getChildren().size() != 0) {
            accumulatedLevels++;
        }
        PrintableTreeNode printNode = buildPrintableTreeNode(tree, first, last, accumulatedLevels);
        printableNodes.add(printNode);
        Collection nodeTrees = tree.getChildren();
        if (nodeTrees != null) {
            Iterator iter = nodeTrees.iterator();
            boolean subFirst = true;
            while (iter.hasNext()) {
                Tree subTree = (Tree) iter.next();
                boolean subLast = !iter.hasNext();
                printableNodes.addAll(buildPrintableTreeNodes(subTree, subFirst, subLast, accumulatedLevels));
                subFirst = false;
            }
        }
        return printableNodes;
    }

    private static PrintableTreeNode buildPrintableTreeNode(Tree tree, boolean first, boolean last, int accumulatedLevels) {
        BasicPrintableTreeNode printableNode = new BasicPrintableTreeNode();
        printableNode.setNode(tree.getNode());
        printableNode.setLastSibiling(last);
        printableNode.setFirstSibiling(first);
        boolean isAncestor = tree.getChildren() != null && tree.getChildren().size() > 0;
        printableNode.setAncestor(isAncestor);
        int finishedLevels = 0;
        if (last && !printableNode.isAncestor()) {
            finishedLevels = accumulatedLevels;
        }
        printableNode.setFinishedLevels(new Integer(finishedLevels));
        return printableNode;
    }
}
