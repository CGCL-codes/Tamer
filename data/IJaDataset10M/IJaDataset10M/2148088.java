package org.openymsg.roster;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.openymsg.network.YahooUser;

/**
 * Testcase for the RosterEvent class. This testcase mainly checks argument validation.
 * 
 * @author Guus der Kinderen, guus@nimbuzz.com
 */
public class RosterEventTest {

    /**
     * Argument 'source' of the constructor cannot be null. Test method for
     * {@link org.openymsg.roster.RosterEvent#RosterEvent(org.openymsg.roster.Roster, org.openymsg.network.YahooUser, org.openymsg.roster.RosterEventType)}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRosterEventArgumentValidationSource() {
        new RosterEvent(null, new YahooUser("dummy"), RosterEventType.add);
    }

    /**
     * Argument 'user' of the constructor cannot be null. Test method for
     * {@link org.openymsg.roster.RosterEvent#RosterEvent(org.openymsg.roster.Roster, org.openymsg.network.YahooUser, org.openymsg.roster.RosterEventType)}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRosterEventArgumentValidationYahooUser() {
        new RosterEvent(new Roster(new MockFriendManager()), null, RosterEventType.add);
    }

    /**
     * Argument 'type' of the constructor cannot be null. Test method for
     * {@link org.openymsg.roster.RosterEvent#RosterEvent(org.openymsg.roster.Roster, org.openymsg.network.YahooUser, org.openymsg.roster.RosterEventType)}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRosterEventArgumentValidationType() {
        new RosterEvent(new Roster(new MockFriendManager()), new YahooUser("dummy"), null);
    }

    /**
     * Test method for {@link org.openymsg.roster.RosterEvent#getUser()}.
     */
    @Test
    public void testGetUser() {
        final YahooUser user = new YahooUser("dummy");
        for (final RosterEventType type : RosterEventType.values()) {
            assertEquals("failed on type=" + type, user, new RosterEvent(new Roster(new MockFriendManager()), user, type).getUser());
        }
    }

    /**
     * Test method for {@link org.openymsg.roster.RosterEvent#getType()}.
     */
    @Test
    public void testGetType() {
        for (final RosterEventType type : RosterEventType.values()) {
            assertEquals(type, new RosterEvent(new Roster(new MockFriendManager()), new YahooUser("dummy"), type).getType());
        }
    }

    /**
     * Test method for {@link org.openymsg.roster.RosterEvent#getSource()}.
     */
    @Test
    public void testGetSource() {
        final Roster roster = new Roster(new MockFriendManager());
        for (final RosterEventType type : RosterEventType.values()) {
            assertEquals(roster, new RosterEvent(roster, new YahooUser("dummy"), type).getSource());
        }
    }
}
