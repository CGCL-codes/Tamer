package com.msli.graphic.input.pick;

import com.msli.core.state.StateSet;

/**
 * A PickSensor that interprets interactive inputs as a "click gesture". A click
 * gesture is generally triggered by a sequence of one or more button "clicks"
 * in combination with the locus (see LocusSensor). A click gesture is typically
 * used to produce a selection or mode change, possibly in combination with an
 * action initiation, such as a "double-click" to select and open a data item.
 * <P>
 * Gesture trigger conditions and hit geometry semantics are defined by the
 * concrete sensor implementation, which are often prescribed by the host
 * platform. Typically, a click gesture triggers when "over" a pick target and a
 * button/key combination is released after being satisfied. The gesture
 * typically resets when the click series ends. A click gesture also typically
 * incorporates an arming deadband, a tolerance for movement during the click
 * series, and a maximum inter-click period within a click series.
 * <P>
 * A ClickSensor produces the following Pick.PickerState sequence: IDLE,
 * STARTED, CONTINUING..., STOPPING. A CONTINUING event is generated each time
 * the click count changes, including the first click, with count 1.
 * <P>
 * Derived from gumbo.graphic.input.target.ClickSensor.
 * @author jonb
 * @param <THIS> The recursive type of this type.
 */
public interface ClickSensor<THIS extends ClickSensor<THIS>> extends PickSensor<THIS> {

    /**
	 * Gets the button flag used to initiate the click gesture. Remains
	 * unchanged during a given gesture cycle.
	 * @return Constant group of constant button flags. Never null.
	 */
    public StateSet<?> getClickButtons();

    /**
	 * Gets the click count for this event. Monotonically increasing during a
	 * given gesture cycle.
	 * @return The click count (>=0).
	 */
    public int getClickCount();
}
