package org.authorsite.mailarchive.services.storage;

import java.util.*;
import junit.framework.*;
import org.hibernate.*;
import org.authorsite.mailarchive.model.*;
import org.authorsite.mailarchive.model.impl.*;
import org.authorsite.mailarchive.services.storage.impl.*;

/**
 * 
 * @author jejking
 * @version $Revision: 1.2 $
 */
public class EmailAddressLocatorTest extends AbstractPersistenceTestCase {

    private EmailAddressDAO addressDAO;

    /**
     * @param name
     * @throws Exception
     */
    public EmailAddressLocatorTest(String name) throws Exception {
        super(name);
        addressDAO = new HibernateEmailAddressDAO(sessionFactory);
    }

    public void testGetEmailAddress() throws Exception {
        EmailAddress original = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Query query = session.createQuery("from EmailAddressImpl as addr where addr.address = 'emailAddressDataLoaderFind4@test.com'");
            original = (EmailAddress) query.uniqueResult();
        } finally {
            session.close();
        }
        EmailAddress fromDAO = addressDAO.getEmailAddress("emailAddressDataLoaderFind4@test.com");
        Assert.assertEquals(original, fromDAO);
    }

    public void testGetEmailAddressesByPerson() throws Exception {
        Person addr6Person = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Query query = session.createQuery("from PersonImpl as person where person.mainName = 'EmailAddressDataLoaderTestPerson'");
            addr6Person = (Person) query.uniqueResult();
        } finally {
            session.close();
        }
        List addresses = addressDAO.getEmailAddressesByPerson(addr6Person);
        Assert.assertEquals(2, addresses.size());
        boolean foundA = false;
        boolean foundB = false;
        Iterator it = addresses.iterator();
        while (it.hasNext()) {
            EmailAddress addr = (EmailAddress) it.next();
            if (addr.getAddress().equals("emailAddressDataLoaderFind6a@test.com")) {
                foundA = true;
            }
            if (addr.getAddress().equals("emailAddressDataLoaderFind6b@test.com")) {
                foundB = true;
            }
        }
        Assert.assertTrue(foundA & foundB);
    }

    public void testGetEmailAddressesByPersonalName() throws Exception {
        List addresses = addressDAO.getEmailAddressesByPersonalName("EmailAddressDataLoaderTest Four");
        Assert.assertEquals(2, addresses.size());
        boolean found4 = false;
        boolean found7 = false;
        Iterator it = addresses.iterator();
        while (it.hasNext()) {
            EmailAddress addr = (EmailAddress) it.next();
            System.out.println(addr);
            if (addr.getAddress().equals("emailAddressDataLoaderFind4@test.com")) {
                found4 = true;
            }
            if (addr.getAddress().equals("emailAddressDataLoaderFind7@test.com")) {
                found7 = true;
            }
        }
        Assert.assertTrue(found4 & found7);
    }

    public void testGetEmailAddressesByPersonalNameLike() throws Exception {
        List addresses = addressDAO.getEmailAddressesByPersonalNameLike("EmailAddressDataLoaderTest%");
        Assert.assertEquals(3, addresses.size());
        Iterator it = addresses.iterator();
        while (it.hasNext()) {
            EmailAddress addr = (EmailAddress) it.next();
            Assert.assertTrue(addr.getPersonalName().startsWith("EmailAddressDataLoaderTest"));
        }
    }

    public void testGetEmailAddressesLike() throws Exception {
        List addresses = addressDAO.getEmailAddressesLike("emailAddressDataLoaderFind%@test.com");
        Iterator it = addresses.iterator();
        while (it.hasNext()) {
            EmailAddress addr = (EmailAddress) it.next();
            Assert.assertTrue(addr.getAddress().startsWith("emailAddressDataLoaderFind"));
        }
    }
}
