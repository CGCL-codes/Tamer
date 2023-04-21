package org.snipsnap.graph.dimensions.pic;

import java.util.*;
import org.snipsnap.graph.context.*;
import org.snipsnap.graph.dimensions.*;
import org.snipsnap.graph.dimensions.rec.*;
import org.snipsnap.graph.graph.*;

public class ContractRecsForMindMap {

    private Tree tree;

    private Dim[] recDim;

    private GraphRendererContext context;

    private int last;

    private int current;

    private boolean visited = false;

    private TreeNode parentNode;

    private int counter;

    private int f = 2;

    private int numberOfRows;

    public ContractRecsForMindMap(Tree tree, RendererContext context) {
        this.tree = tree;
        this.context = (GraphRendererContext) context;
        this.numberOfRows = tree.getDepth();
    }

    public void contractGraph() {
        TreeNode root = (TreeNode) tree.getRoot();
        recDim = new ConverterForTree(tree).convertNodeDimToRecDim(tree.getRoot(), 1);
        counter = numberOfRows;
        iterateForNoChildren(root, 1);
        for (int i = numberOfRows; i > 2; i--) {
            iterateForParents(root, 1);
            counter--;
        }
        setYFirstNode(root);
        if (root.getChildrenList().size() <= 3) makeLonelyOnesStraight(root);
    }

    public void iterateForNoChildren(TreeNode root, int row) {
        if (row == 2) {
            f = 2;
        }
        ArrayList nodelist = root.getChildrenList();
        Iterator iterator = nodelist.iterator();
        while (iterator.hasNext()) {
            TreeNode node = (TreeNode) iterator.next();
            if ((row + 1) != numberOfRows && node.getChildrenList().size() != 0) {
                iterateForNoChildren(node, row + 1);
            } else {
                if ((row + 1) <= numberOfRows) {
                    current = node.getY() - recDim[row + 1].getHeight() / 2;
                    if (visited == true && current > last) {
                        int diff = current - last - ((PicInfo) context.getPicInfo()).getDSameRow() * f;
                        int move = current - diff;
                        node.setY(move);
                    }
                    last = node.getY() + recDim[row + 1].getHeight();
                    if (visited == false) {
                        visited = true;
                    }
                    f = 1;
                }
            }
        }
    }

    public void iterateForParents(TreeNode root, int row) {
        ArrayList nodelist = root.getChildrenList();
        Iterator iterator = nodelist.iterator();
        while (iterator.hasNext()) {
            TreeNode node = (TreeNode) iterator.next();
            if ((row + 1) != counter) {
                iterateForParents(node, row + 1);
            } else {
                if (!(node.getParent().equals(parentNode))) {
                    parentNode = node.getParent();
                    setParentInMiddleOfChildren(parentNode, row);
                }
            }
        }
    }

    private void setParentInMiddleOfChildren(TreeNode node, int row) {
        int childrenSize = node.getChildrenList().size();
        int coord_firstChild = 0;
        int coord_lastChild = 0;
        int middle = 0;
        coord_firstChild = ((TreeNode) node.getChildrenList().get(0)).getY() + recDim[row + 1].getHeight() / 2;
        coord_lastChild = ((TreeNode) node.getChildrenList().get(childrenSize - 1)).getY() + recDim[row + 1].getHeight() / 2;
        middle = (coord_lastChild + coord_firstChild) / 2;
        node.setY(middle - recDim[row].getHeight() / 2);
    }

    private void setYFirstNode(TreeNode root) {
        ArrayList nodelist = root.getChildrenList();
        int size = nodelist.size();
        TreeNode node;
        int yMin = context.getPicDim().getHeight();
        int yMax = 0;
        for (int i = 0; i < size; i++) {
            int y = ((TreeNode) nodelist.get(i)).getY();
            if (y < yMin) {
                yMin = y;
            }
            if (y > yMax) {
                yMax = y;
            }
        }
        int yMiddle = (yMax + yMin) / 2;
        root.setY(yMiddle);
    }

    private void makeLonelyOnesStraight(TreeNode node) {
        int size = node.getChildrenList().size();
        int y = node.getY() + recDim[1].getHeight() / 2;
        int stop = 0;
        if (size == 2) {
            stop = 1;
        }
        for (int i = 0; i <= stop; i++) {
            TreeNode childNode = (TreeNode) node.getChildrenList().get(i);
            int newY = y - recDim[2].getHeight() / 2;
            int diff = childNode.getY() - (y - recDim[2].getHeight() / 2);
            childNode.setY(newY);
            iterateForStraight(childNode, diff);
        }
    }

    private void iterateForStraight(TreeNode node, int diff) {
        ArrayList nodelist = node.getChildrenList();
        Iterator iterator = nodelist.iterator();
        while (iterator.hasNext()) {
            node = (TreeNode) iterator.next();
            node.setY(node.getY() - diff);
            iterateForStraight(node, diff);
        }
    }
}
