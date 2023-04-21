package unit.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import uk.co.q3c.deplan.dao.BaseCalendarDao;
import uk.co.q3c.deplan.dao.DatabaseConnection;
import uk.co.q3c.deplan.dao.ResourceDao;
import uk.co.q3c.deplan.dao.TaskDao;
import uk.co.q3c.deplan.domain.ObjectIdGenerator;
import uk.co.q3c.deplan.domain.resource.BaseCalendar;
import uk.co.q3c.deplan.domain.resource.DefaultResourceException;
import uk.co.q3c.deplan.domain.resource.IndividualResource;
import uk.co.q3c.deplan.domain.resource.InvalidResourceException;
import uk.co.q3c.deplan.domain.resource.Resource;
import uk.co.q3c.deplan.domain.resource.ResourceGroup;
import uk.co.q3c.deplan.domain.resource.ResourcePool;
import uk.co.q3c.deplan.domain.resource.WorkingCalendar;
import uk.co.q3c.deplan.domain.task.ProjectSummaryTask;
import uk.co.q3c.deplan.domain.task.SummaryTask;
import uk.co.q3c.deplan.domain.task.Task;
import uk.co.q3c.deplan.domain.task.TaskAnalysisResult;
import uk.co.q3c.deplan.domain.task.TaskStatusReport;
import uk.co.q3c.deplan.rcp.model.Model;
import uk.co.q3c.deplan.util.InitialisationException;
import uk.co.q3c.deplan.util.XMLImport;
import util.TestUtils;

/**
 * @see XMLImport
 * @author DSowerby 6 Jun 2008 19:47:24
 * 
 */
public class XMLImport_UT {

    class ReadResults {

        int summaryTasks;

        int projects;

        int tasks;
    }

    XMLImport xi;

    protected final transient Logger logger = Logger.getLogger(getClass().getName());

    File f;

    ResourcePool resourcePool;

    List<Task> taskList;

    DatabaseConnection dbc;

    int tasksCreated;

    @BeforeMethod
    public void setupMethod() {
        dbc = TestUtils.dbc(this, true);
        Model.setDbc(dbc);
        f = new File("/home/dave/temp/deplan-import.db4o");
        resourcePool = new ResourcePool();
        resourcePool.setBaseCalendar(new BaseCalendar());
        IndividualResource r1 = new IndividualResource();
        r1.setName("hberrie");
        IndividualResource r2 = new IndividualResource();
        r2.setName("kminogue");
        resourcePool.addResource(r1);
        resourcePool.addResource(r2);
        ResourceGroup group = resourcePool.groupContainingOnly(new IndividualResource[] { r1, r2 });
        resourcePool.renameResource(group, "SysDev");
        taskList = new ArrayList<Task>();
        xi = new XMLImport();
        taskList.clear();
    }

    @AfterMethod
    public void afterMethod() {
        dbc.delete();
        dbc = null;
        resourcePool = null;
        taskList = null;
    }

    @Test
    public void dateFormat() {
        Calendar cal = new GregorianCalendar();
        cal.set(2008, 0, 02, 8, 30, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Assert.assertEquals(sdf.format(cal.getTime()), "2008-01-02T08:30:00");
    }

    @Test(expectedExceptions = { DefaultResourceException.class })
    public void noDefault() throws FileNotFoundException, XMLStreamException {
        xi.setSource("src/unit/util/testData2.xml");
        xi.setResourcePool(resourcePool);
        xi.execute();
        xi.persist(dbc);
    }

    @Test(expectedExceptions = { InitialisationException.class })
    public void resourcePoolNotSet() throws FileNotFoundException, XMLStreamException {
        deleteDatabase();
        xi.setSource("src/unit/util/testData1.xml");
        xi.setDefaultResourceName("SysDev Generic");
    }

    @Test(expectedExceptions = { InvalidResourceException.class })
    public void defaultInvalid() throws FileNotFoundException, XMLStreamException {
        deleteDatabase();
        xi.setSource("src/unit/util/testData1.xml");
        xi.setResourcePool(resourcePool);
        xi.setDefaultResourceName("SysDev Generic");
    }

    @Test(enabled = true)
    public void exec1() throws FileNotFoundException, XMLStreamException {
        xi.setSource("src/unit/util/testData1.xml");
        xi.setResourcePool(resourcePool);
        xi.setDefaultResourceName("SysDev");
        xi.execute();
        xi.persist(dbc);
        Assert.assertEquals(xi.importResults.projects, 1);
        Assert.assertEquals(xi.importResults.tasks, 2);
    }

    @Test(enabled = false)
    public void exec2() throws FileNotFoundException, XMLStreamException {
        deleteDatabase();
        xi.setSource("src/unit/util/testData2.xml");
        xi.setResourcePool(resourcePool);
        xi.setDefaultResourceName("SysDev");
        xi.execute();
        xi.persist(dbc);
        Assert.assertEquals(xi.importResults.newResources, 4);
        Assert.assertEquals(xi.importResults.newResourceGroups, 2);
        Assert.assertEquals(xi.importResults.tasks, 4);
        Assert.assertEquals(xi.importResults.summaryTasks, 0);
        Assert.assertEquals(xi.importResults.projects, 2);
        Assert.assertEquals(resourcePool.individualCount(), 4);
        Assert.assertEquals(resourcePool.groupCount(), 2);
        resourcePool = dbc.getResourceDao().loadResourcePool();
        Assert.assertEquals(resourcePool.individualCount(), 4);
        Assert.assertEquals(resourcePool.groupCount(), 2);
        dbc.close();
        dbc.open();
        resourcePool = dbc.getResourceDao().loadResourcePool();
        Assert.assertEquals(resourcePool.individualCount(), 4);
        Assert.assertEquals(resourcePool.groupCount(), 2);
        TaskDao taskDao = dbc.getTaskDao();
        taskList = taskDao.loadAll();
        logger.info(taskList.toString());
        ReadResults readResults = countTasks(taskList);
        tasksCreated = xi.importResults.projects + xi.importResults.summaryTasks + xi.importResults.tasks;
        Assert.assertEquals(readResults.projects, xi.importResults.projects);
        Assert.assertEquals(readResults.summaryTasks, xi.importResults.summaryTasks);
        Assert.assertEquals(readResults.tasks, xi.importResults.tasks);
        List<Task> tasks = taskDao.findByName("master");
        Assert.assertEquals(tasks.size(), 1);
        Task task = tasks.get(0);
        Assert.assertTrue(task instanceof ProjectSummaryTask);
        SummaryTask st = (SummaryTask) task;
        logger.info(st.getSubTasks().toString());
        Assert.assertEquals(st.getSubTasks().size(), 3);
        tasks = taskDao.findByName("sub project");
        Assert.assertEquals(tasks.size(), 1);
        task = tasks.get(0);
        Assert.assertTrue(task instanceof ProjectSummaryTask);
        st = (SummaryTask) task;
        Assert.assertEquals(st.getSubTasks().toString(), "[master.sub project.subproject  task 1, master.sub project.subproject  task 2]");
        dbc.close();
        dbc.open();
        taskDao = dbc.getTaskDao();
        taskList = taskDao.loadAll();
        Assert.assertEquals(taskList.size(), 6);
        tasks = taskDao.findByName("master");
        Assert.assertEquals(tasks.size(), 1);
        task = tasks.get(0);
        Assert.assertTrue(task instanceof ProjectSummaryTask);
        st = (SummaryTask) task;
        logger.info(st.getSubTasks().toString());
        Assert.assertEquals(st.getSubTasks().size(), 3);
        tasks = taskDao.findByName("sub project");
        Assert.assertEquals(tasks.size(), 1);
        task = tasks.get(0);
        Assert.assertTrue(task instanceof ProjectSummaryTask);
        st = (SummaryTask) task;
        Assert.assertEquals(st.getSubTasks().toString(), "[master.sub project.subproject  task 1, master.sub project.subproject  task 2]");
        tasks = taskDao.findByName("Task three");
        task = tasks.get(0);
        Assert.assertEquals(task.getDuration(), 1.5);
        tasks = taskDao.findByName("Task One");
        task = tasks.get(0);
        Assert.assertEquals(task.getDuration(), (double) 365);
    }

    private ReadResults countTasks(List<Task> list) {
        ReadResults readResults = new ReadResults();
        logger.info("~~~~~~~~~~~ counting tasks ~~~~~~~~~~~~~~~");
        for (Task task : list) {
            switch(task.taskType()) {
                case PROJECT:
                    readResults.projects++;
                    logger.info(task.qualifiedName() + " : " + task.getTaskId());
                    break;
                case PROJECTSUMMARY:
                    readResults.summaryTasks++;
                    break;
                case PROJECTTASK:
                    readResults.tasks++;
                    break;
            }
        }
        return readResults;
    }

    @Test(enabled = false, dependsOnMethods = { "exec2" }, groups = "longImport")
    public void exec3() throws FileNotFoundException, XMLStreamException {
        ObjectIdGenerator.clear();
        xi.setSource("/home/dave/develop/workspace/dePlanTest/src/Master Plan.xml");
        xi.setResourcePool(resourcePool);
        xi.setDefaultResourceName("SysDev");
        xi.execute();
        Assert.assertEquals(xi.globalTasks().size(), 837);
        xi.persist(dbc);
        TaskDao taskDao = dbc.getTaskDao();
        tasksCreated = xi.importResults.projects + xi.importResults.summaryTasks + xi.importResults.tasks;
        logger.info("Import created " + tasksCreated + " tasks");
        List<Task> tasks = taskDao.loadAll();
        logger.info("Total tasks retrieved back " + tasks.size());
        dbc.close();
        dbc.open();
        taskDao = dbc.getTaskDao();
        taskDao.findFirstByName("Master Plan");
        taskList = taskDao.loadAll();
        Assert.assertEquals(taskList.size(), 837);
        BaseCalendarDao bdao = dbc.getBaseCalendarDao();
        WorkingCalendar baseCalendar = bdao.find();
        Assert.assertNotNull(baseCalendar);
        ResourceDao rdao = dbc.getResourceDao();
        Resource r = rdao.findByName("DSowerby");
        Assert.assertTrue(r instanceof IndividualResource);
        IndividualResource ri = (IndividualResource) r;
        Assert.assertEquals(ri.getBaseCalendar(), baseCalendar);
        Assert.assertNotNull(baseCalendar.getBaseWorkingTime());
        taskList = taskDao.findByName("Master Plan");
        Task rootTask = taskList.get(0);
        TaskStatusReport tsr = new TaskStatusReport();
        tsr.setLevelDate(new Date());
        ResourcePool pool = dbc.getResourceDao().loadResourcePool();
        rootTask.reportStatus(tsr);
        StringBuffer s = new StringBuffer();
        tsr.output(s, 0);
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
        dbc.close();
    }

    @Test
    public void getBaseCalendar() {
        BaseCalendar bc;
        xi.setBaseCalendar(null);
        dbc.close();
        bc = xi.getBaseCalendar();
        Assert.assertNotNull(bc);
        Assert.assertNotNull(bc.getBaseWorkingTime());
        bc = new BaseCalendar();
        xi.setBaseCalendar(bc);
        Assert.assertEquals(xi.getBaseCalendar(), bc);
        dbc.delete();
        dbc.open();
        BaseCalendarDao bdao = dbc.getBaseCalendarDao();
        bc = new BaseCalendar();
        bdao.save(bc);
        dbc.commit();
        xi.setBaseCalendar(null);
        Assert.assertEquals(xi.getBaseCalendar(), bc);
    }

    private void deleteDatabase() {
        if (f.exists()) {
            f.delete();
        }
    }
}
