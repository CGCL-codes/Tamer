package org.logitest.ui.undo;

import java.util.List;
import java.util.Iterator;
import javax.swing.tree.*;
import javax.swing.undo.*;
import org.logitest.*;
import org.logitest.ui.*;

/**	Edit for pasting a resource into the test tree.

	@author Anthony Eden
*/
public class PasteResourceEdit extends AbstractUndoableEdit {

    /**	Construct a new edit for the target tree model and node.
	
		@param testTreeModel The TreeModel
		@param node The parent node
		@param resource The resource to paste
	*/
    public PasteResourceEdit(TestTreeModel testTreeModel, DefaultMutableTreeNode node, Resource resource) {
        this.testTreeModel = testTreeModel;
        this.node = node;
        this.test = (Test) node.getUserObject();
        this.resource = resource;
    }

    /**	Execute the paste. */
    public void execute() {
        List resources = test.getResources();
        index = resources.size();
        resources.add(resource);
        DefaultMutableTreeNode resourceNode = new DefaultMutableTreeNode(resource);
        node.add(resourceNode);
        Iterator i = resource.getChildren().iterator();
        while (i.hasNext()) {
            ResourceChild resourceChild = (ResourceChild) i.next();
            resourceNode.add(new DefaultMutableTreeNode(resourceChild));
        }
        testTreeModel.nodeStructureChanged(node);
    }

    /**	Undo the paste.
	
		@throws CannotUndoException
	*/
    public void undo() throws CannotUndoException {
        super.undo();
        test.getResources().remove(resource);
        node.remove(index);
        testTreeModel.nodeStructureChanged(node);
    }

    /**	Redo the paste.
	
		@throws CannotRedoException
	*/
    public void redo() throws CannotRedoException {
        super.redo();
        execute();
    }

    private TestTreeModel testTreeModel;

    private DefaultMutableTreeNode node;

    private Test test;

    private Resource resource;

    private int index;
}
