package org.opencms.scheduler;

import org.opencms.main.CmsContextInfo;
import org.opencms.main.OpenCms;
import org.opencms.util.CmsStringUtil;
import org.opencms.util.CmsUUID;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import junit.framework.TestCase;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;

/** 
 * Test cases for the OpenCms scheduler thread pool.<p>
 * 
 * @author Alexander Kandzior 
 * 
 * @version $Revision: 1.19 $
 * 
 * @since 6.0.0
 */
public class TestCmsScheduler extends TestCase {

    /** Number of seconds to wait. */
    public static final int SECONDS_TO_WAIT = 30;

    /** Number of threads to run. */
    public static final int THREADS_TO_RUN = 20;

    /**
     * Default JUnit constructor.<p>
     * 
     * @param arg0 JUnit parameters
     */
    public TestCmsScheduler(String arg0) {
        super(arg0);
    }

    /**
     * Tests activating and deactivating of scheduled jobs.<p>
     *  
     * @throws Exception if something goes wrong
     */
    public void testActivateAndDeactivateJob() throws Exception {
        System.out.println("Trying to activate and deactivate an OpenCms job from the OpenCms scheduler.");
        TestScheduledJob.m_runCount = 0;
        CmsUUID.init(CmsUUID.getDummyEthernetAddress());
        CmsScheduledJobInfo jobInfo = new CmsScheduledJobInfo();
        CmsContextInfo contextInfo = new CmsContextInfo();
        contextInfo.setUserName(OpenCms.getDefaultUsers().getUserAdmin());
        jobInfo.setContextInfo(contextInfo);
        jobInfo.setClassName(TestScheduledJob.class.getName());
        jobInfo.setCronExpression("0/2 * * * * ?");
        jobInfo.setActive(false);
        List jobs = new ArrayList();
        jobs.add(jobInfo);
        CmsScheduleManager scheduler = new CmsScheduleManager(jobs);
        scheduler.initialize(null);
        int seconds = 0;
        do {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                fail("Something caused the waiting test thread to interrupt!");
            }
            seconds++;
        } while (seconds < 5);
        if (TestScheduledJob.m_runCount > 0) {
            fail("Test job was incorrectly run '" + TestScheduledJob.m_runCount + "' times in OpenCms scheduler.");
        }
        assertEquals(1, scheduler.getJobs().size());
        CmsScheduledJobInfo info = (CmsScheduledJobInfo) scheduler.getJobs().get(0);
        assertEquals(jobInfo.getId(), info.getId());
        assertEquals(jobInfo.getClassName(), info.getClassName());
        assertEquals(false, info.isActive());
        assertNull(info.getExecutionTimeNext());
        info = (CmsScheduledJobInfo) info.clone();
        info.setActive(true);
        scheduler.scheduleJob(null, info);
        seconds = 0;
        do {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                fail("Something caused the waiting test thread to interrupt!");
            }
            seconds++;
        } while ((seconds < SECONDS_TO_WAIT) && (TestScheduledJob.m_runCount < 3));
        if (TestScheduledJob.m_runCount == 3) {
            System.out.println("Test job was correctly run 3 times in OpenCms scheduler.");
        } else {
            fail("Test class not run after " + SECONDS_TO_WAIT + " seconds.");
        }
        assertEquals(1, scheduler.getJobs().size());
        info = (CmsScheduledJobInfo) scheduler.getJobs().get(0);
        assertEquals(jobInfo.getId(), info.getId());
        assertEquals(jobInfo.getClassName(), info.getClassName());
        assertEquals(true, info.isActive());
        assertNotNull(info.getExecutionTimeNext());
        TestScheduledJob.m_runCount = 0;
        info = (CmsScheduledJobInfo) info.clone();
        info.setActive(false);
        scheduler.scheduleJob(null, info);
        seconds = 0;
        do {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                fail("Something caused the waiting test thread to interrupt!");
            }
            seconds++;
        } while (seconds < 5);
        if (TestScheduledJob.m_runCount > 0) {
            fail("Test job was incorrectly run '" + TestScheduledJob.m_runCount + "' times in OpenCms scheduler.");
        }
        assertEquals(1, scheduler.getJobs().size());
        info = (CmsScheduledJobInfo) scheduler.getJobs().get(0);
        assertEquals(jobInfo.getId(), info.getId());
        assertEquals(jobInfo.getClassName(), info.getClassName());
        assertEquals(false, info.isActive());
        assertNull(info.getExecutionTimeNext());
        scheduler.shutDown();
    }

    /**
     * Tests adding and removing a job to the OpenCms schedule manager.<p>
     *  
     * @throws Exception if something goes wrong
     */
    public void testAddAndRemoveJobFromScheduler() throws Exception {
        System.out.println("Trying to add and remove an OpenCms job from the OpenCms scheduler.");
        TestScheduledJob.m_runCount = 0;
        CmsUUID.init(CmsUUID.getDummyEthernetAddress());
        CmsScheduledJobInfo jobInfo = new CmsScheduledJobInfo();
        CmsContextInfo contextInfo = new CmsContextInfo();
        contextInfo.setUserName(OpenCms.getDefaultUsers().getUserAdmin());
        jobInfo.setContextInfo(contextInfo);
        jobInfo.setClassName(TestScheduledJob.class.getName());
        jobInfo.setCronExpression("0/2 * * * * ?");
        List jobs = new ArrayList();
        jobs.add(jobInfo);
        CmsScheduleManager scheduler = new CmsScheduleManager(jobs);
        scheduler.initialize(null);
        int seconds = 0;
        do {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                fail("Something caused the waiting test thread to interrupt!");
            }
            seconds++;
        } while ((seconds < SECONDS_TO_WAIT) && (TestScheduledJob.m_runCount < 3));
        if (TestScheduledJob.m_runCount == 3) {
            System.out.println("Test job was correctly run 3 times in OpenCms scheduler.");
        } else {
            fail("Test class not run after " + SECONDS_TO_WAIT + " seconds.");
        }
        CmsScheduledJobInfo result;
        assertEquals(1, scheduler.getJobs().size());
        result = scheduler.unscheduleJob(null, jobInfo.getId());
        assertNotNull(result);
        assertEquals(0, scheduler.getJobs().size());
        result = scheduler.unscheduleJob(null, "iDontExist");
        assertNull(result);
        scheduler.shutDown();
    }

    /**
     * Tests adding an existing job again to the OpenCms scheduler.<p>
     *  
     * @throws Exception if something goes wrong
     */
    public void testAddExistingJobAgainToScheduler() throws Exception {
        System.out.println("Trying to schedule an existing job again with the OpenCms scheduler.");
        TestScheduledJob.m_runCount = 0;
        CmsUUID.init(CmsUUID.getDummyEthernetAddress());
        CmsScheduledJobInfo jobInfo = new CmsScheduledJobInfo();
        CmsContextInfo contextInfo = new CmsContextInfo();
        contextInfo.setUserName(OpenCms.getDefaultUsers().getUserAdmin());
        jobInfo.setContextInfo(contextInfo);
        jobInfo.setJobName("My job");
        jobInfo.setClassName(TestScheduledJob.class.getName());
        jobInfo.setCronExpression("0/2 * * * * ?");
        List jobs = new ArrayList();
        jobs.add(jobInfo);
        CmsScheduleManager scheduler = new CmsScheduleManager(jobs);
        scheduler.initialize(null);
        int seconds = 0;
        do {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                fail("Something caused the waiting test thread to interrupt!");
            }
            seconds++;
        } while ((seconds < SECONDS_TO_WAIT) && (TestScheduledJob.m_runCount < 3));
        if (TestScheduledJob.m_runCount == 3) {
            System.out.println("Test job was correctly run 3 times in OpenCms scheduler.");
        } else {
            fail("Test class not run after " + SECONDS_TO_WAIT + " seconds.");
        }
        jobInfo = scheduler.getJob(jobInfo.getId());
        assertEquals("My job", jobInfo.getJobName());
        CmsScheduledJobInfo newInfo = (CmsScheduledJobInfo) jobInfo.clone();
        newInfo.setJobName("My CHANGED name");
        newInfo.setActive(true);
        assertEquals(1, scheduler.getJobs().size());
        scheduler.scheduleJob(null, newInfo);
        assertEquals(jobInfo.getId(), newInfo.getId());
        assertEquals(1, scheduler.getJobs().size());
        jobInfo = scheduler.getJob(newInfo.getId());
        assertEquals("My CHANGED name", jobInfo.getJobName());
        newInfo = (CmsScheduledJobInfo) jobInfo.clone();
        newInfo.setActive(true);
        newInfo.setCronExpression("* * * * * *");
        assertEquals(1, scheduler.getJobs().size());
        CmsSchedulerException error = null;
        try {
            scheduler.scheduleJob(null, newInfo);
        } catch (CmsSchedulerException e) {
            error = e;
        }
        assertNotNull(error);
        assertEquals(1, scheduler.getJobs().size());
        scheduler.shutDown();
    }

    /**
     * Tests execution of jobs using CmsSchedulerThreadPool.<p>
     * 
     * @throws Exception if something goes wrong
     */
    public void testBasicJobExecution() throws Exception {
        System.out.println("Testing the OpenCms tread pool.");
        Scheduler scheduler = initOpenCmsScheduler();
        JobDetail[] jobDetail = new JobDetail[THREADS_TO_RUN];
        SimpleTrigger[] trigger = new SimpleTrigger[THREADS_TO_RUN];
        for (int i = 0; i < jobDetail.length; i++) {
            jobDetail[i] = new JobDetail("myJob" + i, Scheduler.DEFAULT_GROUP, TestCmsJob.class);
            trigger[i] = new SimpleTrigger("myTrigger" + i, Scheduler.DEFAULT_GROUP, new Date(), null, 0, 0L);
        }
        for (int i = 0; i < THREADS_TO_RUN; i++) {
            scheduler.scheduleJob(jobDetail[i], trigger[i]);
        }
        int seconds = 0;
        do {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                fail("Something caused the waiting test thread to interrupt!");
            }
            seconds++;
        } while ((seconds < SECONDS_TO_WAIT) && (TestCmsJob.m_running > 0));
        if (TestCmsJob.m_running <= 0) {
            System.out.println("Success: All threads are finished.");
        } else {
            fail("Some threads in the pool are still running after " + SECONDS_TO_WAIT + " seconds.");
        }
        scheduler.shutdown();
    }

    /**
     * Tests launching of an OpenCms job.<p>
     *  
     * @throws Exception if something goes wrong
     */
    public void testCmsJobLaunch() throws Exception {
        System.out.println("Trying to run an OpenCms job 5x.");
        TestScheduledJob.m_runCount = 0;
        Scheduler scheduler = initOpenCmsScheduler();
        JobDetail jobDetail = new JobDetail("cmsLaunch", Scheduler.DEFAULT_GROUP, CmsScheduleManager.class);
        CmsScheduledJobInfo jobInfo = new CmsScheduledJobInfo();
        CmsContextInfo contextInfo = new CmsContextInfo(OpenCms.getDefaultUsers().getUserAdmin());
        jobInfo.setContextInfo(contextInfo);
        jobInfo.setClassName(TestScheduledJob.class.getName());
        JobDataMap jobData = new JobDataMap();
        jobData.put(CmsScheduleManager.SCHEDULER_JOB_INFO, jobInfo);
        jobDetail.setJobDataMap(jobData);
        CronTrigger trigger = new CronTrigger("cmsLaunchTrigger", Scheduler.DEFAULT_GROUP);
        trigger.setCronExpression("0/2 * * * * ?");
        scheduler.scheduleJob(jobDetail, trigger);
        int seconds = 0;
        do {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                fail("Something caused the waiting test thread to interrupt!");
            }
            seconds++;
        } while ((seconds < SECONDS_TO_WAIT) && (TestScheduledJob.m_runCount < 5));
        if (TestScheduledJob.m_runCount == 5) {
            System.out.println("Success: Test job was run 5 times.");
        } else {
            fail("Test class not run after " + SECONDS_TO_WAIT + " seconds.");
        }
        scheduler.shutdown();
    }

    /**
     * Tests launching of an OpenCms job with the OpenCms schedule manager.<p>
     *  
     * @throws Exception if something goes wrong
     */
    public void testJobInOpenCmsScheduler() throws Exception {
        System.out.println("Trying to run an OpenCms job 5x with the OpenCms scheduler.");
        TestScheduledJob.m_runCount = 0;
        CmsScheduledJobInfo jobInfo = new CmsScheduledJobInfo();
        CmsContextInfo contextInfo = new CmsContextInfo();
        contextInfo.setUserName(OpenCms.getDefaultUsers().getUserAdmin());
        jobInfo.setContextInfo(contextInfo);
        jobInfo.setClassName(TestScheduledJob.class.getName());
        jobInfo.setCronExpression("0/2 * * * * ?");
        List jobs = new ArrayList();
        jobs.add(jobInfo);
        CmsScheduleManager scheduler = new CmsScheduleManager(jobs);
        scheduler.initialize(null);
        int seconds = 0;
        do {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                fail("Something caused the waiting test thread to interrupt!");
            }
            seconds++;
        } while ((seconds < SECONDS_TO_WAIT) && (TestScheduledJob.m_runCount < 5));
        if (TestScheduledJob.m_runCount == 5) {
            System.out.println("Test job was correctly run 5 times in OpenCms scheduler.");
        } else {
            fail("Test class not run after " + SECONDS_TO_WAIT + " seconds.");
        }
        if (TestScheduledJob.m_instanceCountCopy == 1) {
            System.out.println("Instance counter has correct value of 1.");
        } else {
            fail("Instance counter value of " + TestScheduledJob.m_instanceCountCopy + " invalid!");
        }
        scheduler.shutDown();
    }

    /**
     * Tests launching of a persistent OpenCms job with the OpenCms schedule manager.<p>
     *  
     * @throws Exception if something goes wrong
     */
    public void testPersitentJobInOpenCmsScheduler() throws Exception {
        System.out.println("Trying to run a persistent OpenCms job 5x with the OpenCms scheduler.");
        TestScheduledJob.m_runCount = 0;
        CmsScheduledJobInfo jobInfo = new CmsScheduledJobInfo();
        CmsContextInfo contextInfo = new CmsContextInfo(OpenCms.getDefaultUsers().getUserAdmin());
        jobInfo.setContextInfo(contextInfo);
        jobInfo.setClassName(TestScheduledJob.class.getName());
        jobInfo.setReuseInstance(true);
        jobInfo.setCronExpression("0/2 * * * * ?");
        List jobs = new ArrayList();
        jobs.add(jobInfo);
        CmsScheduleManager scheduler = new CmsScheduleManager(jobs);
        scheduler.initialize(null);
        int seconds = 0;
        do {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                fail("Something caused the waiting test thread to interrupt!");
            }
            seconds++;
        } while ((seconds < SECONDS_TO_WAIT) && (TestScheduledJob.m_runCount < 5));
        if (TestScheduledJob.m_runCount == 5) {
            System.out.println("Test job was correctly run 5 times in OpenCms scheduler.");
        } else {
            fail("Test class not run after " + SECONDS_TO_WAIT + " seconds.");
        }
        if (TestScheduledJob.m_instanceCountCopy == 5) {
            System.out.println("Instance counter was correctly incremented 5 times.");
        } else {
            fail("Instance counter was not incremented!");
        }
        scheduler.shutDown();
    }

    /**
     * Initializes a Quartz scheduler.<p>
     * 
     * @return the initialized scheduler
     * @throws Exception in case something goes wrong
     */
    private Scheduler initOpenCmsScheduler() throws Exception {
        Properties properties = new Properties();
        properties.put("org.quartz.scheduler.instanceName", "OpenCmsScheduler");
        properties.put("org.quartz.scheduler.threadName", "OpenCms: Scheduler");
        properties.put("org.quartz.scheduler.rmi.export", CmsStringUtil.FALSE);
        properties.put("org.quartz.scheduler.rmi.proxy", CmsStringUtil.FALSE);
        properties.put("org.quartz.scheduler.xaTransacted", CmsStringUtil.FALSE);
        properties.put("org.quartz.threadPool.class", "org.opencms.scheduler.CmsSchedulerThreadPool");
        properties.put("org.quartz.jobStore.misfireThreshold", "60000");
        properties.put("org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore");
        SchedulerFactory schedulerFactory = new StdSchedulerFactory(properties);
        Scheduler scheduler = schedulerFactory.getScheduler();
        scheduler.getMetaData();
        scheduler.start();
        CmsUUID.init(CmsUUID.getDummyEthernetAddress());
        return scheduler;
    }
}
