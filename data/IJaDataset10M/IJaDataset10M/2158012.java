package org.fb4j.impl;

import java.util.Set;
import org.junit.Assert;
import org.fb4j.friends.FriendList;
import org.junit.Test;

/**
 * @author Mino Togna
 * @author Gino Miceli
 */
public class GetFriendListsTest extends SessionTestBase {

    @Test
    public void testGetFriendLists() {
        Set<FriendList> fieldLists = getSession().getFriendLists();
        Assert.assertTrue(!fieldLists.isEmpty());
        for (FriendList friendList : fieldLists) {
            Assert.assertNotSame(0, friendList.getId());
            Assert.assertTrue(friendList.getName().length() > 0);
        }
    }
}
