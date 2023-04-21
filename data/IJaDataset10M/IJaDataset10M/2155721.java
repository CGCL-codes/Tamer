package org.jpox.samples.aogeometry;

import com.esri.arcgis.geometry.Ring;

public class SampleRing extends SampleGeometry {

    private Ring geom;

    public SampleRing(long id, String name, Ring ring) {
        this.id = id;
        this.name = name;
        this.geom = ring;
    }

    public Ring getGeom() {
        return geom;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof SampleRing)) return false;
        SampleRing other = (SampleRing) obj;
        if (!(id == other.getId())) return false;
        if (!(name == null ? other.getName() == null : name.equals(other.getName()))) return false;
        return geom.isEqual(other.getGeom());
    }

    public String toString() {
        return "id = " + id + " / name = " + name + " / geom = " + (geom == null ? "null" : geom.toString());
    }
}
