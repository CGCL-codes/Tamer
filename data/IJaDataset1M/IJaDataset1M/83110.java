package org.kablink.teaming.gwt.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.SimpleEventBus;

/**
 * The InvokeColumnResizerEvent is used to invoke Vibe's column sizing
 * dialog on the specified binder.
 * 
 * @author drfoster@novell.com
 */
public class InvokeColumnResizerEvent extends VibeEventBase<InvokeColumnResizerEvent.Handler> {

    public static Type<Handler> TYPE = new Type<Handler>();

    private Long m_binderId;

    /**
	 * Handler interface for this event.
	 */
    public interface Handler extends EventHandler {

        void onInvokeColumnResizer(InvokeColumnResizerEvent event);
    }

    /**
	 * Class constructor.
	 */
    public InvokeColumnResizerEvent() {
        super();
    }

    /**
	 * Class constructor.
	 * 
	 * @param binderId
	 */
    public InvokeColumnResizerEvent(Long binderId) {
        this();
        setBinderId(binderId);
    }

    /**
	 * Get'er methods.
	 * 
	 * @return
	 */
    public Long getBinderId() {
        return m_binderId;
    }

    /**
	 * Set'er methods.
	 * 
	 * @param binderId
	 */
    public void setBinderId(Long binderId) {
        m_binderId = binderId;
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
        handler.onInvokeColumnResizer(this);
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
        return TeamingEvents.INVOKE_COLUMN_RESIZER;
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
