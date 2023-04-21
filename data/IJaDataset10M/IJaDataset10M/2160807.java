package jpicedt.graphic.model;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import jpicedt.graphic.toolkit.*;
import jpicedt.graphic.*;
import jpicedt.graphic.event.DrawingEvent;
import jpicedt.graphic.view.View;
import jpicedt.graphic.view.HitInfo;
import jpicedt.ui.dialog.UserConfirmationCache;
import jpicedt.widgets.*;
import static java.lang.Math.abs;
import static jpicedt.Log.*;

/**
 * A class implementing either a circle or an arc, whose geometry is specified by a <code>PicEllipse</code>,
 * yet is further controllable by three additional points: this may either be a plain circle going through
 * these three points, or an arc going from <code>P_1</code> to <code>P_3</code> through <code>P_2</code>, in
 * which case a pie or a chord may be added as well.<br> Superclass control-points are still available here,
 * yet geometry is constrained to a circle by invoking e.g. {@link #setCtrlPt super.setCtrlPt} using a {@link
 * EditPointConstraint.EditConstraint#SQUARE SQUARE} <code>EditConstraint</code>.
 * @author Vincent Guirardel
 * @since jPicEdt 1.4
 * @version $Id: PicCircleFrom3Points.java,v 1.35 2011/12/03 21:16:36 vincentb1 Exp $
 */
public class PicCircleFrom3Points extends PicEllipse implements CustomizerFactory {

    /** index of first ctrl-point */
    public static final int P_1 = PicEllipse.LAST_PT + 1;

    /** index of intermediate ctrl-point */
    public static final int P_2 = P_1 + 1;

    /** index of last ctrl-point */
    public static final int P_3 = P_1 + 2;

    public static final int LAST_PT = P_3;

    /**
	 * The first point (out of three) this circle goes through, aka circle's specification point.
	 * It is controlled by the {@link #setCtrlPt setCtrlPt} method, and
	 * is used to update superclass's geometry when {@link #updateEllipse updateEllipse} is invoked.
	 */
    protected PicPoint circlePt1 = new PicPoint();

    /** second spec-point, same as circlePt1 */
    protected PicPoint circlePt2 = new PicPoint();

    /** second spec-point, same as circlePt1 */
    protected PicPoint circlePt3 = new PicPoint();

    /**
	 * true if this is a plain circle, false if this is an arc.
	 */
    private boolean isCircle;

    /**
	 * Creates a new circle reduced to a single point located at (0,0).
	 */
    public PicCircleFrom3Points() {
        this(true, Arc2D.OPEN);
    }

    /**
	 * Creates a new arc or circle reduced to a single point located at (0,0), with a default attribute set
	 * and the given closure type.
	 * @param plain if true, we draw a plain circle and ignore the closure parameter.
	 * @param closure closure type as defined in java.awt.geom.Arc2D
	 */
    public PicCircleFrom3Points(boolean plain, int closure) {
        super(plain ? Arc2D.OPEN : closure);
        isCircle = plain;
    }

    /**
	 * Creates a new (full) circle reduced to a single point located at (0,0), with the given attribute set.
	 */
    public PicCircleFrom3Points(PicAttributeSet set) {
        this(true, Arc2D.OPEN, set);
    }

    /**
	 * Creates a new arc or circle reduced to a single point located at (0,0),
	 * with the given closure and attribute set.
	 * @param plain if true, we draw a plain circle and ignore the closure parameter.
	 * @param closure closure type as defined in java.awt.geom.Arc2D
	 */
    public PicCircleFrom3Points(boolean plain, int closure, PicAttributeSet set) {
        super(plain ? Arc2D.OPEN : closure, set);
        isCircle = plain;
    }

    /**
	 * Create a new circle whose geometry is specificed by the the 3 given points, and
	 * with the given attribute set.
	 */
    public PicCircleFrom3Points(PicPoint pt1, PicPoint pt2, PicPoint pt3, PicAttributeSet set) {
        this(pt1, pt2, pt3, true, Arc2D.OPEN, set);
    }

    /**
	 * Create a new arc or circle whose geometry is specified by the 3 given points, and
	 * with the given attribute set.
	 * @param plain whether to draw a plain circle or an arc
	 * @param closure closure type as defined in <code>java.awt.geom.Arc2D</code>
	 */
    public PicCircleFrom3Points(PicPoint pt1, PicPoint pt2, PicPoint pt3, boolean plain, int closure, PicAttributeSet set) {
        super(plain ? Arc2D.OPEN : closure, set);
        circlePt1.setCoordinates(pt1);
        circlePt2.setCoordinates(pt2);
        circlePt3.setCoordinates(pt3);
        isCircle = plain;
        updateEllipse();
    }

    /**
	 * "cloning" constructor (to be used by clone())
	 */
    public PicCircleFrom3Points(PicCircleFrom3Points circle) {
        super(circle);
        circlePt1.setCoordinates(circle.circlePt1);
        circlePt2.setCoordinates(circle.circlePt2);
        circlePt3.setCoordinates(circle.circlePt3);
        isCircle = circle.isCircle;
    }

    /**
	 * Overrides <code>Object.clone()</code> method
	 */
    public PicCircleFrom3Points clone() {
        return new PicCircleFrom3Points(this);
    }

    /**
	 * Returns the index of the first user-controlled point that can be retrieved by <code>getCtrlPt()</code>.
	 */
    public int getFirstPointIndex() {
        return FIRST_PT;
    }

    /**
	 * Returns the index of the last user-controlled point that can be retrieved by <code>getCtrlPt()</code>.
	 */
    public int getLastPointIndex() {
        return LAST_PT;
    }

    /**
	 * @return A string that represents this object's name ; should be a key-entry to i18n files.
	 * @since jpicedt 1.3.3
	 */
    public String getDefaultName() {
        return jpicedt.Localizer.currentLocalizer().get("model.Circle");
    }

    /**
	 * Convert this circle to a new <code>PicEllipse</code> object.
	 */
    public PicEllipse convertToEllipse() {
        return new PicEllipse(this);
    }

    /**
	 * This method update the geometry of the superclass, i.e. specification points <code>P_BL</code>,
	 * <code>P_BR</code> and <code>P_TL</code>, from the current value of the three control points
	 * <code>P_1</code>, <code>P_2</code>, <code>P_3</code>.<br> Subclasser may want to adapt updating scheme
	 * to their own need here.
	 */
    protected void updateEllipse() {
        PicVector u = new PicVector(circlePt1, circlePt2);
        PicVector v = new PicVector(circlePt1, circlePt3);
        PicPoint m = circlePt1.clone();
        m.middle(circlePt2);
        PicPoint n = circlePt1.clone();
        n.middle(circlePt3);
        if (DEBUG) debug("points m,n,u,v" + m + n + u + v);
        double det = u.det(v);
        if (det == 0) {
            PicPoint extremity1;
            PicPoint extremity2;
            if (u.dot(v) <= 0) {
                extremity1 = circlePt2;
                extremity2 = circlePt3;
            } else {
                PicVector w = new PicVector(circlePt2, circlePt3);
                if (u.dot(w) >= 0) {
                    extremity1 = circlePt1;
                    extremity2 = circlePt3;
                } else {
                    extremity1 = circlePt1;
                    extremity2 = circlePt2;
                }
            }
            ptBL.setCoordinates(extremity1);
            ptBR.setCoordinates(extremity2);
            ptTR.setCoordinates(extremity2);
            setAngleStart(-180);
            setAngleEnd(180);
        } else {
            PicPoint c = new PicPoint((-v.x * n.x * u.y + v.y * u.x * m.x + v.y * u.y * m.y - v.y * n.y * u.y) / det, (u.x * v.x * n.x + u.x * v.y * n.y - u.x * m.x * v.x - u.y * m.y * v.x) / det);
            if (DEBUG) debug("Centre: " + c.x + "," + c.y);
            double R = (new PicVector(c, circlePt1)).norm();
            if (DEBUG) debug("radius" + R);
            PicPoint p = new PicPoint(c.x - R, c.y - R);
            ptBL.setCoordinates(p);
            p.x = c.x + R;
            p.y = c.y - R;
            ptBR.setCoordinates(p);
            p.x = c.x + R;
            p.y = c.y + R;
            ptTR.setCoordinates(p);
            if (isCircle) {
                setAngleStart(-180);
                setAngleEnd(180);
            } else {
                PicVector v1 = new PicVector(c, circlePt1);
                PicVector v2 = new PicVector(c, circlePt2);
                PicVector v3 = new PicVector(c, circlePt3);
                double a1 = PicVector.X_AXIS.angleDegrees(v1);
                double a2 = PicVector.X_AXIS.angleDegrees(v2);
                double a3 = PicVector.X_AXIS.angleDegrees(v3);
                if (a1 <= a2 && a2 <= a3) {
                } else if (a3 <= a2 && a2 <= a1) {
                    double b = a1;
                    a1 = a3;
                    a3 = b;
                } else if (a1 <= a3 && a3 <= a2) {
                    double b = a1;
                    a1 = a3 - 360;
                    a2 = a2 - 360;
                    a3 = b;
                } else if (a3 <= a1 && a1 <= a2) {
                    a1 = a1 - 360;
                    a2 = a2 - 360;
                } else if (a2 <= a3 && a3 <= a1) {
                    a1 = a1 - 360;
                } else if (a2 <= a1 && a1 <= a3) {
                    double b = a1;
                    a1 = a3 - 360;
                    a3 = b;
                }
                setAngleStart(a1);
                setAngleEnd(a3);
            }
        }
        updateAxis();
    }

    /**
	 * Update the values of the three control-points from the geometry of the superclass.
	 * This is aka <code>updateEllipse()</code>, yet the other way around.
	 * @param numPoint index of the <code>PicParallelogram</code>'s control-point that was moved
	 */
    protected void updateControlPoints(int numPoint) {
        warning("Not implemented yet !");
    }

    /**
	 * Set the coordinate of the point indexed by "numPoint" to the given value.
	 */
    public void setCtrlPt(int numPoint, PicPoint pt, EditPointConstraint c) {
        if (numPoint == P_CENTER) {
            PicPoint center = getCtrlPt(P_CENTER, null);
            circlePt1.translate(center, pt);
            circlePt2.translate(center, pt);
            circlePt3.translate(center, pt);
            super.setCtrlPt(P_CENTER, pt, null);
        } else if (numPoint < P_1) {
            BasicEditPointConstraint bepc;
            EditPointConstraint.EditConstraint editConstraint = EditPointConstraint.EditConstraint.SQUARE;
            if (c != null && c instanceof BasicEditPointConstraint) {
                bepc = (BasicEditPointConstraint) c;
                editConstraint = bepc.getEditConstraint();
                bepc.setEditConstraint(EditPointConstraint.EditConstraint.SQUARE);
            } else bepc = BasicEditPointConstraint.SQUARE;
            super.setCtrlPt(numPoint, pt, bepc);
            if (editConstraint != EditPointConstraint.EditConstraint.SQUARE) bepc.setEditConstraint(editConstraint);
            updateControlPoints(numPoint);
        } else {
            switch(numPoint) {
                case P_1:
                    circlePt1.setCoordinates(pt);
                    break;
                case P_2:
                    circlePt2.setCoordinates(pt);
                    break;
                case P_3:
                    circlePt3.setCoordinates(pt);
                    break;
                default:
                    throw new IndexOutOfBoundsException(new Integer(numPoint).toString());
            }
            updateEllipse();
            fireChangedUpdate(DrawingEvent.EventType.GEOMETRY_CHANGE);
        }
    }

    /**
	 * Get the coordinate of the control point indexed by "numPoint".
	 * @param numPoint one of <code>P_1</code>, <code>P_2</code> or <code>P_3</code>.
	 * @param src a <code>PicPoint</code> to update and create if necessary
	 */
    public PicPoint getCtrlPt(int numPoint, PicPoint src) {
        if (numPoint < P_1) {
            return super.getCtrlPt(numPoint, src);
        } else {
            if (src == null) src = new PicPoint();
            switch(numPoint) {
                case P_1:
                    src.setCoordinates(circlePt1);
                    break;
                case P_2:
                    src.setCoordinates(circlePt2);
                    break;
                case P_3:
                    src.setCoordinates(circlePt3);
                    break;
                default:
                    throw new IndexOutOfBoundsException(new Integer(numPoint).toString());
            }
            return src;
        }
    }

    /**
	 * Translate this circle by (dx,dy)
	 */
    public void translate(double dx, double dy) {
        circlePt1.translate(dx, dy);
        circlePt2.translate(dx, dy);
        circlePt3.translate(dx, dy);
        super.translate(dx, dy);
    }

    /**
	 * Scale this object by <code>(sx,sy)</code> using <code>(ptOrgX,ptOrgY)</code> as the origin. This
	 * implementation simply apply a scaling transform to all specification-points.  Note that <code>sx</code>
	 * and <code>sy</code> may be negative.  This method eventually fires a changed-update event.
	 */
    public void scale(double ptOrgX, double ptOrgY, double sx, double sy, UserConfirmationCache ucc) {
        if (abs(sx) == abs(sy)) {
            circlePt1.scale(ptOrgX, ptOrgY, sx, sy);
            circlePt2.scale(ptOrgX, ptOrgY, sx, sy);
            circlePt3.scale(ptOrgX, ptOrgY, sx, sy);
            super.scale(ptOrgX, ptOrgY, sx, sy, ucc);
        } else {
            int ans = ucc.picCircleFrom3PtsScaleHandling.getValue();
            if (ans == JOptionPane.YES_OPTION) {
                PicEllipse el = convertToEllipse();
                el.scale(ptOrgX, ptOrgY, sx, sy, ucc);
                replaceBy(el, ucc.replaceInSelectionHandling.getValue() == JOptionPane.YES_OPTION);
            } else {
                circlePt1.scale(ptOrgX, ptOrgY, sx, sy);
                circlePt2.scale(ptOrgX, ptOrgY, sx, sy);
                circlePt3.scale(ptOrgX, ptOrgY, sx, sy);
                updateEllipse();
            }
        }
    }

    /**
	 * Rotate this <code>Element</code> by the given angle along the given point
	 * @param angle rotation angle in radians
	 */
    public void rotate(PicPoint ptOrg, double angle) {
        circlePt1.rotate(ptOrg, angle);
        circlePt2.rotate(ptOrg, angle);
        circlePt3.rotate(ptOrg, angle);
        super.rotate(ptOrg, angle);
    }

    /**
	 * Effectue une r�flexion sur <code>this</code> relativement � l'axe
	 * d�fini par <code>ptOrg</code> et <code>normalVector</code>.
	 *
	 * @param ptOrg le <code>PicPoint</code> par lequel passe l'axe de r�flexion.
	 * @param normalVector le <code>PicVector</code> normal � l'axe de r�flexion.
	 */
    public void mirror(PicPoint ptOrg, PicVector normalVector) {
        circlePt1.mirror(ptOrg, normalVector);
        circlePt2.mirror(ptOrg, normalVector);
        circlePt3.mirror(ptOrg, normalVector);
        super.mirror(ptOrg, normalVector);
    }

    /**
	 * Shear this <code>Element</code> by the given params wrt to the given origin
	 */
    public void shear(PicPoint ptOrg, double shx, double shy, UserConfirmationCache ucc) {
        if (abs(shx) == abs(shy)) {
            circlePt1.shear(ptOrg, shx, shy);
            circlePt2.shear(ptOrg, shx, shy);
            circlePt3.shear(ptOrg, shx, shy);
            super.shear(ptOrg, shx, shy, ucc);
        } else {
            int ans = ucc.picCircleFrom3PtsShearHandling.getValue();
            if (ans == JOptionPane.YES_OPTION) {
                PicEllipse el = convertToEllipse();
                el.shear(ptOrg, shx, shy);
                replaceBy(el, ucc.replaceInSelectionHandling.getValue() == JOptionPane.YES_OPTION);
            } else {
                circlePt1.shear(ptOrg, shx, shy);
                circlePt2.shear(ptOrg, shx, shy);
                circlePt3.shear(ptOrg, shx, shy);
                updateEllipse();
            }
        }
    }

    /**
	 * @see jpicedt.graphic.model#Element.getCtrlPtSubset(ConvexZoneGroup csg,BitSet czExtension)
	 * @param csg l'ensemble de zones convexes <code>ConvexZoneGroup</code> auquel l'appartenance on teste les
	 * points de contr�le.
	 * @param czExtension cet argument est ignor�
	 * @return a <code>CtrlPtSubset</code> value
	 * @since jPicEdt 1.6
	 */
    public CtrlPtSubset getCtrlPtSubset(ConvexZoneGroup csg, BitSet czExtension) {
        int bitmap = 0;
        if (csg.containsPoint(circlePt1)) bitmap = 1;
        if (csg.containsPoint(circlePt2)) bitmap |= 2;
        if (csg.containsPoint(circlePt3)) bitmap |= 4;
        if (bitmap == 0) return null;
        if (bitmap == 7) return new CtrlPtSubsetPlain(this);
        PicPoint inCZPoint1;
        PicPoint inCZPoint2 = null;
        if ((bitmap & 1) != 0) {
            inCZPoint1 = circlePt1;
            if ((bitmap & 2) != 0) inCZPoint2 = circlePt2; else if ((bitmap & 4) != 0) inCZPoint2 = circlePt3;
        } else if ((bitmap & 2) != 0) {
            inCZPoint1 = circlePt2;
            if ((bitmap & 4) != 0) inCZPoint2 = circlePt3;
        } else inCZPoint1 = circlePt3;
        return new CtrlPtSubsetCircleFrom3Points(this, inCZPoint1, inCZPoint2);
    }

    class CtrlPtSubsetCircleFrom3Points implements CtrlPtSubset {

        PicCircleFrom3Points cf3p;

        PicPoint inCZPoint1;

        PicPoint inCZPoint2;

        public CtrlPtSubsetCircleFrom3Points(PicCircleFrom3Points cf3p, PicPoint inCZPoint1, PicPoint inCZPoint2) {
            this.cf3p = cf3p;
            this.inCZPoint1 = inCZPoint1;
            this.inCZPoint2 = inCZPoint2;
        }

        public void translate(double dx, double dy) {
            inCZPoint1.translate(dx, dy);
            if (inCZPoint2 != null) inCZPoint2.translate(dx, dy);
            cf3p.updateEllipse();
            cf3p.fireChangedUpdate(DrawingEvent.EventType.GEOMETRY_CHANGE);
        }
    }

    public boolean isPlain() {
        return isCircle;
    }

    public void setPlain(boolean b) {
        this.isCircle = b;
        updateEllipse();
    }

    /**
	 * Implementation of the Object.toString() method, used for debugging purpose
	 * @since PicEdt 1.1.4
	 */
    public String toString() {
        String s = super.toString() + "\n\t ctrlPts=" + circlePt1 + " " + circlePt2 + " " + circlePt3;
        return s;
    }

    /**
	 * Create an array of <code>Action</code>'s related to this object
	 * @param actionDispatcher  dispatches events to the proper <code>PECanvas</code>
	 * @param localizer         i18n localizer for PEAction's
	 */
    public ArrayList<PEAction> createActions(ActionDispatcher actionDispatcher, ActionLocalizer localizer, HitInfo hi) {
        ArrayList<PEAction> l = super.createActions(actionDispatcher, localizer, hi);
        if (l == null) l = new ArrayList<PEAction>();
        l.add(new ConvertToEllipseAction(actionDispatcher, localizer));
        return l;
    }

    /**
	 * Convert this circle to an ellipse, selecting it if applicable.
	 * @author    Sylvain Reynal
	 * @since     jpicedt 1.4
	 */
    class ConvertToEllipseAction extends PEAction {

        public static final String KEY = "action.editorkit.ConvertCircleToEllipse";

        public ConvertToEllipseAction(ActionDispatcher actionDispatcher, ActionLocalizer localizer) {
            super(actionDispatcher, KEY, localizer);
        }

        public void undoableActionPerformed(ActionEvent e) {
            PicEllipse curve = convertToEllipse();
            Drawing dr = getDrawing();
            if (dr != null) {
                dr.replace(PicCircleFrom3Points.this, curve);
                View view = curve.getView();
                if (view != null) {
                    PECanvas canvas = view.getContainer();
                    if (canvas != null) canvas.select(curve, PECanvas.SelectionBehavior.INCREMENTAL);
                }
            }
        }
    }

    /**
	 * Return a Customizer for geometry editing
	 * @since jpicedt 1.3.3
	 */
    public AbstractCustomizer createCustomizer() {
        if (cachedCustomizer == null) cachedCustomizer = new Customizer();
        cachedCustomizer.load();
        return cachedCustomizer;
    }

    private Customizer cachedCustomizer = null;

    /**
	 * Geometry customizer
	 */
    class Customizer extends AbstractCustomizer implements ActionListener {

        private DecimalNumberField pt1XTF, pt1YTF, pt2XTF, pt2YTF, pt3XTF, pt3YTF;

        private JCheckBox fullCircleCB;

        private JComboBox arcTypeList;

        private boolean isListenersAdded = false;

        private ImageIcon[] arcTypeIcons = { PEToolKit.createImageIcon("action.draw.ArcChord"), PEToolKit.createImageIcon("action.draw.ArcOpen"), PEToolKit.createImageIcon("action.draw.ArcPie") };

        public Customizer() {
            super();
            JPanel p = new JPanel(new GridLayout(6, 3, 5, 5));
            p.add(PEToolKit.createJLabel("action.draw.CircleFrom3Pts"));
            p.add(new JLabel("x"));
            p.add(new JLabel("y"));
            p.add(new JLabel("1"));
            p.add(pt1XTF = new DecimalNumberField(4));
            p.add(pt1YTF = new DecimalNumberField(4));
            p.add(new JLabel("2"));
            p.add(pt2XTF = new DecimalNumberField(4));
            p.add(pt2YTF = new DecimalNumberField(4));
            p.add(new JLabel("3"));
            p.add(pt3XTF = new DecimalNumberField(4));
            p.add(pt3YTF = new DecimalNumberField(4));
            p.add(new JLabel("Plain :"));
            p.add(fullCircleCB = new JCheckBox());
            p.add(new JLabel(" "));
            arcTypeList = PEToolKit.createComboBox(arcTypeIcons);
            p.add(new JLabel("Arc :"));
            p.add(arcTypeList);
            p.add(new JLabel(" "));
            add(p, BorderLayout.NORTH);
            setPreferredSize(new Dimension(400, 300));
        }

        /** add action listeners to widgets to reflect changes immediately */
        private void addActionListeners() {
            if (isListenersAdded) return;
            pt1XTF.addActionListener(this);
            pt1YTF.addActionListener(this);
            pt2XTF.addActionListener(this);
            pt2YTF.addActionListener(this);
            pt3XTF.addActionListener(this);
            pt3YTF.addActionListener(this);
            fullCircleCB.addActionListener(this);
            arcTypeList.addActionListener(this);
            isListenersAdded = true;
        }

        private void removeActionListeners() {
            if (!isListenersAdded) return;
            pt1XTF.removeActionListener(this);
            pt1YTF.removeActionListener(this);
            pt2XTF.removeActionListener(this);
            pt2YTF.removeActionListener(this);
            pt3XTF.removeActionListener(this);
            pt3YTF.removeActionListener(this);
            fullCircleCB.removeActionListener(this);
            arcTypeList.removeActionListener(this);
            isListenersAdded = false;
        }

        /**
		 * (re)init widgets with Element's properties
		 */
        public void load() {
            removeActionListeners();
            pt1XTF.setValue(circlePt1.x);
            pt1YTF.setValue(circlePt1.y);
            pt2XTF.setValue(circlePt2.x);
            pt2YTF.setValue(circlePt2.y);
            pt3XTF.setValue(circlePt3.x);
            pt3YTF.setValue(circlePt3.y);
            fullCircleCB.setSelected(isPlain());
            if (getArcType() == CHORD) arcTypeList.setSelectedIndex(0); else if (getArcType() == OPEN) arcTypeList.setSelectedIndex(1); else if (getArcType() == PIE) arcTypeList.setSelectedIndex(2);
            addActionListeners();
        }

        /**
		 * update Element's properties
		 */
        public void store() {
            PicPoint p = new PicPoint();
            p.x = pt1XTF.getValue();
            p.y = pt1YTF.getValue();
            circlePt1.setCoordinates(p);
            p.x = pt2XTF.getValue();
            p.y = pt2YTF.getValue();
            circlePt2.setCoordinates(p);
            p.x = pt3XTF.getValue();
            p.y = pt3YTF.getValue();
            circlePt3.setCoordinates(p);
            setPlain(fullCircleCB.isSelected());
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
            updateEllipse();
            fireChangedUpdate(DrawingEvent.EventType.GEOMETRY_CHANGE);
        }

        public void actionPerformed(ActionEvent e) {
            store();
        }

        /**
		 * @return the panel title, used e.g. for Border or Tabpane title.
		 */
        public String getTitle() {
            return PicCircleFrom3Points.this.getName();
        }
    }
}
