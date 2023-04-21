package org.kablink.teaming.gwt.client.event;

import org.kablink.teaming.gwt.client.GwtMainPage;
import org.kablink.teaming.gwt.client.GwtTeaming;
import org.kablink.teaming.gwt.client.event.ActivityStreamExitEvent.ExitMode;
import org.kablink.teaming.gwt.client.util.GwtClientHelper;
import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.SimpleEventBus;

/**
 * The ContextChaingingEvent tells the UI that a context change is
 * about take place, but is not yet in progress.
 * 
 * @author drfoster@novell.com
 */
public class ContextChangingEvent extends VibeEventBase<ContextChangingEvent.Handler> {

    public static Type<Handler> TYPE = new Type<Handler>();

    /**
	 * Handler interface for this event.
	 */
    public interface Handler extends EventHandler {

        void onContextChanging(ContextChangingEvent event);
    }

    private ContextChangingEvent() {
        super();
    }

    /**
	 * Dispatches this event when one is triggered.
	 * 
	 * Implements GwtEvent.dispatch()
	 * 
	 * @param handler
	 */
    @Override
    protected void dispatch(Handler handler) {
        handler.onContextChanging(this);
    }

    /**
	 * Fires a new one of these events.
	 * 
	 * This method is the ONLY way to fire one of these events as it
	 * takes care of other 'pre-context switch' operations that must
	 * occur before a context switch takes place.
	 */
    public static void fireOne() {
        GwtMainPage mp = GwtTeaming.getMainPage();
        if (null != mp) {
            if (mp.isAdminActive()) {
                AdministrationExitEvent.fireOne();
            }
            if (mp.isActivityStreamActive()) {
                ActivityStreamExitEvent.fireOne(ExitMode.EXIT_FOR_CONTEXT_SWITCH);
            }
            GwtTeaming.fireEvent(new ContextChangingEvent());
        } else {
            GwtClientHelper.jsFireVibeEventOnMainEventBus(TeamingEvents.CONTEXT_CHANGING);
        }
    }

    /**
	 * Returns the GwtEvent.Type of this event.
	 *
	 * Implements GwtEvent.getAssociatedType()
	 * 
	 * @return
	 */
    @Override
    public Type<Handler> getAssociatedType() {
        return TYPE;
    }

    /**
	 * Returns the TeamingEvents enumeration value corresponding to
	 * this event.
	 * 
	 * Implements VibeBaseEvent.getEventEnum()
	 * 
	 * @return
	 */
    @Override
    public TeamingEvents getEventEnum() {
        return TeamingEvents.CONTEXT_CHANGING;
    }

    /**
	 * Registers this event on the given event bus and returns its
	 * HandlerRegistration.
	 * 
	 * @param eventBus
	 * @param handler
	 * 
	 * @return
	 */
    public static HandlerRegistration registerEvent(SimpleEventBus eventBus, Handler handler) {
        return eventBus.addHandler(TYPE, handler);
    }
}
