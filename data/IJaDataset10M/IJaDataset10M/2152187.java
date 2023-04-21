package com.bluemarsh.jswat.core.session;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Tests the SessionEventMulticaster class.
 */
public class SessionEventMulticasterTest {

    @Test
    public void test_SessionEventMulticaster() {
        SessionListener sl = SessionEventMulticaster.add(null, null);
        assertEquals(sl, null);
        sl = SessionEventMulticaster.remove(null, null);
        assertEquals(sl, null);
        TestListener l1 = new TestListener(false);
        sl = SessionEventMulticaster.add(sl, l1);
        TestListener l2 = new TestListener(true);
        sl = SessionEventMulticaster.add(sl, l2);
        assertEquals(0, l1.activated);
        assertEquals(0, l1.closing);
        assertEquals(0, l1.deactivated);
        assertEquals(0, l1.opened);
        assertEquals(0, l1.resuming);
        assertEquals(0, l1.suspended);
        Session session = new DummySession();
        session.setIdentifier("eventUnitTest1");
        SessionEvent sevt = new SessionEvent(session, SessionEventType.OPENED);
        sevt.getType().fireEvent(sevt, sl);
        assertEquals(0, l1.activated);
        assertEquals(0, l1.closing);
        assertEquals(0, l1.deactivated);
        assertEquals(1, l1.opened);
        assertEquals(0, l1.resuming);
        assertEquals(0, l1.suspended);
        sevt = new SessionEvent(session, SessionEventType.CONNECTED);
        sevt.getType().fireEvent(sevt, sl);
        assertEquals(1, l1.activated);
        assertEquals(0, l1.closing);
        assertEquals(0, l1.deactivated);
        assertEquals(1, l1.opened);
        assertEquals(0, l1.resuming);
        assertEquals(0, l1.suspended);
        assertEquals(0, l2.listener.activated);
        assertEquals(0, l2.listener.closing);
        assertEquals(0, l2.listener.deactivated);
        assertEquals(1, l2.listener.opened);
        assertEquals(0, l2.listener.resuming);
        assertEquals(0, l2.listener.suspended);
        sevt = new SessionEvent(session, SessionEventType.RESUMING);
        sevt.getType().fireEvent(sevt, sl);
        assertEquals(1, l1.activated);
        assertEquals(0, l1.closing);
        assertEquals(0, l1.deactivated);
        assertEquals(1, l1.opened);
        assertEquals(1, l1.resuming);
        assertEquals(0, l1.suspended);
        sevt = new SessionEvent(session, SessionEventType.SUSPENDED);
        sevt.getType().fireEvent(sevt, sl);
        assertEquals(1, l1.activated);
        assertEquals(0, l1.closing);
        assertEquals(0, l1.deactivated);
        assertEquals(1, l1.opened);
        assertEquals(1, l1.resuming);
        assertEquals(1, l1.suspended);
        sevt = new SessionEvent(session, SessionEventType.DISCONNECTED);
        sevt.getType().fireEvent(sevt, sl);
        assertEquals(1, l1.activated);
        assertEquals(0, l1.closing);
        assertEquals(1, l1.deactivated);
        assertEquals(1, l1.opened);
        assertEquals(1, l1.resuming);
        assertEquals(1, l1.suspended);
        assertEquals(0, l2.listener.activated);
        assertEquals(1, l2.listener.closing);
        assertEquals(0, l2.listener.deactivated);
        assertEquals(1, l2.listener.opened);
        assertEquals(0, l2.listener.resuming);
        assertEquals(0, l2.listener.suspended);
        sevt = new SessionEvent(session, SessionEventType.CLOSING);
        sevt.getType().fireEvent(sevt, sl);
        assertEquals(1, l1.activated);
        assertEquals(1, l1.closing);
        assertEquals(1, l1.deactivated);
        assertEquals(1, l1.opened);
        assertEquals(1, l1.resuming);
        assertEquals(1, l1.suspended);
        l1 = new TestListener(false);
        sl = SessionEventMulticaster.add(sl, l1);
        l2 = new TestListener(true);
        sl = SessionEventMulticaster.add(sl, l2);
        assertEquals(0, l1.activated);
        assertEquals(0, l1.closing);
        assertEquals(0, l1.deactivated);
        assertEquals(0, l1.opened);
        assertEquals(0, l1.resuming);
        assertEquals(0, l1.suspended);
        sevt = new SessionEvent(session, SessionEventType.OPENED);
        sevt.getType().fireEvent(sevt, sl);
        assertEquals(0, l1.activated);
        assertEquals(0, l1.closing);
        assertEquals(0, l1.deactivated);
        assertEquals(1, l1.opened);
        assertEquals(0, l1.resuming);
        assertEquals(0, l1.suspended);
        sevt = new SessionEvent(session, SessionEventType.CONNECTED);
        sevt.getType().fireEvent(sevt, sl);
        sl = SessionEventMulticaster.remove(sl, l2);
        assertEquals(1, l1.activated);
        assertEquals(0, l1.closing);
        assertEquals(0, l1.deactivated);
        assertEquals(1, l1.opened);
        assertEquals(0, l1.resuming);
        assertEquals(0, l1.suspended);
        sevt = new SessionEvent(session, SessionEventType.DISCONNECTED);
        sevt.getType().fireEvent(sevt, sl);
        sevt = new SessionEvent(session, SessionEventType.CLOSING);
        sevt.getType().fireEvent(sevt, sl);
        sl = SessionEventMulticaster.remove(sl, l1);
        assertEquals(1, l1.activated);
        assertEquals(1, l1.closing);
        assertEquals(1, l1.deactivated);
        assertEquals(1, l1.opened);
        assertEquals(0, l1.resuming);
        assertEquals(0, l1.suspended);
    }

    private static class TestListener implements SessionListener {

        public TestListener listener;

        public int activated;

        public int closing;

        public int deactivated;

        public int opened;

        public int resuming;

        public int suspended;

        TestListener(boolean mutate) {
            if (mutate) {
                listener = new TestListener(false);
            }
        }

        @Override
        public void connected(SessionEvent sevt) {
            if (listener != null) {
                sevt.getSession().addSessionListener(listener);
            }
            activated++;
        }

        @Override
        public void closing(SessionEvent sevt) {
            closing++;
        }

        @Override
        public void disconnected(SessionEvent sevt) {
            if (listener != null) {
                sevt.getSession().removeSessionListener(listener);
            }
            deactivated++;
        }

        @Override
        public void opened(Session session) {
            opened++;
        }

        @Override
        public void resuming(SessionEvent sevt) {
            resuming++;
        }

        @Override
        public void suspended(SessionEvent sevt) {
            suspended++;
        }
    }
}
