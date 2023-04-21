package org.kablink.teaming.gwt.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.SimpleEventBus;

/**
 * The GotoUrlEvent tells the UI go to the given url in the content frame.
 * 
 * @author jwootton@novell.com
 */
public class GotoUrlEvent extends VibeEventBase<GotoUrlEvent.Handler> {

    public static Type<Handler> TYPE = new Type<Handler>();

    private String m_url;

    /**
	 * Handler interface for this event.
	 */
    public interface Handler extends EventHandler {

        void onGotoUrl(GotoUrlEvent event);
    }

    /**
	 * Constructor methods.
	 * 
	 */
    public GotoUrlEvent(String url) {
        super();
        m_url = url;
    }

    /**
	 * Get'er methods.
	 * 
	 * @return
	 */
    public String getUrl() {
        return m_url;
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
        handler.onGotoUrl(this);
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
        return TeamingEvents.GOTO_URL;
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
