package org.openymsg.legacy.roster;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.openymsg.legacy.network.YahooProtocol;
import org.openymsg.legacy.network.YahooUser;

/**
 * Checks for {@link Roster#add(org.openymsg.legacy.network.YahooUser)}, calling which should result into outgoing packets to
 * the yahoo network, indicating that you want to add a contact to your roster.
 * 
 * @author Guus der Kinderen, guus@nimbuzz.com
 * 
 */
public class RosterAdd {

    /**
     * Roster makes use of a FriendManager to relay calls to Yahoo. This test checks if calling 'add' triggers the
     * FriendManager.
     */
    @Test
    public void testCallingAddTriggersFriendManager() {
        final MockFriendManager manager = new MockFriendManager();
        final Roster roster = new Roster(manager);
        final YahooUser user = new YahooUser("user", "group", YahooProtocol.YAHOO);
        final boolean result = roster.add(user);
        assertTrue(result);
        assertEquals("sendNewFriendRequest", manager.getMethod());
        assertEquals("user", manager.getFriendId());
        assertEquals("group", manager.getGroupId());
    }

    /**
     * Checks that adding a user to the roster that already exists on the roster does not trigger the FriendManager.
     */
    @Test
    public void testCallingAddTwiceDoesntTriggerFriendManager() {
        final MockFriendManager manager = new MockFriendManager();
        final Roster roster = new Roster(manager);
        final YahooUser user = new YahooUser("user", "group", YahooProtocol.YAHOO);
        roster.add(user);
        manager.reset();
        final boolean result = roster.add(user);
        assertFalse(result);
        assertEquals("sendNewFriendRequest", manager.getMethod());
        assertEquals("user", manager.getFriendId());
        assertEquals("group", manager.getGroupId());
    }
}
