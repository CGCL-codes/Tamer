package es.seat131.javi.util;

import static org.junit.Assert.assertTrue;
import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import es.seat131.javi.config.ApplicationProperties;
import es.seat131.javi.service.UserService;

public class UserServiceTest {

    private UserService _userService;

    @Before
    public void setUp() throws Exception {
        _userService = new UserService();
    }

    @After
    public void tearDown() throws Exception {
        _userService = null;
    }

    @Test
    public void testValidateUser() throws IOException {
        ApplicationProperties.init();
        assertTrue(_userService.validateUser("admin", "admin08"));
    }
}
