package javax.realtime;

/**
 * JPF model class for RealtimeThread.
 * Implemented as a wrapper over an instance of the standard Thread class.
 */
public class RealtimeThread extends Thread {

    static {
        HeapMemory hm = HeapMemory.instance();
    }

    protected SchedulingParameters schedParams = null;

    public RealtimeThread() {
        HeapMemory hm = HeapMemory.instance();
        registerHeapThread(Thread.currentThread(), hm);
    }

    public RealtimeThread(SchedulingParameters scheduling, ReleaseParameters release, MemoryParameters memory, MemoryArea area, ProcessingGroupParameters group, Runnable logic) {
        super(logic);
        if (area == null) area = getCurrentMemoryArea();
        if (scheduling != null) {
            this.schedParams = scheduling;
            scheduling.assocTh = this;
            if (scheduling instanceof PriorityParameters) this.setPriority(((PriorityParameters) scheduling).getPriority());
        }
        registerHeapThread(Thread.currentThread(), area);
    }

    protected RealtimeThread(Runnable logic) {
        super(logic);
    }

    public static MemoryArea getCurrentMemoryArea() {
        return getCurrentMemAreaForThread(Thread.currentThread());
    }

    public void schedulePeriodic() {
    }

    public void deschedulePeriodic() {
    }

    public static boolean waitForNextPeriod() throws IllegalThreadStateException {
        return true;
    }

    public void setReleaseParameters(ReleaseParameters parameters) throws IllegalThreadStateException {
    }

    public void setSchedulingParameters(SchedulingParameters scheduling) {
        if (scheduling != null) {
            this.schedParams = scheduling;
            scheduling.assocTh = this;
            if (scheduling instanceof PriorityParameters) this.setPriority(((PriorityParameters) scheduling).getPriority());
        }
    }

    public native void registerHeapThread(Thread creator, MemoryArea ma);

    public native void registerNoHeapThread(Thread creator, MemoryArea ma, Runnable logic);

    public static native MemoryArea getCurrentMemAreaForThread(Thread th);
}
