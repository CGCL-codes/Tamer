package com.fddtool.pd.common;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This is a base class to run the periodic tasks that should run in the
 * background.
 * 
 * @author SKhramtc
 */
public abstract class TimerTaskRunner {

    /**
	 * Time interval of 24 hours.
	 */
    public static final long ONE_DAY = 1000 * 60 * 60 * 24;

    /**
	 * The logger for this class.
	 */
    protected Log logger = LogFactory.getLog(this.getClass());

    /**
	 * Thread safe storage for the instance of calendar.
	 */
    protected static final ThreadLocal<Calendar> CALENDAR = new ThreadLocal<Calendar>();

    static Timer timer = new Timer(true);

    /**
	 * Returns instance of calendar to validate the dates.
	 * 
	 * @return Calendar instance.
	 */
    protected static Calendar getCalendar() {
        Calendar calendar = CALENDAR.get();
        if (calendar == null) {
            calendar = GregorianCalendar.getInstance();
            CALENDAR.set(calendar);
        }
        return calendar;
    }

    /**
	 * Creates a new instance of this class and schedules next task execution.
	 */
    protected TimerTaskRunner() {
        scheduleNextTimerRun();
    }

    /**
	 * Schedules the time when data collection routine should be invoked next.
	 */
    protected void scheduleNextTimerRun() {
        Calendar c = getCalendar();
        c.setTime(new Date());
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 1);
        c.set(Calendar.MILLISECOND, 0);
        c.add(Calendar.HOUR_OF_DAY, getHourInterval());
        Date nextTime = c.getTime();
        timer.schedule(createTask(), nextTime);
    }

    /**
	 * Stops the timer that runs periodic operations. Call it when application
	 * shuts down.
	 */
    public static void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
	 * Override this method to return the task to be executed on timer.
	 * 
	 * @return TimerTask to be executed.
	 */
    public abstract TimerTask createTask();

    /**
	 * Override this method to return the number of hours to wait before running
	 * the task again.
	 * 
	 * @return integer number of hours. The valid numbers are 1 to 23.
	 */
    public abstract int getHourInterval();
}
