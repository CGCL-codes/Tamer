package de.sonivis.tool.core.filtersystem;

import org.apache.commons.collections.Predicate;
import ca.odell.glazedlists.EventList;
import de.sonivis.tool.core.datamodel.Edge;
import de.sonivis.tool.core.datamodel.Graph;

/**
 * @author robert
 *
 */
public class EdgeFilter extends GraphFilter {

    /**
	 * Build the filter.
	 * @param newGraphToBeFiltered - a {@link Graph} to be filtered
	 */
    public EdgeFilter(Graph newGraphToBeFiltered) {
        super(newGraphToBeFiltered);
    }

    @Override
    public Graph run(Predicate predicate) {
        EventList<Edge> edges = this.graphToBeFiltered.getEdges();
        for (Edge edge : edges) {
            if (!(predicate.evaluate(edge))) {
                edges.remove(edge);
            }
        }
        this.myGraph.setEdgesAndNodes(edges);
        return this.myGraph;
    }
}
