package org.kablink.teaming.gwt.client.event;

import org.kablink.teaming.gwt.client.util.SimpleProfileParams;
import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.SimpleEventBus;

/**
 * The InvokeSimpleProfileEvent is used to invoke the simple profile
 * dialog.
 * 
 * @author drfoster@novell.com
 */
public class InvokeSimpleProfileEvent extends VibeEventBase<InvokeSimpleProfileEvent.Handler> {

    public static Type<Handler> TYPE = new Type<Handler>();

    public SimpleProfileParams m_simpleProfileParams;

    /**
	 * Handler interface for this event.
	 */
    public interface Handler extends EventHandler {

        void onInvokeSimpleProfile(InvokeSimpleProfileEvent event);
    }

    /**
	 * Class constructor.
	 */
    public InvokeSimpleProfileEvent(SimpleProfileParams simpleProfileParams) {
        super();
        m_simpleProfileParams = simpleProfileParams;
    }

    /**
	 * Get'er methods.
	 * 
	 * @return
	 */
    public SimpleProfileParams getSimpleProfileParams() {
        return m_simpleProfileParams;
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
        handler.onInvokeSimpleProfile(this);
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
        return TeamingEvents.INVOKE_SIMPLE_PROFILE;
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
