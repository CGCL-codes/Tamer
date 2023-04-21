package uk.azdev.openfire.net.messages.incoming;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import uk.azdev.openfire.common.SessionId;
import uk.azdev.openfire.net.attrvalues.Int32AttributeValue;
import uk.azdev.openfire.net.attrvalues.ListAttributeValue;
import uk.azdev.openfire.net.attrvalues.SessionIdAttributeValue;
import uk.azdev.openfire.net.attrvalues.StringKeyedAttributeMap;
import uk.azdev.openfire.net.messages.IMessage;
import uk.azdev.openfire.net.messages.StringMapBasedMessage;

public class UserSessionIdListMessage extends StringMapBasedMessage {

    public static final int USER_SESSION_ID_LIST_MESSAGE_ID = 132;

    private static final String USER_ID_LIST_KEY = "userid";

    private static final String SESSION_ID_LIST_KEY = "sid";

    private Map<Long, SessionId> userIdToSessionIdMap;

    public UserSessionIdListMessage() {
        userIdToSessionIdMap = new LinkedHashMap<Long, SessionId>();
    }

    public int getMessageId() {
        return USER_SESSION_ID_LIST_MESSAGE_ID;
    }

    public IMessage newInstance() {
        return new UserSessionIdListMessage();
    }

    @Override
    protected void interpretAttributeMap(StringKeyedAttributeMap map) {
        List<Long> userIdList = map.getAttributeValueAsList(USER_ID_LIST_KEY, new Int32AttributeValue());
        List<SessionId> sessionIdList = map.getAttributeValueAsList(SESSION_ID_LIST_KEY, new SessionIdAttributeValue());
        Iterator<Long> userIdIter = userIdList.iterator();
        Iterator<SessionId> sessionIdIter = sessionIdList.iterator();
        while (userIdIter.hasNext()) {
            long userId = userIdIter.next();
            SessionId sessionId = sessionIdIter.next();
            userIdToSessionIdMap.put(userId, sessionId);
        }
    }

    @Override
    protected void populateAttributeMap(StringKeyedAttributeMap map) {
        ListAttributeValue userIdList = new ListAttributeValue();
        ListAttributeValue sessionIdList = new ListAttributeValue();
        for (Entry<Long, SessionId> entry : userIdToSessionIdMap.entrySet()) {
            userIdList.addValue(new Int32AttributeValue(entry.getKey()));
            sessionIdList.addValue(new SessionIdAttributeValue(entry.getValue()));
        }
        map.addAttribute(USER_ID_LIST_KEY, userIdList);
        map.addAttribute(SESSION_ID_LIST_KEY, sessionIdList);
    }

    public Set<Long> getUserIdList() {
        return userIdToSessionIdMap.keySet();
    }

    public SessionId getSessionIdForUser(long userId) {
        return userIdToSessionIdMap.get(userId);
    }

    public void addUserMapping(long userId, SessionId sessionId) {
        userIdToSessionIdMap.put(userId, sessionId);
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("User Session ID List Message");
        for (Entry<Long, SessionId> entry : userIdToSessionIdMap.entrySet()) {
            buffer.append("\n\t");
            buffer.append(entry.getKey());
            buffer.append(" -> ");
            buffer.append(entry.getValue().toString());
        }
        return buffer.toString();
    }
}
