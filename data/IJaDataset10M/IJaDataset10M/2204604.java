package org.havi.ui;

/**
 * This interface is implemented by all HAVi UI components which have some form
 * of adjustable numerical value (e.g. a range control).
 * <h3>Event Behavior</h3>
 * Subclasses of {@link org.havi.ui.HComponent}which implement
 * <code>HAdjustmentValue</code> must respond to
 * {@link org.havi.ui.event.HFocusEvent}and {@link
 * org.havi.ui.event.HAdjustmentEvent} events.
 * <p>
 * Applications should assume that classes which implement
 * <code>HAdjustmentValue</code> can generate events of the types
 * <code>HFocusEvent</code> and <code>HAdjustmentEvent</code> in response to
 * other types of input event.
 * <p>
 * An application may add one or more <code>HAdjustmentListener</code>
 * listeners to the component. The {@link
 * org.havi.ui.event.HAdjustmentListener#valueChanged} method of the
 * <code>HAdjustmentListener</code> is invoked whenever the value of the
 * <code>HAdjustmentValue</code> is modified.
 * <p>
 * HAVi adjustment events are discussed in detail in the {@link
 * org.havi.ui.HAdjustmentInputPreferred} interface description.
 * <h3>Interaction States</h3>
 * The following interaction states are valid for this
 * <code>HAdjustmentValue</code> component:
 * <p>
 * <ul>
 * <li>{@link org.havi.ui.HState#NORMAL_STATE NORMAL_STATE}
 * <li>{@link org.havi.ui.HState#FOCUSED_STATE FOCUSED_STATE}
 * <li>{@link org.havi.ui.HState#DISABLED_STATE DISABLED_STATE}
 * <li>{@link org.havi.ui.HState#DISABLED_FOCUSED_STATE DISABLED_FOCUSED_STATE}
 * </ul>
 * <p>
 * The state machine diagram below shows the valid state transitions for an
 * <code>HAdjustmentValue</code> component.
 * <p>
 * <table border>
 * <tr>
 * <td><img src="../../../images/HxxxValue_state.gif"></td>
 * </tr>
 * </table>
 * <p>
 * <h3>Platform Classes</h3>
 * The following HAVi platform classes implement or inherit the
 * <code>HAdjustmentValue</code> interface. These classes shall all generate
 * both <code>HFocusEvent</code> and <code>HAdjustmentEvent</code> events in
 * addition to any other events specified in the respective class descriptions.
 * <p>
 * <ul>
 * <li>{@link org.havi.ui.HRangeValue}
 * </ul>
 * <p>
 * @see org.havi.ui.HNavigable
 * @see org.havi.ui.HOrientable
 * @see org.havi.ui.HAdjustmentInputPreferred
 * @see org.havi.ui.event.HAdjustmentEvent
 * @see org.havi.ui.event.HAdjustmentListener
 */
public interface HAdjustmentValue extends HNavigable, HAdjustmentInputPreferred {

    /**
	 * Set the unit increment for this <code>HAdjustmentValue</code>.
	 * @param increment the amount by which the value of the
	 *            <code>HAdjustmentValue</code> should change when an {@link
	 *            org.havi.ui.event.HAdjustmentEvent#ADJUST_LESS} or {@link
	 *            org.havi.ui.event.HAdjustmentEvent#ADJUST_MORE} event is
	 *            received. Values of <code>increment</code> less than one
	 *            shall be treated as a value of one.
	 */
    public void setUnitIncrement(int increment);

    /**
	 * Get the unit increment for this <code>HAdjustmentValue</code>.
	 * <code>1</code> shall be returned if this method is called before its
	 * corresponding set method.
	 * @return the increment value for this <code>HAdjustmentValue</code>.
	 */
    public int getUnitIncrement();

    /**
	 * Set the block increment for this <code>HAdjustmentValue</code>.
	 * @param increment the amount by which the value of the
	 *            <code>HAdjustmentValue</code> should change when an {@link
	 *            org.havi.ui.event.HAdjustmentEvent#ADJUST_PAGE_LESS} or
	 *            {@link org.havi.ui.event.HAdjustmentEvent#ADJUST_PAGE_MORE}
	 *            event is received. Values of <code>increment</code> less
	 *            than one shall be treated as a value of one.
	 */
    public void setBlockIncrement(int increment);

    /**
	 * Get the block increment for this <code>HAdjustmentValue</code>.
	 * <code>1</code> shall be returned if this method is called before its
	 * corresponding set method.
	 * @return the block increment value for this <code>HAdjustmentValue</code>.
	 */
    public int getBlockIncrement();

    /**
	 * Adds the specified HAdjustmentListener to receive
	 * <code>HAdjustmentEvents</code> sent from this object. If the listener
	 * has already been added further calls will add further references to the
	 * listener, which will then receive multiple copies of a single event.
	 * @param l the HAdjustmentListener to be notified.
	 */
    public void addAdjustmentListener(org.havi.ui.event.HAdjustmentListener l);

    /**
	 * Removes the specified HAdjustmentListener so that it no longer receives
	 * <code>HAdjustmentEvents</code> from this object. If the specified
	 * listener is not registered, the method has no effect. If multiple
	 * references to a single listener have been registered it should be noted
	 * that this method will only remove one reference per call.
	 * @param l the HAdjustmentListener to be removed from notification.
	 */
    public void removeAdjustmentListener(org.havi.ui.event.HAdjustmentListener l);

    /**
	 * Associate a sound to be played when the value is modified. The sound is
	 * played irrespective of whether an <code>HAdjustmentEvent</code> is sent
	 * to one or more listeners.
	 * @param sound the sound to be played, when the value is modified. If sound
	 *            content is already set, the original content is replaced. To
	 *            remove the sound specify a null {@linkorg.havi.ui.HSound}.
	 */
    public void setAdjustmentSound(HSound sound);

    /**
	 * Get the sound to be played when the value changes. <code>null</code>
	 * shall be returned if this method is called before its corresponding set
	 * method.
	 * @return The sound played when the value changes
	 */
    public HSound getAdjustmentSound();
}
