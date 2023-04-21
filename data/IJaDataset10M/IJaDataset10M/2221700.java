package net.sf.brightside.qualifications.core.spring;

import net.sf.brightside.qualifications.core.persistence.PersistenceManager;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public abstract class AbstractSpringTest extends AbstractTransactionalDataSourceSpringContextTests {

    private Object underTest;

    protected abstract Object createUnderTest();

    private PersistenceManager<?> persistenceManager;

    public PersistenceManager<?> getPersistenceManager() {
        return persistenceManager;
    }

    public void setPersistenceManager(PersistenceManager<?> persistenceManager) {
        this.persistenceManager = persistenceManager;
    }

    @Override
    protected String[] getConfigLocations() {
        return new ApplicationContextProviderSingleton().getContextLocations();
    }

    @Override
    public void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        underTest = createUnderTest();
    }

    @BeforeMethod
    public void beforeMethod() throws Exception {
        setUp();
    }

    @AfterMethod
    public void afterMethod() throws Exception {
        tearDown();
    }

    @Test
    public void testExist() {
        assertNotNull(underTest);
    }
}
