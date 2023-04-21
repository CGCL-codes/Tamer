package net.frede.gui.file;

import java.io.File;
import javax.swing.tree.TreeNode;
import net.frede.gui.gui.explorer.SelectionExplorer;
import net.frede.gui.gui.explorer.tree.TreeParent;

public class ExplorerFile extends SelectionExplorer {

    /**
	 * default constructor
	 */
    public ExplorerFile() {
        super();
    }

    public File getSelectedDirectory() {
        File back = null;
        TreeParent tp = getTrees().getActiveTree();
        if (tp != null) {
            NodeFile node = null;
            try {
                node = (NodeFile) tp.getSelectedNode();
                back = node.getFile();
                if (getLogger().isDebugEnabled()) {
                    getLogger().debug("selecting dir from node " + node);
                }
            } catch (ClassCastException e_cc) {
            }
            if (node == null) {
                try {
                    node = (NodeFile) tp.getRoot();
                    back = node.getFile();
                    if (getLogger().isDebugEnabled()) {
                        getLogger().debug("selecting dir from root");
                    }
                } catch (ClassCastException e_cc) {
                    TreeNode tn = tp.getRoot();
                    getLogger().warn(tn.getClass() + " " + tn.toString() + "is not a NodeFile instance");
                }
            }
            getLogger().info("selected dir is " + back);
        } else {
            back = new File(System.getProperty("user.dir"));
            getLogger().info("no active Tree");
        }
        return back;
    }

    protected void leafClicked(TreeNode node) {
        getLogger().warn("clicked on " + node);
        if (node instanceof NodeFile) {
            FileManager.currentManager().process(getSelectedDirectory(), sourceValue(node));
        }
    }
}
