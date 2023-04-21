package com.platonov.colorizer.engine.segmentation.graph;

import com.platonov.tools.logger.MyLoggerFactory;
import org.apache.log4j.Logger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Segment a graph
 * <p/>
 * Returns a disjoint-set forest representing the segmentation.
 * <p/>
 * num_vertices: number of vertices in graph.
 * num_edges: number of edges in graph
 * edges: array of edges.
 * c: constant for treshold function.
 * <p/>
 * User: Platonov
 * Date: 27.08.11
 */
public class SegmentGraph {

    private static Logger log = MyLoggerFactory.getColorizerLogger();

    public static DisjointSet segmentGraph(int num_vertices, int num_edges, Edge[] edges, float c) {
        log.trace("sort edges by weight");
        List<Edge> l = Arrays.asList(edges);
        Collections.sort(l);
        edges = l.toArray(new Edge[0]);
        log.trace("make a disjoint-set forest");
        DisjointSet u = new DisjointSet(num_vertices);
        log.trace("init thresholds");
        float[] threshold = new float[num_vertices];
        for (int i = 0; i < num_vertices; i++) threshold[i] = c;
        log.trace("for each edge, in non-decreasing weight order...");
        for (int i = 0; i < num_edges; i++) {
            Edge pedge = edges[i];
            int a = u.find(pedge.a);
            int b = u.find(pedge.b);
            if (a != b) {
                if ((pedge.w <= threshold[a]) && (pedge.w <= threshold[b])) {
                    u.join(a, b);
                    a = u.find(a);
                    threshold[a] = pedge.w + c / (u.size(a));
                }
            }
        }
        return u;
    }
}
