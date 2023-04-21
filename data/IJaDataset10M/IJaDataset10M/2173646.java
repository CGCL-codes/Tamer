package dsb.bar.flowclient.monitor;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Flowmeter measurement result.
 * <p>
 * Instances are immutable.
 */
public class FlowclientMeasurement {

    /** The value of the A meter in mL. */
    private long valueA;

    /** The value of the B meter in mL. */
    private long valueB;

    /**
	 * Create a new {@link FlowclientMeasurement} instance.
	 * 
	 * @param valueA
	 *            The measurement of meter A in milliliters.
	 * @param valueB
	 *            The measurement of meter B in milliliters.
	 */
    public FlowclientMeasurement(long valueA, long valueB) {
        Validate.isTrue(valueA >= 0, "valueA should be >= 0");
        Validate.isTrue(valueB >= 0, "valueB should be >= 0");
        this.valueA = valueA;
        this.valueB = valueB;
    }

    /**
	 * Get the value of the measurement of meter A.
	 * 
	 * @return The value in milliliters.
	 */
    public long getValueA() {
        return this.valueA;
    }

    /**
	 * Get the value of the measurement of meter B.
	 * 
	 * @return The value in milliliters.
	 */
    public long getValueB() {
        return this.valueB;
    }

    /**
	 * Get a {@link String} representation of this {@link FlowclientMeasurement}
	 * instance.
	 * 
	 * @return A {@link String} containing a {@link ToStringBuilder}
	 *         representation of this instance.
	 */
    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
        builder.append("A", this.valueA);
        builder.append("B", this.valueB);
        return builder.toString();
    }
}
