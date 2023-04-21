package org.jpox.samples.jtsgeometry;

import com.vividsolutions.jts.geom.MultiPolygon;

public class SampleMultiPolygon {

    private long id;

    private String name;

    private MultiPolygon geom;

    public SampleMultiPolygon(long id, String name, MultiPolygon multiPolygon) {
        this.id = id;
        this.name = name;
        this.geom = multiPolygon;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public MultiPolygon getGeom() {
        return geom;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof SampleMultiPolygon)) return false;
        SampleMultiPolygon other = (SampleMultiPolygon) obj;
        if (!(id == other.id)) return false;
        if (!(name == null ? other.name == null : name.equals(other.name))) return false;
        geom.normalize();
        other.geom.normalize();
        return (geom == null ? other.geom == null : geom.equalsExact(other.geom));
    }

    public String toString() {
        return "id = " + id + " / name = " + name + " / geom = " + (geom == null ? "null" : geom.toString());
    }
}
