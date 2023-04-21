package upmc.pstl.fw4exAuthenticated.tests;

import upmc.pstl.fw4exAuthenticated.AuthenticatedManager.AuthenticatedManager;
import upmc.pstl.fw4exAuthenticated.AuthenticatedManager.IAuthenticatedManager;
import junit.framework.TestCase;

public class AuthenticatedManagerTest extends TestCase {

    IAuthenticatedManager f01auth, f02auth;

    public AuthenticatedManagerTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        f01auth = new AuthenticatedManager("upmc:000000", "xxx99xx");
        f02auth = new AuthenticatedManager("upmc:2760994", "d76d2770");
    }

    public void testRequestParameters() {
        assertTrue(f01auth.getLogin().equalsIgnoreCase("upmc:000000"));
        assertFalse(f01auth.isAuthentificated());
        assertTrue(f02auth.getLogin().equalsIgnoreCase("upmc:2760994"));
        assertTrue(f02auth.isAuthentificated());
        assertTrue(f02auth.getFirstName().equalsIgnoreCase("YASSAMINE"));
        assertTrue(f02auth.getLastName().equalsIgnoreCase("SELADJI"));
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        f01auth = f02auth = null;
    }
}
