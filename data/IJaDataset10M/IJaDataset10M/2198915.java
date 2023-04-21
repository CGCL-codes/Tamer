package org.kablink.teaming.gwt.client.event;

import org.kablink.teaming.gwt.client.util.OnSelectBinderInfo;
import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.SimpleEventBus;

/**
 * The ChangeContextEvent tells the UI that a context change is
 * currently in progress.
 * 
 * @author drfoster@novell.com
 */
public class ChangeContextEvent extends VibeEventBase<ChangeContextEvent.Handler> {

    public static Type<Handler> TYPE = new Type<Handler>();

    private OnSelectBinderInfo m_osbi;

    /**
	 * Handler interface for this event.
	 */
    public interface Handler extends EventHandler {

        void onChangeContext(ChangeContextEvent event);
    }

    /**
	 * Constructor methods.
	 * 
	 * @param osbi
	 */
    public ChangeContextEvent(OnSelectBinderInfo osbi) {
        super();
        m_osbi = osbi;
    }

    /**
	 * Get'er methods.
	 * 
	 * @return
	 */
    public OnSelectBinderInfo getOnSelectBinderInfo() {
        return m_osbi;
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
        handler.onChangeContext(this);
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
        return TeamingEvents.CONTEXT_CHANGED;
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
