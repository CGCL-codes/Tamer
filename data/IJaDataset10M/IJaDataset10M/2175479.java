package de.grogra.rgg.model;

import java.util.NoSuchElementException;
import de.grogra.graph.Graph;
import de.grogra.graph.impl.GraphManager;
import de.grogra.graph.impl.Node;
import de.grogra.imp3d.objects.GRSVertex;
import de.grogra.persistence.PersistenceField;
import de.grogra.persistence.Transaction;
import de.grogra.xl.impl.base.GraphQueue;
import de.grogra.xl.impl.queues.Queue;
import de.grogra.xl.impl.queues.QueueCollection;
import de.grogra.xl.impl.queues.QueueDescriptor;
import de.grogra.xl.query.RuntimeModelException;
import de.grogra.xl.util.ByteList;
import de.grogra.xl.util.IntList;
import de.grogra.xl.util.LongList;
import de.grogra.xl.util.ObjectList;

/**
 * This XL queue can be used for parallel application of
 * vertex-vertex operations on graph
 * rotation systems. Given an instance <code>g</code> of
 * <code>VVQueue</code>, one can write:
 * <pre>
 * g &lt;== comma-separated list of GRS operations;
 * </pre>
 * where the operations include
 * <dl>
 * <dt><code>v [a b c]</code></dt>
 * <dd>Sets cyclic neighborhood of v to (a, b, c)</dd>
 * <dt><code>a b in v</code></dt>
 * <dd>Modifies cyclic neighborhood of v such that (a, b) is part of the cycle</dd>
 * <dt><code>~a in v</code></dt>
 * <dd>Removes a from cyclic neighbourhood of v</dd>
 * <dt><code>a &gt;&gt; b in v</code></dt>
 * <dd>Replaces a by b in cyclic neighborhood of v</dd>
 * <dt><code>p &lt;+ a +&gt; q</code></dt>
 * <dd>Splits edge from p to q and inserts a</dd>
 * </il>
 * 
 * @author Ole Kniemeyer
 */
public class VVQueue implements Queue {

    private static final byte END = 0;

    private static final byte INSERT_BEFORE = 1;

    private static final byte INSERT_AFTER = 2;

    private static final byte MAKE_FOLLOWER = 3;

    private static final byte REMOVE = 4;

    private static final byte REPLACE = 5;

    private static final byte SET_NEIGHBORS = 6;

    private static final byte ADD_NEIGHBOR = 7;

    private static final byte SPLIT_EDGE = 8;

    private PropertyQueue makePersistentQueue;

    private final ByteList actions = new ByteList();

    private final LongList vertices = new LongList();

    private final IntList segments = new IntList();

    public static final QueueDescriptor<VVQueue> DESCRIPTOR = new Descriptor();

    public static final class Descriptor extends QueueDescriptor<VVQueue> {

        @Override
        protected QueueDescriptor[] queuesToProcessBefore() {
            return new QueueDescriptor[] { GraphQueue.LAST_QUEUE };
        }

        @Override
        protected QueueDescriptor[] queuesToProcessAfter() {
            return QueueDescriptor.DESCRIPTOR_0;
        }

        @Override
        public VVQueue createQueue(QueueCollection qc) {
            return new VVQueue(qc, qc.getQueue(PropertyQueue.MAKE_PERSISTENT));
        }
    }

    private final RGGGraph rggGraph;

    private final GraphManager graph;

    VVQueue(QueueCollection qc, PropertyQueue mpq) {
        rggGraph = (RGGGraph) qc.getGraph();
        graph = rggGraph.manager;
        makePersistentQueue = mpq;
    }

    public QueueDescriptor<?> getDescriptor() {
        return DESCRIPTOR;
    }

    private void addVertex(GRSVertex v) {
        if (v.getId() < 0) {
            makePersistentQueue.makePersistent(v);
        }
        vertices.add(v.getId());
    }

    public void insertBefore(GRSVertex v, GRSVertex old, GRSVertex in) {
        actions.add(INSERT_BEFORE);
        addVertex(v);
        addVertex(old);
        addVertex(in);
    }

    public void insertAfter(GRSVertex v, GRSVertex old, GRSVertex in) {
        actions.add(INSERT_AFTER);
        addVertex(v);
        addVertex(old);
        addVertex(in);
    }

    public void makeFollower(GRSVertex a, GRSVertex b, GRSVertex in) {
        actions.add(MAKE_FOLLOWER);
        addVertex(a);
        addVertex(b);
        addVertex(in);
    }

    public void remove(GRSVertex old, GRSVertex in) {
        actions.add(REMOVE);
        addVertex(old);
        addVertex(in);
    }

    public void replace(GRSVertex subs, GRSVertex old, GRSVertex in) {
        actions.add(REPLACE);
        addVertex(subs);
        addVertex(old);
        addVertex(in);
    }

    public VVQueue setNeighbors(GRSVertex vertex) {
        actions.add(SET_NEIGHBORS);
        addVertex(vertex);
        return this;
    }

    public void addNeighbor(GRSVertex neighbor) {
        actions.add(ADD_NEIGHBOR);
        addVertex(neighbor);
    }

    public VVQueue operator$shl(GRSVertex neighbor) {
        addNeighbor(neighbor);
        return this;
    }

    public void splitEdge(GRSVertex v, GRSVertex p, GRSVertex q) {
        actions.add(SPLIT_EDGE);
        addVertex(v);
        addVertex(p);
        addVertex(q);
    }

    private int actionIndex;

    private int vertexIndex;

    private byte nextAction() {
        if (actionIndex < actions.size()) {
            return actions.get(actionIndex++);
        } else {
            actionIndex++;
            return END;
        }
    }

    private GRSVertex nextVertex() {
        long id = vertices.get(vertexIndex++);
        GRSVertex v = (GRSVertex) graph.getObject(id);
        if (v == null) {
            throw new NoSuchElementException("Node " + id + " does not exist.");
        }
        return v;
    }

    private final int[] index = new int[1];

    private Transaction xa;

    private ObjectList<GRSVertex> newVertices;

    private static final PersistenceField NEIGHBOR = GRSVertex.neighbors$FIELD.getArrayChain(1);

    private void makeChild(GRSVertex v, GRSVertex in) {
        Node mesh = in.findAdjacent(true, false, Graph.BRANCH_EDGE | Graph.SUCCESSOR_EDGE);
        if (mesh != null) {
            Node p = v.findAdjacent(true, false, Graph.BRANCH_EDGE | Graph.SUCCESSOR_EDGE);
            if (p != mesh) {
                if (p != null) {
                    p.removeEdgeBitsTo(v, -1, xa);
                }
                newVertices.add(v);
                mesh.addEdgeBitsTo(v, Graph.BRANCH_EDGE, xa);
            }
        }
    }

    public boolean process(int[] segs) throws RuntimeModelException {
        xa = graph.getActiveTransaction();
        newVertices = rggGraph.getThreadData0().newGRSVertices;
        newVertices.clear();
        actionIndex = 0;
        vertexIndex = 0;
        boolean modified = false;
        if (segs == null) {
            while (actionIndex < actions.size) {
                modified |= applyNext();
            }
        } else {
            for (int s = 0; s < segs.length; s++) {
                if (segs[s] >= 0) {
                    int i = 2 * segs[s];
                    if (i < segments.size) {
                        actionIndex = segments.get(i);
                        vertexIndex = segments.get(i + 1);
                        i += 2;
                        int end = (i == segments.size) ? actions.size : segments.get(i);
                        while (actionIndex < end) {
                            modified |= applyNext();
                        }
                    }
                }
            }
        }
        xa = null;
        return modified;
    }

    private boolean applyNext() {
        switch(nextAction()) {
            case INSERT_BEFORE:
                {
                    GRSVertex v = nextVertex();
                    GRSVertex old = nextVertex();
                    GRSVertex in = nextVertex();
                    ObjectList<GRSVertex> nb = in.getNeighbors();
                    int i = nb.indexOf(old);
                    if (i < 0) {
                        return false;
                    }
                    makeChild(v, in);
                    index[0] = i;
                    NEIGHBOR.insertObject(in, index, v, xa);
                    return true;
                }
            case INSERT_AFTER:
                {
                    GRSVertex v = nextVertex();
                    GRSVertex old = nextVertex();
                    GRSVertex in = nextVertex();
                    ObjectList<GRSVertex> nb = in.getNeighbors();
                    int i = nb.indexOf(old);
                    if (i < 0) {
                        return false;
                    }
                    makeChild(v, in);
                    index[0] = (i < nb.size - 1) ? i + 1 : 0;
                    NEIGHBOR.insertObject(in, index, v, xa);
                    return true;
                }
            case MAKE_FOLLOWER:
                {
                    GRSVertex a = nextVertex();
                    GRSVertex b = nextVertex();
                    GRSVertex in = nextVertex();
                    ObjectList<GRSVertex> nb = in.getNeighbors();
                    int i = nb.indexOf(a);
                    if (i >= 0) {
                        makeChild(b, in);
                        index[0] = (i < nb.size - 1) ? i + 1 : 0;
                        NEIGHBOR.insertObject(in, index, b, xa);
                        return true;
                    } else if ((i = nb.indexOf(b)) >= 0) {
                        makeChild(a, in);
                        index[0] = i;
                        NEIGHBOR.insertObject(in, index, a, xa);
                        return true;
                    }
                    return false;
                }
            case REMOVE:
                {
                    GRSVertex old = nextVertex();
                    GRSVertex in = nextVertex();
                    int i = in.getNeighborIndex(old);
                    if (i < 0) {
                        return false;
                    }
                    index[0] = i;
                    NEIGHBOR.removeObject(in, index, xa);
                    return true;
                }
            case REPLACE:
                {
                    GRSVertex subs = nextVertex();
                    GRSVertex old = nextVertex();
                    GRSVertex in = nextVertex();
                    int i = in.getNeighborIndex(old);
                    if (i < 0) {
                        return false;
                    }
                    makeChild(subs, in);
                    index[0] = i;
                    NEIGHBOR.setObject(in, index, subs, xa);
                    return true;
                }
            case SET_NEIGHBORS:
                {
                    GRSVertex in = nextVertex();
                    ObjectList<GRSVertex> nb = new ObjectList<GRSVertex>(10);
                    while (nextAction() == ADD_NEIGHBOR) {
                        GRSVertex v = nextVertex();
                        makeChild(v, in);
                        nb.add(v);
                    }
                    actionIndex--;
                    nb.trimToSize();
                    GRSVertex.neighbors$FIELD.setObject(in, null, nb, xa);
                    return true;
                }
            case SPLIT_EDGE:
                {
                    GRSVertex v = nextVertex();
                    GRSVertex p = nextVertex();
                    GRSVertex q = nextVertex();
                    makeChild(v, p);
                    int i = p.getNeighborIndex(q);
                    if (i >= 0) {
                        index[0] = i;
                        NEIGHBOR.setObject(p, index, v, xa);
                    }
                    i = q.getNeighborIndex(p);
                    if (i >= 0) {
                        index[0] = i;
                        NEIGHBOR.setObject(q, index, v, xa);
                    }
                    ObjectList<GRSVertex> nb = new ObjectList<GRSVertex>(2);
                    nb.add(p);
                    nb.add(q);
                    GRSVertex.neighbors$FIELD.setObject(v, null, nb, xa);
                    return true;
                }
            default:
                throw new IllegalStateException();
        }
    }

    public void clearSegmentsToExclude(int[] segs) {
    }

    public void clear() {
        actions.clear();
        vertices.clear();
        segments.clear();
    }

    public void markSegment(int n) {
        n = (n + 1) * 2;
        while (segments.size < n) {
            segments.push(actions.size, vertices.size);
        }
    }

    public void resetToSegment(int n) {
        n = (n + 1) * 2;
        actions.setSize(segments.elements[n - 2]);
        vertices.setSize(segments.elements[n - 1]);
        segments.setSize(n);
    }
}
