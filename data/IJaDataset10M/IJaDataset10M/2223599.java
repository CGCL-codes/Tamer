package com.vividsolutions.jts.operation.linemerge;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryComponentFilter;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.planargraph.GraphComponent;
import com.vividsolutions.jts.planargraph.Node;
import com.vividsolutions.jts.util.Assert;

/**
 * Sews together a set of fully noded LineStrings. Sewing stops at nodes of degree 1
 * or 3 or more -- the exception is an isolated loop, which only has degree-2 nodes,
 * in which case a node is simply chosen as a starting point. The direction of each
 * merged LineString will be that of the majority of the LineStrings from which it
 * was derived.
 * <p>
 * Any dimension of Geometry is handled -- the constituent linework is extracted to 
 * form the edges. The edges must be correctly noded; that is, they must only meet
 * at their endpoints.  The LineMerger will still run on incorrectly noded input
 * but will not form polygons from incorrected noded edges.
 * <p>
 * <b>NOTE:</b>once merging has been performed, no more 
 *
 * @version 1.7
 */
public class LineMerger {

    private LineMergeGraph graph = new LineMergeGraph();

    private Collection mergedLineStrings = null;

    private GeometryFactory factory = null;

    /**
   * Adds a Geometry to be processed. May be called multiple times.
   * Any dimension of Geometry may be added; the constituent linework will be
   * extracted.
   */
    public void add(Geometry geometry) {
        geometry.apply(new GeometryComponentFilter() {

            public void filter(Geometry component) {
                if (component instanceof LineString) {
                    add((LineString) component);
                }
            }
        });
    }

    /**
   * Adds a collection of Geometries to be processed. May be called multiple times.
   * Any dimension of Geometry may be added; the constituent linework will be
   * extracted.
   */
    public void add(Collection geometries) {
        mergedLineStrings = null;
        for (Iterator i = geometries.iterator(); i.hasNext(); ) {
            Geometry geometry = (Geometry) i.next();
            add(geometry);
        }
    }

    private void add(LineString lineString) {
        if (factory == null) {
            this.factory = lineString.getFactory();
        }
        graph.addEdge(lineString);
    }

    private Collection edgeStrings = null;

    private void merge() {
        if (mergedLineStrings != null) {
            return;
        }
        GraphComponent.setMarked(graph.nodeIterator(), false);
        GraphComponent.setMarked(graph.edgeIterator(), false);
        edgeStrings = new ArrayList();
        buildEdgeStringsForObviousStartNodes();
        buildEdgeStringsForIsolatedLoops();
        mergedLineStrings = new ArrayList();
        for (Iterator i = edgeStrings.iterator(); i.hasNext(); ) {
            EdgeString edgeString = (EdgeString) i.next();
            mergedLineStrings.add(edgeString.toLineString());
        }
    }

    private void buildEdgeStringsForObviousStartNodes() {
        buildEdgeStringsForNonDegree2Nodes();
    }

    private void buildEdgeStringsForIsolatedLoops() {
        buildEdgeStringsForUnprocessedNodes();
    }

    private void buildEdgeStringsForUnprocessedNodes() {
        for (Iterator i = graph.getNodes().iterator(); i.hasNext(); ) {
            Node node = (Node) i.next();
            if (!node.isMarked()) {
                Assert.isTrue(node.getDegree() == 2);
                buildEdgeStringsStartingAt(node);
                node.setMarked(true);
            }
        }
    }

    private void buildEdgeStringsForNonDegree2Nodes() {
        for (Iterator i = graph.getNodes().iterator(); i.hasNext(); ) {
            Node node = (Node) i.next();
            if (node.getDegree() != 2) {
                buildEdgeStringsStartingAt(node);
                node.setMarked(true);
            }
        }
    }

    private void buildEdgeStringsStartingAt(Node node) {
        for (Iterator i = node.getOutEdges().iterator(); i.hasNext(); ) {
            LineMergeDirectedEdge directedEdge = (LineMergeDirectedEdge) i.next();
            if (directedEdge.getEdge().isMarked()) {
                continue;
            }
            edgeStrings.add(buildEdgeStringStartingWith(directedEdge));
        }
    }

    private EdgeString buildEdgeStringStartingWith(LineMergeDirectedEdge start) {
        EdgeString edgeString = new EdgeString(factory);
        LineMergeDirectedEdge current = start;
        do {
            edgeString.add(current);
            current.getEdge().setMarked(true);
            current = current.getNext();
        } while (current != null && current != start);
        return edgeString;
    }

    /**
   * Returns the LineStrings built by the merging process.
   */
    public Collection getMergedLineStrings() {
        merge();
        return mergedLineStrings;
    }
}
