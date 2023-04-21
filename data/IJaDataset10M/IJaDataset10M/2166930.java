package de.grogra.xl.impl.base;

import java.io.IOException;
import java.util.concurrent.Executor;
import de.grogra.reflect.TypeLoader;
import de.grogra.util.HierarchicalQueue;
import de.grogra.xl.impl.queues.Queue;
import de.grogra.xl.impl.queues.QueueCollection;
import de.grogra.xl.impl.queues.QueueDescriptor;
import de.grogra.xl.query.RuntimeModelException;
import de.grogra.xl.util.ObjectList;

public abstract class GraphQueue extends HierarchicalQueue implements Queue, Executor {

    public static final AddNodeDescriptor ADD_NODE_DESCRIPTOR = new AddNodeDescriptor();

    public static final class AddNodeDescriptor extends QueueDescriptor<GraphQueue> {

        @Override
        protected QueueDescriptor[] queuesToProcessBefore() {
            return DESCRIPTOR_0;
        }

        @Override
        protected QueueDescriptor[] queuesToProcessAfter() {
            return new QueueDescriptor[] { CONNECT_DESCRIPTOR, ADD_EDGE_DESCRIPTOR, ADD_UNDIRECTED_EDGE_DESCRIPTOR, DELETE_EDGE_DESCRIPTOR, DELETE_NODE_DESCRIPTOR };
        }

        @Override
        public GraphQueue createQueue(QueueCollection qc) {
            return ((Graph) qc.getGraph()).createQueue(qc, this);
        }
    }

    public static final ConnectDescriptor CONNECT_DESCRIPTOR = new ConnectDescriptor();

    public static final class ConnectDescriptor extends QueueDescriptor<GraphQueue> {

        @Override
        protected QueueDescriptor[] queuesToProcessBefore() {
            return new QueueDescriptor[] { ADD_NODE_DESCRIPTOR };
        }

        @Override
        protected QueueDescriptor[] queuesToProcessAfter() {
            return new QueueDescriptor[] { ADD_EDGE_DESCRIPTOR, ADD_UNDIRECTED_EDGE_DESCRIPTOR, DELETE_EDGE_DESCRIPTOR, DELETE_NODE_DESCRIPTOR };
        }

        @Override
        public GraphQueue createQueue(QueueCollection qc) {
            return ((Graph) qc.getGraph()).createQueue(qc, this);
        }
    }

    public static final AddEdgeDescriptor ADD_EDGE_DESCRIPTOR = new AddEdgeDescriptor();

    public static final class AddEdgeDescriptor extends QueueDescriptor<GraphQueue> {

        @Override
        protected QueueDescriptor[] queuesToProcessBefore() {
            return new QueueDescriptor[] { ADD_NODE_DESCRIPTOR, CONNECT_DESCRIPTOR };
        }

        @Override
        protected QueueDescriptor[] queuesToProcessAfter() {
            return new QueueDescriptor[] { ADD_UNDIRECTED_EDGE_DESCRIPTOR, DELETE_EDGE_DESCRIPTOR, DELETE_NODE_DESCRIPTOR };
        }

        @Override
        public GraphQueue createQueue(QueueCollection qc) {
            return ((Graph) qc.getGraph()).createQueue(qc, this);
        }
    }

    public static final AddUndirectedEdgeDescriptor ADD_UNDIRECTED_EDGE_DESCRIPTOR = new AddUndirectedEdgeDescriptor();

    public static final class AddUndirectedEdgeDescriptor extends QueueDescriptor<GraphQueue> {

        @Override
        protected QueueDescriptor[] queuesToProcessBefore() {
            return new QueueDescriptor[] { ADD_NODE_DESCRIPTOR, CONNECT_DESCRIPTOR, ADD_EDGE_DESCRIPTOR };
        }

        @Override
        protected QueueDescriptor[] queuesToProcessAfter() {
            return new QueueDescriptor[] { DELETE_EDGE_DESCRIPTOR, DELETE_NODE_DESCRIPTOR };
        }

        @Override
        public GraphQueue createQueue(QueueCollection qc) {
            return ((Graph) qc.getGraph()).createQueue(qc, this);
        }
    }

    public static final DeleteEdgeDescriptor DELETE_EDGE_DESCRIPTOR = new DeleteEdgeDescriptor();

    public static final class DeleteEdgeDescriptor extends QueueDescriptor<GraphQueue> {

        @Override
        protected QueueDescriptor[] queuesToProcessBefore() {
            return new QueueDescriptor[] { ADD_NODE_DESCRIPTOR, CONNECT_DESCRIPTOR, ADD_EDGE_DESCRIPTOR, ADD_UNDIRECTED_EDGE_DESCRIPTOR };
        }

        @Override
        protected QueueDescriptor[] queuesToProcessAfter() {
            return new QueueDescriptor[] { DELETE_NODE_DESCRIPTOR };
        }

        @Override
        public GraphQueue createQueue(QueueCollection qc) {
            return ((Graph) qc.getGraph()).createQueue(qc, this);
        }
    }

    public static final DeleteNodeDescriptor DELETE_NODE_DESCRIPTOR = new DeleteNodeDescriptor();

    public static final class DeleteNodeDescriptor extends QueueDescriptor<GraphQueue> {

        @Override
        protected QueueDescriptor[] queuesToProcessBefore() {
            return new QueueDescriptor[] { ADD_NODE_DESCRIPTOR, CONNECT_DESCRIPTOR, ADD_EDGE_DESCRIPTOR, ADD_UNDIRECTED_EDGE_DESCRIPTOR, DELETE_EDGE_DESCRIPTOR };
        }

        @Override
        protected QueueDescriptor[] queuesToProcessAfter() {
            return new QueueDescriptor[] { EXECUTE_DESCRIPTOR };
        }

        @Override
        public GraphQueue createQueue(QueueCollection qc) {
            return ((Graph) qc.getGraph()).createQueue(qc, this);
        }
    }

    public static final ExecuteDescriptor EXECUTE_DESCRIPTOR = new ExecuteDescriptor();

    public static final class ExecuteDescriptor extends QueueDescriptor<GraphQueue> {

        @Override
        protected QueueDescriptor[] queuesToProcessBefore() {
            return new QueueDescriptor[] { DELETE_NODE_DESCRIPTOR };
        }

        @Override
        protected QueueDescriptor[] queuesToProcessAfter() {
            return DESCRIPTOR_0;
        }

        @Override
        public GraphQueue createQueue(QueueCollection qc) {
            return ((Graph) qc.getGraph()).createQueue(qc, this);
        }
    }

    public static final QueueDescriptor FIRST_QUEUE = ADD_NODE_DESCRIPTOR;

    public static final QueueDescriptor LAST_QUEUE = EXECUTE_DESCRIPTOR;

    private final QueueDescriptor<?> descriptor;

    public GraphQueue(QueueDescriptor<?> descr, boolean usesObjectQueue, boolean createBackLinks) {
        super(usesObjectQueue, createBackLinks);
        descriptor = descr;
    }

    public QueueDescriptor<?> getDescriptor() {
        return descriptor;
    }

    protected class Processor extends Reader {

        protected boolean execute = true;

        protected boolean checkApplicability = false;

        protected boolean markApplicability = false;

        protected boolean clearSegment = false;

        protected Processor(TypeLoader loader) {
            super(loader);
        }

        protected boolean process(int item) throws IOException, RuntimeModelException {
            switch(item & ITEM_MASK) {
                case BEGIN_LEVEL:
                    enter(false);
                    boolean b = process(null);
                    leave();
                    return b;
                default:
                    throw new IOException();
            }
        }

        public boolean process(int[] segs) throws IOException, RuntimeModelException {
            boolean modified = false;
            if (segs == null) {
                int item = readItem();
                while (item >= 0) {
                    modified |= process(item);
                    item = next();
                }
            } else {
                try {
                    segmentLoop: for (int i = 0; i < segs.length; i++) {
                        int s = segs[i];
                        if (s >= 0) {
                            Cursor n = segments.get(s + 1);
                            for (int j = clearInapplicable ? 1 : 0; j >= 0; j--) {
                                execute = !clearInapplicable;
                                checkApplicability = j == 1;
                                markApplicability = clearInapplicable && (j == 0);
                                int item = moveTo(segments.get(s));
                                while ((item >= 0) && !isAt(n)) {
                                    clearSegment = false;
                                    modified |= process(item);
                                    if (clearSegment) {
                                        segs[i] = -1;
                                        continue segmentLoop;
                                    }
                                    item = next();
                                }
                            }
                        }
                    }
                } finally {
                    execute = true;
                    checkApplicability = false;
                    markApplicability = false;
                }
            }
            return modified;
        }
    }

    protected abstract Processor createProcessor();

    private Processor processor;

    public boolean process(int[] segments) throws RuntimeModelException {
        if (processor == null) {
            processor = createProcessor();
        }
        processor.resetCursor();
        try {
            return processor.process(segments);
        } catch (IOException e) {
            throw new RuntimeModelException(e, getModel());
        }
    }

    transient boolean clearInapplicable = false;

    protected void clearSegmentsToExcludeImpl(int[] segs) {
        try {
            clearInapplicable = true;
            process(segs);
        } catch (RuntimeModelException e) {
            e.printStackTrace();
        } finally {
            clearInapplicable = false;
        }
    }

    private ObjectList<Cursor> segments = new ObjectList<Cursor>();

    private int segmentsSize = 0;

    public void markSegment(int n) {
        while (segmentsSize <= n) {
            segments.set(segmentsSize, getCursor(segments.get(segmentsSize)));
            segmentsSize++;
        }
    }

    public void resetToSegment(int n) {
        moveTo(segments.get(n));
        segmentsSize = n + 1;
    }

    @Override
    public void clear() {
        super.clear();
        segmentsSize = 0;
    }

    protected abstract RuntimeModel getModel();

    public abstract void addNode(Object node);

    public abstract void addEdgeBits(Object source, Object target, int edges);

    public abstract void addUndirectedEdgeBits(Object source, Object target, int edges);

    public abstract void deleteEdgeBits(Object source, Object target, int edges);

    public abstract void deleteNode(Object node);

    public abstract void deleteCurrentEdges(Object node, int edges, boolean outgoing);

    public abstract void moveIncoming(Object source, Object target, int edges);

    public abstract void copyIncoming(Object source, Object target, int edges);

    public abstract void copyIncoming(Object source, Object target, int edges, int copyMask, int addMask);

    public abstract void moveOutgoing(Object source, Object target, int edges);

    public abstract void copyOutgoing(Object source, Object target, int edges);

    public abstract void copyOutgoing(Object source, Object target, int edges, int copyMask, int addMask);

    public abstract <N> void connectIncoming(N source, N target, Operator<N> operator);

    public abstract <N> void connectOutgoing(N source, N target, Operator<N> operator);

    public abstract <N, P> void connect(N source, N target, P param, Connector<N, P> redirector);

    public abstract void connectAdjacent(Object start, Object end);

    public abstract void embedInterpretive(Object interpreted, Object start, Object end);
}
