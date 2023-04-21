package net.sf.graphiti.ui.commands.copyPaste;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sf.graphiti.model.Graph;
import net.sf.graphiti.model.Vertex;
import net.sf.graphiti.ui.actions.GraphitiClipboard;
import net.sf.graphiti.ui.editparts.VertexEditPart;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.dnd.Transfer;

/**
 * This class provides a command that removes vertices from their parent.
 * 
 * @author Samuel Beaussier
 * @author Nicolas Isch
 * @author Matthieu Wipliez
 * 
 */
public class CutCommand extends Command {

    private List<?> list;

    /**
	 * Contains the parents of each port/graph.
	 */
    private List<Graph> parents;

    /**
	 * Creates a new cut command with the selected objects.
	 * 
	 * @param objects
	 *            A list of objects to cut.
	 */
    public CutCommand(List<?> objects) {
        list = objects;
    }

    @Override
    public void execute() {
        parents = new ArrayList<Graph>();
        List<Vertex> vertices = new ArrayList<Vertex>();
        for (Object obj : list) {
            if (obj instanceof VertexEditPart) {
                VertexEditPart part = (VertexEditPart) obj;
                Vertex vertex = (Vertex) part.getModel();
                Graph parent = vertex.getParent();
                parent.removeVertex(vertex);
                vertex = new Vertex(vertex);
                vertices.add(vertex);
                parents.add(parent);
            }
        }
        LocalSelectionTransfer transfer = LocalSelectionTransfer.getTransfer();
        Object[] verticesArray = vertices.toArray();
        transfer.setSelection(new StructuredSelection(verticesArray));
        Object[] data = new Object[] { verticesArray };
        Transfer[] transfers = new Transfer[] { transfer };
        GraphitiClipboard.getInstance().setContents(data, transfers);
    }

    @Override
    public String getLabel() {
        return "Cut";
    }

    @Override
    public void undo() {
        Iterator<Graph> it = parents.iterator();
        for (Object obj : list) {
            if (obj instanceof VertexEditPart) {
                VertexEditPart part = (VertexEditPart) obj;
                Vertex vertex = (Vertex) part.getModel();
                Graph parent = it.next();
                parent.addVertex(vertex);
                Rectangle bounds = (Rectangle) vertex.getValue(Vertex.PROPERTY_SIZE);
                vertex.firePropertyChange(Vertex.PROPERTY_SIZE, null, bounds);
            }
        }
        parents = null;
    }
}
