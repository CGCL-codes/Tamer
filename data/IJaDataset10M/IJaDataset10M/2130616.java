package org.gplugins.quartz;

import java.text.ParseException;
import java.util.Map;
import org.apache.geronimo.gbean.GBeanLifecycle;
import org.apache.geronimo.gbean.GBeanInfo;
import org.apache.geronimo.gbean.GBeanInfoBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobDetail;
import org.quartz.CronTrigger;
import org.quartz.SchedulerException;
import org.quartz.JobDataMap;

/**
 * Represents a Quartz job.
 *
 * @version $Rev: 355877 $ $Date: 2005-12-10 21:48:27 -0500 (Sat, 10 Dec 2005) $
 */
public class QuartzJobGBean implements GBeanLifecycle, QuartzJob {

    private static final Log log = LogFactory.getLog(QuartzJobGBean.class);

    private final QuartzScheduler scheduler;

    private final ClassLoader loader;

    private String name;

    private String jobClass;

    private Map jobData;

    private String cronExpression;

    boolean running;

    public QuartzJobGBean(QuartzScheduler scheduler, String jobClass, Map jobData, String name, ClassLoader loader) {
        this.jobClass = jobClass;
        this.name = name;
        this.jobData = jobData;
        this.scheduler = scheduler;
        this.loader = loader;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) throws SchedulerException {
        this.cronExpression = cronExpression;
        if (running) {
            try {
                scheduler.rescheduleJob(name, createTrigger());
            } catch (ParseException e) {
                throw new SchedulerException("Unable to schedule; invalid cron expression '" + cronExpression + "'", e);
            }
        }
    }

    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    public Map getJobData() {
        return jobData;
    }

    public void setJobData(Map jobData) {
        this.jobData = jobData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void pause() throws SchedulerException {
        if (running) {
            scheduler.pauseJob(name);
        }
    }

    public void resume() throws SchedulerException {
        if (running) {
            scheduler.resumeJob(name);
        }
    }

    public void execute() throws SchedulerException {
        if (running) {
            scheduler.executeImmediately(name);
        }
    }

    public void doStart() throws Exception {
        log.info("Scheduling job '" + name + "'");
        Class cls = loader.loadClass(jobClass);
        JobDetail jd = new JobDetail(name, QuartzSchedulerGBean.GROUP_NAME, cls);
        if (jobData != null) {
            jd.setJobDataMap(new JobDataMap(jobData));
        }
        CronTrigger cronTrigger = createTrigger();
        scheduler.scheduleJob(jd, cronTrigger);
        running = true;
    }

    public void doStop() throws Exception {
        running = false;
        scheduler.deleteJob(name);
    }

    public void doFail() {
        running = false;
    }

    private CronTrigger createTrigger() throws ParseException {
        CronTrigger cronTrigger = new CronTrigger(name + " Trigger", QuartzSchedulerGBean.GROUP_NAME);
        cronTrigger.setCronExpression(cronExpression);
        return cronTrigger;
    }

    public static final GBeanInfo GBEAN_INFO;

    static {
        GBeanInfoBuilder infoFactory = GBeanInfoBuilder.createStatic("Quartz Job", QuartzJobGBean.class);
        infoFactory.addAttribute("classLoader", ClassLoader.class, false);
        infoFactory.addReference("QuartzScheduler", QuartzScheduler.class, "GBean");
        infoFactory.addInterface(QuartzJob.class, new String[] { "cronExpression", "jobClass", "name", "jobData" }, new String[] { "cronExpression", "jobClass", "name", "jobData" });
        infoFactory.setConstructor(new String[] { "QuartzScheduler", "jobClass", "jobData", "name", "classLoader" });
        GBEAN_INFO = infoFactory.getBeanInfo();
    }

    public static GBeanInfo getGBeanInfo() {
        return GBEAN_INFO;
    }
}
