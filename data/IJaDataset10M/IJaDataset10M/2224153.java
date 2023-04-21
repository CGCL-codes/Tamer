package at.fhj.itm.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import at.fhj.itm.model.Location;
import at.fhj.itm.model.User;

/**
 * Tests the my sql user DAO
 * @author Seuchter
 *
 */
public class MySqlUserDAOTest extends DatabaseTest {

    private UserDAO dao;

    private DAOFactory factory;

    private Connection connection;

    /**
	 * Creates a trip which is then persisted, afterwards it is checked if the
	 * DAO can perform an update on the already created trip.
	 * @throws SQLException
	 */
    @Before
    public void setUp() throws Exception {
        rebuildDatabase();
        connection = getUnitTestConnection();
        factory = new MySqlDAOFactory();
        dao = factory.getUserDAO();
    }

    @After
    public void tearDown() {
    }

    private void assertUserEquals(User expected, User actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.getSessionID(), actual.getSessionID());
    }

    @Test
    public void testUpdate() {
        User uOld = dao.getByID(1, connection);
        User uNew = dao.getByID(1, connection);
        uNew.setFirstName("Unit");
        uNew.setLastLoginDate(new Date());
        uNew.setLastName("Test");
        uNew.setSessionID(UUID.randomUUID().toString().replace("-", ""));
        uNew.setPassword("1234");
        uNew.setUsername("UnitTest");
        uNew.setEmail("Unit@Test.com");
        dao.update(uNew, connection);
        User uUpdate = dao.getByID(1, connection);
        assertUserEquals(uUpdate, uNew);
        dao.update(uOld, connection);
        User uRestored = dao.getByID(1, connection);
        assertUserEquals(uOld, uRestored);
    }

    @Test
    public void testGetAll() {
        List<User> users = dao.selectAll(connection);
        assertTrue(users.size() >= 1);
        User u = users.get(0);
        assertNotNull(u.getUsername());
        assertNotNull(u.getPassword());
        assertNotNull(u.getPassword());
        assertNotNull(u.getPhone());
        assertTrue(u.getId() >= 0);
    }

    @Test
    public void testInsertDelete() {
        int count = dao.selectAll(connection).size();
        Location location = new Location(1234, "Unit City");
        User user = new User("Unit", "Test", "UnitTest", "1234", "Unit@Test.com", "123456", location, new Date(), UUID.randomUUID().toString().replace("-", ""));
        dao.update(user, connection);
        int newCount = dao.selectAll(connection).size();
        assertTrue(newCount > count);
        dao.delete(user, connection);
        assertEquals(-1, user.getId());
        assertEquals(-1, user.getLocation().getId());
        int countAfterDelete = dao.selectAll(connection).size();
        assertEquals(count, countAfterDelete);
    }
}
