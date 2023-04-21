package junittutorial;

import static org.junit.Assert.*;
import org.junit.*;

/**
 * UserProfileTest
 * @author zadonics
 *
 */
public class UserProfileTest {

    @Test
    public void testGetFullName() {
        UserProfile usrPrf = new UserProfile();
        usrPrf.setFirstName("Zolt�n");
        usrPrf.setLastName("Adonics");
        assertEquals("Zolt�n Adonics", usrPrf.getFullName());
    }
}
