package com.reserveamerica.jirarmi;

import java.rmi.RemoteException;
import com.reserveamerica.jirarmi.beans.user.UserRemote;
import com.reserveamerica.jirarmi.exceptions.UserNotFoundException;

/**
 * Unit test for the Authentication Service.
 * 
 * @author bstasyszyn
 */
public class TestAuthenticationService extends JiraRMITestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testResolveUser() throws UserNotFoundException, RemoteException {
        UserRemote user = authService.resolveUser(token);
        if (!user.getName().equals(getUserName())) {
            fail("Resolved user [" + user.getName() + "] does not match [" + getUserName() + "].");
        }
    }
}
