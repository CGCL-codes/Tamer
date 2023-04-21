package purej.job.impl.jdbcjobstore;

import java.sql.Connection;
import java.util.List;
import java.util.Set;
import purej.job.Calendar;
import purej.job.JobDetail;
import purej.job.JobPersistenceException;
import purej.job.ObjectAlreadyExistsException;
import purej.job.SchedulerConfigException;
import purej.job.SchedulerException;
import purej.job.Trigger;
import purej.job.core.SchedulingContext;
import purej.job.spi.ClassLoadHelper;
import purej.job.spi.SchedulerSignaler;
import purej.job.spi.TriggerFiredBundle;

/**
 * <p>
 * <code>JobStoreTX</code> is meant to be used in a standalone environment.
 * Both commit and rollback will be handled by this class.
 * </p>
 * 
 * <p>
 * If you need a <code>{@link purej.job.spi.JobStore}</code>
 * class to use within an application-server environment, use <code>{@link
 * purej.job.impl.jdbcjobstore.JobStoreCMT}</code>
 * instead.
 * </p>
 * 
 * @author <a href="mailto:jeff@binaryfeed.org">Jeffrey Wescott</a>
 * @author James House
 */
public class JobStoreTX extends JobStoreSupport {

    @Override
    public void initialize(ClassLoadHelper loadHelper, SchedulerSignaler signaler) throws SchedulerConfigException {
        super.initialize(loadHelper, signaler);
        getLog().info("JobScheduler store : JobStoreTX initialized.");
    }

    /**
     * <p>
     * Recover any failed or misfired jobs and clean up the data store as
     * appropriate.
     * </p>
     * 
     * @throws JobPersistenceException
     *                 if jobs could not be recovered
     */
    @Override
    protected void recoverJobs() throws JobPersistenceException {
        Connection conn = getConnection();
        boolean transOwner = false;
        try {
            getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
            transOwner = true;
            recoverJobs(conn);
            commitConnection(conn);
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } finally {
            try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
            } finally {
                closeConnection(conn);
            }
        }
    }

    @Override
    protected void cleanVolatileTriggerAndJobs() throws JobPersistenceException {
        Connection conn = getConnection();
        boolean transOwner = false;
        try {
            getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
            transOwner = true;
            cleanVolatileTriggerAndJobs(conn);
            commitConnection(conn);
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } finally {
            try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
            } finally {
                closeConnection(conn);
            }
        }
    }

    /**
     * <p>
     * Store the given <code>{@link purej.job.JobDetail}</code>
     * and <code>{@link purej.job.Trigger}</code>.
     * </p>
     * 
     * @param newJob
     *                The <code>JobDetail</code> to be stored.
     * @param newTrigger
     *                The <code>Trigger</code> to be stored.
     * @throws ObjectAlreadyExistsException
     *                 if a <code>Job</code> with the same name/group already
     *                 exists.
     */
    public void storeJobAndTrigger(SchedulingContext ctxt, JobDetail newJob, Trigger newTrigger) throws ObjectAlreadyExistsException, JobPersistenceException {
        Connection conn = getConnection();
        boolean transOwner = false;
        try {
            if (isLockOnInsert()) {
                getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
                transOwner = true;
            }
            if (newJob.isVolatile() && !newTrigger.isVolatile()) {
                JobPersistenceException jpe = new JobPersistenceException("Cannot associate non-volatile " + "trigger with a volatile job!");
                jpe.setErrorCode(SchedulerException.ERR_CLIENT_ERROR);
                throw jpe;
            }
            storeJob(conn, ctxt, newJob, false);
            storeTrigger(conn, ctxt, newTrigger, newJob, false, Constants.STATE_WAITING, false, false);
            commitConnection(conn);
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } finally {
            try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
            } finally {
                closeConnection(conn);
            }
        }
    }

    /**
     * <p>
     * Store the given <code>{@link purej.job.JobDetail}</code>.
     * </p>
     * 
     * @param newJob
     *                The <code>JobDetail</code> to be stored.
     * @param replaceExisting
     *                If <code>true</code>, any <code>Job</code> existing
     *                in the <code>JobStore</code> with the same name & group
     *                should be over-written.
     * @throws ObjectAlreadyExistsException
     *                 if a <code>Job</code> with the same name/group already
     *                 exists, and replaceExisting is set to false.
     */
    public void storeJob(SchedulingContext ctxt, JobDetail newJob, boolean replaceExisting) throws ObjectAlreadyExistsException, JobPersistenceException {
        Connection conn = getConnection();
        boolean transOwner = false;
        try {
            if (isLockOnInsert() || replaceExisting) {
                getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
                transOwner = true;
            }
            storeJob(conn, ctxt, newJob, replaceExisting);
            commitConnection(conn);
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } finally {
            try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
            } finally {
                closeConnection(conn);
            }
        }
    }

    /**
     * <p>
     * Remove (delete) the <code>{@link purej.job.Job}</code>
     * with the given name, and any
     * <code>{@link purej.job.Trigger}</code> s that reference
     * it.
     * </p>
     * 
     * <p>
     * If removal of the <code>Job</code> results in an empty group, the group
     * should be removed from the <code>JobStore</code>'s list of known group
     * names.
     * </p>
     * 
     * @param jobName
     *                The name of the <code>Job</code> to be removed.
     * @param groupName
     *                The group name of the <code>Job</code> to be removed.
     * @return <code>true</code> if a <code>Job</code> with the given name &
     *         group was found and removed from the store.
     */
    public boolean removeJob(SchedulingContext ctxt, String jobName, String groupName) throws JobPersistenceException {
        Connection conn = getConnection();
        boolean transOwner = false;
        try {
            getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
            transOwner = true;
            boolean removed = removeJob(conn, ctxt, jobName, groupName, true);
            commitConnection(conn);
            return removed;
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } finally {
            try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
            } finally {
                closeConnection(conn);
            }
        }
    }

    /**
     * <p>
     * Retrieve the <code>{@link purej.job.JobDetail}</code> for
     * the given <code>{@link purej.job.Job}</code>.
     * </p>
     * 
     * @param jobName
     *                The name of the <code>Job</code> to be retrieved.
     * @param groupName
     *                The group name of the <code>Job</code> to be retrieved.
     * @return The desired <code>Job</code>, or null if there is no match.
     */
    public JobDetail retrieveJob(SchedulingContext ctxt, String jobName, String groupName) throws JobPersistenceException {
        Connection conn = getConnection();
        try {
            JobDetail job = retrieveJob(conn, ctxt, jobName, groupName);
            commitConnection(conn);
            return job;
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } finally {
            closeConnection(conn);
        }
    }

    /**
     * <p>
     * Store the given <code>{@link purej.job.Trigger}</code>.
     * </p>
     * 
     * @param newTrigger
     *                The <code>Trigger</code> to be stored.
     * @param replaceExisting
     *                If <code>true</code>, any <code>Trigger</code>
     *                existing in the <code>JobStore</code> with the same name &
     *                group should be over-written.
     * @throws ObjectAlreadyExistsException
     *                 if a <code>Trigger</code> with the same name/group
     *                 already exists, and replaceExisting is set to false.
     */
    public void storeTrigger(SchedulingContext ctxt, Trigger newTrigger, boolean replaceExisting) throws ObjectAlreadyExistsException, JobPersistenceException {
        Connection conn = getConnection();
        boolean transOwner = false;
        try {
            if (isLockOnInsert() || replaceExisting) {
                getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
                transOwner = true;
            }
            storeTrigger(conn, ctxt, newTrigger, null, replaceExisting, STATE_WAITING, false, false);
            commitConnection(conn);
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } finally {
            try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
            } finally {
                closeConnection(conn);
            }
        }
    }

    /**
     * <p>
     * Remove (delete) the <code>{@link purej.job.Trigger}</code>
     * with the given name.
     * </p>
     * 
     * <p>
     * If removal of the <code>Trigger</code> results in an empty group, the
     * group should be removed from the <code>JobStore</code>'s list of known
     * group names.
     * </p>
     * 
     * <p>
     * If removal of the <code>Trigger</code> results in an 'orphaned'
     * <code>Job</code> that is not 'durable', then the <code>Job</code>
     * should be deleted also.
     * </p>
     * 
     * @param triggerName
     *                The name of the <code>Trigger</code> to be removed.
     * @param groupName
     *                The group name of the <code>Trigger</code> to be
     *                removed.
     * @return <code>true</code> if a <code>Trigger</code> with the given
     *         name & group was found and removed from the store.
     */
    public boolean removeTrigger(SchedulingContext ctxt, String triggerName, String groupName) throws JobPersistenceException {
        Connection conn = getConnection();
        boolean transOwner = false;
        try {
            getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
            transOwner = true;
            boolean removed = removeTrigger(conn, ctxt, triggerName, groupName);
            commitConnection(conn);
            return removed;
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } finally {
            try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
            } finally {
                closeConnection(conn);
            }
        }
    }

    /**
     * @see purej.job.spi.JobStore#replaceTrigger(com.legata.soaf.job.core.SchedulingContext,
     *      java.lang.String, java.lang.String, org.quartz.Trigger)
     */
    public boolean replaceTrigger(SchedulingContext ctxt, String triggerName, String groupName, Trigger newTrigger) throws JobPersistenceException {
        Connection conn = getConnection();
        boolean transOwner = false;
        try {
            getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
            transOwner = true;
            boolean removed = replaceTrigger(conn, ctxt, triggerName, groupName, newTrigger);
            commitConnection(conn);
            return removed;
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } finally {
            try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
            } finally {
                closeConnection(conn);
            }
        }
    }

    /**
     * <p>
     * Retrieve the given <code>{@link purej.job.Trigger}</code>.
     * </p>
     * 
     * @param triggerName
     *                The name of the <code>Trigger</code> to be retrieved.
     * @param groupName
     *                The group name of the <code>Trigger</code> to be
     *                retrieved.
     * @return The desired <code>Trigger</code>, or null if there is no
     *         match.
     */
    public Trigger retrieveTrigger(SchedulingContext ctxt, String triggerName, String groupName) throws JobPersistenceException {
        Connection conn = getConnection();
        try {
            Trigger trigger = retrieveTrigger(conn, ctxt, triggerName, groupName);
            commitConnection(conn);
            return trigger;
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } finally {
            closeConnection(conn);
        }
    }

    /**
     * <p>
     * Store the given <code>{@link purej.job.Calendar}</code>.
     * </p>
     * 
     * @param calName
     *                The name of the calendar.
     * @param calendar
     *                The <code>Calendar</code> to be stored.
     * @param replaceExisting
     *                If <code>true</code>, any <code>Calendar</code>
     *                existing in the <code>JobStore</code> with the same name &
     *                group should be over-written.
     * @throws ObjectAlreadyExistsException
     *                 if a <code>Calendar</code> with the same name already
     *                 exists, and replaceExisting is set to false.
     */
    public void storeCalendar(SchedulingContext ctxt, String calName, Calendar calendar, boolean replaceExisting, boolean updateTriggers) throws ObjectAlreadyExistsException, JobPersistenceException {
        Connection conn = getConnection();
        boolean lockOwner = false;
        try {
            if (isLockOnInsert() || updateTriggers) {
                getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
                lockOwner = true;
            }
            storeCalendar(conn, ctxt, calName, calendar, replaceExisting, updateTriggers);
            commitConnection(conn);
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } finally {
            try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, lockOwner);
            } finally {
                closeConnection(conn);
            }
        }
    }

    /**
     * <p>
     * Remove (delete) the <code>{@link purej.job.Calendar}</code>
     * with the given name.
     * </p>
     * 
     * <p>
     * If removal of the <code>Calendar</code> would result in <code.Trigger</code>s
     * pointing to non-existent calendars, then a <code>JobPersistenceException</code>
     * will be thrown.
     * </p> *
     * 
     * @param calName
     *                The name of the <code>Calendar</code> to be removed.
     * @return <code>true</code> if a <code>Calendar</code> with the given
     *         name was found and removed from the store.
     */
    public boolean removeCalendar(SchedulingContext ctxt, String calName) throws JobPersistenceException {
        Connection conn = getConnection();
        boolean lockOwner = false;
        try {
            getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
            lockOwner = true;
            boolean removed = removeCalendar(conn, ctxt, calName);
            commitConnection(conn);
            return removed;
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } finally {
            try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, lockOwner);
            } finally {
                closeConnection(conn);
            }
        }
    }

    /**
     * <p>
     * Retrieve the given <code>{@link purej.job.Trigger}</code>.
     * </p>
     * 
     * @param calName
     *                The name of the <code>Calendar</code> to be retrieved.
     * @return The desired <code>Calendar</code>, or null if there is no
     *         match.
     */
    public Calendar retrieveCalendar(SchedulingContext ctxt, String calName) throws JobPersistenceException {
        Connection conn = getConnection();
        try {
            Calendar cal = retrieveCalendar(conn, ctxt, calName);
            commitConnection(conn);
            return cal;
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } finally {
            closeConnection(conn);
        }
    }

    /**
     * <p>
     * Get the number of <code>{@link purej.job.Job}</code> s
     * that are stored in the <code>JobStore</code>.
     * </p>
     */
    public int getNumberOfJobs(SchedulingContext ctxt) throws JobPersistenceException {
        Connection conn = getConnection();
        try {
            int numJobs = getNumberOfJobs(conn, ctxt);
            commitConnection(conn);
            return numJobs;
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } finally {
            closeConnection(conn);
        }
    }

    /**
     * <p>
     * Get the number of <code>{@link purej.job.Trigger}</code> s
     * that are stored in the <code>JobsStore</code>.
     * </p>
     */
    public int getNumberOfTriggers(SchedulingContext ctxt) throws JobPersistenceException {
        Connection conn = getConnection();
        try {
            int numTriggers = getNumberOfTriggers(conn, ctxt);
            commitConnection(conn);
            return numTriggers;
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } finally {
            closeConnection(conn);
        }
    }

    /**
     * <p>
     * Get the number of <code>{@link purej.job.Calendar}</code>
     * s that are stored in the <code>JobsStore</code>.
     * </p>
     */
    public int getNumberOfCalendars(SchedulingContext ctxt) throws JobPersistenceException {
        Connection conn = getConnection();
        try {
            int numCals = getNumberOfCalendars(conn, ctxt);
            commitConnection(conn);
            return numCals;
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } finally {
            closeConnection(conn);
        }
    }

    public Set<String> getPausedTriggerGroups(SchedulingContext ctxt) throws JobPersistenceException {
        Connection conn = getConnection();
        try {
            Set<String> groups = getPausedTriggerGroups(conn, ctxt);
            commitConnection(conn);
            return groups;
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } finally {
            closeConnection(conn);
        }
    }

    /**
     * <p>
     * Get the names of all of the
     * <code>{@link purej.job.Job}</code> s that have the given
     * group name.
     * </p>
     * 
     * <p>
     * If there are no jobs in the given group name, the result should be a
     * zero-length array (not <code>null</code>).
     * </p>
     */
    public String[] getJobNames(SchedulingContext ctxt, String groupName) throws JobPersistenceException {
        Connection conn = getConnection();
        try {
            String[] jobNames = getJobNames(conn, ctxt, groupName);
            commitConnection(conn);
            return jobNames;
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } finally {
            closeConnection(conn);
        }
    }

    /**
     * <p>
     * Get the names of all of the
     * <code>{@link purej.job.Trigger}</code> s that have the
     * given group name.
     * </p>
     * 
     * <p>
     * If there are no triggers in the given group name, the result should be a
     * zero-length array (not <code>null</code>).
     * </p>
     */
    public String[] getTriggerNames(SchedulingContext ctxt, String groupName) throws JobPersistenceException {
        Connection conn = getConnection();
        try {
            String[] triggerNames = getTriggerNames(conn, ctxt, groupName);
            commitConnection(conn);
            return triggerNames;
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } finally {
            closeConnection(conn);
        }
    }

    /**
     * <p>
     * Get the names of all of the
     * <code>{@link purej.job.Job}</code> groups.
     * </p>
     * 
     * <p>
     * If there are no known group names, the result should be a zero-length
     * array (not <code>null</code>).
     * </p>
     */
    public String[] getJobGroupNames(SchedulingContext ctxt) throws JobPersistenceException {
        Connection conn = getConnection();
        try {
            String[] groupNames = getJobGroupNames(conn, ctxt);
            commitConnection(conn);
            return groupNames;
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } finally {
            closeConnection(conn);
        }
    }

    /**
     * <p>
     * Get the names of all of the
     * <code>{@link purej.job.Trigger}</code> groups.
     * </p>
     * 
     * <p>
     * If there are no known group names, the result should be a zero-length
     * array (not <code>null</code>).
     * </p>
     */
    public String[] getTriggerGroupNames(SchedulingContext ctxt) throws JobPersistenceException {
        Connection conn = getConnection();
        try {
            String[] triggerGroups = getTriggerGroupNames(conn, ctxt);
            commitConnection(conn);
            return triggerGroups;
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } finally {
            closeConnection(conn);
        }
    }

    /**
     * <p>
     * Get the names of all of the
     * <code>{@link purej.job.Calendar}</code> s in the
     * <code>JobStore</code>.
     * </p>
     * 
     * <p>
     * If there are no Calendars in the given group name, the result should be a
     * zero-length array (not <code>null</code>).
     * </p>
     */
    public String[] getCalendarNames(SchedulingContext ctxt) throws JobPersistenceException {
        Connection conn = getConnection();
        try {
            String[] calNames = getCalendarNames(conn, ctxt);
            commitConnection(conn);
            return calNames;
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } finally {
            closeConnection(conn);
        }
    }

    /**
     * <p>
     * Get all of the Triggers that are associated to the given Job.
     * </p>
     * 
     * <p>
     * If there are no matches, a zero-length array should be returned.
     * </p>
     */
    public Trigger[] getTriggersForJob(SchedulingContext ctxt, String jobName, String groupName) throws JobPersistenceException {
        Connection conn = getConnection();
        try {
            return getTriggersForJob(conn, ctxt, jobName, groupName);
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } finally {
            closeConnection(conn);
        }
    }

    /**
     * <p>
     * Get the current state of the identified <code>{@link Trigger}</code>.
     * </p>
     * 
     * @see Trigger#STATE_NORMAL
     * @see Trigger#STATE_PAUSED
     * @see Trigger#STATE_COMPLETE
     * @see Trigger#STATE_ERROR
     * @see Trigger#STATE_NONE
     */
    public int getTriggerState(SchedulingContext ctxt, String triggerName, String groupName) throws JobPersistenceException {
        Connection conn = getConnection();
        try {
            return getTriggerState(conn, ctxt, triggerName, groupName);
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } finally {
            closeConnection(conn);
        }
    }

    /**
     * <p>
     * Pause the <code>{@link purej.job.Trigger}</code> with the
     * given name.
     * </p>
     * 
     * @see #resumeTrigger(SchedulingContext, String, String)
     */
    public void pauseTrigger(SchedulingContext ctxt, String triggerName, String groupName) throws JobPersistenceException {
        Connection conn = getConnection();
        boolean transOwner = false;
        try {
            getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
            transOwner = true;
            pauseTrigger(conn, ctxt, triggerName, groupName);
            commitConnection(conn);
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } finally {
            try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
            } finally {
                closeConnection(conn);
            }
        }
    }

    /**
     * <p>
     * Pause all of the <code>{@link purej.job.Trigger}s</code>
     * in the given group.
     * </p>
     * 
     * @see #resumeTriggerGroup(SchedulingContext, String)
     */
    public void pauseTriggerGroup(SchedulingContext ctxt, String groupName) throws JobPersistenceException {
        Connection conn = getConnection();
        boolean transOwner = false;
        try {
            getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
            transOwner = true;
            pauseTriggerGroup(conn, ctxt, groupName);
            commitConnection(conn);
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } finally {
            try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
            } finally {
                closeConnection(conn);
            }
        }
    }

    /**
     * <p>
     * Pause the <code>{@link purej.job.Job}</code> with the
     * given name - by pausing all of its current <code>Trigger</code>s.
     * </p>
     * 
     * @see #resumeJob(SchedulingContext, String, String)
     */
    public void pauseJob(SchedulingContext ctxt, String jobName, String groupName) throws JobPersistenceException {
        Connection conn = getConnection();
        boolean transOwner = false;
        try {
            getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
            transOwner = true;
            Trigger[] triggers = getTriggersForJob(conn, ctxt, jobName, groupName);
            for (int j = 0; j < triggers.length; j++) {
                pauseTrigger(conn, ctxt, triggers[j].getName(), triggers[j].getGroup());
            }
            commitConnection(conn);
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } finally {
            try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
            } finally {
                closeConnection(conn);
            }
        }
    }

    /**
     * <p>
     * Pause all of the <code>{@link purej.job.Job}s</code> in
     * the given group - by pausing all of their <code>Trigger</code>s.
     * </p>
     * 
     * @see #resumeJobGroup(SchedulingContext, String)
     */
    public void pauseJobGroup(SchedulingContext ctxt, String groupName) throws JobPersistenceException {
        Connection conn = getConnection();
        boolean transOwner = false;
        try {
            getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
            transOwner = true;
            String[] jobNames = getJobNames(conn, ctxt, groupName);
            for (int i = 0; i < jobNames.length; i++) {
                Trigger[] triggers = getTriggersForJob(conn, ctxt, jobNames[i], groupName);
                for (int j = 0; j < triggers.length; j++) {
                    pauseTrigger(conn, ctxt, triggers[j].getName(), triggers[j].getGroup());
                }
            }
            commitConnection(conn);
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } finally {
            try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
            } finally {
                closeConnection(conn);
            }
        }
    }

    /**
     * <p>
     * Resume (un-pause) the <code>{@link purej.job.Trigger}</code>
     * with the given name.
     * </p>
     * 
     * <p>
     * If the <code>Trigger</code> missed one or more fire-times, then the
     * <code>Trigger</code>'s misfire instruction will be applied.
     * </p>
     * 
     * @see #pauseTrigger(SchedulingContext, String, String)
     */
    public void resumeTrigger(SchedulingContext ctxt, String triggerName, String groupName) throws JobPersistenceException {
        Connection conn = getConnection();
        boolean transOwner = false;
        try {
            getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
            transOwner = true;
            resumeTrigger(conn, ctxt, triggerName, groupName);
            commitConnection(conn);
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } finally {
            try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
            } finally {
                closeConnection(conn);
            }
        }
    }

    /**
     * <p>
     * Resume (un-pause) all of the
     * <code>{@link purej.job.Trigger}s</code> in the given
     * group.
     * </p>
     * 
     * <p>
     * If any <code>Trigger</code> missed one or more fire-times, then the
     * <code>Trigger</code>'s misfire instruction will be applied.
     * </p>
     * 
     * @see #pauseTriggerGroup(SchedulingContext, String)
     */
    public void resumeTriggerGroup(SchedulingContext ctxt, String groupName) throws JobPersistenceException {
        Connection conn = getConnection();
        boolean transOwner = false;
        try {
            getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
            transOwner = true;
            resumeTriggerGroup(conn, ctxt, groupName);
            commitConnection(conn);
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } finally {
            try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
            } finally {
                closeConnection(conn);
            }
        }
    }

    /**
     * <p>
     * Resume (un-pause) the <code>{@link purej.job.Job}</code>
     * with the given name.
     * </p>
     * 
     * <p>
     * If any of the <code>Job</code>'s<code>Trigger</code> s missed one
     * or more fire-times, then the <code>Trigger</code>'s misfire
     * instruction will be applied.
     * </p>
     * 
     * @see #pauseJob(SchedulingContext, String, String)
     */
    public void resumeJob(SchedulingContext ctxt, String jobName, String groupName) throws JobPersistenceException {
        Connection conn = getConnection();
        boolean transOwner = false;
        try {
            getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
            transOwner = true;
            Trigger[] triggers = getTriggersForJob(conn, ctxt, jobName, groupName);
            for (int j = 0; j < triggers.length; j++) {
                resumeTrigger(conn, ctxt, triggers[j].getName(), triggers[j].getGroup());
            }
            commitConnection(conn);
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } finally {
            try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
            } finally {
                closeConnection(conn);
            }
        }
    }

    /**
     * <p>
     * Resume (un-pause) all of the
     * <code>{@link purej.job.Job}s</code> in the given group.
     * </p>
     * 
     * <p>
     * If any of the <code>Job</code> s had <code>Trigger</code> s that
     * missed one or more fire-times, then the <code>Trigger</code>'s misfire
     * instruction will be applied.
     * </p>
     * 
     * @see #pauseJobGroup(SchedulingContext, String)
     */
    public void resumeJobGroup(SchedulingContext ctxt, String groupName) throws JobPersistenceException {
        Connection conn = getConnection();
        boolean transOwner = false;
        try {
            getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
            transOwner = true;
            String[] jobNames = getJobNames(conn, ctxt, groupName);
            for (int i = 0; i < jobNames.length; i++) {
                Trigger[] triggers = getTriggersForJob(conn, ctxt, jobNames[i], groupName);
                for (int j = 0; j < triggers.length; j++) {
                    resumeTrigger(conn, ctxt, triggers[j].getName(), triggers[j].getGroup());
                }
            }
            commitConnection(conn);
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } finally {
            try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
            } finally {
                closeConnection(conn);
            }
        }
    }

    /**
     * <p>
     * Pause all triggers - equivalent of calling
     * <code>pauseTriggerGroup(group)</code> on every group.
     * </p>
     * 
     * <p>
     * When <code>resumeAll()</code> is called (to un-pause), trigger misfire
     * instructions WILL be applied.
     * </p>
     * 
     * @see #resumeAll(SchedulingContext)
     * @see #pauseTriggerGroup(SchedulingContext, String)
     */
    public void pauseAll(SchedulingContext ctxt) throws JobPersistenceException {
        Connection conn = getConnection();
        boolean transOwner = false;
        try {
            getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
            transOwner = true;
            pauseAll(conn, ctxt);
            commitConnection(conn);
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } finally {
            try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
            } finally {
                closeConnection(conn);
            }
        }
    }

    /**
     * <p>
     * Resume (un-pause) all triggers - equivalent of calling
     * <code>resumeTriggerGroup(group)</code> on every group.
     * </p>
     * 
     * <p>
     * If any <code>Trigger</code> missed one or more fire-times, then the
     * <code>Trigger</code>'s misfire instruction will be applied.
     * </p>
     * 
     * @see #pauseAll(SchedulingContext)
     */
    public void resumeAll(SchedulingContext ctxt) throws JobPersistenceException {
        Connection conn = getConnection();
        boolean transOwner = false;
        try {
            getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
            transOwner = true;
            resumeAll(conn, ctxt);
            commitConnection(conn);
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } finally {
            try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
            } finally {
                closeConnection(conn);
            }
        }
    }

    /**
     * <p>
     * Get a handle to the next trigger to be fired, and mark it as 'reserved'
     * by the calling scheduler.
     * </p>
     * 
     * @see #releaseAcquiredTrigger(SchedulingContext, Trigger)
     */
    public Trigger acquireNextTrigger(SchedulingContext ctxt, long noLaterThan) throws JobPersistenceException {
        Connection conn = getConnection();
        boolean transOwner = false;
        try {
            getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
            transOwner = true;
            Trigger trigger = acquireNextTrigger(conn, ctxt, noLaterThan);
            commitConnection(conn);
            return trigger;
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } finally {
            try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
            } finally {
                closeConnection(conn);
            }
        }
    }

    /**
     * <p>
     * Inform the <code>JobStore</code> that the scheduler no longer plans to
     * fire the given <code>Trigger</code>, that it had previously acquired
     * (reserved).
     * </p>
     */
    public void releaseAcquiredTrigger(SchedulingContext ctxt, Trigger trigger) throws JobPersistenceException {
        Connection conn = getConnection();
        boolean transOwner = false;
        try {
            getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
            transOwner = true;
            releaseAcquiredTrigger(conn, ctxt, trigger);
            commitConnection(conn);
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } finally {
            try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
            } finally {
                closeConnection(conn);
            }
        }
    }

    /**
     * <p>
     * Inform the <code>JobStore</code> that the scheduler is now firing the
     * given <code>Trigger</code> (executing its associated <code>Job</code>),
     * that it had previously acquired (reserved).
     * </p>
     * 
     * @return null if the trigger or it's job or calendar no longer exist, or
     *         if the trigger was not successfully put into the 'executing'
     *         state.
     */
    public TriggerFiredBundle triggerFired(SchedulingContext ctxt, Trigger trigger) throws JobPersistenceException {
        Connection conn = getConnection();
        boolean transOwner = false;
        try {
            getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
            transOwner = true;
            TriggerFiredBundle tfb = null;
            JobPersistenceException err = null;
            try {
                tfb = triggerFired(conn, ctxt, trigger);
            } catch (JobPersistenceException jpe) {
                if (jpe.getErrorCode() != SchedulerException.ERR_PERSISTENCE_JOB_DOES_NOT_EXIST) throw jpe;
                err = jpe;
            }
            commitConnection(conn);
            if (err != null) throw err;
            return tfb;
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } finally {
            try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
            } finally {
                closeConnection(conn);
            }
        }
    }

    /**
     * <p>
     * Inform the <code>JobStore</code> that the scheduler has completed the
     * firing of the given <code>Trigger</code> (and the execution its
     * associated <code>Job</code>), and that the
     * <code>{@link purej.job.JobDataMap}</code> in the given
     * <code>JobDetail</code> should be updated if the <code>Job</code> is
     * stateful.
     * </p>
     */
    public void triggeredJobComplete(SchedulingContext ctxt, Trigger trigger, JobDetail jobDetail, int triggerInstCode) throws JobPersistenceException {
        Connection conn = getConnection();
        boolean transOwner = false;
        try {
            getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
            transOwner = true;
            triggeredJobComplete(conn, ctxt, trigger, jobDetail, triggerInstCode);
            commitConnection(conn);
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } finally {
            try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
            } finally {
                closeConnection(conn);
            }
        }
    }

    @Override
    protected boolean doRecoverMisfires() throws JobPersistenceException {
        Connection conn = getConnection();
        boolean transOwner = false;
        boolean moreToDo = false;
        try {
            getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
            transOwner = true;
            try {
                moreToDo = recoverMisfiredJobs(conn, false);
            } catch (Exception e) {
                throw new JobPersistenceException(e.getMessage(), e);
            }
            commitConnection(conn);
            return moreToDo;
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } finally {
            try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
            } finally {
                closeConnection(conn);
            }
        }
    }

    @Override
    protected boolean doCheckin() throws JobPersistenceException {
        Connection conn = getConnection();
        boolean transOwner = false;
        boolean transStateOwner = false;
        boolean recovered = false;
        try {
            List<?> failedRecords = (firstCheckIn) ? null : clusterCheckIn(conn);
            if (firstCheckIn || (failedRecords.size() > 0)) {
                getLockHandler().obtainLock(conn, LOCK_STATE_ACCESS);
                transStateOwner = true;
                failedRecords = (firstCheckIn) ? clusterCheckIn(conn) : findFailedInstances(conn);
                if (failedRecords.size() > 0) {
                    getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
                    transOwner = true;
                    clusterRecover(conn, failedRecords);
                    recovered = true;
                }
            }
            commitConnection(conn);
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            throw e;
        } finally {
            try {
                releaseLock(conn, LOCK_TRIGGER_ACCESS, transOwner);
            } finally {
                try {
                    releaseLock(conn, LOCK_STATE_ACCESS, transStateOwner);
                } finally {
                    closeConnection(conn);
                }
            }
        }
        firstCheckIn = false;
        return recovered;
    }
}
