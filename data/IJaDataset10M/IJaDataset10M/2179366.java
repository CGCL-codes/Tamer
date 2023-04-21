package org.kablink.teaming.gwt.client.event;

import org.kablink.teaming.gwt.client.GwtTeaming;
import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.SimpleEventBus;

/**
 * The ViewTeamingFeedEvent is used to open the Vibe OnPrem Teaming
 * Feeds in a new window.
 * 
 * @author drfoster@novell.com
 */
public class ViewTeamingFeedEvent extends VibeEventBase<ViewTeamingFeedEvent.Handler> {

    public static Type<Handler> TYPE = new Type<Handler>();

    /**
	 * Handler interface for this event.
	 */
    public interface Handler extends EventHandler {

        void onViewTeamingFeed(ViewTeamingFeedEvent event);
    }

    /**
	 * Class constructor.
	 */
    public ViewTeamingFeedEvent() {
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
        handler.onViewTeamingFeed(this);
    }

    /**
	 * Fires a new one of these events.
	 */
    public static void fireOne() {
        GwtTeaming.fireEvent(new ViewTeamingFeedEvent());
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
        return TeamingEvents.VIEW_TEAMING_FEED;
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
