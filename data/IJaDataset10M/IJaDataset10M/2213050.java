package org.kablink.teaming.gwt.client.event;

import org.kablink.teaming.gwt.client.whatsnew.ActivityStreamUIEntry;
import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.SimpleEventBus;

/**
 * The InvokeTagEvent is used to invoke the 'tag this entry' UI.
 * 
 * @author drfoster@novell.com
 */
public class InvokeTagEvent extends VibeEventBase<InvokeTagEvent.Handler> {

    public static Type<Handler> TYPE = new Type<Handler>();

    private ActivityStreamUIEntry m_uiEntry;

    /**
	 * Handler interface for this event.
	 */
    public interface Handler extends EventHandler {

        void onInvokeTag(InvokeTagEvent event);
    }

    /**
	 * Constructor methods.
	 * 
	 * @param searchTabId
	 */
    public InvokeTagEvent(ActivityStreamUIEntry uiEntry) {
        super();
        m_uiEntry = uiEntry;
    }

    public InvokeTagEvent() {
        this(null);
    }

    /**
	 * Get'er and Set'er methods.
	 * 
	 * @return
	 */
    public ActivityStreamUIEntry getUIEntry() {
        return m_uiEntry;
    }

    public void setUIEntry(ActivityStreamUIEntry uiEntry) {
        m_uiEntry = uiEntry;
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
        handler.onInvokeTag(this);
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
        return TeamingEvents.INVOKE_TAG;
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
