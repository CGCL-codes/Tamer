package es.aeat.eett.rubik.treeNavi;

import java.awt.Point;
import java.awt.dnd.DnDConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import com.tonbeller.jpivot.olap.model.Hierarchy;
import com.tonbeller.jpivot.olap.model.Member;
import com.tonbeller.jpivot.olap.model.OlapModel;
import com.tonbeller.jpivot.olap.navi.ChangeSlicer;
import swingUtil.dragANDrop.tree.AbstractTreeTransferHandler;
import swingUtil.dragANDrop.tree.DNDTree;

class TreeTransferHandler extends AbstractTreeTransferHandler {

    TreeTransferHandler(DNDTree tree, int action) {
        super(tree, action, true);
    }

    public boolean canPerformAction(DNDTree target, DefaultMutableTreeNode draggedNode, int action, Point location) {
        TreePath pathTarget = target.getPathForLocation(location.x, location.y);
        if (pathTarget == null) {
            target.setSelectionPath(null);
            return (false);
        }
        target.setSelectionPath(pathTarget);
        if (action == DnDConstants.ACTION_COPY) {
            return (false);
        } else if (action == DnDConstants.ACTION_MOVE) {
            DefaultMutableTreeNode dropNode = (DefaultMutableTreeNode) pathTarget.getLastPathComponent();
            if (draggedNode.isRoot() || dropNode == draggedNode.getParent() || draggedNode.isNodeDescendant(dropNode) || (!movingHierarchies(draggedNode, dropNode) && (draggedNode.getLevel() < 2 || draggedNode.getParent() != dropNode.getParent()))) {
                return (false);
            } else {
                if (movingHierarchies(draggedNode, dropNode) && !canMoveHierarchy(draggedNode)) {
                    return (false);
                }
                return (true);
            }
        } else {
            return (false);
        }
    }

    public boolean executeDrop(DNDTree target, DefaultMutableTreeNode draggedNode, DefaultMutableTreeNode dropNode, int action) {
        if (action == DnDConstants.ACTION_COPY) {
            return (false);
        }
        if (action == DnDConstants.ACTION_MOVE) {
            if (!movingHierarchies(draggedNode, dropNode)) {
                DefaultMutableTreeNode nParent = (DefaultMutableTreeNode) dropNode.getParent();
                draggedNode.removeFromParent();
                ((DefaultTreeModel) target.getModel()).insertNodeInto(draggedNode, nParent, getIndex(dropNode));
            } else {
                draggedNode.removeFromParent();
                if (dropNode.getLevel() == 1) {
                    CubeNode cNode = (CubeNode) dropNode.getRoot();
                    if (cNode.getChildAt(CubeNode.SLICER_AXIS) == dropNode) {
                        cNode.addExpanded((Hierarchy) draggedNode.getUserObject());
                        clearSelected((MemberTreeNode) draggedNode);
                    }
                    ((DefaultTreeModel) target.getModel()).insertNodeInto(draggedNode, dropNode, dropNode.getChildCount());
                } else {
                    ((DefaultTreeModel) target.getModel()).insertNodeInto(draggedNode, (MutableTreeNode) dropNode.getParent(), getIndex(dropNode));
                }
            }
            return (true);
        }
        return (false);
    }

    private void clearSelected(MemberTreeNode n) {
        if (n instanceof CheckNode && ((CheckNode) n).isSelected()) {
            ((CheckNode) n).setSelected(false);
            ((DefaultTreeModel) ((DNDTree) target).getModel()).nodeChanged(n);
        }
        if (n.isHasLoaded()) {
            int size = n.getChildCount();
            for (int i = 0; i < size; i++) {
                MemberTreeNode nChild = (MemberTreeNode) n.getChildAt(i);
                clearSelected(nChild);
            }
        }
    }

    private int getIndex(DefaultMutableTreeNode node) {
        int index = 0;
        DefaultMutableTreeNode nParent = (DefaultMutableTreeNode) node.getParent();
        int nChildren = nParent.getChildCount();
        for (int i = 0; i < nChildren; i++) {
            if (nParent.getChildAt(i) == node) {
                index = i;
                break;
            }
        }
        return index;
    }

    private boolean canMoveHierarchy(DefaultMutableTreeNode node) {
        DefaultMutableTreeNode nParent = (DefaultMutableTreeNode) node.getParent();
        if ((TreeNavi.NAME_COLS == nParent.getUserObject() || TreeNavi.NAME_ROWS == nParent.getUserObject()) && nParent.getChildCount() == 1) {
            return false;
        } else if (TreeNavi.NAME_FILTER == nParent.getUserObject()) {
            Hierarchy hierarchy = (Hierarchy) node.getUserObject();
            if (isInSlicer(hierarchy)) {
                return false;
            }
        }
        return true;
    }

    /**
	 * @param hierarchy
	 * @return true si la hierarchy esta en el Slicer
	 */
    private boolean isInSlicer(Hierarchy hierarchy) {
        OlapModel olapModel = ((TreeNavi) target).getOlapModel();
        ChangeSlicer mcs = (ChangeSlicer) olapModel.getExtension(ChangeSlicer.ID);
        Member[] ms = mcs.getSlicer();
        for (int i = 0; i < ms.length; i++) {
            if (ms[i].getLevel().getHierarchy().equals(hierarchy)) return true;
        }
        return false;
    }

    private boolean movingHierarchies(DefaultMutableTreeNode draggedNode, DefaultMutableTreeNode dropNode) {
        return ((dropNode.getLevel() == 2 || dropNode.getLevel() == 1) && draggedNode.getLevel() == 2);
    }

    public boolean canPerformAction(Object draggedObject, int action, Point location) {
        return canPerformAction((DNDTree) target, (DefaultMutableTreeNode) draggedObject, action, location);
    }

    public boolean executeDrop(Object draggedObject, Object droppedObject, int action) {
        return executeDrop((DNDTree) target, (DefaultMutableTreeNode) draggedObject, (DefaultMutableTreeNode) droppedObject, action);
    }
}
