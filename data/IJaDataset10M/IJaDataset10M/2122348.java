package org.fb4j.impl;

import java.util.Set;
import junit.framework.Assert;
import org.fb4j.Session;
import org.junit.Test;

/**
 * @author Mino Togna
 *
 */
public class SendNotificationTest extends SessionTestBase {

    @Test
    public void testSendNotification() {
        Session session = getSession();
        Set<Long> set = session.sendNotification("is sending this notification via fb4j2", new Long[] { 752141196L, 733192756L });
        Assert.assertNotNull(set);
        Assert.assertSame(2, set.size());
        for (Long long1 : set) {
            log.debug(long1);
        }
    }

    @Test
    public void testSendNotificationAppToUser() {
        Session session = getSession();
        Set<Long> set = session.sendNotification("app_to_user notification type via fb4j2", new Long[] { 752141196L, 733192756L }, "app_to_user");
        Assert.assertNotNull(set);
        Assert.assertSame(2, set.size());
        for (Long long1 : set) {
            log.debug(long1);
        }
    }
}
