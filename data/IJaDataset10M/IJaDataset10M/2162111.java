package de.bigzee.tippspiel2008.webapp.controller;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.subethamail.wiser.Wiser;

public class PasswordHintControllerTest extends BaseControllerTestCase {

    private PasswordHintController c = null;

    public void setPasswordHintController(PasswordHintController password) {
        this.c = password;
    }

    public void testExecute() throws Exception {
        MockHttpServletRequest request = newGet("/passwordHint.html");
        request.addParameter("username", "user");
        Wiser wiser = new Wiser();
        wiser.setPort(getSmtpPort());
        wiser.start();
        c.handleRequest(request, new MockHttpServletResponse());
        wiser.stop();
        assertTrue(wiser.getMessages().size() == 1);
        assertNotNull(request.getSession().getAttribute(BaseFormController.MESSAGES_KEY));
    }
}
