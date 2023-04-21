package uk.ac.bolton.archimate.editor.diagram.commands;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.gef.commands.Command;
import uk.ac.bolton.archimate.model.IDiagramModelConnection;
import uk.ac.bolton.archimate.model.IDiagramModelContainer;
import uk.ac.bolton.archimate.model.IDiagramModelObject;

/**
 * Command for deleting an Object from its parent container
 * It puts it back at the index position from where it was removed.
 * 
 * @author Phillip Beauvoir
 */
class DeleteDiagramObjectCommand extends Command {

    private IDiagramModelContainer fParent;

    private IDiagramModelObject fObject;

    private int fIndex;

    private List<IDiagramModelConnection> fSourceConnections;

    private List<IDiagramModelConnection> fTargetConnections;

    public DeleteDiagramObjectCommand(IDiagramModelObject object) {
        fParent = (IDiagramModelContainer) object.eContainer();
        fObject = object;
    }

    @Override
    public boolean canExecute() {
        return fParent != null && fParent.getChildren().contains(fObject);
    }

    @Override
    public void execute() {
        fSourceConnections = new ArrayList<IDiagramModelConnection>();
        fTargetConnections = new ArrayList<IDiagramModelConnection>();
        fSourceConnections.addAll(fObject.getSourceConnections());
        fTargetConnections.addAll(fObject.getTargetConnections());
        fIndex = fParent.getChildren().indexOf(fObject);
        if (fIndex != -1) {
            fParent.getChildren().remove(fObject);
        }
        removeConnections(fSourceConnections);
        removeConnections(fTargetConnections);
    }

    @Override
    public void undo() {
        if (fIndex != -1) {
            fParent.getChildren().add(fIndex, fObject);
        }
        addConnections(fSourceConnections);
        addConnections(fTargetConnections);
        fSourceConnections.clear();
        fTargetConnections.clear();
    }

    /**
     * Reconnects a List of Connections with their previous endpoints.
     * @param connections a non-null List of connections
     */
    private void addConnections(List<IDiagramModelConnection> connections) {
        for (IDiagramModelConnection conn : connections) {
            conn.reconnect();
        }
    }

    /**
     * Disconnects a List of Connections from their endpoints.
     * @param connections a non-null List of connections
     */
    private void removeConnections(List<IDiagramModelConnection> connections) {
        for (IDiagramModelConnection conn : connections) {
            conn.disconnect();
        }
    }

    @Override
    public void dispose() {
        fParent = null;
        fObject = null;
        fSourceConnections = null;
        fTargetConnections = null;
    }
}
