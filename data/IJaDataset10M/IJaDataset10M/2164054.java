package net.sf.brightside.eterminals.service;

import net.sf.brightside.eterminals.core.spring.AbstractSpringTest;
import net.sf.brightside.eterminals.facade.GetFacade;
import net.sf.brightside.eterminals.metamodel.Distributer;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class GetTest extends AbstractSpringTest {

    protected GetFacade<Distributer> getUnderTest;

    @Override
    public void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        getUnderTest = createUnderTest();
    }

    @Override
    protected GetFacade<Distributer> createUnderTest() {
        return (GetFacade<Distributer>) getApplicationContext().getBean(GetFacade.class.getName());
    }

    protected Distributer createPersistentObject() {
        return (Distributer) getApplicationContext().getBean(Distributer.class.getName());
    }

    @Test
    public void testIsSingleton() {
        assertTrue(getUnderTest.equals(createUnderTest()));
    }

    @Test(threadPoolSize = 10, invocationCount = 30)
    public void testGetDistributers() {
        getPersistenceManager().save(createPersistentObject());
        assertNotNull(getUnderTest.getAll(Distributer.class));
    }

    @Override
    public void beforeMethod() throws Exception {
    }

    @Override
    public void afterMethod() throws Exception {
    }

    @BeforeClass
    public void beforeClass() throws Exception {
        super.beforeMethod();
    }

    @AfterClass
    public void afterClass() throws Exception {
        super.afterMethod();
    }
}
