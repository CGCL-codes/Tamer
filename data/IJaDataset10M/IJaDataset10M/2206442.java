package org.acs.elated.test.app;

import junit.framework.*;
import org.acs.elated.app.*;
import org.acs.elated.test.commons.CleanFedora;
import org.acs.elated.test.mockObjects.MockDatabaseConnectorInterface;
import org.acs.elated.test.mockObjects.MockFedoraInterface;
import org.acs.elated.database.*;
import java.sql.SQLException;
import org.acs.elated.exception.*;

public class TestAuthorization extends TestCase {

    private SecurityMgr securityMgr = null;

    protected void setUp() throws Exception {
        super.setUp();
        securityMgr = new SecurityMgr(new MockDatabaseConnectorInterface());
        CollectionMgr cm = new CollectionMgr(new MockFedoraInterface());
    }

    protected void tearDown() throws Exception {
        securityMgr = null;
        super.tearDown();
    }

    public void testIsAuthenticated() throws InternalException {
        CleanFedora.resetMySQLDB(null);
        User user = securityMgr.authenticateUser("KELSON", "KELSON");
        assertEquals("5", user.getUserID());
    }

    public void testIsAuthenticatedBadPassword() throws InternalException {
        CleanFedora.resetMySQLDB(null);
        User user = securityMgr.authenticateUser("BINAYA", "binaya");
        assertFalse(user.isAuthenticated());
    }

    public void testIsAuthenticatedInvalidUser() throws InternalException {
        CleanFedora.resetMySQLDB(null);
        User user = securityMgr.authenticateUser("MIKE", "MIKE");
        assertFalse(user.isAuthenticated());
    }

    public void testIsAuthenticatedWithAddAndRemove() throws AccessException, CreateException, SQLException, InternalException {
        CleanFedora.resetMySQLDB(null);
        DBManagement dbm = DatabaseFactory.getDBManagement();
        dbm.addUser("testUser", "12345", "first", "last", "email", "0", "0");
        User user = securityMgr.authenticateUser("testUser", "12345");
        assertEquals("99", user.getUserID());
        dbm.removeUser("99");
        user = securityMgr.authenticateUser("testUser", "12345");
        assertFalse(user.isAuthenticated());
    }
}
