package com.googlecode.yamaguchi;

import java.util.Date;
import java.util.List;
import java.util.Set;
import org.quartz.Calendar;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.quartz.SchedulerListener;
import org.quartz.SchedulerMetaData;
import org.quartz.Trigger;
import org.quartz.TriggerListener;
import org.quartz.UnableToInterruptJobException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * @author h_yamaguchi
 * 
 */
public class GuiceScheduler implements Scheduler {

    private Scheduler scheduler;

    public GuiceScheduler(Scheduler scheduler, final Module module) throws SchedulerException {
        this.scheduler = scheduler;
        setJobFactory(new JobFactory() {

            public Job newJob(TriggerFiredBundle bundle) throws SchedulerException {
                JobDetail jobDetail = bundle.getJobDetail();
                Class<?> jobClass = jobDetail.getJobClass();
                try {
                    Injector injector = Guice.createInjector(module);
                    Job job = (Job) injector.getInstance(jobClass);
                    if (job == null) {
                        return (Job) jobClass.newInstance();
                    }
                    return job;
                } catch (Exception e) {
                    throw new SchedulerException("Problem instantiating class '" + jobDetail.getJobClass().getName() + "'", e);
                }
            }
        });
    }

    public void addCalendar(String arg0, Calendar arg1, boolean arg2, boolean arg3) throws SchedulerException {
        this.scheduler.addCalendar(arg0, arg1, arg2, arg3);
    }

    public void addGlobalJobListener(JobListener arg0) throws SchedulerException {
        this.scheduler.addGlobalJobListener(arg0);
    }

    public void addGlobalTriggerListener(TriggerListener arg0) throws SchedulerException {
        this.scheduler.addGlobalTriggerListener(arg0);
    }

    public void addJob(JobDetail arg0, boolean arg1) throws SchedulerException {
        this.scheduler.addJob(arg0, arg1);
    }

    public void addJobListener(JobListener arg0) throws SchedulerException {
        this.scheduler.addJobListener(arg0);
    }

    public void addSchedulerListener(SchedulerListener arg0) throws SchedulerException {
        this.scheduler.addSchedulerListener(arg0);
    }

    public void addTriggerListener(TriggerListener arg0) throws SchedulerException {
        this.scheduler.addTriggerListener(arg0);
    }

    public boolean deleteCalendar(String arg0) throws SchedulerException {
        return this.scheduler.deleteCalendar(arg0);
    }

    public boolean deleteJob(String arg0, String arg1) throws SchedulerException {
        return this.scheduler.deleteJob(arg0, arg1);
    }

    public Calendar getCalendar(String arg0) throws SchedulerException {
        return this.scheduler.getCalendar(arg0);
    }

    public String[] getCalendarNames() throws SchedulerException {
        return this.scheduler.getCalendarNames();
    }

    public SchedulerContext getContext() throws SchedulerException {
        return this.scheduler.getContext();
    }

    public List getCurrentlyExecutingJobs() throws SchedulerException {
        return this.scheduler.getCurrentlyExecutingJobs();
    }

    public JobListener getGlobalJobListener(String arg0) throws SchedulerException {
        return this.scheduler.getGlobalJobListener(arg0);
    }

    public List getGlobalJobListeners() throws SchedulerException {
        return this.scheduler.getGlobalJobListeners();
    }

    public TriggerListener getGlobalTriggerListener(String arg0) throws SchedulerException {
        return this.scheduler.getGlobalTriggerListener(arg0);
    }

    public List getGlobalTriggerListeners() throws SchedulerException {
        return this.scheduler.getGlobalTriggerListeners();
    }

    public JobDetail getJobDetail(String arg0, String arg1) throws SchedulerException {
        return this.scheduler.getJobDetail(arg0, arg1);
    }

    public String[] getJobGroupNames() throws SchedulerException {
        return this.scheduler.getJobGroupNames();
    }

    public JobListener getJobListener(String arg0) throws SchedulerException {
        return this.scheduler.getJobListener(arg0);
    }

    public Set getJobListenerNames() throws SchedulerException {
        return this.scheduler.getJobListenerNames();
    }

    public String[] getJobNames(String arg0) throws SchedulerException {
        return this.scheduler.getJobNames(arg0);
    }

    public SchedulerMetaData getMetaData() throws SchedulerException {
        return this.scheduler.getMetaData();
    }

    public Set getPausedTriggerGroups() throws SchedulerException {
        return this.scheduler.getPausedTriggerGroups();
    }

    public String getSchedulerInstanceId() throws SchedulerException {
        return this.scheduler.getSchedulerInstanceId();
    }

    public List getSchedulerListeners() throws SchedulerException {
        return this.scheduler.getSchedulerListeners();
    }

    public String getSchedulerName() throws SchedulerException {
        return this.scheduler.getSchedulerName();
    }

    public Trigger getTrigger(String arg0, String arg1) throws SchedulerException {
        return this.scheduler.getTrigger(arg0, arg1);
    }

    public String[] getTriggerGroupNames() throws SchedulerException {
        return this.scheduler.getTriggerGroupNames();
    }

    public TriggerListener getTriggerListener(String arg0) throws SchedulerException {
        return this.scheduler.getTriggerListener(arg0);
    }

    public Set getTriggerListenerNames() throws SchedulerException {
        return this.scheduler.getTriggerListenerNames();
    }

    public String[] getTriggerNames(String arg0) throws SchedulerException {
        return this.scheduler.getTriggerNames(arg0);
    }

    public int getTriggerState(String arg0, String arg1) throws SchedulerException {
        return this.scheduler.getTriggerState(arg0, arg1);
    }

    public Trigger[] getTriggersOfJob(String arg0, String arg1) throws SchedulerException {
        return this.scheduler.getTriggersOfJob(arg0, arg1);
    }

    public boolean interrupt(String arg0, String arg1) throws UnableToInterruptJobException {
        return this.scheduler.interrupt(arg0, arg1);
    }

    public boolean isInStandbyMode() throws SchedulerException {
        return this.scheduler.isInStandbyMode();
    }

    @Deprecated
    public boolean isPaused() throws SchedulerException {
        return this.scheduler.isPaused();
    }

    public boolean isShutdown() throws SchedulerException {
        return this.scheduler.isShutdown();
    }

    public boolean isStarted() throws SchedulerException {
        return this.scheduler.isStarted();
    }

    @Deprecated
    public void pause() throws SchedulerException {
        this.scheduler.pause();
    }

    public void pauseAll() throws SchedulerException {
        this.scheduler.pauseAll();
    }

    public void pauseJob(String arg0, String arg1) throws SchedulerException {
        this.scheduler.pauseJob(arg0, arg1);
    }

    public void pauseJobGroup(String arg0) throws SchedulerException {
        this.scheduler.pauseJobGroup(arg0);
    }

    public void pauseTrigger(String arg0, String arg1) throws SchedulerException {
        this.scheduler.pauseTrigger(arg0, arg1);
    }

    public void pauseTriggerGroup(String arg0) throws SchedulerException {
        this.scheduler.pauseTriggerGroup(arg0);
    }

    @Deprecated
    public boolean removeGlobalJobListener(JobListener arg0) throws SchedulerException {
        return this.scheduler.removeGlobalJobListener(arg0);
    }

    public boolean removeGlobalJobListener(String arg0) throws SchedulerException {
        return this.scheduler.removeGlobalJobListener(arg0);
    }

    @Deprecated
    public boolean removeGlobalTriggerListener(TriggerListener arg0) throws SchedulerException {
        return this.scheduler.removeGlobalTriggerListener(arg0);
    }

    public boolean removeGlobalTriggerListener(String arg0) throws SchedulerException {
        return this.scheduler.removeGlobalTriggerListener(arg0);
    }

    public boolean removeJobListener(String arg0) throws SchedulerException {
        return this.scheduler.removeJobListener(arg0);
    }

    public boolean removeSchedulerListener(SchedulerListener arg0) throws SchedulerException {
        return this.scheduler.removeSchedulerListener(arg0);
    }

    public boolean removeTriggerListener(String arg0) throws SchedulerException {
        return this.scheduler.removeTriggerListener(arg0);
    }

    public Date rescheduleJob(String arg0, String arg1, Trigger arg2) throws SchedulerException {
        return this.scheduler.rescheduleJob(arg0, arg1, arg2);
    }

    public void resumeAll() throws SchedulerException {
        this.scheduler.resumeAll();
    }

    public void resumeJob(String arg0, String arg1) throws SchedulerException {
        this.scheduler.resumeJob(arg0, arg1);
    }

    public void resumeJobGroup(String arg0) throws SchedulerException {
        this.scheduler.resumeJobGroup(arg0);
    }

    public void resumeTrigger(String arg0, String arg1) throws SchedulerException {
        this.scheduler.resumeTrigger(arg0, arg1);
    }

    public void resumeTriggerGroup(String arg0) throws SchedulerException {
        this.scheduler.resumeTriggerGroup(arg0);
    }

    public Date scheduleJob(Trigger arg0) throws SchedulerException {
        return this.scheduler.scheduleJob(arg0);
    }

    public Date scheduleJob(JobDetail arg0, Trigger arg1) throws SchedulerException {
        return this.scheduler.scheduleJob(arg0, arg1);
    }

    public void setJobFactory(JobFactory arg0) throws SchedulerException {
        this.scheduler.setJobFactory(arg0);
    }

    public void shutdown() throws SchedulerException {
        this.scheduler.shutdown();
    }

    public void shutdown(boolean arg0) throws SchedulerException {
        this.scheduler.shutdown(arg0);
    }

    public void standby() throws SchedulerException {
        this.scheduler.standby();
    }

    public void start() throws SchedulerException {
        this.scheduler.start();
    }

    public void triggerJob(String arg0, String arg1) throws SchedulerException {
        this.scheduler.triggerJob(arg0, arg1);
    }

    public void triggerJob(String arg0, String arg1, JobDataMap arg2) throws SchedulerException {
        this.scheduler.triggerJob(arg0, arg1, arg2);
    }

    public void triggerJobWithVolatileTrigger(String arg0, String arg1) throws SchedulerException {
        this.scheduler.triggerJobWithVolatileTrigger(arg0, arg1);
    }

    public void triggerJobWithVolatileTrigger(String arg0, String arg1, JobDataMap arg2) throws SchedulerException {
        this.scheduler.triggerJobWithVolatileTrigger(arg0, arg1, arg2);
    }

    public boolean unscheduleJob(String arg0, String arg1) throws SchedulerException {
        return this.scheduler.unscheduleJob(arg0, arg1);
    }

    public Scheduler getScheduler() {
        return this.scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }
}
