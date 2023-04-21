package scaffoldhunter.vis.view;

import java.util.*;
import java.awt.Color;
import java.awt.geom.Point2D;
import scaffoldhunter.vis.VISControl;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolox.util.PFixedWidthStroke;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;

/**
 * This layout is a modified radial layout which is based 
 * on the paper 'Drawing Free Trees' from Peter Eades
 * 
 * @author Gorecki, Kriege, Schrader, Wiesniewski
 */
public class VRadialLayout extends VLayout {

    /**
	 * <code>circlelayer</code> is a special layer with circles that underlines 
	 * the alignment of nodes.
	 */
    private PLayer circlelayer;

    /**	  
	 *<code>minimum_distance</code> is the minimum distance between to nodes, so 
	 *that they do not overlap. 
	 */
    private double minimum_distance;

    /**	  
	 *<code>maximum_SVG_size</code> is the maximum size of all SVGs in the actual
	 *VTree. 
	 */
    private double maximum_SVG_size;

    /**	  
	 *<code>radiusAdd</code> is the value that will be added to the actual 
	 *radius to draw the next layer of nodes.
	 */
    protected double radiusAdd = vtree.getInitalMRA();

    /**
	 * If this flag is <b>true</b> the radii are fixed and will not scale while 
	 * zooming.
	 */
    protected boolean fixedRA;

    /**	  
	 *<code>radii</code> is an ArrayList, that saves the radius for each layer of nodes.
	 */
    protected ArrayList<Double> radii;

    /**
	 * <code>firstRadius</code> saves the radius of the first layer of nodes.
	 */
    protected double firstRadius;

    /**	  
	 *<code>angles</code> is a HashMap, that saves the angles for each nodes.
	 */
    protected HashMap<Integer, Sector> angles;

    private ArrayList<VNode> separators;

    private ArrayList<CircularSector> sectors;

    /**
	 * Creates a new VRadialLayout for the <code>vtree</code>
	 * @param vtree that will use this layout
	 */
    public VRadialLayout(VTree vtree) {
        super(vtree);
        radii = new ArrayList<Double>();
        angles = new HashMap<Integer, Sector>();
        circlelayer = new PLayer();
        separators = new ArrayList<VNode>();
        sectors = new ArrayList<CircularSector>();
        fixedRA = false;
        firstRadius = 0;
    }

    /**
	 * The radial layout returns a special layer with circles that underlines 
	 * the alignment of nodes and divides the first ring in sectors.
	 * @return a layer that will be displayed under all nodes and edges.
	 */
    public PLayer getBackgroundLayer() {
        return circlelayer;
    }

    /**
	 * Calculates and draws the layout of the nodes.
	 * Draws also the special layer that underlines the alignment of nodes.
	 */
    public void drawLayout() {
        vtree.countSubTreeLeaves(vtree.getRoot());
        radii.clear();
        angles.clear();
        maximum_SVG_size = vtree.getRoot().getSVGSize();
        calculateLayout(vtree.getRoot(), 0, 0, Math.PI * 2);
        for (int i = radii.size(); i > 2; i--) {
            radii.set(i - 2, radii.get(i - 1) - radiusAdd);
        }
        drawNodes(vtree.getRoot());
        drawCircles();
    }

    ;

    /**
	 * This method calculates the angle for all nodes in the subtree
	 * under the given node. It also recalculates the radius for each layer
	 * that the nodes do not overlap.
	 * @param Vnode v, the root of the subtree
	 * @param radius, the actual radius for the layer where VNode v is
	 * @param angle1, angle2, they describe the cone of the subtree in which
	 * the nodes will be drawn
	 */
    private void calculateLayout(VNode v, double radius, double angle1, double angle2) {
        angles.put(v.getScaffoldID(), new Sector(angle1, angle2));
        double s;
        double alpha;
        if ((vtree.getRoot() == v) && v.getTreeChildren().size() == 1) {
            s = (Math.PI / 2) / (v.getNumLeaves());
            alpha = angle1;
        } else {
            s = (angle2 - angle1) / v.getNumLeaves();
            alpha = angle1;
        }
        double new_radius;
        int depth = vtree.getNodesDepth(v);
        if (radii.size() <= depth) {
            radii.add(depth, radius);
        } else if (radii.get(depth) < radius) {
            radii.set(depth, radius);
        }
        for (VNode n : v.getTreeChildren()) {
            maximum_SVG_size = Math.max(n.getSVGSize(), maximum_SVG_size);
        }
        minimum_distance = maximum_SVG_size + 5;
        new_radius = minimum_distance / (2 * (Math.sin((s / 2))));
        double temp_radius = radius + radiusAdd;
        if (new_radius < temp_radius) new_radius = temp_radius;
        for (int i = 0; i < v.getChildCount(); i++) {
            VNode u = v.getTreeChildren().get(i);
            calculateLayout(u, new_radius, alpha, alpha + (s * u.getNumLeaves()));
            alpha = alpha + (s * u.getNumLeaves());
        }
    }

    /**
	 * This method calculates the x and y coordinates for each node of
	 * the tree with the root <code>v</code> and draws them at this point.
	 * @param v the root of the tree
	 */
    public void drawNodes(VNode v) {
        if (vtree.getRoot() == v) {
            double rootSVGSize = v.getSVGSize();
            if (radii.size() >= 2) firstRadius = radii.get(1);
            if (firstRadius == 0) firstRadius = 1;
            double factor = (firstRadius / rootSVGSize) - 1.5;
            factor = Math.max(factor, 1);
            v.setScale(factor);
            Point2D c = v.getFullBoundsReference().getCenter2D();
            v.centerFullBoundsOnPoint(c.getX(), c.getY());
            if (VISControl.getInstance().getHideSubtreeEdges()) {
                for (VEdge e : v.getEdges()) e.setVisible(false);
            }
        }
        Sector sec = getSector(v);
        double angle = (sec.getStartAngle() + sec.getEndAngle()) / 2;
        double x = Math.cos(angle) * radii.get(vtree.getNodesDepth(v));
        double y = Math.sin(angle) * radii.get(vtree.getNodesDepth(v));
        centerNodeOn(v, new Point2D.Double(x, y));
        for (int i = 0; i < v.getChildCount(); i++) {
            VNode u = v.getTreeChildren().get(i);
            drawNodes(u);
        }
    }

    /**
	 * This method draws the circles on which the nodes are lying.
	 */
    public void drawCircles() {
        ArrayList<PNode> tmp = new ArrayList<PNode>();
        for (Object p : circlelayer.getChildrenReference()) if (p instanceof PPath) tmp.add((PPath) p);
        circlelayer.removeChildren(tmp);
        int depth = vtree.getMaxDepth(vtree.getRoot(), 0);
        double tempradius;
        for (int i = depth; i > 0; i--) {
            tempradius = radii.get(i) * 2;
            PPath circle = PPath.createEllipse(0, 0, (float) tempradius, (float) tempradius);
            PFixedWidthStroke stroke = new PFixedWidthStroke((float) VISControl.getInstance().getCircleStrokeWidth());
            circle.setStroke(stroke);
            circle.setStrokePaint(VISControl.getInstance().getCircleColor());
            circle.setPaint(null);
            circlelayer.addChild(circle);
            circle.centerFullBoundsOnPoint(0, 0);
        }
    }

    public void setSeparators(ArrayList<VNode> separators, ArrayList<String> caption, ArrayList<Color> colors) {
        this.separators = separators;
        clearSeparators();
        VNode n1, n2;
        for (int i = 0; i < separators.size(); i++) {
            n1 = separators.get(i);
            if (i == separators.size() - 1) n2 = separators.get(0); else n2 = separators.get(i + 1);
            addSector(new CircularSector(n1, n2.getAnticlockwiseSibling(), caption.get(i), colors.get(i), this));
        }
    }

    public void clearSeparators() {
        circlelayer.removeChildren(sectors);
        sectors.clear();
        for (VNode v : vtree.getVNodes().values()) {
            v.setPaint(Color.WHITE);
        }
    }

    private void addSector(CircularSector s) {
        sectors.add(s);
    }

    private boolean separatorsValid() {
        int i = 0;
        ArrayList<VNode> nodes = vtree.getRoot().getTreeChildren();
        for (VNode v : separators) {
            while (i < nodes.size() && nodes.get(i) != v) i++;
        }
        return i < nodes.size();
    }

    public void layoutAnimationFinished() {
        if (!sectors.isEmpty()) {
            if (!separatorsValid()) {
                clearSeparators();
                return;
            }
            for (CircularSector s : sectors) {
                s.updateArc();
                if (!circlelayer.getChildrenReference().contains(s)) {
                    circlelayer.addChild(0, s);
                    s.colorNodesBackground();
                }
            }
        }
    }

    /**
	 * Rescales the radii while zooming.
	 */
    public void updateLayout() {
        double viewScale = vtree.getVCanvas().getCamera().getViewScale();
        if (!fixedRA) {
            double newMRA = 1500 * (1 - Math.min(viewScale, 1)) * (1 - Math.min(viewScale, 1));
            newMRA = Math.max(newMRA, 1.2 * maximum_SVG_size);
            this.setRadiusAdd(newMRA);
            vtree.setInitalMRA(newMRA);
            this.doLayout(true);
        }
    }

    /**
	 * Rescales the radii manually.
	 * @param delta the difference between the old and the new radius
	 */
    public void updateRadii(double delta) {
        radiusAdd += delta;
        radiusAdd = Math.max(radiusAdd, minimum_distance);
        radiusAdd = Math.min(radiusAdd, 10000);
        this.doLayout(true);
    }

    ;

    /**
	 * Sets the flag <b>fixedRA</b>
	 * @param enable
	 */
    public void setFixedLayout(boolean enable) {
        fixedRA = enable;
    }

    /**
	 * Returns the flag <code>fixedRA</code>
	 * @return the boolean value of the flag <code>fixedRA</code> 
	 */
    public boolean getFixedLayout() {
        return fixedRA;
    }

    /**
	 * Sets the <code>radiusAdd</code>. This is the
	 * minimum value that will be added to the actual radius to draw the 
	 * next layer of nodes.
	 * * @param ra, the value that will be added to the actual 
	 * radius to draw the next layer of nodes.
	 */
    public void setRadiusAdd(double ra) {
        radiusAdd = Math.max(ra, minimum_distance);
    }

    /**
	 * Returns the <code>radiusAdd</code>.
	 * @return the value that will be added to the actual radius to draw the 
	 * next layer of nodes.
	 */
    public double getRadiusAdd() {
        return radiusAdd;
    }

    /**
	 * Returns the sector assigned to the node <code>v</code>. All
	 * successors of <code>v</code> will be layed out within this
	 * sector.
	 * @param v
	 */
    public Sector getSector(VNode v) {
        return angles.get(v.getScaffoldID());
    }

    /**
	 * Returns the radius of the outer ring
	 */
    public double getOuterRadius() {
        return radii.get(radii.size() - 1);
    }

    /**
	 * Returns the radius of the inner ring
	 */
    public double getInnerRadius() {
        return radii.get(1);
    }

    /**
	 * A sector defined by two angles
	 */
    public class Sector {

        private double startAngle;

        private double endAngle;

        public Sector(double startAngle, double endAngle) {
            this.startAngle = startAngle;
            this.endAngle = endAngle;
        }

        public double getStartAngle() {
            return startAngle;
        }

        public double getEndAngle() {
            return endAngle;
        }
    }
}
