package devbureau.fstore.common;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author buyakov
 */
public class UserRegistryTest {

    public UserRegistryTest() {
    }

    @Test
    public void testRegistry() {
        try {
            UserRegistry registry = UserRegistryFactory.getUserRegistry();
            if (!registry.checkUserPassword("buyakov", "password")) {
                fail("Wrong password");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        }
    }
}
