package net.sf.bulimgr.persistance.daoSpring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import net.sf.bulimgr.persistance.GameResult;
import net.sf.bulimgr.persistance.GoalScorer;
import net.sf.bulimgr.persistance.PlayerSaison;
import net.sf.bulimgr.persistance.dao.GameResultDAO;
import net.sf.bulimgr.persistance.dao.GoalScorerDAO;
import net.sf.bulimgr.persistance.dao.PlayerSaisonDAO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import com.carbonfive.testutils.spring.dbunit.DataSet;

/**
 * @author Detlev Struebig
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:CommonDAOTest-context.xml")
@Transactional
@DataSet(setupOperation = "CLEAN_INSERT", value = "classpath:/dbunit/daogoalscorer.db.xml")
public class GoalScorerDAOTest extends BaseDaoTest {

    @Resource
    private GoalScorerDAO dao;

    @Resource
    private GameResultDAO daoGameResult;

    @Resource
    private PlayerSaisonDAO daoPlayerSaison;

    private DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    /**
	 * @throws java.lang.Exception
	 */
    @Before
    public void setUp() throws Exception {
    }

    /**
	 * @throws java.lang.Exception
	 */
    @After
    public void tearDown() throws Exception {
    }

    /**
	 * Test method for {@link net.sf.bulimgr.persistance.dao.GoalScorerDAO#findByGame(long)}.
	 */
    @Test
    public void testFindByGameResult() {
        assertNotNull(dao);
        long idGame = 1;
        List<GoalScorer> list = dao.findByGameResult(idGame);
        assertNotNull(list);
        assertEquals(4, list.size());
    }

    /**
	 * Test method for {@link net.sf.bulimgr.persistance.dao.GoalScorerDAO#findByPlayerSaison(long)}.
	 */
    @Test
    public void testFindByPlayerSaison() {
        assertNotNull(dao);
        long idPlayerSaison = 1;
        List<GoalScorer> list = dao.findByPlayerSaison(idPlayerSaison);
        assertNotNull(list);
        assertEquals(2, list.size());
        idPlayerSaison = 2;
        list = dao.findByPlayerSaison(idPlayerSaison);
        assertNotNull(list);
        assertEquals(2, list.size());
    }

    /**
	 * Test method for {@link net.sf.bulimgr.persistance.dao.GoalScorerDAO#findByDate(java.util.Date)}.
	 * @throws ParseException 
	 */
    @Test
    public void testFindByDate() throws ParseException {
        assertNotNull(dao);
        Date goalDate = dateFormat.parse("01.08.2010");
        List<GoalScorer> list = dao.findByDate(goalDate);
        assertNotNull(list);
        assertEquals(4, list.size());
        goalDate = dateFormat.parse("01.08.1990");
        list = dao.findByDate(goalDate);
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    /**
	 * Test method for {@link net.sf.bulimgr.persistance.dao.GoalScorerDAO#findByDateYear(..)}.
	 * @throws ParseException 
	 */
    @Test
    public void testFindByDateYear() throws ParseException {
        assertNotNull(dao);
        int year = 2010;
        List<GoalScorer> list = dao.findByDateYear(year);
        assertNotNull(list);
        assertEquals(4, list.size());
        year = 1990;
        list = dao.findByDateYear(year);
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    /**
	 * Test method for {@link net.sf.bulimgr.persistance.dao.GoalScorerDAO#findByDateYearMonth(..,..)}.
	 * @throws ParseException 
	 */
    @Test
    public void testFindByDateYearMonth() throws ParseException {
        assertNotNull(dao);
        int year = 2010;
        int month = 8;
        List<GoalScorer> list = dao.findByDateYearMonth(year, month);
        assertNotNull(list);
        assertEquals(4, list.size());
        year = 1990;
        month = 1;
        list = dao.findByDateYearMonth(year, month);
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    /**
	 * Test method for {@link org.synyx.hades.dao.GenericDao#save(java.lang.Object)}.
	 */
    @Test
    public void testSaveT() {
        assertNotNull(dao);
        assertEquals(4, dao.count().longValue());
        assertNotNull(daoGameResult);
        GameResult gameResult = daoGameResult.readByPrimaryKey(new Long(1));
        assertNotNull(daoPlayerSaison);
        PlayerSaison player = daoPlayerSaison.readByPrimaryKey(new Long(1));
        try {
            GoalScorer gpNew = new GoalScorer();
            gpNew.setGameResult(gameResult);
            gpNew.setPlayerSaison(player);
            GoalScorer gpNewRef = dao.save(gpNew);
            assertNotNull(gpNewRef);
            assertEquals(5, gpNewRef.getId());
        } catch (RuntimeException e) {
            throw e;
        }
        assertEquals(5, dao.count().longValue());
    }

    /**
	 * Test method for {@link org.synyx.hades.dao.GenericDao#readByPrimaryKey(java.io.Serializable)}.
	 */
    @Test
    public void testReadByPrimaryKey() {
        assertNotNull(dao);
        GoalScorer gc = dao.readByPrimaryKey(new Long(1));
        assertNotNull(gc);
        gc = dao.readByPrimaryKey(new Long(2));
        assertNotNull(gc);
        gc = dao.readByPrimaryKey(new Long(11));
        assertNull(gc);
    }

    /**
	 * Test method for {@link org.synyx.hades.dao.GenericDao#exists(java.io.Serializable)}.
	 */
    @Test
    public void testExists() {
        assertNotNull(dao);
        long primaryKey = 1;
        assertTrue(dao.exists(primaryKey));
        primaryKey = 21;
        assertFalse(dao.exists(primaryKey));
    }

    /**
	 * Test method for {@link org.synyx.hades.dao.GenericDao#readAll()}.
	 */
    @Test
    public void testReadAll() {
        assertNotNull(dao);
        List<GoalScorer> list = dao.readAll();
        assertNotNull(list);
        assertEquals(4, list.size());
    }

    /**
	 * Test method for {@link org.synyx.hades.dao.GenericDao#count()}.
	 */
    @Test
    public void testCount() {
        assertNotNull(dao);
        Long count = dao.count();
        assertNotNull(count);
        assertEquals(4, count.longValue());
    }

    /**
	 * Test method for {@link org.synyx.hades.dao.GenericDao#deleteAll()}.
	 */
    @Test
    public void testDeleteAll() {
        assertNotNull(dao);
        List<GoalScorer> list = dao.readAll();
        assertNotNull(list);
        assertEquals(4, list.size());
        try {
            dao.deleteAll();
            list = dao.readAll();
            assertNotNull(list);
            assertEquals(0, list.size());
        } catch (RuntimeException e) {
            throw e;
        }
    }
}
