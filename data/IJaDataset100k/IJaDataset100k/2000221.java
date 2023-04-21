package ch.olsen.routes.cell.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import ch.olsen.products.util.executor.ThreadPool;
import ch.olsen.products.util.executor.ThreadPool.AsyncResultHandler;
import ch.olsen.products.util.logging.Logger;
import ch.olsen.routes.atom.Atom;
import ch.olsen.routes.atom.AtomInput;
import ch.olsen.routes.atom.AtomOutput;
import ch.olsen.routes.atom.LinkInput;
import ch.olsen.routes.atom.RoutesStep;
import ch.olsen.routes.cell.Cell;
import ch.olsen.routes.cell.service.gwt.client.DebugTrace;
import ch.olsen.routes.data.DataElement;

/**
 * The Job scheduler accepts new jobs with time constraints
 * (https://corp.olsen.ch/twiki/bin/view/Routes/OlsenRoutesTaskScheduling)
 * and executes them optionally having some QoS functionality
 * 
 * The scheduler uses a controlled number of threads, and binds threads
 * to their status. So at any job start, one or more threads need to be bound 
 * to it.
 * @author vito
 *
 */
public class JobScheduler implements Serializable {

    private static final long serialVersionUID = 1L;

    ThreadPool threadPool = new ThreadPool(5);

    SeqFrame root;

    Map<Integer, DebugTraceFlag> suspendedTasks;

    int frameCounter = 0;

    /**
	 * used to sync use of threads
	 */
    ReadWriteLock jobsLock = new ReentrantReadWriteLock();

    Condition jobsCondition = jobsLock.writeLock().newCondition();

    /**
	 * used to sync jobs insertion/removal
	 */
    Lock queueLock = new ReentrantLock();

    Map<Thread, Task> activeJobs = new HashMap<Thread, Task>();

    ResultHandler resultHandler = new ResultHandler();

    Logger log;

    boolean kill = false;

    public JobScheduler(Logger log) {
        this.log = log;
        root = new SeqFrame(null, this);
        suspendedTasks = new HashMap<Integer, DebugTraceFlag>();
    }

    public JobScheduler(Logger log, Cell cell, JobSchedulerPersistence schedPersist) {
        this.log = log;
        this.root = schedPersist.root;
        this.suspendedTasks = schedPersist.suspendedTasks;
        this.frameCounter = schedPersist.frameCounter;
        root.outer = this;
        rebind(root.children, cell, this);
        List<Task> toRun = resumeJobs(root, this);
        schedule(toRun);
    }

    private List<Task> resumeJobs(SeqFrame frame, JobScheduler outer) {
        List<Task> toRun = new LinkedList<Task>();
        for (Task t : frame.children) {
            if (t.link != null && t.data != null && !t.suspended && !t.done) {
                toRun.add(t);
            }
            if (t.children.size() > 0) {
                toRun.addAll(resumeJobs(t.children.iterator().next(), outer));
            }
        }
        return toRun;
    }

    /**
	 * start a new job by activating a single link. Any future job scheduling 
	 * will be associated to this job by lookign at the thread
	 */
    public void initJob(LinkInput ai, DataElement de) {
        Task t;
        queueLock.lock();
        try {
            t = root.add(ai, de);
        } finally {
            queueLock.unlock();
        }
        if (log.isDebug()) log.debug("Initing " + t);
        schedule(t);
    }

    /**
	 * this is called after a job has been initialized, when a task produces
	 * children
	 * @param tasks
	 * @throws JobSchedulerException
	 */
    public void scheduleJob(List<RoutesStep> tasks) throws JobSchedulerException {
        jobsLock.readLock().lock();
        Task je = activeJobs.get(Thread.currentThread());
        jobsLock.readLock().unlock();
        if (je == null) throw new JobSchedulerException("Invalid thread access");
        boolean first = true;
        int time = 0;
        List<RoutesStep> now = new LinkedList<RoutesStep>();
        List<Task> runNow = new LinkedList<Task>();
        queueLock.lock();
        try {
            for (RoutesStep t : tasks) {
                if (t.getTime() > time) {
                    if (now.size() > 0) {
                        SeqFrame sf = je.add();
                        for (RoutesStep t2 : now) {
                            Task j = sf.add(t2.getLink(), t2.getData());
                            if (first) {
                                if (log.isDebug()) log.debug("Scheduling " + j);
                                runNow.add(j);
                            } else {
                                if (log.isDebug()) log.debug("Deferring " + j);
                            }
                        }
                        first = false;
                    }
                    time = t.getTime();
                    now.clear();
                }
                now.add(t);
            }
            if (now.size() > 0) {
                SeqFrame sf = je.add();
                for (RoutesStep t2 : now) {
                    Task j = sf.add(t2.getLink(), t2.getData());
                    if (first) {
                        if (log.isDebug()) log.debug("Scheduling " + j);
                        runNow.add(j);
                    } else {
                        if (log.isDebug()) log.debug("Deferring " + j);
                    }
                }
            }
        } finally {
            queueLock.unlock();
        }
        if (runNow.size() > 0) schedule(runNow);
    }

    private void schedule(List<Task> runNow) {
        if (kill) return;
        List<Task> copy = new LinkedList<Task>();
        copy.addAll(runNow);
        Iterator<Task> it = copy.iterator();
        while (it.hasNext()) {
            if (it.next().link == null) it.remove();
        }
        threadPool.executeJobsAsync(copy, resultHandler);
    }

    private class ResultHandler implements AsyncResultHandler {

        public void handleCompletion() {
        }

        public void handleErrors(Collection<Exception> exceptions) {
            for (Exception e : exceptions) {
                log.warn("Error in job: " + e.getMessage(), e);
            }
        }
    }

    private void schedule(Task t) {
        if (kill) return;
        List<Task> runNow = new LinkedList<Task>();
        runNow.add(t);
        threadPool.executeJobsAsync(runNow, null);
    }

    private interface Parent {

        List<? extends Parent> getChildren();

        void setOuter(JobScheduler outer);
    }

    /**
	 * A SeqFrame represents a single time unit. Two different SeqFrames
	 * are executed sequentially, while all the elements in the same SeqFrame
	 * are executed in parallel
	 * 
	 * @author vito
	 *
	 */
    private static class SeqFrame implements Serializable, Parent {

        private static final long serialVersionUID = 1L;

        Task parent;

        List<Task> children = new ArrayList<Task>();

        int id;

        transient JobScheduler outer;

        /**
		 * This method assumes the lock has already been acquired
		 */
        public SeqFrame(Task parent, JobScheduler outer) {
            this.parent = parent;
            id = outer.frameCounter++;
            this.outer = outer;
        }

        /**
		 * This method assumes the lock has already been acquired
		 */
        public Task add(LinkInput link, DataElement data) {
            Task ret = new Task(this, link, data, outer);
            children.add(ret);
            return ret;
        }

        /**
		 * this method assumes the lock has been acquired already
		 * @param task
		 */
        public void remove(Task task) {
            if (outer.log.isDebug()) outer.log.debug("Removing " + task);
            if (!children.remove(task)) outer.log.warn("Unable to remove child " + task + " from " + this);
            if (children.size() == 0 && parent != null) {
                parent.remove(this);
                parent = null;
            }
        }

        public String toString() {
            String ret = "SeqFrame: " + id;
            if (parent != null) ret += " <- " + parent.toString();
            return ret;
        }

        public List<? extends Parent> getChildren() {
            return children;
        }

        public void setOuter(JobScheduler outer) {
            this.outer = outer;
        }
    }

    private static class Task implements Serializable, Runnable, Parent {

        private static final long serialVersionUID = 1L;

        SeqFrame parent;

        transient LinkInput link;

        String linkLookup;

        DataElement data;

        List<SeqFrame> children = new ArrayList<SeqFrame>();

        int id;

        boolean suspended = false;

        boolean done = false;

        transient JobScheduler outer;

        /**
		 * This method assumes the lock has already been acquired
		 */
        public Task(SeqFrame parent, LinkInput link, DataElement data, JobScheduler outer) {
            this.parent = parent;
            if (link == null) System.out.print("");
            this.link = link;
            this.data = data;
            synchronized (outer) {
                id = outer.frameCounter++;
            }
            linkLookup = link.getLookup();
            this.outer = outer;
        }

        /**
		 * This method assumes the lock has already been acquired
		 */
        public void remove(SeqFrame seqFrame) {
            if (outer.log.isDebug()) outer.log.debug("Removing " + seqFrame);
            if (!children.remove(seqFrame)) outer.log.warn("Unable to remove child " + seqFrame + " from " + this);
            if (children.size() > 0) {
                SeqFrame next = children.get(0);
                if (outer.log.isDebug()) {
                    for (Task t : next.children) outer.log.debug("Scheduling " + t);
                }
                compress();
                outer.schedule(next.children);
            } else if (children.size() == 0 && parent != null && done) {
                parent.remove(this);
                parent = null;
            }
        }

        /**
		 * This method assumes the lock has already been acquired
		 */
        public SeqFrame add() {
            SeqFrame ret = new SeqFrame(this, outer);
            children.add(ret);
            return ret;
        }

        public void run() {
            final boolean isResuming = suspended;
            if (suspended) {
                suspended = false;
                if (outer.log.isDebug()) outer.log.debug("Resuming " + this);
            } else {
                if (outer.log.isDebug()) outer.log.debug("Executing " + this);
            }
            outer.jobsLock.writeLock().lock();
            outer.activeJobs.put(Thread.currentThread(), this);
            outer.jobsLock.writeLock().unlock();
            try {
                link.receive(data, isResuming);
                if (outer.log.isDebug()) outer.log.debug("Done " + this);
            } catch (Exception e) {
                outer.log.warn("Exception should have already been catched: " + e.getMessage(), e);
            }
            outer.jobsLock.writeLock().lock();
            outer.activeJobs.remove(Thread.currentThread());
            outer.jobsCondition.signalAll();
            outer.jobsLock.writeLock().unlock();
            outer.queueLock.lock();
            if (!suspended) done = true;
            try {
                if (!suspended) {
                    link = null;
                    data = null;
                    if (children.size() == 0) {
                        if (parent != null) {
                            parent.remove(this);
                            parent = null;
                        }
                    } else compress();
                }
            } finally {
                outer.queueLock.unlock();
            }
        }

        /**
		 * if a Task contains only one SeqFrame as child, the elements
		 * of the child SeqFrame can be inserted in place of the Task,
		 * and the current task and its only child can be removed from the
		 * graph
		 * 
		 * 
		 * This method assumes the lock has already been acquired
		 */
        public void compress() {
            if (parent == null) return;
            if (children.size() == 1) {
                if (outer.log.isDebug()) outer.log.debug("Compressing task " + this);
                boolean removed = parent.children.remove(this);
                if (removed) {
                    int n = 0;
                    for (Task t : children.iterator().next().children) {
                        t.parent = parent;
                        parent.children.add(t);
                        n++;
                    }
                    children.clear();
                    id = -1;
                    parent = null;
                } else outer.log.warn("Unable to remove task from parent during compress");
            }
        }

        public String toString() {
            String ret = "Task: " + id + " link: " + linkLookup;
            if (parent != null) ret += " <- " + parent.toString();
            return ret;
        }

        public List<? extends Parent> getChildren() {
            return children;
        }

        public void setOuter(JobScheduler outer) {
            this.outer = outer;
        }
    }

    public static class JobSchedulerException extends Exception {

        private static final long serialVersionUID = 1L;

        public JobSchedulerException(String message) {
            super(message);
        }
    }

    private static class DebugTraceFlag implements Serializable {

        private static final long serialVersionUID = 1L;

        private static int idCounter = 0;

        Task task;

        DebugTrace trace;

        public DebugTraceFlag(String lookup, String message, Task task) {
            int id = idCounter++;
            trace = new DebugTrace(lookup, message, id);
            this.task = task;
        }
    }

    public void suspendCurrent(String lookup, String message) throws JobSchedulerException {
        jobsLock.readLock().lock();
        Task je = activeJobs.get(Thread.currentThread());
        jobsLock.readLock().unlock();
        if (je == null) throw new JobSchedulerException("Invalid thread access");
        je.suspended = true;
        DebugTraceFlag flag = new DebugTraceFlag(lookup, message, je);
        synchronized (suspendedTasks) {
            suspendedTasks.put(flag.trace.id, flag);
        }
        if (log.isDebug()) log.debug("Suspended " + this);
    }

    public void resumeAll() {
        List<Task> toRun = new LinkedList<Task>();
        synchronized (suspendedTasks) {
            for (DebugTraceFlag t : suspendedTasks.values()) {
                toRun.add(t.task);
            }
            suspendedTasks.clear();
        }
        schedule(toRun);
    }

    public boolean resume(String lookup) {
        List<Task> toRun = new LinkedList<Task>();
        synchronized (suspendedTasks) {
            Iterator<DebugTraceFlag> it = suspendedTasks.values().iterator();
            while (it.hasNext()) {
                DebugTraceFlag t = it.next();
                if (t.trace.lookup.equals(lookup)) {
                    toRun.add(t.task);
                    it.remove();
                }
            }
        }
        schedule(toRun);
        return toRun.size() > 0;
    }

    public void resume(int debugTraceId) {
        DebugTraceFlag flag;
        synchronized (suspendedTasks) {
            flag = suspendedTasks.remove(debugTraceId);
        }
        if (flag == null) {
            log.warn("Nothing to resume for flag id: " + debugTraceId);
            return;
        }
        schedule(flag.task);
    }

    public DebugTrace[] getDebugTraces() {
        synchronized (suspendedTasks) {
            DebugTrace[] ret = new DebugTrace[suspendedTasks.size()];
            int n = 0;
            for (DebugTraceFlag f : suspendedTasks.values()) {
                ret[n++] = f.trace;
            }
            return ret;
        }
    }

    /**
	 * When passivate is called, all running threads are finished but no new 
	 * task will be executed. When all running threads are done, we export
	 * a data structure which will be later used to resume execution
	 * (look at the second constructor)
	 * @return an object to store for later restore
	 */
    public JobSchedulerPersistence passivate() {
        kill = true;
        jobsLock.writeLock().lock();
        while (activeJobs.size() > 0) {
            try {
                jobsCondition.await();
            } catch (InterruptedException e) {
            }
        }
        jobsLock.writeLock().unlock();
        JobSchedulerPersistence ret = new JobSchedulerPersistence();
        ret.root = root;
        ret.suspendedTasks = suspendedTasks;
        ret.frameCounter = frameCounter;
        return ret;
    }

    /**
	 * rebind all link inputs starting from their lookup
	 * @param children
	 * @param cell
	 */
    private void rebind(List<? extends Parent> children, Cell cell, JobScheduler outer) {
        for (Parent p : children) {
            p.setOuter(outer);
            if (p instanceof Task) {
                Task t = (Task) p;
                if (!t.done) {
                    try {
                        String split[] = t.linkLookup.split("#");
                        String atoms[] = split[0].split("/");
                        Atom a = cell;
                        for (int n = 1; n < atoms.length; n++) {
                            a = ((Cell) a).getAtom(atoms[n]);
                        }
                        if (split.length == 3) {
                            AtomOutput out = a.getOutput(split[1]);
                            for (LinkInput l : out.getLinks()) {
                                if (l.getName().equals(split[2])) t.link = l;
                            }
                            if (t.link == null) throw new RuntimeException("Cound not find link");
                        } else {
                            AtomInput in = a.getInput(split[1]);
                            t.link = in;
                        }
                    } catch (Exception e) {
                        log.warn("Could not rebind link " + t.linkLookup + " for resume: " + e.getMessage(), e);
                        t.done = true;
                        if (t.children.size() == 0 && t.parent != null) {
                            t.parent.remove(t);
                        }
                    }
                }
            }
            rebind(p.getChildren(), cell, outer);
        }
    }

    public static class JobSchedulerPersistence implements Serializable {

        private static final long serialVersionUID = 1L;

        SeqFrame root;

        Map<Integer, DebugTraceFlag> suspendedTasks;

        int frameCounter;
    }
}
