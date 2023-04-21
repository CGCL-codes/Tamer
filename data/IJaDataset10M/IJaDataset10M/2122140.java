package net.sf.doolin.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.HashMap;
import net.sf.doolin.util.factory.InitialisingDataFactory;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link InitialisingDataFactory}.
 * 
 * @author Damien Coraboeuf
 * 
 */
public class TestInitialisingDataFactory {

    /**
	 * The Class Address.
	 */
    public static class Address {

        private String city;

        /**
		 * Gets the city.
		 * 
		 * @return the city
		 */
        public String getCity() {
            return this.city;
        }

        /**
		 * Sets the city.
		 * 
		 * @param city
		 *            the new city
		 */
        public void setCity(String city) {
            this.city = city;
        }
    }

    /**
	 * The Class Person.
	 */
    public static class Person {

        private String name;

        private Address address;

        /**
		 * Gets the address.
		 * 
		 * @return the address
		 */
        public Address getAddress() {
            return this.address;
        }

        /**
		 * Gets the name.
		 * 
		 * @return the name
		 */
        public String getName() {
            return this.name;
        }

        /**
		 * Sets the address.
		 * 
		 * @param address
		 *            the new address
		 */
        public void setAddress(Address address) {
            this.address = address;
        }

        /**
		 * Sets the name.
		 * 
		 * @param name
		 *            the new name
		 */
        public void setName(String name) {
            this.name = name;
        }
    }

    private Person person;

    /**
	 * Initilisation
	 */
    @Before
    public void before() {
        Address address = new Address();
        address.setCity("Bruxelles");
        this.person = new Person();
        this.person.setName("Damien");
        this.person.setAddress(address);
    }

    /**
	 * With an empty map
	 */
    @Test
    public void testEmpty() {
        InitialisingDataFactory<Person> factory = new InitialisingDataFactory<Person>();
        factory.setInitialisationMap(new HashMap<String, Object>());
        Person t = factory.create(this.person);
        assertNotNull(t);
        assertTrue(t == this.person);
        assertEquals("Damien", t.getName());
        assertNotNull(t.getAddress());
        assertEquals("Bruxelles", t.getAddress().getCity());
    }

    /**
	 * With a null map
	 */
    @Test
    public void testNull() {
        InitialisingDataFactory<Person> factory = new InitialisingDataFactory<Person>();
        Person t = factory.create(this.person);
        assertNotNull(t);
        assertTrue(t == this.person);
        assertEquals("Damien", t.getName());
        assertNotNull(t.getAddress());
        assertEquals("Bruxelles", t.getAddress().getCity());
    }

    /**
	 * With objects
	 */
    @Test
    public void testObjects() {
        InitialisingDataFactory<Person> factory = new InitialisingDataFactory<Person>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("name", "Damien C.");
        Address newAddress = new Address();
        newAddress.setCity("Bremen");
        map.put("address", newAddress);
        factory.setInitialisationMap(map);
        Person t = factory.create(this.person);
        assertNotNull(t);
        assertTrue(t == this.person);
        assertEquals("Damien C.", t.getName());
        assertNotNull(t.getAddress());
        assertEquals("Bremen", t.getAddress().getCity());
    }

    /**
	 * With properties
	 */
    @Test
    public void testProperties() {
        InitialisingDataFactory<Person> factory = new InitialisingDataFactory<Person>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("name", "Damien C.");
        map.put("address.city", "Orléans");
        factory.setInitialisationMap(map);
        Person t = factory.create(this.person);
        assertNotNull(t);
        assertTrue(t == this.person);
        assertEquals("Damien C.", t.getName());
        assertNotNull(t.getAddress());
        assertEquals("Orléans", t.getAddress().getCity());
    }
}
