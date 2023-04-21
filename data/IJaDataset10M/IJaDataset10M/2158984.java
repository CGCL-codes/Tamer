package com.spring66.training;

import com.spring66.training.entity.Owner;
import com.spring66.training.entity.Pet;
import com.spring66.training.entity.PetType;
import com.spring66.training.entity.Visit;
import java.util.Date;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.MySQLDialect;

/**
 * Unit test for simple App.
 */
public class AppTestPet extends TestCase {

    private static SessionFactory sessionFactory;

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTestPet(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTestPet.class);
    }

    @Override
    protected void setUp() throws Exception {
        try {
            AnnotationConfiguration configuration = new AnnotationConfiguration();
            configuration.setProperty(Environment.DRIVER, "com.mysql.jdbc.Driver");
            configuration.setProperty(Environment.URL, "jdbc:mysql://localhost:3306/dem");
            configuration.setProperty(Environment.USER, "root");
            configuration.setProperty(Environment.PASS, "password");
            configuration.setProperty(Environment.DIALECT, MySQLDialect.class.getName());
            configuration.setProperty(Environment.SHOW_SQL, "true");
            configuration.setProperty(Environment.HBM2DDL_AUTO, "create");
            configuration.addPackage("com.spring66.training.hibernate");
            configuration.addAnnotatedClass(Owner.class);
            configuration.addAnnotatedClass(Pet.class);
            configuration.addAnnotatedClass(Visit.class);
            configuration.addAnnotatedClass(PetType.class);
            sessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            ex.printStackTrace();
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        assertNotNull(sessionFactory);
        Pet t = new Pet();
        t.setName("Final cut");
        PetType pt = new PetType();
        pt.setName("Dog");
        Visit vandamme = new Visit();
        vandamme.setName("JC Vandamme");
        t.addVisit(vandamme);
        Visit rambo = new Visit();
        rambo.setName("Rambo");
        t.addVisit(rambo);
        t.setType(pt);
        session.persist(t);
        tx.commit();
        session.close();
        session = sessionFactory.openSession();
        tx = session.beginTransaction();
        Visit newVisit = new Visit();
        newVisit.setDescription("description");
        newVisit.setName("name");
        newVisit.setPet(t);
        newVisit.setVisitDate(new Date());
        session.persist(newVisit);
        tx.commit();
        session.close();
        session = sessionFactory.openSession();
        tx = session.beginTransaction();
        Pet pet = (Pet) session.get(Pet.class, t.getId());
        assertNotNull(pet.getVisits());
        assertEquals("Dog", pet.getType().getName());
        assertFalse(Hibernate.isInitialized(pet.getVisits()));
        assertEquals(3, pet.getVisits().size());
        assertEquals(rambo.getName(), pet.getVisits().iterator().next().getName());
        tx.commit();
        session.close();
    }
}
