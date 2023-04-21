package org.middleheaven.quantity.time.clocks;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import org.middleheaven.core.bootstrap.BootstapListener;
import org.middleheaven.core.bootstrap.BootstrapEvent;
import org.middleheaven.core.bootstrap.BootstrapService;
import org.middleheaven.core.reflection.ReflectionException;
import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.quantity.time.EpocTimePoint;
import org.middleheaven.quantity.time.TimePoint;
import org.middleheaven.quantity.time.TimeZone;

/**
 * The clock used by the machine. This is the standard system clock with
 * cadence of 1 and default time zone. 
 *
 */
public class MachineClock extends Clock {

    public MachineClock() {
    }

    @Override
    public TimeZone getTimeZone() {
        return TimeZone.getDefault();
    }

    @Override
    public TimePoint getTime() {
        return new EpocTimePoint(System.currentTimeMillis());
    }

    /**
	 * @return 1 as this clock represent the machine clock with universal cadence
	 */
    @Override
    public double getCadence() {
        return 1;
    }

    final Map<Schedule, ClockTicked> timers = new HashMap<Schedule, ClockTicked>();

    Timer timer = null;

    @Override
    protected ClockTicked schedule(Schedule chronogram, Clock clock) {
        ClockTicked ticked = timers.get(chronogram);
        if (ticked == null) {
            synchronized (timers) {
                if (timer == null) {
                    timer = new Timer("Machine Clock Alert Timer");
                }
                ServiceRegistry.getService(BootstrapService.class).addListener(new BootstapListener() {

                    @Override
                    public void onBoostapEvent(BootstrapEvent event) {
                        if (timer != null) {
                            timer.cancel();
                        }
                    }
                });
            }
            ticked = new TimerClockTicked(chronogram, clock);
            timers.put(chronogram, ticked);
        }
        return ticked;
    }

    private class TimerClockTicked extends ClockTicked {

        Schedule chronogram;

        public TimerClockTicked(Schedule chronogram, Clock clock) {
            this.chronogram = chronogram;
            Method translatingMethod = null;
            Object translatingObject = null;
            try {
                translatingMethod = clock.getClass().getDeclaredMethod("calculateTimeFromReference", TimePoint.class);
                translatingObject = clock;
            } catch (SecurityException e) {
                throw ReflectionException.manage(e, clock.getClass());
            } catch (NoSuchMethodException e) {
                try {
                    translatingMethod = this.getClass().getDeclaredMethod("calculateTimeFromReference", TimePoint.class);
                    translatingObject = this;
                } catch (SecurityException e1) {
                    throw ReflectionException.manage(e1, this.getClass());
                } catch (NoSuchMethodException e1) {
                    throw ReflectionException.manage(e1, this.getClass());
                }
            }
            if (chronogram.getStartTime() == null) {
                timer.schedule(new TickTimerTask(translatingMethod, translatingObject), 0, chronogram.repetitionPeriod().milliseconds());
            } else {
                timer.schedule(new TickTimerTask(translatingMethod, translatingObject), new Date(chronogram.getStartTime().getMilliseconds()), chronogram.repetitionPeriod().milliseconds());
            }
        }

        protected TimePoint calculateTimeFromReference(TimePoint referenceTime) {
            return referenceTime;
        }

        private class TickTimerTask extends TimerTask {

            Method translatingMethod;

            Object translatingObject;

            public TickTimerTask(Method translatingMethod, Object translatingObject) {
                this.translatingMethod = translatingMethod;
                this.translatingObject = translatingObject;
            }

            @Override
            public void run() {
                TimePoint t = Introspector.of(translatingMethod).invoke(TimePoint.class, this.translatingObject, new EpocTimePoint(this.scheduledExecutionTime()));
                if (chronogram.include(t)) {
                    tick(t);
                }
                if (chronogram.isLimited() && chronogram.getEndTime().compareTo(t) <= 0) {
                    timers.remove(chronogram);
                    this.cancel();
                }
            }
        }
    }
}
