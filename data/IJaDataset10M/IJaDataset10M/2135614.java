package de.grogra.graph;

import java.util.Arrays;
import de.grogra.xl.util.ObjectList;

public final class ArrayPath implements Path {

    public final ObjectList stack = new ObjectList();

    private static final byte DIRECTION = 1;

    private static final byte INSTANCING = 2;

    Graph graph;

    private Object[] objects;

    private long[] ids;

    private int[] edges;

    private byte[] edgeInfos;

    private int count;

    public ArrayPath(Graph graph) {
        objects = new Object[8];
        ids = new long[8];
        count = 0;
        this.graph = graph;
    }

    public ArrayPath(Path path) {
        objects = new Object[Math.max(path.getNodeAndEdgeCount(), 8)];
        ids = new long[Math.max(path.getNodeAndEdgeCount(), 8)];
        set(path);
    }

    public Graph getGraph() {
        return graph;
    }

    public int indexOf(Object object, boolean asNode) {
        for (int i = asNode ? (count - 1) & ~1 : (count - 2) | 1; i >= 0; i -= 2) {
            if (objects[i] == object) {
                return i;
            }
        }
        return -1;
    }

    public void clear(Graph graph) {
        this.graph = graph;
        count = 0;
        for (int i = objects.length - 1; i >= 0; i--) {
            objects[i] = null;
        }
    }

    public void set(Path path) {
        graph = path.getGraph();
        count = path.getNodeAndEdgeCount();
        if (count <= objects.length) {
            for (int i = objects.length - 1; i >= count; i--) {
                objects[i] = null;
            }
        } else {
            objects = new Object[Math.max(count, 8)];
            ids = new long[Math.max(count, 8)];
        }
        for (int i = count - 1; i >= 0; i--) {
            objects[i] = path.getObject(i);
            ids[i] = path.getObjectId(i);
            if ((i & 1) != 0) {
                setEdges(i, path.getEdgeBits(i), (byte) ((path.isInEdgeDirection(i) ? DIRECTION : 0) + (path.isInstancingEdge(i) ? INSTANCING : 0)));
            }
        }
    }

    public void pushNode(Object node, long id) {
        if ((count & 1) != 0) {
            throw new IllegalStateException("Path already ends in a node, cannot push another node.");
        }
        push(node, id);
    }

    public void pushEdgeSet(Object edge, long id, boolean instancing) {
        if ((count & 1) == 0) {
            throw new IllegalStateException("Path already ends in an edgeset, cannot push " + "another edgeset.");
        }
        setEdges(count, 0, instancing ? INSTANCING : 0);
        push(edge, id);
    }

    public void pushEdges(int e, boolean dir, long id, boolean instancing) {
        if ((count & 1) == 0) {
            throw new IllegalStateException("Path already ends in an edgeset, cannot push " + "another edgeset.");
        }
        setEdges(count, e, (byte) ((dir ? DIRECTION : 0) + (instancing ? INSTANCING : 0)));
        push(null, id);
    }

    private void setEdges(int index, int e, byte info) {
        int[] a = edges;
        byte[] d = edgeInfos;
        int n;
        if (a == null) {
            edges = a = new int[index + 1];
            edgeInfos = d = new byte[index + 1];
        } else if (index >= (n = a.length)) {
            System.arraycopy(a, 0, edges = a = new int[2 * index + 8], 0, n);
            System.arraycopy(d, 0, edgeInfos = d = new byte[2 * index + 8], 0, n);
        }
        d[index] = info;
        a[index] = e;
    }

    private void push(Object object, long id) {
        if (objects.length == count) {
            System.arraycopy(objects, 0, objects = new Object[count * 2], 0, count);
            System.arraycopy(ids, 0, ids = new long[count * 2], 0, count);
        }
        objects[count] = object;
        ids[count++] = id;
    }

    public Object popNode() {
        if ((count & 1) == 0) {
            throw new IllegalStateException("Path does not end in a node.");
        }
        return pop();
    }

    public Object popEdgeSet() {
        if ((count & 1) != 0) {
            throw new IllegalStateException("Path does not end in an edgeset.");
        }
        return pop();
    }

    private Object pop() {
        int c = --count;
        Object o = objects[c];
        objects[c] = null;
        return o;
    }

    public int getNodeAndEdgeCount() {
        return count;
    }

    public boolean endsInNode() {
        return (count & 1) != 0;
    }

    private int getIndex(int index) {
        if (index >= 0) {
            if (index >= count) {
                throw new IndexOutOfBoundsException(Integer.toString(index));
            }
        } else if ((index += count) < 0) {
            throw new IndexOutOfBoundsException(Integer.toString(index - count));
        }
        return index;
    }

    public Object getObject(int index) {
        return objects[getIndex(index)];
    }

    public long getObjectId(int index) {
        return ids[getIndex(index)];
    }

    public int getEdgeBits(int index) {
        index = getIndex(index);
        if ((index & 1) == 0) {
            throw new IllegalArgumentException("An index of an edgeset within a path has to be odd.");
        }
        Object e = objects[index];
        return (e != null) ? graph.getEdgeBits(e) : edges[index];
    }

    public boolean isInEdgeDirection(int index) {
        index = getIndex(index);
        if ((index & 1) == 0) {
            throw new IllegalArgumentException("An index of an edgeset within a path has to be odd.");
        }
        Object e = objects[index];
        return (e != null) ? graph.getSourceNode(e) == objects[index - 1] : (edgeInfos[index] & DIRECTION) != 0;
    }

    public boolean isInstancingEdge(int index) {
        index = getIndex(index);
        if ((index & 1) == 0) {
            throw new IllegalArgumentException("An index of an edgeset within a path has to be odd.");
        }
        return (edgeInfos[index] & INSTANCING) != 0;
    }

    public Object getNode(int index) {
        index = getIndex(index);
        if ((index & 1) != 0) {
            throw new IllegalArgumentException("An index of a node within a path has to be even.");
        }
        return objects[index];
    }

    @Override
    public String toString() {
        Object[] copy = new Object[count];
        System.arraycopy(objects, 0, copy, 0, count);
        return Arrays.toString(copy);
    }
}
