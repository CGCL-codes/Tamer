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
 * An InsertNodesCommand is a Command which, when executed, will insert a
 * Collection of Nodes into a Graph.
 * 
 * Note that insertion of the Node may have side effects upon other objects in
 * the Graph (this will depend on the Graph implementation).
 * 
 * The UndoableEdit returned by the execute() method in InsertNodesCommand will
 * track any side effects to the graph itself (Node or Edge addition or
 * removal). Thus, if the addition of one Node causes another Node to be
 * deleted, the returned UndoableEdit will represent both the Node addition and
 * the Node deletion (and the undo() and redo() methods of the UndoableEdit
 * would undo and redo both changes).
 */
public class InsertNodesCommand<N, ET extends Edge<N>> implements Command {

    /**
	 * The Graph on which this InsertNodesCommand will operate.
	 */
    private final Graph<N, ET> graph;

    /**
	 * The Collection of Nodes to be inserted into the Graph when this
	 * InsertNodesCommand is executed.
	 */
    private final List<N> nodeList;

    /**
	 * The name of this InsertNodesCommand. Intended to be accessible to the end
	 * user as a potential name for this Command.
	 */
    private final String name;

    /**
	 * Creates a new InsertNodesCommand with the given Name, Graph in which the
	 * Insertion will take place, and Collection of Nodes to be added to the
	 * Graph. The given Graph must not be null. The given Collection must not be
	 * null or empty. Upon execution, the given Nodes will be inserted into the
	 * Graph.
	 * 
	 * @param editName
	 *            The edit name for this Command. This Command will use its
	 *            default edit name is this parameter is null or a String
	 *            composed entirely of whitespace.
	 * @param g
	 *            The Graph in which the Insertion will take place
	 * @param nodeCollection
	 *            The Nodes to be added to the Graph
	 */
    public InsertNodesCommand(String editName, Graph<N, ET> g, Collection<N> nodeCollection) {
        if (g == null) {
            throw new IllegalArgumentException("Graph cannot be null");
        }
        if (nodeCollection == null) {
            throw new IllegalArgumentException("Node Collection cannot be null");
        }
        graph = g;
        nodeList = new ArrayList<N>(nodeCollection);
        if (nodeList.isEmpty()) {
            throw new IllegalArgumentException("Node Collection cannot be empty");
        }
        if (editName == null || editName.trim().length() == 0) {
            name = "Insert Graph Nodes";
        } else {
            name = editName;
        }
    }

    /**
	 * Execute the InsertNodesCommand on the Graph provided during Command
	 * construction. Returns an UndoableEdit indicating the changes that took
	 * place in the Graph.
	 * 
	 * @see pcbase.lang.Command#execute()
	 */
    public UndoableEdit execute() {
        GraphEditMonitor<N, ET> edit = GraphEditMonitor.getGraphEditMonitor(graph);
        boolean added = false;
        for (N node : nodeList) {
            added |= graph.addNode(node);
        }
        if (!added) {
            throw new UnsupportedGraphOperationException("Graph did not add any Nodes to be inserted");
        }
        graph.removeGraphChangeListener(edit);
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
