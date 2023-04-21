package com.tensegrity.apidemo.gui.treemodel.executionhandler;

import javax.swing.tree.DefaultMutableTreeNode;
import com.tensegrity.apidemo.gui.PaloBrowser;

/**
 * <code>TreeNodeExecutionHandler</code>
 * Whenever a node in the <code>PaloBrowserTree</code> is <i>executed</i>, i.e.
 * a double-click is performed on it, or, as in this application, the user
 * clicks on the node with the right mouse button, the appropriate handle method
 * for the respective type of the node will be executed. This interface
 * provides a generic handle method for all execution handlers. 
 *
 * @author Philipp Bouillon
 * @version $Id: TreeNodeExecutionHandler.java,v 1.1 2007/05/21 13:48:44 PhilippBouillon Exp $
 */
public interface TreeNodeExecutionHandler {

    /**
	 * This method is called for each execution handler and it's its
	 * responsibility to check the node type and take appropriate action.
	 * 
	 * @param browser the <code>PaloBrowser</code> for this handler
	 * @param node the tree node currently being executed.
	 * @return true if an execution handler takes responsibility for this node,
	 * false otherwise.
	 */
    public abstract boolean handle(PaloBrowser browser, DefaultMutableTreeNode node);
}
