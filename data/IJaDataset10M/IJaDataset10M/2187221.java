package quickfix;

import junit.framework.TestCase;

public class SessionStateTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
        MockSystemTimeSource mockTimeSource = new MockSystemTimeSource(1000);
        SystemTime.setTimeSource(mockTimeSource);
    }

    protected void tearDown() throws Exception {
        SystemTime.setTimeSource(null);
        super.tearDown();
    }

    public void testTimeoutDefaultsAreNonzero() throws Exception {
        SessionState state = new SessionState(new Object(), null, 0, false, null, Session.DEFAULT_TEST_REQUEST_DELAY_MULTIPLIER);
        state.setLastReceivedTime(900);
        assertFalse("logon timeout not init'ed", state.isLogonTimedOut());
        state.setLogoutSent(true);
        state.setLastSentTime(900);
        assertFalse("logout timeout not init'ed", state.isLogoutTimedOut());
    }

    public void testTestRequestTiming() throws Exception {
        SessionState state = new SessionState(new Object(), null, 0, false, null, Session.DEFAULT_TEST_REQUEST_DELAY_MULTIPLIER);
        state.setLastReceivedTime(950);
        state.setHeartBeatInterval(50);
        assertFalse("testRequest shouldn't be needed yet", state.isTestRequestNeeded());
        for (int i = 0; i < 5; i++) {
            state.incrementTestRequestCounter();
        }
        assertFalse("testRequest should be needed", state.isTestRequestNeeded());
        state.setHeartBeatInterval(3);
        assertFalse("testRequest shouldn't be needed yet", state.isTestRequestNeeded());
    }
}
