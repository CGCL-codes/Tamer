package com.mycompany.system.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.apache.struts2.ServletActionContext;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.subethamail.wiser.Wiser;
import com.mycompany.core.Constants;
import com.mycompany.core.model.Address;
import com.mycompany.core.model.User;
import com.mycompany.core.webapp.action.BaseAction;
import com.mycompany.core.webapp.action.BaseActionTestCase;
import com.mycompany.system.webapp.action.SignupAction;
import com.opensymphony.xwork2.Action;

public class SignupActionTest extends BaseActionTestCase {

    @Autowired
    private SignupAction action;

    @Test
    public void testDisplayForm() throws Exception {
        final MockHttpServletRequest request = new MockHttpServletRequest(null, "GET", "/signup.html");
        ServletActionContext.setRequest(request);
        assertEquals("input", action.execute());
    }

    @Test
    public void testExecutePost() throws Exception {
        final MockHttpServletRequest request = new MockHttpServletRequest(null, "POST", "/signup.html");
        ServletActionContext.setRequest(request);
        assertEquals(Action.SUCCESS, action.execute());
    }

    @Test
    public void testExecuteCancel() throws Exception {
        action.setCancel(BaseAction.CANCEL);
        assertEquals(BaseAction.CANCEL, action.execute());
    }

    @Test
    public void testDefault() throws Exception {
        action.setCancel(BaseAction.CANCEL);
        assertEquals(BaseAction.INPUT, action.doDefault());
    }

    @Test
    public void testSave() throws Exception {
        final User user = createUser();
        final User user2 = createUser();
        action.setUser(user);
        ServletActionContext.setResponse(new MockHttpServletResponse());
        final Wiser wiser = new Wiser();
        wiser.setPort(getSmtpPort());
        wiser.start();
        assertNull(action.getUser().getId());
        assertEquals("success", action.save());
        assertFalse(action.hasActionErrors());
        assertNotNull(action.getUser().getId());
        wiser.stop();
        assertTrue(wiser.getMessages().size() == 1);
        assertNotNull(action.getSession().getAttribute(Constants.REGISTERED));
        action.setUser(user2);
        assertEquals(Action.INPUT, action.save());
        assertTrue(action.hasActionErrors());
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    private User createUser() {
        final User user = new User();
        user.setUsername("self-registered");
        user.setPassword("Password1");
        user.setConfirmPassword("Password1");
        user.setFirstName("First");
        user.setLastName("Last");
        final Address address = new Address();
        address.setCity("Denver");
        address.setProvince("CO");
        address.setCountry("USA");
        address.setPostalCode("80210");
        user.setAddress(address);
        user.setEmail("self-registered@raibledesigns.com");
        user.setWebsite("http://raibledesigns.com");
        user.setPasswordHint("Password is one with you.");
        return user;
    }
}
