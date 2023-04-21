package net.sf.jerkbot.scheduler.impl;

import net.sf.jerkbot.JerkBotConstants;
import net.sf.jerkbot.scheduler.JobInfo;
import net.sf.jerkbot.scheduler.SchedulerService;
import net.sf.jerkbot.util.IOUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;
import org.quartz.JobDetail;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

/**
 * @author Yves Zoundi <yveszoundi at users dot sf dot net> Default job
 *         scheduler implementation based on Quartz providing common methods
 * @version 0.0.1
 */
@Component(immediate = true)
@Service(value = SchedulerService.class)
public class SchedulerServiceImpl implements SchedulerService {

    private static final Logger Log = LoggerFactory.getLogger(SchedulerServiceImpl.class.getName());

    private SchedulerFactory schedFact;

    private Scheduler scheduler;

    @Activate
    protected void activate(ComponentContext componentContext) {
        try {
            Log.info("Creating scheduler");
            String schedulerConfig = System.getProperty(JerkBotConstants.SCHEDULER_CONFIG);
            Properties props = new Properties();
            if (!StringUtils.isEmpty(schedulerConfig)) {
                Log.debug("Scheduler configuration file specified {}", schedulerConfig);
                File schedulerConfigFile = new File(schedulerConfig);
                if (schedulerConfigFile.exists() && schedulerConfigFile.isFile()) {
                    InputStream inputStream = null;
                    try {
                        inputStream = new FileInputStream(schedulerConfigFile);
                        props.load(inputStream);
                    } catch (IOException ioe) {
                        Log.warn("Error reading config file '{}' {}", schedulerConfig, ioe);
                    } finally {
                        IOUtil.closeQuietly(inputStream);
                    }
                } else {
                    Log.warn("Cannot load scheduler config '{}'", schedulerConfig);
                }
            }
            if (!props.isEmpty()) {
                try {
                    schedFact = new org.quartz.impl.StdSchedulerFactory(props);
                } catch (Exception e) {
                    Log.warn("Caught exception, using default scheduler", e.getMessage());
                    schedFact = new org.quartz.impl.StdSchedulerFactory();
                }
            } else {
                schedFact = new org.quartz.impl.StdSchedulerFactory();
            }
            scheduler = schedFact.getScheduler();
            scheduler.start();
            Log.info("Scheduler started successfully!");
        } catch (SchedulerException e) {
            Log.error("Error creating scheduler", e);
        }
    }

    @Deactivate
    protected void deactivate(ComponentContext componentContext) {
        stop();
    }

    public void addJobListener(JobListener jobListener) {
        try {
            scheduler.addJobListener(jobListener);
        } catch (SchedulerException e) {
            Log.error("Error adding job listener '{}'", jobListener.getName());
        }
    }

    public boolean updateJobParams(JobInfo jobInfo, Map<?, ?> parameters) {
        try {
            JobDetail detail = scheduler.getJobDetail(jobInfo.getJobName(), jobInfo.getJobGroup());
            if (detail == null) {
                Log.debug("No such job '{}'", jobInfo.getJobName());
                return false;
            }
            JobDetail updateJob = new JobDetail(detail.getName(), detail.getGroup(), detail.getJobClass());
            updateJob.getJobDataMap().putAll(detail.getJobDataMap());
            if (parameters != null) {
                updateJob.getJobDataMap().putAll(parameters);
            }
            scheduler.addJob(updateJob, true);
            return true;
        } catch (SchedulerException e) {
            Log.error("Error updating job '{}'. Error : {}", jobInfo.getJobName(), e);
            return false;
        } catch (Exception e) {
            Log.warn("Unexpected error", e);
            return false;
        }
    }

    public boolean updateJobTrigger(JobInfo jobInfo, Trigger trigger) {
        try {
            trigger.setJobName(jobInfo.getJobName());
            trigger.setJobGroup(jobInfo.getJobGroup());
            Date jobDate = scheduler.rescheduleJob(jobInfo.getJobName(), jobInfo.getJobGroup(), trigger);
            Log.debug("The job '{}' will next start at {}", jobInfo.getJobName(), jobDate);
            return true;
        } catch (SchedulerException e) {
            Log.error("Error updating job trigger '{}' {}", jobInfo.getJobName(), e);
            return false;
        }
    }

    public boolean updateJob(JobInfo jobInfo, Map<?, ?> parameters, Trigger trigger) {
        boolean updateSucceeded = updateJobParams(jobInfo, parameters);
        if (!updateSucceeded) {
            return false;
        }
        updateSucceeded = updateJobTrigger(jobInfo, trigger);
        return updateSucceeded;
    }

    public void scheduleJob(JobDetail job, Trigger trigger) {
        try {
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            Log.error("Error unscheduling job '{}' {}", job.getName(), e);
        }
    }

    public boolean unscheduleJob(JobInfo jobInfo) {
        try {
            scheduler.deleteJob(jobInfo.getJobName(), jobInfo.getJobGroup());
            return true;
        } catch (SchedulerException e) {
            Log.error("Error unscheduling job '{}' {}", jobInfo.getJobName(), e);
            return false;
        }
    }

    public void stop() {
        try {
            if (scheduler != null) {
                if (scheduler.isStarted()) {
                    scheduler.shutdown();
                }
            }
        } catch (SchedulerException e) {
            Log.error("Error stopping job scheduler {}", e);
        }
    }
}
