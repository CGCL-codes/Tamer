package de.grogra.xl.impl.base;

import de.grogra.reflect.Reflection;
import de.grogra.reflect.Type;
import de.grogra.xl.lang.VMXState;
import de.grogra.xl.qnp.EdgeDirection;
import de.grogra.xl.qnp.MatchConsumer;
import de.grogra.xl.qnp.NodeData;
import de.grogra.xl.qnp.Predicate;
import de.grogra.xl.qnp.QueryState;
import de.grogra.xl.runtime.Extent;
import de.grogra.xl.runtime.Model;
import de.grogra.xl.util.EHashMap;
import de.grogra.xl.util.IntList;
import de.grogra.xl.util.XBitSet;

/**
 * This class is an abstract base class for implementations of
 * XL's runtime model for graph-like structures. <code>ExtentBase</code>
 * represents a single graph on which XL's query and production statements
 * may operate.
 * <p>
 * The {@linkplain de.grogra.xl.impl.base package documentation} contains
 * more information about the structure of the graph that is implied by
 * this base class.
 * <p>
 * The current derivation mode for rule applications acting on this graph
 * is set by {@link #setDerivationMode(int)} and may be truly parallel
 * ({@link #PARALLEL_MODE}), parallel and non-deterministic
 * ({@link #PARALLEL_NON_DETERMINISTIC_MODE}, one application is chosen
 * out of several applications which delete the same node), or
 * sequential ({@link #SEQUENTIAL_MODE}, only a single rule application
 * per derivation step). 
 * 
 * @author Ole Kniemeyer
 */
public abstract class ExtentBase implements Extent, Cloneable {

    /**
	 * Bit mask for {@link #getDerivationMode()} indicating a true parallel
	 * derivation mode. All rules are applied via every possible match in
	 * parallel. A single node may be deleted by several rule applications. 
	 */
    public static final int PARALLEL_MODE = 0;

    /**
	 * Bit mask for {@link #getDerivationMode()} indicating a parallel
	 * non-deterministic derivation mode. It is ensured that a single node is
	 * deleted by at most one actual rule application. If several potential
	 * rule applications delete the same node, one actual application is chosen
	 * pseudo-randomly.
	 */
    public static final int PARALLEL_NON_DETERMINISTIC_MODE = 1;

    /**
	 * Bit mask for {@link #getDerivationMode()} indicating a sequential
	 * derivation mode. Only one rule application is perfomed in a single step
	 * (as marked by {@link #passTransformationBoundary(boolean)}). If several
	 * applications are possible, only the first one is chosen.
	 */
    public static final int SEQUENTIAL_MODE = 2;

    /**
	 * Bit mask for {@link #getDerivationMode()} indicating a sequential
	 * derivation mode. Only one rule application is perfomed in a single step
	 * (as marked by {@link #passTransformationBoundary(boolean)}). If several
	 * applications are possible, one is chosen pseudo-randomly.
	 */
    public static final int SEQUENTIAL_NON_DETERMINISTIC_MODE = 3;

    /**
	 * Mask for {@link #getDerivationMode()} to obtain the mode part
	 * (one of {@link #PARALLEL_MODE},
	 * {@link #PARALLEL_NON_DETERMINISTIC_MODE},
	 * {@link #SEQUENTIAL_MODE}).
	 */
    public static final int MODE_MASK = 3;

    /**
	 * Bit mask for {@link #getDerivationMode()} which indicates that
	 * rules have to be applied as interpretive rules. 
	 */
    public static final int INTERPRETIVE_FLAG = 4;

    /**
	 * Bit mask for {@link #getDerivationMode()} which indicates that
	 * nodes which were already deleted by previous rule applications
	 * of the current derivation step shall be excluded from further
	 * matches of the same step.
	 */
    public static final int EXCLUDE_DELETED_FLAG = 8;

    final RuntimeModel model;

    VMXState state;

    /**
	 * Current derivation mode.
	 * 
	 * @see #getDerivationMode()
	 */
    int derivationMode = PARALLEL_MODE | EXCLUDE_DELETED_FLAG;

    QueueData queues = null;

    private int seqMatchCount = 0;

    private int seqSegment = -1;

    public ExtentBase(RuntimeModel model, VMXState state) {
        this.model = model;
        this.state = state;
    }

    protected ExtentBase dup(VMXState state) {
        try {
            ExtentBase e = (ExtentBase) clone();
            e.state = state;
            e.derivationMode = derivationMode & ~INTERPRETIVE_FLAG;
            e.queues = null;
            return e;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    public Model getModel() {
        return model;
    }

    public VMXState getVMXState() {
        return state;
    }

    /**
	 * Creates an iterator over the edges of <code>node</code>. The iterator
	 * is set to the first edge of the node, if any. The direction <code>dir</code>
	 * has to be respected by the iterator.
	 * 
	 * @param node the node whose edges are to be iterated
	 * @param dir direction in which edges are traversed (seen from <code>node</code>)
	 * @return an edge iterator
	 */
    public abstract EdgeIterator createEdgeIterator(Object node, EdgeDirection dir);

    public abstract GraphQueue createQueue(QueueCollection qc, boolean additional, QueueDescriptor descr);

    private class QState extends QueryState {

        boolean allowNoninjective;

        QState() {
            super(state, ExtentBase.this);
        }
    }

    public QueryState createQueryState() {
        QState qs = new QState();
        qs.allowNoninjective = nextAllowNoninjective;
        nextAllowNoninjective = defaultAllowNoninjective;
        return qs;
    }

    private boolean defaultAllowNoninjective = false;

    private boolean nextAllowNoninjective = false;

    public boolean allowsNoninjectiveMatches(QueryState qs) {
        return ((QState) qs).allowNoninjective;
    }

    public void allowNoninjectiveMatchesByDefault(boolean value) {
        defaultAllowNoninjective = value;
        nextAllowNoninjective = value;
    }

    public void allowNoninjectiveMatchesForNextQuery(boolean value) {
        nextAllowNoninjective = value;
    }

    public void dispose(QueryState qs) {
    }

    static class QueueData {

        boolean closed = true;

        long stamp = Long.MIN_VALUE;

        QueueCollection queues;

        EHashMap excludeFromMatch = new EHashMap(new NodeData[1], 32, 0.75f);

        NodeData key = new NodeData();

        QueueData(QueueCollection qc) {
            queues = qc;
        }

        boolean isExcludedFromMatch(Object node) {
            key.setNode(node);
            return excludeFromMatch.get(key) != null;
        }

        void excludeFromMatch(Object node) {
            NodeData d = (NodeData) excludeFromMatch.popEntryFromPool();
            if (d == null) {
                d = new NodeData();
            }
            d.setNode(node);
            excludeFromMatch.getOrPut(d);
        }

        void close() {
            closed = true;
            excludeFromMatch.clear();
        }
    }

    /**
	 * Returns the current derivation mode. This is a combination of one of
	 * the bit masks {@link #PARALLEL_MODE},
	 * {@link #PARALLEL_NON_DETERMINISTIC_MODE},
	 * {@link #SEQUENTIAL_MODE} with the flags
	 * {@link #INTERPRETIVE_FLAG} and {@link #EXCLUDE_DELETED_FLAG}.
	 * 
	 * @return current derivation mode
	 * 
	 * @see #setDerivationMode(int)
	 */
    public int getDerivationMode() {
        return derivationMode;
    }

    /**
	 * Sets the current derivation mode. <code>mode</code> is a combination
	 * of the bit masks {@link #PARALLEL_MODE},
	 * {@link #PARALLEL_NON_DETERMINISTIC_MODE},
	 * {@link #SEQUENTIAL_MODE} with the flags
	 * {@link #INTERPRETIVE_FLAG} and {@link #EXCLUDE_DELETED_FLAG}.
	 * 
	 * @param mode desired derivation mode
	 * 
	 * @see #getDerivationMode()
	 */
    public void setDerivationMode(int mode) {
        derivationMode = mode;
        switch(mode & MODE_MASK) {
            case SEQUENTIAL_MODE:
            case SEQUENTIAL_NON_DETERMINISTIC_MODE:
                break;
            default:
                seqSegment = -1;
                break;
        }
    }

    boolean applyMatch(QueryState qs) {
        switch(derivationMode & MODE_MASK) {
            case PARALLEL_MODE:
                return true;
            case PARALLEL_NON_DETERMINISTIC_MODE:
                getQueues().startNewSegment();
                return true;
            case SEQUENTIAL_MODE:
                qs.breakMatching();
                break;
            case SEQUENTIAL_NON_DETERMINISTIC_MODE:
                break;
            default:
                throw new IllegalStateException();
        }
        if (seqSegment < 0) {
            seqSegment = getQueues().startNewSegment();
            seqMatchCount = 1;
            return true;
        }
        if (state.random.nextInt(++seqMatchCount) > 0) {
            return false;
        }
        getQueues().resetToSegment(seqSegment);
        return true;
    }

    public long passTransformationBoundary(boolean close) {
        seqSegment = -1;
        QueueData q = queues;
        if (q == null) {
            q = new QueueData(new QueueCollection(state, this));
            queues = q;
        }
        if (!q.closed) {
            int[] order = null;
            if ((derivationMode & MODE_MASK) == PARALLEL_NON_DETERMINISTIC_MODE) {
                int s = q.queues.startNewSegment();
                order = new int[s];
                for (int i = 0; i < s; i++) {
                    order[i] = i;
                }
                for (int i = 0; i < s; i++) {
                    int j = state.random.nextInt(s);
                    int t = order[i];
                    order[i] = order[j];
                    order[j] = t;
                }
            }
            try {
                if (q.queues.apply(order)) {
                    q.stamp++;
                }
                commitModifications();
            } catch (Exception e) {
                e.printStackTrace();
            }
            q.close();
        }
        if (!close) {
            q.closed = false;
            beginModifications();
        }
        return q.stamp;
    }

    protected abstract void beginModifications();

    protected abstract void commitModifications();

    public QueueCollection getQueues() {
        QueueData q = queues;
        return ((q != null) && !q.closed) ? q.queues : null;
    }

    protected abstract PropertyModificationQueueBase createPropertyModificationQueue(QueueCollection qc);

    public PropertyModificationQueueBase getPropertyModificationQueue() {
        QueueCollection qc = getQueues();
        if (qc == null) {
            throw new IllegalStateException("Queues are currently closed for " + this);
        }
        return qc.getPropertyModificationQueue();
    }

    public boolean excludeFromMatch(Object node, QueryState qs) {
        return queues.isExcludedFromMatch(node);
    }

    public boolean canSupply(Type type) {
        return Reflection.isSupertypeOrSame(model.getNodeType(), type);
    }

    public void supplyNeighbors(Object node, EdgeDirection dir, QueryState qs, int toIndex, int matchIndex, int patternIndex, java.io.Serializable pattern, MatchConsumer consumer, int arg) {
        int bits = (patternIndex >= 0) ? qs.ibound(patternIndex) : ((Number) pattern).intValue();
        for (EdgeIterator i = createEdgeIterator(node, dir); i.hasEdge(); i.moveToNext()) {
            Object o;
            if (dir == EdgeDirection.UNDIRECTED) {
                o = (node == i.source) ? i.target : i.source;
                ;
            } else {
                o = (dir == EdgeDirection.FORWARD) ? i.target : i.source;
                if (o == node) {
                    continue;
                }
            }
            int b = i.edgeBits;
            if (RuntimeModel.testEdgeBits(b, bits)) {
                switch(qs.ibind(matchIndex, b)) {
                    case QueryState.BINDING_MISMATCHED:
                        break;
                    case QueryState.BINDING_MATCHED:
                        qs.amatch(toIndex, o, consumer, arg);
                        break;
                    case QueryState.BINDING_PERFORMED:
                        try {
                            qs.amatch(toIndex, o, consumer, arg);
                        } finally {
                            qs.unbind(matchIndex);
                        }
                        break;
                }
            }
        }
    }

    protected Object getPredecessor(Object node) {
        if (!canSupplyNeighbors(EdgeDirection.BACKWARD)) {
            return null;
        }
        for (EdgeIterator i = createEdgeIterator(node, EdgeDirection.BACKWARD); i.hasEdge(); i.moveToNext()) {
            if ((i.source != node) && RuntimeModel.testEdgeBits(i.edgeBits, RuntimeModel.BRANCH_EDGE | RuntimeModel.SUCCESSOR_EDGE)) {
                node = i.source;
                i.dispose();
                return node;
            }
        }
        return null;
    }

    public Predicate.Matcher createMatcher(Predicate pred, XBitSet providedConstants, IntList neededConstantsOut) {
        return pred.createMatcher(this, providedConstants, neededConstantsOut);
    }
}
