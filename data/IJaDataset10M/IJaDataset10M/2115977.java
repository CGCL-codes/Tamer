package gear.application.events;

import gear.application.Event;
import gear.location.GearLocationProvider;

/**
 * Event used to enable or disable the current running LocationProvider/s
 * @author Stefano Driussi
 */
public class EnableDisableLocationProviderEvent extends Event {

    private boolean active;

    private GearLocationProvider target;

    /**
	 * Constructor used to define a "broadcast" event
	 * @param sender instance of the class calling this event
	 * @param active activation status
	 */
    public EnableDisableLocationProviderEvent(Object sender, boolean active) {
        this(sender, null, active);
    }

    /**
	 * Constructor used to define a "targeted" event
	 * @param sender instance of the class calling this event
	 * @param target the target location provider
	 * @param active activation status
	 */
    public EnableDisableLocationProviderEvent(Object sender, GearLocationProvider target, boolean active) {
        super(sender);
        this.active = active;
        this.target = target;
    }

    /**
	 * Sets the event as enabling
	 */
    public void setEnabled() {
        this.active = true;
    }

    /**
	 * Sets the event as disabling
	 */
    public void setDisabled() {
        this.active = false;
    }

    /**
	 * Returns whether the event is enabling
	 * @return whether the event is enabling
	 */
    public boolean isEnabling() {
        return this.active;
    }

    /**
	 * Returns the current event target
	 * @return event target
	 */
    public GearLocationProvider getTarget() {
        return target;
    }

    /**
	 * Returns event category
	 * @return current Category.HARDWARE
	 * @see gear.application.Event.Category
	 */
    public Category getCategory() {
        return Category.LOCATION;
    }
}
