package org.npsnet.v.configuration;

import java.util.*;
import org.npsnet.v.services.configuration.CompositeGeometry;
import org.npsnet.v.services.configuration.PhysicalGeometry;
import org.w3c.dom.*;

/**
 * The common composite geometry class.
 *
 * @author Andrzej Kapolka
 */
public class CommonCompositeGeometry extends CompositeGeometry {

    /**
     * The namespace URI of the common physical geometry factory.
     */
    private static final String CPGF_NAMESPACE_URI = "resource:///org/npsnet/v/configuration/CommonPhysicalGeometryFactory.class";

    /**
     * The composite element.
     */
    private static final String COMPOSITE = "cpgf:Composite";

    /**
     * Creates a new <code>CommonCompositeGeometry</code> with no component
     * geometries.
     */
    public CommonCompositeGeometry() {
        super();
    }

    /**
     * Creates a new <code>CommonCompositeGeometry</code>.  The initial
     * geometries specified will be copied, not referenced.
     *
     * @param pComponentGeometries the initial collection of component
     * geometries
     */
    public CommonCompositeGeometry(Collection pComponentGeometries) {
        super(pComponentGeometries);
    }

    /**
     * Creates and returns a copy of this <code>PhysicalGeometry</code>
     * object.
     *
     * @return the newly created cloned copy
     */
    public Object clone() {
        return new CommonCompositeGeometry(componentGeometries);
    }

    /**
     * Serializes this physical geometry under the specified element.
     *
     * @param element the element under which to place the serialized
     * representation
     */
    public void serialize(Element element) {
        Element e = element.getOwnerDocument().createElementNS(CPGF_NAMESPACE_URI, COMPOSITE);
        Iterator it = getComponentGeometries();
        while (it.hasNext()) {
            ((PhysicalGeometry) it.next()).serialize(e);
        }
        element.appendChild(e);
    }
}
