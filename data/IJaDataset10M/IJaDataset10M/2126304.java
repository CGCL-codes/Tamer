package br.gov.framework.demoiselle.persistence.JDBC;

import org.easymock.classextension.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PersistenceJDBCExceptionTst {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testPersistenceJDBCException() {
        try {
            throw new PersistenceJDBCException();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof PersistenceJDBCException);
        }
    }

    @Test
    public void testPersistenceJDBCExceptionString() {
        try {
            throw new PersistenceJDBCException("Test");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof PersistenceJDBCException);
        }
    }

    @Test
    public void testPersistenceJDBCExceptionStringThrowable() {
        Throwable cause = null;
        try {
            cause = EasyMock.createMock(Throwable.class);
            EasyMock.replay(cause);
            throw new PersistenceJDBCException("Test", cause);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof PersistenceJDBCException);
            Assert.assertEquals(cause, e.getCause());
        }
    }
}
