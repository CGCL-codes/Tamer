package geovista.projection;

import java.awt.geom.Point2D;
import geovista.projection.units.MapMath;
import geovista.projection.units.ProjectionException;

/**
 * The Orthographic Azimuthal or Globe map projection.
 */
public class OrthographicAzimuthalProjection extends AzimuthalProjection {

    public OrthographicAzimuthalProjection() {
        initialize();
    }

    @Override
    public Point2D.Double project(double lam, double phi, Point2D.Double xy) {
        double sinphi;
        double cosphi = Math.cos(phi);
        double coslam = Math.cos(lam);
        switch(mode) {
            case EQUATOR:
                xy.y = Math.sin(phi);
                break;
            case OBLIQUE:
                sinphi = Math.sin(phi);
                xy.y = cosphi0 * sinphi - sinphi0 * cosphi * coslam;
                break;
            case NORTH_POLE:
                coslam = -coslam;
            case SOUTH_POLE:
                xy.y = cosphi * coslam;
                break;
        }
        xy.x = cosphi * Math.sin(lam);
        return xy;
    }

    @Override
    public Point2D.Double projectInverse(double x, double y, Point2D.Double lp) {
        double rh, cosc, sinc;
        if ((sinc = (rh = MapMath.distance(x, y))) > 1.) {
            if ((sinc - 1.) > EPS10) {
                throw new ProjectionException();
            }
            sinc = 1.;
        }
        cosc = Math.sqrt(1. - sinc * sinc);
        if (Math.abs(rh) <= EPS10) {
            lp.y = projectionLatitude;
        } else {
            switch(mode) {
                case NORTH_POLE:
                    y = -y;
                    lp.y = Math.acos(sinc);
                    break;
                case SOUTH_POLE:
                    lp.y = -Math.acos(sinc);
                    break;
                case EQUATOR:
                    lp.y = y * sinc / rh;
                    x *= sinc;
                    y = cosc * rh;
                    if (Math.abs(lp.y) >= 1.) {
                        lp.y = lp.y < 0. ? -MapMath.HALFPI : MapMath.HALFPI;
                    } else {
                        lp.y = Math.asin(lp.y);
                    }
                    break;
                case OBLIQUE:
                    lp.y = cosc * sinphi0 + y * sinc * cosphi0 / rh;
                    y = (cosc - sinphi0 * lp.y) * rh;
                    x *= sinc * cosphi0;
                    if (Math.abs(lp.y) >= 1.) {
                        lp.y = lp.y < 0. ? -MapMath.HALFPI : MapMath.HALFPI;
                    } else {
                        lp.y = Math.asin(lp.y);
                    }
                    break;
            }
        }
        lp.x = (y == 0. && (mode == OBLIQUE || mode == EQUATOR)) ? (x == 0. ? 0. : x < 0. ? -MapMath.HALFPI : MapMath.HALFPI) : Math.atan2(x, y);
        return lp;
    }

    @Override
    public boolean hasInverse() {
        return true;
    }

    @Override
    public String toString() {
        return "Orthographic Azimuthal";
    }
}
