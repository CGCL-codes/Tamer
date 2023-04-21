package com.webtair.session;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import com.webtair.session.artifact.*;

/**
 * @author Vyacheslav Yakovenko (Vaclav)
 */
public class SessionManTest extends TestCase {

    private static SessionMan<SimpleSessionInfo> sessions;

    /**
	 * @throws java.lang.Exception
	 */
    @Before
    public void setUp() throws Exception {
        sessions = new SessionMan<SimpleSessionInfo>();
    }

    /**
	 * Test method for {@link com.webtair.session.SessionMan#SessionMan(com.webtair.session.config.ConfigBean)}.
	 */
    @Test
    public void testSessionManConfigBean() {
        if (sessions == null) assertTrue("SessionMan isn't properlly init", false);
    }

    /**
	 * Test method for {@link com.webtair.session.SessionMan#validateSession(java.lang.String)}.
	 */
    @Test
    public void testValidateSession() {
        SimpleSessionInfo info = new SimpleSessionInfo();
        String idSession = sessions.putSessionInfo("test", info);
        if (!sessions.validateSession(idSession)) {
            assertTrue("Resently puted session isn't valid!", false);
        }
    }

    /**
	 * Test method for {@link com.webtair.session.SessionMan#isSessionExpired(java.lang.String)}.
	 */
    @Test
    public void testIsSessionExpired() {
        SimpleSessionInfo info = new SimpleSessionInfo();
        String idSession = sessions.putSessionInfo("test1", info);
        if (sessions.isSessionExpired(idSession)) {
            assertTrue("Resently puted session can't be expired right away", false);
        }
    }

    /**
	 * Test method for {@link com.webtair.session.SessionMan#removeSession(java.lang.String)}.
	 */
    @Test
    public void testRemoveSession() {
        SimpleSessionInfo info = new SimpleSessionInfo();
        String idSession = sessions.putSessionInfo("test1", info);
        sessions.removeSession(idSession);
        if (sessions.validateSession(idSession)) {
            assertTrue("Resently removed session can't be alive", false);
        }
    }
}
