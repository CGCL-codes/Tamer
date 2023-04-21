package org.scubatoolkit;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Vector;
import junit.framework.TestCase;
import org.scubatoolkit.db.Database;
import org.scubatoolkit.exceptions.InvalidPersonException;

/** * */
public class PersonTest extends TestCase {

    /**	 * Constructor for PersonTest.	 * @param arg0	 */
    public PersonTest(String arg0) {
        super(arg0);
    }

    protected void setUp() throws Exception {
        super.setUp();
        if (!Database.instance.exists()) {
            Database.instance.create();
        }
        Connection con = Database.getConnection();
        Statement stmt = con.createStatement();
        stmt.executeQuery("delete FROM person");
        stmt.executeQuery("delete FROM country");
        stmt.executeQuery("delete FROM contact_info");
        stmt.close();
        con.close();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        Connection con = Database.getConnection();
        Statement stmt = con.createStatement();
        stmt.executeQuery("delete FROM person");
        stmt.executeQuery("delete FROM contact_info");
        stmt.executeQuery("delete FROM country");
        stmt.close();
        con.close();
        Database.instance.shutdown();
    }

    public void testGetPersons() {
        Country country = new Country();
        country.setName("country");
        try {
            country.save();
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
        Person person1 = new Person();
        person1.setFirstName("fname");
        person1.setLastName("lname");
        person1.getContactInfo().setAddress1("address1");
        person1.getContactInfo().setAddress2("address2");
        person1.getContactInfo().setAddress3("address3");
        person1.getContactInfo().setAddress4("address4");
        person1.getContactInfo().setCity("city");
        person1.getContactInfo().setCountry(country);
        person1.getContactInfo().setEmail("email");
        person1.getContactInfo().setFax("fax");
        person1.getContactInfo().setPhone("phone");
        person1.getContactInfo().setPostalCode("postalcode");
        person1.getContactInfo().setStateProvince("stateprovince");
        try {
            person1.save();
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
        Person person2 = new Person();
        person2.setFirstName("fname");
        person2.setLastName("lname");
        person2.getContactInfo().setAddress1("address12");
        person2.getContactInfo().setAddress2("address22");
        person2.getContactInfo().setAddress3("address32");
        person2.getContactInfo().setAddress4("address42");
        person2.getContactInfo().setCity("city2");
        person2.getContactInfo().setCountry(country);
        person2.getContactInfo().setEmail("email2");
        person2.getContactInfo().setFax("fax2");
        person2.getContactInfo().setPhone("phone2");
        person2.getContactInfo().setPostalCode("postalcode2");
        person2.getContactInfo().setStateProvince("stateprovince2");
        try {
            person2.save();
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
        try {
            Vector persons = Person.getPersons();
            assertEquals(2, persons.size());
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    public void testSaveLoadRemovePerson() {
        Person person = new Person();
        person.setFirstName("first name");
        person.setLastName("last name");
        person.getContactInfo().setAddress1("address1");
        person.getContactInfo().setAddress2("address2");
        person.getContactInfo().setAddress3("address3");
        person.getContactInfo().setAddress4("address4");
        Country country = new Country();
        country.setName("country");
        try {
            country.save();
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
        person.getContactInfo().setCity("city");
        person.getContactInfo().setCountry(country);
        person.getContactInfo().setEmail("email");
        person.getContactInfo().setFax("fax");
        person.getContactInfo().setPhone("phone");
        person.getContactInfo().setPostalCode("postalcode");
        person.getContactInfo().setStateProvince("stateprovince");
        int personID = -1;
        try {
            personID = person.save();
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
        assertFalse(personID == -1);
        Person loadedPerson = null;
        try {
            loadedPerson = Person.loadPerson(personID);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
        assertEquals("address1", loadedPerson.getContactInfo().getAddress1());
        assertEquals("address2", loadedPerson.getContactInfo().getAddress2());
        assertEquals("address3", loadedPerson.getContactInfo().getAddress3());
        assertEquals("address4", loadedPerson.getContactInfo().getAddress4());
        assertEquals("city", loadedPerson.getContactInfo().getCity());
        assertEquals("country", loadedPerson.getContactInfo().getCountry().getName());
        assertEquals("email", loadedPerson.getContactInfo().getEmail());
        assertEquals("fax", loadedPerson.getContactInfo().getFax());
        assertEquals("phone", loadedPerson.getContactInfo().getPhone());
        assertEquals("postalcode", loadedPerson.getContactInfo().getPostalCode());
        assertEquals("stateprovince", loadedPerson.getContactInfo().getStateProvince());
        try {
            Person.remove(person);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
        try {
            Country.remove(country);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    public void testPerson() {
        Person person = new Person();
        assertEquals(-1, person.getPersonID());
    }

    public void testSaveNullPerson() {
        Person person = new Person();
        try {
            person.save();
        } catch (InvalidPersonException invEx) {
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
        person = new Person();
        person.setFirstName("name");
        try {
            person.save();
        } catch (InvalidPersonException invEx) {
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
        person = new Person();
        person.setLastName("name");
        try {
            person.save();
        } catch (InvalidPersonException invEx) {
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    public void testToString() {
        Person person = new Person();
        person.setFirstName("Test");
        person.setLastName("Name");
        assertEquals("Name, Test", person.toString());
    }

    public void testUpdatePerson() {
        Person person = new Person();
        person.setFirstName("first name");
        person.setLastName("last name");
        person.getContactInfo().setAddress1("address1");
        person.getContactInfo().setAddress2("address2");
        person.getContactInfo().setAddress3("address3");
        person.getContactInfo().setAddress4("address4");
        Country country = new Country();
        country.setName("country");
        try {
            country.save();
        } catch (Exception ex) {
            fail();
        }
        person.getContactInfo().setCity("city");
        person.getContactInfo().setCountry(country);
        person.getContactInfo().setEmail("email");
        person.getContactInfo().setFax("fax");
        person.getContactInfo().setPhone("phone");
        person.getContactInfo().setPostalCode("postalcode");
        person.getContactInfo().setStateProvince("stateprovince");
        int personID = -1;
        try {
            personID = person.save();
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
        assertFalse(personID == -1);
        person.setFirstName("first name2");
        person.setLastName("last name2");
        person.getContactInfo().setAddress1("address12");
        person.getContactInfo().setAddress2("address22");
        person.getContactInfo().setAddress3("address32");
        person.getContactInfo().setAddress4("address42");
        person.getContactInfo().setCity("city2");
        person.getContactInfo().setCountry(country);
        person.getContactInfo().setEmail("email2");
        person.getContactInfo().setFax("fax2");
        person.getContactInfo().setPhone("phone2");
        person.getContactInfo().setPostalCode("postalcode2");
        person.getContactInfo().setStateProvince("stateprovince2");
        int personID2 = -1;
        try {
            personID2 = person.save();
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
        assertEquals(personID, personID2);
        Person loadedPerson = null;
        try {
            loadedPerson = Person.loadPerson(personID);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
        assertEquals("first name2", loadedPerson.getFirstName());
        assertEquals("last name2", loadedPerson.getLastName());
        assertEquals("address12", loadedPerson.getContactInfo().getAddress1());
        assertEquals("address22", loadedPerson.getContactInfo().getAddress2());
        assertEquals("address32", loadedPerson.getContactInfo().getAddress3());
        assertEquals("address42", loadedPerson.getContactInfo().getAddress4());
        assertEquals("city2", loadedPerson.getContactInfo().getCity());
        assertEquals("country", loadedPerson.getContactInfo().getCountry().getName());
        assertEquals("email2", loadedPerson.getContactInfo().getEmail());
        assertEquals("fax2", loadedPerson.getContactInfo().getFax());
        assertEquals("phone2", loadedPerson.getContactInfo().getPhone());
        assertEquals("postalcode2", loadedPerson.getContactInfo().getPostalCode());
        assertEquals("stateprovince2", loadedPerson.getContactInfo().getStateProvince());
        try {
            Person.remove(person);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
        try {
            Country.remove(country);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }
}
