package org.herasaf.xacml.pdp.persistence.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.herasaf.xacml.DataIntegrityException;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.impl.EvaluatableIDImpl;
import org.herasaf.xacml.core.policy.impl.PolicyType;
import org.herasaf.xacml.pdp.persistence.DataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class PersistenceManagerGeneralTest extends PersistenceManagerTest {

    private String deleteAllStatementForCleanUp;

    @BeforeClass
    public void init() throws Exception {
        super.initData();
        Field field = XMLDatabasePersistenceManager.class.getDeclaredField("deleteAllStatement");
        field.setAccessible(true);
        deleteAllStatementForCleanUp = (String) field.get(manager);
    }

    @Override
    @AfterClass
    public void after() throws Exception {
        ((XMLDatabasePersistenceManager) manager).setDeleteAllStatement(deleteAllStatementForCleanUp);
        manager.deleteAll();
    }

    ;

    @DirtiesContext
    @Test(expectedExceptions = { DataAccessException.class })
    public void testWrongArgumentInLoad() throws Throwable {
        ((XMLDatabasePersistenceManager) manager).setSelectStatement("wrongStatement");
        manager.load(new EvaluatableIDImpl("123"));
    }

    @DirtiesContext
    @Test(expectedExceptions = { DataAccessException.class })
    public void testWrongArgumentInDelete() throws Exception {
        ((XMLDatabasePersistenceManager) manager).setDeleteAllStatement("wrongStatement");
        manager.deleteAll();
    }

    @DirtiesContext
    @Test(expectedExceptions = { DataAccessException.class })
    public void testWrongTypeInLoadAllPolicies() throws Exception {
        ((XMLDatabasePersistenceManager) manager).setSelectAllStatement("wrongStatement");
        manager.loadAll();
    }

    @DirtiesContext
    @Test(expectedExceptions = { DataAccessException.class })
    public void testWrongStatementInSaveAll() throws Exception {
        ((XMLDatabasePersistenceManager) manager).setInsertStatement("wrongStatement");
        List<Evaluatable> list = new ArrayList<Evaluatable>();
        PolicyType p = new PolicyType();
        p.setPolicyId("123");
        list.add(p);
        manager.persistAll(list);
    }

    @DirtiesContext
    @Test(expectedExceptions = { DataAccessException.class })
    public void testWrongStatementInSavePolicy() throws Exception {
        ((XMLDatabasePersistenceManager) manager).setInsertStatement("wrongStatment;");
        PolicyType p = new PolicyType();
        p.setPolicyId("123");
        manager.persist(p);
    }

    @DirtiesContext
    @Test(expectedExceptions = { DataAccessException.class })
    public void testWrongStatementInDeletePolicy() throws Exception {
        ((XMLDatabasePersistenceManager) manager).setDeleteStatement("wrongStatment;");
        manager.delete(new EvaluatableIDImpl(P_ID1));
    }

    @DirtiesContext
    @Test(expectedExceptions = { DataAccessException.class })
    public void testWrongStatementInDeleteAllPolicies() throws Exception {
        ((XMLDatabasePersistenceManager) manager).setDeleteAllStatement("wrongStatment;");
        manager.deleteAll();
    }
}
