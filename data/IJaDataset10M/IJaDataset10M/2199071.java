package org.openymsg.roster;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openymsg.network.FriendManager;
import org.openymsg.network.YahooProtocol;

/**
 * Mock (empty) implementation of the FriendManager interface. Useful for unit testing.
 * 
 * @author Guus der Kinderen, guus@nimbuzz.com
 */
public class MockFriendManager implements FriendManager {

    private static final Log log = LogFactory.getLog(MockFriendManager.class);

    private String friendId = null;

    private String groupId = null;

    private String method = null;

    public void removeFriendFromGroup(String friendId, String groupId) throws IOException {
        log.info("Mock removeFriendFromGroup triggered with friendId [" + friendId + "] and groupId [" + groupId + "]");
        this.friendId = friendId;
        this.groupId = groupId;
        this.method = "removeFriendFromGroup";
    }

    public void sendNewFriendRequest(String userId, String groupId, YahooProtocol yahooProtocol) throws IOException {
        log.info("Mock sendNewFriendRequest triggered with friendId [" + userId + "] and groupId [" + groupId + "]");
        this.friendId = userId;
        this.groupId = groupId;
        this.method = "sendNewFriendRequest";
    }

    /**
     * Resets the data by setting them to <tt>null</tt>
     */
    public void reset() {
        friendId = null;
        groupId = null;
        method = null;
    }

    /**
     * @return the friendId
     */
    public String getFriendId() {
        return friendId;
    }

    /**
     * @return the groupId
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * @return the method
     */
    public String getMethod() {
        return method;
    }
}
