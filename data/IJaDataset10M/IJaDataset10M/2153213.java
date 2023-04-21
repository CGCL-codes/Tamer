package org.cresques.cts.gt2;

import org.geotools.cs.AxisInfo;
import org.geotools.cs.Projection;
import org.geotools.units.Unit;
import org.opengis.referencing.FactoryException;

/**
 * @author "Luis W. Sevilla" <sevilla_lui@gva.es>
 */
public class CSUTM extends CoordSys {

    public CSUTM(CSDatum datum, int zone) {
        super(datum);
        init(datum, zone, "N");
    }

    public CSUTM(CSDatum datum, int zone, String ns) {
        super(datum);
        init(datum, zone, ns);
    }

    public void init(CSDatum datum, int zone, String ns) {
        Unit linearUnit = Unit.METRE;
        javax.media.jai.ParameterList params = csFactory.createProjectionParameterList("Transverse_Mercator");
        params.setParameter("semi_major", datum.getDatum().getEllipsoid().getSemiMajorAxis());
        params.setParameter("semi_minor", datum.getDatum().getEllipsoid().getSemiMinorAxis());
        params.setParameter("central_meridian", (double) ((zone * 6) - 183));
        params.setParameter("latitude_of_origin", 0.0);
        params.setParameter("scale_factor", 0.9996);
        params.setParameter("false_easting", 500000.0);
        if (ns.toUpperCase().compareTo("S") == 0) params.setParameter("false_northing", 10000000.0); else params.setParameter("false_northing", 0.0);
        try {
            Projection projection = csFactory.createProjection("UTM" + zone, "Transverse_Mercator", params);
            projCS = csFactory.createProjectedCoordinateSystem(projection.getName().toString(), geogCS, projection, linearUnit, AxisInfo.X, AxisInfo.Y);
        } catch (FactoryException e) {
            e.printStackTrace();
        }
    }

    public double getScale(double minX, double maxX, double w, double dpi) {
        double scale = super.getScale(minX, maxX, w, dpi);
        if (projCS != null) {
            scale = ((maxX - minX) * (dpi / 2.54 * 100.0)) / w;
        }
        return scale;
    }

    public String toString() {
        return projCS.toString();
    }
}
