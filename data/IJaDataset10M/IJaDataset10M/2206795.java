package jpicedt.graphic.model;

import java.awt.geom.PathIterator;
import java.awt.Shape;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.geom.Arc2D;
import java.awt.geom.Arc2D.Double;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JLabel;
import jpicedt.graphic.PEToolKit;
import jpicedt.graphic.PicPoint;
import jpicedt.graphic.PicVector;
import jpicedt.graphic.event.DrawingEvent;
import jpicedt.graphic.toolkit.AbstractCustomizer;
import jpicedt.graphic.view.ArrowView;
import jpicedt.ui.dialog.UserConfirmationCache;
import jpicedt.widgets.DecimalNumberField;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;
import static java.lang.Math.abs;
import static java.lang.Math.sqrt;
import static java.lang.Math.acos;
import static java.lang.Math.atan;
import static java.lang.Math.atan2;
import static java.lang.Math.PI;
import static jpicedt.Log.*;
import static jpicedt.graphic.model.PicEllipseConstants.*;

/**
 * Ellipse or arc, based on parallelogram
 * <br>
 * The geometrical model of this ellipse (or its arc counterpart) is based on a parallelogram, yet
 * is equivalent to an ellipse rotated around its center.
 * <p>
 * In the following code documentation, we will be using two coordinate-systems, namely :
 * <ul>
 * <li> a so-called <b>parallelogram basis</b>, whose vectors are defined by the sides of the parallelogram.
 *      These correspond to fields {@link #l2rVec l2rVec} and {@link #b2tVec b2tVec} inherited from
 *      superclass.
 * <li> an <b>ellipse basis</b> (also called canonical basis), whose vectors are defined by the great and
 *      small axes of the ellipse respectively.
 * </ul>
 * Arc angles may be expressed in either system, and this is indicated in code documentation when applicable.
 * Prefix <code>skew</code> refers to the parallelogram basis, whereas <code>rotated</code> denotes angles
 * measured in the ellipse basis.  Since ellipse axes are generally not parallel to parallelogram sides,
 * except in the event this parallelogram is a rectangle, both sets of angle DO obviously differ.
 * <p>
 * In the parallelogram basis, arc is modelled by the following parametrized curve :<br> <b>Center</b> +
 * cos(t)*<b>U</b>+sin(t)*<b>V</b>,<br> for t ranging from <code>skewAngleStart</code> to
 * <code>skewAngleEnd</code>.
 * <p>
 * Rotated angles are computed so that the arc of ellipse is obtained simply by rotating by "rotationAngle"
 * (CCW) the arc of ellipse whose parametrization is Center+greatAxis*cos(t)*I+smallAxis*sin(t)*J where I,J is
 * the ellipse canonical basis, and t varies between "rotatedAngleStart" and "rotatedAngleEnd".  Note that
 * this means that when small axis is negative, this parametrization goes CW and not CCW.
 * <p>
 * <dl>
 * <dt>[TODO]</dt><dd>changes ALL internal angles to radian, and move conversion to setters/getters is user
 *        may want to enter degrees.</dd>
 * <dt>[TODO]</dt><dd> add a picture (in GIF or PNG format) to this documentation (as in AbstractCurve) to
 *        help end-users understand all these tricky things!</dd>
 * </dl>
 * @author Vincent Guirardel, Sylvain Reynal.
 * @since jPicEdt 1.4
 * @version $Id: PicEllipse.java,v 1.45 2011/12/03 21:16:30 vincentb1 Exp $
 */
public class PicEllipse extends PicParallelogram {

    /** point marking start-of-arc  */
    public static final int P_ANGLE_START = 9;

    /** point marking end-of-arc  */
    public static final int P_ANGLE_END = 10;

    public static final int LAST_PT = 10;

    /** prefined closure type for arcs */
    public static final int CHORD = Arc2D.CHORD;

    /** prefined closure type for arcs */
    public static final int OPEN = Arc2D.OPEN;

    /** prefined closure type for arcs */
    public static final int PIE = Arc2D.PIE;

    /**
	 * Arc start- and end- angles in degrees as measured with respect to parallolegram's basis
	 * (see {@link #toParalleloBasisCoordinates toParalleloBasisCoordinates()} for
	 * details on how this basis is defined).
	 * <p>
	 * To wind up shortly, let <b>U</b> and <b>V</b> denote unit-vectors defined by the sides of the
	 * parallelogram (these are actually given by "l2rVec" and "b2tVec" protected fields, yet immediately
	 * after {@link PicParallelogram#updateParalleloBasis() updateParalleloBasis} has been called), then the
	 * starting point of the arc is located at :
	 * <b>Center</b>+cos(skewAngleStart)*<b>U</b>+sin(skewAngleStart)*<b>V</b>.
	 * <p>
	 * This value is closer to the user than rotatedAngleStart. [SR:je ne comprend pas cette phrase !!!]
	 * <p>
	 * Moreover, arc is modelled by the following parametrized curve :<br>
	 * <b>Center</b> + cos(t)*<b>U</b>+sin(t)*<b>V</b>,<br>
	 * for t ranging from skewAngleStart to skewAngleEnd.
	 * <p>
	 * We set skewAngleStart in (-180,180], and skewAngleEnd in (skewAngleStart,skewAngleStart+360].  This
	 * means that if one makes a mirror symmetry of the parallelogram and keeps the values of these "skew"
	 * angles unchanged, then we get the mirror image of the arc.
	 * <br>
	 * We use degrees because user may enter a value.
	 * [SR:pending] On peut faire la conversion dans les setters correspondants (setAngleXXX).
	 * L'ideal est qd meme d'avoir la meme unite partout (en interne) pour ne pas s'emmeler les pedales.
	 * @see #l2rVec
	 * @see #b2tVec
	 */
    protected double skewAngleStart;

    /**
	 * @see #skewAngleStart
	 */
    protected double skewAngleEnd;

    /**
	 * Angle in radians (CCW) between the great axis of the ellipse and the horizontal axis.
	 */
    protected double rotationAngle;

    /**
	 * Length of the small axis of the ellipse.
	 * May be negative, as
	 * this gives a more coherent behaviour under negative scaling in one direction.
	 * The sign is determined by the orientation of the parallelogram.
	 */
    protected double smallAxis;

    /**
	 * Lengths of the great axis of the ellipse.
	 */
    protected double greatAxis;

    /**
	 * Arc angles in degrees, as measured in the <b>ellipse basis</b>. These are computed from their "skew"
	 * counterparts, through a call to {@link #updateRotatedAngles updateRotatedAngles}, and are obviously
	 * identical with them if surrounding parallelogram is a rectangle.  <p> These angles may be used to build
	 * an appropriate {@link java.awt.Shape Shape} for this Element, by first creating an {@link
	 * java.awt.geom.Arc2D Arc2D} from these angles, then rotating this shape by {@link #rotationAngle
	 * rotationAngle}.  <p> We restrict values of <code>rotatedAngleStart</code> to (-180,180], and those of
	 * <code>rotatedAngleEnd</code> to (<code>rotatedAngleStart</code>,<code>rotatedAngleStart</code>+360].
	 */
    protected double rotatedAngleStart, rotatedAngleEnd;

    /** closure type */
    protected int closure;

    /**
	 * Create a new PicEllipse, centered at (0,0), with a null radius.
	 * CLOSED is the default closure
	 */
    public PicEllipse() {
        super();
        initDefault();
    }

    /**
	 * Create a new <code>PicEllipse</code>, centered at (0,0), with a null radius, and the given attribute
	 * set.
	 */
    public PicEllipse(PicAttributeSet set) {
        super(set);
        initDefault();
    }

    /**
	 * Create a new <code>PicEllipse</code>, centered at (0,0), with a null radius, the given closure type.
	 * @param closure one of <code>CHORD</code>, <code>PIE</code> or <code>OPEN</code>.
	 */
    public PicEllipse(int closure) {
        super();
        initDefault();
        this.closure = closure;
    }

    /**
	 * Create a new PicEllipse, centered at (0,0), with a null radius,
	 * the given closure type, and the given attribute set.
	 * @param closure one of CHORD, PIE or OPEN.
	 */
    public PicEllipse(int closure, PicAttributeSet set) {
        super(set);
        initDefault();
        this.closure = closure;
    }

    /**
	 * Create a new <code>PicEllipse</code> object using the 3 given points as 3 consecutive points of the
	 * surrounding parallelogram, and the given attribute set.
	 * @param closure one of <code>CHORD</code>, <code>PIE</code> or <code>OPEN</code>.
	 */
    public PicEllipse(PicPoint p1, PicPoint p2, PicPoint p3, int closure, PicAttributeSet set) {
        super(p1, p2, p3, set);
        initDefault();
        this.closure = closure;
        updateAxis();
    }

    /**
	 * Create a new <code>PicEllipse</code> object using the 3 given points as 3 consecutive points of the
	 * surrounding parallelogram, and a default attribute set.
	 * @param closure one of <code>CHORD</code>, <code>PIE</code> or <code>OPEN</code>.
	 */
    public PicEllipse(PicPoint p1, PicPoint p2, PicPoint p3, int closure) {
        super(p1, p2, p3);
        initDefault();
        this.closure = closure;
        updateAxis();
    }

    /**
	 * "cloning" constructor (to be used by clone())
	 * @param src The PicEllipse object to clone
	 */
    public PicEllipse(PicEllipse src) {
        super(src);
        greatAxis = src.greatAxis;
        smallAxis = src.smallAxis;
        rotationAngle = src.rotationAngle;
        closure = src.closure;
        rotatedAngleStart = src.rotatedAngleStart;
        rotatedAngleEnd = src.rotatedAngleEnd;
        skewAngleStart = src.skewAngleStart;
        skewAngleEnd = src.skewAngleEnd;
    }

    /**
	 * Called by contructors to init fields to their default value
	 */
    private void initDefault() {
        greatAxis = 0;
        smallAxis = 0;
        rotationAngle = 0;
        closure = Arc2D.OPEN;
        rotatedAngleStart = 0;
        rotatedAngleEnd = 360;
        skewAngleStart = 0;
        skewAngleEnd = 360;
    }

    /**
	 * Overload Object.clone() method
	 */
    public PicEllipse clone() {
        return new PicEllipse(this);
    }

    /**
	 * Return a localised string that represents this object's name
	 */
    public String getDefaultName() {
        return jpicedt.Localizer.currentLocalizer().get("model.Ellipse");
    }

    /**
	 * Set the coordinate of the point indexed by "numPoint" to the given value.
	 * If point controls the parallelogram, calls the paralleogram setCtrlPt method, and updates axis.
	 */
    public void setCtrlPt(int numPoint, PicPoint pt, EditPointConstraint constraint) {
        PicPoint center;
        PicVector vec;
        switch(numPoint) {
            case P_ANGLE_END:
                if (isFlat()) return;
                updateParalleloBasis();
                center = getCtrlPt(P_CENTER, null);
                vec = toParalleloBasisCoordinates(center, pt, null);
                setAngleEnd(toDegrees(PicVector.X_AXIS.angle(vec)));
                break;
            case P_ANGLE_START:
                if (isFlat()) return;
                updateParalleloBasis();
                center = getCtrlPt(P_CENTER, null);
                vec = toParalleloBasisCoordinates(center, pt, null);
                setAngleStart(toDegrees(PicVector.X_AXIS.angle(vec)));
                break;
            default:
                super.setCtrlPt(numPoint, pt, constraint);
                updateAxis();
                fireChangedUpdate(DrawingEvent.EventType.GEOMETRY_CHANGE);
        }
    }

    /**
	 * Sets the coordinates and angle extents of this <code>Element</code> from those of the given source.
	 * This methods fires a <code>DrawingEvent</code>.
	 * @since jpicedt 1.5
	 */
    public void setGeometry(PicEllipse ell) {
        this.skewAngleStart = ell.skewAngleStart;
        this.skewAngleEnd = ell.skewAngleEnd;
        this.rotationAngle = ell.rotationAngle;
        this.smallAxis = ell.smallAxis;
        this.greatAxis = ell.greatAxis;
        this.rotatedAngleStart = ell.rotatedAngleStart;
        this.rotatedAngleEnd = ell.rotatedAngleEnd;
        this.closure = ell.closure;
        super.setGeometry(ell);
    }

    /**
	 * Sets the coordinates of this element from the given shape.
	 * This methods fires a DrawingEvent.
	 * @since jpicedt 1.5
	 */
    public void setGeometry(Arc2D arc) {
        throw new RuntimeException("Not implemented yet!");
    }

    /**
	 * Return the <b>user-controlled point</b> having the given index. The general contract in
	 * <code>Element</code> is to return an IMMUTABLE instance of PicPoint, so that the only way to alter the
	 * geometry of this element is by calling the <code>setCtrlPt</code> method.<br>
	 * @return the point indexed by <code>numPoint</code> ;
	 *         if <code>src</code> is null, allocates a new <code>PicPoint</code> and return it,
	 *         otherwise directly modifies <code>src</code> and returns it as well for convenience.
	 * @param numPoint the point index, should be greater or equal to the value returned by
	 *        <code>getFirstPointIndex</code>, and lower or equal to <code>getLastPointIndex</code>.
	 */
    public PicPoint getCtrlPt(int numPoint, PicPoint src) {
        switch(numPoint) {
            case P_ANGLE_START:
                return getCtrlPt(P_CENTER, src).translate(ptBL, ptBR, 0.5 * cos(toRadians(skewAngleStart))).translate(ptBR, ptTR, 0.5 * sin(toRadians(skewAngleStart)));
            case P_ANGLE_END:
                return getCtrlPt(P_CENTER, src).translate(ptBL, ptBR, 0.5 * cos(toRadians(skewAngleEnd))).translate(ptBR, ptTR, 0.5 * sin(toRadians(skewAngleEnd)));
            default:
                return super.getCtrlPt(numPoint, src);
        }
    }

    /**
	 * Scale this object by <code>(sx,sy)</code> using <code>(ptOrgX,ptOrgY)</code> as the origin. This
	 * implementation simply apply a scaling transform to all specification-points.  Note that <code>sx</code>
	 * and <code>sy</code> may be negative.  This method eventually fires a changed-update event.
	 */
    public void scale(double ptOrgX, double ptOrgY, double sx, double sy, UserConfirmationCache ucc) {
        ptBL.scale(ptOrgX, ptOrgY, sx, sy);
        ptBR.scale(ptOrgX, ptOrgY, sx, sy);
        ptTR.scale(ptOrgX, ptOrgY, sx, sy);
        updateAxis();
        fireChangedUpdate(DrawingEvent.EventType.GEOMETRY_CHANGE);
    }

    /**
	 * Rotate this <code>Element</code> by the given angle along the given point.
	 * @param angle rotation angle in radians
	 */
    public void rotate(PicPoint ptOrg, double angle) {
        ptBL.rotate(ptOrg, angle);
        ptBR.rotate(ptOrg, angle);
        ptTR.rotate(ptOrg, angle);
        updateParalleloBasis();
        updateAxis();
        fireChangedUpdate(DrawingEvent.EventType.GEOMETRY_CHANGE);
    }

    /**
	 * Effectue une r�flexion sur <code>this</code> relativement � l'axe
	 * d�fini par <code>ptOrg</code> et <code>normalVector</code>.
	 *
	 * @param ptOrg le <code>PicPoint</code> par lequel passe l'axe de r�flexion.
	 * @param normalVector le <code>PicVector</code> normal � l'axe de r�flexion.
	 */
    public void mirror(PicPoint ptOrg, PicVector normalVector) {
        ptBL.mirror(ptOrg, normalVector);
        ptBR.mirror(ptOrg, normalVector);
        ptTR.mirror(ptOrg, normalVector);
        updateParalleloBasis();
        updateAxis();
        fireChangedUpdate(DrawingEvent.EventType.GEOMETRY_CHANGE);
    }

    /**
	 * Shear this <code>Element</code> by the given params wrt to the given origin.
	 */
    public void shear(PicPoint ptOrg, double shx, double shy, UserConfirmationCache ucc) {
        ptBL.shear(ptOrg, shx, shy);
        ptBR.shear(ptOrg, shx, shy);
        ptTR.shear(ptOrg, shx, shy);
        updateParalleloBasis();
        updateAxis();
        fireChangedUpdate(DrawingEvent.EventType.GEOMETRY_CHANGE);
    }

    /**
	 * Return a Bezier curve created from this ellipse.
	 * @since jpicedt 1.4
	 */
    public PicMultiCurve convertToMultiCurve() {
        Arc2D shapeEllipse = new Arc2D.Double(getArcType());
        shapeEllipse.setAngleExtent(-getRotatedAngleExtent());
        shapeEllipse.setAngleStart(-getRotatedAngleStart());
        shapeEllipse.setFrameFromCenter(0, 0, getGreatAxisLength() / 2, abs(getSmallAxisLength() / 2));
        AffineTransform rot = new AffineTransform();
        rot.setToIdentity();
        PicPoint center = getCtrlPt(P_CENTER, null);
        rot.translate(center.x, center.y);
        rot.rotate(getRotationAngle());
        if (getSmallAxisLength() < 0) rot.scale(1, -1);
        Shape shape = rot.createTransformedShape(shapeEllipse);
        PicMultiCurve curve = new PicMultiCurve(false, this.attributeSet);
        PathIterator pi = shape.getPathIterator(null);
        double[] coords = new double[6];
        while (!pi.isDone()) {
            int type = pi.currentSegment(coords);
            switch(type) {
                case PathIterator.SEG_MOVETO:
                    curve.addPoint(new PicPoint(coords[0], coords[1]));
                    break;
                case PathIterator.SEG_QUADTO:
                    break;
                case PathIterator.SEG_CUBICTO:
                    curve.curveTo(new PicPoint(coords[0], coords[1]), new PicPoint(coords[2], coords[3]), new PicPoint(coords[4], coords[5]));
                    break;
                case PathIterator.SEG_LINETO:
                    curve.lineTo(new PicPoint(coords[0], coords[1]));
                    break;
                case PathIterator.SEG_CLOSE:
                    curve.setClosed(true);
                    int Npts = curve.getBezierPtsCount();
                    curve.setBezierPt(Npts - 1, curve.getBezierPt(0));
                    curve.setBezierPt(Npts - 2, curve.getBezierPt(Npts - 3));
                    curve.fireChangedUpdate(DrawingEvent.EventType.GEOMETRY_CHANGE);
                    break;
                default:
            }
            pi.next();
        }
        return curve;
    }

    private Arc2D.Double shapeEllipse;

    /**
	 * Creates a Shape that reflects the geometry of this model.
	 */
    public Shape createShape() {
        if (shapeEllipse == null) shapeEllipse = new Arc2D.Double();
        shapeEllipse.setAngleExtent(-getRotatedAngleExtent());
        shapeEllipse.setAngleStart(-getRotatedAngleStart());
        shapeEllipse.setArcType(getArcType());
        if (isPlain()) shapeEllipse.setArcType(Arc2D.OPEN);
        shapeEllipse.setFrameFromCenter(0, 0, getGreatAxisLength() / 2, abs(getSmallAxisLength() / 2));
        AffineTransform rot = new AffineTransform();
        rot.setToIdentity();
        PicPoint center = getCtrlPt(P_CENTER, null);
        rot.translate(center.x, center.y);
        rot.rotate(getRotationAngle());
        if (getSmallAxisLength() < 0) rot.scale(1, -1);
        return rot.createTransformedShape(shapeEllipse);
    }

    public Rectangle2D getBoundingBox(Rectangle2D r) {
        return super.getBoundingBox(r);
    }

    /**
	 * Helper for the associated View. This implementation updates the geometry of
	 * the given <code>ArrowView</code> only if <code>isArc()==true</code>.
	 */
    public void syncArrowGeometry(ArrowView v, ArrowView.Direction d) {
        if (isPlain()) return;
        PicPoint loc;
        PicVector dir;
        switch(d) {
            case LEFT:
                dir = getTangentAtAngleStart(null);
                loc = getCtrlPt(P_ANGLE_START, null);
                v.updateShape(loc, dir);
                break;
            case RIGHT:
                dir = getTangentAtAngleEnd(null);
                loc = getCtrlPt(P_ANGLE_END, null);
                v.updateShape(loc, dir);
                break;
            default:
        }
    }

    /**
	 * Return the rotation angle, ie the angle between the great axis of the ellipse and the horizontal axis.<br>
	 * @return the rotation angle in radians (CCW)
	 * @see #rotationAngle
	 */
    public double getRotationAngle() {
        return rotationAngle;
    }

    /**
	 * Return the length of the great-axis of this ellipse/arc.
	 */
    public double getGreatAxisLength() {
        return greatAxis;
    }

    /**
	 * Return the <b>signed</b> length of the small-axis of this
	 * ellipse/arc. A positive sign indicate that the rotated angles are CCW,
	 * while a negative sign indicates that the rotated angles are CW.
	 */
    public double getSmallAxisLength() {
        return smallAxis;
    }

    /**
	 * Set the angle start, as measured in the parallelogram basis, then fire a changed-update.
	 * Arc always extends CCW from angle-start to angle-end, ie angle-extent is always positive.
	 * @param angleStart The starting angle of the arc, in degrees,
	 *        counted COUNTERCLOCKWISE (aka trigonometric),
	 *        and measured in the parallelogram basis, ie {@link #skewAngleStart skewAngleStart}.
	 */
    public void setAngleStart(double angleStart) {
        if (DEBUG) debug("angleStart=" + angleStart);
        this.skewAngleStart = angleStart % 360;
        if (skewAngleStart > 180) skewAngleStart -= 360;
        if (skewAngleStart <= -180) skewAngleStart += 360;
        if (skewAngleEnd <= skewAngleStart + MAX_FLAT_ANGLE) skewAngleEnd += 360; else if (skewAngleEnd > skewAngleStart - MAX_FLAT_ANGLE + 360) skewAngleEnd -= 360;
        updateRotatedAngles();
        fireChangedUpdate(DrawingEvent.EventType.GEOMETRY_CHANGE);
    }

    /**
	 * Returns {@link #skewAngleStart skewAngleStart}, ie the starting angle of the arc in degrees,
	 * measured in the frame defined by the parallelogram.
	 */
    public double getAngleStart() {
        return skewAngleStart;
    }

    /**
	 * Set the angle end, then fire a changed-update.
	 * Arc always extends CCW from angle-start to angle-end, ie angle-extent is always positive.
	 * @param angleEnd The angle end of the arc, in degrees,
	 *        measured in the frame defined by the parallelogram, ie {@link #skewAngleEnd skewAngleEnd}.
	 */
    public void setAngleEnd(double angleEnd) {
        if (DEBUG) debug("angleEnd=" + angleEnd);
        this.skewAngleEnd = angleEnd % 360;
        if (skewAngleEnd <= skewAngleStart + MAX_FLAT_ANGLE) skewAngleEnd += 360;
        if (skewAngleEnd <= skewAngleStart + MAX_FLAT_ANGLE) skewAngleEnd += 360;
        if (skewAngleEnd > skewAngleStart - MAX_FLAT_ANGLE + 360) skewAngleEnd -= 360;
        if (skewAngleEnd > skewAngleStart - MAX_FLAT_ANGLE + 360) skewAngleEnd -= 360;
        updateRotatedAngles();
        fireChangedUpdate(DrawingEvent.EventType.GEOMETRY_CHANGE);
    }

    /**
	 * Returns {@link #skewAngleEnd skewAngleEnd}, ie angle end of the arc in degrees,
	 * measured in the frame defined by the parallelogram. (value of skewAngleStart)
	 */
    public double getAngleEnd() {
        return skewAngleEnd;
    }

    /**
	 * Sets the angle extent, keeping the angle start fixed, then fire a changed update.
	 * If not positive, 360 is added to it.
	 * @param angleExtent Angular extent of the arc, in degrees, counted COUNTERCLOCKWISE
	 *        as measured in parallelogram basis.
	 */
    public void setAngleExtent(double angleExtent) {
        setAngleEnd(skewAngleStart + angleExtent);
    }

    /**
	 * Returns the extent of the arc in degrees (CCW) as measured in the parallelogram basis.
	 * Note that this method returns 360 for a plain ellipse, not 0.
	 * @return an angle in (0,360]
	 */
    public double getAngleExtent() {
        return skewAngleEnd - skewAngleStart;
    }

    /**
	 * Return {@link #rotatedAngleStart rotatedAngleStart}, ie the angle start
	 * of the arc in degrees, as measured in the frame defined by the axes of
	 * the ellipse, that is to say relative to the great axe, and with CW or
	 * CCW orientiation depending on whether getSmallAxisLength() returns a
	 * positive or negative number.
	 */
    public double getRotatedAngleStart() {
        return rotatedAngleStart;
    }

    /**
	 * Returns {@link #rotatedAngleEnd rotatedAngleEnd}, ie the angle end of
	 * the arc in degrees as measured in the frame defined by the axes of the
	 * ellipse, that is to say relative to the great axe, and with CW or CCW
	 * orientiation depending on whether getSmallAxisLength() returns a
	 * positive or negative number.
	 */
    public double getRotatedAngleEnd() {
        return rotatedAngleEnd;
    }

    /**
	 * Returns the angle extent of the arc in degrees
	 * as measured in the frame defined by the axes of the ellipse.
	 */
    public double getRotatedAngleExtent() {
        return rotatedAngleEnd - rotatedAngleStart;
    }

    /**
	 * Returns the closure type, ie one of CHORD, PIE or OPEN predefined constant fields.
	 */
    public int getArcType() {
        return closure;
    }

    /**
	 * Returns the closure type as a String, ie one of "chord", "pie" or "open"
	 */
    public String getArcTypeAsString() {
        switch(closure) {
            case OPEN:
                return "open";
            case PIE:
                return "pie";
            case CHORD:
                return "chord";
            default:
                return null;
        }
    }

    /**
	 * Sets the closure type to one of CHORD, PIE or OPEN.
	 */
    public void setArcType(int closure) {
        this.closure = closure;
        fireChangedUpdate(DrawingEvent.EventType.ATTRIBUTE_CHANGE);
    }

    /**
	 * Return the index of the last point that can be retrieved by
	 * {@link #getCtrlPt getCtrlPt}.
	 */
    public int getLastPointIndex() {
        return LAST_PT;
    }

    /**
	 * Return true if this ellipse is flat.
	 */
    public boolean isFlat() {
        return ((smallAxis == 0) || (greatAxis == 0));
    }

    /**
	 * Returns TRUE if this ellipse is circular, ie if smallAxis==greatAxis.
	 * @see #getSmallAxisLength
	 * @see #getGreatAxisLength
	 */
    public boolean isCircular() {
        return abs(greatAxis - abs(smallAxis)) < 0.0000001 * greatAxis;
    }

    /**
	 * Return true if this PicEllipse is a plain ellipse, ie if angle extent equals 360.
	 */
    public boolean isPlain() {
        if (abs(getAngleExtent()) < MAX_FLAT_ANGLE) return true;
        if (abs(getAngleExtent() - 360) < MAX_FLAT_ANGLE) return true;
        return false;
    }

    /**
	 * Returns true if this PicEllipse is an arc, ie if angle extent does not equl 360
	 */
    public boolean isArc() {
        return !isPlain();
    }

    /**
	 * Return true if this PicEllipse has a null rotation angle along the x-axis
	 */
    public boolean isRotated() {
        if (abs(rotationAngle) < MAX_FLAT_ANGLE) return false;
        return true;
    }

    /**
	 * Set angle parameters so that this ellipse is a plain ellipse.
	 */
    public void setPlain() {
        setAngleStart(0);
        setAngleEnd(0);
    }

    /**
	 * Returns whether this arc or ellipse is closed or not. This depends on the closure type, and this PicEllipse being plain or not.
	 */
    public boolean isClosed() {
        return (isPlain() || getArcType() == CHORD || getArcType() == PIE);
    }

    protected void _updateAxis() {
        double uX = l2rVec.x;
        double vX = b2tVec.x;
        double uY = l2rVec.y;
        double vY = b2tVec.y;
        double det = l2rVec.det(b2tVec);
        double lu = l2rVec.norm2();
        double lv = b2tVec.norm2();
        if (det * det <= MAX_FLAT_ANGLE * lu * lv) {
            this.smallAxis = 0;
            this.greatAxis = sqrt(lu + lv);
            if (lu > lv) {
                rotationAngle = PicVector.X_AXIS.angle(l2rVec);
            } else {
                rotationAngle = PicVector.X_AXIS.angle(b2tVec);
            }
            updateRotatedAngles();
            return;
        }
        double delta = (lu + lv) * (lu + lv) - 4 * det * det;
        if (delta <= 0) {
            this.greatAxis = abs(det) / sqrt(uX * uX + vX * vX);
            this.smallAxis = det / greatAxis;
            rotationAngle = 0;
            updateRotatedAngles();
            return;
        }
        double lambda = ((lu + lv) - sqrt(delta)) / 2;
        PicVector greatAxisDir = new PicVector();
        if (abs(vY * vY + uY * uY - lambda) > abs(vX * vX + uX * uX - lambda)) {
            greatAxisDir.x = vX * vY + uX * uY;
            greatAxisDir.y = vY * vY + uY * uY - lambda;
        } else {
            greatAxisDir.x = vX * vX + uX * uX - lambda;
            greatAxisDir.y = vX * vY + uX * uY;
        }
        rotationAngle = PicVector.X_AXIS.angle(greatAxisDir);
        greatAxis = abs(det) / sqrt(lambda);
        smallAxis = det / greatAxis;
        updateRotatedAngles();
    }

    protected void updateAxis() {
        PicVector u = new PicVector(ptBL, ptBR);
        PicVector v = new PicVector(ptBR, ptTR);
        double det = u.det(v);
        double lu = u.norm2();
        double lv = v.norm2();
        if (det * det <= MAX_FLAT_ANGLE * lu * lv) {
            this.smallAxis = 0;
            this.greatAxis = sqrt(lu + lv);
            if (lu > lv) rotationAngle = computeAngleWithHorizontal(u.x, u.y); else rotationAngle = computeAngleWithHorizontal(v.x, v.y);
            updateRotatedAngles();
            return;
        }
        double delta = (lu + lv) * (lu + lv) - 4 * det * det;
        if (delta <= 0) {
            this.greatAxis = abs(det) / sqrt(u.x * u.x + v.x * v.x);
            this.smallAxis = det / greatAxis;
            rotationAngle = 0;
            updateRotatedAngles();
            return;
        }
        double lambda = ((lu + lv) - sqrt(delta)) / 2;
        double greatAxisX, greatAxisY;
        if (abs(v.y * v.y + u.y * u.y - lambda) > abs(v.x * v.x + u.x * u.x - lambda)) {
            greatAxisX = v.x * v.y + u.x * u.y;
            greatAxisY = v.y * v.y + u.y * u.y - lambda;
        } else {
            greatAxisX = v.x * v.x + u.x * u.x - lambda;
            greatAxisY = v.x * v.y + u.x * u.y;
        }
        rotationAngle = computeAngleWithHorizontal(greatAxisX, greatAxisY);
        greatAxis = abs(det) / sqrt(lambda);
        smallAxis = det / greatAxis;
        updateRotatedAngles();
    }

    /**
	 * Updates values of rotatedAngleStart and  rotatedAngleEnd.
	 * Axes must be updated beforehands for this computation to be valid.
	 * if greatAxis==0 (implies smallAxis==0), we keep the old values [case of ellipse reduced to a point].
	 * Flat case is handled by fromSkewToRotated.
	 */
    protected void updateRotatedAngles() {
        if (skewAngleEnd - skewAngleStart == 360) {
            rotatedAngleStart = 0;
            rotatedAngleEnd = 360;
            return;
        }
        if (greatAxis == 0) return;
        rotatedAngleStart = fromSkewToRotated(skewAngleStart);
        rotatedAngleEnd = fromSkewToRotated(skewAngleEnd);
        if (rotatedAngleEnd <= rotatedAngleStart + MAX_FLAT_ANGLE) rotatedAngleEnd += 360;
    }

    /**
	 * Converts an angle measured wrt the parallelogram basis to an
	 * angle as measured in the ellipse basis.
	 * <p>
	 * More precisely, if U and V are the vectors defined by the sides of the parallelogram,
	 * and U' V' are the vectors corresponding
	 * to the small and great axes of the ellipse, and if W=cos(theta)*U+sin(theta)*V ,
	 * then computes theta' such that W=cos(theta')*U'+sin(theta')*V'.
	 * <p>
	 *  Axes must be updated beforehands for this computation to be valid.
	 * This method returns 0 whenever greatAxis==0
	 * [but one should rather keep old values of rotatedAngles in this case.]
	 * @param angle an angle in degrees
	 */
    private double _fromSkewToRotated(double angle) {
        if (greatAxis == 0) return 0;
        double x = 0.5 * l2rVec.x * cos(toRadians(angle)) + 0.5 * b2tVec.x * sin(toRadians(angle));
        double y = 0.5 * l2rVec.y * cos(toRadians(angle)) + 0.5 * b2tVec.y * sin(toRadians(angle));
        double u = x * cos(rotationAngle) + y * sin(rotationAngle);
        double v = -x * sin(rotationAngle) + y * cos(rotationAngle);
        if (smallAxis == 0) return toDegrees(acos(u / greatAxis));
        return toDegrees(computeAngleWithHorizontal(u / greatAxis, v / smallAxis));
    }

    private double fromSkewToRotated(double angle) {
        if (greatAxis == 0) return 0;
        double x = 0.5 * (ptBR.x - ptBL.x) * cos(toRadians(angle)) + 0.5 * (ptTR.x - ptBR.x) * sin(toRadians(angle));
        double y = 0.5 * (ptBR.y - ptBL.y) * cos(toRadians(angle)) + 0.5 * (ptTR.y - ptBR.y) * sin(toRadians(angle));
        double u = x * cos(rotationAngle) + y * sin(rotationAngle);
        double v = -x * sin(rotationAngle) + y * cos(rotationAngle);
        if (smallAxis == 0) return toDegrees(acos(u / greatAxis));
        return toDegrees(computeAngleWithHorizontal(u / greatAxis, v / smallAxis));
    }

    /**
	 * Compute the angle (in Radians) of a vector (x,y) with the horizontal in (-Pi,Pi]
	 */
    private double computeAngleWithHorizontal(double x, double y) {
        return atan2(y, x);
    }

    /**
	 * Compute the angle (in Radians) of a vector (x,y) with the horizontal in -Pi,Pi
	 */
    private double computeAngleWithHorizontal(PicVector p) {
        return PicVector.X_AXIS.angle(p);
    }

    /**
	 * Utility for computing arrow direction.
	 * @return a unit-1 vector tangent to the arc, at phi = start angle, pointing "outwardly"
	 * @param pt a preallocated PicPoint that get filled with the result ; a new one is allocated if pt==null
	 */
    public PicVector getTangentAtAngleStart(PicVector pt) {
        if (pt == null) pt = new PicVector();
        pt.setCoordinates(ptBL, ptBR);
        pt.scale(sin(toRadians(skewAngleStart)));
        PicVector vec = new PicVector();
        vec.setCoordinates(ptBR, ptTR);
        vec.scale(-cos(toRadians(skewAngleStart)));
        pt.add(vec);
        pt.normalize();
        return pt;
    }

    /**
	 * Utility for computing arrow direction.
	 * @return a unit-1 vector tangent to the arc, at phi = end angle, pointing "outwardly"
	 * @param pt a preallocated PicPoint that get filled with the result ; a new one is allocated if pt==null
	 */
    public PicVector getTangentAtAngleEnd(PicVector pt) {
        if (pt == null) pt = new PicVector();
        pt.setCoordinates(ptBL, ptBR);
        pt.scale(sin(toRadians(skewAngleEnd)));
        PicVector vec = new PicVector();
        vec.setCoordinates(ptBR, ptTR);
        vec.scale(-cos(toRadians(skewAngleEnd)));
        pt.add(vec);
        pt.normalize();
        pt.inverse();
        return pt;
    }

    /**
	 * Implementation of the Object.toString() method, used for debugging purpose
	 * <br><b>author:</b> Sylvain Reynal
	 * @since PicEdt 1.1.4
	 */
    public String toString() {
        String s = super.toString() + "\n\t greatAxis=" + greatAxis + ", smallAxis=" + smallAxis + ", rotationAngle=" + rotationAngle + ", skewAngleStart=" + skewAngleStart + ", skewAngleEnd=" + skewAngleEnd + ", rotatedAngleStart=" + rotatedAngleStart + ", rotatedAngleEnd=" + rotatedAngleEnd + ", isPlain=" + (isPlain() ? "true" : "false") + ", closure=" + getArcTypeAsString();
        return s;
    }

    /**
	 * @return a Customizer for geometry editing
	 * @since jpicedt 1.3.3
	 */
    public AbstractCustomizer createCustomizer() {
        if (cachedCustomizer == null) cachedCustomizer = new Customizer();
        cachedCustomizer.load();
        return cachedCustomizer;
    }

    private Customizer cachedCustomizer = null;

    /**
	 * geometry customizer
	 */
    class Customizer extends PicParallelogram.Customizer implements ActionListener {

        private DecimalNumberField ellipseAngleStartTF, ellipseAngleEndTF;

        private JComboBox arcTypeList;

        private boolean isListenersAdded = false;

        private ImageIcon[] arcTypeIcons = { PEToolKit.createImageIcon("action.draw.ArcChord"), PEToolKit.createImageIcon("action.draw.ArcOpen"), PEToolKit.createImageIcon("action.draw.ArcPie") };

        /** create a new Customizer for editing the geometry and attributes of this PicEllipse */
        public Customizer() {
            super();
            JPanel p = new JPanel(new GridLayout(3, 3, 5, 5));
            p.add(PEToolKit.createJLabel("attributes.EllipseStartAngle"));
            p.add(ellipseAngleStartTF = new DecimalNumberField(5));
            p.add(new JLabel("deg"));
            p.add(PEToolKit.createJLabel("attributes.EllipseEndAngle"));
            p.add(ellipseAngleEndTF = new DecimalNumberField(5));
            p.add(new JLabel("deg"));
            arcTypeList = PEToolKit.createComboBox(arcTypeIcons);
            p.add(new JLabel(" "));
            p.add(arcTypeList);
            p.add(new JLabel(" "));
            add(p, BorderLayout.CENTER);
            setPreferredSize(new Dimension(400, 250));
        }

        /** add action listeners to widgets to reflect changes immediately */
        private void addActionListeners() {
            if (isListenersAdded) return;
            ellipseAngleStartTF.addActionListener(this);
            ellipseAngleEndTF.addActionListener(this);
            arcTypeList.addActionListener(this);
            isListenersAdded = true;
        }

        private void removeActionListeners() {
            if (!isListenersAdded) return;
            ellipseAngleStartTF.removeActionListener(this);
            ellipseAngleEndTF.removeActionListener(this);
            arcTypeList.removeActionListener(this);
            isListenersAdded = false;
        }

        /**
		 * (re)init widgets with Element's properties
		 */
        public void load() {
            super.load();
            removeActionListeners();
            ellipseAngleStartTF.setValue(getAngleStart());
            ellipseAngleEndTF.setValue(getAngleEnd());
            if (getArcType() == CHORD) arcTypeList.setSelectedIndex(0); else if (getArcType() == OPEN) arcTypeList.setSelectedIndex(1); else if (getArcType() == PIE) arcTypeList.setSelectedIndex(2);
            addActionListeners();
        }

        /**
		 * update Element's properties
		 */
        public void store() {
            super.store();
            setAngleStart(ellipseAngleStartTF.getValue());
            ellipseAngleStartTF.setValue(getAngleStart());
            setAngleEnd(ellipseAngleEndTF.getValue());
            ellipseAngleEndTF.setValue(getAngleEnd());
            switch(arcTypeList.getSelectedIndex()) {
                case 0:
                    setArcType(Arc2D.CHORD);
                    break;
                case 1:
                    setArcType(Arc2D.OPEN);
                    break;
                case 2:
                    setArcType(Arc2D.PIE);
                    break;
                default:
            }
            updateAxis();
            fireChangedUpdate(DrawingEvent.EventType.GEOMETRY_CHANGE);
        }

        public void actionPerformed(ActionEvent e) {
            store();
        }

        public String getTitle() {
            return PicEllipse.this.getName();
        }
    }
}
