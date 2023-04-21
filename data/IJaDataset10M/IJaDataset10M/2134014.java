package darwin.nodeFilter;

import java.util.ArrayList;
import java.util.List;
import darwin.population.Node;

/**
 * Object representing a filter for nodes
 * @author Kevin Dolan
 */
public abstract class Filter {

    /**
	 * Determine whether to accept this node
	 * @param node node to be examined
	 * @return 	   true if the node is accepted
	 */
    public abstract boolean accept(Filterable node);

    /**
	 * Apply this filter to a list of Nodes
	 * @param nodes the list of nodes to filter
	 * @return		the list of nodes satisfying this condition
	 */
    public List<Node> applyFilter(List<Node> nodes) {
        ArrayList<Node> good = new ArrayList<Node>();
        for (Node node : nodes) {
            if (accept(node)) good.add(node);
        }
        return good;
    }
}
