package br.com.nix.map.display;

import org.apache.commons.collections15.Predicate;
import br.com.nix.beans.edge.Edge;
import br.com.nix.beans.room.Room;
import br.com.nix.beans.vertex.Vertex;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.visualization.picking.PickedInfo;

public class VertexPredicate implements Predicate<Context<Graph<Vertex, Edge>, Vertex>> {

    private PickedInfo<Vertex> pi;

    public VertexPredicate(PickedInfo<Vertex> pi) {
        this.pi = pi;
    }

    @Override
    public boolean evaluate(Context<Graph<Vertex, Edge>, Vertex> context) {
        if (!(context.element instanceof Room)) {
            if (pi.isPicked(context.element)) return true;
            if (context.graph.getNeighborCount(context.element) == 0) return true;
            for (Vertex v : context.graph.getNeighbors(context.element)) {
                if (pi.isPicked(v)) return true;
                for (Vertex v2 : context.graph.getNeighbors(v)) if (pi.isPicked(v2) && !(v2 instanceof Room)) return true;
            }
            return true;
        } else return true;
    }
}
