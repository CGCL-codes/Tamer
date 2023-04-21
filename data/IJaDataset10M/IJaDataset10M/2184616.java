package vmap.view.mindmapview;

import vmap.main.VmapMain;
import java.awt.LayoutManager;
import java.awt.Container;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.util.LinkedList;
import java.util.ListIterator;
import java.lang.Math;
import javax.swing.JLabel;

/**
 * This class will Layout the Nodes and Edges of an MapView.
 */
public class MindMapLayout implements LayoutManager {

    private final int BORDER = 30;

    private final int HGAP_BASE = 20;

    private final int VGAP = 3;

    private MapView map;

    private int ySize;

    private int totalXSize;

    public MindMapLayout(MapView map) {
        this.map = map;
        ySize = Integer.parseInt(getFrame().getProperty("mapysize"));
        totalXSize = Integer.parseInt(getFrame().getProperty("mapxsize"));
    }

    public void addLayoutComponent(String name, Component comp) {
    }

    public void removeLayoutComponent(Component comp) {
    }

    public void layoutContainer(Container parent) {
        layout(map.getRoot());
    }

    /**
     * Make abolute positioning for all nodes providing that relative and heights
     * are up to date.
     */
    public void layout() {
        layout(map.getRoot());
    }

    /**
     * This places the node's subtree if the relative
     * position of every node to its parent is known.
     */
    private void layout(NodeView node) {
        int x = 0;
        int hgap = map.getZoomed(HGAP_BASE);
        if (node.isRoot()) {
            x = 0;
        } else if (node.isLeft()) {
            x = -hgap - node.getPreferredSize().width;
        } else {
            x = node.getParentView().getPreferredSize().width + hgap;
        }
        placeNode(node, x, node.relYPos);
        for (ListIterator e = node.getChildrenViews().listIterator(); e.hasNext(); ) {
            layout((NodeView) e.next());
        }
    }

    /**
     * Set the position and the size of the node.
     * Set the position of the edge of the node (the edge of the node is the edge
     * connecting the node to its parent).
     *
     * Preconditions: Absolute position of the parent is already set correctly.
     *                Relative positions and TreeHeights are up to dare.
     */
    private void placeNode(NodeView node, int relativeX, int relativeY) {
        if (node.isRoot()) {
            node.setBounds(totalXSize / 2 - node.getPreferredSize().width / 2, ySize / 2 - node.getPreferredSize().height / 2, node.getPreferredSize().width, node.getPreferredSize().height);
        } else {
            int x = node.getParentView().getLocation().x + relativeX;
            int y = node.getParentView().getLocation().y + relativeY;
            if (x < 0 || x + node.getPreferredSize().width > map.getSize().width) {
                if (node.isLeft()) {
                    resizeMap(x);
                } else {
                    resizeMap(x + node.getPreferredSize().width);
                }
                return;
            }
            node.setBounds(x, y, node.getPreferredSize().width, node.getPreferredSize().height);
            JLabel label = node.getEdge().getLabel();
            Point start = node.getParentView().getOutPoint();
            Point end = node.getInPoint();
            if (node.getParentView().isRoot()) {
                if (node.isLeft()) {
                    start = node.getParentView().getInPoint();
                }
            }
            node.getEdge().start = start;
            node.getEdge().end = end;
            int relX = (start.x - end.x) / 2;
            int absX = start.x - relX;
            int relY = (start.y - end.y) / 2;
            int absY = start.y - relY;
            label.setBounds(absX - label.getPreferredSize().width / 2, absY - label.getPreferredSize().height / 2, label.getPreferredSize().width, label.getPreferredSize().height);
        }
    }

    /**
     *
     */
    public void resizeMap(int outmostX) {
        int oldTotalXSize = totalXSize;
        totalXSize = BORDER * 2 + (outmostX < 0 ? totalXSize + -outmostX : outmostX);
        getMapView().setSize(totalXSize, ySize);
        getMapView().scrollBy((totalXSize - oldTotalXSize) / 2, 0);
        layout(map.getRoot());
    }

    void updateTreeHeightsAndRelativeYOfDescendantsAndAncestors(NodeView node) {
        updateTreeHeightsAndRelativeYOfDescendants(node);
        updateTreeHeightsAndRelativeYOfAncestors(node);
    }

    /**
     * This is called by treeNodesChanged(), treeNodesRemoved() & treeNodesInserted(), so it's the
     * standard mechanism to update the graphical node structure. It updates the parent of the 
     * significant node, and follows recursivly the hierary upwards to root.
     */
    void updateTreeHeightsAndRelativeYOfAncestors(NodeView node) {
        if (node.isRoot()) {
            updateRelativeYOfChildren(node);
        } else {
            updateTreeHeightFromChildren(node);
            updateRelativeYOfChildren(node);
            updateTreeHeightsAndRelativeYOfAncestors(node.getParentView());
        }
    }

    void updateTreeHeightsAndRelativeYOfWholeMap() {
        updateTreeHeightsAndRelativeYOfDescendants(getRoot());
    }

    void updateTreeHeightsAndRelativeYOfDescendants(NodeView node) {
        for (ListIterator e = node.getChildrenViews().listIterator(); e.hasNext(); ) {
            updateTreeHeightsAndRelativeYOfDescendants((NodeView) e.next());
        }
        updateTreeHeightFromChildren(node);
        updateRelativeYOfChildren(node);
    }

    private void updateRelativeYOfChildren(NodeView node) {
        if (node.isRoot()) {
            int pointer = -(sumOfAlreadyComputedTreeHeights(getRoot().getLeft()) / 2);
            for (ListIterator e = getRoot().getLeft().listIterator(); e.hasNext(); ) {
                NodeView child = (NodeView) e.next();
                pointer += (child.getTreeHeight() / 2);
                child.relYPos = pointer - 2;
                pointer += (child.getTreeHeight() / 2);
            }
            pointer = -(sumOfAlreadyComputedTreeHeights(getRoot().getRight()) / 2);
            for (ListIterator e = getRoot().getRight().listIterator(); e.hasNext(); ) {
                NodeView child = (NodeView) e.next();
                pointer += (child.getTreeHeight() / 2);
                child.relYPos = pointer - 2;
                pointer += (child.getTreeHeight() / 2);
            }
        } else {
            int pointer = (node.getPreferredSize().height - node.getTreeHeight()) / 2;
            if (node.getModel().getCloud() != null) {
                pointer = (int) (pointer + 15 * getMapView().getZoom());
            }
            ListIterator it = node.getChildrenViews().listIterator();
            while (it.hasNext()) {
                NodeView child = (NodeView) it.next();
                child.relYPos = pointer + (child.getTreeHeight() - child.getPreferredSize().height) / 2 - 2;
                pointer += child.getTreeHeight();
            }
        }
    }

    private int sumOfAlreadyComputedTreeHeights(LinkedList v) {
        if (v == null || v.size() == 0) {
            return 0;
        }
        int height = 0;
        for (ListIterator e = v.listIterator(); e.hasNext(); ) {
            NodeView node = (NodeView) e.next();
            if (node != null) {
                height += node.getTreeHeight();
            }
        }
        return height;
    }

    protected void updateTreeHeightFromChildren(NodeView node) {
        int iHeight = Math.max(sumOfAlreadyComputedTreeHeights(node.getChildrenViews()), node.getPreferredSize().height + VGAP);
        if (node.getModel().getCloud() != null) {
            iHeight = (int) (iHeight + 30 * getMapView().getZoom());
        }
        node.setTreeHeight(iHeight);
    }

    private RootNodeView getRoot() {
        return (RootNodeView) map.getRoot();
    }

    private MapView getMapView() {
        return map;
    }

    private VmapMain getFrame() {
        return map.getController().getFrame();
    }

    public Dimension minimumLayoutSize(Container parent) {
        return new Dimension(200, 200);
    }

    public Dimension preferredLayoutSize(Container parent) {
        return new Dimension(totalXSize, ySize);
    }
}
