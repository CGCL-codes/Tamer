package edu.kds.circuit.wireShapeCreators.shortestPathCreator;

import java.util.Collection;
import jdsl.graph.api.Graph;
import jdsl.graph.api.Vertex;

/**
 * Overrides GraphGenerator, and produces two layer of vertices, 
 * two at every intersection. It also connects these two vertices.
 * @author Rasmus Hansen
 */
class DoubleLayerGraphGenerator extends GraphGenerator {

    @Override
    protected void createVertices(Graph graph, Collection<BoundaryLine> lines) {
        Vertex v1, v2;
        for (BoundaryLine l : lines) {
            if (!l.isHorizontal()) continue;
            Collection<Intersection> intersections = getIntersections(l, lines);
            for (Intersection i : intersections) {
                v1 = graph.insertVertex(i.point);
                i.line1.addVertex(v1);
                v2 = graph.insertVertex(i.point);
                i.line2.addVertex(v2);
                graph.insertEdge(v1, v2, getWeight(null, v1, v2));
            }
        }
    }
}
