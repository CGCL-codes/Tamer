package wilos.test.hibernate.spem2.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import wilos.hibernate.spem2.task.StepDao;
import wilos.model.spem2.task.Step;
import wilos.model.spem2.task.TaskDefinition;
import wilos.test.TestConfiguration;

/**
 * Test for DAO That manage Step objets
 * 
 * @author garwind
 * 
 */
public class StepDaoTest {

    private StepDao stepDao = (StepDao) TestConfiguration.getInstance().getApplicationContext().getBean("StepDao");

    ;

    private Step step = null;

    /**
     * attributes from Element
     */
    public static final String ID = "thisId";

    public static final String NAME = "thisStep";

    public static final String DESCRIPTION = "step";

    @Before
    public void setUp() {
        this.step = new Step();
        this.step.setName(NAME);
        this.step.setDescription(DESCRIPTION);
    }

    @After
    public void tearDown() {
        this.step = null;
    }

    @Test
    public void testSaveOrUpdateStep() {
        String id = this.stepDao.saveOrUpdateStep(this.step);
        TaskDefinition taskTmp = (TaskDefinition) this.stepDao.getHibernateTemplate().load(TaskDefinition.class, id);
        assertNotNull(taskTmp);
        this.stepDao.deleteStep(this.step);
    }

    @Test
    public void testGetAllSteps() {
        this.stepDao.getHibernateTemplate().saveOrUpdate(this.step);
        List<Step> steps = this.stepDao.getAllSteps();
        assertNotNull(steps);
        assertTrue(steps.size() >= 1);
        this.stepDao.deleteStep(this.step);
    }

    @Test
    public void testGetStep() {
        String id = this.stepDao.saveOrUpdateStep(this.step);
        Step sectionTmp = this.stepDao.getStep(id);
        assertNotNull(sectionTmp);
        assertEquals("Name", sectionTmp.getName(), NAME);
        assertEquals("Description", sectionTmp.getDescription(), DESCRIPTION);
        this.stepDao.getHibernateTemplate().delete(step);
    }

    @Test
    public void testDeleteTask() {
        String id = this.stepDao.saveOrUpdateStep(this.step);
        this.stepDao.deleteStep(this.step);
        Step stepTmp = (Step) this.stepDao.getStep(id);
        assertNull(stepTmp);
    }
}
