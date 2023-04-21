package de.hpi.eworld.networkview.objects;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import org.apache.log4j.Logger;
import de.hpi.eworld.gui.util.Geometry;
import de.hpi.eworld.gui.util.Point2DUtils;
import de.hpi.eworld.model.db.data.EdgeModel;
import de.hpi.eworld.model.db.data.GlobalPosition;
import de.hpi.eworld.model.db.data.ModelElement;
import de.hpi.eworld.model.db.data.event.EdgeLocationModel;
import de.hpi.eworld.model.db.data.event.RoadEventModel;
import de.hpi.eworld.networkview.GraphController;
import de.hpi.eworld.networkview.GraphUtils;
import de.hpi.eworld.networkview.settings.NetworkViewSettings;

public class RoadEventView extends GraphicsView<RoadEventModel> {

    private static final transient Logger logger = Logger.getLogger(RoadEventView.class);

    private static final transient int DEFAULT_Z_LEVEL = 20;

    private static double SOME_WEIRD_MAGIC_EPSILON = 2;

    /**
	 * Constructs a RoadEventView with the given underlying road event
	 * 
	 * @param roadEvent
	 *            Underlying road event from database
	 */
    public RoadEventView(final RoadEventModel roadEvent) {
        this(roadEvent, new EdgeLocationModel(null, 0, 0));
    }

    /**
	 * Constructs a RoadEventView with the given road event and edge location
	 * 
	 * @param event
	 *            The RoadEventModel for this Item.
	 * @param location
	 *            The EdgeLocationModelModel to use for positioning this Item.
	 * @author Bernd Schaeufele
	 */
    public RoadEventView(final RoadEventModel event, final EdgeLocationModel location) {
        super(event, resourceNameFromType(event), true, false);
    }

    @Override
    protected void initializeZLevel() {
        setZLevel(DEFAULT_Z_LEVEL);
    }

    @Override
    public Rectangle2D getInitialBounds() {
        if ((getModelElement() != null) && (getModelElement().getLocation() != null)) {
            final EdgeLocationModel edgeLocation = (EdgeLocationModel) getModelElement().getLocation();
            final List<EdgeModel> edgeList = edgeLocation.getEdges();
            if ((edgeList != null) && (edgeList.size() != 0)) {
                EdgeModel edge = edgeList.get(0);
                GlobalPosition edgeStartGlobal = edge.getFromNode().getPosition();
                GlobalPosition edgeEndGlobal = edge.getToNode().getPosition();
                double globalLenght = edgeEndGlobal.distanceTo(edgeStartGlobal);
                double distanceToStart = edgeLocation.getDistance();
                Point2D edgeStartLocal = edge.getFromNode().getPosition().projected();
                Point2D edgeEndLocal = edge.getToNode().getPosition().projected();
                double localLength = Point2DUtils.distance(edgeEndLocal, edgeStartLocal);
                Point2D edgeVector = Point2DUtils.divide(Point2DUtils.subtract(edgeEndLocal, edgeStartLocal), localLength);
                double stretchFactor = distanceToStart * localLength / globalLenght;
                Point2D centerPosition = Point2DUtils.add(edgeStartLocal, Point2DUtils.multiply(edgeVector, stretchFactor));
                Point2D topLeft = Point2DUtils.subtract(centerPosition, new Point2D.Double(getWidth() / 2, getHeight() / 2));
                return new Rectangle2D.Double(topLeft.getX(), topLeft.getY(), getWidth(), getHeight());
            }
        }
        logger.error("Could not calculate initial position for: " + this);
        return new Rectangle2D.Double();
    }

    protected void doDeepCloneFrom(final RoadEventView origin) {
        final RoadEventView originalItem = (RoadEventView) origin;
        setModelElement((RoadEventModel) originalItem.getModelElement().clone());
        setPixmap(originalItem.getPixmap());
    }

    /**
	 * Finds all edges in a list that are affected by 
	 * a given ViewItem
	 */
    private EdgeModel findAffectedEdges(WayView item, List<EdgeModel> edges) {
        EdgeModel edge = null;
        Point2D oldPoint = null;
        for (final EdgeModel e : edges) {
            final Point2D point = e.getToNode().getPosition().projected();
            if (oldPoint != null) {
                final Line2D line = new Line2D.Double(oldPoint, point);
                if (Geometry.isPointNearLine(getAffectionPosition(), line, SOME_WEIRD_MAGIC_EPSILON)) {
                    edge = e;
                }
            }
            oldPoint = point;
        }
        return edge;
    }

    @Override
    public void propagatePosition() {
        final EdgeLocationModel location = (EdgeLocationModel) getModelElement().getLocation();
        final Ellipse2D rect = new Ellipse2D.Double(getPosition().getX(), getPosition().getY(), 2 * SOME_WEIRD_MAGIC_EPSILON, 2 * SOME_WEIRD_MAGIC_EPSILON);
        final List<WayView> ways = GraphUtils.getWaysIn(graphController, rect);
        EdgeModel foundForwardEdge = null;
        EdgeModel foundBackwardEdge = null;
        EdgeModel chosenEdge = null;
        for (final WayView view : ways) {
            final List<EdgeModel> forwardEdges = ((WayView) view).getModelElement().getForwardEdges();
            foundForwardEdge = findAffectedEdges(view, forwardEdges);
            final List<EdgeModel> backwardEdges = ((WayView) view).getModelElement().getBackwardEdges();
            foundBackwardEdge = findAffectedEdges(view, backwardEdges);
        }
        chosenEdge = foundBackwardEdge;
        if (foundBackwardEdge == null) {
            if (foundForwardEdge == null) {
                return;
            }
            chosenEdge = foundForwardEdge;
        }
        location.resetEdges();
        location.addEdge(chosenEdge);
        location.setDistance(GlobalPosition.from(getPosition()).distanceTo(chosenEdge.getFromNode().getPosition()));
        getModelElement().setLocation(location);
    }

    /**
	 * Takes an position and adds edge information to the RoadEventModel ModelElement 
	 * 
	 * @param position position in networkView relative coordinates (without eventual scrolling)
	 * @return adjusted ModelElement attribute
	 * @author 'Martin Horst Boissier'
	 * FIXME take the nearest edge
	 */
    @Override
    public ModelElement getAdjustedAssociatedElement(Point2D position, GraphController graphController) {
        RoadEventModel modelElement = (RoadEventModel) getModelElement().clone();
        if (this.graphController == null) this.graphController = graphController;
        EdgeLocationModel edgeLocation = findEdgeLocationAt(graphController, position);
        if (edgeLocation != null) {
            modelElement.setLocation(edgeLocation);
            return modelElement;
        }
        return null;
    }

    private EdgeLocationModel findEdgeLocationAt(GraphController controller, Point2D scrolledLocalPoint) {
        final List<WayView> views = GraphUtils.getWaysInVicinity(this, controller, scrolledLocalPoint);
        for (final WayView way : views) {
            return createEdgeLocation(way, scrolledLocalPoint);
        }
        return null;
    }

    @Override
    public void modelChanged() {
    }

    /**
	 * creates a EdgeLocation -> this seems to be a container which is used to manage the associated Edges of an EdgeModel
	 */
    private EdgeLocationModel createEdgeLocation(WayView view, Point2D position) {
        EdgeModel edge = GraphUtils.getNearestEdgeOutOf(position, view.getEdges());
        if (edge == null) {
            return null;
        }
        GlobalPosition edgeStartGlobal = edge.getFromNode().getPosition();
        GlobalPosition edgeEndGlobal = edge.getToNode().getPosition();
        Point2D edgeStartLocal = edgeStartGlobal.projected();
        Point2D edgeEndLocal = edgeEndGlobal.projected();
        double globalLenght = edgeEndGlobal.distanceTo(edgeStartGlobal);
        double localLength = Point2DUtils.distance(edgeEndLocal, edgeStartLocal);
        double distanceToStart = Point2DUtils.distance(position, edgeStartLocal);
        double realtiveEdgePosition = distanceToStart / localLength;
        if (realtiveEdgePosition < 0.0d || realtiveEdgePosition > 1.0d) {
            logger.warn("Attaching edge location to edge failed due to an out of range position");
            return null;
        }
        double globalDistance = globalLenght * (distanceToStart / localLength);
        return initializeEdgeLocation(edge, globalDistance);
    }

    @Override
    public boolean isDroppableAt(Point2D scrolledLocalPoint, GraphController controller) {
        return !GraphUtils.getWaysInVicinity(this, controller, scrolledLocalPoint).isEmpty();
    }

    /**
	 * creates a EdgeLocationModel for the RoadEvent<br/>
	 * the EdgeLocationModel allows to influence the properties of an edge during simulation 
	 * @param edge - the nearest edge
	 * @param distanceToStartPoint - the distance to the edge start point
	 * @return
	 */
    private EdgeLocationModel initializeEdgeLocation(EdgeModel edge, double distanceToStartPoint) {
        EdgeLocationModel edgeLocation = new EdgeLocationModel();
        edgeLocation.addEdge(edge);
        edgeLocation.setDistance(distanceToStartPoint);
        return edgeLocation;
    }

    @Override
    public boolean isIntersecting(Shape searchArea) {
        return false;
    }

    @Override
    public String toString() {
        return "Road Event";
    }

    @Override
    protected boolean isVisibleAtCurrentLevelOfDetail() {
        return currentLevelOfDetail() >= NetworkViewSettings.GENERAL_LOD_MEDIUM;
    }
}
