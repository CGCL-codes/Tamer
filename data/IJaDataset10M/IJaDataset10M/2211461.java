package com.rcreations.persist.test;

import com.rcreations.persist.BasePersistService;
import com.rcreations.persist.PersistServiceManager;
import com.rcreations.persist.PersistWrapper;
import com.rcreations.persist.dao.CrudDao;
import java.sql.SQLException;
import java.util.List;
import junit.framework.TestCase;
import org.hsqldb.jdbc.jdbcDataSource;

/**
 */
public class CrudDaoTest extends TestCase {

    jdbcDataSource ds;

    protected void setUp() throws Exception {
        CreateSchemaTest.createNewSchema();
    }

    protected void tearDown() throws SQLException {
        PersistWrapper.stopAllPersist();
    }

    public interface TestServiceInterface {

        public void testMethod();
    }

    public static class TestServiceImpl extends BasePersistService implements TestServiceInterface {

        @Override
        public void testMethod() {
            CrudDao d = getDao(CrudDao.class);
            List<FeatureDb> features = d.readList(FeatureDb.class);
            assertTrue("list size", features.size() > 0);
            FeatureDb f = features.get(0);
            assertNotNull("index 0", f);
            assertNotNull("feature id", f.getId());
            assertNotNull("feature name", f.getFeatureName());
        }
    }

    public void testDaoReadList() {
        PersistServiceManager.getSingleton().registerService(TestServiceInterface.class, TestServiceImpl.class);
        TestServiceInterface s = PersistServiceManager.getSingleton().getService(TestServiceInterface.class);
        s.testMethod();
    }
}
