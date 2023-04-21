package org.opengis.test.metadata;

import org.opengis.metadata.*;
import org.opengis.metadata.extent.*;
import org.opengis.geometry.Geometry;
import org.opengis.test.ValidatorContainer;
import static org.opengis.test.Assert.*;

/**
 * Validates {@link Extent} and related objects from the
 * {@code org.opengis.metadata.extent} package.
 * <p>
 * This class is provided for users wanting to override the validation methods. When the default
 * behavior is sufficient, the {@link org.opengis.test.Validators} static methods provide a more
 * convenient way to validate various kinds of objects.
 *
 * @author  Martin Desruisseaux (Geomatys)
 * @version 3.1
 * @since   2.2
 */
public class ExtentValidator extends MetadataValidator {

    /**
     * Creates a new validator instance.
     *
     * @param container The set of validators to use for validating other kinds of objects
     *                  (see {@linkplain #container field javadoc}).
     */
    public ExtentValidator(final ValidatorContainer container) {
        super(container, "org.opengis.metadata.extent");
    }

    /**
     * For each interface implemented by the given object, invokes the corresponding
     * {@code validate(...)} method defined in this class (if any).
     *
     * @param  object The object to dispatch to {@code validate(...)} methods, or {@code null}.
     * @return Number of {@code validate(...)} methods invoked in this class for the given object.
     */
    public int dispatch(final GeographicExtent object) {
        int n = 0;
        if (object != null) {
            if (object instanceof GeographicDescription) {
                validate((GeographicDescription) object);
                n++;
            }
            if (object instanceof GeographicBoundingBox) {
                validate((GeographicBoundingBox) object);
                n++;
            }
            if (object instanceof BoundingPolygon) {
                validate((BoundingPolygon) object);
                n++;
            }
        }
        return n;
    }

    /**
     * Validates the geographic description.
     *
     * @param object The object to validate, or {@code null}.
     */
    public void validate(final GeographicDescription object) {
        if (object == null) {
            return;
        }
        final Identifier identifier = object.getGeographicIdentifier();
        mandatory("GeographicDescription: must have an identifier.", identifier);
    }

    /**
     * Validates the bounding polygon.
     *
     * @param object The object to validate, or {@code null}.
     *
     * @todo Not yet implemented.
     */
    public void validate(final BoundingPolygon object) {
        if (object == null) {
            return;
        }
        for (final Geometry e : toArray(Geometry.class, object.getPolygons())) {
        }
    }

    /**
     * Validates the geographic bounding box.
     *
     * @param object The object to validate, or {@code null}.
     */
    public void validate(final GeographicBoundingBox object) {
        if (object == null) {
            return;
        }
        final double west = object.getWestBoundLongitude();
        final double east = object.getEastBoundLongitude();
        final double south = object.getSouthBoundLatitude();
        final double north = object.getNorthBoundLatitude();
        assertBetween("GeographicBoundingBox: illegal west bound.", -180, +180, west);
        assertBetween("GeographicBoundingBox: illegal east bound.", -180, +180, east);
        assertBetween("GeographicBoundingBox: illegal south bound.", -90, +90, south);
        assertBetween("GeographicBoundingBox: illegal north bound.", -90, +90, north);
        assertTrue("GeographicBoundingBox: invalid range of longitudes.", west <= east);
        assertTrue("GeographicBoundingBox: invalid range of latitudes.", south <= north);
    }

    /**
     * Validates the vertical extent.
     *
     * @param object The object to validate, or {@code null}.
     */
    public void validate(final VerticalExtent object) {
        if (object == null) {
            return;
        }
        final Double minimum = object.getMinimumValue();
        final Double maximum = object.getMaximumValue();
        mandatory("VerticalExtent: must have a minimum value.", minimum);
        mandatory("VerticalExtent: must have a maximum value.", maximum);
        if (minimum != null && maximum != null) {
            assertTrue("VerticalExtent: invalid range.", minimum <= maximum);
        }
        container.validate(object.getVerticalCRS());
    }

    /**
     * Validates the temporal extent.
     *
     * @param object The object to validate, or {@code null}.
     *
     * @todo Validation of temporal primitives not yet implemented.
     */
    public void validate(final TemporalExtent object) {
        if (object == null) {
            return;
        }
        if (object instanceof SpatialTemporalExtent) {
            for (final GeographicExtent e : toArray(GeographicExtent.class, ((SpatialTemporalExtent) object).getSpatialExtent())) {
                dispatch(e);
            }
        }
    }

    /**
     * Validates the given extent.
     *
     * @param object The object to validate, or {@code null}.
     */
    public void validate(final Extent object) {
        if (object == null) {
            return;
        }
        validateOptional(object.getDescription());
        for (GeographicExtent e : toArray(GeographicExtent.class, object.getGeographicElements())) dispatch(e);
        for (VerticalExtent e : toArray(VerticalExtent.class, object.getVerticalElements())) validate(e);
        for (TemporalExtent e : toArray(TemporalExtent.class, object.getTemporalElements())) validate(e);
    }
}
