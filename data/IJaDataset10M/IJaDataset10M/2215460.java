package org.kablink.teaming.gwt.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.SimpleEventBus;

/**
 * The TrashPurgeAllEvent is used to purge all the entries from the
 * trash.
 * 
 * @author drfoster@novell.com
 */
public class TrashPurgeAllEvent extends VibeEventBase<TrashPurgeAllEvent.Handler> {

    public static Type<Handler> TYPE = new Type<Handler>();

    public Long m_binderId;

    /**
	 * Handler interface for this event.
	 */
    public interface Handler extends EventHandler {

        void onTrashPurgeAll(TrashPurgeAllEvent event);
    }

    /**
	 * Class constructor.
	 */
    public TrashPurgeAllEvent() {
        super();
    }

    /**
	 * Class constructor.
	 * 
	 * @param binderId
	 */
    public TrashPurgeAllEvent(Long binderId) {
        this();
        m_binderId = binderId;
    }

    /**
	 * Get'er method.
	 * 
	 * @return
	 */
    public Long getBinderId() {
        return m_binderId;
    }

    /**
	 * Set'er method.
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
        handler.onTrashPurgeAll(this);
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
        return TeamingEvents.TRASH_PURGE_ALL;
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
