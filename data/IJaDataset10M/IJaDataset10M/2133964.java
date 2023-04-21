package org.hip.vif.mail.test;

import org.hip.vif.mail.GroupStateChangeNotification;
import junit.framework.TestCase;

/**
 * @author Benno Luthiger
 * Created on Jan 18, 2004
 */
public class GroupStateChangeNotificationTest extends TestCase {

    public GroupStateChangeNotificationTest(String arg0) {
        super(arg0);
    }

    public void testGetSubject() throws Exception {
        String lExpected = "[VIF] State change of discussion group \"The Test Group\"";
        assertEquals("subject", lExpected, GroupStateChangeNotification.getSubject("The Test Group", "en"));
    }

    public void testGetBody() throws Exception {
        String lExpected = "To the participants of the discussion group \"The Test Group\"\n\n" + "The state of the group \"The Test Group\" has changed.\n" + "Tell this!!!\n\n" + "Regards,\n\n" + "The VIF Administration";
        assertEquals("body", lExpected, GroupStateChangeNotification.getBody("The Test Group", "en", "Tell this!!!"));
    }
}
