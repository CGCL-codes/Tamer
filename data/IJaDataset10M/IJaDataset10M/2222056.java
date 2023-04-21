package org.gudy.azureus2.core3.util;

/**
 * @author parg
 *
 */
public class SimpleTimer {

    /**
		 * A simple timer class for use by application components that want to schedule 
		 * low-overhead events (i.e. when fired the event shouldn't take significant processing
		 * time as there is a limited thread pool to service it 
		 */
    protected static final Timer timer;

    static {
        timer = new Timer("Simple Timer", 32);
        timer.setIndestructable();
        timer.setWarnWhenFull();
    }

    public static TimerEvent addEvent(String name, long when, TimerEventPerformer performer) {
        TimerEvent res = timer.addEvent(name, when, performer);
        return (res);
    }

    public static TimerEvent addEvent(String name, long when, boolean absolute, TimerEventPerformer performer) {
        TimerEvent res = timer.addEvent(name, when, absolute, performer);
        return (res);
    }

    public static TimerEventPeriodic addPeriodicEvent(String name, long frequency, TimerEventPerformer performer) {
        TimerEventPeriodic res = timer.addPeriodicEvent(name, frequency, performer);
        return (res);
    }

    public static TimerEventPeriodic addPeriodicEvent(String name, long frequency, boolean absolute, TimerEventPerformer performer) {
        TimerEventPeriodic res = timer.addPeriodicEvent(name, frequency, absolute, performer);
        return (res);
    }
}
