package play.ide.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import play.higraph.view.PLAYHigraphView;
import play.higraph.view.PLAYNodeView;
import play.ide.util.Observable;
import play.ide.util.Observer;

/**
 * @author Kai Zhu
 * 
 */
public class NodesOutline extends JTree implements Observer {

    private static final long serialVersionUID = -6060878672788658126L;

    private DefaultMutableTreeNode root;

    private DefaultMutableTreeNode playTreeNode;

    private DefaultMutableTreeNode trashTreeNode;

    private PLAYHigraphView higraphView;

    public NodesOutline(PLAYHigraphView higraphView) {
        this.higraphView = higraphView;
        this.higraphView.addObserver(this);
        this.root = new DefaultMutableTreeNode("World");
        this.setModel(new DefaultTreeModel(this.root));
        this.playTreeNode = new DefaultMutableTreeNode("PLAY");
        this.root.add(this.playTreeNode);
        this.trashTreeNode = new DefaultMutableTreeNode("Trash");
        this.root.add(this.trashTreeNode);
        this.setCellRenderer(new NodesTreeCellRender());
        this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        this.expandRow(0);
        this.expandRow(1);
        this.setShowsRootHandles(true);
    }

    /**
     * @see play.ide.util.Observer#update(play.ide.util.Observable,
     *      java.lang.Object)
     */
    @Override
    public void update(Observable o, Object arg) {
        this.playTreeNode.removeAllChildren();
        ((DefaultTreeModel) super.treeModel).removeNodeFromParent(this.playTreeNode);
        ((DefaultTreeModel) super.treeModel).insertNodeInto(this.playTreeNode, this.root, 0);
        Iterator<?> iterator = this.higraphView.getTops();
        while (iterator.hasNext()) {
            PLAYNodeView nodeView = (PLAYNodeView) iterator.next();
            this.addPlayTreeNode(nodeView, this.playTreeNode);
        }
        this.trashTreeNode.removeAllChildren();
        ((DefaultTreeModel) super.treeModel).removeNodeFromParent(this.trashTreeNode);
        ((DefaultTreeModel) super.treeModel).insertNodeInto(this.trashTreeNode, this.root, 1);
        if (arg != null) {
            if (arg instanceof List) {
                ArrayList<?> nodeViewList = ((ArrayList<?>) arg);
                for (Object object : nodeViewList) {
                    PLAYNodeView nodeView = (PLAYNodeView) object;
                    DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(nodeView.getNode().getTag());
                    ((DefaultTreeModel) super.treeModel).insertNodeInto(treeNode, this.trashTreeNode, this.trashTreeNode.getChildCount());
                }
            }
        }
        this.setSelectionRow(1);
    }

    private void addPlayTreeNode(PLAYNodeView nodeView, DefaultMutableTreeNode parentTreeNode) {
        DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(nodeView.getNode().getTag());
        ((DefaultTreeModel) super.treeModel).insertNodeInto(treeNode, parentTreeNode, parentTreeNode.getChildCount());
        for (int i = 0; i < nodeView.getNumChildren(); i++) {
            this.addPlayTreeNode((PLAYNodeView) nodeView.getChild(i), treeNode);
        }
    }
}
