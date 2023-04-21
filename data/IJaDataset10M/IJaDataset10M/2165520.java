package org.opengis.temporal;

import org.opengis.annotation.UML;
import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;

/**
 * A one-dimensional geometric primitive that represent extent in time.
 *
 * @author Stephane Fellah (Image Matters)
 * @author Alexander Petkov
 */
@UML(identifier = "TM_Period", specification = ISO_19108)
public interface Period extends TemporalGeometricPrimitive {

    /**
     * Links this period to the instant at which it starts.
     */
    @UML(identifier = "Beginning", obligation = MANDATORY, specification = ISO_19108)
    Instant getBeginning();

    /**
     * Links this period to the instant at which it ends.
     */
    @UML(identifier = "Ending", obligation = MANDATORY, specification = ISO_19108)
    Instant getEnding();
}
