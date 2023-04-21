package org.geonetwork.domain.gml311.geometrybasic0d1d;

/**
 * 
 * This is the abstract root type of the geometric primitives. A geometric primitive is a geometric 
 * object that is not decomposed further into other primitives in the system. All primitives are 
 * oriented in the direction implied by the sequence of their coordinate tuples.
 * 
 * @author heikki doeleman
 *
 */
public abstract class AbstractGeometricPrimitive extends AbstractGeometry {

    public boolean equals(Object o) {
        if (o instanceof AbstractGeometricPrimitive) {
            AbstractGeometry base = (AbstractGeometry) o;
            return super.equals(base);
        } else {
            return false;
        }
    }

    public int hashcode() {
        return super.hashCode();
    }
}
