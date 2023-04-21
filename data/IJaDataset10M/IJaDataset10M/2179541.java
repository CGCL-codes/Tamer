package drcl.sim.event;

import java.util.*;
import java.beans.*;
import drcl.comp.*;
import drcl.util.queue.*;

/**
 * This is the previous version of the sequential event simulation engine.
 * Refer to {@link SESimulator} for the complete implementation.
 */
public class SESimulatorOld extends drcl.comp.ACARuntime {

    SEThreadOld mainThread;

    protected long startTime = 0;

    protected long ltime = 0;

    protected double time = 0.0;

    boolean suspended = false;

    /** Used to store tasks. */
    protected FIFOQueue qReady = new FIFOQueue();

    protected TreeMapQueue qWaiting = new TreeMapQueue();

    int threadcount = 0;

    int maxlength = 0;

    long totalNumEvents = 0;

    static int RUNTIME_COUNTER = 0;

    public SESimulatorOld() {
        this("SESimOld");
    }

    public SESimulatorOld(String name_) {
        super(name_);
        setTimeScale(Double.POSITIVE_INFINITY);
    }

    void ___TASK_MANAGEMENT___() {
    }

    /**
   * The only way to trigger new tasks to be executed.
   */
    protected synchronized void newTask(Task task_, WorkerThread currentThread_) {
        if (resetting || task_ == null) return;
        if (task_.getTime() > time) {
            if (debug) println(Thread.currentThread().getName() + " to waiting-queue:" + task_);
            qWaiting.enqueue(task_.getTime(), task_);
            if (logenabled) {
                if (tf == null) _openlog();
                try {
                    tf.write(("+ " + task_.getTime() + "\t\t" + qWaiting.getLength() + "\n").toCharArray());
                    tf.flush();
                } catch (Exception e_) {
                }
            }
            if (maxlength < qWaiting.getLength()) maxlength = qWaiting.getLength();
        } else {
            if (debug) println(Thread.currentThread().getName() + " to ready-queue:" + task_);
            qReady.enqueue(task_);
        }
        if (state == State_INACTIVE) startNewThread();
    }

    /** Returns the current thread context. */
    protected WorkerThread getThread() {
        return mainThread;
    }

    synchronized void startNewThread() {
        if (debug) {
            if (Thread.currentThread() instanceof SEThreadOld) System.out.println("startNewThread(): called by " + Thread.currentThread() + ", " + ((SEThreadOld) Thread.currentThread())._currentContext()); else System.out.println("startNewThread(): called by " + Thread.currentThread());
        }
        mainThread = new SEThreadOld("WK" + threadcount);
        mainThread.runtime = mainThread.aruntime = this;
        mainThread.start();
        if (threadcount == 0) {
            startTime = System.currentTimeMillis();
        }
        setState(State_RUNNING);
        threadcount++;
    }

    /** Returns a task to be executed. */
    synchronized Task getTask() {
        boolean jump_ = false;
        if (qReady.isEmpty()) {
            qWaiting.dequeueTransfer(qReady);
            jump_ = true;
        }
        if (qReady.isEmpty()) return null;
        Task t_ = (Task) qReady.dequeue();
        if (jump_) {
            time = t_.getTime();
            if (debug) println(Thread.currentThread().getName() + "from waiting-queue, <TIME_JUMP>:" + t_);
        } else {
            if (debug) println(Thread.currentThread().getName() + "from ready-queue:" + t_);
        }
        return t_;
    }

    private void ___PROPERTIES___() {
    }

    public synchronized String info() {
        return s_info2();
    }

    /** Synchronized version of {@link #s_info()}. */
    public synchronized String ss_info() {
        return s_info();
    }

    String s_info2() {
        long numberOfArrivalEvents_ = getNumberOfArrivalEvents();
        StringBuffer sb_ = new StringBuffer(toString());
        if (state != State_INACTIVE && state != State_SUSPENDED) ltime = System.currentTimeMillis() - startTime;
        sb_.append(" --- State:" + state + "\n");
        sb_.append("# of events:            " + numberOfArrivalEvents_ + "\n");
        sb_.append("Event processing rate:  " + _getEventRate(numberOfArrivalEvents_) + "(#/s)\n");
        if (resetting) sb_.append("Resetting...\n");
        sb_.append("Time:  " + time + "\n");
        sb_.append("Wall time elapsed: ");
        sb_.append(((double) ltime / 1000.0) + " sec.\n");
        return sb_.toString();
    }

    public String s_info() {
        long numberOfArrivalEvents_ = getNumberOfArrivalEvents();
        StringBuffer sb_ = new StringBuffer(toString());
        if (state != State_INACTIVE && state != State_SUSPENDED) ltime = System.currentTimeMillis() - startTime;
        sb_.append(" --- State:" + state);
        if (resetting) sb_.append(", Resetting...");
        sb_.append("\n");
        sb_.append("# of events:   " + numberOfArrivalEvents_ + "\n");
        sb_.append("Event request rate:    " + _getEventRate(numberOfArrivalEvents_) + "(#/s)\n");
        sb_.append("Time:  " + time + "\n");
        sb_.append("Wall time elapsed: ");
        sb_.append(((double) ltime / 1000.0) + " sec.\n");
        sb_.append("#Tasks_queued: " + qReady.getLength() + "\n");
        sb_.append("#Future tasks: " + qWaiting.getLength() + ", maxlength=" + maxlength + "\n");
        sb_.append("#threads created: " + threadcount + "\n");
        sb_.append("Main_thread: " + (mainThread == null ? "null" : mainThread._toString()) + "\n");
        return sb_.toString();
    }

    public SEThreadOld getMainThread() {
        return mainThread;
    }

    /** Asynchronous version of {@link #diag()}. */
    public String a_info(boolean listWaitingTasks_) {
        StringBuffer sb_ = new StringBuffer(s_info());
        if (listWaitingTasks_) {
            sb_.append("Task queue: " + (qReady.isEmpty() ? "empty.\n" : "\n" + qReady.info()));
            sb_.append("Future task queue: " + (qWaiting.isEmpty() ? "empty.\n" : "\n" + qWaiting.info()));
        }
        return sb_.toString();
    }

    /** Returns information of the task queue. */
    public String tasks() {
        return "Task queue: " + (qReady == null || qReady.isEmpty() ? "<empty>.\n" : "\n" + qReady.info());
    }

    /** Forces to reset this runtime. */
    public void forceReset() {
        resetting = false;
        reset();
    }

    public void ___PROFILE___() {
    }

    public long getNumberOfArrivalEvents() {
        if (mainThread == null) return totalNumEvents; else return totalNumEvents + mainThread.totalNumEvents;
    }

    public double getEventRate() {
        return _getEventRate(getNumberOfArrivalEvents());
    }

    protected double _getEventRate(long numArrivals_) {
        if (state == State_SUSPENDED || state == State_INACTIVE) return (double) numArrivals_ / ltime * 1000.0; else return (double) numArrivals_ / (System.currentTimeMillis() - startTime) * 1000.0;
    }

    void ___EXECUTION_CONTROL___() {
    }

    /** Asynchronized version of getTime(), for diagnosis. */
    protected double _getTime() {
        return time;
    }

    protected synchronized void _stop(boolean block_) {
        if (suspended) return;
        ltime = System.currentTimeMillis() - startTime;
        suspended = true;
        if (block_ && mainThread != null) try {
            this.wait();
        } catch (Exception e_) {
            e_.printStackTrace();
            drcl.Debug.fatalError(e_.toString());
        }
        setState(State_SUSPENDED);
    }

    public synchronized void resume() {
        if (!suspended) return;
        suspended = false;
        setState(State_RUNNING);
        startTime = System.currentTimeMillis() - ltime;
        if (mainThread == null) startNewThread(); else this.notify();
    }

    public synchronized void reset() {
        if (resetting) return;
        resetting = true;
        if (mainThread != null) {
            mainThread.runtime = null;
            if (suspended) this.notify();
            try {
                this.wait();
            } catch (Exception e_) {
                e_.printStackTrace();
                drcl.Debug.fatalError(e_.toString());
            }
        }
        setState(State_INACTIVE);
        totalNumEvents = 0;
        startTime = 0;
        ltime = 0;
        time = 0.0;
        qReady.reset();
        qWaiting.reset();
        mainThread = null;
        resetting = suspended = false;
        maxlength = 0;
        threadcount = 0;
    }

    void threadRetired() {
        if (debug) println(mainThread + " RETIRED");
        totalNumEvents += mainThread.totalNumEvents;
        mainThread.totalNumEvents = 0;
        mainThread = null;
        ltime = System.currentTimeMillis() - startTime;
        setState(State_INACTIVE);
    }

    public long getWallTimeElapsed() {
        if (state != State_INACTIVE && !suspended) ltime = System.currentTimeMillis() - startTime;
        return ltime;
    }

    public String t_info() {
        return t_info("");
    }

    protected String t_info(String prefix_) {
        if (state != State_INACTIVE && state != State_SUSPENDED) ltime = System.currentTimeMillis() - startTime;
        StringBuffer sb_ = new StringBuffer();
        sb_.append(prefix_ + "timeScale     = " + (timeScale / 1.0e3) + " (wall time/virtual time)\n");
        sb_.append(prefix_ + "1.0/timeScale = " + (timeScaleReciprocal * 1.0e3) + " (virtual time/wall time)\n");
        sb_.append(prefix_ + "startTime     = " + startTime + "\n");
        sb_.append(prefix_ + "ltime         = " + ltime + "\n");
        sb_.append(prefix_ + "currentTime   = " + time + "\n");
        return sb_.toString();
    }

    protected void setState(String new_) {
        if (state == new_) return;
        if (vStateListener != null) notifyStateListeners(new PropertyChangeEvent(this, "State", state, new_));
        if (debug) println(Thread.currentThread().getName() + " " + state + " --> " + new_);
        state = new_;
    }

    private void ___TRACE___() {
    }

    public void println(String msg_) {
        drcl.Debug.debug("SIMDEBUG | " + time + (msg_ == null ? "" : "| " + msg_) + "\n");
    }

    protected void off(ACATimer handle_) {
        qWaiting.remove(handle_.getTime(), handle_);
    }

    public Object getEventQueue() {
        return qWaiting;
    }
}
