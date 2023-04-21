package de.beas.explicanto.client.model.commands;

import org.eclipse.gef.commands.Command;
import de.beas.explicanto.client.model.Node;
import de.beas.explicanto.client.model.ParentNode;

/**
 * NodeRemoveCommand
 *
 * Removes a node  
 *
 * @author Lucian Brancovean
 * @version 1.0
 *
 */
public class NodeRemoveCommand extends Command {

    private final ParentNode parent;

    private final Node child;

    /**
     * Creates a command to remove a node from its parent.
     * @param parent
     * @param child
     */
    private NodeRemoveCommand(ParentNode parent, Node child) {
        this.parent = parent;
        this.child = child;
    }

    /**
     * Returns true if the command can be executed.
     * @see org.eclipse.gef.commands.Command#canExecute()
     * @return true if the command can be executed
     */
    public boolean canExecute() {
        if (parent.getContainer().getDocument().isReadOnly()) return false;
        return true;
    }

    /**
     * Removes the node from its parent.
     * @see org.eclipse.gef.commands.Command#execute()
     */
    public void execute() {
        redo();
    }

    /**
     * Removes the child node from the parent.
     * @see org.eclipse.gef.commands.Command#redo()
     */
    public void redo() {
        parent.removeChild(child);
    }

    /**
     * Adds te child node to the parent.
     * @see org.eclipse.gef.commands.Command#undo()
     */
    public void undo() {
        parent.addChild(child);
    }
}
