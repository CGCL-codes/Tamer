package graphlab.library;

import java.util.*;
import graphlab.library.event.handlers.PreWorkHandler;
import graphlab.library.event.handlers.PreWorkPostWorkHandler;
import graphlab.library.exceptions.InvalidEdgeException;
import graphlab.library.exceptions.InvalidGraphException;
import graphlab.library.exceptions.InvalidVertexException;
import graphlab.library.genericcloners.GraphConverter;
import Jama.*;

/**
 * Adjacency Matrix Graph.
 * For information about Adjacency Matrix refer to http://en.wikipedia.org/wiki/Adjacency_matrix * @author Omid Aladini
 *
 * @param <VertexType> Type of the vertices the graph can work with.
 * @param <EdgeType> Type of the edges the graph can work with.
 * @author Omid Aladini
 */
public class MatrixGraph<VertexType extends BaseVertex, EdgeType extends BaseEdge<VertexType>> extends BaseGraph<VertexType, EdgeType> {

    /**
     * The data structure to store graph data, it looks like a three dimensional matrix with EdgeType elements.
     * The third dimension is designed to store multiple edges between two vertices.
     * ArrayLists can work with generic type's arameters and they are as fast as arrays for indexing, but
     * they are not thread safe. So MatrixGraph operations are not thread-safe and should be synchronized 
     * externally.
     */
    private ArrayList<ArrayList<ArrayList<EdgeType>>> adjacencyMatrix;

    private ArrayList<VertexType> vertices;

    private ArrayList<Integer> inDegree;

    private ArrayList<Integer> outDegree;

    private boolean directed;

    /**
     * Constructs a graph object that stores graph data using adjacency matrix data structure.
     * @param directed Indicated whether the graph is directed.
     * @param expectedNumberOfVertices Approximate number of vertices that will be 
     * added to the graph. This paramether is optional and is available for performance 
     * reasons.
     */
    public MatrixGraph(boolean directed, int expectedNumberOfVertices) {
        this.directed = directed;
        adjacencyMatrix = new ArrayList<ArrayList<ArrayList<EdgeType>>>(expectedNumberOfVertices);
        for (int rowCount = 0; rowCount < expectedNumberOfVertices; rowCount++) {
            ArrayList<ArrayList<EdgeType>> columns = new ArrayList<ArrayList<EdgeType>>(expectedNumberOfVertices);
            adjacencyMatrix.add(columns);
        }
        vertices = new ArrayList<VertexType>(expectedNumberOfVertices);
        inDegree = new ArrayList<Integer>(expectedNumberOfVertices);
        if (!directed) outDegree = inDegree; else outDegree = new ArrayList<Integer>(expectedNumberOfVertices);
    }

    /**
     * Constructs a MatrixGraph object
     * @param directed Whether the graph is directed or undirected.
     */
    public MatrixGraph(boolean directed) {
        this(directed, 5);
    }

    /**
     * Constructs an undirected graph object that stores graph data using 
     * adjacency list data structure. 
     */
    public MatrixGraph() {
        this(false, 5);
    }

    /**
     * Constructs a graph object that stores graph data using adjacency matrix data structure by importing
     * graph data from a pre-existing graph. A GraphConvertor object is passed as a parameter which is 
     * reponsible for duplication/type-convertion of graph elements.
     * @param <ImportVertexType> The type of vertex object which the input graph contain.
     * @param <ImportEdgeType> The type of edge object which the input graph contain.
     * @param graph
     * @param converter A GraphConverter object which is responsible for duplicating/converting graph
     * elements.
     * @throws InvalidGraphException Throws when the input graph is an invalid graph object.
     */
    public <ImportVertexType extends BaseVertex, ImportEdgeType extends BaseEdge<ImportVertexType>> MatrixGraph(MatrixGraph<ImportVertexType, ImportEdgeType> graph, GraphConverter<ImportEdgeType, EdgeType, ImportVertexType, VertexType> gc) throws InvalidGraphException {
        ArrayList<VertexType> tempAL = new ArrayList<VertexType>(getVerticesCount());
        for (ImportVertexType v : graph) {
            insertVertex(gc.convert(v));
            tempAL.add(gc.convert(v));
        }
        Iterator<ImportEdgeType> iet = graph.edgeIterator();
        ImportEdgeType edge;
        try {
            while (iet.hasNext()) {
                edge = iet.next();
                insertEdge(gc.convert(edge, tempAL.get(edge.head.getId()), tempAL.get(edge.tail.getId())));
            }
        } catch (InvalidVertexException e) {
            throw new InvalidGraphException();
        }
    }

    @Override
    public int getVerticesCount() {
        return vertices.size();
    }

    /**
     * Checks if a vertex with internal id <i>id</i> exist.
     * @param id Id of the vertex.
     * @return true of exist false if not.
     */
    private boolean vertexIdOutOfRange(int id) {
        return id < 0 || id >= vertices.size();
    }

    /**
     * Returns the vertex with internal id <I>id</I>
     * @param id Internal index of the vertex.
     * @return Reference of the vertex object.
     */
    private VertexType getVertex(int id) throws InvalidVertexException {
        if (vertexIdOutOfRange(id)) throw new InvalidVertexException();
        return vertices.get(id);
    }

    /**
     * Lables the vertices using their internal Id property by the index they live inside the graph.
     */
    private void setVertexIds() {
        try {
            for (int i = 0; i < getVerticesCount(); i++) getVertex(i).setId(i);
        } catch (InvalidVertexException e) {
            System.out.println("NEVER-HAPPENS EXCEPTION");
            e.printStackTrace();
        }
    }

    @Override
    public void insertEdge(EdgeType newEdge) throws InvalidVertexException {
        VertexType headObj, tailObj;
        int head, tail;
        headObj = newEdge.head;
        tailObj = newEdge.tail;
        head = newEdge.head.getId();
        tail = newEdge.tail.getId();
        checkVertex(headObj);
        checkVertex(tailObj);
        ArrayList<EdgeType> edges;
        if (adjacencyMatrix.get(head).get(tail) == null) {
            edges = new ArrayList<EdgeType>(5);
            adjacencyMatrix.get(head).set(tail, edges);
        } else {
            edges = adjacencyMatrix.get(head).get(tail);
        }
        edges.add(newEdge);
        if (!directed) adjacencyMatrix.get(tail).set(head, edges);
        outDegree.set(head, inDegree.get(head) + 1);
        inDegree.set(tail, inDegree.get(tail) + 1);
        if (!directed) {
            inDegree.set(head, inDegree.get(head) + 1);
            outDegree.set(tail, inDegree.get(tail) + 1);
        }
    }

    /**
     * Returns all edges between two vertices.
     * @param head Index of the edges' start point.
     * @param tail Index of the edges' end point.
     * @throws graphlab.library.exceptions.InvalidVertexException Thrown when two supplied indexes of vertices are invalid.
     * @return An ArrayList of <I>EdgeType</I> containing all edges between <I>from</I> and <I>to</I>.
     */
    private ArrayList<EdgeType> getEdges(int head, int tail) throws InvalidVertexException {
        if (vertexIdOutOfRange(head) || vertexIdOutOfRange(tail)) throw new InvalidVertexException();
        return adjacencyMatrix.get(head).get(tail);
    }

    /**
     * Returns all edges between two vertices.
     * @param from Index of the edges' start point.
     * @param to Index of the edges' end point.
     * @throws graphlab.library.exceptions.InvalidVertexException Thrown when two supplied indexes of vertices are invalid.
     * @return An ArrayList of <I>EdgeType</I> containing all edges between <I>from</I> and <I>to</I>.
     */
    @Override
    public ArrayList<EdgeType> getEdges(VertexType head, VertexType tail) throws InvalidVertexException {
        int headId = head.getId();
        int tailId = tail.getId();
        checkVertex(tail);
        checkVertex(head);
        return adjacencyMatrix.get(headId).get(tailId);
    }

    @Override
    public void insertVertex(VertexType newVertex) {
        vertices.add(newVertex);
        int size = getVerticesCount();
        newVertex.setId(size - 1);
        if (adjacencyMatrix.size() < size) {
            adjacencyMatrix.ensureCapacity(size * 2);
            adjacencyMatrix.add(new ArrayList<ArrayList<EdgeType>>());
        }
        int newSize = adjacencyMatrix.size();
        for (int row = 0; row < newSize; row++) {
            if (row == newSize - 1) {
                for (int newSizeIndex = 0; newSizeIndex < newSize; newSizeIndex++) {
                    adjacencyMatrix.get(row).add(null);
                }
            } else {
                adjacencyMatrix.get(row).add(null);
            }
        }
        inDegree.add(0);
        outDegree.add(0);
    }

    /**
     * Runs Depth First Search (DFS) algorithm on the graph starting from vertex <I>vertexId</I>.
     * A reference to a PreWorkPostWorkHandler is supplied that contains implementation
     * of pre-work and post-work operations that depends on the application of DFS.
     * @param vertex Starting vertex of the traversal.
     * @param handler A reference to a PreWorkPostWorkHandler that contains implementation
     * of pre-work and post-work operations that depends on the application of DFS.
     * @return Whether the traversal has stopped at the middle by the handler.
     * @throws InvalidVertexException 
     */
    public boolean depthFirstSearch(VertexType vertex, PreWorkPostWorkHandler<VertexType> handler) throws InvalidVertexException {
        return new graphlab.library.algorithms.traversal.DepthFirstSearch<VertexType, EdgeType>(this).doSearch(vertex, handler);
    }

    /**
     * Runs Breadth First Search (BFS) algorithm on the graph starting from vertex <I>vertexId</I>.
     * A reference to a PreWorkHandler is supplied that contains implementation
     * of pre-work operation that depends on the application of BFS.
     * @param vertex Starting vertex of the traversal.
     * @param handler A reference to a PreWorkHandler that contains implementation
     * of pre-work operation that depends on the application of DFS.
     * @return Whether the traversal has stopped at the middle by the handler.
     * @throws InvalidVertexException 
     */
    public boolean breadthFirstSearch(VertexType vertex, PreWorkHandler<VertexType> handler) throws InvalidVertexException {
        return new graphlab.library.algorithms.traversal.BreadthFirstSearch<VertexType, EdgeType>(this).doSearch(vertex, handler);
    }

    /**
     * Checks whether the current graph is a connected graph.
     * @return True if graph is connected and false otherwise.
     */
    public boolean isGraphConnected() {
        try {
            return graphlab.library.algorithms.util.ConnectivityChecker.isGraphConnected(this);
        } catch (InvalidGraphException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks whether the current graph is acyclic.
     * @return True if graph is acyclic and false otherwise.
     */
    public boolean isGraphAcyclic() {
        try {
            return graphlab.library.algorithms.util.AcyclicChecker.isGraphAcyclic(this);
        } catch (InvalidGraphException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Iterator<VertexType> iterator() {
        return vertices.iterator();
    }

    @Override
    public boolean isDirected() {
        return directed;
    }

    /**
     * Returns in-degree of vertex <I>vertexId</I>, the number of edges which
     * their tail goes to the specified vertex.
     * @return in-degree of vertex <I>vertexId</I>.
     * @throws InvalidVertexException 
     */
    private int getInDegree(int vertexId) throws InvalidVertexException {
        if (vertexIdOutOfRange(vertexId)) throw new InvalidVertexException();
        return inDegree.get(vertexId);
    }

    /**
     * Returns out-degree of vertex <I>vertexId</I>, the number of edges which
     * their tale is attached to the specified vertex.
     * @return out-degree of vertex <I>vertexId</I>.
     * @throws InvalidVertexException 
     */
    private int getOutDegree(int vertexId) throws InvalidVertexException {
        if (vertexIdOutOfRange(vertexId)) throw new InvalidVertexException();
        return outDegree.get(vertexId);
    }

    /**
     * This class iterates all, edges coming from or going to a specified vertex. 
     * The order of edges the iterator iterate is undefined because of future code changes.
     * @author Omid Aladini
     *
     */
    private class EdgeIterator implements Iterator<EdgeType> {

        private Iterator<EdgeType> edgesIterator;

        private EdgeType lastEdge = null;

        /**
		 * Constructs an Edge Iterator object which iterates through all the edges in the graph.
		 * Note that if the graph object is changed during iteration, the iteration may not
		 * actually represent current state of the graph. For example, if you deleted an edge 
		 * after construction of this object, the edge would be included in the iteration.
		 */
        public EdgeIterator() {
            ArrayList<EdgeType> edges = new ArrayList<EdgeType>();
            if (directed) {
                for (ArrayList<ArrayList<EdgeType>> aae : adjacencyMatrix) for (ArrayList<EdgeType> ae : aae) for (EdgeType e : ae) edges.add(e);
            } else {
                int iCount = 0;
                int jCount;
                for (ArrayList<ArrayList<EdgeType>> aae : adjacencyMatrix) {
                    Iterator<ArrayList<EdgeType>> it = aae.iterator();
                    jCount = 0;
                    while (iCount >= jCount) {
                        ++jCount;
                        ArrayList<EdgeType> ae = it.next();
                        if (ae == null) continue;
                        for (EdgeType e : ae) edges.add(e);
                    }
                    ++iCount;
                }
            }
            edgesIterator = edges.iterator();
        }

        /**
	     * Number of times edge Iteration is called. This will set as a temporary flag into
	     * edges in order to reduce running time of edge iteration back to O(n^2).
	     */
        int edgeIterationIndex = 0;

        /**
		 * Constructs an Edge Iterator object which iterates through all the edges going to 
		 * or coming from the specified vertex <code>v</code>. 
		 * Note that if the graph object is changed during iteration, the iteration may not
		 * actually represent current state of the graph. For example, if you deleted an edge 
		 * after construction of this object, the edge would be included in the iteration.
		 * @param v Head or tail of desired edges.
		 */
        private EdgeIterator(VertexType v) throws InvalidVertexException {
            checkVertex(v);
            if (!directed) ++edgeIterationIndex;
            ArrayList<EdgeType> edges = new ArrayList<EdgeType>();
            ArrayList<ArrayList<EdgeType>> row = adjacencyMatrix.get(v.getId());
            for (ArrayList<EdgeType> ae : row) for (EdgeType e : ae) {
                edges.add(e);
                if (!directed) e.edgeIterationIndex = edgeIterationIndex;
            }
            for (ArrayList<ArrayList<EdgeType>> aae : adjacencyMatrix) {
                if (row == aae) continue;
                for (ArrayList<EdgeType> ae : aae) for (EdgeType e : ae) {
                    if (e.tail == v) {
                        if (directed) {
                            edges.add(e);
                        } else if (e.edgeIterationIndex != edgeIterationIndex) {
                            e.edgeIterationIndex = edgeIterationIndex;
                            edges.add(e);
                        }
                    }
                }
            }
            edgesIterator = edges.iterator();
        }

        /**
		 * Constructs an Edge Iterator object which iterates through all the edges going to 
		 * or coming from (depending on the second parameter) the specified vertex <code>v</code>. 
		 * If the second parameter it true, then the first parameter is considered to be head of 
		 * all desired edges, and if it's false the first parameter is considered to be tail of desired edges.
		 * Note that if the graph object is changed during iteration, the iteration may not
		 * actually represent current state of the graph. For example, if you deleted an edge 
		 * after construction of this object, the edge would be included in the iteration.
		 * @param v If the second parameter is true indicated the vertex which is head of desired edges, otherwise
		 * it is considered to be tail of desired edges.
		 * @param head True means the first parameter should be considered head of desired edges.
		 */
        public EdgeIterator(VertexType v, boolean head) throws InvalidVertexException {
            checkVertex(v);
            if (!directed) ++edgeIterationIndex;
            ArrayList<EdgeType> edges = new ArrayList<EdgeType>();
            ArrayList<ArrayList<EdgeType>> row = adjacencyMatrix.get(v.getId());
            if (head) {
                for (ArrayList<EdgeType> ae : row) for (EdgeType e : ae) {
                    edges.add(e);
                    if (!directed) e.edgeIterationIndex = edgeIterationIndex;
                }
            } else {
                for (ArrayList<ArrayList<EdgeType>> aae : adjacencyMatrix) {
                    if (row == aae) continue;
                    for (ArrayList<EdgeType> ae : aae) for (EdgeType e : ae) {
                        if (e.tail == v) {
                            if (directed) {
                                edges.add(e);
                            } else if (e.edgeIterationIndex != edgeIterationIndex) {
                                e.edgeIterationIndex = edgeIterationIndex;
                                edges.add(e);
                            }
                        }
                    }
                }
            }
            edgesIterator = edges.iterator();
        }

        public boolean hasNext() {
            return edgesIterator.hasNext();
        }

        public EdgeType next() {
            lastEdge = edgesIterator.next();
            return lastEdge;
        }

        public void remove() {
            try {
                removeEdge(lastEdge);
            } catch (InvalidEdgeException e) {
                System.out.println("Invalid remove operation.");
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns iterator object for the edges.
     * @return iterator object for the edges.
     */
    public Iterator<EdgeType> edgeIterator() {
        return new EdgeIterator();
    }

    @Override
    public Iterator<EdgeType> edgeIterator(VertexType v) throws InvalidVertexException {
        return new EdgeIterator(v);
    }

    @Override
    public Iterator<EdgeType> edgeIterator(VertexType v, boolean head) throws InvalidVertexException {
        return new EdgeIterator(v, head);
    }

    @Override
    public Matrix getAdjacencyMatrix() {
        Matrix matrix = new Matrix(getVerticesCount(), getVerticesCount());
        try {
            if (directed) {
                for (int i = 0; i < getVerticesCount(); i++) for (int j = 0; j < getVerticesCount(); j++) if (getEdges(i, j) != null) matrix.set(i, j, getEdges(i, j).size());
            } else {
                for (int i = 0; i < getVerticesCount(); i++) for (int j = 0; j <= i; j++) if (getEdges(i, j) != null) {
                    matrix.set(i, j, getEdges(i, j).size());
                    if (i != j) matrix.set(j, i, getEdges(i, j).size());
                }
            }
        } catch (Exception e) {
            System.out.println("NEVER-HAPPENS-BUG:getAdjMatrix:");
            e.printStackTrace();
        }
        return matrix;
    }

    @Override
    public void dump() {
        System.out.print('\n');
        for (int i = 0; i < getVerticesCount(); i++) {
            for (int j = 0; j < getVerticesCount(); j++) {
                System.out.print(" ");
                System.out.print(adjacencyMatrix.get(i).get(j) == null ? 0 : 1);
            }
            System.out.println("");
        }
    }

    @Override
    public void removeAllEdges(VertexType head, VertexType tail) throws InvalidVertexException {
        if (vertexIdOutOfRange(head.getId()) || vertexIdOutOfRange(tail.getId()) || head != vertices.get(head.getId()) || tail != vertices.get(tail.getId())) throw new InvalidVertexException();
        adjacencyMatrix.get(head.getId()).get(tail.getId()).clear();
        if (!directed) removeAllEdges(tail, head);
    }

    /**
     * Removes a vertex and all it's connected edges.
     * @param vertexId index of the vertex to be removed
     */
    private void removeVertex(int vertexId) throws InvalidVertexException {
        for (ArrayList<ArrayList<EdgeType>> c : adjacencyMatrix) {
            c.remove(vertexId);
        }
        adjacencyMatrix.remove(vertexId);
        vertices.remove(vertexId);
        inDegree.remove(vertexId);
        outDegree.remove(vertexId);
        setVertexIds();
    }

    @Override
    public void removeVertex(VertexType v) throws InvalidVertexException {
        checkVertex(v);
        removeVertex(v.getId());
    }

    @Override
    public int getInDegree(VertexType v) throws InvalidVertexException {
        checkVertex(v);
        return getInDegree(v.getId());
    }

    @Override
    public int getOutDegree(VertexType v) throws InvalidVertexException {
        checkVertex(v);
        return getOutDegree(v.getId());
    }

    @Override
    public void removeEdge(EdgeType edge) throws InvalidEdgeException {
        int head = edge.head.getId();
        int tail = edge.tail.getId();
        try {
            checkVertex(edge.tail);
            checkVertex(edge.head);
        } catch (InvalidVertexException e) {
            throw new InvalidEdgeException();
        }
        adjacencyMatrix.get(tail).get(head).remove(edge);
        if (!directed) adjacencyMatrix.get(head).get(tail).remove(edge);
    }

    public VertexType getAVertex() {
        Iterator<VertexType> it = iterator();
        if (it.hasNext()) return it.next(); else return null;
    }

    @Override
    public BaseGraph<VertexType, EdgeType> copy(GraphConverter<EdgeType, EdgeType, VertexType, VertexType> gc) throws InvalidGraphException {
        MatrixGraph<VertexType, EdgeType> oGraph = new MatrixGraph<VertexType, EdgeType>(directed, getVerticesCount());
        ArrayList<VertexType> tempAL = new ArrayList<VertexType>(getVerticesCount());
        VertexType tempVertex;
        for (VertexType v : this) {
            tempVertex = gc.convert(v);
            oGraph.insertVertex(tempVertex);
            tempAL.add(tempVertex);
        }
        Iterator<EdgeType> iet = edgeIterator();
        EdgeType edge;
        try {
            while (iet.hasNext()) {
                edge = iet.next();
                oGraph.insertEdge(gc.convert(edge, tempAL.get(edge.head.getId()), tempAL.get(edge.tail.getId())));
            }
        } catch (InvalidVertexException e) {
            throw new InvalidGraphException();
        }
        return oGraph;
    }

    @Override
    public boolean containsVertex(VertexType v) {
        return vertices.contains(v);
    }

    @Override
    public void checkVertex(VertexType v) throws InvalidVertexException {
        if (vertexIdOutOfRange(v.getId()) || v != vertices.get(v.getId())) throw new InvalidVertexException();
    }

    @Override
    public boolean isEdge(VertexType head, VertexType tail) throws InvalidVertexException {
        return (getEdges(head, tail) != null) && (getEdges(head, tail).size() != 0);
    }

    @Override
    public BaseGraph<VertexType, EdgeType> createEmptyGraph() {
        return new MatrixGraph<VertexType, EdgeType>(directed, 0);
    }

    @Override
    public void setDirected(boolean isDirected) {
        throw new RuntimeException("Not yet implemented.");
    }

    @Override
    public BaseVertex[] getVertexArray() {
        BaseVertex[] arr = new BaseVertex[getVerticesCount()];
        for (VertexType v : this) arr[v.getId()] = v;
        return arr;
    }

    @Override
    public int[][] getEdgeArray() {
        int[][] arr = new int[getVerticesCount()][];
        int i = 0;
        int j;
        int k;
        for (ArrayList<ArrayList<EdgeType>> ll : adjacencyMatrix) {
            j = 0;
            ArrayList<Integer> temp = new ArrayList<Integer>();
            for (ArrayList<EdgeType> alet : ll) {
                if (alet != null && alet.size() != 0) temp.add(j++);
            }
            arr[i] = new int[temp.size()];
            k = 0;
            for (Integer vertexId : temp) arr[i][k++] = vertexId;
            ++i;
        }
        return arr;
    }

    @Override
    public Iterator<EdgeType> lightEdgeIterator() {
        return edgeIterator();
    }

    @Override
    public Iterator<EdgeType> lightEdgeIterator(VertexType v) throws InvalidVertexException {
        return edgeIterator(v);
    }

    @Override
    public Iterator<EdgeType> lightEdgeIterator(VertexType v, boolean head) throws InvalidVertexException {
        return edgeIterator(v, head);
    }

    @Override
    public void clear() {
        int expectedNumberOfVertices = 0;
        adjacencyMatrix = new ArrayList<ArrayList<ArrayList<EdgeType>>>(expectedNumberOfVertices);
        for (int rowCount = 0; rowCount < expectedNumberOfVertices; rowCount++) {
            ArrayList<ArrayList<EdgeType>> columns = new ArrayList<ArrayList<EdgeType>>(expectedNumberOfVertices);
            adjacencyMatrix.add(columns);
        }
        vertices = new ArrayList<VertexType>(expectedNumberOfVertices);
        inDegree = new ArrayList<Integer>(expectedNumberOfVertices);
        if (!directed) outDegree = inDegree; else outDegree = new ArrayList<Integer>(expectedNumberOfVertices);
    }
}
