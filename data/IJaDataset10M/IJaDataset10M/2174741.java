package org.geonetwork.gaap.client;

import junit.framework.TestCase;
import org.junit.Test;
import org.geonetwork.gaap.domain.user.User;
import org.geonetwork.gaap.domain.group.Group;
import java.util.List;

/**
 * Tests for GaapClient
 *
 * @author Jose
 */
public class GaapClientTest extends TestCase {

    GaapClient gaapClient;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        gaapClient = new GaapClient();
    }

    @Test
    public void testDummy() {
        assertTrue(true);
    }

    @Test
    public void xtestGetUsers() {
        try {
            List<User> users = gaapClient.getUsers();
            assertNotNull(users);
        } catch (GeonetworkGaapClientException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void xtestGetGroups() {
        try {
            List<Group> groups = gaapClient.getGroups();
            assertNotNull(groups);
        } catch (GeonetworkGaapClientException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void xtestGetUser() {
        try {
            User user = gaapClient.getUser("u1");
            assertNotNull(user);
        } catch (GeonetworkGaapClientException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void xtestGetGroup() {
        try {
            Group group = gaapClient.getGroup("group1");
            assertNotNull(group);
        } catch (GeonetworkGaapClientException e) {
            e.printStackTrace();
            fail();
        }
    }
}
