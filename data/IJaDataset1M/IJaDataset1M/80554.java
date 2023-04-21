package br.ufmg.ubicomp.droidguide.eventservice;

import junit.framework.TestCase;
import br.ufmg.ubicomp.droidguide.common.DroidGuideUser;
import br.ufmg.ubicomp.droidguide.eventservice.enums.EventState;
import br.ufmg.ubicomp.droidguide.eventservice.event.UserEvent;
import br.ufmg.ubicomp.droidguide.eventservice.exception.EventServiceError;
import br.ufmg.ubicomp.droidguide.eventservice.exception.EventServiceException;
import br.ufmg.ubicomp.droidguide.eventservice.management.EventManager;

public class TestEventManager extends TestCase {

    private EventManager manager;

    private DroidGuideUser user;

    protected void setUp() throws Exception {
        manager = EventManager.getInstance();
        user = new DroidGuideUser();
    }

    protected void tearDown() throws Exception {
        manager = null;
    }

    public void testSubscribeForEvent() throws EventServiceException {
        UserEvent event = new UserEvent();
        assertTrue("This user should not have any events", manager.getUserEvents().isEmpty());
        manager.subscribeForEvent(event);
        assertFalse("This user should have events", manager.getUserEvents().isEmpty());
        assertTrue("This user should have this event", manager.getUserEvents().contains(event));
    }

    public void testUnsubscribeForEvent() throws EventServiceException {
        UserEvent event = new UserEvent();
        manager.subscribeForEvent(event);
        assertTrue("This user should not have events", manager.getUserEvents().isEmpty());
        assertFalse("This user should not have this event", manager.getUserEvents().contains(event));
    }

    public void testConsumeEvent() throws EventServiceException {
        DroidGuideUser user = new DroidGuideUser();
        UserEvent event = new UserEvent();
        manager.subscribeForEvent(event);
        manager.consumeEvent(event, user);
        assertEquals(EventState.CONSUMED, event.getState());
        assertFalse("This user should not have this event", manager.getUserEvents().contains(event));
    }

    public void testConsumeEventError1() {
        UserEvent event = new UserEvent();
        try {
            manager.subscribeForEvent(event);
            event.setState(EventState.OUTDATED);
            manager.consumeEvent(event, user);
        } catch (EventServiceException e) {
            assertEquals(EventServiceError.EVENT_CANNOT_BE_CONSUMED, e.getErrorCode());
        }
    }
}
