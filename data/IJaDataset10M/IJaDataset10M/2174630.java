package org.datacollection.webapp.action;

import org.subethamail.wiser.Wiser;

public class PasswordHintActionTest extends BaseActionTestCase {

    private PasswordHintAction action;

    public void setPasswordHintAction(PasswordHintAction action) {
        this.action = action;
    }

    public void testExecute() throws Exception {
        Wiser wiser = new Wiser();
        wiser.setPort(getSmtpPort());
        wiser.start();
        action.setUsername("user");
        assertEquals("success", action.execute());
        assertFalse(action.hasActionErrors());
        wiser.stop();
        assertTrue(wiser.getMessages().size() == 1);
        assertNotNull(action.getSession().getAttribute("messages"));
    }
}
