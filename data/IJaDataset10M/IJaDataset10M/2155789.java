package org.hibernate.test.component.cascading.toone;

import junit.framework.Test;
import org.hibernate.junit.functional.FunctionalTestCase;
import org.hibernate.junit.functional.FunctionalTestClassTestSuite;
import org.hibernate.Session;

/**
 * {@inheritDoc}
 *
 * @author Steve Ebersole
 */
public class CascadeToComponentAssociationTest extends FunctionalTestCase {

    public CascadeToComponentAssociationTest(String string) {
        super(string);
    }

    public String[] getMappings() {
        return new String[] { "component/cascading/toone/Mappings.hbm.xml" };
    }

    public static Test suite() {
        return new FunctionalTestClassTestSuite(CascadeToComponentAssociationTest.class);
    }

    public void testMerging() {
        Session session = openSession();
        session.beginTransaction();
        User user = new User();
        Document document = new Document();
        document.setOwner(user);
        session.persist(document);
        session.getTransaction().commit();
        session.close();
        session = openSession();
        session.beginTransaction();
        document = (Document) session.get(Document.class, document.getId());
        assertNotNull(document.getOwner());
        assertNull(document.getOwner().getPersonalInfo());
        session.getTransaction().commit();
        session.close();
        Address addr = new Address();
        addr.setStreet1("123 6th St");
        addr.setCity("Austin");
        addr.setState("TX");
        document.getOwner().setPersonalInfo(new PersonalInfo(addr));
        session = openSession();
        session.beginTransaction();
        session.merge(document);
        session.getTransaction().commit();
        session.close();
        session = openSession();
        session.beginTransaction();
        document = (Document) session.get(Document.class, document.getId());
        assertNotNull(document.getOwner());
        assertNotNull(document.getOwner().getPersonalInfo());
        assertNotNull(document.getOwner().getPersonalInfo().getHomeAddress());
        session.getTransaction().commit();
        session.close();
    }
}
