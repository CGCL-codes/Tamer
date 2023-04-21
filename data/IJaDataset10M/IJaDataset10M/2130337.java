package de.creatronix.artist3k.model;

import static org.junit.Assert.assertEquals;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;
import de.creatronix.artist3k.db.HibernateUtil;
import de.creatronix.artist3k.model.Address;
import de.creatronix.artist3k.model.ContactDetails;

public class ContactDetailsTest {

    static Long id = new Long(1);

    @Test
    public void testStoreContactDetails() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Address address = new Address();
        address.setCountry("Germany");
        address.setTown("D-Dorf");
        address.setGeoCoordinates("NordS�d");
        address.setStreet("Stra�e der Hoffnung");
        address.setStreetNumber("9");
        address.setZipcode("4711");
        ContactDetails contactDetails = new ContactDetails();
        contactDetails.setCellPhoneNumber("0174/6124343");
        contactDetails.setEmail("xxx@yyy.de");
        contactDetails.setLandlineNumber("05731/52144");
        contactDetails.getAddresses().add(address);
        session.saveOrUpdate(contactDetails);
        id = contactDetails.getId();
        transaction.commit();
        session.close();
    }

    @Test
    public void testLoadContactDetails() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        ContactDetails contactDetails = (ContactDetails) session.get(ContactDetails.class, id);
        Address address = (Address) contactDetails.getAddresses().toArray()[0];
        assertEquals(address.getTown(), "D-Dorf");
        transaction.commit();
        session.close();
    }

    @Test
    public void testDeleteContactDetails() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        ContactDetails contactDetails = new ContactDetails();
        contactDetails.setId(id);
        session.delete(contactDetails);
        transaction.commit();
        session.close();
    }
}
