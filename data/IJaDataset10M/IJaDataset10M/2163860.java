package org.progeeks.graph.proc;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import org.apache.commons.collections.Predicate;
import com.phoenixst.collections.*;
import com.phoenixst.plexus.*;
import org.progeeks.util.*;
import org.progeeks.util.log.*;
import org.progeeks.graph.*;

/**
 *  A thread pool style process manager that tracks its
 *  worker and job state in a graph.
 *
 *  @version   $Revision: 4273 $
 *  @author    Paul Speed
 */
public class ProcessManager<E> {

    static Log log = Log.getLog();

    private static final Predicate DEFAULT_FILTER = new NotPredicate(new InstanceofPredicate(ProcessState.class));

    private static final int DEFAULT_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    private String name;

    private AtomicGraph graph;

    private Predicate jobFilter = DEFAULT_FILTER;

    private boolean watchEdges = false;

    private boolean initialized = false;

    private BlockingQueue<E> jobs;

    private Set<E> processing = new CopyOnWriteArraySet<E>();

    private GraphObserver graphObserver = new GraphObserver();

    private List<WorkerThread> workers = new CopyOnWriteArrayList<WorkerThread>();

    private ObjectConfigurator<Worker<E>> workerFactory;

    private int idCounter = 0;

    private int poolSize = DEFAULT_POOL_SIZE;

    private Predicate userTypePred = new EqualPredicate(ProcessAttributeType.STATE);

    private Predicate headTypePred = new InstanceofPredicate(ProcessState.class);

    public ProcessManager() {
    }

    public ProcessManager(String name, AtomicGraph graph) {
        setName(name);
        setGraph(graph);
    }

    public void initialize() {
        if (initialized) return;
        log.trace("Initializing:" + getName() + "  Pool size:" + poolSize);
        jobs = new ArrayBlockingQueue<E>(poolSize);
        for (int i = 0; i < poolSize; i++) addWorker();
        graph.addGraphListener(graphObserver);
        for (ProcessState s : ProcessState.values()) graph.addNode(s);
        primeHoldingQueue(true);
        initialized = true;
    }

    /**
     *  Terminates the process manager by halting the processing
     *  of new items and waiting for all active items to finish.
     *  This will not abort existing processing.
     */
    public void terminate() throws InterruptedException {
        if (!initialized) return;
        initialized = false;
        log.trace("Terminating:" + getName());
        if (this.graph != null) {
            this.graph.removeGraphListener(graphObserver);
        }
        for (WorkerThread w : workers) {
            w.terminate();
        }
        workers.clear();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPoolSize(int size) {
        if (initialized) throw new IllegalStateException("Cannot reset pool size after init.");
        this.poolSize = size;
    }

    public int getPoolSize() {
        return poolSize;
    }

    /**
     *  Set to true if the job objects are actually graph edges
     *  and not nodes.  The priming is done a little differently
     *  in this case but all other processing is the same except
     *  when to apply the filters.
     *  Defaults to false and uses the jobFilter as a node filter.
     */
    public void setWatchEdges(boolean b) {
        this.watchEdges = b;
    }

    public boolean getWatchEdges() {
        return watchEdges;
    }

    public void setWorkerFactory(ObjectConfigurator<Worker<E>> factory) {
        this.workerFactory = factory;
    }

    public ObjectConfigurator<Worker<E>> getWorkerFactory() {
        return workerFactory;
    }

    /**
     *  Sets the graph that will be used to identify new
     *  jobs and manage the job and worker state edges.
     */
    public void setGraph(AtomicGraph g) {
        this.graph = g;
    }

    public AtomicGraph getGraph() {
        return graph;
    }

    /**
     *  Sets the filter that will be used to identify
     *  jobs in the graph.
     */
    public void setJobFilter(Predicate p) {
        this.jobFilter = p;
    }

    public Predicate getJobFilter() {
        return jobFilter;
    }

    protected boolean needMoreJobs() {
        return jobs.size() < workers.size();
    }

    protected synchronized void primeHoldingQueue(boolean checkForNewItems) {
        if (log.isTraceEnabled()) log.trace("primeHoldingQueue(" + checkForNewItems + ")");
        if (!needMoreJobs()) return;
        Predicate tp = TraverserPredicateFactory.createPredicated(userTypePred, jobFilter, GraphUtils.DIRECTED_IN_MASK);
        for (Iterator i = graph.traverser(ProcessState.PENDING, tp); i.hasNext(); ) {
            E item = (E) i.next();
            if (log.isTraceEnabled()) log.trace("Priming item:" + item);
            if (!prepareItem(item)) {
                if (i instanceof CloseableIterator) {
                    log.trace("closing iterator.");
                    ((CloseableIterator) i).close();
                }
                break;
            }
        }
        if (!checkForNewItems) return;
        Predicate edgePred = EdgePredicateFactory.createPredicated(userTypePred, jobFilter, headTypePred, GraphUtils.DIRECTED_OUT_MASK);
        Collection stateEdges = graph.edges(edgePred);
        Collection newJobs;
        if (watchEdges) newJobs = new ArrayList(graph.edges(jobFilter)); else newJobs = new ArrayList(graph.nodes(jobFilter));
        if (log.isDebugEnabled()) log.debug("Existing state edges:" + stateEdges.size() + " number of jobs:" + newJobs.size());
        if (stateEdges.size() != newJobs.size()) {
            Predicate travPred = TraverserPredicateFactory.createPredicated(userTypePred, headTypePred, GraphUtils.DIRECTED_OUT_MASK);
            for (Iterator i = newJobs.iterator(); i.hasNext(); ) {
                Object j = i.next();
                if (!graph.containsNode(j) || graph.degree(j, travPred) == 0) {
                    itemAdded((E) j);
                }
            }
        }
    }

    /**
     *  Adds the item to the temporary holding queue and
     *  creates more workers as needed.  Returns false if
     *  the item was not queued because there are already
     *  enough primed items waiting.
     */
    protected boolean prepareItem(E item) {
        if (log.isTraceEnabled()) log.trace("prepareItem(" + item + ")");
        if (jobs.contains(item)) {
            log.trace("Alreading queued.");
            return true;
        }
        if (processing.contains(item)) {
            log.trace("Alreading processing.");
            return true;
        }
        if (!jobs.offer(item)) {
            log.trace("Queue has enough items.");
            return false;
        }
        if (log.isTraceEnabled()) log.trace("Queued:" + item);
        return true;
    }

    protected void addWorker() {
        WorkerThread w = new WorkerThread();
        w.start();
        workers.add(w);
        w.setName(getName() + ":worker[" + (++idCounter) + "]");
        log.trace("Started worker:" + w);
    }

    /**
     *  Called by the worker thread when it is done with an
     *  item and about ready to grab another.
     */
    protected void workerDone(WorkerThread w) {
        primeHoldingQueue(false);
    }

    protected void itemAdded(E item) {
        if (watchEdges) {
            if (log.isTraceEnabled()) log.trace("adding edge node:" + item);
            graph.addNode(item);
        }
        StateUtils.setState(graph, item, ProcessState.PENDING);
        primeHoldingQueue(false);
    }

    protected void itemAborting(E item) {
        if (log.isTraceEnabled()) log.trace("itemAborting:" + item);
        for (WorkerThread w : workers) w.abortItem(item);
    }

    protected void itemRemoved(E item) {
        if (log.isTraceEnabled()) log.trace("itemRemoved:" + item);
    }

    protected class WorkerThread extends Thread {

        private boolean go = true;

        private Worker<E> worker;

        private E currentItem;

        public WorkerThread() {
            worker = getWorkerFactory().createObject();
        }

        /**
         *  If the specified item is the same as the item that this
         *  thread is processing then it will be aborted.
         */
        public void abortItem(E item) {
            if (ObjectUtils.areEqual(item, currentItem)) worker.abort();
        }

        public void terminate() throws InterruptedException {
            log.trace("WorkerThread.terminate():" + getName());
            go = false;
            if (currentItem == null) interrupt();
            log.debug("Waiting for worker:" + getName() + " to shutdown.");
            join();
        }

        protected void processItem(E item) {
            if (log.isTraceEnabled()) log.trace(getName() + " -> processItem(" + item + ")");
            if (!processing.add(item)) {
                if (log.isTraceEnabled()) log.trace(getName() + " item is being processed by another worker:" + item);
                return;
            }
            this.currentItem = item;
            ProcessState resultState = null;
            try {
                worker.prepare(item);
                if (!StateUtils.moveState(graph, item, ProcessState.PENDING, ProcessState.PROCESSING)) {
                    if (log.isTraceEnabled()) log.trace(getName() + " item is not pending anymore:" + item);
                    return;
                }
                if (!worker.execute(item)) {
                    if (log.isTraceEnabled()) log.trace(getName() + " aborted:" + item);
                    if (!StateUtils.moveState(graph, item, ProcessState.ABORT_SIGNALED, ProcessState.ABORTED)) throw new RuntimeException("Incosistent aborting state for item:" + item); else resultState = ProcessState.ABORTED;
                } else {
                    if (log.isTraceEnabled()) log.trace(getName() + " completed:" + item);
                    if (!StateUtils.moveState(graph, item, ProcessState.PROCESSING, ProcessState.COMPLETED)) throw new RuntimeException("Incosistent processing state for item:" + item); else resultState = ProcessState.COMPLETED;
                }
            } catch (Throwable t) {
                if (log.isDebugEnabled()) log.debug(getName() + " errored:" + item, t);
                worker.error(currentItem, t);
                boolean itemExists = false;
                try {
                    ProcessState p = StateUtils.getState(graph, item);
                    if (p != null) itemExists = true;
                } catch (Exception e) {
                }
                if (itemExists) {
                    StateUtils.setState(graph, item, ProcessState.ERRORED);
                    resultState = ProcessState.ERRORED;
                }
                if (t instanceof Error) throw (Error) t;
            } finally {
                if (log.isTraceEnabled()) log.trace("Worker:" + getName() + " done.");
                workerDone(this);
                processing.remove(currentItem);
                worker.done(currentItem, resultState);
                currentItem = null;
            }
        }

        public void run() {
            worker.initialize(ProcessManager.this);
            try {
                while (go) {
                    E item = null;
                    try {
                        item = jobs.take();
                        processItem(item);
                    } catch (InterruptedException e) {
                        if (go) throw new RuntimeException("Unexpected thread interruption", e);
                        log.trace("Shutting down from interrupt:" + getName());
                    }
                }
            } finally {
                log.trace("Thread exiting:" + getName());
                worker.terminate();
            }
        }
    }

    protected class GraphObserver implements GraphListener {

        public void nodeAdded(GraphEvent e) {
            Object o = e.getObject();
            if (log.isTraceEnabled()) log.trace("Node added:" + o);
            if (!watchEdges && jobFilter != null && jobFilter.evaluate(o)) itemAdded((E) o);
        }

        public void nodeRemoved(GraphEvent e) {
            Object o = e.getObject();
            if (log.isTraceEnabled()) log.trace("Node removed:" + o);
            if (!watchEdges && jobFilter != null && jobFilter.evaluate(o)) itemRemoved((E) o);
        }

        public void edgeAdded(GraphEvent e) {
            Object o = e.getObject();
            Graph.Edge edge = (Graph.Edge) o;
            if (log.isTraceEnabled()) {
                log.trace("Edge added:" + o);
                log.trace("matches filter:" + jobFilter.evaluate(o) + "  filter:" + jobFilter);
            }
            if (ProcessAttributeType.STATE == edge.getUserObject() && ProcessState.ABORT_SIGNALED == edge.getHead()) {
                itemAborting((E) edge.getTail());
            } else if (ProcessAttributeType.STATE == edge.getUserObject() && ProcessState.PENDING == edge.getHead()) {
                primeHoldingQueue(false);
            } else if (watchEdges && jobFilter != null && jobFilter.evaluate(o)) {
                itemAdded((E) edge);
            }
        }

        public void edgeRemoved(GraphEvent e) {
            Object o = e.getObject();
            if (log.isTraceEnabled()) log.trace("Edge removed:" + o);
            if (watchEdges && jobFilter != null && jobFilter.evaluate(o)) {
                itemRemoved((E) o);
            }
        }
    }
}
