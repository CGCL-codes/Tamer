package org.npsnet.v.configuration;

import org.npsnet.v.services.configuration.PhysicalGeometry;
import org.npsnet.v.services.configuration.UniformSpatialField;
import org.w3c.dom.*;

/**
 * The common uniform spatial field class.
 *
 * @author Andrzej Kapolka
 */
public class CommonUniformSpatialField extends UniformSpatialField {

    /**
     * The namespace URI of the common level field factory.
     */
    private static final String CLFF_NAMESPACE_URI = "resource:///org/npsnet/v/configuration/CommonLevelFieldFactory.class";

    /**
     * The uniform spatial element tag.
     */
    private static final String UNIFORM_SPATIAL = "clff:UniformSpatial";

    /**
     * The level attribute.
     */
    private static final String LEVEL = "level";

    /**
     * Constructor.  The geometry specified will be copied, not
     * referenced.  The level of the field within the bounding
     * geometry will be <code>1.0</code>.
     *
     * @param pGeometry the geometry that defines the boundaries
     * of the field
     */
    public CommonUniformSpatialField(PhysicalGeometry pGeometry) {
        super(pGeometry);
    }

    /**
     * Constructor.  The geometry specified will be copied, not
     * referenced.
     *
     * @param pGeometry the geometry that defines the boundaries
     * of the field
     * @param pLevel the level of the field within the bounding
     * geometry
     */
    public CommonUniformSpatialField(PhysicalGeometry pGeometry, double pLevel) {
        super(pGeometry, pLevel);
    }

    /**
     * Creates and returns a copy of this <code>PhysicalGeometry</code>
     * object.
     *
     * @return the newly created cloned copy
     */
    public Object clone() {
        return new CommonUniformSpatialField(geometry, level);
    }

    /**
     * Serializes this physical geometry under the specified element.
     *
     * @param element the element under which to place the serialized
     * representation
     */
    public void serialize(Element element) {
        Element e = element.getOwnerDocument().createElementNS(CLFF_NAMESPACE_URI, UNIFORM_SPATIAL);
        e.setAttribute(LEVEL, Double.toString(level));
        geometry.serialize(e);
        element.appendChild(e);
    }
}
