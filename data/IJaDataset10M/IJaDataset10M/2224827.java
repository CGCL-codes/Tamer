package net.confex.schema.command;

import net.confex.schema.model.ModelConnection;
import net.confex.schema.model.NodeElement;
import org.eclipse.gef.commands.Command;

public class DeleteConnectionCommand extends Command {

    private NodeElement out_element;

    private NodeElement in_element;

    private ModelConnection relationship;

    public DeleteConnectionCommand(NodeElement foreignKeySource, NodeElement primaryKeyTarget, ModelConnection relationship) {
        super();
        this.out_element = foreignKeySource;
        this.in_element = primaryKeyTarget;
        this.relationship = relationship;
    }

    /**
	 * @see Removes the relationship
	 */
    public void execute() {
        out_element.removeOutConnections(relationship);
        in_element.removeInConnections(relationship);
        relationship.setOutElement(null);
        relationship.setInElement(null);
    }

    /**
	 * @see Restores the relationship
	 */
    public void undo() {
        relationship.setOutElement(out_element);
        relationship.setInElement(in_element);
        out_element.addOutConnections(relationship);
        in_element.addInConnections(relationship);
    }
}
