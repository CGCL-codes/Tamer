package org.go.ee.jmx;

import java.util.concurrent.ConcurrentHashMap;
import javax.management.AttributeList;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import org.go.Calendar;
import org.go.Scheduler;
import org.go.Trigger;
import org.go.TriggerKey;
import org.go.Work;
import org.go.WorkKey;
import org.go.core.SchedulerResources;
import org.go.expcetion.GoException;
import org.go.expcetion.JobPersistenceException;
import org.go.expcetion.SchedulerException;

/**
 * <p>
 * An implementation of the <code>Scheduler</code> interface that remotely
 * proxies all method calls to the equivalent call on a given <code>Scheduler</code>
 * instance, via JMX.
 * </p>
 * 
 * <p>
 * A user must create a subclass to implement the actual connection to the remote 
 * MBeanServer using their application specific connector.
 * For example <code>{@link org.go.ee.jmx.middle.localhost.RmiRemoteMBeanScheduler}</code>.
 * </p>
 * @see org.go.Scheduler
 * @see org.go.core.QuartzScheduler
 * @see org.go.core.SchedulingContext
 */
public abstract class RemoteMBeanScheduler implements Scheduler {

    private ObjectName schedulerObjectName;

    public RemoteMBeanScheduler() {
    }

    /**
	 * Get the given attribute of the remote Scheduler MBean.
	 */
    protected abstract Object getAttribute(String attribute) throws SchedulerException;

    /**
	 * Get the given attributes of the remote Scheduler MBean.
	 */
    protected abstract AttributeList getAttributes(String[] attributes) throws SchedulerException;

    /**
	 * Get the name under which the Scheduler MBean is registered on the
	 * remote MBean server.
	 */
    protected ObjectName getSchedulerObjectName() {
        return schedulerObjectName;
    }

    /**
	 * Invoke the given operation on the remote Scheduler MBean.
	 */
    protected abstract Object invoke(String operationName, Object[] params, String[] signature) throws SchedulerException;

    protected Boolean toBoolean(boolean bool) {
        return (bool) ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
	 * <p>
	 * Calls the equivalent method on the 'proxied' <code>QuartzScheduler</code>,
	 * passing the <code>SchedulingContext</code> associated with this
	 * instance.
	 * </p>
	 */
    @Override
    public void addCalendar(String calName, Calendar calendar, boolean replace, boolean updateTriggers) throws SchedulerException {
        invoke("addCalendar", new Object[] { calName, calendar, toBoolean(replace), toBoolean(updateTriggers) }, new String[] { String.class.getName(), Calendar.class.getName(), boolean.class.getName(), boolean.class.getName() });
    }

    /**
	 * Set the name under which the Scheduler MBean is registered on the
	 * remote MBean server.
	 */
    public void setSchedulerObjectName(String schedulerObjectName) throws SchedulerException {
        try {
            this.schedulerObjectName = new ObjectName(schedulerObjectName);
        } catch (MalformedObjectNameException e) {
            throw new SchedulerException("Failed to parse Scheduler MBean name: " + schedulerObjectName, e);
        }
    }

    @Override
    public ConcurrentHashMap<String, Object> getContext() throws SchedulerException {
        return null;
    }

    @Override
    public String getJobGroupNames() throws SchedulerException {
        return null;
    }

    @Override
    public SchedulerResources getResource() throws SchedulerException {
        return null;
    }

    @Override
    public String getSchedulerName() throws SchedulerException {
        return null;
    }

    @Override
    public Trigger getTrigger(TriggerKey tKey) throws SchedulerException {
        return null;
    }

    @Override
    public boolean isShutdown() throws SchedulerException {
        return ((Boolean) getAttribute("shutdown")).booleanValue();
    }

    @Override
    public void notifySchedulerListenersError(String string, GoException se) throws SchedulerException {
    }

    @Override
    public void notifySchedulerListenersError(String string, JobPersistenceException se) throws SchedulerException {
    }

    @Override
    public void pauseWork(WorkKey workKey) throws SchedulerException {
    }

    @Override
    public void shutdown(boolean waitForJobsToComplete) throws SchedulerException {
    }

    @Override
    public void shutdownNow() throws SchedulerException {
    }

    @Override
    public void unscheduleWork(TriggerKey tKey) throws SchedulerException {
    }

    @Override
    public void pauseAllWorks() throws SchedulerException {
    }

    @Override
    public void scheduler(Work work, Trigger trigger) throws SchedulerException {
    }
}
