package org.dita2indesign.indesign.inx.model;

/**
 *
 */
public class InxGeometry extends InxValue {

    private Geometry geometry = null;

    /**
	 * @param geometry
	 */
    public InxGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    @Override
    public Object getValue() {
        return this.geometry;
    }

    @Override
    public String toEncodedString() {
        return InxHelper.encodeGeometry(this.geometry);
    }
}
