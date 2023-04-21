package net.sf.drawbridge.dao;

import java.util.List;
import junit.framework.TestCase;
import net.sf.drawbridge.test.TestBeanFactory;
import net.sf.drawbridge.vo.Driver;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class DriverDaoTest extends TestCase {

    private DriverDao target;

    private AbstractPlatformTransactionManager txManager;

    protected void setUp() {
        txManager = (AbstractPlatformTransactionManager) TestBeanFactory.getBean("TxManager");
        target = (DriverDao) TestBeanFactory.getBean("DriverDao");
    }

    public void testShouldGetDriverObject() throws Exception {
        Driver result = target.get(0);
        assertNotNull("result is null", result);
        assertEquals("id", new Integer(0), result.getId());
        assertEquals("class name", "org.hsqldb.jdbcDriver", result.getClassName());
        assertEquals("name", "HSQLDB Driver", result.getName());
    }

    public void testShouldCreateNewDriverObject() throws Exception {
        TransactionStatus tx = txManager.getTransaction(new DefaultTransactionDefinition());
        try {
            Driver driver = new Driver(null, "name", "class_name");
            target.create(driver);
            assertNotNull("id is still null", driver.getId());
        } finally {
            txManager.rollback(tx);
        }
    }

    public void testShouldSaveChangesToDriverObject() throws Exception {
        TransactionStatus tx = txManager.getTransaction(new DefaultTransactionDefinition());
        try {
            Driver existing = target.get(0);
            existing.setName("a different name");
            target.update(existing);
            Driver result = target.get(0);
            assertEquals("a different name", result.getName());
        } finally {
            txManager.rollback(tx);
        }
    }

    public void testShouldDeleteObjectFromDriver() throws Exception {
        TransactionStatus tx = txManager.getTransaction(new DefaultTransactionDefinition());
        try {
            Driver existing = target.get(0);
            assertNotNull(existing);
            target.delete(existing);
            assertNull("should be deleted", target.get(0));
        } finally {
            txManager.rollback(tx);
        }
    }

    public void testShouldRetrieveAllObjectsFromDriver() throws Exception {
        TransactionStatus tx = txManager.getTransaction(new DefaultTransactionDefinition());
        try {
            Driver driver = new Driver(null, "name", "class_name");
            target.create(driver);
            List<Driver> all = target.getAll();
            assertEquals(2, all.size());
        } finally {
            txManager.rollback(tx);
        }
    }
}
