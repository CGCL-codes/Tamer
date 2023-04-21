package odrop.client.handlers;

import odrop.client.handlers.ToggleButtonGroupNotifyEvent.ToggleButtonGroupNotifyEventHandler;
import odrop.client.pages.INavigationItem;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * ToggleButtonGroupNotifyEvent is a sub-class of GwtEvent.
 * This event helps reacting to clicks of toggleButtons, that are grouped together.
 * In the navigation the buttons are grouped together so if you click on one, all the others are unpressed.
 * 
 * @author divStar
 */
public class ToggleButtonGroupNotifyEvent extends GwtEvent<ToggleButtonGroupNotifyEventHandler> {

    /**
	 * ToggleButtonGroupNotifyEventHandler is implemented by each page, that must react to a NavigationWidget-change.
	 * E.g.: if you switch to Belongings, it shows the associated controls in the navigation panel.
	 * If you switch away from it, it makes sure that the controls are hidden.
	 * Another page makes then sure that its controls are shown.
	 * 
	 * @author divStar
	 */
    public static interface ToggleButtonGroupNotifyEventHandler extends EventHandler {

        /**
		 * onReceiveNotify 
		 * @param event
		 */
        public void onReceiveNotify(ToggleButtonGroupNotifyEvent event);
    }

    public static Type<ToggleButtonGroupNotifyEventHandler> TYPE = new Type<ToggleButtonGroupNotifyEventHandler>();

    /**
	 * This method registers this event with a given eventbus, attaching it to a listener that implements
	 * the appropriate handler to react to the change (the listener must implement the handler-interface).
	 * NOTE: in ODrop these events are attached using
	 * 		 ODrop.eventBus.addHandler(Event.TYPE, EventHandler);
	 * instead of using this register-method.
	 * 
	 * @param eventBus	EventBus to register this event on (ODrop has a central eventbus in odrop.client.ODrop)
	 * @param handler	potential listener, that implements the ToggleButtonGroupNotifyEventHandler-interface
	 * @return			HandlerRegistration-object that contains further information.
	 */
    public static HandlerRegistration register(final EventBus eventBus, final ToggleButtonGroupNotifyEventHandler handler) {
        return eventBus.addHandler(ToggleButtonGroupNotifyEvent.TYPE, handler);
    }

    private INavigationItem selectedItem;

    public INavigationItem getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(INavigationItem selectedItem) {
        this.selectedItem = selectedItem;
    }

    /**
	 * Constructor
	 * Every IPage-object which receives this event, will check whether it's the selectedItem.
	 * If it isn't, it will unselect itself (its togglebutton will call setDown(false)).
	 * 
	 * @param selectedItem	specifies the INavigationItem-object that we will keep pressed
	 */
    public ToggleButtonGroupNotifyEvent(INavigationItem selectedItem) {
        this.setSelectedItem(selectedItem);
    }

    @Override
    protected void dispatch(ToggleButtonGroupNotifyEventHandler handler) {
        handler.onReceiveNotify(this);
    }

    @Override
    public Type<ToggleButtonGroupNotifyEventHandler> getAssociatedType() {
        return ToggleButtonGroupNotifyEvent.TYPE;
    }
}
