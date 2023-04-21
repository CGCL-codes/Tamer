package net.sf.opendf.hades.des.schedule;

import net.sf.opendf.hades.des.EventProcessor;

public interface SchedulerObserver {

    public void schedulerException(double now, Exception e);

    public void schedulerSchedule(double now, double time, double precedence, EventProcessor ep);

    public void schedulerUnschedule(double now, EventProcessor ep);

    public void schedulerExecute(double time, double precedence, EventProcessor ep, boolean weak, boolean result);
}
