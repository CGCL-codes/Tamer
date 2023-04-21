package org.fb4j.friends.impl;

import net.sf.json.JSONObject;
import org.fb4j.friends.FriendListMember;
import org.fb4j.impl.AbstractJsonFacebookObject;
import org.fb4j.impl.AssertInitialized;

/**
 * @author Mino Togna
 * 
 */
public class FriendListMemberImpl extends AbstractJsonFacebookObject implements FriendListMember {

    private long friendListId;

    private long user;

    @Override
    protected void processJsonObject(JSONObject jsonObject) {
        friendListId = jsonObject.optLong("flid");
        user = jsonObject.optLong("uid");
    }

    @AssertInitialized("flid")
    public long getFriendListId() {
        return friendListId;
    }

    @AssertInitialized("uid")
    public long getUser() {
        return user;
    }
}
