package be.kuleuven.cs.mop.domain.model.impl;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.junit.*;
import be.kuleuven.cs.mop.TestEnvironment;
import be.kuleuven.cs.mop.domain.exceptions.TaskManagerException;

public class TaskTypeImplTest {

    private static TestEnvironment ENV;

    @Before
    public void setUp() throws Exception {
        ENV = TestEnvironment.newInstance();
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorInvalid0() throws TaskManagerException {
        new TaskTypeImpl(null, null, null, null, null, null);
    }

    @Test(expected = TaskManagerException.class)
    public void testConstructorInvalid1() throws TaskManagerException {
        new TaskTypeImpl(ENV.world, null, null, null, null, null);
    }

    @Test(expected = Exception.class)
    public void testConstructorInvalid2() throws TaskManagerException {
        new TaskTypeImpl(ENV.world, "TestTaskType", null, null, null, null);
    }

    @Test(expected = Exception.class)
    public void testConstructorInvalid3() throws TaskManagerException {
        new TaskTypeImpl(ENV.world, "TestTaskType", new HashSet<UserTypeImpl>(ENV.world.getUserTypes()), null, null, null);
    }

    @Test(expected = Exception.class)
    public void testConstructorInvalid4() throws TaskManagerException {
        new TaskTypeImpl(ENV.world, "TestTaskType", new HashSet<UserTypeImpl>(ENV.world.getUserTypes()), new HashMap<UserTypeImpl, Interval>(), null, null);
    }

    @Test(expected = Exception.class)
    public void testConstructorInvalid5() throws TaskManagerException {
        new TaskTypeImpl(ENV.world, "TestTaskType", new HashSet<UserTypeImpl>(ENV.world.getUserTypes()), new HashMap<UserTypeImpl, Interval>(), new HashMap<String, FieldType>(), null);
    }

    @Test
    public void testConstructorValid() throws TaskManagerException {
        final TaskTypeImpl type = new TaskTypeImpl(ENV.world, "TestTaskType", new HashSet<UserTypeImpl>(ENV.world.getUserTypes()), new HashMap<UserTypeImpl, Interval>(), new HashMap<String, FieldType>(), new HashMap<ResourceTypeImpl, Interval>());
        assertEquals(type.getName(), "TestTaskType");
        assertTrue(type.getFieldsTemplate().isEmpty());
        assertTrue(type.getHelperUserTypesConstraints().isEmpty());
        assertFalse(type.getOwnerUserTypes().isEmpty());
        assertTrue(type.getRequiredResourceTypes().isEmpty());
        assertTrue(ENV.world.getTaskTypes().contains(type));
        for (final UserTypeImpl userType : ENV.world.getUserTypes()) assertTrue(type.isCreatableBy(userType));
    }

    @Test
    public void testMatchesFields() throws TaskManagerException {
        final TaskTypeImpl type = new TaskTypeImpl(ENV.world, "TypeA", new HashSet<UserTypeImpl>(ENV.world.getUserTypes()), new HashMap<UserTypeImpl, Interval>(), new HashMap<String, FieldType>(), new HashMap<ResourceTypeImpl, Interval>());
        final Map<String, Field> fields = new HashMap<String, Field>();
        assertTrue(type.matchesFields(fields));
        fields.put("fake", new Field(FieldType.TEXTUAL));
        assertFalse(type.matchesFields(fields));
    }

    @Test
    public void testMatchesHelperUsers() throws TaskManagerException {
        final TaskTypeImpl type = new TaskTypeImpl(ENV.world, "TypeA", new HashSet<UserTypeImpl>(ENV.world.getUserTypes()), new HashMap<UserTypeImpl, Interval>(), new HashMap<String, FieldType>(), new HashMap<ResourceTypeImpl, Interval>());
        final List<UserImpl> helpers = new ArrayList<UserImpl>();
        assertTrue(type.matchesHelperUsers(helpers));
        helpers.add(ENV.user1);
        helpers.add(ENV.user2);
        assertFalse(type.matchesHelperUsers(helpers));
    }

    @Test
    public void testMatchesReservations() throws TaskManagerException {
        final TaskTypeImpl type = new TaskTypeImpl(ENV.world, "TypeA", new HashSet<UserTypeImpl>(ENV.world.getUserTypes()), new HashMap<UserTypeImpl, Interval>(), new HashMap<String, FieldType>(), new HashMap<ResourceTypeImpl, Interval>());
        final List<ReservationImpl> reservations = new ArrayList<ReservationImpl>();
        assertTrue(type.matchesReservations(reservations));
        reservations.add(new ReservationImpl(ENV.task1, ENV.resource1, ENV.datum3, ENV.duration));
        assertFalse(type.matchesReservations(reservations));
    }
}
