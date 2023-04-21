package jp.go.ipa.jgcl;

import java.io.PrintWriter;
import org.magiclight.geometry.ITransformation2D;

/**
 * Transformation operator following ISO 10303-42
 * <p>
 * A Cartesian transformation operator defines a geometric transformation composed of
 * translation, rotation, mirroring and uniform scaling.
 * The list of normalized vectors u defines the columns of an orthogonal matrix T.
 * These vectors are computed, by the base axis function, from the direction attributes
 * axis1, axis2 and, in Cartesian transformation operator 3d, axis3. If |T|= -1,
 * the transformation includes mirroring. The local origin point A, the scale value S
 * and the matrix T together define a transformation.
 * <p>
 * The transformation for a point with position vector P is defined by
 * <pre>
 *      P -> A + STP
 * </pre>
 * The transformation for a direction d is defined by
 * <pre>
 *      d -> Td
 * </pre>
 * The transformation for a vector with orientation d and magnitude k is defined by
 * <pre>
 *      d -> Td, and
 *
 *      k -> Sk
 * </pre>
 * <p>
 * For those entities whose attributes include an axis2 placement, the transformation is
 * applied, after the derivation, to the derived attributes p defining the placement
 * coordinate directions. For a transformed surface, the direction of the surface normal
 * at any point is obtained by transforming the normal, at the corresponding point,
 * to the original surface. For geometric entities with attributes (such as the
 * radius of a circle) which have the dimensionality of length, the values will
 * be multiplied by S.
 * <p>
 * For curves on surface the p curve.reference to curve will be unaffected by any
 * transformation. The Cartesian transformation operator shall only be applied to
 * geometry defined in a consistent system of units with the same units on each axis.
 * With all optional attributes omitted, the transformation defaults to the identity
 * transformation. The Cartesian transformation operator shall only be instantiated
 * as one of its subtypes.
 * <p>
 * A Cartesian transformation operator 2d defines a geometric transformation in
 * two-dimensional space composed of translation, rotation, mirroring and uniform scaling.
 * The list of normalized vectors u defines the columns of an orthogonal matrix T.
 * These vectors are computed from the direction attributes axis1 and axis2 by the base
 * axis function. If |T|= -1, the transformation includes mirroring.
 *
 * <pre>
 *  Point
 *	Q.x = A.x + S * (P.x * U1.x + P.y * U2.x)
 *	Q.y = A.y + S * (P.x * U1.y + P.y * U2.y)
 * </pre>
 *
 * <pre>
 *  Vector
 *	W.x = S * (V.x * U1.x + V.y * U2.x)
 *	W.y = S * (V.x * U1.y + V.y * U2.y)
 * </pre>
 * <pre>
 * M = S * L
 * </pre>
 *
 * @author Information-technology Promotion Agency, Japan
 * @see	CartesianTransformationOperator3D
 */
public class CTransformationOperator2D extends CTransformationOperator implements ITransformation2D {

    private static final long serialVersionUID = 1L;

    /**
	 * The direction used to determine U[1], the derived X axis direction.
	 */
    private Vec2D axis1;

    /**
	 * The direction used to determine U[2], the derived Y axis direction.
	 */
    private Vec2D axis2;

    /**
	 * The required translation, specified as a cartesian point.
	 * The actual translation included in the transformation is from the geometric
	 * origin to the local origin.
	 */
    private Pnt2D localOrigin;

    /**
	 * The list of mutually orthogonal, normalized vectors defining the transformation matrix T.
	 * They are derived from the explicit attributes Axis1 and Axis2 in that order.
	 */
    private Vec2D u[];

    /**
	 * Constructor
	 *
	 * @param axis1       X axis
	 * @param axis2       Y axis
	 * @param localOrigin local origin (cannot be null)
	 * @param scale       scale (cannot be zero)
	 */
    public CTransformationOperator2D(Vec2D axis1, Vec2D axis2, Pnt2D localOrigin, double scale) {
        super(scale);
        if (localOrigin == null) {
            throw new ExceptionGeometryInvalidArgumentValue("localOrigin cannot be null");
        }
        this.localOrigin = localOrigin;
        this.axis1 = axis1;
        this.axis2 = axis2;
    }

    /**
	 * Constructor
	 *
	 * @param position
	 * @param scale
	 */
    public CTransformationOperator2D(Axis2Placement2D position, double scale) {
        super(scale);
        if (position == null) {
            throw new ExceptionGeometryInvalidArgumentValue();
        }
        this.u = position.axes();
        this.localOrigin = position.location();
        this.axis1 = u1();
        this.axis2 = u2();
    }

    public int dimension() {
        return 2;
    }

    /**
	 * Returns X axis
	 *
	 * @return
	 */
    public Vec2D axis1() {
        return axis1;
    }

    /**
	 * Returns Y axis
	 *
	 * @return
	 */
    public Vec2D axis2() {
        return axis2;
    }

    /**
	 * Returns local origin
	 *
	 * @return
	 */
    public Pnt2D localOrigin() {
        return localOrigin;
    }

    /**
	 * Returns u1
	 *
	 * @return
	 */
    public Vec2D u1() {
        if (u == null) {
            u();
        }
        return u[0];
    }

    /**
	 * Returns u2
	 *
	 * @return
	 */
    public Vec2D u2() {
        if (u == null) {
            u();
        }
        return u[1];
    }

    /**
	 * Returns U vectors
	 *
	 * @return
	 */
    public Vec2D[] u() {
        if (u == null) {
            u = GeometrySchemaFunction.baseAxis(axis1, axis2);
        }
        return u.clone();
    }

    /**
	 * Transforms vector
	 *
	 * @param vector
	 * @return
	 */
    public Vec2D transform(Vec2D vector) {
        double x, y;
        x = scale() * (vector.x() * u1().x() + vector.y() * u2().x());
        y = scale() * (vector.x() * u1().y() + vector.y() * u2().y());
        return new LVec2D(x, y);
    }

    /**
	 * Transforms point
	 *
	 * @param point
	 * @return
	 */
    public Pnt2D transform(Pnt2D point) {
        double x, y;
        x = localOrigin.x() + scale() * (point.x() * u1().x() + point.y() * u2().x());
        y = localOrigin.y() + scale() * (point.x() * u1().y() + point.y() * u2().y());
        return new CPnt2D(x, y);
    }

    /**
	 * Transforms vector
	 *
	 * @param vector
	 * @param transformedGeometries
	 * @return
	 */
    public Vec2D transform(Vec2D vector, java.util.HashMap<Geometry, Geometry> transformedGeometries) {
        return vector.transformBy(this, transformedGeometries);
    }

    /**
	 * Transforms point
	 *
	 * @param point
	 * @param transformedGeometries
	 * @return
	 */
    public Pnt2D transform(Pnt2D point, java.util.HashMap<Geometry, Geometry> transformedGeometries) {
        return point.transformBy(this, transformedGeometries);
    }

    /**
	 * Transforms curve
	 *
	 * @param curve
	 * @param transformedGeometries
	 * @return
	 */
    public ParametricCurve2D transform(ParametricCurve2D curve, java.util.HashMap<Geometry, Geometry> transformedGeometries) {
        return curve.transformBy(this, transformedGeometries);
    }

    /**
	 * Reverse transforms vector
	 *
	 * @param vector
	 * @return
	 */
    public Vec2D reverseTransform(Vec2D vector) {
        double x, y;
        x = (vector.x() * u1().x() + vector.y() * u1().y()) / scale();
        y = (vector.x() * u2().x() + vector.y() * u2().y()) / scale();
        return new LVec2D(x, y);
    }

    /**
	 * Reverse transform point
	 *
	 * @param point
	 * @return
	 */
    public Pnt2D reverseTransform(Pnt2D point) {
        Vec2D wk;
        double x, y;
        wk = point.subtract(localOrigin);
        x = (wk.x() * u1().x() + wk.y() * u1().y()) / scale();
        y = (wk.x() * u2().x() + wk.y() * u2().y()) / scale();
        return new CPnt2D(x, y);
    }

    /**
	 * Reverse transforms vector
	 *
	 * @param vector
	 * @param transformedGeometries
	 * @return
	 */
    public Vec2D reverseTransform(Vec2D vector, java.util.HashMap<Geometry, Geometry> transformedGeometries) {
        return vector.reverseTransformBy(this, transformedGeometries);
    }

    /**
	 * Reverse transforms point
	 *
	 * @param point
	 * @param transformedGeometries
	 * @return
	 */
    public Pnt2D reverseTransform(Pnt2D point, java.util.HashMap<Geometry, Geometry> transformedGeometries) {
        return point.reverseTransformBy(this, transformedGeometries);
    }

    /**
	 * Reverse transforms curve
	 *
	 * @param curve 
	 * @param transformedGeometries
	 * @return
	 */
    public ParametricCurve2D reverseTransform(ParametricCurve2D curve, java.util.HashMap<Geometry, Geometry> transformedGeometries) {
        return curve.reverseTransformBy(this, transformedGeometries);
    }

    /**
	 * Returns vector enclosed
	 *
	 * @param vector
	 * @return
	 */
    public Vec2D toEnclosed(Vec2D vector) {
        return transform(vector);
    }

    /**
	 * Returns point enclosed
	 *
	 * @param point
	 * @return
	 */
    public Pnt2D toEnclosed(Pnt2D point) {
        return transform(point);
    }

    /**
	 * Returns vector enclosed
	 *
	 * @param vector
	 * @param transformedGeometries
	 * @return
	 */
    public Vec2D toEnclosed(Vec2D vector, java.util.HashMap<Geometry, Geometry> transformedGeometries) {
        return transform(vector, transformedGeometries);
    }

    /**
	 * Returns point enclosed
	 *
	 * @param point
	 * @param transformedGeometries
	 * @return
	 */
    public Pnt2D toEnclosed(Pnt2D point, java.util.HashMap<Geometry, Geometry> transformedGeometries) {
        return transform(point, transformedGeometries);
    }

    /**
	 * Returns curve enclosed
	 *
	 * @param curve 
	 * @param transformedGeometries
	 * @return
	 */
    public ParametricCurve2D toEnclosed(ParametricCurve2D curve, java.util.HashMap<Geometry, Geometry> transformedGeometries) {
        return transform(curve, transformedGeometries);
    }

    /**
	 * Returns vector local
	 *
	 * @param vector
	 * @return
	 */
    public Vec2D toLocal(Vec2D vector) {
        return reverseTransform(vector);
    }

    /**
	 * Returns point local
	 *
	 * @param point
	 * @return
	 */
    public Pnt2D toLocal(Pnt2D point) {
        return reverseTransform(point);
    }

    /**
	 * Returns vector local
	 *
	 * @param vector 
	 * @param transformedGeometries
	 * @return
	 */
    public Vec2D toLocal(Vec2D vector, java.util.HashMap<Geometry, Geometry> transformedGeometries) {
        return reverseTransform(vector, transformedGeometries);
    }

    /**
	 * Returns point locla
	 *
	 * @param point
	 * @param transformedGeometries
	 * @return
	 */
    public Pnt2D toLocal(Pnt2D point, java.util.HashMap<Geometry, Geometry> transformedGeometries) {
        return reverseTransform(point, transformedGeometries);
    }

    /**
	 * Returns curve local
	 *
	 * @param curve 
	 * @param transformedGeometries
	 * @return
	 */
    public ParametricCurve2D toLocal(ParametricCurve2D curve, java.util.HashMap<Geometry, Geometry> transformedGeometries) {
        return reverseTransform(curve, transformedGeometries);
    }

    protected void output(PrintWriter writer, int indent) {
        String indent_tab = makeIndent(indent);
        writer.println(indent_tab + getClassName());
        writer.println(indent_tab + "\tscale\t" + scale());
        if (axis1 != null) {
            writer.println(indent_tab + "\taxis1");
            axis1.output(writer, indent + 2);
        }
        if (axis2 != null) {
            writer.println(indent_tab + "\taxis2");
            axis2.output(writer, indent + 2);
        }
        writer.println(indent_tab + "\tlocalOrigin");
        localOrigin.output(writer, indent + 2);
        writer.println("End");
    }
}
