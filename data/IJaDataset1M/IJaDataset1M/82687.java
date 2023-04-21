package net.sf.bulimgr.persistance.daoSpring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.util.List;
import javax.annotation.Resource;
import net.sf.bulimgr.persistance.Game;
import net.sf.bulimgr.persistance.GamePlan;
import net.sf.bulimgr.persistance.dao.GameDAO;
import net.sf.bulimgr.persistance.dao.GamePlanDAO;
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
@DataSet(setupOperation = "CLEAN_INSERT", value = "classpath:/dbunit/daogame.db.xml")
public class GameDAOTest extends BaseDaoTest {

    @Resource
    private GameDAO dao;

    @Resource
    private GamePlanDAO daoGamePlan;

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
	 * Test method for {@link net.sf.bulimgr.persistance.dao.GameDAO#findByHomeTeam(long)}.
	 */
    @Test
    public void testFindByHomeTeam() {
        assertNotNull(dao);
        long teamSaisonid = 1;
        List<Game> games = dao.findByHomeTeam(teamSaisonid);
        assertNotNull(games);
    }

    /**
	 * Test method for {@link net.sf.bulimgr.persistance.dao.GameDAO#findByGuestTeam(long)}.
	 */
    @Test
    public void testFindByGuestTeam() {
        assertNotNull(dao);
        long teamSaisonid = 2;
        List<Game> games = dao.findByGuestTeam(teamSaisonid);
        assertNotNull(games);
    }

    /**
	 * Test method for {@link net.sf.bulimgr.persistance.dao.GameDAO#findByHomeTeamIsNull()}.
	 */
    @Test
    public void testFindByHomeTeamIsNull() {
        assertNotNull(dao);
        List<Game> games = dao.findByHomeTeamIsNull();
        assertNotNull(games);
    }

    /**
	 * Test method for {@link net.sf.bulimgr.persistance.dao.GameDAO#findByGuestTeamIsNull()}.
	 */
    @Test
    public void testFindByGuestTeamIsNull() {
        assertNotNull(dao);
        List<Game> games = dao.findByGuestTeamIsNull();
        assertNotNull(games);
    }

    /**
	 * Test method for {@link net.sf.bulimgr.persistance.dao.GameDAO#findByGamePlan(long)}.
	 */
    @Test
    public void testFindByGamePlan() {
        assertNotNull(dao);
        long gamePlanid = 1;
        List<Game> list = dao.findByGamePlan(gamePlanid);
        assertNotNull(list);
        assertEquals(4, list.size());
        gamePlanid = 2;
        list = dao.findByGamePlan(gamePlanid);
        assertNotNull(list);
        assertEquals(2, list.size());
    }

    /**
	 * Test method for {@link org.synyx.hades.dao.GenericDao#save(java.lang.Object)}.
	 */
    @Test
    public void testSaveT() {
        assertNotNull(dao);
        assertNotNull(daoGamePlan);
        assertEquals(6, dao.count().longValue());
        GamePlan gameplan = daoGamePlan.readByPrimaryKey(new Long(1));
        try {
            Game gpNew = new Game();
            gpNew.setGamePlan(gameplan);
            Game gpNewRef = dao.save(gpNew);
            assertNotNull(gpNewRef);
            assertEquals(7, gpNewRef.getId());
        } catch (RuntimeException e) {
            throw e;
        }
        assertEquals(7, dao.count().longValue());
    }

    /**
	 * Test method for {@link org.synyx.hades.dao.GenericDao#readByPrimaryKey(java.io.Serializable)}.
	 */
    @Test
    public void testReadByPrimaryKey() {
        assertNotNull(dao);
        Game game = dao.readByPrimaryKey(new Long(1));
        assertNotNull(game);
        game = dao.readByPrimaryKey(new Long(2));
        assertNotNull(game);
        game = dao.readByPrimaryKey(new Long(11));
        assertNull(game);
    }

    /**
	 * Test method for {@link org.synyx.hades.dao.GenericDao#exists(java.io.Serializable)}.
	 */
    @Test
    public void testExists() {
        assertNotNull(dao);
        long idPK = 1;
        assertTrue(dao.exists(idPK));
        idPK = 11;
        assertFalse(dao.exists(idPK));
    }

    /**
	 * Test method for {@link org.synyx.hades.dao.GenericDao#readAll()}.
	 */
    @Test
    public void testReadAll() {
        assertNotNull(dao);
        assertEquals(6, dao.readAll().size());
    }

    /**
	 * Test method for {@link org.synyx.hades.dao.GenericDao#count()}.
	 */
    @Test
    public void testCount() {
        assertNotNull(dao);
        assertEquals(6, dao.count().longValue());
    }

    /**
	 * Test method for {@link org.synyx.hades.dao.GenericDao#deleteAll()}.
	 */
    @Test
    public void testDeleteAll() {
        assertNotNull(dao);
        List<Game> list = dao.readAll();
        assertNotNull(list);
        assertEquals(6, list.size());
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
