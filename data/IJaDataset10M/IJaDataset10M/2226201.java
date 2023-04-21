package org.fb4j.impl;

import junit.framework.Assert;
import org.fb4j.Session;
import org.fb4j.groups.GroupMembers;
import org.junit.Test;

/**
 * @author Mino Togna
 * 
 */
public class GetGroupMembersTest extends SessionTestBase {

    @Test
    public void testGetGroupMembers() {
        Session session = getSession();
        GroupMembers groupMembers = session.getGroupMembers(2207661688L);
        Assert.assertNotNull(groupMembers);
        log.debug(groupMembers);
    }
}
