package net.confex.schema.command;

import java.util.ArrayList;
import java.util.List;
import net.confex.schema.model.IModelElementContainer;
import net.confex.schema.model.ModelConnection;
import net.confex.schema.model.Schema;
import net.confex.schema.model.SimpleContainer;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

public class DeleteSimpleContainerCommand extends Command {

    private SimpleContainer container;

    private IModelElementContainer parent_container;

    private List foreignKeyRelationships = new ArrayList();

    private List primaryKeyRelationships = new ArrayList();

    private Rectangle bounds;

    private void deleteRelationships(SimpleContainer t) {
        this.foreignKeyRelationships.addAll(t.getOutConnections());
        for (int i = 0; i < foreignKeyRelationships.size(); i++) {
            ModelConnection r = (ModelConnection) foreignKeyRelationships.get(i);
            r.getInElement().removeInConnections(r);
            t.removeOutConnections(r);
        }
        this.primaryKeyRelationships.addAll(t.getInConnections());
        for (int i = 0; i < primaryKeyRelationships.size(); i++) {
            ModelConnection r = (ModelConnection) primaryKeyRelationships.get(i);
            r.getOutElement().removeOutConnections(r);
            t.removeInConnections(r);
        }
    }

    /**
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
    public void execute() {
        primExecute();
    }

    /**
	 * Invokes the execution of this command.
	 */
    protected void primExecute() {
        deleteRelationships(container);
        parent_container.removeModelElement(container);
    }

    /**
	 * @see org.eclipse.gef.commands.Command#redo()
	 */
    public void redo() {
        primExecute();
    }

    private void restoreRelationships() {
        for (int i = 0; i < foreignKeyRelationships.size(); i++) {
            ModelConnection r = (ModelConnection) foreignKeyRelationships.get(i);
            r.getOutElement().addOutConnections(r);
            r.getInElement().addInConnections(r);
        }
        foreignKeyRelationships.clear();
        for (int i = 0; i < primaryKeyRelationships.size(); i++) {
            ModelConnection r = (ModelConnection) primaryKeyRelationships.get(i);
            r.getOutElement().addOutConnections(r);
            r.getInElement().addInConnections(r);
        }
        primaryKeyRelationships.clear();
    }

    /**
	 * Sets the child to the passed Table
	 * 
	 * @param a
	 *            the child
	 */
    public void setSimpleContainer(SimpleContainer a) {
        container = a;
    }

    /**
	 * Sets the parent to the passed Schema
	 * 
	 * @param sa
	 *            the parent
	 */
    public void setContainer(IModelElementContainer sa) {
        parent_container = sa;
    }

    /**
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
    public void undo() {
        parent_container.addModelElement(container);
        restoreRelationships();
        container.modifyBounds(bounds);
    }

    /**
	 * Sets the original bounds for the container so that these can be restored
	 */
    public void setOriginalBounds(Rectangle bounds) {
        this.bounds = bounds;
    }
}
