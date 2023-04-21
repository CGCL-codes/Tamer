package edu.umd.cs.skolli.model;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestGetTestRun {

    static EntityManagerFactory emf = null;

    EntityManager em = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        emf = ModelUtil.dbStartup();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        ModelUtil.dbShutdown();
    }

    @Before
    public void setUp() throws Exception {
        em = emf.createEntityManager();
        QueryUtil.begin(em);
    }

    @After
    public void tearDown() throws Exception {
        QueryUtil.commit(em);
        em.close();
    }

    @Test
    public final void getTestRuns() {
        List l = QueryUtil.getTestRuns(em, null, "c.option0 = 4");
        System.out.println("Result");
        for (Object o : l) {
            Object[] result = (Object[]) o;
            for (Object r : result) System.out.print(r.getClass() + ", ");
            System.out.println();
        }
    }

    @Test
    public final void getTestCases() {
        List l = QueryUtil.getTestCases(em, null, 20);
        System.out.println("Result TC");
        for (Object o : l) {
            Object[] result = (Object[]) o;
            for (Object r : result) System.out.print(r + ", ");
            System.out.println();
        }
    }
}
