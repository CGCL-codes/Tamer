package edu.kit.pse.ass.facility.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import edu.kit.pse.ass.entity.Facility;
import edu.kit.pse.ass.entity.Property;
import edu.kit.pse.ass.entity.Room;
import edu.kit.pse.ass.entity.Workplace;

/**
 * The Class FacilityDAOImplTest.
 * 
 * @author Fabian
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext/applicationContext-*.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class FacilityDAOImplTest {

    @Autowired
    private JpaTemplate jpaTemplate;

    @Autowired
    private FacilityDAO facilityDAO;

    private Facility persistedRoom1;

    private Facility persistedRoom2;

    private Property propertyWLAN;

    private Collection<Facility> roomsWithWLAN;

    /**
	 * Sets the up.
	 */
    @Before
    public void setUp() {
        propertyWLAN = new Property("WLAN");
        persistedRoom1 = new Room();
        persistedRoom1.addProperty(propertyWLAN);
        jpaTemplate.persist(persistedRoom1);
        persistedRoom2 = new Room();
        persistedRoom2.addProperty(propertyWLAN);
        jpaTemplate.persist(persistedRoom2);
        roomsWithWLAN = new ArrayList<Facility>();
        roomsWithWLAN.add(persistedRoom1);
        roomsWithWLAN.add(persistedRoom2);
    }

    /**
	 * Tests getFacility() with null parameter.
	 */
    @Test(expected = IllegalArgumentException.class)
    public void testGetFacilityWithNullParameter() {
        facilityDAO.getFacility(null);
    }

    /**
	 * Tests getFacility() with empty parameter.
	 */
    @Test(expected = IllegalArgumentException.class)
    public void testGetFacilityWithEmptyParameter() {
        facilityDAO.getFacility("");
    }

    /**
	 * Tests getFacility() with parameter that leads to a return equal null.
	 */
    @Test
    public void testGetFacilityWithNotExistingParameter() {
        assertNull("There should not be a facility with this ID. Really!", facilityDAO.getFacility("INVALID"));
    }

    /**
	 * Test getFacility() whether this method returns the right object.
	 */
    @Test
    public void testGetFacilityWithValidParameter() {
        Facility result = facilityDAO.getFacility(persistedRoom1.getId());
        assertNotNull("No facility found despite it was added before.", result);
        assertTrue("facility is not a Room", result instanceof Room);
        assertEquals("ID of facility is wrong", persistedRoom1.getId(), result.getId());
        assertEquals("containedFacilities are different", persistedRoom1.getContainedFacilities().size(), result.getContainedFacilities().size());
        assertTrue("containedFacilities are different", result.getContainedFacilities().containsAll(persistedRoom1.getContainedFacilities()));
        assertTrue("properties are different", result.getProperties().containsAll(persistedRoom1.getProperties()));
        assertTrue("Wrong facility returned.", persistedRoom1.equals(result));
    }

    /**
	 * Tests getAllFacilities with null as parameter.
	 */
    @Test(expected = IllegalArgumentException.class)
    public void testGetAllFacilitiesWithNullParameter() {
        facilityDAO.getAllFacilities(null);
    }

    /**
	 * Tests getAllFacilities with not existing parameter.
	 */
    @Test
    public void testGetAllFacilitiesWithNotExistingParameter() {
        Collection<Workplace> workplaces = facilityDAO.getAllFacilities(Workplace.class);
        assertNotNull("There are no workplaces in database, but the return value should not be null.", workplaces);
        assertEquals("There should not be any workplaces.", workplaces.size(), 0);
    }

    /**
	 * Tests getAllFacilities with valid parameter.
	 */
    @Test
    public void testGetAllFacilitiesWithValidParameter() {
        Collection<Room> rooms = facilityDAO.getAllFacilities(Room.class);
        assertNotNull("There should be returned rooms from the database.", rooms);
        assertEquals("There are exactly 2 rooms in the database.", rooms.size(), 2);
        assertTrue("The room persistedRoom1 was added to the database.", rooms.contains(persistedRoom1));
        assertTrue("The room persistedRoom2 was added to the database.", rooms.contains(persistedRoom2));
    }

    /**
	 * Tests getFacility() with first parameter null.
	 */
    @Test(expected = IllegalArgumentException.class)
    public void testGetFacilityWithFirstParameterNull() {
        facilityDAO.getFacility(null, persistedRoom1.getId());
    }

    /**
	 * Tests getFacility() with second parameter null.
	 */
    @Test(expected = IllegalArgumentException.class)
    public void testGetFacilityWithSecondParameterNull() {
        facilityDAO.getFacility(Room.class, null);
    }

    /**
	 * Tests getFacility() with all parameter null.
	 */
    @Test(expected = IllegalArgumentException.class)
    public void testGetFacilityWithAllParametersNull() {
        facilityDAO.getFacility(null, null);
    }

    /**
	 * Tests getFacility() with second parameter empty.
	 */
    @Test(expected = IllegalArgumentException.class)
    public void testGetFacilityWithSecondParameterEmpty() {
        facilityDAO.getFacility(Room.class, "");
    }

    /**
	 * Tests getFacility() with not existing first parameter.
	 */
    @Test
    public void testGetFacilityWithNotExistingFirstParameter() {
        Facility facility = facilityDAO.getFacility(Workplace.class, persistedRoom1.getId());
        assertNull("There is no workplace, thus no facility should be returned.", facility);
    }

    /**
	 * Tests getFacility() with not existing second parameter.
	 */
    @Test
    public void testGetFacilityWithNotExistingSecondParameter() {
        Facility facility = facilityDAO.getFacility(Room.class, "ThisIsNoID");
        assertNull("There is no such ID, thus no facility should be returned.", facility);
    }

    /**
	 * Tests getFacility() with not existing parameters at all.
	 */
    @Test
    public void testGetFacilityWithNotExistingParameters() {
        Facility facility = facilityDAO.getFacility(Workplace.class, "ThisIsNoID");
        assertNull("There are no such parameters, thus no facility should be returned.", facility);
    }

    /**
	 * Tests getFacility() with all parameter valid.
	 */
    @Test
    public void testGetFacilityWithAllParameterValid() {
        Facility facility = facilityDAO.getFacility(Room.class, persistedRoom1.getId());
        assertEquals("The returned facility should be equals persistedRoom1.", facility, persistedRoom1);
    }

    /**
	 * Tests getAvailablePropertiesOf() with null parameter.
	 */
    @Test(expected = IllegalArgumentException.class)
    public void testGetAvailablePropertiesOfWithNullParameter() {
        facilityDAO.getAvailablePropertiesOf(null);
    }

    /**
	 * Tests getAvailablePropertiesOf() with parameter that leads to a return equal null.
	 */
    @Test
    public void testGetAvailablePropertiesOfNotExistingParameter() {
        Collection<Property> properties = facilityDAO.getAvailablePropertiesOf(Workplace.class);
        assertTrue("There should not be a workingplace, thus no properties either.", properties.isEmpty());
    }

    /**
	 * Test getAvailablePropertiesOf() with valid parameter.
	 */
    @Test
    public void testGetAvailablePropertiesOfWithValidParameter() {
        Collection<Property> result = facilityDAO.getAvailablePropertiesOf(Room.class);
        assertFalse("The properties were not found.", result.isEmpty());
        assertTrue("The 2 Collection<Property> should have same size.", persistedRoom1.getProperties().size() == result.size());
        assertTrue("More properties as intended were found.", persistedRoom1.getProperties().containsAll(result));
        assertTrue("Not all properties were found.", result.containsAll(persistedRoom1.getProperties()));
    }

    /**
	 * Tests getFacilities with null parameter.
	 */
    @Test(expected = IllegalArgumentException.class)
    public void testGetFacilitiesWithNullParameter() {
        facilityDAO.getFacilities(null);
    }

    /**
	 * Tests getFacilities() with empty parameter.
	 */
    @Test
    public void testGetFacilitiesWithEmptyParameter() {
        Collection<Facility> facilities = facilityDAO.getFacilities(new ArrayList<Property>());
        assertNotNull("This collection should not be null.", facilities);
        assertTrue("Every facility fits to the requirements, which are nothing specific.", !facilities.isEmpty());
        assertTrue("All facility in the datebase fits to the empty parameter, " + "so there should be returned every facility available.", facilities.size() == 2);
    }

    /**
	 * Tests getFacilities() with property parameter that should lead to a result equal null.
	 */
    @Test
    public void testGetFacilitiesWithNotExistingProperty() {
        Property propertyToillet = new Property();
        propertyToillet.setName("Toillet");
        Collection<Facility> result = facilityDAO.getFacilities(Arrays.asList(propertyToillet));
        assertNotNull("getFacilities() should have returned an empty collection.", result);
        assertTrue("There should not be any facilities with this property", result.isEmpty());
    }

    /**
	 * Tests getFacilities() with valid parameter.
	 */
    @Test
    public void testGetFacilitiesWithValidParameter() {
        Collection<Facility> result = facilityDAO.getFacilities(Arrays.asList(propertyWLAN));
        assertNotNull("No facilities found.", result);
        assertFalse("Not one facility found.", result.isEmpty());
        assertTrue("Not all facilities were found.", result.containsAll(roomsWithWLAN));
        assertTrue("Too many facilities were returned.", roomsWithWLAN.containsAll(result));
        assertTrue("Wrong facilities were returned.", roomsWithWLAN.equals(result));
    }

    /**
	 * Tests merge() with null parameter.
	 */
    @Test(expected = IllegalArgumentException.class)
    public void testMergeWithInvalidParameter() {
        facilityDAO.merge(null);
    }

    /**
	 * Test merge(), if the facility is updated correct
	 */
    @Test
    public void testMerge() {
        persistedRoom2.addProperty(new Property("LAN"));
        facilityDAO.merge(persistedRoom2);
        Facility result = facilityDAO.getFacility(persistedRoom2.getId());
        assertTrue("No Property was added", result.getProperties().size() == 2);
        assertTrue("Wrong Property was added", result.getProperties().contains(new Property("LAN")));
    }

    /**
	 * Tests remove() with null parameter.
	 */
    @Test(expected = IllegalArgumentException.class)
    public void testRemoveWithInvalidParameter() {
        facilityDAO.remove(null);
    }

    /**
	 * Tests remove() with empty parameter.
	 */
    @Test(expected = IllegalArgumentException.class)
    public void testRemoveWithEmptyParameter() {
        facilityDAO.remove("");
    }

    /**
	 * Test remove()
	 */
    @Test
    public void testRemove() {
        String roomId = persistedRoom1.getId();
        facilityDAO.remove(roomId);
        assertNull("There should no longer be a facility with this ID.", facilityDAO.getFacility(roomId));
    }

    /**
	 * Test remove a removed facility
	 */
    @Test
    public void testRemoveRemoved() {
        String roomId = persistedRoom1.getId();
        facilityDAO.remove(roomId);
        facilityDAO.remove(roomId);
    }

    /**
	 * Tests persist() with null parameter.
	 */
    @Test(expected = IllegalArgumentException.class)
    public void testPersistWithInvalidParameter() {
        facilityDAO.persist(null);
    }

    /**
	 * Tests persist()
	 */
    @Test
    public void testPersist() {
        Room persistRoom = new Room();
        persistRoom.addProperty(propertyWLAN);
        facilityDAO.persist(persistRoom);
        Facility result = facilityDAO.getFacility(persistRoom.getId());
        assertTrue("Room doesn't contain the property", result.getProperties().contains(propertyWLAN));
    }
}
