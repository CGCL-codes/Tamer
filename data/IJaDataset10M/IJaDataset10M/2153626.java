package de.uniwuerzburg.informatik.mindmapper.spi.actions;

import de.uniwuerzburg.informatik.mindmapper.spi.*;
import de.uniwuerzburg.informatik.mindmapper.api.Node;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

/**
 * An action which appends a given node to a given parent.
 * @author Christian "blair" Schwartz
 */
public class AppendChildAction extends AbstractUndoableAction {

    /**
     * The parent to add the child to.
     */
    protected Node parent;

    /**
     * The child to add to the parent.
     */
    protected Node child;

    /**
     * Create and execute an action which appends the child to the parent MindMap
     * Node
     * @param document The document owning the parent
     * @param parent The parent to add the child to.
     * @param child The child to add to the parent.
     */
    public AppendChildAction(DocumentImpl document, Node parent, Node child) {
        super(document);
        this.parent = parent;
        this.child = child;
        ((NodeImpl) child).setDocument(document);
        parent.addChild(child);
        postInit();
    }

    @Override
    public void undo() throws CannotUndoException {
        super.undo();
        parent.removeChild(child);
    }

    @Override
    public void redo() throws CannotRedoException {
        super.redo();
        parent.addChild(child);
    }

    @Override
    public String getPresentationName() {
        return "Append Child";
    }

    @Override
    public String getUndoPresentationName() {
        return "Append Child";
    }

    @Override
    public String getRedoPresentationName() {
        return "Append Child";
    }
}
