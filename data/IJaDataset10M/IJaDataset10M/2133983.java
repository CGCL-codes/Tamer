package unit.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import uk.co.q3c.deplan.dao.DatabaseConnection;
import uk.co.q3c.deplan.domain.resource.BaseCalendar;
import uk.co.q3c.deplan.domain.resource.DefaultWorkingTime;
import uk.co.q3c.deplan.domain.resource.IndividualResource;
import uk.co.q3c.deplan.domain.resource.Resource;
import uk.co.q3c.deplan.domain.resource.ResourceAssignmentException;
import uk.co.q3c.deplan.domain.resource.ResourcePool;
import uk.co.q3c.deplan.domain.task.AbstractAtomicTask;
import uk.co.q3c.deplan.domain.task.AtomicTask;
import uk.co.q3c.deplan.domain.task.ProjectSummaryTask;
import uk.co.q3c.deplan.domain.task.ProjectTask;
import uk.co.q3c.deplan.domain.task.ResourcedTask;
import uk.co.q3c.deplan.domain.task.SummaryTask;
import uk.co.q3c.deplan.domain.task.Task;
import uk.co.q3c.deplan.domain.task.TaskAnalysisResult;
import uk.co.q3c.deplan.domain.task.TaskStatusReport;
import uk.co.q3c.deplan.rcp.action.ImportAction;
import uk.co.q3c.deplan.rcp.model.Model;
import uk.co.q3c.deplan.service.Leveller;
import uk.co.q3c.deplan.util.Q3Calendar;
import util.TestUtils;

/**
 * Tests {@link Leveller}
 * 
 * @see Leveller_Bug_Test_1
 * @author DSowerby 3 Aug 2008 21:15:33
 * 
 */
@Test
public class Leveller_UT {

    protected final Logger logger = Logger.getLogger(getClass().getName());

    Leveller leveller;

    SummaryTask task1;

    SummaryTask task11;

    AtomicTask task12;

    AtomicTask task111;

    SummaryTask task2;

    SummaryTask task21;

    AtomicTask task22;

    SummaryTask task211;

    AtomicTask task2111;

    AbstractAtomicTask task3;

    AbstractAtomicTask task4;

    AbstractAtomicTask task5;

    BaseCalendar baseCalendar;

    Resource resources[] = new Resource[7];

    DefaultWorkingTime dwt = new DefaultWorkingTime();

    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yy HH:mm:ss");

    SimpleDateFormat sdf2 = new SimpleDateFormat("dd MMM yy");

    String testDb = "/home/dave/temp/deplan/leveller_test.db4o";

    ResourcePool resourcePool;

    DatabaseConnection dbc;

    @BeforeClass
    public void setupClass() {
    }

    @BeforeMethod
    public void setUp() throws Exception {
        dbc = TestUtils.dbc(this, true);
        Model.setDbc(dbc);
        baseCalendar = new BaseCalendar();
        baseCalendar.setBaseWorkingTime(dwt);
        buildPlan();
        buildResources();
        assignResources();
        leveller = new Leveller();
        leveller.setBaseCalendar(baseCalendar);
        leveller.setResourcePool(resourcePool);
    }

    @AfterMethod
    public void setDown() {
        dbc.delete();
    }

    /**
	 * Set of tasks all with the same resource, but in priority order, 100
	 * percent resource allocation.
	 */
    public void levelPriorityOnly100Percent() {
        ResourcedTask task1 = createTask(1, "t1");
        ResourcedTask task2 = createTask(2, "t2");
        ResourcedTask task3 = createTask(3, "t3");
        ResourcedTask task4 = createTask(4, "t4");
        ResourcedTask task5 = createTask(5, "t5");
        leveller.addCandidate(task1);
        leveller.addCandidate(task2);
        leveller.addCandidate(task3);
        leveller.addCandidate(task4);
        leveller.addCandidate(task5);
        task1.assignResource(resources[0], 1);
        task2.assignResource(resources[0], 1);
        task3.assignResource(resources[0], 1);
        task4.assignResource(resources[0], 1);
        task5.assignResource(resources[0], 1);
        task4.setPriority(2000);
        task1.setPriority(1999);
        task3.setPriority(1400);
        task2.setPriority(1100);
        task5.setPriority(300);
        Calendar cal = new GregorianCalendar();
        cal.set(2008, 4, 5);
        leveller.setLevelDate(cal.getTime());
        ((IndividualResource) resources[0]).setProfile(leveller.getLevelDate(), 100);
        leveller.level();
        logger.info("task4 start: " + task4.getStart() + " finish:" + task4.getFinish());
        logger.info("task1 start: " + task1.getStart() + " finish:" + task1.getFinish());
        logger.info("task3 start: " + task3.getStart() + " finish:" + task3.getFinish());
        logger.info("task2 start: " + task2.getStart() + " finish:" + task2.getFinish());
        logger.info("task5 start: " + task5.getStart() + " finish:" + task5.getFinish());
        Assert.assertTrue(task4.getStart().before(task1.getStart()));
        Assert.assertTrue(task1.getStart().before(task3.getStart()));
        Assert.assertTrue(task3.getStart().before(task2.getStart()));
        Assert.assertTrue(task2.getStart().before(task5.getStart()));
        Assert.assertTrue(task4.getFinish().before(task1.getStart()));
        Assert.assertTrue(task1.getFinish().before(task3.getStart()));
        Assert.assertTrue(task3.getFinish().before(task2.getStart()));
        Assert.assertTrue(task2.getFinish().before(task5.getStart()));
        Assert.assertEquals(task4.getStart().toString(), "Mon May 05 09:00:00 BST 2008");
        Assert.assertEquals(task4.getFinish().toString(), "Wed May 07 10:39:00 BST 2008");
        Assert.assertEquals(task1.getStart().toString(), "Wed May 07 10:40:00 BST 2008");
        Assert.assertEquals(task1.getFinish().toString(), "Fri May 09 12:19:00 BST 2008");
        Assert.assertEquals(task3.getStart().toString(), "Fri May 09 12:20:00 BST 2008");
        Assert.assertEquals(task3.getFinish().toString(), "Tue May 13 14:29:00 BST 2008");
        Assert.assertEquals(task2.getStart().toString(), "Tue May 13 14:30:00 BST 2008");
        Assert.assertEquals(task2.getFinish().toString(), "Thu May 15 16:09:00 BST 2008");
    }

    /**
	 * Set of tasks all with the same resource, but in priority order, 100
	 * percent resource allocation.
	 */
    public void levelPriorityOnlyNot100Percent() {
        ResourcedTask task1 = createTask(1, "t1");
        ResourcedTask task2 = createTask(2, "t2");
        ResourcedTask task3 = createTask(3, "t3");
        ResourcedTask task4 = createTask(4, "t4");
        ResourcedTask task5 = createTask(5, "t5");
        leveller.addCandidate(task1);
        leveller.addCandidate(task2);
        leveller.addCandidate(task3);
        leveller.addCandidate(task4);
        leveller.addCandidate(task5);
        task1.assignResource(resources[0], 1);
        task2.assignResource(resources[0], 1);
        task3.assignResource(resources[0], 1);
        task4.assignResource(resources[0], 0.5);
        task5.assignResource(resources[0], 1);
        task4.setPriority(2000);
        task1.setPriority(1999);
        task3.setPriority(1400);
        task2.setPriority(1100);
        task5.setPriority(300);
        Calendar cal = new GregorianCalendar();
        cal.set(2008, 4, 5);
        leveller.setLevelDate(cal.getTime());
        ((IndividualResource) resources[0]).setProfile(leveller.getLevelDate(), 100);
        leveller.level();
        logger.info(">>>>>>>>>>>>>>>>. not 100 percent <<<<<<<<<<");
        logger.info("task4 start: " + task4.getStart() + " finish:" + task4.getFinish());
        logger.info("task1 start: " + task1.getStart() + " finish:" + task1.getFinish());
        logger.info("task3 start: " + task3.getStart() + " finish:" + task3.getFinish());
        logger.info("task2 start: " + task2.getStart() + " finish:" + task2.getFinish());
        logger.info("task5 start: " + task5.getStart() + " finish:" + task5.getFinish());
        Assert.assertTrue(task4.getStart().before(task1.getStart()));
        Assert.assertTrue(task1.getStart().before(task3.getStart()));
        Assert.assertTrue(task3.getStart().before(task2.getStart()));
        Assert.assertTrue(task2.getStart().before(task5.getStart()));
        Assert.assertEquals(task4.getStart().toString(), "Mon May 05 09:00:00 BST 2008");
        Assert.assertEquals(task4.getFinish().toString(), "Fri May 09 10:39:00 BST 2008");
        Assert.assertEquals(task1.getStart().toString(), "Mon May 05 12:45:00 BST 2008");
        Assert.assertEquals(task1.getFinish().toString(), "Fri May 09 12:19:00 BST 2008");
        Assert.assertEquals(task3.getStart().toString(), "Fri May 09 12:20:00 BST 2008");
        Assert.assertEquals(task3.getFinish().toString(), "Tue May 13 14:29:00 BST 2008");
        Assert.assertEquals(task2.getStart().toString(), "Tue May 13 14:30:00 BST 2008");
        Assert.assertEquals(task2.getFinish().toString(), "Thu May 15 16:09:00 BST 2008");
    }

    /**
	 * Set of tasks all with the same resource, but with predecessors preventing
	 * natural priority order.
	 */
    public void levelDependenciesAndPriority() {
        ResourcedTask task1 = createTask(1, "t1");
        ResourcedTask task2 = createTask(2, "t2");
        task1.assignResource(resources[0], 1);
        task2.assignResource(resources[1], 1);
        task1.setPriority(1999);
        task2.setPriority(1100);
        task1.addPredecessor(task2);
        leveller.addCandidate(task1);
        leveller.addCandidate(task2);
        Calendar cal = new GregorianCalendar();
        cal.set(2008, 4, 5);
        leveller.setLevelDate(cal.getTime());
        ((IndividualResource) resources[0]).setProfile(leveller.getLevelDate(), 100);
        leveller.level();
        logger.info(">>>>>>>>>>>>>>>>. levelDependenciesAndPriority <<<<<<<<<<");
        logger.info("task1 start: " + task1.getStart() + " finish:" + task1.getFinish());
        logger.info("task2 start: " + task2.getStart() + " finish:" + task2.getFinish());
        Assert.assertEquals(task2.getStart().toString(), "Mon May 05 09:00:00 BST 2008");
        Assert.assertEquals(task2.getFinish().toString(), "Wed May 07 10:39:00 BST 2008");
        Assert.assertEquals(sdf2.format(task1.getStart()), "07 May 08");
        Assert.assertEquals(sdf2.format(task1.getFinish()), "09 May 08");
    }

    /**
	 * Set of tasks with unique resources should all complete in parallel
	 * regardless of priority
	 */
    public void parallelTasks() {
        ResourcedTask task1 = createTask(1, "t1");
        ResourcedTask task2 = createTask(2, "t2");
        ResourcedTask task3 = createTask(3, "t3");
        ResourcedTask task4 = createTask(4, "t4");
        ResourcedTask task5 = createTask(5, "t5");
        leveller.addCandidate(task1);
        leveller.addCandidate(task2);
        leveller.addCandidate(task3);
        leveller.addCandidate(task4);
        leveller.addCandidate(task5);
        task1.assignResource(resources[0], 1);
        task2.assignResource(resources[1], 1);
        task3.assignResource(resources[2], 1);
        task4.assignResource(resources[3], 1);
        task5.assignResource(resources[4], 1);
        task4.setPriority(2000);
        task1.setPriority(1999);
        task3.setPriority(1400);
        task2.setPriority(1100);
        task5.setPriority(300);
        Calendar cal = new GregorianCalendar();
        cal.set(2008, 4, 5);
        leveller.setLevelDate(cal.getTime());
        leveller.level();
        logger.info(">>>>>>>>>>>>>>>>. parallel tasks <<<<<<<<<<");
        logger.info("task4 start: " + task4.getStart() + " finish:" + task4.getFinish());
        logger.info("task1 start: " + task1.getStart() + " finish:" + task1.getFinish());
        logger.info("task3 start: " + task3.getStart() + " finish:" + task3.getFinish());
        logger.info("task2 start: " + task2.getStart() + " finish:" + task2.getFinish());
        logger.info("task5 start: " + task5.getStart() + " finish:" + task5.getFinish());
        Assert.assertEquals(task4.getStart().toString(), "Mon May 05 09:00:00 BST 2008");
        Assert.assertEquals(task4.getFinish().toString(), "Wed May 07 10:39:00 BST 2008");
        Assert.assertEquals(task1.getStart().toString(), "Mon May 05 09:00:00 BST 2008");
        Assert.assertEquals(task1.getFinish().toString(), "Wed May 07 10:39:00 BST 2008");
        Assert.assertEquals(task3.getStart().toString(), "Mon May 05 09:00:00 BST 2008");
        Assert.assertEquals(task3.getFinish().toString(), "Wed May 07 10:39:00 BST 2008");
        Assert.assertEquals(task2.getStart().toString(), "Mon May 05 09:00:00 BST 2008");
        Assert.assertEquals(task2.getFinish().toString(), "Wed May 07 10:39:00 BST 2008");
    }

    public void fixedDuration() {
        ResourcedTask task1 = createTask(1, "t1");
        ResourcedTask task2 = createTask(2, "t2");
        leveller.addCandidate(task1);
        leveller.addCandidate(task2);
        task1.assignResource(resources[0], 1);
        task2.assignResource(resources[0], 1);
        task1.setPriority(1000);
        task2.setPriority(500);
        task2.setFixedDuration(true);
        task2.setDuration(1.5);
        Calendar cal = new GregorianCalendar();
        cal.set(2008, 4, 5);
        leveller.setLevelDate(cal.getTime());
        leveller.level();
        ((IndividualResource) resources[0]).setProfile(leveller.getLevelDate(), 100);
        Assert.assertEquals(task1.getStart().toString(), "Mon May 05 09:00:00 BST 2008");
        Assert.assertEquals(task1.getFinish().toString(), "Wed May 07 10:39:00 BST 2008");
        Assert.assertEquals(task2.getStart().toString(), "Thu May 08 09:00:00 BST 2008");
        Assert.assertEquals(task2.getFinish().toString(), "Fri May 09 12:29:00 BST 2008");
    }

    public void bookFullTaskFixedWorkOnly() throws ResourceAssignmentException {
        ProjectTask task = new ProjectTask();
        task.setWorkRemaining(1000);
        task.assignResource(resources[0], 1);
        Q3Calendar cal = new Q3Calendar();
        cal.set(2008, 4, 5);
        leveller.setLevelDate(cal.getTime());
        ((IndividualResource) resources[0]).setProfile(leveller.getLevelDate(), 100);
        leveller.bookFullTask(task);
        Assert.assertTrue(task.isFullyAllocated());
        IndividualResource ir = (IndividualResource) resources[0];
        Assert.assertEquals(ir.getTotalEffortBooked(), 1000);
        Assert.assertEquals(ir.timeBooked(cal), 450);
        cal.incDay();
        Assert.assertEquals(ir.timeBooked(cal), 450);
        cal.incDay();
        Assert.assertEquals(ir.timeBooked(cal), 100);
        Assert.assertEquals(sdf.format(task.getStart()), "05 May 08 09:00:00");
        Assert.assertEquals(sdf.format(task.getFinish()), "07 May 08 10:39:00");
    }

    public void bookFixedDurationTask() throws ResourceAssignmentException {
        ResourcedTask task = new ProjectTask();
        task.assignResource(resources[0], 1);
        task.setFixedDuration(true);
        task.setDuration(2);
        Calendar cal = new GregorianCalendar();
        cal.set(2008, 4, 5);
        leveller.setLevelDate(cal.getTime());
        ((IndividualResource) resources[0]).setProfile(leveller.getLevelDate(), 100);
        leveller.bookFullTask(task);
        Assert.assertEquals(sdf.format(task.getStart()), "05 May 08 09:00:00");
        Assert.assertEquals(sdf.format(task.getFinish()), "06 May 08 16:29:00");
    }

    public void resourceNotAssigned() {
        SummaryTask st = new ProjectSummaryTask();
        ResourcedTask rt = new ProjectTask();
        rt.setParentTask(st);
        rt.setWorkRemaining(1000);
        rt.assignResource(resources[0], 0);
        leveller.addCandidate(st);
        leveller.level();
    }

    /**
	 * Duration is not a whole number
	 * 
	 * @throws ResourceAssignmentException
	 */
    public void bookFixedDurationTask_partDay() throws ResourceAssignmentException {
        ResourcedTask task = new ProjectTask();
        task.assignResource(resources[0], 1);
        task.setFixedDuration(true);
        task.setDuration(2.5);
        task.setName("bookFixedDurationTask_partDay");
        Q3Calendar cal = new Q3Calendar();
        cal.set(2008, 4, 5);
        leveller.setLevelDate(cal.getTime());
        ((IndividualResource) resources[0]).setProfile(leveller.getLevelDate(), 100);
        leveller.bookFullTask(task);
        Assert.assertEquals(resources[0].timeBooked(cal), 450);
        cal.incDay();
        Assert.assertEquals(resources[0].timeBooked(cal), 450);
        cal.incDay();
        Assert.assertEquals(resources[0].timeBooked(cal), 225);
        Assert.assertEquals(sdf.format(task.getStart()), "05 May 08 09:00:00");
        Assert.assertEquals(sdf.format(task.getFinish()), "07 May 08 12:44:00");
    }

    public void bookFixedDurationTask_bookingConflict() throws ResourceAssignmentException {
        ResourcedTask task = new ProjectTask();
        task.assignResource(resources[0], 1);
        task.setFixedDuration(true);
        task.setDuration(2.5);
        task.setName("bookFixedDurationTask_bookingConflict");
        Q3Calendar cal = new Q3Calendar();
        cal.set(2008, 4, 5);
        leveller.setLevelDate(cal.getTime());
        ((IndividualResource) resources[0]).setProfile(leveller.getLevelDate(), 100);
        cal.incDay();
        resources[0].bookEffort(20, cal, task);
        leveller.bookFullTask(task);
        Assert.assertEquals(sdf.format(task.getStart()), "07 May 08 09:00:00");
        Assert.assertEquals(sdf.format(task.getFinish()), "09 May 08 12:29:00");
    }

    /**
	 * When an adjustment has been made to the base calendar which prevents a
	 * booking being made, the task should continue in the same way as it would
	 * over a weekend - no work is done but the task is considered continuous
	 * 
	 * @throws ResourceAssignmentException
	 */
    public void bookFixedDurationTask_holiday() throws ResourceAssignmentException {
        ResourcedTask task = new ProjectTask();
        task.assignResource(resources[0], 1);
        task.setFixedDuration(true);
        task.setDuration(2.5);
        task.setName("bookFixedDurationTask_holiday");
        Calendar cal = new GregorianCalendar();
        cal.set(2008, 4, 5);
        leveller.setLevelDate(cal.getTime());
        ((IndividualResource) resources[0]).setProfile(leveller.getLevelDate(), 100);
        Calendar publicHoliday = new GregorianCalendar();
        publicHoliday.set(2008, 4, 6);
        baseCalendar.addAdjustment(publicHoliday, true, 0);
        leveller.bookFullTask(task);
        Assert.assertEquals(sdf.format(task.getStart()), "05 May 08 09:00:00");
        Assert.assertEquals(sdf.format(task.getFinish()), "08 May 08 12:44:00");
    }

    /**
	 * Predecessor holds up a task because it isn't finished. Test by making
	 * successor higher priority using the same resource
	 */
    public void predecessorDelay() {
        ResourcedTask rt1 = createResourcedTask(1, "predecessor", 450);
        ResourcedTask rt2 = createResourcedTask(2, "successor", 450);
        rt2.addPredecessor(rt1);
        ResourcedTask rt3 = createResourcedTask(3, "standalone med priority", 450);
        rt1.assignResource(resources[0], 1);
        rt2.assignResource(resources[0], 1);
        rt3.assignResource(resources[0], 1);
        rt1.setPriority(100);
        rt2.setPriority(1000);
        rt3.setPriority(200);
        Calendar cal = new GregorianCalendar();
        cal.set(2008, 4, 5);
        leveller.setLevelDate(cal.getTime());
        logger.info("Level date set to " + leveller.getLevelDate());
        leveller.addCandidate(rt1);
        leveller.addCandidate(rt2);
        leveller.addCandidate(rt3);
        leveller.level();
        Assert.assertEquals(sdf.format(rt3.getStart()), "05 May 08 09:00:00");
        Assert.assertEquals(sdf.format(rt1.getStart()), "06 May 08 09:00:00");
        Assert.assertEquals(sdf.format(rt2.getStart()), "07 May 08 09:00:00");
    }

    /**
	 * Uses UoB MasterPlan as a test source, importing from XML and then
	 * levelling. Obviously therefore requires import to work, but does also add
	 * a bit of testing of the import routine
	 */
    @Test(enabled = true, groups = { "heapEater" })
    public void bigLevel() {
        ImportAction ia = new ImportAction();
        ia.setImportDb(dbc, false);
        ia.run();
        logger.info(dbc.isOpen());
        dbc.open();
        Model.setDbc(dbc);
        Model.taskManager().setRoot((SummaryTask) dbc.getTaskDao().findFirstByName("Master Plan"));
        Model.level(null);
        TaskStatusReport tsr = new TaskStatusReport();
        tsr.setLevelDate(Model.getLevelDate());
        Model.taskManager().getRoot().reportStatus(tsr);
        logger.info(">>>>>>>>>>>> TSR >>>>>>>>>>>>>");
        StringBuffer s = new StringBuffer();
        tsr.output(s, 0);
        System.out.print(s.toString());
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<");
        TaskAnalysisResult tar;
        tar = TaskAnalysisResult.NOT_RESOURCED;
        System.out.println(tar + "  " + tsr.detail(tar));
        Assert.assertEquals(tsr.getResults(tar), 0);
        tar = TaskAnalysisResult.ZERO_ASSIGNMENT;
        System.out.println(tar + "  " + tsr.detail(tar));
        Assert.assertEquals(tsr.getResults(tar), 0);
        tar = TaskAnalysisResult.RESOURCE_GROUP_EMPTY;
        System.out.println(tar + "  " + tsr.detail(tar));
        Assert.assertEquals(tsr.getResults(tar), 0);
        tar = TaskAnalysisResult.NOT_PLANNED;
        System.out.println(tar + "  " + tsr.detail(tar));
        List<Task> detail = tsr.detail(tar);
        for (Task task : detail) {
            ResourcedTask rt = (ResourcedTask) task;
            System.out.println(rt.getTaskId());
        }
        Assert.assertEquals(tsr.getResults(tar), 0);
        tar = TaskAnalysisResult.PLANNED_TOO_EARLY;
        System.out.println(tar + "  " + tsr.detail(tar));
        Assert.assertEquals(tsr.getResults(tar), 0);
    }

    private ProjectTask createTask(int id, String name) {
        ProjectTask task = new ProjectTask();
        task.setName(name);
        task.setWorkRemaining(1000);
        return task;
    }

    private ResourcedTask createResourcedTask(int id, String name, int work) {
        ResourcedTask task = new ProjectTask();
        task.setName(name);
        task.setWorkRemaining(work);
        return task;
    }

    private SummaryTask createSummaryTask(int id, String name) {
        SummaryTask task = new ProjectSummaryTask();
        task.setName(name);
        return task;
    }

    private Resource createResource(int id, String name) {
        IndividualResource resource = new IndividualResource();
        resource.setName(name);
        resource.setResourceId(id);
        resource.getResourceCalendar().setBaseCalendar(baseCalendar);
        resourcePool.addResource(resource);
        return resource;
    }

    private void buildPlan() {
        int i = 1;
        task1 = createSummaryTask(i++, "Task:1");
        task11 = createSummaryTask(i++, "Task:11");
        task12 = createTask(i++, "Task:12");
        task111 = createTask(i++, "Task:111");
        task2 = createSummaryTask(i++, "Task:2");
        task21 = createSummaryTask(i++, "Task:21");
        task22 = createTask(i++, "Task:22");
        task211 = createSummaryTask(i++, "Task:211");
        task2111 = createTask(i++, "Task:2111");
        task3 = createTask(i++, "Task:3");
        task11.setParentTask(task1);
        task12.setParentTask(task1);
        task111.setParentTask(task11);
        task21.setParentTask(task2);
        task22.setParentTask(task2);
        task211.setParentTask(task21);
        task2111.setParentTask(task211);
        task12.addPredecessor(task111);
        task2111.addPredecessor(task22);
    }

    private void buildResources() {
        resourcePool = new ResourcePool();
        String[] names = new String[] { "dsowerby", "mpaine", "rvafadari", "ahare", "gryder", "svanage", "ldiniro" };
        for (int i = 0; i < 7; i++) {
            resources[i] = createResource(i, names[i]);
        }
    }

    private void assignResources() {
    }
}
