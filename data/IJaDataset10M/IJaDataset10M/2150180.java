package net.sf.pged.util.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * The UnorientGraph class is the concrete implementation
 * of unorient graph with adjacency list.
 *
 * @author dude03
 *
 * @param <V> the vertex class.
 * @param <E> the edge class.
 */
public class UnorientGraph<V, E extends Edge<V>> extends OrientGraph<V, E> {

    public static void main(String[] args) {
        UnorientGraph<String, SimpleEdge<String>> g = new UnorientGraph<String, SimpleEdge<String>>();
        g.addVertex("1");
        g.addVertex("2");
        g.addVertex("3");
        g.addEdge(new SimpleEdge<String>("1", "1"));
        g.addEdge(new SimpleEdge<String>("1", "2"));
        g.addEdge(new SimpleEdge<String>("1", "3"));
        System.out.println(g);
    }

    public UnorientGraph() {
        orient = false;
    }

    public boolean addEdge(E e) {
        if (super.addEdge(e)) {
            V source = e.getSource();
            V target = e.getTarget();
            if (source != target) {
                E newEdge = (E) e.clone();
                newEdge.revert();
                super.addEdge(newEdge);
            }
            return true;
        }
        return false;
    }

    public boolean removeEdges(V v) {
        Set<E> edgesOfVertex = incidentList.get(v);
        if ((edgesOfVertex != null) && !edgesOfVertex.isEmpty()) {
            Iterator<E> edgeIter = edgesOfVertex.iterator();
            while (edgeIter.hasNext()) {
                E edge = edgeIter.next();
                V source = edge.getSource();
                V target = edge.getTarget();
                if (!target.equals(source)) {
                    edge.setSource(target);
                    edge.setTarget(source);
                    incidentList.get(target).remove(edge);
                }
            }
            edgesOfVertex.clear();
            return true;
        }
        return false;
    }

    public boolean removeEdge(E e) {
        V source = e.getSource();
        V target = e.getTarget();
        if (super.removeEdge(e)) {
            removeDupEdge(e);
            return true;
        }
        return false;
    }

    private void removeDupEdge(E e) {
        V source = e.getSource();
        V target = e.getTarget();
        if (source != target) {
            e.setSource(target);
            e.setTarget(source);
            super.removeEdge(e);
        }
    }

    public boolean removeVertex(V v) {
        removeEdges(v);
        return incidentList.remove(v) != null;
    }

    public int edgeCount() {
        int edgeCount = 0;
        Iterator<E> allEdgeIter = edgeIterator();
        while (allEdgeIter.hasNext()) {
            allEdgeIter.next();
            edgeCount++;
        }
        return edgeCount;
    }

    public String toString() {
        String result = "";
        result += "UnorientGraph = {\n";
        Iterator<V> vertexIter = vertexIterator();
        while (vertexIter.hasNext()) {
            V v = vertexIter.next();
            result += v.toString() + " : ";
            Iterator<E> edgeIter = edgeIteratorOut(v);
            while (edgeIter.hasNext()) {
                E e = edgeIter.next();
                result += (e.getSource().equals(v)) ? (e.getTarget().toString()) : (e.getSource().toString()) + " ";
            }
            result += "\n";
        }
        result += "}";
        return result;
    }

    public Object getAttr(E e, int k) {
        Map<E, Object> edgeMap = edgesAttrs.get(k);
        if (edgeMap != null) {
            Object attr = edgeMap.get(e);
            if (attr == null) {
                e.revert();
                attr = edgeMap.get(e);
                e.revert();
            }
            return attr;
        }
        return null;
    }

    public Object putAttr(E e, int k, Object o) {
        boolean isReverted = false;
        if (!containsEdge(e)) {
            e.revert();
            isReverted = true;
            if (!containsEdge(e)) throw new NoSuchThatEdgeException("Only debug exception");
        }
        Map<E, Object> edgeMap = edgesAttrs.get(k);
        if (edgeMap == null) {
            edgeMap = new HashMap<E, Object>();
            edgesAttrs.put(k, edgeMap);
        }
        edgeMap.put(e, o);
        if (isReverted) e.revert();
        return o;
    }

    public Object removeAttr(E e, int k) {
        Map<E, Object> edgeMap = edgesAttrs.get(k);
        if (edgeMap != null) {
            Object value;
            if (edgeMap.containsKey(e)) {
                value = edgeMap.remove(e);
            } else {
                e.revert();
                value = edgeMap.remove(e);
                e.revert();
            }
            return value;
        }
        return null;
    }

    public Iterator<E> edgeIterator() {
        return new AllEdgeIterator();
    }

    public int edgeCountIn(V v) {
        return edgeCountOut(v);
    }

    public Iterator<E> edgeIteratorOut(V v) {
        if (incidentList.containsKey(v)) return new OutEdgeIterator(v);
        return null;
    }

    public Iterator<E> edgeIteratorIn(V v) {
        return edgeIteratorOut(v);
    }

    public Iterator<V> vertexIterator() {
        return new VertexIterator();
    }

    protected class OutEdgeIterator implements Iterator<E> {

        private Iterator<E> edgeIter = null;

        private E curEdge = null;

        public OutEdgeIterator(V v) {
            edgeIter = incidentList.get(v).iterator();
        }

        public boolean hasNext() {
            return edgeIter.hasNext();
        }

        public E next() {
            curEdge = edgeIter.next();
            return curEdge;
        }

        public void remove() {
            if (curEdge == null) throw new IllegalStateException();
            removeDupEdge(curEdge);
            edgeIter.remove();
            curEdge = null;
        }
    }

    private class VertexIterator extends OrientGraph<V, E>.VertexIterator<V, E> {

        public VertexIterator() {
            super();
        }

        public void remove() {
            removeEdges((V) curVertex);
            vertexIter.remove();
        }
    }

    private class AllEdgeIterator implements Iterator<E> {

        private OrientGraph<V, E>.AllEdgeIterator<V, E> orientEdgeIter = null;

        private Set<V> viewedVertexes = null;

        private V prevVertex = null;

        private E nextEdge = null;

        private boolean isNextCalled = false;

        public AllEdgeIterator() {
            orientEdgeIter = new OrientGraph.AllEdgeIterator();
            viewedVertexes = new HashSet<V>();
            isNextCalled = false;
        }

        public boolean hasNext() {
            return findNextEdge();
        }

        public E next() {
            if (!findNextEdge()) throw new NoSuchElementException();
            isNextCalled = true;
            return nextEdge;
        }

        public void remove() {
            orientEdgeIter.remove();
            removeDupEdge(nextEdge);
        }

        private boolean findNextEdge() {
            if (!isNextCalled && nextEdge != null) return true;
            while (orientEdgeIter.hasNext()) {
                if (orientEdgeIter.prevEdgeIter != null) viewedVertexes.add(prevVertex);
                nextEdge = orientEdgeIter.next();
                prevVertex = nextEdge.getSource();
                if (!viewedVertexes.contains(nextEdge.getTarget())) {
                    System.out.println("viewedVertexes " + viewedVertexes);
                    isNextCalled = false;
                    return true;
                }
            }
            return false;
        }
    }
}
