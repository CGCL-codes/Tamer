package net.walend.digraph;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.util.Arrays;
import java.util.NoSuchElementException;
import net.walend.collection.IndexedIterator;
import net.walend.collection.HasState;
import net.walend.collection.Bag;
import net.walend.collection.MapBag;
import net.walend.collection.ImmutableBag;
import net.walend.collection.ConcurrentModificationException;
import net.walend.grid.MutableArrayGrid2D;
import net.walend.grid.MutableGrid2D;

/**
An extension of AbstractMatrixCEDigraph that keeps an additional int[][] that shows what nodes can be reached from what other nodes. This speeds up the getFromIndices[] method to constant time and the EdgeIterator to O(e) time. It also slows down the removeEdge to O(n) and the removeNode method to O(n^2).

@author <a href="http://walend.net">David Walend</a> <a href="mailto:dfw1@cornell.edu">dfw1@cornell.edu</a>
 */
public abstract class AbstractLMCEDigraph extends AbstractMatrixCEDigraph {

    private int[][] fromIndices;

    protected AbstractLMCEDigraph(int nodeCapacity) {
        super(nodeCapacity);
        fromIndices = new int[nodeCapacity][];
        Arrays.fill(fromIndices, new int[0]);
    }

    protected AbstractLMCEDigraph(CEDigraph digraph) {
        this(digraph.nodeCount());
        addNodes(digraph.getNodes());
        try {
            EdgeIterator edgeIt = digraph.edgeIterator();
            while (edgeIt.hasNext()) {
                edgeIt.next();
                Object edge = edgeIt.edge();
                Object fromNode = edgeIt.fromNode();
                Object toNode = edgeIt.toNode();
                this.addEdge(fromNode, toNode, edge);
            }
        } catch (DigraphException de) {
            throw new ConcurrentModificationException(de);
        }
    }

    protected AbstractLMCEDigraph(UEDigraph digraph) {
        this(digraph.nodeCount());
        addNodes(digraph.getNodes());
        try {
            EdgeIterator edgeIt = digraph.edgeIterator();
            while (edgeIt.hasNext()) {
                edgeIt.next();
                Object edge = edgeIt.edge();
                Object fromNode = edgeIt.fromNode();
                Object toNode = edgeIt.toNode();
                this.addEdge(fromNode, toNode, edge);
            }
        } catch (DigraphException de) {
            throw new ConcurrentModificationException(de);
        }
    }

    protected AbstractLMCEDigraph(GEDigraph digraph, Object edge) {
        this(digraph.nodeCount());
        addNodes(digraph.getNodes());
        try {
            EdgeNodeIterator edgeIt = digraph.edgeNodeIterator();
            while (edgeIt.hasNext()) {
                edgeIt.next();
                Object fromNode = edgeIt.fromNode();
                Object toNode = edgeIt.toNode();
                this.addEdge(fromNode, toNode, edge);
            }
        } catch (DigraphException de) {
            throw new ConcurrentModificationException(de);
        }
    }

    protected class LMEdgeIterator implements IndexedEdgeIterator {

        private int i = 0;

        private int j = -1;

        protected LMEdgeIterator() {
        }

        /**
           Returns true if there are more edges in this iterator.
        */
        public boolean hasNext() {
            if (i >= fromIndices.length) {
                return false;
            }
            if (j + 1 < fromIndices[i].length) {
                return true;
            }
            for (int ii = i + 1; ii < fromIndices.length; ii++) {
                if (fromIndices[ii].length > 0) {
                    return true;
                }
            }
            return false;
        }

        /**
           Advances to the next edge in the iterator.
           
           @throws NoSuchElementException if the iterator has nothing left.
        */
        public void next() {
            if (i >= fromIndices.length) {
                throw new NoSuchElementException("i is " + i + " and j is " + j);
            }
            if (j + 1 < fromIndices[i].length) {
                j++;
                return;
            }
            for (i++; i < fromIndices.length; i++) {
                if (fromIndices[i].length > 0) {
                    j = 0;
                    return;
                }
            }
            throw new NoSuchElementException("i is " + i + " and j is " + j);
        }

        /**
           Removes the current edge from the digraph.
           
           @throws UnsupportedOperationException if the digraph is immutable.
           @throws IllegalStateException if the next method has not yet been called, or the remove method has already been called after the last call to the next method.
        */
        public void remove() {
            try {
                removeEdge(fromNode(), toNode());
            } catch (NodeMissingException nme) {
                throw new ConcurrentModificationException(nme);
            }
        }

        private void check() {
            if (j < -1) {
                throw new IllegalStateException("Please call next() before calling fromNode()");
            }
            if (i >= fromIndices.length || j >= fromIndices[i].length) {
                throw new IllegalStateException("This iterator has no more edges.");
            }
        }

        /**
           Returns the node that his edge begins at.
           
           @throws IllegalStateException if the next method has not yet been called, or the remove method has already been called after the last call to the next method.
        */
        public Object fromNode() {
            check();
            return getNode(fromIndices[i][j]);
        }

        public int fromIndex() {
            check();
            return fromIndices[i][j];
        }

        /**
           Returns the node that this edge reaches.
           
           @throws IllegalStateException if the next method has not yet been called, or the remove method has already been called after the last call to the next method.
        */
        public Object toNode() {
            check();
            return getNode(i);
        }

        public int toIndex() {
            check();
            return i;
        }

        public Object edge() {
            check();
            return getEdge(fromIndices[i][j], i);
        }
    }

    public EdgeIterator edgeIterator() {
        return indexedEdgeIterator();
    }

    /**
@throws ArrayIndexOutOfBoundsException if index does not have a node. Use nodeIndices() or getNodeIndex() to avoid these. In your code, catch ArrayIndexOutOfBoundsException and throw a ConcurrentModificationException if you think that's the problem.
     */
    public int countInboundEdges(int toIndex) {
        return fromIndices[toIndex].length;
    }

    public int[] getFromIndices(int toIndex) {
        return fromIndices[toIndex];
    }

    /**
@throws ArrayIndexOutOfBoundsException if index does not have a node. Use nodeIndices() or getNodeIndex() to avoid these. In your code, catch ArrayIndexOutOfBoundsException and throw a ConcurrentModificationException if you think that's the problem.
     */
    public Bag getInboundEdges(int toIndex) {
        Bag result = new MapBag(fromIndices[toIndex].length);
        for (int i = 0; i < fromIndices[toIndex].length; i++) {
            result.add(getEdge(fromIndices[toIndex][i], toIndex));
        }
        return new ImmutableBag(result);
    }

    public IndexedEdgeIterator indexedEdgeIterator() {
        return new LMEdgeIterator();
    }

    /**
@throws ArrayIndexOutOfBoundsException if index does not have a node. Use nodeIndices() or getNodeIndex() to avoid these. In your code, catch ArrayIndexOutOfBoundsException and throw a ConcurrentModificationException if you think that's the problem.
     */
    protected Object addEdge(int fromIndex, int toIndex, Object edge) {
        Object result = super.addEdge(fromIndex, toIndex, edge);
        if (result == null) {
            int[] fromInds = fromIndices[toIndex];
            int[] newFromInds = new int[fromInds.length + 1];
            System.arraycopy(fromInds, 0, newFromInds, 0, fromInds.length);
            newFromInds[fromInds.length] = fromIndex;
            fromIndices[toIndex] = newFromInds;
        }
        return result;
    }

    /**
@throws ArrayIndexOutOfBoundsException if index does not have a node. Use nodeIndices() or getNodeIndex() to avoid these. In your code, catch ArrayIndexOutOfBoundsException and throw a ConcurrentModificationException if you think that's the problem.
     */
    protected Bag removeNode(int index) {
        Bag result = super.removeNode(index);
        fromIndices[index] = new int[0];
        for (int i = 0; i < fromIndices.length; i++) {
            if (fromIndices[i].length != 0) {
                int indexOfGoner = -1;
                for (int j = 0; j < fromIndices[i].length; j++) {
                    if (fromIndices[i][j] == index) {
                        indexOfGoner = j;
                    }
                }
                if (indexOfGoner > -1) {
                    int[] newFromInds = new int[fromIndices[i].length - 1];
                    System.arraycopy(fromIndices[i], 0, newFromInds, 0, indexOfGoner);
                    if (indexOfGoner < newFromInds.length) {
                        System.arraycopy(fromIndices[i], indexOfGoner + 1, newFromInds, indexOfGoner, newFromInds.length - indexOfGoner);
                    }
                    fromIndices[i] = newFromInds;
                }
            }
        }
        return result;
    }

    /**
@throws ArrayIndexOutOfBoundsException if index does not have a node. Use nodeIndices() or getNodeIndex() to avoid these. In your code, catch ArrayIndexOutOfBoundsException and throw a ConcurrentModificationException if you think that's the problem.
     */
    protected Object removeEdge(int fromIndex, int toIndex) {
        Object result = super.removeEdge(fromIndex, toIndex);
        if (result != null) {
            int[] fromInds = fromIndices[toIndex];
            int indexOfGoner = -1;
            for (int j = 0; j < fromInds.length; j++) {
                if (fromInds[j] == fromIndex) {
                    indexOfGoner = j;
                }
            }
            if (indexOfGoner != -1) {
                int[] newFromInds = new int[fromInds.length - 1];
                System.arraycopy(fromInds, 0, newFromInds, 0, indexOfGoner);
                if (indexOfGoner < newFromInds.length) {
                    System.arraycopy(fromInds, indexOfGoner + 1, newFromInds, indexOfGoner, fromInds.length - 1);
                }
                fromIndices[toIndex] = newFromInds;
            }
        }
        return result;
    }

    /**
       This method doubles the size of the matrix 
    */
    protected void growMatrix(int newSize) {
        super.growMatrix(newSize);
        if (newSize > fromIndices.length) {
            int[][] newFrom = new int[newSize][];
            System.arraycopy(fromIndices, 0, newFrom, 0, fromIndices.length);
            Arrays.fill(newFrom, fromIndices.length, newFrom.length, new int[0]);
            fromIndices = newFrom;
        }
    }

    protected void clearEdges() {
        super.clearEdges();
        Arrays.fill(fromIndices, new int[0]);
    }
}
