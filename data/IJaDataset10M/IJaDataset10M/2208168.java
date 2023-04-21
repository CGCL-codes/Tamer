package org.springframework.webflow.execution;

import org.springframework.core.enums.StaticLabeledEnum;

/**
 * Type-safe enumeration of possible flow session statuses. Consult the JavaDoc for the {@link FlowSession} for more
 * information on how these statuses are used during the life cycle of a flow session.
 * 
 * @see org.springframework.webflow.execution.FlowSession
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public class FlowSessionStatus extends StaticLabeledEnum {

    /**
	 * Initial status of a flow session; the session has been created but not yet activated.
	 */
    public static final FlowSessionStatus CREATED = new FlowSessionStatus(0, "Created");

    /**
	 * A flow session with STARTING status is about to enter its start state.
	 */
    public static final FlowSessionStatus STARTING = new FlowSessionStatus(1, "Starting");

    /**
	 * A flow session with ACTIVE status is currently executing.
	 */
    public static final FlowSessionStatus ACTIVE = new FlowSessionStatus(2, "Active");

    /**
	 * A flow session with PAUSED status is currently waiting on the user to signal an event.
	 */
    public static final FlowSessionStatus PAUSED = new FlowSessionStatus(3, "Paused");

    /**
	 * A flow session that is SUSPENDED is not actively executing a flow. It is waiting for subflow execution to
	 * complete before continuing.
	 */
    public static final FlowSessionStatus SUSPENDED = new FlowSessionStatus(4, "Suspended");

    /**
	 * A flow session that has ENDED is no longer actively executing a flow. This is the final status of a flow session.
	 */
    public static final FlowSessionStatus ENDED = new FlowSessionStatus(5, "Ended");

    /**
	 * Private constructor because this is a typesafe enum!
	 */
    private FlowSessionStatus(int code, String label) {
        super(code, label);
    }
}
