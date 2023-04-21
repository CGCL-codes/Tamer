package net.sf.jdsc;

/**
 * @author <a href="mailto:twyss@users.sourceforge.net">twyss</a>
 * @version 1.0
 */
public interface IGraph<V, E> extends IDataStructure {

    @Override
    public IGraph<V, E> create();

    /**
     * Inserts a new vertex into this {@link IGraph}.
     * 
     * @param element
     *            The element of the vertex.
     * @return the new {@link IVertex}.
     * @throws FullDataStructureException
     *             if this {@link IGraph} is full.
     */
    public IVertex<V> insertVertex(V element) throws FullDataStructureException;

    /**
     * Inserts a new edge into this {@link IGraph}.
     * 
     * @param origin
     *            The origin of the edge.
     * @param destination
     *            The destination of the edge.
     * @param element
     *            The element of the edge.
     * @return the new {@link IEdge}.
     * @throws PositionNotFoundException
     *             if the origin or the destination not exists in this
     *             {@link IGraph}.
     * @throws FullDataStructureException
     *             if this {@link IGraph} is full.
     */
    public IEdge<E> insertEdge(IVertex<V> origin, IVertex<V> destination, E element) throws PositionNotFoundException, FullDataStructureException;

    /**
     * Returns the origin of the specified <i>edge</i>.
     * 
     * @param edge
     *            The edge.
     * @return the origin of the edge.
     * @throws PositionNotFoundException
     *             if no <i>edge</i> is specified.
     */
    public IVertex<V> origin(IEdge<E> edge) throws PositionNotFoundException;

    /**
     * Returns the destination of the specified <i>edge</i>.
     * 
     * @param edge
     *            The edge.
     * @return the destination of the edge.
     * @throws PositionNotFoundException
     *             if no <i>edge</i> is specified.
     */
    public IVertex<V> destination(IEdge<E> edge) throws PositionNotFoundException;

    /**
     * Returns the edge between the specified <i>origin</i> and
     * <i>destination</i>.
     * 
     * @param origin
     *            The origin of the {@link IEdge}.
     * @param destination
     *            The destination of the {@link IEdge}.
     * @return the {@link IEdge} between the two vertices.
     * @throws PositionNotFoundException
     *             if the specified <i>origin</i> or <i>destination</i> not
     *             exists in this {@link IGraph}.
     */
    public IEdge<E> getEdge(IVertex<V> origin, IVertex<V> destination) throws PositionNotFoundException;

    /**
     * Searchs an edge with the specified <i>origin</i> and <i>element</i>.
     * 
     * @param origin
     *            The origin of the {@link IEdge}.
     * @param element
     *            The element of the {@link IEdge}.
     * @return an {@link IEdge}.
     * @throws PositionNotFoundException
     *             if the specified <i>origin</i> not exists in this
     *             {@link IGraph}.
     */
    public IEdge<E> getEdge(IVertex<V> origin, E element) throws PositionNotFoundException;

    /**
     * Enumerates all edges between the specified <i>origin</i> and
     * <i>destination</i>.
     * 
     * @param origin
     *            The origin of the edges.
     * @param destination
     *            The destination of the edges.
     * @return an {@link IEnumerator} over the edges.
     * @throws PositionNotFoundException
     *             if the specified <i>origin</i> or <i>destination</i> not
     *             exists in this {@link IGraph}.
     */
    public IEnumerator<? extends IEdge<E>> getEdges(IVertex<V> origin, IVertex<V> destination) throws PositionNotFoundException;

    /**
     * Enumerates all edges with the specified <i>origin</i> and <i>element</i>.
     * 
     * @param origin
     *            The origin of the {@link IEdge}.
     * @param element
     *            The element of the {@link IEdge}.
     * @return an {@link IEnumerator} over the edges.
     * @throws PositionNotFoundException
     *             if the specified <i>origin</i> not exists in this
     *             {@link IGraph}.
     */
    public IEnumerator<? extends IEdge<E>> getEdges(IVertex<V> origin, E element) throws PositionNotFoundException;

    /**
     * Enumerates all outgoing edges from the specified <i>origin</i>-vertex.
     * 
     * @param origin
     *            The origin of the edges.
     * @return an {@link IEnumerator} over the outgoing edges of the origin.
     * @throws PositionNotFoundException
     *             if the specified <i>origin</i> not exists in this
     *             {@link IGraph}.
     */
    public IEnumerator<? extends IEdge<E>> outgoingEdges(IVertex<V> origin) throws PositionNotFoundException;

    /**
     * Enumerates all incoming edges into the specified
     * <i>destination</i>-vertex.
     * 
     * @param destination
     *            The destination of the edges.
     * @return an {@link IEnumerator} over the incoming edges of the
     *         destination.
     * @throws PositionNotFoundException
     *             if the specified <i>destination</i> not exists in this
     *             {@link IGraph}.
     */
    public IEnumerator<? extends IEdge<E>> incomingEdges(IVertex<V> destination) throws PositionNotFoundException;

    /**
     * Enumerates all incoming and outgoing edges of the specified
     * <i>vertex</i>.
     * 
     * @param vertex
     *            The vertex.
     * @return an {@link IEnumerator} over the incident edges.
     * @throws PositionNotFoundException
     *             if no <i>vertex</i> is specified.
     */
    public IEnumerator<? extends IEdge<E>> incidentEdges(IVertex<V> vertex) throws PositionNotFoundException;

    /**
     * Enumerates all neighbors of the specified <i>vertex</i>.
     * 
     * @param vertex
     *            The vertex.
     * @return an {@link IEnumerator} over the neighbors.
     * @throws PositionNotFoundException
     *             if no <i>vertex</i> is specified.
     */
    public IEnumerator<? extends IVertex<V>> neighbors(IVertex<V> vertex) throws PositionNotFoundException;

    /**
     * Returns all vertices of this {@link IGraph}.
     * 
     * @return an {@link IEnumerator} over the vertices.
     */
    public IEnumerator<? extends IVertex<V>> vertices();

    /**
     * Returns all edges of this {@link IGraph}.
     * 
     * @return an {@link IEnumerator} over the edges.
     */
    public IEnumerator<? extends IEdge<E>> edges();

    /**
     * Counts all vertices in this {@link IGraph}.
     * 
     * @return the amount of vertices.
     */
    public int countVertices();

    /**
     * Counts all edges in this {@link IGraph}.
     * 
     * @return the amount of edges.
     */
    public int countEdges();

    /**
     * Returns <code>true</code> if the edges in this {@link IGraph} are
     * directed, otherwise <code>false</code>.
     * 
     * @return if this is a directed graph.
     */
    public boolean isDirected();

    /**
     * Checks if this {@link IGraph} contains an edge between the specified
     * <i>origin</i> and <i>destination</i>.
     * 
     * @see IVertex#isAdjacent(IVertex)
     * @param origin
     *            The origin of the edge.
     * @param destination
     *            The destination of the edge.
     * @return <code>true</code> if an edge between the vertices exists,
     *         otherwise <code>false</code>.
     */
    public boolean contains(IVertex<V> origin, IVertex<V> destination);

    public boolean containsVertex(IVertex<V> vertex);

    public boolean containsEdge(IEdge<E> edge);

    /**
     * Removes the specified <i>vertex</i> and all incident edges.
     * 
     * @param vertex
     *            The vertex to remove.
     * @return <code>true</code> if this {@link IGraph} has changed, otherwise
     *         <code>false</code>.
     * @throws PositionNotFoundException
     *             if no <i>vertex</i> is specified.
     */
    public boolean remove(IVertex<V> vertex) throws PositionNotFoundException;

    /**
     * Removes the specified <i>edge</i> from this {@link IGraph}.
     * 
     * @param edge
     *            The edge to remove.
     * @return <code>true</code> if this {@link IGraph} has changed, otherwise
     *         <code>false</code>.
     */
    public boolean remove(IEdge<E> edge);

    /**
     * Removes all edges from this {@link IGraph}.
     * 
     * @see IDataStructure#clear()
     * @return the number of removed edges.
     */
    public int removeEdges();

    @Override
    public IGraph<V, E> clone();

    @Override
    public IGraph<V, E> clone(boolean deepclone);
}
