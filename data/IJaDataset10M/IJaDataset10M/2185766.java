package karto.DataModels;

import org.jgrapht.graph.DefaultEdge;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Leto
 * Date: 25.11.2007
 * Time: 20:25:51
 * To change this template use File | Settings | File Templates.
 */
public class MapEdge extends DefaultEdge {

    public static final long serialVersionUID = 43890428950279042L;

    private ArrayList<EdgeType> edgeLabels;

    public MapEdge() {
        edgeLabels = new ArrayList<EdgeType>(1);
    }

    public String toString() {
        return edgeLabels.toString();
    }

    public boolean containsEdgeTypes(EdgeType edgeType) {
        return edgeLabels.contains(edgeType);
    }

    public EdgeType addEdgeType(EdgeType edgeType) {
        if (!edgeLabels.contains(edgeType)) {
            edgeLabels.add(edgeType);
            return edgeType;
        } else return null;
    }

    public Collection<EdgeType> getEdgeLabels() {
        return edgeLabels;
    }
}
