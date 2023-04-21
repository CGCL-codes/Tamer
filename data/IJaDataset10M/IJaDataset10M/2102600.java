package org.fb4j.events.impl;

import net.sf.json.JSONObject;
import org.fb4j.events.EventMembers;
import org.fb4j.impl.InvalidResponseException;
import org.fb4j.impl.JsonMethodCallBase;

/**
 * @author Mino Togna
 * 
 */
public class GetEventMembersMethodCall extends JsonMethodCallBase<EventMembers> {

    public GetEventMembersMethodCall(Long eventId) {
        super("events.getMembers");
        setParameter("eid", String.valueOf(eventId));
    }

    @Override
    protected EventMembers processJsonResponse(String responseData) throws InvalidResponseException {
        EventMembersImpl eventMembersByStatus = new EventMembersImpl();
        JSONObject object = JSONObject.fromObject(responseData);
        eventMembersByStatus.processJsonObject(object);
        return eventMembersByStatus;
    }
}
