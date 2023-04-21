package binky.reportrunner.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import binky.reportrunner.data.RunnerGroup;
import binky.reportrunner.data.RunnerJob;
import binky.reportrunner.data.RunnerJob_pk;
import binky.reportrunner.scheduler.SchedulerException;
import binky.reportrunner.service.GroupService;
import binky.reportrunner.service.ReportService;

public class ReportServiceImplTest extends TestCase {

    ReportService reportService;

    GroupService groupService;

    private RunnerGroup group;

    private RunnerJob j;

    protected void setUp() throws Exception {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        reportService = (ReportService) ctx.getBean("runnerJobService");
        groupService = (GroupService) ctx.getBean("groupService");
        group = new RunnerGroup();
        group.setGroupName(getUID());
        groupService.saveOrUpdate(group);
        this.j = getTestJob();
        reportService.addUpdateJob(j);
    }

    private String getUID() {
        return UUID.randomUUID().toString();
    }

    @Override
    protected void tearDown() throws Exception {
        reportService.deleteJob(j.getPk().getJobName(), group.getGroupName());
        groupService.delete(group.getGroupName());
    }

    private RunnerJob getTestJob() {
        RunnerJob j = new RunnerJob();
        RunnerJob_pk pk = new RunnerJob_pk();
        pk.setGroup(group);
        pk.setJobName(getUID());
        j.setPk(pk);
        j.setScheduled(true);
        j.setStartDate(Calendar.getInstance().getTime());
        j.setCronString("0 0 * ? * *");
        return j;
    }

    public void testAddUpdateJob() {
        RunnerJob c = reportService.getJob(j.getPk().getJobName(), group.getGroupName());
        assertNotNull(c);
    }

    public void testDeleteJob() {
        try {
            reportService.deleteJob(j.getPk().getJobName(), group.getGroupName());
            RunnerJob c = reportService.getJob(j.getPk().getJobName(), group.getGroupName());
            assertNull(c);
        } catch (SchedulerException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    public void testGetJob() {
        RunnerJob c = reportService.getJob(j.getPk().getJobName(), group.getGroupName());
        assertNotNull(c);
    }

    public void testListJobs() {
        assertTrue(reportService.listJobs(group.getGroupName()).size() > 0);
    }

    public void testIsJobActive() {
        try {
            assertTrue(reportService.isJobActive(j.getPk().getJobName(), group.getGroupName()));
        } catch (SchedulerException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    public void testPauseJob() {
        try {
            reportService.pauseJob(j.getPk().getJobName(), group.getGroupName());
            assertFalse(reportService.isJobActive(j.getPk().getJobName(), group.getGroupName()));
        } catch (SchedulerException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    public void testResumeJob() {
        try {
            reportService.pauseJob(j.getPk().getJobName(), group.getGroupName());
            reportService.resumeJob(j.getPk().getJobName(), group.getGroupName());
            assertTrue(reportService.isJobActive(j.getPk().getJobName(), group.getGroupName()));
        } catch (SchedulerException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    public void testGetNextRunTime() {
        try {
            Date next = reportService.getNextRunTime(j.getPk().getJobName(), group.getGroupName());
            assertTrue(next.getTime() > Calendar.getInstance().getTimeInMillis());
        } catch (SchedulerException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    public void testPauseGroup() {
        try {
            reportService.pauseGroup(group.getGroupName());
            assertFalse(reportService.isJobActive(j.getPk().getJobName(), group.getGroupName()));
            reportService.resumeGroup(group.getGroupName());
        } catch (SchedulerException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    public void testResumeGroup() {
        try {
            reportService.pauseGroup(group.getGroupName());
            reportService.resumeGroup(group.getGroupName());
            assertTrue(reportService.isJobActive(j.getPk().getJobName(), group.getGroupName()));
        } catch (SchedulerException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}
