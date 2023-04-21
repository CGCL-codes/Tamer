package it.cnr.stlab.xd.plugin.editor.delegates;

import java.util.List;
import org.eclipse.gef.commands.Command;
import org.semanticweb.owl.model.OWLOntologyChange;

/**
 * Base implementation for all commands of the editor. Saves changes to the
 * ontology for undo/redo operations in parallel
 * 
 * @author Enrico Daga
 * 
 */
public abstract class OWLDelegateCommand extends Command {

    private List<OWLOntologyChange> changes;

    private OWLModelDelegate delegate;

    public OWLDelegateCommand(OWLModelDelegate delegate) {
        this.delegate = delegate;
    }

    protected void setChanges(List<OWLOntologyChange> changes) {
        this.changes = changes;
    }

    protected List<OWLOntologyChange> getChanges() {
        return this.changes;
    }

    @Override
    public void redo() {
        delegate.applyChanges(getChanges());
    }

    public void undo() {
        delegate.doRollback(getChanges());
    }

    public OWLModelDelegate getOWLModelDelegate() {
        return delegate;
    }
}
