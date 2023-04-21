package org.npsnet.v.services.configuration;

import org.w3c.dom.*;

/**
 * A physical geometry factory.
 *
 * @author Andrzej Kapolka
 */
public interface PhysicalGeometryFactory {

    /**
     * Creates a new <code>PhysicalGeometry</code> object based on the
     * serialized representation stored under the specified element.
     *
     * @param element the element from which to obtain the serialized
     * representation
     * @return the newly created physical geometry object
     * @exception InvalidPhysicalGeometryElementException if the specified physical
     * geometry element is invalid
     */
    public PhysicalGeometry newPhysicalGeometry(Element element) throws InvalidPhysicalGeometryElementException;
}
