package org.opengis.observation.sampling;

import org.opengis.geometry.Geometry;
import org.opengis.metadata.extent.GeographicDescription;
import org.opengis.annotation.UML;
import static org.opengis.annotation.Specification.*;
import static org.opengis.annotation.Obligation.*;

/**
 * Observations may be associated with a geospatial location. The primary location of
 * interest is usually associated with the ultimate feature-of-interest, so this is a principle
 * classifier of an observation and its result, used in indexing and discovery.
 *
 * However, the location may not be trivially available. For example: in remote sensing
 * applications, a complex processing chain is required to geolocate the scene or swath; in
 * feature-detection applications the initial observation may be made on a scene, but the
 * detected entity, which is the ultimate feature of interest, occupies some location within it.
 * The distinction between the proximate and ultimate feature of interest is a key
 * consideration in these cases (see sub-clauses 6.3.1 and O&M-Part 2).
 * 
 * @version <A HREF="http://www.opengeospatial.org/standards/om">Implementation specification 1.0</A>
 * @author Open Geospatial Consortium
 * @author Guilhem Legal (Geomatys)
 * @since GeoAPI 2.3
 */
@UML(identifier = "Location", specification = OGC_07022)
public interface Location {

    /**
     * @return Geometry : location geometry
     */
    @UML(identifier = "geometryLocation", obligation = MANDATORY, specification = OGC_07022)
    Geometry getGeometryLocation();

    /**
     * @return GeographicDescription : named identified geographic area
     */
    @UML(identifier = "nameLocation", obligation = MANDATORY, specification = OGC_07022)
    GeographicDescription getNameLocation();
}
