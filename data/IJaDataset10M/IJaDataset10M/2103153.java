package org.addsimplicity.anicetus.entity;

/**
 * A state telemetry is a statement of a fact. Applications will use the state
 * telemetry to update the bus with facts that might be interesting for
 * monitoring or managing. For example, the number of progress of a long running
 * map/reduce could be reported periodically with state telemetry.
 * 
 * @author Dan Pritchett (driveawedge@yahoo.com)
 * 
 */
public class TelemetryState extends SubTypedInfo {

    static {
        EntityTypeRegistry.addClassShortName(TelemetryState.class, "ST");
    }

    /**
	 * Construct a state without a parent.
	 */
    public TelemetryState() {
        super();
    }

    /**
	 * Construct a state with the specified artifact as the parent.
	 * 
	 * @param parent
	 */
    public TelemetryState(GlobalInfo parent) {
        super(parent);
    }
}
