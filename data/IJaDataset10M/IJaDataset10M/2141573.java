package net.seagis.swe;

import java.util.Collection;
import org.opengis.annotation.UML;
import static org.opengis.annotation.Specification.*;
import static org.opengis.annotation.Obligation.*;

/**
 * Implementation of ISO-11404 Record datatype that takes only simple scalars (i.e. no data aggregates).
 * SimpleDataRecord is a data-type so usually appears "by value" rather than by reference.
 *
 * @version $Id:
 * @author legal
 */
@UML(identifier = "SimpleDataRecord", specification = UNSPECIFIED)
public interface SimpleDataRecord extends AbstractDataRecord {

    /**
     * this field is restricted to AnyScalar value.
     */
    Collection<AnyScalarPropertyType> getField();
}
