package org.protune.core;

/**
 * The interface <tt>Status</tt> represents the state of a negotiation. A state should contain (at
 * least)
 * <ul>
 * <li>sent/received {@link org.protune.api.FilteredPolicy}</li>
 * <li>{@link org.protune.api.Notification} of performed actions</li>
 * <li>{@link org.protune.api.Check} testing whether the notifications are (not) reliable</li>
 * </ul>
 * The interface <tt>Status</tt> does not provide methods directly allowing the addition of
 * such entities to the state, since they are meant to be wrapped by a {@link
 * org.protune.core.NegotiationElement} object, therefore only the method {@link
 * #addNegotiationElement(NegotiationElement)} was provided.
 * @author jldecoi
 */
public interface Status {

    /**
	 * Returns the number of the current step of the negotiation.
	 * @return The number of the current step of the negotiation.
	 */
    public int getCurrentNegotiationStepNumber();

    /**
	 * Increases the number of the current step of the negotiation.
	 */
    public void increaseNegotiationStepNumber();

    /**
	 * Adds a new negotiation element to the state.
	 * @param ne The negotiation element to be added.
	 */
    public void addNegotiationElement(NegotiationElement ne);
}
