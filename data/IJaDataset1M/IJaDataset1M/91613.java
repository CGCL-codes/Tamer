package jaxlib.mbeans.cron;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.ReflectionException;
import javax.management.openmbean.OpenMBeanAttributeInfoSupport;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.SimpleType;
import jaxlib.array.ObjectArrays;
import jaxlib.lang.Enums;
import jaxlib.lang.StackTraces;
import jaxlib.management.AnnotatedMBean;
import jaxlib.management.LocalMBeanReference;
import jaxlib.management.MBean;
import jaxlib.management.MBeanAttribute;
import jaxlib.management.MBeanOperation;
import jaxlib.prefs.XPreferences;
import jaxlib.ref.UnmodifiableWeakReference;
import jaxlib.thread.FutureCommand;
import jaxlib.thread.ScheduledFutureCommand;
import jaxlib.thread.TaskState;
import jaxlib.thread.TaskStateEvent;
import jaxlib.thread.TaskStateListener;
import jaxlib.time.DayOfWeek;
import jaxlib.time.Month;
import jaxlib.time.cron.DaysOfMonth;
import jaxlib.time.cron.DaysOfWeek;
import jaxlib.time.cron.Hours;
import jaxlib.time.cron.Minutes;
import jaxlib.time.cron.Months;
import jaxlib.time.cron.RecurringSchedule;
import jaxlib.time.cron.Seconds;
import jaxlib.time.cron.Years;
import jaxlib.util.CheckArg;
import jaxlib.util.Strings;

/**
 * @author  <a href="mailto:joerg.wassmer@web.de">J�rg Wa�mer</a>
 * @since   JaXLib 1.0
 * @version $Id: CronService.java 1461 2005-11-20 20:38:37Z joerg_wassmer $
 */
public abstract class CronService extends AnnotatedMBean implements CronServiceMBean {

    private volatile RecurringSchedule schedule = RecurringSchedule.NEVER;

    private volatile boolean schedulerEnabled = false;

    private DayOfWeek firstDayOfWeek = DayOfWeek.LOCAL_FIRST_DAY_OF_WEEK;

    private volatile Date nextStartDate = null;

    private volatile boolean reschedulingOnException;

    private volatile Date currentStartDate = null;

    private volatile Date previousEndDate = null;

    private volatile Date previousStartDate = null;

    private final WeakReference<CronService> self = new UnmodifiableWeakReference<CronService>(this);

    private volatile ScheduledFutureCommand<?> task;

    private volatile Throwable taskException;

    private volatile TaskState taskState = TaskState.READY;

    protected CronService() {
        super();
    }

    protected abstract ScheduledFutureCommand<?> scheduleTask(long delay, TimeUnit timeUnit);

    private synchronized void reschedule(boolean immediately) {
        ScheduledFutureCommand<?> task = this.task;
        if (task != null) {
            task.cancel(false);
            if (!task.isDone()) return;
            task = null;
            this.task = null;
        }
        if (!immediately && this.schedule.isNever()) this.schedulerEnabled = false;
        if (isMBeanRegistered() && (immediately || isSchedulerEnabled())) {
            long now = System.currentTimeMillis();
            Date nextDate = immediately ? new Date(now) : this.schedule.nextScheduledDate(new Date(now), null, null);
            if (nextDate == null) {
                this.schedulerEnabled = false;
            } else {
                long delayMillis = Math.max(0, nextDate.getTime() - now);
                delayMillis = Math.max(delayMillis, 999);
                nextDate = null;
                this.taskException = null;
                this.taskState = TaskState.READY;
                try {
                    task = scheduleTask(delayMillis, TimeUnit.MILLISECONDS);
                    if (task != null) {
                        new TaskHandler(this, task);
                        this.task = task;
                    }
                } catch (Throwable ex) {
                    ex.printStackTrace();
                    getLogger().log(Level.SEVERE, "Exception thrown while scheduling task", ex);
                    this.taskState = TaskState.FAILED;
                    this.taskException = ex;
                }
            }
        }
    }

    private synchronized void taskStateChanged(ScheduledFutureCommand<?> task) {
        if (task == this.task) {
            this.taskException = task.getException();
            this.taskState = task.getTaskState();
            if (task.isDone()) {
                this.task = null;
                taskStateChanged(task, true);
                reschedule(false);
            }
        } else {
            taskStateChanged(task, false);
        }
    }

    protected void taskStateChanged(ScheduledFutureCommand<?> task, boolean isCurrentTask) {
    }

    @Override
    protected synchronized void postDeregister(LocalMBeanReference mbeanRef, int remainingRegistrations) throws Exception {
        super.postDeregister(mbeanRef, remainingRegistrations);
        if (remainingRegistrations == 0) {
            cancel(false);
        }
    }

    @Override
    protected synchronized void postRegister(LocalMBeanReference mbeanRef, int countRegistrations) throws Exception {
        super.postRegister(mbeanRef, countRegistrations);
        if (countRegistrations == 1) {
            reschedule(false);
        }
    }

    @MBeanOperation
    public boolean cancel(boolean mayInterruptIfRunning) {
        Future<?> task = this.task;
        this.schedulerEnabled = false;
        return (task != null) && task.cancel(mayInterruptIfRunning);
    }

    public String dumpStack() {
        Throwable exception = this.taskException;
        if (exception != null) {
            return StackTraces.toString(exception);
        } else {
            FutureCommand<?> task = this.task;
            if (task == null) {
                return null;
            } else {
                StackTraceElement[] stackTrace = task.getStackTrace();
                if (stackTrace == null) return null; else return StackTraces.toString(task.getStackTrace());
            }
        }
    }

    @MBeanAttribute
    public Date getCurrentStartDate() {
        Date d = this.currentStartDate;
        return (d == null) ? null : (Date) d.clone();
    }

    @MBeanAttribute
    public Throwable getException() {
        return this.taskException;
    }

    @MBeanAttribute(persistent = true)
    public Date getFinalDate() {
        return this.schedule.getFinalDate();
    }

    @MBeanAttribute(persistent = true)
    public Date getInitialDate() {
        return this.schedule.getInitialDate();
    }

    @MBeanAttribute
    public synchronized Date getNextStartDate() {
        Date d = this.nextStartDate;
        if ((d == null) && this.schedulerEnabled) d = this.schedule.nextScheduledDate(this.previousEndDate, null, null);
        return (d == null) ? null : (Date) d.clone();
    }

    @MBeanAttribute
    public Date getPreviousEndDate() {
        Date d = this.previousEndDate;
        return (d == null) ? null : (Date) d.clone();
    }

    @MBeanAttribute
    public Date getPreviousStartDate() {
        Date d = this.previousStartDate;
        return (d == null) ? null : (Date) d.clone();
    }

    @MBeanAttribute(visible = false)
    public RecurringSchedule getSchedule() {
        return this.schedule;
    }

    public DayOfWeek getFirstDayOfWeek() {
        return this.firstDayOfWeek;
    }

    @MBeanAttribute(string = true)
    public Years getScheduledYears() {
        return this.schedule.getYears();
    }

    @MBeanAttribute(string = true)
    public Months getScheduledMonths() {
        return this.schedule.getMonths();
    }

    @MBeanAttribute(string = true)
    public DaysOfMonth getScheduledDaysOfMonth() {
        return this.schedule.getDaysOfMonth();
    }

    @MBeanAttribute(string = true)
    public DaysOfWeek getScheduledDaysOfWeek() {
        return this.schedule.getDaysOfWeek();
    }

    @MBeanAttribute(string = true)
    public Hours getScheduledHours() {
        return this.schedule.getHours();
    }

    @MBeanAttribute(string = true)
    public Minutes getScheduledMinutes() {
        return this.schedule.getMinutes();
    }

    @MBeanAttribute(string = true)
    public Seconds getScheduledSeconds() {
        return this.schedule.getSeconds();
    }

    @MBeanAttribute
    public TimeZone getTimeZone() {
        return this.schedule.getScheduledTimeZone();
    }

    /**
   * Returns a weak reference to this {@code CronService} instance.
   * The returned reference neither supports the {@link WeakReference#clear()} nor the 
   * {@link WeakReference#enqueue()} operations.
   *
   * @since JaXLib 1.0
   */
    public WeakReference<? extends CronService> getSelfReference() {
        return this.self;
    }

    @MBeanAttribute
    public TaskState getTaskState() {
        return this.taskState;
    }

    @MBeanAttribute(persistent = true)
    public boolean isReschedulingOnException() {
        return this.reschedulingOnException;
    }

    @MBeanAttribute()
    public boolean isSchedulerEnabled() {
        return this.schedulerEnabled;
    }

    @MBeanOperation
    public void schedule(Date initialDate, Date finalDate, Years years, Months months, DaysOfMonth daysOfMonth, DaysOfWeek daysOfWeek, Hours hours, Minutes minutes, Seconds seconds) {
        synchronized (this) {
            setSchedule(this.schedule.replace(null, initialDate, finalDate, years, months, daysOfMonth, daysOfWeek, hours, minutes, seconds));
        }
    }

    @MBeanAttribute(visible = false)
    public synchronized void setSchedule(RecurringSchedule schedule) {
        if (schedule == null) throw new NullPointerException("schedule");
        if (!this.schedule.equals(schedule)) {
            this.schedule = schedule;
            reschedule(false);
        }
    }

    @MBeanAttribute(persistent = true)
    public synchronized void setSchedulerEnabled(boolean v) {
        if (this.schedulerEnabled != v) {
            this.schedulerEnabled = v;
            if (v) {
                if (this.task == null) reschedule(false);
            } else {
                if (this.task != null) cancel(false);
                this.nextStartDate = null;
            }
        }
    }

    @MBeanAttribute
    public void setFinalDate(Date date) {
        setSchedule(this.schedule.replaceFinalTime(date));
    }

    @MBeanAttribute
    public void setInitialDate(Date date) {
        setSchedule(this.schedule.replaceInitialTime(date));
    }

    @MBeanAttribute
    public void setTimeZone(TimeZone timeZone) {
        setSchedule(this.schedule.replace(timeZone));
    }

    protected void setPreviousEndDate(Date v) {
        this.previousEndDate = v;
    }

    protected void setPreviousStartDate(Date v) {
        this.previousStartDate = v;
    }

    @MBeanAttribute
    public synchronized void setReschedulingOnException(boolean v) {
        this.reschedulingOnException = v;
    }

    @MBeanOperation
    public synchronized boolean startTaskNow() {
        Future<?> task = this.task;
        if (((task != null) && !task.isDone()) || !isMBeanRegistered()) {
            return false;
        } else {
            reschedule(true);
            return true;
        }
    }

    private static final class TaskHandler<T extends ScheduledFutureCommand> extends Object implements TaskStateListener {

        private WeakReference<CronService> cronService;

        private ScheduledFutureCommand<?> task;

        TaskHandler(CronService cronService, ScheduledFutureCommand<?> task) {
            super();
            this.cronService = cronService.self;
            this.task = task;
            task.addTaskStateListener(this);
        }

        private CronService cronService() {
            WeakReference<CronService> ref = this.cronService;
            CronService cronService = (ref == null) ? null : ref.get();
            if (cronService == null) {
                this.cronService = null;
                stopListening();
            }
            return cronService;
        }

        private void stopListening() {
            this.cronService = null;
            ScheduledFutureCommand<?> task = this.task;
            this.task = null;
            if (task != null) task.removeTaskStateListener(this);
        }

        private void taskStateChanged() {
            CronService cronService = cronService();
            ScheduledFutureCommand<?> task = this.task;
            if ((cronService != null) && (task != null)) {
                cronService.taskStateChanged(task);
            }
        }

        public void taskCancelled(TaskStateEvent e) {
            taskStateChanged();
        }

        public void taskFailed(TaskStateEvent e) {
            taskStateChanged();
        }

        public void taskStarting(TaskStateEvent e) {
            taskStateChanged();
        }

        public void taskSucceeded(TaskStateEvent e) {
            taskStateChanged();
        }

        public void taskTerminated(TaskStateEvent e) {
            CronService cronService = cronService();
            ScheduledFutureCommand<?> task = this.task;
            stopListening();
            if ((cronService != null) && (task != null)) {
                cronService.taskStateChanged(task);
            }
        }
    }
}
