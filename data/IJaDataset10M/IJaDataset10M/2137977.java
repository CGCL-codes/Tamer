package org.fb4j.groups.impl;

import net.sf.json.JSONObject;
import org.fb4j.groups.GroupMember;
import org.fb4j.impl.AbstractJsonFacebookObject;
import org.fb4j.impl.AssertInitialized;

/**
 * @author Mino Togna
 * 
 */
public class GroupMemberImpl extends AbstractJsonFacebookObject implements GroupMember {

    private long user, groupId;

    private String positions;

    @Override
    protected void processJsonObject(JSONObject jsonObject) {
        user = jsonObject.optLong("uid");
        groupId = jsonObject.optLong("gid");
        positions = jsonObject.optString("positions");
    }

    @AssertInitialized("uid")
    public long getUser() {
        return user;
    }

    @AssertInitialized("gid")
    public long getGroupId() {
        return groupId;
    }

    @AssertInitialized("positions")
    public String getPositions() {
        return positions;
    }
}
