package playground.tnicolai.matsim4opus.gis;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.geotools.referencing.CRS;
import org.opengis.metadata.Identifier;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * Utility-class providing functionality related to coordinate reference
 * systems.
 * 
 * @author illenberger
 * 
 */
public class CRSUtils {

    private static Logger logger = Logger.getLogger(CRSUtils.class);

    private static final Map<Integer, CoordinateReferenceSystem> crsMappings = new HashMap<Integer, CoordinateReferenceSystem>();

    private static GeometryFactory geoFactory;

    /**
	 * Retrieves the coordinate reference system from the EPSG database.
	 * 
	 * @param srid
	 *            the spatial reference id.
	 * 
	 * @return a coordinate reference system.
	 */
    public static CoordinateReferenceSystem getCRS(int srid) {
        CoordinateReferenceSystem crs = crsMappings.get(srid);
        if (crs == null) {
            CRSAuthorityFactory factory = CRS.getAuthorityFactory(true);
            try {
                crs = factory.createCoordinateReferenceSystem("EPSG:" + srid);
            } catch (NoSuchAuthorityCodeException e) {
                logger.warn(e.getLocalizedMessage());
            } catch (FactoryException e) {
                e.printStackTrace();
            }
        }
        return crs;
    }

    /**
	 * Returns the spatial reference id for a given coordinate reference system.
	 * If the coordinate reference system has multiple identifiers one is
	 * randomly selected.
	 * 
	 * @param crs
	 *            a coordinate reference system.
	 * 
	 * @return the spatial reference id for the coordinate reference system or
	 *         <tt>0</tt> if the coordinate reference system has no identifiers.
	 */
    public static int getSRID(CoordinateReferenceSystem crs) {
        Identifier identifier = (Identifier) (crs.getIdentifiers().iterator().next());
        if (identifier == null) {
            return 0;
        } else {
            return Integer.parseInt(identifier.getCode());
        }
    }

    /**
	 * Determines the transformation from the coordinate reference system of
	 * <tt>source</tt> to the one of <tt>target</tt>.
	 * 
	 * @param source
	 *            a geometry.
	 * @param target
	 *            a geometry.
	 * @return a transformation or <tt>null</tt> if the transformation could not
	 *         be determined.
	 */
    public static MathTransform findTransform(Geometry source, Geometry target) {
        CoordinateReferenceSystem sourceCRS = getCRS(source.getSRID());
        CoordinateReferenceSystem targetCRS = getCRS(target.getSRID());
        try {
            return CRS.findMathTransform(sourceCRS, targetCRS);
        } catch (FactoryException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
	 * Creates a new point that is a transformed copy of the original point.
	 * 
	 * @param point
	 *            a the original point.
	 * @param transform
	 *            the transformation to be applied.
	 * @return a new transformed point.
	 */
    public static Point transformPoint(Point point, MathTransform transform) {
        if (geoFactory == null) geoFactory = new GeometryFactory();
        double[] points = new double[] { point.getCoordinate().x, point.getCoordinate().y };
        try {
            transform.transform(points, 0, points, 0, 1);
        } catch (TransformException e) {
            e.printStackTrace();
        }
        Point p = geoFactory.createPoint(new Coordinate(points[0], points[1]));
        return p;
    }
}
