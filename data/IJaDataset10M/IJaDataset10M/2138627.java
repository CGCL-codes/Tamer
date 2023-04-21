package uk.org.ogsadai.util.graph.search;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import uk.org.ogsadai.util.graph.DirectedGraph;
import uk.org.ogsadai.util.graph.Edge;

/**
 * Performs a depth-first-search of a graph. During the search, events are
 * dispatched to a handler. Several handler implementations are provided to
 * achieve different results. This class is not thread-safe, but an instance may
 * be reused.
 * 
 * @author The OGSA-DAI Project Team
 */
public class DirectedDepthFirstSearch {

    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh 2006 - 2007";

    /** The graph that is being searched. */
    private DirectedGraph mGraph;

    /** The handler to which events are dispatched during the search. */
    private DepthFirstSearchHandler mHandler;

    /** Maps vertices of the graph to vertex markers (<Vertex, VertexMarker>) */
    private final Map mMarkers = new HashMap();

    /**
     * Performs a depth-first-search of a graph.
     * 
     * @param graph
     *            the graph to search
     * @param handler
     *            the handler to receive the events generated during the search
     */
    public final void search(final DirectedGraph graph, final DepthFirstSearchHandler handler) {
        mGraph = graph;
        mHandler = handler;
        final Set vertices = graph.getVertices();
        initialiseMarkers(vertices);
        startSearch(vertices);
    }

    /**
     * Initialises the map of vertices to vertex markers so that each vertex is
     * marked as <code>VertexMarker.WHITE</code>. Also invokes the handler's
     * <code>initialiseVertex</code> method for each vertex of the graph.
     * 
     * @param vertices
     *            the vertices to initialise
     */
    private void initialiseMarkers(final Set vertices) {
        mMarkers.clear();
        for (Iterator i = vertices.iterator(); i.hasNext(); ) {
            final Object vertex = i.next();
            mMarkers.put(vertex, VertexMarker.WHITE);
            mHandler.initialiseVertex(vertex, mGraph);
        }
    }

    /**
     * Starts the search with the first vertex from the graph. After the search
     * tree from that root has been explored, another search begins from the
     * next unexplored vertex. This repeats until all the vertices of the graph
     * have been explored. The handler's <code>startVertex</code> method is
     * invoked for each vertex that starts a search-tree.
     * 
     * @param vertices
     *            vertices to search
     */
    private void startSearch(final Set vertices) {
        for (Iterator i = vertices.iterator(); i.hasNext(); ) {
            final Object vertex = i.next();
            if (mMarkers.get(vertex) == VertexMarker.WHITE) {
                mHandler.startVertex(vertex, mGraph);
                searchVertex(vertex);
            }
        }
    }

    /**
     * Performs a recursive search of the specified vertex, exploring in a
     * depth-first manner each adjacent vertex. The handler's
     * <code>discoverVertex</code> method is invoked whenever a new vertex is
     * discovered, and the <code>finishVertex</code> method is invoked
     * whenever there are no more undiscovered adjacent vertices of the vertex.
     * 
     * @param vertex
     *            start vertex for the search
     */
    private void searchVertex(final Object vertex) {
        mMarkers.put(vertex, VertexMarker.GREY);
        mHandler.discoverVertex(vertex, mGraph);
        final Set outgoingEdges = mGraph.getOutgoingEdges(vertex);
        for (Iterator i = outgoingEdges.iterator(); i.hasNext(); ) {
            final Edge edge = (Edge) i.next();
            final Object adjacentVertex = edge.getTarget();
            final Object marker = mMarkers.get(adjacentVertex);
            if (marker == VertexMarker.WHITE) {
                mHandler.treeEdge(edge, mGraph);
                searchVertex(adjacentVertex);
            } else if (marker == VertexMarker.GREY) {
                mHandler.backEdge(edge, mGraph);
            } else if (marker == VertexMarker.BLACK) {
                mHandler.forwardOrCrossEdge(edge, mGraph);
            }
        }
        mMarkers.put(vertex, VertexMarker.BLACK);
        mHandler.finishVertex(vertex, mGraph);
    }
}
