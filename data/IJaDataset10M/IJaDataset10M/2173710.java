package org.geotools.referencing.operation.projection;

import java.awt.geom.Point2D;
import java.util.Collection;
import javax.units.NonSI;
import javax.units.SI;
import javax.units.Unit;
import org.geotools.metadata.iso.citation.CitationImpl;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.referencing.operation.projection.IdrPolyconic.Provider;
import org.geotools.resources.cts.ResourceKeys;
import org.geotools.resources.cts.Resources;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.operation.CylindricalProjection;
import org.opengis.referencing.operation.MathTransform;

/**
 * Oblique Mercator Projection. A conformal, oblique, cylindrical projection 
 * with the cylinder touching the ellipsoid (or sphere) along a great circle 
 * path (the central line). The Mercator and Transverse Mercator projections 
 * can be thought of as special cases of the oblique mercator, where the central 
 * line is along the equator or a meridian, respectively. The Oblique Mercator 
 * projection has been used in Switzerland, Hungary, Madagascar, 
 * Malaysia, Borneo and the panhandle of Alaska.
 * <br><br>
 * 
 * The Oblique Mercator projection uses a (U,V) coordinate system, with the 
 * U axis along the central line. During the forward projection, coordinates 
 * from the ellipsoid are projected conformally to a sphere of constant total 
 * curvature, called the 'aposphere', before being projected onto the plane. 
 * The projection coordinates are further convented to a (X,Y) coordinate system 
 * by rotating the calculated (u,v) coordinates to give output (x,y) coordinates. 
 * The rotation value is usually the same as the projection azimuth (the angle, 
 * east of north, of the central line), but some cases allow a separate 
 * rotation parameter. 
 * <br><br>
 * 
 * There are two forms of the oblique mercator, differing in the origin of
 * their grid coordinates. The Hotine_Oblique_Mercator (EPSG code 9812) has grid 
 * coordinates start at the intersection of the central line and the equator of the 
 * aposphere. The Oblique_Mercator (EPSG code 9815) is the same, except the grid 
 * coordinates begin at the central point (where the latitude of center and 
 * central line intersect). ESRI separates these two case by appending
 * "Natural_Origin" (for the Hotine_Oblique_Mercator) and "Center" 
 * (for the Obique_Mercator) to the projection names.
 * <br><br>
 * 
 * Two different methods are used to specify the central line for the 
 * oblique mercator: 1) a central point and an azimuth, 
 * east of north, describing the central line and 
 * 2) two points on the central line. The EPSG does not use the two point method, 
 * while ESRI separates the two cases by putting "Azimuth" and "Two_Point" in 
 * their projection names. Both cases use the point where the "latitude_of_center" 
 * parameter crosses the central line as the projection's central point. 
 * The central meridian is not a projection parameter, and is instead calculated 
 * as the intersection between the central line and the equator of the aposphere. 
 * <br><br>
 *
 * For the azimuth method, the central latitude cannot be +- 90.0 degrees
 * and the central line cannot be at a maximum or minimum latitude at the central point.
 * In the two point method, the latitude of the first and second points cannot be
 * equal. Also, the latitude of the first point and central point cannot be
 * +- 90.0 degrees. Furthermore, the latitude of the first point cannot be 0.0 and 
 * the latitude of the second point cannot be - 90.0 degrees. A change of 
 * 10^-7 radians can allow calculation at these special cases. Snyder's restriction
 * of the central latitude being 0.0 has been removed, since the equaitons appear
 * to work correctly in this case.
 * <br><br>
 *
 * Azimuth values of 0.0 and +- 90.0 degrees are allowed (and used in Hungary
 * and Switzerland), though these cases would usually use a Mercator or 
 * Transverse Mercator projection instead. Azimuth values > 90 degrees cause
 * errors in the equations.
 * <br><br>
 * 
 * The oblique mercator is also called the "Rectified Skew Orthomorphic" (RSO). 
 * It appears is that the only difference from the oblique mercator is that
 * the RSO allows the rotation from the (U,V) to (X,Y) coordinate system to be different
 * from the azimuth. This separate parameter is called "rectified_grid_angle" (or 
 * "XY_Plane_Rotation" by ESRI) and is also included in the EPSG's parameters
 * for the Oblique Mercator and Hotine Oblique Mercator. 
 * The rotation parameter is optional in all the non-two point projections and will be
 * set to the azimuth if not specified.
 * <br><br>
 * 
 * Projection cases and aliases implemented by the {@link IdrLabordeMadagascar} are:
 * <ul>
 * <li>Oblique_Mercator (EPSG code 9815) - grid coordinates begin at the central point, has "rectified_grid_angle" parameter.</li>
 * <li>Hotine_Oblique_Mercator_Azimuth_Center (ESRI) - grid coordinates begin at the central point.</li>
 * <li>Rectified_Skew_Orthomorphic_Center (ESRI) - grid coordinates begin at the central point, has "rectified_grid_angle" parameter.</li>
 * 
 * <li>Hotine_Oblique_Mercator (EPSG code 9812) - grid coordinates begin at the interseciton of the central line and aposphere equator, has "rectified_grid_angle" parameter.</li>
 * <li>Hotine_Oblique_Mercator_Azimuth_Natural_Origin (ESRI) - grid coordinates begin at the interseciton of the central line and aposphere equator.</li>
 * <li>Rectified_Skew_Orthomorphic_Natural_Origin (ESRI) - grid coordinates begin at the interseciton of the central line and aposphere equator, has "rectified_grid_angle" parameter.</li>
 * 
 * <li>Hotine_Oblique_Mercator_Two_Point_Center (ESRI) - grid coordinates begin at the central point.</li>
 * <li>Hotine_Oblique_Mercator_Two_Point_Natural_Origin (ESRI) - grid coordinates begin at the interseciton of the central line and aposphere equator.</li>
 * </ul>
 * 
 * <strong>References:</strong>
 * <ul>
 *   <li><code>libproj4</code> is available at
 *       <A HREF="http://members.bellatlantic.net/~vze2hc4d/proj4/">libproj4 Miscellanea</A><br>
 *        Relevent files are: <code>PJ_omerc.c</code>, <code>pj_tsfn.c</code>,
 *        <code>pj_fwd.c</code>, <code>pj_inv.c</code> and <code>lib_proj.h</code></li>
 *   <li> John P. Snyder (Map Projections - A Working Manual,
 *        U.S. Geological Survey Professional Paper 1395, 1987)</li>
 *   <li> "Coordinate Conversions and Transformations including Formulas",
 *        EPSG Guidence Note Number 7 part 2, Version 24.</li>
 *   <li>Gerald Evenden, 2004, <a href="http://members.verizon.net/~vze2hc4d/proj4/omerc.pdf">
 *         Documentation of revised Oblique Mercator</a></li>
 * </ul>
 * 
 * @see <A HREF="http://mathworld.wolfram.com/MercatorProjection.html">Oblique Mercator projection on MathWorld</A>
 * @see <A HREF="http://www.remotesensing.org/geotiff/proj_list/hotine_oblique_mercator.html">hotine_oblique_mercator on Remote Sensing</A>
 * @see <A HREF="http://www.remotesensing.org/geotiff/proj_list/oblique_mercator.html">oblique_mercator on Remote Sensing</A>
 *
 * @version $Id: IdrLabordeMadagascar.java 12357 2007-06-27 09:05:41Z dguerrero $
 * @author  Rueben Schulz
 */
public class IdrLabordeMadagascar extends MapProjection {

    private final double scaleFactorLocal = 0.9995;

    private final double falseEastingLocal = 400000.0;

    private final double falseNorthingLocal = 800000.0;

    /**
     * Latitude of the projection centre. This is similar to the 
     * {@link #latitudeOfOrigin}, but the latitude of origin is the
     * Earth equator on aposphere for the oblique mercator. Needed
     * for WKT.
     */
    private final double latitudeOfCentre = -18.9;

    /**
     * Longitude of the projection centre. This is <strong>NOT</strong> equal
     * to the {@link #centralMeridian}, which is the meridian where the
     * central line intersects the Earth equator on aposphere. Needed for
     * for non-two point WKT.
     */
    private final double longitudeOfCentre = 46.4372291700;

    /**
     * The azimuth of the central line passing throught the centre of the
     * projection, needed for for non-two point WKT. 
     */
    private double azimuth = 18.9;

    protected IdrLabordeMadagascar(ParameterValueGroup parameters) throws ParameterNotFoundException {
        super(parameters);
    }

    public ParameterDescriptorGroup getParameterDescriptors() {
        return Provider.PARAMETERS;
    }

    public ParameterValueGroup getParameterValues() {
        final ParameterValueGroup values = super.getParameterValues();
        return values;
    }

    protected Point2D inverseTransformNormalized(double x, double y, Point2D ptDst) throws ProjectionException {
        return null;
    }

    protected Point2D transformNormalized(double x, double y, Point2D ptDst) throws ProjectionException {
        return null;
    }

    public static class Provider extends AbstractProvider {

        public static final ParameterDescriptor SCALE_FACTOR_LOCAL = createDescriptor(new NamedIdentifier[] { new NamedIdentifier(CitationImpl.OGC, "scale_factor"), new NamedIdentifier(CitationImpl.EPSG, "Scale factor on initial line"), new NamedIdentifier(CitationImpl.EPSG, "Scale factor at natural origin"), new NamedIdentifier(CitationImpl.GEOTIFF, "ScaleAtNatOrigin"), new NamedIdentifier(CitationImpl.GEOTIFF, "ScaleAtCenter") }, 0.9995, 0, Double.POSITIVE_INFINITY, Unit.ONE);

        public static final ParameterDescriptor FALSE_EASTING_LOCAL = createDescriptor(new NamedIdentifier[] { new NamedIdentifier(CitationImpl.OGC, "false_easting"), new NamedIdentifier(CitationImpl.EPSG, "False easting"), new NamedIdentifier(CitationImpl.EPSG, "Easting at projection centre"), new NamedIdentifier(CitationImpl.EPSG, "Easting at false origin"), new NamedIdentifier(CitationImpl.GEOTIFF, "FalseEasting") }, 400000.0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, SI.METER);

        public static final ParameterDescriptor FALSE_NORTHING_LOCAL = createDescriptor(new NamedIdentifier[] { new NamedIdentifier(CitationImpl.OGC, "false_northing"), new NamedIdentifier(CitationImpl.EPSG, "False northing"), new NamedIdentifier(CitationImpl.EPSG, "Northing at projection centre"), new NamedIdentifier(CitationImpl.EPSG, "Northing at false origin"), new NamedIdentifier(CitationImpl.GEOTIFF, "FalseNorthing") }, 800000.0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, SI.METER);

        /**
         * The operation parameter descriptor for the {@link #latitudeOfCentre}
         * parameter value. Valid values range is from -90 to 90. Default value is 0.
         */
        public static final ParameterDescriptor LAT_OF_CENTRE = createDescriptor(new NamedIdentifier[] { new NamedIdentifier(CitationImpl.OGC, "latitude_of_center"), new NamedIdentifier(CitationImpl.EPSG, "Latitude of projection centre"), new NamedIdentifier(CitationImpl.ESRI, "Latitude_Of_Center"), new NamedIdentifier(CitationImpl.GEOTIFF, "CenterLat") }, -18.9, -90, 90, NonSI.DEGREE_ANGLE);

        /**
         * The operation parameter descriptor for the {@link #longitudeOfCentre}
         * parameter value. Valid values range is from -180 to 180. Default value is 0.
         */
        public static final ParameterDescriptor LONG_OF_CENTRE = createDescriptor(new NamedIdentifier[] { new NamedIdentifier(CitationImpl.OGC, "longitude_of_center"), new NamedIdentifier(CitationImpl.EPSG, "Longitude of projection centre"), new NamedIdentifier(CitationImpl.ESRI, "Longitude_Of_Center"), new NamedIdentifier(CitationImpl.GEOTIFF, "CenterLong") }, 46.4372291700, -180, 180, NonSI.DEGREE_ANGLE);

        /**
         * The operation parameter descriptor for the {@link #alpha_c}
         * parameter value. Valid values range is from -360 to -270, -90 to 90, 
         * and 270 to 360 degrees. Default value is 0.
         */
        public static final ParameterDescriptor AZIMUTH = createDescriptor(new NamedIdentifier[] { new NamedIdentifier(CitationImpl.OGC, "azimuth"), new NamedIdentifier(CitationImpl.ESRI, "Azimuth"), new NamedIdentifier(CitationImpl.EPSG, "Azimuth of initial line"), new NamedIdentifier(CitationImpl.GEOTIFF, "AzimuthAngle") }, 18.9, -360, 360, NonSI.DEGREE_ANGLE);

        /**
         * The parameters group.
         */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(new NamedIdentifier[] { new NamedIdentifier(CitationImpl.OGC, "Laborde Madagascar"), new NamedIdentifier(CitationImpl.EPSG, "Laborde Madagascar"), new NamedIdentifier(CitationImpl.EPSG, "Laborde_Madagascar"), new NamedIdentifier(CitationImpl.EPSG, "9813"), new NamedIdentifier(CitationImpl.GEOTOOLS, Resources.formatInternational(ResourceKeys.OBLIQUE_MERCATOR_PROJECTION)), new NamedIdentifier(new CitationImpl("IDR"), "IDR") }, new ParameterDescriptor[] { SEMI_MAJOR, SEMI_MINOR, LONG_OF_CENTRE, LAT_OF_CENTRE, AZIMUTH, SCALE_FACTOR_LOCAL, FALSE_EASTING_LOCAL, FALSE_NORTHING_LOCAL });

        /**
         * Constructs a new provider. 
         */
        public Provider() {
            super(PARAMETERS);
        }

        /**
         * Constructs a new provider. 
         */
        protected Provider(final ParameterDescriptorGroup params) {
            super(params);
        }

        /**
         * Returns the operation type for this map projection.
         */
        protected Class getOperationType() {
            return CylindricalProjection.class;
        }

        /**
         * Creates a transform from the specified group of parameter values.
         *
         * @param  parameters The group of parameter values.
         * @return The created math transform.
         * @throws ParameterNotFoundException if a required parameter was not found.
         */
        public MathTransform createMathTransform(final ParameterValueGroup parameters) throws ParameterNotFoundException {
            return new IdrLabordeMadagascar(parameters);
        }
    }
}
