package org.herasaf.xacml.pdp.persistence.impl;

import junit.framework.Assert;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.policy.impl.IdReferenceType;
import org.herasaf.xacml.pdp.persistence.EmptyResultException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class PersistenceManagerLoadIdReferenceTest extends PersistenceManagerTest {

    @BeforeClass
    public void init() throws Exception {
        super.initData();
    }

    @Test
    public void testLoadIdReferencePolicy() throws Exception {
        IdReferenceType idReferenceType = new IdReferenceType();
        idReferenceType.setValue(P_ID1);
        Evaluatable evaluatable = manager.load(idReferenceType.getId());
        Assert.assertEquals(policy1.getId().getId(), evaluatable.getId().getId());
    }

    @Test
    public void testLoadIdReferencePolicyVersion() throws Exception {
        IdReferenceType idReferenceType = new IdReferenceType();
        idReferenceType.setValue(P_ID1);
        idReferenceType.setVersion("1.0");
        Evaluatable evaluatable = manager.load(idReferenceType.getId(), idReferenceType.getVersion());
        Assert.assertEquals(policy1.getId().getId(), evaluatable.getId().getId());
    }

    @Test
    public void testLoadIdReferencePolicyVersionEarliest() throws Exception {
        IdReferenceType idReferenceType = new IdReferenceType();
        idReferenceType.setValue(P_ID1);
        idReferenceType.setEarliestVersion("0.9");
        Evaluatable evaluatable = manager.load(idReferenceType.getId(), idReferenceType.getEarliestVersion(), null);
        Assert.assertEquals(policy1.getId().getId(), evaluatable.getId().getId());
    }

    @Test
    public void testLoadIdReferencePolicyVersionLatest() throws Exception {
        IdReferenceType idReferenceType = new IdReferenceType();
        idReferenceType.setValue(P_ID1);
        idReferenceType.setLatestVersion("1.1");
        Evaluatable evaluatable = manager.load(idReferenceType.getId(), null, idReferenceType.getLatestVersion());
        Assert.assertEquals(policy1.getId().getId(), evaluatable.getId().getId());
    }

    @Test
    public void testLoadIdReferencePolicyVersionRange() throws Exception {
        IdReferenceType idReferenceType = new IdReferenceType();
        idReferenceType.setValue(P_ID1);
        idReferenceType.setLatestVersion("1.1");
        idReferenceType.setEarliestVersion("0.9");
        Evaluatable evaluatable = manager.load(idReferenceType.getId(), idReferenceType.getEarliestVersion(), idReferenceType.getLatestVersion());
        Assert.assertEquals(policy1.getId().getId(), evaluatable.getId().getId());
    }

    @Test(expectedExceptions = { EmptyResultException.class })
    public void testLoadIdReferencePolicyVersionWrong() throws Exception {
        IdReferenceType idReferenceType = new IdReferenceType();
        idReferenceType.setValue(P_ID1);
        idReferenceType.setVersion("2.0");
        manager.load(idReferenceType.getId(), idReferenceType.getVersion());
    }

    @Test(expectedExceptions = { EmptyResultException.class })
    public void testLoadIdReferencePolicyVersionEarliestWrong() throws Exception {
        IdReferenceType idReferenceType = new IdReferenceType();
        idReferenceType.setValue(P_ID1);
        idReferenceType.setEarliestVersion("1.1");
        manager.load(idReferenceType.getId(), idReferenceType.getEarliestVersion(), null);
    }

    @Test(expectedExceptions = { EmptyResultException.class })
    public void testLoadIdReferencePolicyVersionLatestWrong() throws Exception {
        IdReferenceType idReferenceType = new IdReferenceType();
        idReferenceType.setValue(P_ID1);
        idReferenceType.setLatestVersion("0.9");
        manager.load(idReferenceType.getId(), null, idReferenceType.getLatestVersion());
    }
}
