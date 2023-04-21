package org.genxdm.processor.w3c.xs.exception.scc;

import org.genxdm.exceptions.PreCondition;
import org.genxdm.xs.exceptions.ComponentConstraintException;
import org.genxdm.xs.exceptions.SchemaException;
import org.genxdm.xs.resolve.LocationInSchema;

/**
 * A Schema Component Constraint Exception wrapped in physical location information.
 */
@SuppressWarnings("serial")
public final class SccLocationException extends ComponentConstraintException {

    private final LocationInSchema m_location;

    public SccLocationException(final LocationInSchema location, final SchemaException cause) {
        super(PreCondition.assertArgumentNotNull(cause, "cause").getOutcome(), cause.getPartNumber(), cause);
        m_location = PreCondition.assertArgumentNotNull(location, "location");
    }

    public final LocationInSchema getLocation() {
        return m_location;
    }

    @Override
    public SchemaException getCause() {
        return (SchemaException) super.getCause();
    }
}
