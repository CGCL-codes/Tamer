package org.gvt.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.gvt.ChisioMain;
import org.gvt.GraphAnimation;
import org.gvt.LayoutManager;
import org.ivis.layout.Cluster;
import org.ivis.layout.ClusterManager;
import org.ivis.layout.Clustered;
import org.ivis.layout.LGraphObject;
import org.ivis.layout.LNode;
import org.ivis.layout.Updatable;

/**
 * This class implements a node in a graph.  A node can have its own color,
 * border color, label/text, source and target edges.
 *
 * @author Cihan Kucukkececi
 * @author Selcuk Onur Sumer
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class NodeModel extends GraphObject implements Updatable, Clustered {

    private CompoundModel parentModel = null;

    protected Rectangle constraint = new Rectangle();

    protected String shape;

    protected List<Cluster> clusters;

    protected Color borderColor;

    protected List sourceConnections = new ArrayList();

    protected List targetConnections = new ArrayList();

    public static String[] shapes = { "Rectangle", "Ellipse", "Triangle" };

    public NodeModel() {
        this(new Rectangle(new Point(0, 0), NodeModel.DEFAULT_SIZE));
    }

    public NodeModel(Point pt) {
        this(new Rectangle(pt, NodeModel.DEFAULT_SIZE));
    }

    public NodeModel(Rectangle rect) {
        this.setConstraint(rect);
        this.text = DEFAULT_TEXT;
        this.textFont = DEFAULT_TEXT_FONT;
        this.textColor = DEFAULT_TEXT_COLOR;
        this.color = DEFAULT_COLOR;
        this.borderColor = DEFAULT_BORDER_COLOR;
        this.shape = DEFAULT_SHAPE;
        this.clusters = new ArrayList<Cluster>();
    }

    public void update(LGraphObject lGraphObj) {
        LNode lNode = (LNode) lGraphObj;
        this.setLocationAbs(new Point(lNode.getRect().x, lNode.getRect().y));
    }

    public void setConstraint(Rectangle rectangle) {
        this.constraint.setLocation(rectangle.getLocation());
        this.constraint.setSize(rectangle.getSize());
        this.firePropertyChange(P_CONSTRAINT, null, this.constraint);
    }

    public Rectangle getConstraint() {
        return this.constraint;
    }

    public void addSourceConnection(EdgeModel connx) {
        this.sourceConnections.add(connx);
        this.firePropertyChange(P_CONNX_SOURCE, null, null);
    }

    public void addTargetConnection(EdgeModel connx) {
        this.targetConnections.add(connx);
        this.firePropertyChange(P_CONNX_TARGET, null, null);
    }

    public void removeSourceConnection(EdgeModel connx) {
        this.sourceConnections.remove(connx);
        this.firePropertyChange(P_CONNX_SOURCE, null, null);
    }

    public void removeTargetConnection(EdgeModel connx) {
        this.targetConnections.remove(connx);
        this.firePropertyChange(P_CONNX_TARGET, null, null);
    }

    public List getSourceConnections() {
        return this.sourceConnections;
    }

    public List getTargetConnections() {
        return this.targetConnections;
    }

    public String getShape() {
        return this.shape;
    }

    public void setShape(String s) {
        this.shape = s;
        this.firePropertyChange(P_SHAPE, null, this.shape);
    }

    public void setBorderColor(Color c) {
        this.borderColor = c;
        this.firePropertyChange(P_BORDERCOLOR, null, this.borderColor);
    }

    public Color getBorderColor() {
        return this.borderColor;
    }

    public NodeModel(Point pt, String str, Color c, String lbl) {
        this(new Rectangle(pt, NodeModel.DEFAULT_SIZE));
        this.setShape(str);
        this.setColor(c);
        this.setText(lbl);
    }

    public CompoundModel getParentModel() {
        return this.parentModel;
    }

    public void setParentModel(CompoundModel parent) {
        this.parentModel = parent;
    }

    /**
	 * This method sets the location of this graph object relative to its
	 * container, if any.
	 *
	 * @param p the new location of this graph object
	 */
    public void setLocation(Point p) {
        this.constraint.setLocation(p);
        this.firePropertyChange(P_CONSTRAINT, null, this.constraint);
        Iterator<Cluster> iter = this.getClusters().iterator();
        while (iter.hasNext()) {
            Cluster cluster = iter.next();
            cluster.calculatePolygon();
        }
    }

    /**
	 * This method sets the absolute location of this graph object; that is,
	 * w.r.t the root container as opposed to the parent container.
	 *
	 * @param p the new location of this graph object
	 */
    public void setLocationAbs(Point p) {
        CompoundModel parent = this.getParentModel();
        if (parent != null && parent instanceof CompoundModel) {
            Point parentLocation = parent.getLocationAbs();
            p.translate(-parentLocation.x, -parentLocation.y);
        }
        this.setLocation(p);
    }

    /**
	 * This method gets the current location of this graph object relative to
	 * its parent container.
	 *
	 * @return current location relative to its parent
	 */
    public Point getLocation() {
        return this.constraint.getLocation();
    }

    /**
	 * This method gets the current absolute location of this graph object; that
	 * is, w.r.t. the root container as opposed to the parent container.
	 *
	 * @return current absolute location
	 */
    public Point getLocationAbs() {
        Point location = this.constraint.getLocation();
        CompoundModel parent = this.getParentModel();
        if (parent != null && parent instanceof CompoundModel) {
            location.translate(parent.getLocationAbs());
        }
        return location;
    }

    public void setSize(Dimension d) {
        this.constraint.setSize(d);
        this.firePropertyChange(P_CONSTRAINT, null, this.constraint);
    }

    public Dimension getSize() {
        return this.constraint.getSize();
    }

    public List getEdgeListToNode(NodeModel to) {
        List<EdgeModel> edgeList = new ArrayList();
        if (this.sourceConnections != null) {
            int i = 0;
            while (i < this.sourceConnections.size()) {
                EdgeModel edge = (EdgeModel) this.sourceConnections.get(i++);
                if (edge.getTarget() == to) {
                    edgeList.add(edge);
                }
            }
        }
        return edgeList;
    }

    public boolean isNeighbor(NodeModel node) {
        if (this.sourceConnections.contains(node) || this.targetConnections.contains(node)) {
            return true;
        }
        return false;
    }

    public List getNeighborsList() {
        List<NodeModel> neigbors = new ArrayList();
        for (int i = 0; i < this.sourceConnections.size(); i++) {
            EdgeModel edge = (EdgeModel) this.sourceConnections.get(i);
            neigbors.add(edge.getTarget());
        }
        for (int i = 0; i < this.targetConnections.size(); i++) {
            EdgeModel edge = (EdgeModel) this.targetConnections.get(i);
            neigbors.add(edge.getSource());
        }
        return neigbors;
    }

    /**
	 * This method returns the string that contains cluster ids separated by "|"
	 */
    public String getClusterIDs() {
        String clusterIDs = "";
        for (Cluster cluster : clusters) {
            clusterIDs = clusterIDs + cluster.getClusterID() + "|";
        }
        if (clusterIDs.length() - 1 > 0) return clusterIDs.substring(0, clusterIDs.length() - 1); else return "0";
    }

    public double getLeft() {
        return this.constraint.x;
    }

    public double getRight() {
        return this.constraint.x + this.constraint.width;
    }

    public double getTop() {
        return this.constraint.y;
    }

    public double getBottom() {
        return this.constraint.y + this.constraint.height;
    }

    public int getCenterX() {
        return this.constraint.getCenter().x;
    }

    public int getCenterY() {
        return this.constraint.getCenter().y;
    }

    public void setPositiveLocation(Rectangle r) {
        this.setConstraint(r);
        Point loc = this.getLocationAbs();
        if (loc.x < 0) {
            loc.x = 0;
        }
        if (loc.y < 0) {
            loc.y = 0;
        }
        this.setLocationAbs(loc);
    }

    public List<Cluster> getClusters() {
        return this.clusters;
    }

    /**
	 * This method add this node model into a cluster with given cluster ID. If
	 * such cluster doesn't exist in ClusterManager, it creates a new cluster.
	 */
    public void addCluster(int clusterID) {
        EClusterManager cm = this.parentModel.getClusterManager();
        Cluster eCluster = cm.getClusterByID(clusterID);
        if (eCluster == null) {
            eCluster = new ECluster(cm, clusterID, "Cluster " + clusterID);
            cm.addCluster(eCluster);
        }
        this.addCluster(eCluster);
    }

    /**
	 * This method adds the given cluster into cluster list of this node model, 
	 * and moreover it adds this node model into set of node models of the given
	 * cluster.
	 */
    public void addCluster(Cluster eCluster) {
        if (!this.clusters.contains(eCluster)) {
            this.clusters.add(eCluster);
            eCluster.getNodes().add(this);
            eCluster.calculatePolygon();
        }
    }

    /**
	 * This method removes the given cluster from cluster list of this node 
	 * model, and moreover it removes this node model from the set of node 
	 * models of the given cluster.
	 */
    public void removeCluster(Cluster eCluster) {
        if (this.clusters.contains(eCluster)) {
            this.clusters.remove(eCluster);
            eCluster.getNodes().remove(this);
            eCluster.calculatePolygon();
        }
    }

    /**
	 * This method resets all cluster information of this node model.
	 * This node model is deleted from all clusters it belongs to.
	 */
    public void resetClusters() {
        for (Cluster cluster : this.clusters) {
            cluster.getNodes().remove(this);
            cluster.calculatePolygon();
        }
        this.clusters.clear();
    }

    public Clustered getParent() {
        return this.parentModel;
    }

    protected void updatePolygonsOfChildren() {
        Iterator<Cluster> clusterIter = this.getClusters().iterator();
        while (clusterIter.hasNext()) {
            Cluster cluster = clusterIter.next();
            cluster.calculatePolygon();
        }
    }

    public boolean hasCommonCluster(NodeModel node) {
        for (Cluster cluster : this.getClusters()) {
            if (node.getClusters().contains(cluster)) {
                return true;
            }
        }
        return false;
    }

    public static Dimension DEFAULT_SIZE = new Dimension(40, 40);

    public static String DEFAULT_TEXT = "Node";

    public static Font DEFAULT_TEXT_FONT = new Font(null, new FontData("Arial", 8, SWT.NORMAL));

    public static Color DEFAULT_TEXT_COLOR = ColorConstants.black;

    public static Color DEFAULT_COLOR = new Color(null, 14, 112, 130);

    public static Color DEFAULT_BORDER_COLOR = new Color(null, 14, 112, 130);

    public static String DEFAULT_SHAPE = shapes[0];

    public static final String P_CONSTRAINT = "_constraint";

    public static final String P_SHAPE = "_shape";

    public static final String P_CLUSTERID = "_clusterID";

    public static final String P_BORDERCOLOR = "_borderColor";

    public static final String P_CONNX_SOURCE = "_connx_source";

    public static final String P_CONNX_TARGET = "_connx_target";
}
