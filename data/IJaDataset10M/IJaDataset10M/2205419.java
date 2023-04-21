package pcgen.base.graph.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.undo.UndoableEdit;
import pcgen.base.graph.core.Edge;
import pcgen.base.graph.core.Graph;
import pcgen.base.graph.core.UnsupportedGraphOperationException;
import pcgen.base.graph.monitor.GraphEditMonitor;
import pcgen.base.lang.Command;

/**
 * @author Thomas Parker (thpr [at] yahoo.com)
 * 
 * A DeleteEdgesCommand is a Command which, when executed, will delete a
 * Collection of Edges from a Graph.
 * 
 * Note that deletion of an Edge may have side effects upon the Nodes to which
 * the Edge was connected (this will depend on the Graph implementation).
 * 
 * The UndoableEdit returned by the execute() method in DeleteEdgesCommand will
 * track any side effects to the graph itself (Node or Edge addition or
 * removal). Thus, if the deletion of one Edge causes a Node to also be deleted,
 * the returned UndoableEdit will represent both the Edge removal and the Node
 * removal (and the undo() and redo() methods of the UndoableEdit would undo and
 * redo both deletions)
 */
public class DeleteEdgesCommand<N, ET extends Edge<N>> implements Command {

    /**
	 * The Graph on which this DeleteEdgesCommand will operate.
	 */
    private final Graph<N, ET> graph;

    /**
	 * The List of Edges to be deleted from the Graph when this
	 * DeleteEdgesCommand is executed.
	 */
    private final List<ET> edgeList;

    /**
	 * The name of this DeleteEdgesCommand. Intended to be accessible to the end
	 * user as a potential name for this Command.
	 */
    private final String name;

    /**
	 * Creates a new DeleteEdgesCommand with the given Name, Graph in which the
	 * Deletion will take place, and Collection of Edges to be removed from the
	 * Graph. The given Graph and Edges must not be null. The Collection of
	 * Edges must not be Empty. Upon execution, the given Edges will be removed
	 * from the Graph.
	 * 
	 * @param editName
	 *            The edit name for this Command. This Command will use its
	 *            default edit name is this parameter is null or a String
	 *            composed entirely of whitespace.
	 * @param g
	 *            The Graph in which the Deletion will take place
	 * @param edgeCollection
	 *            The Collection of Edges to be removed from the Graph
	 */
    public DeleteEdgesCommand(String editName, Graph<N, ET> g, Collection<ET> edgeCollection) {
        if (g == null) {
            throw new IllegalArgumentException("Graph cannot be null");
        }
        if (edgeCollection == null) {
            throw new IllegalArgumentException("Edge Collection cannot be null");
        }
        graph = g;
        edgeList = new ArrayList<ET>(edgeCollection);
        if (edgeList.isEmpty()) {
            throw new IllegalArgumentException("Edge Collection cannot be empty");
        }
        if (editName == null || editName.trim().length() == 0) {
            name = "Delete Graph Edges";
        } else {
            name = editName;
        }
    }

    /**
	 * Execute the DeleteEdgesCommand on the Graph provided during Command
	 * construction. Returns an UndoableEdit indicating the changes that took
	 * place in the Graph.
	 * 
	 * @see pcbase.lang.Command#execute()
	 */
    public UndoableEdit execute() {
        GraphEditMonitor<N, ET> edit = GraphEditMonitor.getGraphEditMonitor(graph);
        boolean removed = false;
        for (ET edge : edgeList) {
            removed |= graph.removeEdge(edge);
        }
        if (!removed) {
            throw new UnsupportedGraphOperationException("Graph did not contain Edge to be deleted");
        }
        return edit.getEdit(name);
    }

    /**
	 * Returns the user-presentable name for this Command
	 * 
	 * @see pcbase.lang.Command#getPresentationName()
	 */
    public String getPresentationName() {
        return name;
    }
}
