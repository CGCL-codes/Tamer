package be.kuleuven.cs.mop.domain.model;

import be.kuleuven.cs.mop.domain.model.impl.InvitationStatus;

/**
 * Represents a helper-invitation
 */
public interface Invitation {

    /**
	 * Returns the {@link InvitationStatus} of this {@link Invitation}
	 */
    public InvitationStatus getStatus();

    /**
	 * Returns the {@link Task} of this <code>Invitation</code>
	 */
    public Task getTask();

    /**
	 * Returns the {@link User} from this <code>Invitation</code>
	 */
    public User getUser();

    /**
	 * Determines whether or not this <code>Invitation</code> has been accepted
	 */
    public boolean isAccepted();

    /**
	 * Determines whether or not this <code>Invitation</code> has been rejepted
	 */
    public boolean isPending();

    /**
	 * Determines whether or not this <code>Invitation</code> is pending
	 */
    public boolean isRejected();
}
