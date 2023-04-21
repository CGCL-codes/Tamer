package org.geotools.referencefork.referencing.operation.transform;

import java.awt.geom.AffineTransform;
import java.io.Serializable;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.operation.Matrix;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.opengis.spatialschema.geometry.DirectPosition;
import org.geotools.referencefork.geometry.GeneralDirectPosition;
import org.geotools.referencefork.referencing.operation.matrix.MatrixFactory;

/**
 * The identity transform. The data are only copied without any transformation. This class is
 * used for identity transform of dimension greater than 2. For 1D and 2D identity transforms,
 * {@link LinearTransform1D} and {@link java.awt.geom.AffineTransform} already provide their
 * own optimisations.
 *
 * @since 2.0
 * @source $URL: http://svn.geotools.org/geotools/trunk/gt/modules/library/referencing/src/main/java/org/geotools/referencing/operation/transform/IdentityTransform.java $
 * @version $Id: IdentityTransform.java 29825 2008-04-07 15:22:58Z desruisseaux $
 * @author Martin Desruisseaux
 */
public class IdentityTransform extends AbstractMathTransform implements Serializable {

    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -5339040282922138164L;

    /**
     * The input and output dimension.
     */
    private final int dimension;

    /**
     * Identity transforms for dimensions ranging from to 0 to 7.
     * Elements in this array will be created only when first requested.
     */
    private static final MathTransform[] POOL = new MathTransform[8];

    /**
     * Constructs an identity transform of the specified dimension.
     */
    protected IdentityTransform(final int dimension) {
        this.dimension = dimension;
    }

    /**
     * Constructs an identity transform of the specified dimension.
     */
    public static synchronized MathTransform create(final int dimension) {
        MathTransform candidate;
        if (dimension < POOL.length) {
            candidate = POOL[dimension];
            if (candidate != null) {
                return candidate;
            }
        }
        switch(dimension) {
            case 1:
                candidate = LinearTransform1D.IDENTITY;
                break;
            case 2:
                candidate = new AffineTransform2D(new AffineTransform());
                break;
            default:
                candidate = new IdentityTransform(dimension);
                break;
        }
        if (dimension < POOL.length) {
            POOL[dimension] = candidate;
        }
        return candidate;
    }

    /**
     * Tests whether this transform does not move any points.
     * This implementation always returns {@code true}.
     */
    @Override
    public boolean isIdentity() {
        return true;
    }

    /**
     * Tests whether this transform does not move any points.
     * This implementation always returns {@code true}.
     */
    public boolean isIdentity(double tolerance) {
        return true;
    }

    /**
     * Gets the dimension of input points.
     */
    public int getSourceDimensions() {
        return dimension;
    }

    /**
     * Gets the dimension of output points.
     */
    public int getTargetDimensions() {
        return dimension;
    }

    /**
     * Returns a copy of the identity matrix.
     */
    public Matrix getMatrix() {
        return MatrixFactory.create(dimension + 1);
    }

    /**
     * Gets the derivative of this transform at a point. For an identity transform,
     * the derivative is the same everywhere.
     */
    @Override
    public Matrix derivative(final DirectPosition point) {
        return MatrixFactory.create(dimension);
    }

    /**
     * Copies the values from {@code ptSrc} to {@code ptDst}.
     * Overrides the super-class method for performance reason.
     *
     * @since 2.2
     */
    @Override
    public DirectPosition transform(final DirectPosition ptSrc, final DirectPosition ptDst) {
        if (ptSrc.getDimension() == dimension) {
            if (ptDst == null) {
                return new GeneralDirectPosition(ptSrc);
            }
            if (ptDst.getDimension() == dimension) {
                for (int i = 0; i < dimension; i++) {
                    ptDst.setOrdinate(i, ptSrc.getOrdinate(i));
                }
                return ptDst;
            }
        }
        try {
            return super.transform(ptSrc, ptDst);
        } catch (TransformException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Transforms an array of floating point coordinates by this transform.
     */
    @Override
    public void transform(final float[] srcPts, int srcOff, final float[] dstPts, int dstOff, int numPts) {
        System.arraycopy(srcPts, srcOff, dstPts, dstOff, numPts * dimension);
    }

    /**
     * Transforms an array of floating point coordinates by this transform.
     */
    public void transform(final double[] srcPts, int srcOff, final double[] dstPts, int dstOff, int numPts) {
        System.arraycopy(srcPts, srcOff, dstPts, dstOff, numPts * dimension);
    }

    /**
     * Returns the inverse transform of this object, which
     * is this transform itself
     */
    @Override
    public MathTransform inverse() {
        return this;
    }

    /**
     * Returns a hash value for this transform.
     * This value need not remain consistent between
     * different implementations of the same class.
     */
    @Override
    public int hashCode() {
        return (int) serialVersionUID + dimension;
    }

    /**
     * Compares the specified object with
     * this math transform for equality.
     */
    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (super.equals(object)) {
            final IdentityTransform that = (IdentityTransform) object;
            return this.dimension == that.dimension;
        }
        return false;
    }

    public int getDimSource() {
        return getSourceDimensions();
    }

    public int getDimTarget() {
        return getTargetDimensions();
    }

    public String toWKT() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
}
