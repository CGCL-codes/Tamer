package org.geotools.referencefork.referencing.operation.transform;

import org.opengis.referencing.operation.Matrix;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.NoninvertibleTransformException;

/**
 * Projective transform in 2D case.
 *
 * @source $URL: http://svn.geotools.org/geotools/trunk/gt/modules/library/referencing/src/main/java/org/geotools/referencing/operation/transform/ProjectiveTransform2D.java $
 * @version $Id: ProjectiveTransform2D.java 28846 2008-01-20 20:30:06Z desruisseaux $
 * @author Jan Jezek
 */
final class ProjectiveTransform2D extends ProjectiveTransform implements MathTransform2D {

    /**
     * For cross-version compatibility.
     */
    private static final long serialVersionUID = -3101392684596817045L;

    /**
     * Creates projective transform from a matrix.
     */
    public ProjectiveTransform2D(final Matrix matrix) {
        super(matrix);
    }

    /**
     * Creates the inverse transform of this object.
     */
    @Override
    public MathTransform2D inverse() throws NoninvertibleTransformException {
        return (MathTransform2D) super.inverse();
    }

    /**
     * Creates an inverse transform using the specified matrix.
     */
    @Override
    MathTransform2D createInverse(final Matrix matrix) {
        return new ProjectiveTransform2D(matrix);
    }
}
