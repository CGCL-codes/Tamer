package org.opencms.scheduler;

import org.opencms.file.CmsObject;
import org.opencms.i18n.CmsMessageContainer;
import org.opencms.main.CmsIllegalArgumentException;
import org.opencms.main.CmsLog;
import org.opencms.main.OpenCms;
import org.opencms.security.CmsRole;
import org.opencms.security.CmsRoleViolationException;
import org.opencms.util.CmsStringUtil;
import org.opencms.util.CmsUUID;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Manages the OpenCms scheduled jobs.<p>
 * 
 * Please see the documentation of the class {@link org.opencms.scheduler.CmsScheduledJobInfo} 
 * for a full description of the OpenCms scheduling capabilities.<p>
 * 
 * The OpenCms scheduler implementation internally uses the
 * <a href="http://www.opensymphony.com/quartz/">Quartz scheduler</a> from
 * the <a href="http://www.opensymphony.com/">OpenSymphony project</a>.<p>
 * 
 * This manager class implements the <code>org.quartz.Job</code> interface
 * and wraps all calls to the {@link org.opencms.scheduler.I_CmsScheduledJob} implementing 
 * classes.<p>
 * 
 * @author Alexander Kandzior 
 *  
 * @version $Revision: 1.34 $ 
 * 
 * @since 6.0.0 
 * 
 * @see org.opencms.scheduler.CmsScheduledJobInfo
 */
public class CmsScheduleManager implements Job {

    /** Key for the scheduled job description in the job data map. */
    public static final String SCHEDULER_JOB_INFO = "org.opencms.scheduler.CmsScheduledJobInfo";

    /** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(CmsScheduleManager.class);

    /** The Admin context used for creation of users for the individual jobs. */
    private CmsObject m_adminCms;

    /** The list of job entries from the configuration. */
    private List m_configuredJobs;

    /** The list of scheduled jobs. */
    private List m_jobs;

    /** The initialized scheduler. */
    private Scheduler m_scheduler;

    /**
     * Default constructor for the scheduler manager, 
     * used only when a new job is scheduled.<p>
     */
    public CmsScheduleManager() {
    }

    /**
     * Used by the configuration to create a new Scheduler during system startup.<p>
     * 
     * @param configuredJobs the jobs from the configuration
     */
    public CmsScheduleManager(List configuredJobs) {
        m_configuredJobs = configuredJobs;
        int size = 0;
        if (m_configuredJobs != null) {
            size = m_configuredJobs.size();
        }
        if (CmsLog.INIT.isInfoEnabled()) {
            CmsLog.INIT.info(Messages.get().getBundle().key(Messages.INIT_SCHEDULER_CREATED_1, new Integer(size)));
        }
    }

    /**
     * Implementation of the Quartz job interface.<p>
     * 
     * The architecture is that this scheduler manager generates
     * a new (empty) instance of itself for every OpenCms job scheduled with Quartz. 
     * When the Quartz job is executed, the configured 
     * implementation of {@link I_CmsScheduledJob} will be called from this method.<p>
     * 
     * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
     */
    public void execute(JobExecutionContext context) {
        JobDataMap jobData = context.getJobDetail().getJobDataMap();
        CmsScheduledJobInfo jobInfo = (CmsScheduledJobInfo) jobData.get(SCHEDULER_JOB_INFO);
        if (jobInfo == null) {
            LOG.error(Messages.get().getBundle().key(Messages.LOG_INVALID_JOB_1, context.getJobDetail().getFullName()));
            return;
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.get().getBundle().key(Messages.LOG_JOB_STARTING_1, jobInfo.getJobName()));
        }
        I_CmsScheduledJob job = jobInfo.getJobInstance();
        if (job != null) {
            try {
                CmsObject cms = null;
                jobInfo.updateContextRequestTime();
                if (OpenCms.getRunLevel() >= OpenCms.RUNLEVEL_3_SHELL_ACCESS) {
                    cms = OpenCms.initCmsObject(OpenCms.getScheduleManager().getAdminCms(), jobInfo.getContextInfo());
                }
                String result = job.launch(cms, jobInfo.getParameters());
                if (CmsStringUtil.isNotEmpty(result) && LOG.isInfoEnabled()) {
                    LOG.info(Messages.get().getBundle().key(Messages.LOG_JOB_EXECUTION_OK_2, jobInfo.getJobName(), result));
                }
            } catch (Throwable t) {
                LOG.error(Messages.get().getBundle().key(Messages.LOG_JOB_EXECUTION_ERROR_1, jobInfo.getJobName()), t);
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.get().getBundle().key(Messages.LOG_JOB_EXECUTED_1, jobInfo.getJobName()));
            Date nextExecution = jobInfo.getExecutionTimeNext();
            if (nextExecution != null) {
                LOG.info(Messages.get().getBundle().key(Messages.LOG_JOB_NEXT_EXECUTION_2, jobInfo.getJobName(), nextExecution));
            }
        }
    }

    /**
     * Returns the currently scheduled job description identified by the given id.
     * 
     * @param id the job id
     * 
     * @return a job or <code>null</code> if not found
     */
    public CmsScheduledJobInfo getJob(String id) {
        Iterator it = m_jobs.iterator();
        while (it.hasNext()) {
            CmsScheduledJobInfo job = (CmsScheduledJobInfo) it.next();
            if (job.getId().equals(id)) {
                return job;
            }
        }
        return null;
    }

    /**
     * Returns the currently scheduled job descriptions in an unmodifiable list.<p>
     *
     * The objects in the List are of type <code>{@link CmsScheduledJobInfo}</code>.<p>
     *
     * @return the currently scheduled job descriptions in an unmodifiable list
     */
    public List getJobs() {
        return Collections.unmodifiableList(m_jobs);
    }

    /**
     * Initializes the OpenCms scheduler.<p> 
     * 
     * @param adminCms an OpenCms context object that must have been initialized with "Admin" permissions
     * 
     * @throws CmsRoleViolationException if the user has insufficient role permissions
     */
    public synchronized void initialize(CmsObject adminCms) throws CmsRoleViolationException {
        if (OpenCms.getRunLevel() > OpenCms.RUNLEVEL_1_CORE_OBJECT) {
            OpenCms.getRoleManager().checkRole(adminCms, CmsRole.WORKPLACE_MANAGER);
        }
        m_jobs = new ArrayList();
        m_adminCms = adminCms;
        Properties properties = new Properties();
        properties.put(StdSchedulerFactory.PROP_SCHED_INSTANCE_NAME, "OpenCmsScheduler");
        properties.put(StdSchedulerFactory.PROP_SCHED_THREAD_NAME, "OpenCms: Scheduler");
        properties.put(StdSchedulerFactory.PROP_SCHED_RMI_EXPORT, CmsStringUtil.FALSE);
        properties.put(StdSchedulerFactory.PROP_SCHED_RMI_PROXY, CmsStringUtil.FALSE);
        properties.put(StdSchedulerFactory.PROP_THREAD_POOL_CLASS, CmsSchedulerThreadPool.class.getName());
        properties.put(StdSchedulerFactory.PROP_JOB_STORE_CLASS, "org.quartz.simpl.RAMJobStore");
        try {
            SchedulerFactory schedulerFactory = new StdSchedulerFactory(properties);
            m_scheduler = schedulerFactory.getScheduler();
        } catch (Exception e) {
            LOG.error(Messages.get().getBundle().key(Messages.LOG_NO_SCHEDULER_0), e);
            m_scheduler = null;
            return;
        }
        if (CmsLog.INIT.isInfoEnabled()) {
            CmsLog.INIT.info(Messages.get().getBundle().key(Messages.INIT_SCHEDULER_INITIALIZED_0));
        }
        if (m_configuredJobs != null) {
            for (int i = 0; i < m_configuredJobs.size(); i++) {
                try {
                    CmsScheduledJobInfo job = (CmsScheduledJobInfo) m_configuredJobs.get(i);
                    scheduleJob(adminCms, job);
                } catch (CmsSchedulerException e) {
                }
            }
        }
        try {
            m_scheduler.start();
        } catch (Exception e) {
            LOG.error(Messages.get().getBundle().key(Messages.LOG_CANNOT_START_SCHEDULER_0), e);
            m_scheduler = null;
            return;
        }
        if (CmsLog.INIT.isInfoEnabled()) {
            CmsLog.INIT.info(Messages.get().getBundle().key(Messages.INIT_SCHEDULER_STARTED_0));
            CmsLog.INIT.info(Messages.get().getBundle().key(Messages.INIT_SCHEDULER_CONFIG_FINISHED_0));
        }
    }

    /**
     * Adds a new job to the scheduler.<p>
     *  
     * @param cms an OpenCms context object that must have been initialized with "Admin" permissions
     * @param jobInfo the job info describing the job to schedule
     * 
     * @throws CmsRoleViolationException if the user has insufficient role permissions
     * @throws CmsSchedulerException if the job could not be scheduled for any reason
     */
    public synchronized void scheduleJob(CmsObject cms, CmsScheduledJobInfo jobInfo) throws CmsRoleViolationException, CmsSchedulerException {
        if (OpenCms.getRunLevel() > OpenCms.RUNLEVEL_1_CORE_OBJECT) {
            OpenCms.getRoleManager().checkRole(cms, CmsRole.WORKPLACE_MANAGER);
        }
        if ((jobInfo == null) || (jobInfo.getClassName() == null)) {
            CmsMessageContainer message = Messages.get().container(Messages.ERR_INVALID_JOB_CONFIGURATION_0);
            LOG.error(message.key());
            throw new CmsSchedulerException(message);
        }
        if (m_scheduler == null) {
            CmsMessageContainer message = Messages.get().container(Messages.ERR_NO_SCHEDULER_1, jobInfo.getJobName());
            LOG.error(message.key());
            throw new CmsSchedulerException(message);
        }
        Class jobClass;
        try {
            jobClass = Class.forName(jobInfo.getClassName());
            if (!I_CmsScheduledJob.class.isAssignableFrom(jobClass)) {
                CmsMessageContainer message = Messages.get().container(Messages.ERR_JOB_CLASS_BAD_INTERFACE_2, jobInfo.getClassName(), I_CmsScheduledJob.class.getName());
                LOG.error(message.key());
                if (OpenCms.getRunLevel() > OpenCms.RUNLEVEL_2_INITIALIZING) {
                    throw new CmsIllegalArgumentException(message);
                } else {
                    jobInfo.setActive(false);
                }
            }
        } catch (ClassNotFoundException e) {
            CmsMessageContainer message = Messages.get().container(Messages.ERR_JOB_CLASS_NOT_FOUND_1, jobInfo.getClassName());
            LOG.error(message.key());
            if (OpenCms.getRunLevel() > OpenCms.RUNLEVEL_2_INITIALIZING) {
                throw new CmsIllegalArgumentException(message);
            } else {
                jobInfo.setActive(false);
            }
        }
        String jobId = jobInfo.getId();
        boolean idCreated = false;
        if (jobId == null) {
            CmsUUID jobUUID = new CmsUUID();
            jobId = "OpenCmsJob_".concat(jobUUID.toString());
            jobInfo.setId(jobId);
            idCreated = true;
        }
        CronTrigger trigger = new CronTrigger(jobId, Scheduler.DEFAULT_GROUP);
        try {
            trigger.setCronExpression(jobInfo.getCronExpression());
        } catch (ParseException e) {
            if (idCreated) {
                jobInfo.setId(null);
            }
            CmsMessageContainer message = Messages.get().container(Messages.ERR_BAD_CRON_EXPRESSION_2, jobInfo.getJobName(), jobInfo.getCronExpression());
            LOG.error(message.key());
            throw new CmsSchedulerException(message);
        }
        CmsScheduledJobInfo oldJob = null;
        if (!idCreated) {
            oldJob = unscheduleJob(cms, jobId);
            if (oldJob == null) {
                CmsMessageContainer message = Messages.get().container(Messages.ERR_JOB_WITH_ID_DOES_NOT_EXIST_1, jobId);
                LOG.warn(message.key());
                throw new CmsSchedulerException(message);
            }
            jobInfo.setFrozen(false);
        }
        if (jobInfo.isActive()) {
            JobDetail jobDetail = new JobDetail(jobInfo.getId(), Scheduler.DEFAULT_GROUP, CmsScheduleManager.class);
            jobInfo.setTrigger(trigger);
            JobDataMap jobData = new JobDataMap();
            jobData.put(CmsScheduleManager.SCHEDULER_JOB_INFO, jobInfo);
            jobDetail.setJobDataMap(jobData);
            try {
                m_scheduler.scheduleJob(jobDetail, trigger);
                if (LOG.isInfoEnabled()) {
                    LOG.info(Messages.get().getBundle().key(Messages.LOG_JOB_SCHEDULED_4, new Object[] { new Integer(m_jobs.size()), jobInfo.getJobName(), jobInfo.getClassName(), jobInfo.getContextInfo().getUserName() }));
                    Date nextExecution = jobInfo.getExecutionTimeNext();
                    if (nextExecution != null) {
                        LOG.info(Messages.get().getBundle().key(Messages.LOG_JOB_NEXT_EXECUTION_2, jobInfo.getJobName(), nextExecution));
                    }
                }
            } catch (Exception e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(e.getMessage(), e);
                }
                if (idCreated) {
                    jobInfo.setId(null);
                }
                CmsMessageContainer message = Messages.get().container(Messages.ERR_COULD_NOT_SCHEDULE_JOB_2, jobInfo.getJobName(), jobInfo.getClassName());
                if (oldJob != null) {
                    jobDetail = new JobDetail(oldJob.getId(), Scheduler.DEFAULT_GROUP, CmsScheduleManager.class);
                    jobDetail.setJobDataMap(jobData);
                    try {
                        m_scheduler.scheduleJob(jobDetail, oldJob.getTrigger());
                        m_jobs.add(oldJob);
                    } catch (SchedulerException e2) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug(e2.getMessage(), e2);
                        }
                        message = Messages.get().container(Messages.ERR_COULD_NOT_RESCHEDULE_JOB_2, jobInfo.getJobName(), jobInfo.getClassName());
                    }
                }
                if (LOG.isWarnEnabled()) {
                    LOG.warn(message.key());
                }
                throw new CmsSchedulerException(message);
            }
        }
        jobInfo.initConfiguration();
        m_jobs.add(jobInfo);
    }

    /** 
     * Shuts down this instance of the OpenCms scheduler manager.<p>
     */
    public synchronized void shutDown() {
        m_adminCms = null;
        if (CmsLog.INIT.isInfoEnabled()) {
            CmsLog.INIT.info(Messages.get().getBundle().key(Messages.INIT_SHUTDOWN_1, this.getClass().getName()));
        }
        if (m_scheduler != null) {
            try {
                m_scheduler.shutdown();
            } catch (SchedulerException e) {
                LOG.error(Messages.get().getBundle().key(Messages.LOG_SHUTDOWN_ERROR_0));
            }
        }
        m_scheduler = null;
    }

    /**
     * Removes a currently scheduled job from the scheduler.<p>
     *  
     * @param cms an OpenCms context object that must have been initialized with "Admin" permissions
     * @param jobId the id of the job to unschedule, obtained with <code>{@link CmsScheduledJobInfo#getId()}</code>
     * 
     * @return the <code>{@link CmsScheduledJobInfo}</code> of the sucessfully unscheduled job, 
     *      or <code>null</code> if the job could not be unscheduled
     * 
     * @throws CmsRoleViolationException if the user has insufficient role permissions
     */
    public synchronized CmsScheduledJobInfo unscheduleJob(CmsObject cms, String jobId) throws CmsRoleViolationException {
        if (OpenCms.getRunLevel() > OpenCms.RUNLEVEL_1_CORE_OBJECT) {
            OpenCms.getRoleManager().checkRole(cms, CmsRole.WORKPLACE_MANAGER);
        }
        CmsScheduledJobInfo jobInfo = null;
        if (m_jobs.size() > 0) {
            for (int i = (m_jobs.size() - 1); i >= 0; i--) {
                CmsScheduledJobInfo job = (CmsScheduledJobInfo) m_jobs.get(i);
                if (jobId.equals(job.getId())) {
                    m_jobs.remove(i);
                    if (jobInfo != null) {
                        LOG.error(Messages.get().getBundle().key(Messages.LOG_MULTIPLE_JOBS_FOUND_1, jobId));
                    }
                    jobInfo = job;
                }
            }
        }
        if ((jobInfo != null) && jobInfo.isActive()) {
            try {
                m_scheduler.unscheduleJob(jobId, Scheduler.DEFAULT_GROUP);
                if (LOG.isDebugEnabled()) {
                    LOG.debug(Messages.get().getBundle().key(Messages.LOG_UNSCHEDULED_JOB_1, jobId));
                }
            } catch (SchedulerException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(Messages.get().getBundle().key(Messages.LOG_UNSCHEDULING_ERROR_1, jobId));
                }
            }
        }
        return jobInfo;
    }

    /**
     * Returns the {@link CmsObject} this Scheduler Manager was initialized with.<p>
     * 
     * @return the {@link CmsObject} this Scheduler Manager was initialized with
     */
    private synchronized CmsObject getAdminCms() {
        return m_adminCms;
    }
}
