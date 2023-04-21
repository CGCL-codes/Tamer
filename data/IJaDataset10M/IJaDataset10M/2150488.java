package de.fuh.xpairtise.tests.common.replication;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;
import junit.framework.TestCase;
import de.fuh.xpairtise.common.SessionRole;
import de.fuh.xpairtise.common.replication.UnexpectedReplicationState;
import de.fuh.xpairtise.common.replication.elements.ReplicatedProjectInfo;
import de.fuh.xpairtise.common.replication.elements.ReplicatedUser;
import de.fuh.xpairtise.common.replication.elements.ReplicatedXPSession;
import de.fuh.xpairtise.tests.util.PrivateAccessor;

public class ReplicatedXPSessionTest extends TestCase {

    private AtomicInteger elementIdGenerator = new AtomicInteger(0);

    public void testReplicatedXPSession() throws UnexpectedReplicationState {
        ReplicatedXPSession xp = new ReplicatedXPSession("topic 1");
        assertEquals("topic 1", xp.getTopic());
        assertEquals(0, xp.listProjectNames().size());
        assertEquals(0, xp.listProjects().size());
        xp.addProject(new ReplicatedProjectInfo("test"));
        assertEquals(1, xp.listProjectNames().size());
        assertEquals(1, xp.listProjects().size());
        assertEquals(xp.toString(), xp.getTopic() + "_" + xp.getXpSessionId());
        xp.setTopic("topic 2");
        assertEquals("topic 2", xp.getTopic());
        ReplicatedUser user = createUser("John");
        assertEquals(0, xp.getNumberOfUsers());
        assertFalse(xp.containsUser(user));
        xp.join(user, SessionRole.NONE.ordinal());
        assertEquals(1, xp.getNumberOfUsers());
        assertTrue(xp.containsUser(user));
        try {
            xp.join(user, SessionRole.NONE.ordinal());
        } catch (UnexpectedReplicationState e) {
            assertEquals(e.getMessage(), "session \"" + xp.getXpSessionId() + "\" (topic \"" + xp.getTopic() + "\"): user \"" + user.getUserId() + "\" already joined");
        }
        xp.leave(user);
        assertEquals(0, xp.getNumberOfUsers());
        assertFalse(xp.containsUser(user));
        ReplicatedUser user2 = createUser("Jack");
        try {
            xp.leave(user2);
        } catch (UnexpectedReplicationState e) {
            assertEquals(e.getMessage(), "session \"" + xp.getXpSessionId() + "\" (topic \"" + xp.getTopic() + "\"): Cannot remove user \"" + user2.getUserId() + "\" because he isn't a member of this session.");
        }
    }

    private ReplicatedUser createUser(String name) {
        ReplicatedUser user = new ReplicatedUser(name);
        user.setElementId(elementIdGenerator.incrementAndGet());
        return user;
    }

    public void testRoleAssignment() throws UnexpectedReplicationState {
        ReplicatedXPSession session = new ReplicatedXPSession("topic 1");
        assertEquals(0, session.getNumberOfUsers());
        ReplicatedUser user1 = createUser("user 1");
        ReplicatedUser user2 = createUser("user 2");
        ReplicatedUser user3 = createUser("user 3");
        ReplicatedUser user4 = createUser("user 4");
        assertEquals(SessionRole.NONE, user1.getSessionRole());
        assertEquals(SessionRole.NONE, user2.getSessionRole());
        assertEquals(SessionRole.NONE, user3.getSessionRole());
        assertEquals(SessionRole.NONE, user4.getSessionRole());
        session.join(user1, SessionRole.NONE.ordinal());
        assertEquals(SessionRole.DRIVER, user1.getSessionRole());
        assertEquals(user1, session.getDriver());
        session.join(user2, SessionRole.NONE.ordinal());
        assertEquals(SessionRole.NAVIGATOR, user2.getSessionRole());
        assertEquals(user2, session.getNavigator());
        session.join(user3, SessionRole.NONE.ordinal());
        assertEquals(SessionRole.SPECTATOR, user3.getSessionRole());
        session.join(user4, SessionRole.NONE.ordinal());
        assertEquals(SessionRole.SPECTATOR, user4.getSessionRole());
        session.leave(user1);
        session.leave(user4);
        session.join(user4, SessionRole.NONE.ordinal());
        assertEquals(SessionRole.DRIVER, user4.getSessionRole());
        session.leave(user2);
        session.leave(user3);
        session.join(user3, SessionRole.NONE.ordinal());
        assertEquals(SessionRole.NAVIGATOR, user3.getSessionRole());
        session.join(user1, SessionRole.NONE.ordinal());
        session.join(user2, SessionRole.NONE.ordinal());
        assertEquals(SessionRole.SPECTATOR, user1.getSessionRole());
        assertEquals(SessionRole.SPECTATOR, user2.getSessionRole());
    }

    public void testPerformCopyDataFromValidatedElement() throws UnexpectedReplicationState {
        ReplicatedXPSession session1 = new ReplicatedXPSession("topic 1", "group 1");
        session1.setElementId(elementIdGenerator.incrementAndGet());
        assertEquals(0, session1.getNumberOfUsers());
        LinkedList<ReplicatedProjectInfo> projectInfoList = new LinkedList<ReplicatedProjectInfo>();
        projectInfoList.add(new ReplicatedProjectInfo("test"));
        ReplicatedXPSession session2 = new ReplicatedXPSession("", "");
        assertNotNull(session2.listProjects());
        session2.setElementId(session1.getElementId());
        assertEquals(0, session2.getNumberOfUsers());
        ReplicatedUser user1 = createUser("user 1");
        ReplicatedUser user2 = createUser("user 2");
        session1.join(user1, SessionRole.NONE.ordinal());
        session1.join(user2, SessionRole.NONE.ordinal());
        Object[] params = { session1 };
        PrivateAccessor.invokePrivateMethod((Object) session2, "performCopyDataFromValidatedElement", params);
        assertEquals(session1.getTopic(), session2.getTopic());
        assertEquals(session1.getXpSessionId(), session2.getXpSessionId());
        assertEquals(session1.listProjectNames().size(), session2.listProjectNames().size());
        assertEquals(session1.listProjects().size(), session2.listProjects().size());
        assertEquals(session1.getDriver(), session2.getDriver());
        assertEquals(session1.getNavigator(), session2.getNavigator());
        assertEquals(session1.getNumberOfUsers(), session2.getNumberOfUsers());
        assertEquals(session1.getUserGroup(), session2.getUserGroup());
        assertTrue(areCollectionsEqual(session1.getParticipants(), session2.getParticipants()));
    }

    private boolean areCollectionsEqual(Collection<ReplicatedUser> coll1, Collection<ReplicatedUser> coll2) {
        return ((coll1.size() == coll2.size()) & ((coll1.containsAll(coll2)) & (coll2.containsAll(coll1))));
    }

    public void testPreferredRoleAssignment() throws UnexpectedReplicationState {
        ReplicatedXPSession session = new ReplicatedXPSession("topic 1");
        assertEquals(0, session.getNumberOfUsers());
        ReplicatedUser user1 = createUser("user 1");
        ReplicatedUser user2 = createUser("user 2");
        ReplicatedUser user3 = createUser("user 3");
        session.join(user1, SessionRole.DRIVER.ordinal());
        assertEquals(SessionRole.DRIVER, user1.getSessionRole());
        assertEquals(user1, session.getDriver());
        session.join(user2, SessionRole.NAVIGATOR.ordinal());
        assertEquals(SessionRole.NAVIGATOR, user2.getSessionRole());
        assertEquals(user2, session.getNavigator());
        assertFalse(session.getDriverRequestsRoleChange());
        assertFalse(session.getNavigatorRequestsRoleChange());
        boolean result;
        result = session.updateRoleChangeRequestState(user2, true);
        assertTrue(result);
        assertFalse(session.getDriverRequestsRoleChange());
        assertTrue(session.getNavigatorRequestsRoleChange());
        result = session.updateRoleChangeRequestState(user3, true);
        assertFalse(result);
        assertFalse(session.getDriverRequestsRoleChange());
        assertTrue(session.getNavigatorRequestsRoleChange());
        result = session.updateRoleChangeRequestState(user1, true);
        assertTrue(result);
        assertTrue(session.getDriverRequestsRoleChange());
        assertTrue(session.getNavigatorRequestsRoleChange());
    }

    public void testRoleChangeRequests() throws UnexpectedReplicationState {
        ReplicatedXPSession session = new ReplicatedXPSession("topic 1");
        assertEquals(0, session.getNumberOfUsers());
        ReplicatedUser user1 = createUser("user 1");
        ReplicatedUser user2 = createUser("user 2");
        ReplicatedUser user3 = createUser("user 3");
        session.join(user1, SessionRole.SPECTATOR.ordinal());
        assertEquals(SessionRole.SPECTATOR, user1.getSessionRole());
        session.join(user2, SessionRole.NAVIGATOR.ordinal());
        assertEquals(SessionRole.NAVIGATOR, user2.getSessionRole());
        assertEquals(user2, session.getNavigator());
        session.join(user3, SessionRole.DRIVER.ordinal());
        assertEquals(SessionRole.DRIVER, user3.getSessionRole());
        assertEquals(user3, session.getDriver());
    }
}
