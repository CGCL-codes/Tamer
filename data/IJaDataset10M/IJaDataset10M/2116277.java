package hrashk.chemistry.layout;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import org._3pq.jgrapht.Edge;
import org._3pq.jgrapht.EdgeFactory;
import org._3pq.jgrapht.graph.SimpleGraph;

/**
 * A simple bipartite graph. This implementation doesn't make strong checks on
 * the partition classes.
 */
public class NPartiteGraph extends SimpleGraph {

    /** Second partition class. */
    private List<Set> _partitions = new ArrayList<Set>();

    /**
	 * Creates a new bipartite graph.
	 * @param count the number of partitions.
	 */
    public NPartiteGraph(int count) {
        super();
        for (int i = 0; i < count; ++i) _partitions.add(new HashSet());
    }

    /**
	 * Creates a new bipartite graph with the specified edge factory.
	 * @param ef the edge factory of the new graph.
	 * @param count the number of partitions.
	 */
    public NPartiteGraph(EdgeFactory ef, int count) {
        super(ef);
        for (int i = 0; i < count; ++i) _partitions.add(new HashSet());
    }

    /**
	 * Returns an unmodifiable copy of the list of partitions.
	 * @return the list of partitions.
	 */
    public List<Set> getPartitions() {
        return Collections.unmodifiableList(_partitions);
    }

    /**
	 * Adds the specified vertex to this graph in a specified partition class.
	 * @param v the vertex to add.
	 * @param partition the number of the partiton  to add to.
	 * @return {@code true} if the vertex was not in the graph, {@code false} otherwise.
	 * @throws ArrayIndexOutOfBoundsException if {@code partition} is out of range.
	 */
    public boolean addVertex(Object v, int partition) {
        _partitions.get(partition).add(v);
        return super.addVertex(v);
    }

    /**
	 * Removes the specified vertex from the graph and its partitions.
	 * @param v the vertex to remove.
	 * @return {@code true} if {@code v} belongs to the graph, {@code false} otherwise.
	 */
    public boolean removeVertex(Object v) {
        boolean ret = super.removeVertex(v);
        if (ret) for (Set p : _partitions) p.remove(v);
        return ret;
    }
}
