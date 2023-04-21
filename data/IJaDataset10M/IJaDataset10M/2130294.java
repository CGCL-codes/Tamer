package drcl.comp;

import java.util.*;
import java.beans.*;
import drcl.util.queue.FIFOQueue;
import drcl.util.StringUtil;

/**
 * @see ARuntime
 */
public class AWorkerThread extends WorkerThread {

    static boolean RECYCLING = true;

    static final Task DUMMY_CONTEXT = Task.createNotify("DUMMY_TASK");

    static final Task REQUEST_WAITING = Task.createNotify("REQUEST_WAITING");

    static final Task GETTING_TASK = Task.createNotify("Getting_Task");

    static final Object SEND_RCV_REQUEST = Port.SEND_RCV_REQUEST;

    /** The worker runtime of this thread. */
    protected ARuntime aruntime;

    /** The object that this thread waits on. */
    protected transient Object sleepOn = null;

    Task nextTask;

    double wakeUpTime = 0.0;

    WaitPack waitPack;

    boolean beingWakedup = false;

    Thread lastwakeupthread = null;

    Object lastsleepon = null;

    public AWorkerThread() {
        super();
    }

    public AWorkerThread(ThreadGroup group_, String name_) {
        super(group_, name_);
    }

    public String info(String prefix_) {
        if (runtime == null) return StringUtil.lastSubstring(prefix_ + super.toString(), ".") + "," + "<orphan>";
        StringBuffer sb_ = new StringBuffer(prefix_ + StringUtil.lastSubstring(super.toString(), ".") + ", " + state + (sleepOn == null || sleepOn == this ? "" : " on " + sleepOn + (waitPack != null && waitPack.target != null ? "(waiting/locking)" : "")) + (state == State_SLEEPING ? " until " + wakeUpTime : "") + "\n" + prefix_ + "Context=" + mainContext() + (mainContext == null ? ", " : "\n" + prefix_) + "Next_Task=" + (nextTask != null ? nextTask.toString() : "<null>") + "\n");
        if (currentContext != mainContext) sb_.append(prefix_ + "CurrentContext: " + _currentContext() + "\n");
        if (returnPort != null) sb_.append(prefix_ + "return_port: " + returnPort + "\n");
        if (sendingPort != null) sb_.append(prefix_ + "sending_port: " + sendingPort + "\n");
        sb_.append(prefix_ + "Locks: " + locks());
        sb_.append(prefix_ + "#arrivals: " + totalNumEvents + "\n");
        sb_.append(prefix_ + "last_sleepOn: " + lastsleepon + "\n");
        sb_.append(prefix_ + "last_wkUp_thread: " + (lastwakeupthread == null ? null : currentThread(lastwakeupthread)) + "\n");
        return sb_.toString();
    }

    private String currentThread(Thread t_) {
        if (t_ == null) t_ = Thread.currentThread();
        return t_.toString();
    }

    public String _toString() {
        if (runtime == null) return StringUtil.lastSubstring(super.toString(), ".") + "," + "<orphan>"; else return StringUtil.lastSubstring(super.toString(), ".") + "--" + state + (sleepOn == null || sleepOn == this ? "" : " on " + sleepOn + (waitPack != null && waitPack.target != null ? "(waiting/locking)" : "")) + (state == State_SLEEPING ? " until " + wakeUpTime : "") + "--" + "context=" + mainContext() + "--" + "next_task=" + (nextTask != null ? nextTask.toString() : "<null>") + "--" + (currentContext == mainContext ? "" : "--currentContext=" + _currentContext()) + (returnPort == null ? "" : "--return_port=" + returnPort) + (sendingPort == null ? "" : "--sending_port=" + sendingPort);
    }

    String mainContext() {
        if (mainContext == null) return "<null>";
        return (mainContext.port != null ? mainContext.port + "," : "") + (mainContext.data != null ? mainContext.data.toString() : "<null>") + (returnPort == null ? "" : ",return_port:" + returnPort) + (sendingPort == null ? "" : ",sending_port:" + sendingPort);
    }

    /**
   * Delay the starting until the time specified later(second).
   */
    public void start() {
        if (aruntime.isSuspend()) return;
        if (isAlive()) {
            synchronized (this) {
                lastsleepon = this;
                lastwakeupthread = Thread.currentThread();
                try {
                    this.notify();
                } catch (Exception e_) {
                    drcl.Debug.error(this, "start()| " + e_, false);
                }
            }
        } else {
            setState(State_INACTIVE);
            super.start();
        }
    }

    final void setWakeupTime(double time_) {
        wakeUpTime = time_;
    }

    final double getWakeupTime() {
        return wakeUpTime;
    }

    /**
   */
    protected final void __sleepOn(Object sleepOn_, String prestate_, String poststate_) {
        sleepOn = sleepOn_;
        if (prestate_ != null) {
            if (poststate_ == null) poststate_ = state;
            setState(prestate_);
        }
        synchronized (sleepOn) {
            try {
                sleepOn.wait();
            } catch (InterruptedException e_) {
                throw new WorkerThreadInterruptedException();
            }
        }
        if (poststate_ != null) setState(poststate_);
        sleepOn = null;
    }

    /**
   * Standard Thread.run().
   */
    public void run() {
        if (Thread.currentThread() != this) {
            throw new WorkerThreadException("not run() by the owning thread.  Current thread:" + Thread.currentThread() + "---owning thread:" + this);
        }
        try {
            if (aruntime.debug && aruntime.isDebugEnabledAt(ARuntime.Debug_THREAD)) aruntime.println(ARuntime.Debug_THREAD, this, "THREAD STARTS");
            for (; ; ) {
                synchronized (aruntime) {
                    if (mainContext == null || mainContext == DUMMY_CONTEXT) {
                        nextTask = GETTING_TASK;
                        nextTask = aruntime.getTask(mainContext == null);
                        if (nextTask != null) {
                            mainContext = nextTask;
                            nextTask = null;
                        } else {
                            setState(State_RECYCLING);
                            sleepOn = this;
                            aruntime.recycle(this);
                        }
                    }
                }
                if (state == State_RECYCLING) {
                    setState(State_INACTIVE);
                    synchronized (this) {
                        if (mainContext == DUMMY_CONTEXT) mainContext = null;
                        if (mainContext == null) {
                            __sleepOn(this, State_INACTIVE, State_INACTIVE);
                            continue;
                        } else sleepOn = null;
                    }
                }
                if (runtime.resetting) {
                    throw new WorkerThreadInterruptedException();
                } else if (aruntime.isSuspend()) {
                    synchronized (this) {
                        __sleepOn(this, State_PREACTIVE, State_ACTIVE);
                    }
                }
                if (mainContext.threadGroup != null) {
                    if (mainContext instanceof TaskNotify) {
                        synchronized (mainContext.data) {
                            if (aruntime.debug && aruntime.isDebugEnabledAt(ARuntime.Debug_THREAD)) aruntime.println(ARuntime.Debug_THREAD, this, "EXECUTING notify:" + mainContext.data + "," + System.currentTimeMillis());
                            mainContext.data.notify();
                            aruntime.nthreadsWaiting--;
                        }
                        mainContext = null;
                        continue;
                    } else if (getThreadGroup() != mainContext.threadGroup) {
                        aruntime.immediatelyStart(mainContext);
                        mainContext = null;
                        continue;
                    }
                }
                if (aruntime.debug && aruntime.isDebugEnabledAt(ARuntime.Debug_THREAD)) aruntime.println(ARuntime.Debug_THREAD, this, "EXECUTING:" + mainContext);
                setState(State_ACTIVE);
                String method_ = "process()";
                currentContext = mainContext;
                returnPort = currentContext.returnPort;
                try {
                    mainContext.execute(this);
                } catch (NullPointerException e_) {
                    if (runtime == null) return;
                    e_.printStackTrace();
                    drcl.Debug.error(info());
                } catch (Exception e_) {
                    e_.printStackTrace();
                    drcl.Debug.error(info());
                } catch (SendReceiveException e_) {
                    e_.printStackTrace();
                    drcl.Debug.error(info());
                }
                currentContext.port = null;
                currentContext.data = null;
                finishing();
                mainContext = DUMMY_CONTEXT;
                if (!RECYCLING) {
                    aruntime.remove(this);
                    break;
                }
                if (nextTask != null) {
                    if (mainContext != null && mainContext != DUMMY_CONTEXT) drcl.Debug.systemFatalError("task assigned to occupied thread:" + this);
                    mainContext = nextTask;
                    nextTask = null;
                }
            }
            drcl.Debug.systemFatalError("Unexpected finish at " + this);
        } catch (WorkerThreadInterruptedException e_) {
            if (mainContext != null && mainContext.port != null) releaseAllLocks(mainContext.port.host);
            aruntime.remove(this);
        } catch (NullPointerException e_) {
            if (runtime != null) {
                e_.printStackTrace();
                drcl.Debug.error(info());
            }
        }
        runtime = null;
        aruntime = null;
    }

    static void ___SUBCLASS_OVERRIDES___() {
    }

    void _sleepUntil(double time_, String poststate_) {
        if (poststate_ == null) poststate_ = state;
        setWakeupTime(time_);
        if (sleepOn == null) sleepOn = this;
        synchronized (sleepOn) {
            if (nextTask != null) _semanticsError("sleep()");
            if (aruntime.threadRequestsSleeping(this, time_)) {
                __sleepOn(sleepOn, State_SLEEPING, State_AWAKENING);
                if (aruntime.debug && aruntime.isDebugEnabledAt(aruntime.Debug_Q)) aruntime.println(aruntime.Debug_Q, this, "awaked from finite sleep, " + _currentContext());
            }
            setWakeupTime(Double.NaN);
        }
        setState(poststate_);
    }

    void _sleepOn(Object sleepOn_, String prestate_, String poststate_) {
        if (poststate_ == null) poststate_ = state;
        sleepOn = sleepOn_;
        synchronized (sleepOn) {
            if (nextTask != null) _semanticsError("wait()");
            aruntime.threadBecomesWaiting(this);
            __sleepOn(sleepOn_, prestate_, State_AWAKENING);
            if (aruntime.debug && aruntime.isDebugEnabledAt(aruntime.Debug_Q)) aruntime.println(aruntime.Debug_Q, this, "awaked from indefinite sleep: " + _currentContext());
        }
        setState(poststate_);
    }

    static void ___API_FOR_COMPONENT___() {
    }

    protected final void sleepFor(double time_) {
        _sleepUntil(runtime.getTime() + time_, State_AWAKENING);
        setState(State_ACTIVE);
    }

    protected final void sleepUntil(double time_) {
        _sleepUntil(time_, State_AWAKENING);
        setState(State_ACTIVE);
    }

    /** Let go of the thread from sleep in idle. */
    public void kill() {
        if (isAlive()) interrupt(); else aruntime.remove(this);
    }

    static void ___EVENTS___() {
    }

    protected void changeCurrentContext(Port port_, Object data_, String state_) {
        if (currentContext == mainContext || currentContext == null) currentContext = new TaskReceive(port_, data_); else {
            currentContext.data = data_;
            currentContext.port = port_;
        }
        if (state_ != null) state = state_;
        ;
        if (aruntime.debug && aruntime.isDebugEnabledAt(ARuntime.Debug_THREAD)) aruntime.println(ARuntime.Debug_THREAD, this, "currentContext ---> " + "state:" + state + "," + _currentContext());
    }

    String _currentContext() {
        if (currentContext == null || currentContext.data == null && currentContext.port == null) return "<null>"; else return "PORT:" + currentContext.port + ",DATA:" + drcl.util.StringUtil.toString(currentContext.data);
    }

    public final boolean isReadyForNextTask() {
        return (state == State_FINISHING || state == State_INACTIVE) && nextTask == null;
    }

    static void ___INFO___() {
    }

    static void ___SYNC_APIS___() {
    }

    private void _semanticsError(String method_) {
        drcl.Debug.fatalError("Calling " + method_ + " after doLastSending()/finishing() is prohibited.\n" + "Current_thread:" + this + "\n");
    }

    public final String locks() {
        return "<not implemented yet>\n";
    }

    class WaitPack {

        Object target;

        int counter;

        public WaitPack() {
        }
    }

    private LockPack lookforLock(Component host_, Object o_) {
        LockPack p_ = null;
        synchronized (host_) {
            if (host_.locks != null) {
                p_ = (LockPack) host_.locks;
                while (p_ != null && p_.target != o_) p_ = p_.next;
            }
            if (p_ == null) {
                p_ = new LockPack(o_);
                p_.next = (LockPack) host_.locks;
                host_.locks = p_;
            }
        }
        return p_;
    }

    protected final void lock(Component host_, Object o_) {
        LockPack p_ = lookforLock(host_, o_);
        if (p_.holder == this) {
            p_.counter++;
            return;
        }
        String old_ = state;
        synchronized (p_) {
            if (p_.counter == 0 || (p_.holder != null && p_.holder.isOrphan())) {
                p_.holder = this;
                p_.counter = 1;
                return;
            }
            p_.lockReqCount++;
            if (waitPack == null) waitPack = new WaitPack();
            waitPack.target = p_.target;
            _sleepOn(p_, State_LOCKING, null);
            waitPack.target = null;
            if (isOrphan()) {
                p_.notify();
                return;
            }
            p_.holder = this;
            p_.counter = 1;
        }
    }

    protected final void unlock(Component host_, Object o_) {
        unlock(host_, o_, false);
    }

    protected void unlock(Component host_, Object o_, boolean release_) {
        LockPack p_ = lookforLock(host_, o_);
        String old_ = state;
        setState(State_UNLOCKING);
        synchronized (p_) {
            if (release_ || --p_.counter < 0) p_.counter = 0;
            if (p_.counter == 0) {
                if (p_.lockReqCount == 0) {
                    p_.holder = null;
                    return;
                }
                p_.lockReqCount--;
                p_.counter = -1;
                p_.holder = null;
                synchronized (aruntime) {
                    if (aruntime.getWorkforce()) {
                        p_.notify();
                        aruntime.nthreadsWaiting--;
                    } else {
                        aruntime.newTask(Task.createNotify(p_), this);
                    }
                }
            }
        }
        setState(old_);
    }

    protected final void releaseAllLocks(Component host_) {
        String old_ = state;
        setState(State_UNLOCKING);
        for (LockPack p_ = (LockPack) host_.locks; p_ != null; p_ = p_.next) if (p_.holder == this) {
            synchronized (p_) {
                p_.holder = null;
                p_.counter = -1;
                if (p_.lockReqCount == 0) {
                    continue;
                }
                p_.lockReqCount--;
                synchronized (aruntime) {
                    if (aruntime.getWorkforce()) {
                        p_.notify();
                        aruntime.nthreadsWaiting--;
                    } else aruntime.newTask(Task.createNotify(p_), this);
                }
            }
        }
        setState(old_);
    }

    protected final void wait(Component host_, Object o_) {
        LockPack p_ = lookforLock(host_, o_);
        String old_ = state;
        setState(State_WAITING);
        synchronized (o_) {
            if (waitPack == null) waitPack = new WaitPack();
            waitPack.target = p_.target;
            if (p_.holder == this) {
                waitPack.counter = p_.counter;
                synchronized (p_) {
                    p_.holder = null;
                    if (p_.lockReqCount == 0) p_.counter = 0; else {
                        p_.counter = -1;
                        p_.lockReqCount--;
                        synchronized (aruntime) {
                            if (aruntime.getWorkforce()) {
                                p_.notify();
                                aruntime.nthreadsWaiting--;
                            } else aruntime.newTask(Task.createNotify(p_), this);
                        }
                    }
                }
            }
            p_.waitCount++;
            _sleepOn(o_, State_WAITING, null);
            if (isOrphan()) throw new WorkerThreadInterruptedException("Orphan thread");
            if (waitPack.counter > 0) {
                synchronized (p_) {
                    if (p_.holder != null) lock(host_, p_.target); else {
                        p_.holder = this;
                        p_.counter = waitPack.counter;
                    }
                }
            }
        }
        if (p_ != null) {
            waitPack.counter = 0;
            waitPack.target = null;
        }
    }

    protected final void notify(Component host_, Object o_) {
        if (o_ == null) return;
        String old_ = state;
        setState(State_NOTIFYING);
        LockPack p_ = lookforLock(host_, o_);
        synchronized (o_) {
            if (p_.waitCount == 0) return;
            p_.waitCount--;
            synchronized (aruntime) {
                if (aruntime.getWorkforce()) {
                    o_.notify();
                    aruntime.nthreadsWaiting--;
                } else {
                    aruntime.newTask(Task.createNotify(o_), this);
                }
            }
        }
        setState(old_);
    }

    protected final void notifyAll(Component host_, Object o_) {
        if (o_ == null) return;
        String old_ = state;
        setState(State_NOTIFYING);
        LockPack p_ = lookforLock(host_, o_);
        synchronized (o_) {
            if (p_.waitCount == 0) return;
            synchronized (aruntime) {
                if (aruntime.getWorkforce(p_.waitCount)) {
                    o_.notifyAll();
                    aruntime.nthreadsWaiting -= p_.waitCount;
                } else {
                    int remainingWorkforce_ = aruntime.getRemainingWorkforce();
                    for (int i = 0; i < remainingWorkforce_; i++) o_.notify();
                    aruntime.nthreadsWaiting -= remainingWorkforce_;
                    for (int i = p_.waitCount - remainingWorkforce_; i > 0; i--) aruntime.newTask(Task.createNotify(o_), this);
                }
            }
            p_.waitCount = 0;
        }
        setState(old_);
    }

    /**
   * Returns the thread that is holding the lock of the target object.
   * The returned thread may be an orphan thread from previous run.
  final WorkerThread getHolder(Object o_)
  {
    Component host_ = getHost();
    if (host_ == null) return null;
    LockPack p_ = lookforLock(host_, o_);
    return p_ == null? null: p_.holder;
  }
   */
    protected void yieldToRuntime() {
        synchronized (this) {
            if (aruntime.isSuspend()) __sleepOn(this, State_PREACTIVE, state); else if (runtime.resetting) throw new WorkerThreadInterruptedException();
            aruntime.newTask(Task.createNotify(this), this);
            _sleepOn(this, State_YIELD, null);
        }
    }

    static void ___STATE___() {
    }

    protected final void setState(String new_) {
        if (state == new_) return;
        if (aruntime.debug) aruntime.threadStateChange(this, state, new_);
        state = new_;
    }

    final boolean isActive() {
        return !(state == State_INACTIVE || state == State_SLEEPING || state == State_PREACTIVE || state == State_AWAKENING_WAITING || state == State_LOCKING || state == State_WAITING || state == State_YIELD);
    }

    final boolean isInActive() {
        return state == State_INACTIVE || state == State_PREACTIVE;
    }

    /** State of acquiring a lock. */
    static final String State_LOCKING = "LOCKING";

    static final String State_RECYCLING = "RECYCLING";

    static final String State_PREACTIVE = "PREACTIVE";

    static final String State_AWAKENING_WAITING = "AWAKENING-WAITING";

    static final String State_NOTIFYING = "NOTIFYING(debug)";

    static final String State_NOTIFYING2 = "NOTIFYING2(debug)";

    static final String State_AWAKENING = "AWAKENING";

    static final String State_UNLOCKING = "UNLOCKING(debug)";

    static final String State_YIELD = "YIELD(debug)";
}
