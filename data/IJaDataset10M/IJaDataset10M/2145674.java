package net.sourceforge.code2uml.graph;

import java.awt.Color;
import java.awt.Point;
import java.util.Collection;
import java.util.LinkedList;
import javax.swing.JComponent;
import net.sourceforge.code2uml.util.MouseDragListener;

/**
 * An abstract graphics component that represents a class/enum/interface on an
 * UML diagram. Defines four abstract methods that add information about represented
 * class/unit/enum. It depends on subclasses if and how they store and 
 * display given information. To display a NodeComponent simply add it
 * to a Container (NodeComponent extends JComponent). <br/><br/>
 *
 * Note: Whenever any information is added, subclasses should auto-resize to
 * make sure that it can be displayed correctly.
 *
 * @author Mateusz Wenus
 */
public abstract class NodeComponent extends JComponent {

    /**
     * Horizontal padding of text within NodeComponent and its subclasses.
     */
    protected static final int padding = 5;

    private static final int pointPadding = 3;

    private String unitName;

    private Collection<EdgeComponent> edges = new LinkedList<EdgeComponent>();

    private Collection<EdgeComponent> incomingEdges = new LinkedList<EdgeComponent>();

    /**
     * Background color of this node.
     */
    protected Color backColor;

    /** 
     * Creates a new instance of NodeComponent 
     *
     * @param backColor background color of this node on UML diagram
     * @param draggable true if this node should allow to be moved by mouse
     */
    public NodeComponent(Color backColor, boolean draggable) {
        this.backColor = backColor;
        if (draggable) addMouseListener(new MouseDragListener(this));
    }

    /**
     * Returns coordinates of point in the middle of this node.
     *
     * @return coordinates of point in the middle of this node
     */
    public Point getMiddle() {
        return new Point(getX() + getWidth() / 2, getY() + getHeight() / 2);
    }

    /**
     * Returns coordinates of point in the middle of top edge of this node.
     *
     * @return coordinates of point in the middle of top edge of this node
     */
    public Point getTopMiddle() {
        return new Point(getX() + getWidth() / 2, getY() - pointPadding);
    }

    /**
     * Returns coordinates of point in the middle of bottom edge of this node.
     *
     * @return coordinates of point in the middle of bottom edge of this node
     */
    public Point getBottomMiddle() {
        return new Point(getX() + getWidth() / 2, getY() + getHeight() + pointPadding);
    }

    /**
     * Returns coordinates of point in the middle of left edge of this node.
     *
     * @return coordinates of point in the middle of left edge of this node
     */
    public Point getLeftMiddle() {
        return new Point(getX() - pointPadding, getY() + getHeight() / 2);
    }

    /**
     * Returns coordinates of point in the middle of right edge of this node.
     *
     * @return coordinates of point in the middle of right edge of this node
     */
    public Point getRightMiddle() {
        return new Point(getX() + getWidth() + pointPadding, getY() + getHeight() / 2);
    }

    /**
     * Returns qualified name of class/interface/enum represented by this node.
     *
     * @return qualified name of class/interface/enum represented by this node
     */
    public String getUnitName() {
        return unitName;
    }

    /**
     * Sets qualified name of class/interface/enum represented by this node.
     * @param name name of class/interface/enum represented by this node
     */
    public void setUnitName(String name) {
        this.unitName = name;
    }

    /**
     * Returns edges that start in this node.
     *
     * @return edges that start in this node
     */
    public Collection<EdgeComponent> getOutEdges() {
        return edges;
    }

    /**
     * Returns edges that end in this node.
     *
     * @return edges that end in this node
     */
    public Collection<EdgeComponent> getInEdges() {
        return incomingEdges;
    }

    /**
     * Adds a new edge that starts in this node. Automatically adds this edge
     * to its other end.
     *
     * @param edge edge to add, all its fields should have proper values (are not
     *        modified by this method)
     */
    public void addEdge(EdgeComponent edge) {
        edges.add(edge);
        edge.getTo().getInEdges().add(edge);
    }

    /**
     * Adds general information about represented class/interface/enum, for
     * example its name. 
     *
     * @param str information
     */
    public abstract void addToName(String str);

    /**
     * Adds information about an enum value that represented enum has. 
     *
     * @param str information
     */
    public abstract void addToEnum(String str);

    /**
     * Adds information about a field which represented class/interface/enum has.
     *
     * @param str information
     */
    public abstract void addToField(String str);

    /**
     * Adds information about a method which represented class/interface/enum has.
     *
     * @param str information
     */
    public abstract void addToMethod(String str);
}
