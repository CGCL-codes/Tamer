package org.geotools.referencefork.referencing.operation.builder;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.vecmath.MismatchedSizeException;
import org.geotools.referencefork.geometry.GeneralDirectPosition;
import org.geotools.referencefork.math.Statistics;
import org.gvsig.referencing.ReferencingUtil;
import org.opengis.metadata.Identifier;
import org.opengis.metadata.extent.Extent;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.DerivedCRS;
import org.opengis.referencing.crs.EngineeringCRS;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.crs.ImageCRS;
import org.opengis.referencing.crs.ProjectedCRS;
import org.opengis.referencing.cs.CartesianCS;
import org.opengis.referencing.cs.CoordinateSystem;
import org.opengis.referencing.operation.Conversion;
import org.opengis.referencing.operation.CoordinateOperationFactory;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.OperationMethod;
import org.opengis.referencing.operation.TransformException;
import org.opengis.referencing.operation.Transformation;
import org.opengis.spatialschema.geometry.DirectPosition;
import org.opengis.spatialschema.geometry.MismatchedDimensionException;
import org.opengis.spatialschema.geometry.MismatchedReferenceSystemException;
import org.opengis.util.InternationalString;

/**
 * Provides a basic implementation for {@linkplain MathTransform math transform}
 * builders.
 * 
 * Math transform builders create {@link MathTransform} objects for transforming 
 * coordinates from a source CRS 
 * ({@linkplain CoordinateReferenceSystem Coordinate Reference System}) to
 * a target CRS using empirical parameters. Usually, one of those CRS is a
 * {@linkplain GeographicCRS geographic} or {@linkplain ProjectedCRS projected}
 * one with a well known relationship to the earth. The other CRS is often an
 * {@linkplain EngineeringCRS engineering} or {@linkplain ImageCRS image} one
 * tied to some ship. For example a remote sensing image <em>before</em>
 * georectification may be referenced by an {@linkplain ImageCRS image CRS}.
 *
 * <blockquote><p><font size=-1><strong>Design note:</strong>
 * It is technically possible to reference such remote sensing images with a
 * {@linkplain DerivedCRS CRS derived} from the geographic or projected CRS,
 * where the {@linkplain DerivedCRS#getConversionFromBase conversion from base}
 * is the math transform {@linkplain #getMathTransform computed by this builder}.
 * Such approach is advantageous for {@linkplain CoordinateOperationFactory
 * coordinate operation factory} implementations, since they can determine the
 * operation just by inspection of the {@link DerivedCRS} instance. However this
 * is conceptually incorrect since {@link DerivedCRS} can be related to an other
 * CRS only through {@linkplain Conversion conversions}, which by definition are
 * accurate up to rounding errors. The operations created by math transform
 * builders are rather {@linkplain Transformation transformations}, which can't
 * be used for {@link DerivedCRS} creation.
 * </font></p></blockquote>
 *
 * The math transform from {@linkplain #getSourceCRS source CRS} to {@linkplain
 * #getTargetCRS target CRS} is calculated by {@code MathTransformBuilder} from
 * a set of {@linkplain #getMappedPositions mapped positions} in both CRS.
 * <p>
 * Subclasses must implement at least the {@link #getMinimumPointCount()} and
 * {@link #computeMathTransform()} methods.
 *
 * @since 2.4
 * @source $URL: http://svn.geotools.org/geotools/trunk/gt/modules/library/referencing/src/main/java/org/geotools/referencing/operation/builder/MathTransformBuilder.java $
 * @version $Id: MathTransformBuilder.java 28984 2008-01-28 18:08:29Z acuster $
 * @author Jan Jezek
 * @author Martin Desruisseaux
 */
public abstract class MathTransformBuilder {

    /**
     * The list of mapped positions.
     */
    private final List<MappedPosition> positions = new ArrayList<MappedPosition>();

    /**
     * An unmodifiable view of mapped positions to be returned by {@link #getMappedPositions}.
     */
    private final List<MappedPosition> unmodifiablePositions = Collections.unmodifiableList(positions);

    /**
     * Coordinate Reference System of the source and target points,
     * or {@code null} if unknown.
     */
    private CoordinateReferenceSystem sourceCRS, targetCRS;

    /**
     * The math transform. Will be computed only when first needed.
     */
    private transient MathTransform transform;

    /**
     * The transformation. Will be computed only when first needed.
     */
    private transient Transformation transformation;

    /**
     * Creates a builder with the default factories.
     */
    public MathTransformBuilder() {
    }

    /**
     * Returns the name for the {@linkplain #getTransformation transformation} to
     * be created by this builder.
     */
    public String getName() {
        return this.toString();
    }

    /**
     * Returns the minimum number of points required by this builder. This minimum depends on the
     * algorithm used. For example {@linkplain AffineTransformBuilder affine transform builders}
     * require at least 3 points, while {@linkplain SimilarTransformBuilder similar transform
     * builders} requires only 2 points.
     */
    public abstract int getMinimumPointCount();

    /**
     * Returns the dimension for {@linkplain #getSourceCRS source} and
     * {@link #getTargetCRS target} CRS. The default value is 2.
     */
    public int getDimension() {
        return 2;
    }

    /**
     * Returns the list of mapped positions.
     */
    public List<MappedPosition> getMappedPositions() {
        return unmodifiablePositions;
    }

    /**
     * Set the list of mapped positions.
     *
     * @throws MismatchedSizeException if the list doesn't have the expected number of points.
     * @throws MismatchedDimensionException if some points doesn't have the
     *         {@linkplain #getDimension expected number of dimensions}.
     * @throws MismatchedReferenceSystemException if CRS is not the same for all points.
     */
    public void setMappedPositions(final List<MappedPosition> positions) throws MismatchedSizeException, MismatchedDimensionException, MismatchedReferenceSystemException {
        final CoordinateReferenceSystem source, target;
        source = ensureValid(getPoints(positions, false), "sourcePoints");
        target = ensureValid(getPoints(positions, true), "targetPoints");
        this.positions.clear();
        this.positions.addAll(positions);
        this.sourceCRS = source;
        this.targetCRS = target;
        this.transform = null;
    }

    /**
     * Extracts the source or target points from the specified list.
     *
     * @param positions The array where to take points from.
     * @param target {@code false} for extracting source points,
     *        or {@code true} for extracting target points.
     */
    private static DirectPosition[] getPoints(List<MappedPosition> positions, boolean target) {
        final DirectPosition[] points = new DirectPosition[positions.size()];
        for (int i = 0; i < points.length; i++) {
            final MappedPosition mp = (MappedPosition) positions.get(i);
            points[i] = target ? mp.getTarget() : mp.getSource();
        }
        return points;
    }

    /**
     * Set the source or target points. Note: {@link #sourceCRS} or {@link #targetCRS} must be
     * setup appropriately before this method is invoked.
     *
     * @param points The new points to use.
     * @param target {@code false} for setting the source points,
     *        or {@code true} for setting the target points.
     *
     * @throws MismatchedSizeException if the array doesn't have the expected number of points.
     */
    private void setPoints(final DirectPosition[] points, final boolean target) throws MismatchedSizeException {
        transform = null;
        final boolean add = positions.isEmpty();
        if (!add && points.length != positions.size()) {
            throw new MismatchedSizeException("mismatched size exception");
        }
        final int dimension = getDimension();
        for (int i = 0; i < points.length; i++) {
            final MappedPosition mp;
            if (add) {
                mp = new MappedPosition(dimension);
                positions.add(mp);
            } else {
                mp = positions.get(i);
            }
            final DirectPosition point = points[i];
            if (target) {
                mp.setTarget(point);
            } else {
                mp.setSource(point);
            }
        }
    }

    /**
     * Returns the source points. This convenience method extracts those points from
     * the {@linkplain #getMappedPositions mapped positions}.
     */
    public DirectPosition[] getSourcePoints() {
        final DirectPosition[] points = getPoints(getMappedPositions(), false);
        assert ensureValid(points, "sourcePoints", sourceCRS);
        return points;
    }

    /**
     * Convenience method setting the {@linkplain MappedPosition#getSource source points}
     * in mapped positions.
     *
     * @param  points The source points.
     * @throws MismatchedSizeException if the list doesn't have the expected number of points.
     * @throws MismatchedDimensionException if some points doesn't have the
     *         {@linkplain #getDimension expected number of dimensions}.
     * @throws MismatchedReferenceSystemException if CRS is not the same for all points.
     */
    public void setSourcePoints(final DirectPosition[] points) throws MismatchedSizeException, MismatchedDimensionException, MismatchedReferenceSystemException {
        sourceCRS = ensureValid(points, "sourcePoints");
        setPoints(points, false);
    }

    /**
     * Returns the target points. This convenience method extracts those points from
     * the {@linkplain #getMappedPositions mapped positions}.
     */
    public DirectPosition[] getTargetPoints() {
        final DirectPosition[] points = getPoints(getMappedPositions(), true);
        assert ensureValid(points, "targetPoints", targetCRS);
        return points;
    }

    /**
     * Convenience method setting the {@linkplain MappedPosition#getTarget target points}
     * in mapped positions.
     *
     * @param  points The target points.
     * @throws MismatchedSizeException if the list doesn't have the expected number of points.
     * @throws MismatchedDimensionException if some points doesn't have the
     *         {@linkplain #getDimension expected number of dimensions}.
     * @throws MismatchedReferenceSystemException if CRS is not the same for all points.
     */
    public void setTargetPoints(final DirectPosition[] points) throws MismatchedSizeException, MismatchedDimensionException, MismatchedReferenceSystemException {
        targetCRS = ensureValid(points, "targetPoints");
        setPoints(points, true);
    }

    /**
     * Prints a table of all source and target points stored in this builder.
     *
     * @param  out The output device where to print all points.
     * @param  locale The locale, or {@code null} for the default.
     * @throws IOException if an error occured while printing.
     *
     * @todo Insert a double-line column separator between the source and target points.
     */
    public void printPoints(final Writer out, Locale locale) throws IOException {
    }

    /**
     * Returns the coordinate reference system for the {@link #getSourcePoints source points}.
     * This method determines the CRS as below:
     * <p>
     * <ul>
     *   <li>If at least one source points has a CRS, then this CRS is selected
     *       as the source one and returned.</li>
     *   <li>If no source point has a CRS, then this method creates an
     *       {@linkplain EngineeringCRS engineering CRS} using the same
     *       {@linkplain CoordinateSystem coordinate system} than the one used
     *       by the {@linkplain #getTargetCRS target CRS}.</li>
     * </ul>
     *
     * @throws FactoryException if the CRS can't be created.
     */
    public CoordinateReferenceSystem getSourceCRS() throws FactoryException {
        if (sourceCRS == null) {
            sourceCRS = createEngineeringCRS(false);
        }
        assert sourceCRS.getCoordinateSystem().getDimension() == getDimension();
        return sourceCRS;
    }

    /**
     * Returns the coordinate reference system for the {@link #getTargetPoints target points}.
     * This method determines the CRS as below:
     * <p>
     * <ul>
     *   <li>If at least one target points has a CRS, then this CRS is selected
     *       as the target one and returned.</li>
     *   <li>If no target point has a CRS, then this method creates an
     *       {@linkplain EngineeringCRS engineering CRS} using the same
     *       {@linkplain CoordinateSystem coordinate system} than the one used
     *       by the {@linkplain #getSourceCRS source CRS}.</li>
     * </ul>
     *
     * @throws FactoryException if the CRS can't be created.
     */
    public CoordinateReferenceSystem getTargetCRS() throws FactoryException {
        if (targetCRS == null) {
            targetCRS = createEngineeringCRS(true);
        }
        assert targetCRS.getCoordinateSystem().getDimension() == getDimension();
        return targetCRS;
    }

    /**
     * Creates an engineering CRS using the same {@linkplain CoordinateSystem
     * coordinate system} than the existing CRS, and an area of validity
     * determined from the specified points. This method is used for creating
     * a {@linkplain #getTargetCRS target CRS} from the
     * {@linkplain #getSourceCRS source CRS}, or conversely.
     *
     * @param target {@code false} for creating the source CRS, or
     *        or {@code true} for creating the target CRS.
     * @throws FactoryException if the CRS can't be created.
     */
    private EngineeringCRS createEngineeringCRS(final boolean target) throws FactoryException {
        throw new FactoryException("Not implemented yet");
    }

    /**
     * Returns the CRS of the specified point. If the CRS of the previous point is known,
     * it can be specified. This method will then ensure that the two CRS are compatibles.
     */
    private static CoordinateReferenceSystem getCoordinateReferenceSystem(final DirectPosition point, CoordinateReferenceSystem previousCRS) throws MismatchedReferenceSystemException {
        final CoordinateReferenceSystem candidate = point.getCoordinateReferenceSystem();
        if (candidate != null) {
            if (previousCRS == null) {
                return candidate;
            }
            if (!previousCRS.equals(candidate)) {
                throw new MismatchedReferenceSystemException("mismatched reference system");
            }
        }
        return previousCRS;
    }

    /**
     * Returns the required coordinate system type. The default implementation returns
     * {@code CoordinateSystem.class}, which means that every kind of coordinate system
     * is legal. Some subclasses will restrict to {@linkplain CartesianCS cartesian CS}.
     */
    public Class<? extends CoordinateSystem> getCoordinateSystemType() {
        return CoordinateSystem.class;
    }

    /**
     * Ensures that the specified list of points is valid, and returns their CRS.
     *
     * @param points The points to check.
     * @param label  The argument name, used for formatting error message only.
     *
     * @throws MismatchedSizeException if the list doesn't have the expected number of points.
     * @throws MismatchedDimensionException if some points doesn't have the
     *         {@linkplain #getDimension expected number of dimensions}.
     * @throws MismatchedReferenceSystemException if CRS is not the same for all points.
     * @return The CRS used for the specified points, or {@code null} if unknown.
     */
    private CoordinateReferenceSystem ensureValid(final DirectPosition[] points, final String label) throws MismatchedSizeException, MismatchedDimensionException, MismatchedReferenceSystemException {
        final int necessaryNumber = getMinimumPointCount();
        if (points.length < necessaryNumber) {
            throw new MismatchedSizeException("mismatched dimension");
        }
        CoordinateReferenceSystem crs = null;
        final int dimension = getDimension();
        for (int i = 0; i < points.length; i++) {
            DirectPosition point = points[i];
            point = ReferencingUtil.getInstance().truncateOrShrinkDimension(point, dimension);
            crs = getCoordinateReferenceSystem(point, crs);
        }
        if (crs != null) {
            final CoordinateSystem cs = crs.getCoordinateSystem();
            if (!getCoordinateSystemType().isAssignableFrom(cs.getClass())) {
                throw new MismatchedReferenceSystemException("unsupported coordinate system");
            }
        }
        return crs;
    }

    /**
     * Used for assertions only.
     */
    private boolean ensureValid(final DirectPosition[] points, final String label, final CoordinateReferenceSystem expected) {
        final CoordinateReferenceSystem actual = ensureValid(points, label);
        return actual == null || actual == expected;
    }

    /**
     * Returns statistics about the errors. The errors are computed as the distance between
     * {@linkplain #getSourcePoints source points} transformed by the math transform computed
     * by this {@code MathTransformBuilder}, and the {@linkplain #getTargetPoints target points}.
     * Use {@link Statistics#rms} for the <cite>Root Mean Squared error</cite>.
     *
     * @throws FactoryException If the math transform can't be created or used.
     */
    public Statistics getErrorStatistics() throws FactoryException {
        final MathTransform mt = getMathTransform();
        final Statistics stats = new Statistics();
        final DirectPosition buffer = new GeneralDirectPosition(getDimension());
        for (final Iterator<MappedPosition> it = getMappedPositions().iterator(); it.hasNext(); ) {
            final MappedPosition mp = (MappedPosition) it.next();
            final double error;
            try {
                error = mp.getError(mt, buffer);
            } catch (TransformException e) {
                throw new FactoryException("cant transform valid points", e);
            }
            stats.add(error);
        }
        return stats;
    }

    /**
     * Calculates the math transform immediately.
     *
     * @return Math transform from {@link #setMappedPositions MappedPosition}.
     * @throws FactoryException if the math transform can't be created.
     */
    protected abstract MathTransform computeMathTransform() throws FactoryException;

    /**
     * Returns the calculated math transform. This method {@linkplain #computeMathTransform the math
     * transform} the first time it is requested.
     *
     * @return Math transform from {@link #setMappedPositions MappedPosition}.
     * @throws FactoryException if the math transform can't be created.
     */
    public final MathTransform getMathTransform() throws FactoryException {
        if (transform == null) {
            transform = computeMathTransform();
        }
        return transform;
    }

    class SimpleTransform implements Transformation {

        CoordinateReferenceSystem source;

        CoordinateReferenceSystem target;

        MathTransform mathTransform;

        public String getOperationVersion() {
            return "";
        }

        public OperationMethod getMethod() {
            return null;
        }

        public ParameterValueGroup getParameterValues() {
            return null;
        }

        public MathTransform getMathTransform() {
            return null;
        }

        public Collection getPositionalAccuracy() {
            return null;
        }

        public InternationalString getScope() {
            return null;
        }

        public CoordinateReferenceSystem getSourceCRS() {
            return null;
        }

        public CoordinateReferenceSystem getTargetCRS() {
            return null;
        }

        public Extent getValidArea() {
            return null;
        }

        public Collection getAlias() {
            return null;
        }

        public Set getIdentifiers() {
            return null;
        }

        public Identifier getName() {
            return null;
        }

        public InternationalString getRemarks() {
            return null;
        }

        public String toWKT() throws UnsupportedOperationException {
            return "";
        }

        public SimpleTransform(CoordinateReferenceSystem source, CoordinateReferenceSystem target, MathTransform mathTransform) {
            this.source = source;
            this.target = target;
            this.mathTransform = mathTransform;
        }
    }

    /**
     * Returns the coordinate operation wrapping the {@linkplain #getMathTransform() calculated
     * math transform}. The {@linkplain Transformation#getPositionalAccuracy positional
     * accuracy} will be set to the Root Mean Square (RMS) of the differences between the
     * source points transformed to the target CRS, and the expected target points.
     */
    public Transformation getTransformation() throws FactoryException {
        if (transformation == null) {
            final Map<String, Object> properties = new HashMap<String, Object>();
            properties.put(Transformation.NAME_KEY, getName());
            final CoordinateReferenceSystem sourceCRS = getSourceCRS();
            final CoordinateReferenceSystem targetCRS = getTargetCRS();
            final double error = getErrorStatistics().rms();
            final MathTransform transform = getMathTransform();
        }
        return new SimpleTransform(sourceCRS, targetCRS, transform);
    }

    /**
     * Returns a string representation of this builder. The default implementation
     * returns a table containing all source and target points.
     */
    @Override
    public String toString() {
        final StringWriter out = new StringWriter();
        try {
            printPoints(out, null);
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        return out.toString();
    }
}
