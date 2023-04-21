package test.AcceptanceTests;

import java.util.Collection;
import java.util.LinkedList;

public class LoginTest extends ProjectTestBridge {

    public LoginTest(String string) {
        super(string);
    }

    public void testLogin() {
        assertTrue(login("grupi", "123456"));
        assertFalse(login("grupi", ""));
        assertFalse(login(" grupi ", "123456"));
        assertFalse(login("grupi", "1234566"));
        logout("grupi");
    }

    public void testLogout() {
        login("grupi", "123456");
        assertTrue(logout("grupi"));
    }

    public void testCurrentUser() {
        String name = "grupi";
        login(name, "123456");
        assertEquals(getCurrentUser(), name);
        logout(name);
        assertNull(getCurrentUser());
    }

    public void testOnlineUsers() {
        login("grupi", "123456");
        login("dikla", "123456");
        Collection<String> list = new LinkedList<String>();
        list.add("grupi");
        list.add("dikla");
        assertEquals(getOnlineUsers(), list);
        logout("dikla");
        logout("grupi");
    }
}
