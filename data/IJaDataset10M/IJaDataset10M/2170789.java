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
public class ScriptTest {

    public ScriptTest() {
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
     * Test of getCommand method, of class Script.
     */
    @Test
    public void testGetCommand() {
    }

    /**
     * Test of setCommand method, of class Script.
     */
    @Test
    public void testSetCommand() {
    }

    @Test
    public void testSave() {
        org.hibernate.Session session = com.acgvision.core.model.HibernateUtil.getSessionFactory().openSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        session.save(new Script());
        tx.commit();
        session.close();
    }

    @Test
    public void testCleaner() {
        Script j = new Script();
        Script result = null;
        result = (Script) j.clean();
        j.setName("test");
        j.setCommand(new Command());
        result = (Script) j.clean();
        assertTrue(j.getName().equals(result.getName()));
        assertTrue(j.getCommand().equals(result.getCommand()));
    }
}
