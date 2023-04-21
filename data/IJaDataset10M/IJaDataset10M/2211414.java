package org.webguitoolkit.ui.controls.tree;

import java.io.Serializable;

/**
 * <b>The TreeListener is used as a callback for user input on tree nodes</b>
 * 
 * <pre>
 * public class MyTreeListener implements ITreeListener{
 *   public void onTreeNodeClicked( ITree tree, String nodeId ){
 *     tree.getPage().sendInfo("Tree Node Clicked: "+ nodeId );
 *   }
 *   public void on...
 *   
 *   ...
 * }
 * //set the Tree's TreeListener
 * tree.setListener( new MyTreeListener() );
 * </pre>
 * <p>
 * The AbstractTreeListener implements all methods with standard behavior except the onTreeNodeClicked(), it is recommended to use
 * the AbstractTreeListener as base class for the listener.
 * </p>
 * 
 * @author Martin
 * 
 * @see AbstractTreeListener
 */
public interface ITreeListener extends Serializable {

    /**
	 * <b>This method is called when a node is clicked.</b><br>
	 * It it's the most interesting one for applications.
	 * 
	 * @param tree the tree
	 * @param nodeId the clicked node id
	 */
    void onTreeNodeClicked(ITree tree, String nodeId);

    /**
	 * This method is called when a folder is opened.
	 * 
	 * @param tree the tree
	 * @param nodeId the node id of the folder
	 */
    void onTreeNodeOpen(ITree tree, String nodeId);

    /**
	 * This method is called when a node is dropped on an other one.
	 * 
	 * @param tree the tree
	 * @param nodeId the node on which the other dropped (target)
	 * @param droppedId the node that is dropped
	 */
    void onTreeNodeDropped(ITree tree, String nodeId, String droppedId);

    /**
	 * This method is called when the dragging of a node starts.
	 * 
	 * @param tree the tree
	 * @param nodeId the dragged node id
	 */
    void onTreeNodeDragged(ITree tree, String nodeId);

    /**
	 * This method is called when a nodes check box has been clicked.
	 * 
	 * @param tree the tree
	 * @param nodeId the checked node id
	 */
    void onTreeNodeChecked(ITree tree, String nodeId);
}
