package fr.gfi.gfinet.server.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import fr.gfi.gfinet.server.info.Mission;
import fr.gfi.gfinet.server.util.ServerToolsForTest;

/**
 * Test MissionDao class.
 * @author AVCAMPEN
 *
 */
public class MissionDaoTestCase {

    private Mission mission;

    private MissionDao missionDao;

    private EntityManager entityManager = null;

    /**
	 * Executes before each test method.
	 */
    @BeforeMethod
    public void setUp() throws Throwable {
        entityManager = ServerToolsForTest.getEntityManager();
        missionDao = new MissionDao();
        missionDao.setEntityManager(entityManager);
        entityManager.getTransaction().begin();
    }

    /**
	 * Executes after each test method.
	 */
    @AfterMethod
    public void tearDown() throws Throwable {
        EntityTransaction tx = null;
        try {
            tx = entityManager.getTransaction();
            if (tx.isActive()) {
                if (tx.getRollbackOnly()) {
                    tx.rollback();
                } else {
                    tx.commit();
                }
            }
        } catch (Throwable e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            entityManager.close();
            missionDao = null;
        }
    }
}
