package org.kablink.teaming.gwt.client.event;

import org.kablink.teaming.gwt.client.binderviews.ViewReady;
import org.kablink.teaming.gwt.client.util.BinderInfo;
import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.SimpleEventBus;

/**
 * The ShowProjectManagementWSEvent is used to display a project management workspace
 * 
 * @author jwootton@novell.com
 */
public class ShowProjectManagementWSEvent extends VibeEventBase<ShowProjectManagementWSEvent.Handler> {

    public static Type<Handler> TYPE = new Type<Handler>();

    private BinderInfo m_binderInfo;

    private ViewReady m_viewReady;

    /**
	 * Handler interface for this event.
	 */
    public interface Handler extends EventHandler {

        void onShowProjectManagementWS(ShowProjectManagementWSEvent event);
    }

    /**
	 * Class constructor.
	 * 
	 * @param binderInfo
	 * @param viewReady
	 */
    public ShowProjectManagementWSEvent(BinderInfo binderInfo, ViewReady viewReady) {
        super();
        m_viewReady = viewReady;
        m_binderInfo = binderInfo;
    }

    /**
	 * Get'er methods.
	 * 
	 * @return
	 */
    public BinderInfo getBinderInfo() {
        return m_binderInfo;
    }

    public ViewReady getViewReady() {
        return m_viewReady;
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
        handler.onShowProjectManagementWS(this);
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
        return TeamingEvents.SHOW_PROJECT_MANAGEMENT_WORKSPACE;
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
