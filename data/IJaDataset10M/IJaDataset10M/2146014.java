package net.datao.selector;

import edu.uci.ics.jung.graph.Graph;
import net.datao.jung.ontologiesItems.Edge;
import net.datao.jung.ontologiesItems.Vertex;
import java.util.List;

public interface GraphSelector extends GraphSelectionDispatcher {

    public Graph<Vertex, Edge> getSelectedGraph();

    public List<Graph> getGraphsAsList();

    public void setSelectedGraph(Graph graph);
}
