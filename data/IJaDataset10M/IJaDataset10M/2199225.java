package ijgen.generator.gui.components;

import ijgen.generator.gui.commands.impl.misc.CommandNodeSelected;
import ijgen.generator.gui.util.Mediator;
import java.awt.Dimension;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

/**
 * @author Detelin Zlatev
 *
 */
public class ModelsBrowser {

    /**
	 * Serial UID
	 */
    private static final long serialVersionUID = 2629405262570847941L;

    /**
	 * 
	 */
    private JTree modelsTree;

    /**
	 * 
	 */
    private JScrollPane treeView;

    /**
	 * 
	 */
    private Mediator mediator;

    /**
	 * 
	 */
    private DefaultMutableTreeNode selectedNode;

    /**
	 * 
	 */
    public ModelsBrowser() {
        this.createDefaulJTree();
        treeView = new JScrollPane(modelsTree);
        treeView.setPreferredSize(new Dimension(300, 400));
    }

    /**
	 * 
	 * @return
	 */
    public Mediator getMediator() {
        return mediator;
    }

    /**
	 * 
	 * @param mediator
	 */
    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
        CommandNodeSelected commandNodeSelected = new CommandNodeSelected();
        commandNodeSelected.setMediator(mediator);
        modelsTree.addTreeSelectionListener(commandNodeSelected);
        modelsTree.setCellRenderer(new IJTreeCellRenderer());
    }

    /**
	 * 
	 * @return
	 */
    public JTree getModelsTree() {
        return modelsTree;
    }

    /**
	 * 
	 * @return
	 */
    public DefaultMutableTreeNode getSelectedNode() {
        return selectedNode;
    }

    /**
	 * 
	 * @param selectedNode
	 */
    public void setSelectedNode(DefaultMutableTreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    /**
	 * 
	 * @param modelsTreeNodes
	 */
    public void setTreeNodes(DefaultMutableTreeNode modelsTreeNodes) {
        this.modelsTree.setModel(new DefaultTreeModel(modelsTreeNodes, false));
        this.treeView.repaint();
    }

    /**
	 * 
	 */
    public void createDefaulJTree() {
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("Create new or open existing project");
        modelsTree = new JTree(top);
        modelsTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    }

    public JScrollPane getTreeView() {
        return treeView;
    }
}
