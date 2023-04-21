package higraph.view;

import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.util.Iterator;
import tm.backtrack.*;
import tm.utilities.Assert;
import higraph.model.interfaces.*;

/**
 * ZoneView represents mouse sensitive areas that have no counterparts in the
 * original model. They are used to permit mouse detection in areas rather
 * than of model components. For example, the region between two child nodes
 * could be a ZoneView object that responds to mouseovers by showing a
 * cursor between the children showing a node could be dropped there.
 * 
 * Such specific requirements should be realized by specializing the class.
 * In the example above , the drawArea method would have to be overloaded
 * to show the cursor.
 *
 * @param NP The Node Payload type
 * @param EP The Edge Payload type
 * @param HG The Higraph type 
 * @param WG The Whole Graph type
 * @param SG The Subgraph type
 * @param N  The Node type
 * @param E  The Edge type
 * 
 * @author mpbl
*/
public abstract class ZoneView<NP extends Payload<NP>, EP extends Payload<EP>, HG extends Higraph<NP, EP, HG, WG, SG, N, E>, WG extends WholeGraph<NP, EP, HG, WG, SG, N, E>, SG extends Subgraph<NP, EP, HG, WG, SG, N, E>, N extends Node<NP, EP, HG, WG, SG, N, E>, E extends Edge<NP, EP, HG, WG, SG, N, E>> extends ComponentView<NP, EP, HG, WG, SG, N, E> {

    /** Standard Reference point positions for ZoneViews wrt to Shape of their associatedComponent.
	 */
    public static final int CENTER = 0;

    public static final int EAST = 1;

    public static final int NORTHEAST = 2;

    public static final int NORTH = 3;

    public static final int NORTHWEST = 4;

    public static final int WEST = 5;

    public static final int SOUTHWEST = 6;

    public static final int SOUTH = 7;

    public static final int SOUTHEAST = 8;

    public static final int POSITION_MIN = CENTER;

    public static final int POSITION_MAX = SOUTHEAST;

    public static final int PORT = 1;

    public static final int PORTSTERN = 2;

    public static final int STERN = 3;

    public static final int STARBOARDSTERN = 4;

    public static final int STARBOARD = 5;

    public static final int STARBOARDBOW = 6;

    public static final int BOW = 7;

    public static final int PORTBOW = 8;

    protected final BTVar<RectangularShape> shapeVar;

    protected final BTVar<RectangularShape> nextShapeVar;

    protected final BTVar<ComponentView<NP, EP, HG, WG, SG, N, E>> associatedComponentVar;

    /** The reference point position (see above) which is combined with a nudgePoint
     *  (see below) to specify the ZoneView's position wrt to its associated ComponentView.
     * Layout of ZoneView wrt to that position varies and is controlled by the component.
     * For example a ZoneView placed at the East position might be layed out centered
     * vertically, but with its left edge, on the specified point
     */
    protected final BTVar<Integer> placementVar;

    /**
     * The nudgePoint is used as an offset from the standard reference point to produce
     * the actual reference point for the ZoneView
     */
    protected final BTVar<Point2D.Double> nudgeVar;

    protected ZoneView(ComponentView<NP, EP, HG, WG, SG, N, E> view, BTTimeManager timeMan) {
        super(view.getHigraphView(), timeMan);
        shapeVar = new BTVar<RectangularShape>(timeMan);
        nextShapeVar = new BTVar<RectangularShape>(timeMan, view.getHigraphView().getDefaultZoneShape());
        associatedComponentVar = new BTVar<ComponentView<NP, EP, HG, WG, SG, N, E>>(timeMan, view);
        placementVar = new BTVar<Integer>(timeMan);
        nudgeVar = new BTVar<Point2D.Double>(timeMan, new Point2D.Double());
    }

    public ComponentView<NP, EP, HG, WG, SG, N, E> getAssociatedComponent() {
        return associatedComponentVar.get();
    }

    public int getPlacement() {
        return placementVar.get();
    }

    public void setPlacement(int p) {
        placementVar.set(new Integer(p));
        getAssociatedComponent().moveZone(this);
    }

    public Point2D.Double getNudge() {
        return nudgeVar.get();
    }

    public void setNudge(double dx, double dy) {
        nudgeVar.set(new Point2D.Double(dx, dy));
        getAssociatedComponent().moveZone(this);
    }

    @Override
    public Shape getShape() {
        return shapeVar.get() == null ? null : (Shape) shapeVar.get().clone();
    }

    @Override
    public void doTransition() {
        Assert.check(nextShapeVar.get() != null, "trying to update to a null nextShape");
        shapeVar.set((RectangularShape) (nextShapeVar.get().clone()));
    }

    /**
	 * Default is true, override for componentViews for which it is false
	 * 
	 * @return true if labels may be added to this component, false otherwise
	 */
    @Override
    public boolean mayAddLabels() {
        return false;
    }

    /*****************************************
	 *  implementation of Layable interface
	 *  */
    public boolean isPinned() {
        return false;
    }

    public RectangularShape getNextShape() {
        return (RectangularShape) nextShapeVar.get().clone();
    }

    /** 
	 * @param shape must be a RectangularShape
	 */
    @Override
    public void setNextShape(Shape shape) {
        Assert.check(shape instanceof RectangularShape);
        RectangularShape newShape = (RectangularShape) ((RectangularShape) shape).clone();
        nextShapeVar.set(newShape);
    }

    public void translateNext(double dx, double dy) {
        RectangularShape newShape = getNextShape();
        Rectangle2D r = newShape.getFrame();
        newShape.setFrame(r.getMinX() + dx, r.getMinY() + dy, r.getWidth(), r.getHeight());
        nextShapeVar.set(newShape);
        super.translateNext(dx, dy);
    }

    protected ZoneView<NP, EP, HG, WG, SG, N, E> getThis() {
        return this;
    }

    public String toString() {
        return "zoneView";
    }
}
