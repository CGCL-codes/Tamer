package wilos.test.hibernate.spem2.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import wilos.hibernate.misc.concretetask.ConcreteTaskDescriptorDao;
import wilos.hibernate.spem2.task.TaskDescriptorDao;
import wilos.model.misc.concretetask.ConcreteTaskDescriptor;
import wilos.model.spem2.task.TaskDescriptor;
import wilos.test.TestConfiguration;

/**
 * Unit test for TaskDescriptorDao
 *
 * @author eperico
 *
 */
public class TaskDescriptorDaoTest {

    private TaskDescriptorDao taskDescriptorDao = (TaskDescriptorDao) TestConfiguration.getInstance().getApplicationContext().getBean("TaskDescriptorDao");

    private ConcreteTaskDescriptorDao concreteTaskDescriptorDao = (ConcreteTaskDescriptorDao) TestConfiguration.getInstance().getApplicationContext().getBean("ConcreteTaskDescriptorDao");

    private TaskDescriptor taskDescriptor = null;

    /**
     * attributes from Element
     */
    public static final String ID = "thisId";

    public static final String NAME = "thisTaskDescriptor";

    public static final String DESCRIPTION = "taskDescriptor description";

    /**
     * attributes from BreakdownElement
     */
    public static final String PREFIX = "prefix";

    public static final Boolean IS_PLANNED = true;

    public static final Boolean HAS_MULTIPLE_OCCURENCES = true;

    public static final Boolean IS_OPTIONAL = true;

    /**
     * attributes from WorkBreakdownElement
     */
    public static final Boolean IS_REPEATABLE = true;

    public static final Boolean IS_ON_GOING = true;

    public static final Boolean IS_EVEN_DRIVEN = true;

    @Before
    public void setUp() {
        this.taskDescriptor = new TaskDescriptor();
        this.taskDescriptor.setName(NAME);
        this.taskDescriptor.setDescription(DESCRIPTION);
        this.taskDescriptor.setPrefix(PREFIX);
        this.taskDescriptor.setIsPlanned(IS_PLANNED);
        this.taskDescriptor.setHasMultipleOccurrences(HAS_MULTIPLE_OCCURENCES);
        this.taskDescriptor.setIsOptional(IS_OPTIONAL);
        this.taskDescriptor.setIsRepeatable(IS_REPEATABLE);
        this.taskDescriptor.setIsOngoing(IS_ON_GOING);
        this.taskDescriptor.setIsEvenDriven(IS_EVEN_DRIVEN);
    }

    @After
    public void tearDown() {
        this.taskDescriptor = null;
    }

    @Test
    public void testSaveOrUpdateTaskDescriptor() {
        String id = this.taskDescriptorDao.saveOrUpdateTaskDescriptor(this.taskDescriptor);
        TaskDescriptor taskDescriptorTmp = (TaskDescriptor) this.taskDescriptorDao.getHibernateTemplate().load(TaskDescriptor.class, id);
        assertNotNull(taskDescriptorTmp);
        this.taskDescriptorDao.deleteTaskDescriptor(this.taskDescriptor);
    }

    @Test
    public void testGetAllTaskDescriptors() {
        this.taskDescriptorDao.saveOrUpdateTaskDescriptor(this.taskDescriptor);
        List<TaskDescriptor> taskDescriptors = this.taskDescriptorDao.getAllTaskDescriptors();
        assertNotNull(taskDescriptors);
        assertTrue(taskDescriptors.size() >= 1);
        this.taskDescriptorDao.deleteTaskDescriptor(this.taskDescriptor);
    }

    @Test
    public void testGetTaskDescriptor() {
        String id = this.taskDescriptorDao.saveOrUpdateTaskDescriptor(this.taskDescriptor);
        ConcreteTaskDescriptor concreteTaskDescriptor = new ConcreteTaskDescriptor();
        concreteTaskDescriptor.setConcreteName("My name");
        this.concreteTaskDescriptorDao.saveOrUpdateConcreteTaskDescriptor(concreteTaskDescriptor);
        this.taskDescriptor.addConcreteTaskDescriptor(concreteTaskDescriptor);
        this.concreteTaskDescriptorDao.saveOrUpdateConcreteTaskDescriptor(concreteTaskDescriptor);
        this.taskDescriptorDao.saveOrUpdateTaskDescriptor(this.taskDescriptor);
        TaskDescriptor taskDescriptorTmp = this.taskDescriptorDao.getTaskDescriptor(id);
        assertNotNull(taskDescriptorTmp);
        assertEquals("Name", taskDescriptorTmp.getName(), NAME);
        assertEquals("Description", taskDescriptorTmp.getDescription(), DESCRIPTION);
        assertEquals("Prefix", taskDescriptorTmp.getPrefix(), PREFIX);
        assertEquals("IsPlanned", taskDescriptorTmp.getIsPlanned(), IS_PLANNED);
        assertEquals("HasMultipleOccurences", taskDescriptorTmp.getHasMultipleOccurrences(), HAS_MULTIPLE_OCCURENCES);
        assertEquals("IsOptional", taskDescriptorTmp.getIsOptional(), IS_OPTIONAL);
        assertEquals("IsRepeatable", taskDescriptorTmp.getIsRepeatable(), IS_REPEATABLE);
        assertEquals("IsOnGoing", taskDescriptorTmp.getIsOngoing(), IS_ON_GOING);
        assertEquals("IsEvenDriven", taskDescriptorTmp.getIsEvenDriven(), IS_EVEN_DRIVEN);
        this.concreteTaskDescriptorDao.deleteConcreteTaskDescriptor(concreteTaskDescriptor);
        this.taskDescriptorDao.deleteTaskDescriptor(this.taskDescriptor);
    }

    @Test
    public void testDeleteTaskDescriptor() {
        String id = this.taskDescriptorDao.saveOrUpdateTaskDescriptor(this.taskDescriptor);
        this.taskDescriptorDao.deleteTaskDescriptor(this.taskDescriptor);
        TaskDescriptor taskDescriptorTmp = (TaskDescriptor) this.taskDescriptorDao.getHibernateTemplate().get(TaskDescriptor.class, id);
        assertNull(taskDescriptorTmp);
    }
}
