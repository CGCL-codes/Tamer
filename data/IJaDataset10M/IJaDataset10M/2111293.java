package com.acgvision.core.model;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Rémi Debay <remi.debay@acgcenter.com>
 */
public class LimitValueTest {

    public LimitValueTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getValue method, of class LimitValue.
     */
    @Test
    public void testGetValue() {
    }

    /**
     * Test of setValue method, of class LimitValue.
     */
    @Test
    public void testSetValue() {
    }

    /**
     * Test of getMonitor method, of class LimitValue.
     */
    @Test
    public void testGetMonitor() {
    }

    /**
     * Test of setMonitor method, of class LimitValue.
     */
    @Test
    public void testSetMonitor() {
    }

    @Test
    public void testSave() {
        org.hibernate.Session session = com.acgvision.core.model.HibernateUtil.getSessionFactory().openSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        session.save(new LimitValue());
        tx.commit();
        session.close();
    }
}
