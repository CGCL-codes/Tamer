package cz.kibo.ekonom.service;

import cz.kibo.ekonom.model.Question;
import cz.kibo.ekonom.model.Testset;
import cz.kibo.ekonom.model.TestsetResult;
import cz.kibo.ekonom.model.Verdict;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.*;

/**
 *
 * @author tomas
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:applicationContext.xml" })
public class TestsetResultDaoTest {

    private final Integer TESTSET_ID = 1;

    @Autowired
    private TestsetDao testsetDao;

    @Autowired
    private TestsetResultDao testsetResultDao;

    @Autowired
    private Evaluation evaluation;

    @Test
    public void testSaveResult() {
        System.out.println("saveResult");
        Testset ts = testsetDao.findById(TESTSET_ID);
        ts.getStudent().setFirstname("Tomáš");
        ts.getStudent().setLastname("Jurman");
        ts.getStudent().setGroup("g2");
        for (Question question : ts.getQuestions()) {
            question.getItems().get(0).setChecked(true);
        }
        Verdict ver = evaluation.evaluate(ts);
        Integer testsetResultId = testsetResultDao.saveTestsetResult(ts, ver);
        assertNotNull(testsetResultId);
        assertNotNull(testsetResultDao.findById(testsetResultId).getTestset().getQuestions().get(0).getItems().get(0).getText());
    }

    /**
     * Test of allTestResults method, of class TestsetResultDao.
     */
    @Test
    public void testAllTestResults() {
        System.out.println("allTestResults");
        for (TestsetResult tr : testsetResultDao.allTestResults(TESTSET_ID)) {
            assertNotNull(tr.getTestset().getQuestions().get(0).getItems().get(0).getText());
            System.out.println(tr);
        }
        assertNotNull(testsetResultDao.allTestResults(TESTSET_ID));
    }

    /**
     * Test of deleteAll method, of class TestsetResultDao.
     */
    @Test
    public void testDeleteAll() {
        System.out.println("deleteAll");
        testsetResultDao.deleteAll(TESTSET_ID);
        assertTrue(testsetResultDao.allTestResults(TESTSET_ID).size() == 0);
    }
}
