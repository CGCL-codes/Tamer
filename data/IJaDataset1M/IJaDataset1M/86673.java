package org.destecs.vdmj.scheduler;

import org.overturetool.vdmj.lex.LexLocation;
import org.overturetool.vdmj.runtime.Context;
import org.overturetool.vdmj.scheduler.CPUResource;
import org.overturetool.vdmj.scheduler.ISchedulableThread;
import org.overturetool.vdmj.scheduler.RunState;
import org.overturetool.vdmj.scheduler.Signal;
import org.overturetool.vdmj.values.ObjectValue;

public class EventThread implements ISchedulableThread {

    private Thread thread;

    private long tid = 0;

    public EventThread(Thread t) {
        this.thread = t;
    }

    public void duration(long pause, Context ctxt, LexLocation location) {
    }

    public CPUResource getCPUResource() {
        return null;
    }

    public long getDurationEnd() {
        return 0;
    }

    public long getId() {
        return tid;
    }

    public String getName() {
        return "Event thread - " + getId();
    }

    public ObjectValue getObject() {
        return null;
    }

    public RunState getRunState() {
        return null;
    }

    public long getSwapInBy() {
        return 0;
    }

    public Thread getThread() {
        return this.thread;
    }

    public long getTimestep() {
        return 0;
    }

    public void inOuterTimestep(boolean b) {
    }

    public boolean inOuterTimestep() {
        return false;
    }

    public boolean isActive() {
        return false;
    }

    public boolean isAlive() {
        return false;
    }

    public boolean isPeriodic() {
        return false;
    }

    public boolean isVirtual() {
        return false;
    }

    public void locking(Context ctxt, LexLocation location) {
    }

    public void run() {
    }

    public void runslice(long slice) {
    }

    public void setName(String name) {
    }

    public void setSignal(Signal sig) {
    }

    public void setState(RunState newstate) {
    }

    public void setSwapInBy(long swapInBy) {
    }

    public void setTimestep(long step) {
    }

    public void start() {
    }

    public void step(Context ctxt, LexLocation location) {
    }

    public void suspendOthers() {
    }

    public void waiting(Context ctxt, LexLocation location) {
    }

    public void alarming(long expected) {
    }

    public long getAlarmWakeTime() {
        return 0;
    }

    public void clearAlarm() {
    }
}
