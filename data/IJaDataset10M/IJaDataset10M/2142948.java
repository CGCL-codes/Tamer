package megamek.client.event;

/**
 * This adapter class provides default implementations for the methods described
 * by the <code>MechDisplayListener</code> interface.
 * <p>
 * Classes that wish to deal with <code>MechDisplayEvent</code>s can extend
 * this class and override only the methods which they are interested in.
 * </p>
 * 
 * @see MechDisplayListener
 * @see MechDisplayEvent
 */
public class MechDisplayListenerAdapter implements MechDisplayListener {

    /**
     * Sent when user selects a weapon in the weapon panel.
     * 
     * @param b an event
     */
    public void WeaponSelected(MechDisplayEvent b) {
    }
}
