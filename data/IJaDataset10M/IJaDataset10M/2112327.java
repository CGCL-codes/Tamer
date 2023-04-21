package org.jgrapht.event;

/**
 * An empty do-nothing implementation of the {@link TraversalListener} interface
 * used for subclasses.
 *
 * @author Barak Naveh
 * @since Aug 6, 2003
 */
public class TraversalListenerAdapter<V, E> implements TraversalListener<V, E> {

    /**
     * @see TraversalListener#connectedComponentFinished(ConnectedComponentTraversalEvent)
     */
    public void connectedComponentFinished(ConnectedComponentTraversalEvent e) {
    }

    /**
     * @see TraversalListener#connectedComponentStarted(ConnectedComponentTraversalEvent)
     */
    public void connectedComponentStarted(ConnectedComponentTraversalEvent e) {
    }

    /**
     * @see TraversalListener#edgeTraversed(EdgeTraversalEvent)
     */
    public void edgeTraversed(EdgeTraversalEvent<V, E> e) {
    }

    /**
     * @see TraversalListener#vertexTraversed(VertexTraversalEvent)
     */
    public void vertexTraversed(VertexTraversalEvent<V> e) {
    }

    /**
     * @see TraversalListener#vertexFinished(VertexTraversalEvent)
     */
    public void vertexFinished(VertexTraversalEvent<V> e) {
    }
}
