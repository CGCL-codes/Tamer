package org.chessworks.chessclub;

import junit.framework.TestCase;

public class TestSimpleLoginHandler extends TestCase {

    public void testDefaultLoginName() {
        SimpleLoginHandler handler = new SimpleLoginHandler();
        String loginName = handler.getLoginName();
        assertEquals("guest", loginName);
    }

    public void testDefaultGuest() {
        SimpleLoginHandler handler = new SimpleLoginHandler();
        handler.setGuest(true);
        String loginName = handler.getLoginName();
        assertEquals("guest", loginName);
    }

    public void testSetGuestAfter() {
        SimpleLoginHandler handler = new SimpleLoginHandler();
        handler.setLoginName("DuckStorm");
        handler.setGuest(true);
        String loginName = handler.getLoginName();
        assertEquals("guest", loginName);
    }

    public void testSetGuestBefore() {
        SimpleLoginHandler handler = new SimpleLoginHandler();
        handler.setGuest(true);
        handler.setLoginName("DuckStorm");
        String loginName = handler.getLoginName();
        assertEquals("guest", loginName);
    }

    public void testDefaultAnon() {
        SimpleLoginHandler handler = new SimpleLoginHandler();
        handler.setAnonymous(true);
        String loginName = handler.getLoginName();
        assertEquals("guest", loginName);
    }

    public void testBangAnon() {
        SimpleLoginHandler handler = new SimpleLoginHandler();
        handler.setLoginName("!DuckStorm");
        boolean anon = handler.isAnonymous();
        String loginName = handler.getLoginName();
        assertEquals(true, anon);
        assertEquals("!DuckStorm", loginName);
    }

    public void testSetAnonAfter() {
        SimpleLoginHandler handler = new SimpleLoginHandler();
        handler.setLoginName("DuckStorm");
        handler.setAnonymous(true);
        String loginName = handler.getLoginName();
        assertEquals("!DuckStorm", loginName);
    }

    public void testSetAnonBefore() {
        SimpleLoginHandler handler = new SimpleLoginHandler();
        handler.setAnonymous(true);
        handler.setLoginName("DuckStorm");
        String loginName = handler.getLoginName();
        assertEquals("!DuckStorm", loginName);
    }
}
