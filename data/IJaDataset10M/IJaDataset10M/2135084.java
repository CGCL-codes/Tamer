package jp.go.ipa.jgcl;

import java.io.PrintWriter;

/**
 * Axis placement following ISO 10303-42, same as "axis2_placement" in STEP.
 * <P>
 * The location and orientation in three dimensional space of three mutually perpendicular axes.
 * An axis2_placement_3D is defined in terms of a point (inherited from placement supertype) 
 * and two (ideally orthogonal) axes. It can be used to locate and originate an object in three 
 * dimensional space and to define a placement coordinate system. The entity includes a point which 
 * forms the origin of the placement coordinate system. Two direction vectors are required to 
 * complete the definition of the placement coordinate system. The axis is the placement Z axis 
 * direction and the ref_direction is an approximation to the placement X axis direction.
 * <p>
 * If the attribute values for axis and refDirection are not given, the placement defaults to 
 * P[1] (x-axis) as [1.,0.,0.], P[2] (y-axis) as [0.,1.,0.] and P[3] (z-axis) as [0.,0.,1.].
 *
 * @author Information-technology Promotion Agency, Japan
 * @see Axis1Placement3D
 * @see Axis2Placement2D
 */
public class Axis2Placement3D extends Placement3D {

    private static final long serialVersionUID = 1L;

    /**
	 * Origin
	 */
    public static final Axis2Placement3D origin;

    /**
	 * Initrialize static data
	 */
    static {
        origin = new Axis2Placement3D(Pnt3D.origin, Vec3D.zUnitVector, Vec3D.xUnitVector);
    }

    /**
	 * The exact direction of the local Z Axis.
	 */
    private final Vec3D axis;

    /**
	 * The direction used to determine the direction of the local X Axis.
	 * If necessary an adjustment is made to maintain orthogonality to the Axis direction.
	 * If axis and/or refDirection is omitted, these directions are taken from the geometric
	 * coordinate system.
	 */
    private final Vec3D refDirection;

    /**
	 * The normalized directions of the placement X Axis (P[1]) and the placement Y Axis (P[2])
	 * and the placement Z Axis (P[3]).
	 */
    private Vec3D[] axes;

    /**
	 * Constructor
	 *
	 * @param location
	 * @param axis
	 * @param refDirection
	 */
    public Axis2Placement3D(Pnt3D location, Vec3D axis, Vec3D refDirection) {
        super(location);
        this.axis = axis;
        this.refDirection = refDirection;
        checkFields();
    }

    /**
	 * Check validity
	 */
    private void checkFields() {
        ConditionOfOperation condition = ConditionOfOperation.getCondition();
        double tol_2d = condition.getToleranceForDistance2();
        if (axis != null) {
            if (axis.lengthSquared() <= tol_2d) {
                throw new ExceptionGeometryInvalidArgumentValue();
            }
        }
        if (refDirection != null) {
            if (refDirection.lengthSquared() <= tol_2d) {
                throw new ExceptionGeometryInvalidArgumentValue();
            }
        }
        if (axis != null || refDirection != null) {
            Vec3D a, b;
            if (axis == null) {
                a = Vec3D.zUnitVector;
            } else {
                a = axis;
            }
            if (refDirection == null) {
                b = Vec3D.xUnitVector;
            } else {
                b = refDirection;
            }
            if (true) {
                Vec3D c;
                c = a.cross(b);
                if (c.lengthSquared() <= tol_2d) {
                    throw new ExceptionGeometryInvalidArgumentValue();
                }
            } else {
                if (a.identicalDirection(b)) {
                    throw new ExceptionGeometryInvalidArgumentValue();
                }
            }
        }
    }

    /**
	 * Returns axis (can be null)
	 *
	 * @return
	 */
    public Vec3D axis() {
        return axis;
    }

    /**
	 * Returns effective axis (default is Z)
	 *
	 * @return
	 */
    public Vec3D effectiveAxis() {
        return (axis != null) ? axis : GeometrySchemaFunction.defaultAxis3D;
    }

    /**
	 * Returns reference direction (X axis)
	 *
	 * @return
	 */
    public Vec3D refDirection() {
        return refDirection;
    }

    /**
	 * Returns effective reference direction (default is X)
	 *
	 * @return
	 */
    public Vec3D effectiveRefDirection() {
        return (refDirection != null) ? refDirection : GeometrySchemaFunction.defaultRefDirection3D;
    }

    /**
	 * Returns X
	 *
	 * @return
	 */
    public Vec3D x() {
        if (axes == null) {
            axes();
        }
        return axes[0];
    }

    /**
	 * Returns Y
	 *
	 * @return
	 */
    public Vec3D y() {
        if (axes == null) {
            axes();
        }
        return axes[1];
    }

    /**
	 * Returns Z
	 *
	 * @return
	 */
    public Vec3D z() {
        if (axes == null) {
            axes();
        }
        return axes[2];
    }

    /**
	 * Returns axis vector
	 *
	 * @return
	 */
    public Vec3D[] axes() {
        if (axes == null) {
            axes = GeometrySchemaFunction.buildAxes(axis, refDirection);
        }
        return axes.clone();
    }

    /**
	 * Returns paralell translated
	 *
	 * @param moveVec
	 * @return
	 */
    public Axis2Placement3D parallelTranslate(Vec3D moveVec) {
        return new Axis2Placement3D(location().add(moveVec), axis, refDirection);
    }

    /**
	 * Rotate around Z
	 */
    Axis2Placement3D rotateZ(CTransformationOperator3D trns, double rCos, double rSin) {
        Pnt3D rloc = location().rotateZ(trns, rCos, rSin);
        Vec3D raxis = z().rotateZ(trns, rCos, rSin);
        Vec3D rref = x().rotateZ(trns, rCos, rSin);
        return new Axis2Placement3D(rloc, raxis, rref);
    }

    /**
	 * Returns as transformation operator
	 *
	 * @param scale
	 * @return
	 */
    public CTransformationOperator3D toCartesianTransformationOperator(double scale) {
        return new CTransformationOperator3D(this, scale);
    }

    /**
	 * Returns as transformation operator
	 *
	 * @return
	 */
    public CTransformationOperator3D toCartesianTransformationOperator() {
        return new CTransformationOperator3D(this);
    }

    /**
	 * Transform
	 *
	 * @param reverseTransform
	 * @param transformationOperator
	 * @param transformedGeometries
	 * @return
	 */
    protected synchronized Axis2Placement3D doTransformBy(boolean reverseTransform, CTransformationOperator3D transformationOperator, java.util.HashMap<Geometry, Geometry> transformedGeometries) {
        Pnt3D tLocation = this.location().transformBy(reverseTransform, transformationOperator, transformedGeometries);
        Vec3D tAxis = this.effectiveAxis().transformBy(reverseTransform, transformationOperator, transformedGeometries);
        Vec3D tRefDirection = this.effectiveRefDirection().transformBy(reverseTransform, transformationOperator, transformedGeometries);
        return new Axis2Placement3D(tLocation, tAxis, tRefDirection);
    }

    /**
	 * Transform
	 *
	 * @param reverseTransform
	 * @param transformationOperator
	 * @param transformedGeometries
	 * @return
	 */
    public synchronized Axis2Placement3D transformBy(boolean reverseTransform, CTransformationOperator3D transformationOperator, java.util.HashMap<Geometry, Geometry> transformedGeometries) {
        if (transformedGeometries == null) {
            return this.doTransformBy(reverseTransform, transformationOperator, transformedGeometries);
        }
        Axis2Placement3D transformed = (Axis2Placement3D) transformedGeometries.get(this);
        if (transformed == null) {
            transformed = this.doTransformBy(reverseTransform, transformationOperator, transformedGeometries);
            transformedGeometries.put(this, transformed);
        }
        return transformed;
    }

    /**
	 * Transform
	 *
	 * @param transformationOperator
	 * @param transformedGeometries
	 * @return
	 */
    public synchronized Axis2Placement3D transformBy(CTransformationOperator3D transformationOperator, java.util.HashMap<Geometry, Geometry> transformedGeometries) {
        return this.transformBy(false, transformationOperator, transformedGeometries);
    }

    /**
	 * Reverse transform
	 *
	 * @param transformationOperator
	 * @param transformedGeometries
	 * @return
	 */
    public synchronized Axis2Placement3D reverseTransformBy(CTransformationOperator3D transformationOperator, java.util.HashMap<Geometry, Geometry> transformedGeometries) {
        return this.transformBy(true, transformationOperator, transformedGeometries);
    }

    protected void output(PrintWriter writer, int indent) {
        String indent_tab = makeIndent(indent);
        writer.println(indent_tab + getClassName());
        writer.println(indent_tab + "\tlocation");
        location().output(writer, indent + 2);
        if (axis != null) {
            writer.println(indent_tab + "\taxis");
            axis.output(writer, indent + 2);
        }
        if (refDirection != null) {
            writer.println(indent_tab + "\trefDirection");
            refDirection.output(writer, indent + 2);
        }
        writer.println(indent_tab + "End");
    }
}
