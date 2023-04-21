package cube42.napkin.classDiagram;

import cube42.napkin.interfaces.*;
import cube42.napkin.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This class is used to make a connection between any two data types that
 * support the connectable interface.  For the class diagrams that means
 * notes and class perceptions.  Once the connection is made, the connection can
 * be turned into a number of different types of connections. Currently there
 * are five connections.
 * 1) Extends, this is used when a class has a super class
 * 2) Impliments, this is used when a class implments the interface of another
 *    class.
 * 3) Contains, this connection is used to show that one class cannot exist with
 *    out the classes it contains.  The classes it contains make it what it is.
 * 4) Associated, this connection is used to show that a class uses the other
 *    class as a member field within it.  It is not as powerful as to contain
 *    but, it is not as weak as to depend on the other class.
 * 5) Depends, this just shows that one class uses the other class somehow but,
 *    its not a very strong connection
 *
 * @author Matt Paulin
 * @version 1.0
 * @since JDK 1.2
 */
public class ClassConnection implements MouseSelectionArea, RenderGraphic, ActionListener {

    /**
  * Constant used to set the type of connection to show that one class extends
  * another
  */
    public static final int EXTENDS = 0;

    /**
  * Constant used to set the type of a connection to show that one class
  * implement another classes interface
  */
    public static final int IMPLEMENTS = 1;

    /**
  * Constant used to set the type of a connection to show that one class contains
  * another class.
  */
    public static final int CONTAINS = 2;

    /**
  * Constant used to set the connection to show that two classes are associated
  * with each other.
  */
    public static final int ASSOCIATED = 3;

    /**
  * Constant used to set up a connection to show that one class is dependent on
  * another.
  */
    public static final int DEPENDENT = 4;

    /**
  * The connectable data that this connection is going to.
  */
    private Connectable toClass;

    /**
  * The connectable data that this connection is coming from.
  */
    private Connectable fromClass;

    /**
  * The type of connection it is.  By default it is set to DEPENDENT.
  */
    private int type = DEPENDENT;

    /**
  * A flag used to tell if this connection has been selected.
  */
    private boolean selected = false;

    /**
  * The popup menu used to let the user alter the margins in the drawing area
  */
    public JPopupMenu popup;

    /**
  * This string is used in the popup menu.  It provides text that explains
  * what this selection will turn the type of connection into. In this case
  * the user would see this text if they wanted the connection to extend.
  */
    private static final String EXTEND_SELECTION = "Extends (1)";

    /**
  * This string is used in the popup menu.  It provides text that explains
  * what this selection will turn the type of connection into. In this case
  * the user would see this text if they wanted the connection to implement.
  */
    private static final String IMPLEMENTS_SELECTION = "Implements (2)";

    /**
  * This string is used in the popup menu.  It provides text that explains
  * what this selection will turn the type of connection into. In this case
  * the user would see this text if they wanted the connection to contain.
  */
    private static final String CONTAINS_SELECTION = "Contains (3)";

    /**
  * This string is used in the popup menu.  It provides text that explains
  * what this selection will turn the type of connection into. In this case
  * the user would see this text if they wanted the connection to associate.
  */
    private static final String ASSOCIATES_SELECTION = "Associated with (4)";

    /**
  * This string is used in the popup menu.  It provides text that explains
  * what this selection will turn the type of connection into. In this case
  * the user would see this text if they wanted the connection to show a dependecy.
  */
    private static final String DEPENDENTS_SELECTION = "Depends on (5)";

    /**
  * The length of an arrow head, this is used for the graphics of a association,
  * dependency, and contains.
  */
    private static final int ARROWHEAD_LENGTH = 15;

    /**
  * This is the distance above the class that the extend stub goes too.
  * The extend stub is the line with a filled triangle on top.
  */
    private static final int EXTENDS_STUB_LENGTH = 70;

    /**
  * This is the distance above the class that the implements stub goes too.
  * The implements stub is the line with a hollow triangle on top.
  */
    private static final int IMPLEMENTS_STUB_LENGTH = 50;

    /**
  * This is the length of the triangle on top of a extends stub, or a implements
  * stub.  This number can determine the size of that graphic.
  */
    private static final int INHERIT_TRIANGLE_LENGTH = 20;

    /**
  * This is the width of the selection area around any line.
  */
    private static final int SELECTION_WIDTH = 10;

    /**
  * Constructor for the connection. In it you tell it the two connectable classes
  * that it is connecting together, the type of connection and who to notify
  * when something needs to be redrawn.
  *
  * @param  _type   the type of connection.  The different type are constants in
  *                 this class.
  * @param  _toClass    The class that this connection is going to
  * @param  _fromClass  The class that this connection is coming from
  * @param  parent      The parent panel that can be notified when things need
  *                     to be redrawn.
  * @since JDK 1.2
  */
    public ClassConnection(int _type, Connectable _toClass, Connectable _fromClass, ActionListener parent) {
        type = _type;
        toClass = _toClass;
        fromClass = _fromClass;
        JMenuItem menuItem;
        popup = new JPopupMenu();
        menuItem = new JMenuItem(EXTEND_SELECTION);
        menuItem.setToolTipText("Changes the type of connection so that it shows" + " that one class extends another");
        menuItem.addActionListener(this);
        menuItem.addActionListener(parent);
        popup.add(menuItem);
        menuItem = new JMenuItem(IMPLEMENTS_SELECTION);
        menuItem.setToolTipText("Changes the type of connection so that it shows" + " that one class implement a interface");
        menuItem.addActionListener(this);
        menuItem.addActionListener(parent);
        popup.add(menuItem);
        menuItem = new JMenuItem(CONTAINS_SELECTION);
        menuItem.setToolTipText("Changes the type of connection so that it shows" + " that one class contains another.");
        menuItem.addActionListener(parent);
        menuItem.addActionListener(this);
        popup.add(menuItem);
        menuItem = new JMenuItem(ASSOCIATES_SELECTION);
        menuItem.setToolTipText("Changes the type of connection so that it shows" + " that one class is associated with another.");
        menuItem.addActionListener(this);
        menuItem.addActionListener(parent);
        popup.add(menuItem);
        menuItem = new JMenuItem(DEPENDENTS_SELECTION);
        menuItem.setToolTipText("Changes the type of connection so that it shows" + " that one class depends on another.");
        menuItem.addActionListener(this);
        menuItem.addActionListener(parent);
        popup.add(menuItem);
    }

    /**
  * This is used to determine if a connection starts and ends at the same point
  * if it is it is considered self referencing and needs to have a special
  * drawing and selection area.
  *
  * @return   true if the endpoint and start point are the same
  * @since JDK 1.2
  */
    public boolean isSelfRefencing() {
        return toClass.getConnectionPoint().equals(fromClass.getConnectionPoint());
    }

    /**
  * Set the type of the connection.  Use the constants contained in this class
  *
  * @param    newType   the type to set the connection to
  * @since JDK 1.2
  */
    public void setType(int newType) {
        type = newType;
    }

    /**
  * Tells the connection to fire up its popup menu and allow the user to change its
  * type.
  *
  * @param  parent  The component the menu is to pop up in
  * @param  x       The x cordinate of the component the menu is to popup at
  * @param  y       The y cordinate of the component the menu is to popup at
  * @since JDK 1.2
  */
    public void edit(Component parent, int x, int y) {
        popup.show(parent, x, y);
    }

    /**
  * This event is called when the popup menu is done and the user has selected
  * the type that they want this connection to become.
  *
  * @param    e   event passed in from the popup menu
  * @since JDK 1.2
  */
    public void actionPerformed(ActionEvent e) {
        String tempString;
        JMenuItem source = (JMenuItem) (e.getSource());
        tempString = source.getText();
        if (tempString.equals(EXTEND_SELECTION)) {
            type = EXTENDS;
        }
        if (tempString.equals(IMPLEMENTS_SELECTION)) {
            type = IMPLEMENTS;
        }
        if (tempString.equals(CONTAINS_SELECTION)) {
            type = CONTAINS;
        }
        if (tempString.equals(ASSOCIATES_SELECTION)) {
            type = ASSOCIATED;
        }
        if (tempString.equals(DEPENDENTS_SELECTION)) {
            type = DEPENDENT;
        }
    }

    /**
  * Used to delete classes.  If a class is built ontop of another class, they
  * both must be removed if the lower one is destroyed.  This method can be used
  * so that the class ontop can tell if it should be destroyed
  *
  * @return   if this class is about to die and be deleted
  * @since JDK 1.2
  */
    public boolean isDead() {
        if (toClass.isDead() || fromClass.isDead()) {
            return true;
        } else {
            return false;
        }
    }

    /**
  * Returns the bounding polygon used to figure out if the point was selecting
  * the data element.
  * <p>
  * This method is from the MouseSelectionAreaInterface
  *
  * @return the polygon representing the bounding area of this element
  * @see        cube42.napkin.interfaces.MouseSelectionArea
  * @since JDK 1.1.6
  */
    public Polygon getBoundingPolygon() {
        return makeBoundingPolygon();
    }

    /**
  * Used to check if the element was selected.
  * <p>
  * This method is from the MouseSelectionAreaInterface
  *
  * @return   True if this element was selected
  * @see        cube42.napkin.interfaces.MouseSelectionArea
  * @since JDK 1.1.6
  */
    public boolean isSelected() {
        return selected;
    }

    /**
  * Sets a flag on the element of weather its been selected or not.
  * <p>
  * This method is from the MouseSelectionAreaInterface
  *
  * @param    _selected   true if you want to set this as selected
  * @see        cube42.napkin.interfaces.MouseSelectionArea
  * @since JDK 1.1.6
  */
    public void setSelected(boolean _selected) {
        selected = _selected;
    }

    /**
  * Checks to see if the point was within the bounding area of this element.
  * <p>
  * This method is from the MouseSelectionAreaInterface
  *
  * @param    p   The point you are checking.
  * @return   true if the point is within the area.
  * @see        cube42.napkin.interfaces.MouseSelectionArea
  * @since JDK 1.1.6
  */
    public boolean isWithinArea(Point p) {
        return makeBoundingPolygon().contains(p);
    }

    /**
  * Returns true if this element is to be removed from the RenderGraphicBin
  * <p>
  * This method is from the RenderGraphics interface
  *
  * @return true if the graphic w
  * @see        cube42.napkin.interfaces.RenderGraphics
  * @since JDK 1.1.6
  */
    public boolean isTerminatedGraphic() {
        return selected;
    }

    /**
  * This method is not implemented. The only way to select a Header as terminated
  * is to select it with the mouse.
  * <p>
  * This method is from the RenderGraphics interface
  *
  * @param  _terminate  set to true if it is to be cleaned out of the graphics bin
  * @see        cube42.napkin.interfaces.RenderGraphics
  * @since JDK 1.1.6
  */
    public void setTerminatedGraphic(boolean _terminate) {
    }

    /**
  * Draws the image or icon for this class.  The element must know its own
  * cordinates.
  * <p>
  * This method is from the RenderGraphics interface
  *
  * @param      g     The graphics object used to draw the element
  * @see        cube42.napkin.interfaces.RenderGraphics
  * @since JDK 1.1.6
  */
    public void render(Graphics g) {
        switch(type) {
            case DEPENDENT:
                if (this.isSelfRefencing()) {
                    drawSelfDependency(g);
                } else {
                    drawDependency(g);
                }
                break;
            case ASSOCIATED:
                if (this.isSelfRefencing()) {
                    drawSelfAssociation(g);
                } else {
                    drawAssociation(g);
                }
                break;
            case CONTAINS:
                if (this.isSelfRefencing()) {
                    drawSelfContained(g);
                } else {
                    drawContains(g);
                }
                break;
            case EXTENDS:
                drawExtends(g);
                break;
            case IMPLEMENTS:
                drawImplements(g);
                break;
        }
    }

    /**
  * Draws a light arrow that loops back onto itself.  That way a user can show
  * that a class depends on itself.
  *
  * @param    g   The graphics class of the panel that the connection is to be
  *               drawn in.
  * @since JDK 1.2
  */
    private void drawSelfDependency(Graphics g) {
        Point center = toClass.getConnectionPoint();
        int width = toClass.getWidth();
        int height = toClass.getHeight();
        g.setColor(GraphicsConstants.DEPENDENT_COLOR);
        g.drawLine(center.x + width / 2, center.y, center.x + width, center.y);
        g.drawLine(center.x + width, center.y, center.x + width, center.y + height);
        g.drawLine(center.x + width, center.y + height, center.x, center.y + height);
        g.drawLine(center.x, center.y + height, center.x, center.y + height / 2);
        PrimitiveShapesLibrary.drawLightArrowhead(g, center.x, center.y + height / 2, center.x, center.y + height / 2 + ARROWHEAD_LENGTH, 3);
    }

    /**
  * Draws a heavy arrow that loops back onto itself.  That way a user can show
  * that a class is associated with  itself.
  *
  * @param    g   The graphics class of the panel that the connection is to be
  *               drawn in.
  * @since JDK 1.2
  */
    private void drawSelfAssociation(Graphics g) {
        Point center = toClass.getConnectionPoint();
        int width = toClass.getWidth();
        int height = toClass.getHeight();
        g.setColor(GraphicsConstants.ASSOCIATION_COLOR);
        g.drawLine(center.x + width / 2, center.y, center.x + width, center.y);
        g.drawLine(center.x + width, center.y, center.x + width, center.y + height);
        g.drawLine(center.x + width, center.y + height, center.x, center.y + height);
        g.drawLine(center.x, center.y + height, center.x, center.y + height / 2);
        PrimitiveShapesLibrary.drawHeavyArrowhead(g, center.x, center.y + height / 2, center.x, center.y + height / 2 + ARROWHEAD_LENGTH, 3, true);
    }

    /**
  * Draws a diamone arrow that loops back onto itself.  That way a user can show
  * that a class contains itself.  Whatever the heck that means.  I personally
  * have no idea.  But, maybe someday someone will want to show that one class
  * cannot exist without itself.  And damn it we are going to support this kind
  * of bazar idealism.  Heck why not? Hmmm?  Should a class really not contain
  * itself.  I mean can you really exist without containing yourself.  Technically
  * you only exist because of yourself so maybe all classes should be self
  * contained.  Maybe they already are.
  *
  * @param    g   The graphics class of the panel that the connection is to be
  *               drawn in.
  * @since JDK 1.2
  */
    private void drawSelfContained(Graphics g) {
        Point center = toClass.getConnectionPoint();
        int width = toClass.getWidth();
        int height = toClass.getHeight();
        g.setColor(GraphicsConstants.CONTAINS_COLOR);
        g.drawLine(center.x + width / 2, center.y, center.x + width, center.y);
        g.drawLine(center.x + width, center.y, center.x + width, center.y + height);
        g.drawLine(center.x + width, center.y + height, center.x, center.y + height);
        g.drawLine(center.x, center.y + height, center.x, center.y + height / 2);
        PrimitiveShapesLibrary.drawDiamondHead(g, center.x, center.y + height / 2, center.x, center.y + height / 2 + ARROWHEAD_LENGTH, 3, true);
    }

    /**
  * Draws a dependency.  This looks like a arrow with a simple arrow head.
  *
  * @param    g   The graphics class of the panel that the connection is to be
  *               drawn in.
  * @since JDK 1.2
  */
    private void drawDependency(Graphics g) {
        int totalDist = 0;
        int wDist = 0;
        int hDist = 0;
        int boxWidth = toClass.getWidth();
        int boxHeight = toClass.getHeight();
        Point toPoint = toClass.getConnectionPoint();
        Point fromPoint = fromClass.getConnectionPoint();
        Point startArrow = new Point();
        Point endArrow = new Point();
        int yDiff = toPoint.y - fromPoint.y;
        int xDiff = toPoint.x - fromPoint.x;
        int arrowHeadOffset = 0;
        totalDist = distance(xDiff, yDiff);
        if (xDiff != 0) {
            wDist = (totalDist * boxWidth) / (xDiff * 2);
        } else {
            wDist = boxHeight / 2;
        }
        if (yDiff != 0) {
            hDist = (totalDist * boxHeight) / (yDiff * 2);
        } else {
            hDist = boxWidth / 2;
        }
        if (wDist == 0) wDist = 1;
        if (hDist == 0) hDist = 1;
        if (Math.abs(wDist) < Math.abs(hDist)) {
            if (wDist < 0) wDist = Math.abs(wDist);
            startArrow.x = toPoint.x - (xDiff * wDist) / totalDist;
            startArrow.y = toPoint.y - (yDiff * wDist) / totalDist;
            endArrow.x = toPoint.x - (xDiff * (wDist + ARROWHEAD_LENGTH)) / totalDist;
            endArrow.y = toPoint.y - (yDiff * (wDist + ARROWHEAD_LENGTH)) / totalDist;
        } else {
            if (hDist < 0) hDist = Math.abs(hDist);
            startArrow.x = toPoint.x - (xDiff * hDist) / totalDist;
            startArrow.y = toPoint.y - (yDiff * hDist) / totalDist;
            endArrow.x = toPoint.x - (xDiff * (hDist + ARROWHEAD_LENGTH)) / totalDist;
            endArrow.y = toPoint.y - (yDiff * (hDist + ARROWHEAD_LENGTH)) / totalDist;
        }
        g.setColor(GraphicsConstants.DEPENDENT_COLOR);
        g.drawLine(toPoint.x, toPoint.y, fromPoint.x, fromPoint.y);
        PrimitiveShapesLibrary.drawLightArrowhead(g, startArrow.x, startArrow.y, endArrow.x, endArrow.y, 3);
    }

    /**
  * Draws a association.  This looks like a arrow with a solid arrow head.
  *
  * @param    g   The graphics class of the panel that the connection is to be
  *               drawn in.
  * @since JDK 1.2
  */
    private void drawAssociation(Graphics g) {
        int totalDist = 0;
        int wDist = 0;
        int hDist = 0;
        int boxWidth = toClass.getWidth();
        int boxHeight = toClass.getHeight();
        Point toPoint = toClass.getConnectionPoint();
        Point fromPoint = fromClass.getConnectionPoint();
        Point startArrow = new Point();
        Point endArrow = new Point();
        int yDiff = toPoint.y - fromPoint.y;
        int xDiff = toPoint.x - fromPoint.x;
        int arrowHeadOffset = 0;
        totalDist = distance(xDiff, yDiff);
        if (xDiff != 0) {
            wDist = (totalDist * boxWidth) / (xDiff * 2);
        } else {
            wDist = boxHeight / 2;
        }
        if (yDiff != 0) {
            hDist = (totalDist * boxHeight) / (yDiff * 2);
        } else {
            hDist = boxWidth / 2;
        }
        if (wDist == 0) wDist = 1;
        if (hDist == 0) hDist = 1;
        if (Math.abs(wDist) < Math.abs(hDist)) {
            if (wDist < 0) wDist = Math.abs(wDist);
            startArrow.x = toPoint.x - (xDiff * wDist) / totalDist;
            startArrow.y = toPoint.y - (yDiff * wDist) / totalDist;
            endArrow.x = toPoint.x - (xDiff * (wDist + ARROWHEAD_LENGTH)) / totalDist;
            endArrow.y = toPoint.y - (yDiff * (wDist + ARROWHEAD_LENGTH)) / totalDist;
        } else {
            if (hDist < 0) hDist = Math.abs(hDist);
            startArrow.x = toPoint.x - (xDiff * hDist) / totalDist;
            startArrow.y = toPoint.y - (yDiff * hDist) / totalDist;
            endArrow.x = toPoint.x - (xDiff * (hDist + ARROWHEAD_LENGTH)) / totalDist;
            endArrow.y = toPoint.y - (yDiff * (hDist + ARROWHEAD_LENGTH)) / totalDist;
        }
        g.setColor(GraphicsConstants.ASSOCIATION_COLOR);
        g.drawLine(toPoint.x, toPoint.y, fromPoint.x, fromPoint.y);
        PrimitiveShapesLibrary.drawHeavyArrowhead(g, startArrow.x, startArrow.y, endArrow.x, endArrow.y, 3, true);
    }

    /**
  * Draws a connection that shows containment.  This looks like a arrow with a
  * solid diamond head.
  *
  * @param    g   The graphics class of the panel that the connection is to be
  *               drawn in.
  * @since JDK 1.2
  */
    private void drawContains(Graphics g) {
        int totalDist = 0;
        int wDist = 0;
        int hDist = 0;
        int boxWidth = toClass.getWidth();
        int boxHeight = toClass.getHeight();
        Point toPoint = toClass.getConnectionPoint();
        Point fromPoint = fromClass.getConnectionPoint();
        Point startArrow = new Point();
        Point endArrow = new Point();
        int yDiff = toPoint.y - fromPoint.y;
        int xDiff = toPoint.x - fromPoint.x;
        int arrowHeadOffset = 0;
        totalDist = distance(xDiff, yDiff);
        if (xDiff != 0) {
            wDist = (totalDist * boxWidth) / (xDiff * 2);
        } else {
            wDist = boxHeight / 2;
        }
        if (yDiff != 0) {
            hDist = (totalDist * boxHeight) / (yDiff * 2);
        } else {
            hDist = boxWidth / 2;
        }
        if (wDist == 0) wDist = 1;
        if (hDist == 0) hDist = 1;
        if (Math.abs(wDist) < Math.abs(hDist)) {
            if (wDist < 0) wDist = Math.abs(wDist);
            startArrow.x = toPoint.x - (xDiff * wDist) / totalDist;
            startArrow.y = toPoint.y - (yDiff * wDist) / totalDist;
            endArrow.x = toPoint.x - (xDiff * (wDist + ARROWHEAD_LENGTH)) / totalDist;
            endArrow.y = toPoint.y - (yDiff * (wDist + ARROWHEAD_LENGTH)) / totalDist;
        } else {
            if (hDist < 0) hDist = Math.abs(hDist);
            startArrow.x = toPoint.x - (xDiff * hDist) / totalDist;
            startArrow.y = toPoint.y - (yDiff * hDist) / totalDist;
            endArrow.x = toPoint.x - (xDiff * (hDist + ARROWHEAD_LENGTH)) / totalDist;
            endArrow.y = toPoint.y - (yDiff * (hDist + ARROWHEAD_LENGTH)) / totalDist;
        }
        g.setColor(GraphicsConstants.CONTAINS_COLOR);
        g.drawLine(toPoint.x, toPoint.y, fromPoint.x, fromPoint.y);
        PrimitiveShapesLibrary.drawDiamondHead(g, startArrow.x, startArrow.y, endArrow.x, endArrow.y, 3, true);
    }

    /**
  * Draws a connection that one class extending another.  It looks like the
  * UML Specializes symbol where there is a big triangle above the class
  * extending from the super class.
  *
  * @param    g   The graphics class of the panel that the connection is to be
  *               drawn in.
  * @since JDK 1.2
  */
    private void drawExtends(Graphics g) {
        int xPoints[] = new int[4];
        int yPoints[] = new int[4];
        Point toPoint = toClass.getConnectionPoint();
        Point fromPoint = fromClass.getConnectionPoint();
        Point triangleStart = new Point();
        xPoints[0] = fromPoint.x - fromClass.getWidth() / 3;
        yPoints[0] = fromPoint.y;
        xPoints[1] = xPoints[0];
        yPoints[1] = fromPoint.y - fromClass.getHeight() / 2 - EXTENDS_STUB_LENGTH;
        xPoints[2] = toPoint.x;
        yPoints[2] = yPoints[1];
        xPoints[3] = toPoint.x;
        yPoints[3] = toPoint.y;
        g.setColor(GraphicsConstants.EXTENDS_COLOR);
        g.drawPolyline(xPoints, yPoints, 4);
        triangleStart.x = xPoints[0];
        triangleStart.y = yPoints[1] + (EXTENDS_STUB_LENGTH / 2) - (INHERIT_TRIANGLE_LENGTH / 2);
        PrimitiveShapesLibrary.drawHeavyArrowhead(g, triangleStart.x, triangleStart.y, triangleStart.x, triangleStart.y + INHERIT_TRIANGLE_LENGTH, 2, true);
    }

    /**
  * Draws a connection that one class implements an interface.  It looks like the
  * UML symbol where there is a big hollow class triangle above the class
  * implementing the interface.
  *
  * @param    g   The graphics class of the panel that the connection is to be
  *               drawn in.
  * @since JDK 1.2
  */
    private void drawImplements(Graphics g) {
        int xPoints[] = new int[4];
        int yPoints[] = new int[4];
        Point toPoint = toClass.getConnectionPoint();
        Point fromPoint = fromClass.getConnectionPoint();
        Point triangleStart = new Point();
        xPoints[0] = fromPoint.x + fromClass.getWidth() / 3;
        yPoints[0] = fromPoint.y;
        xPoints[1] = xPoints[0];
        yPoints[1] = fromPoint.y - fromClass.getHeight() - IMPLEMENTS_STUB_LENGTH;
        xPoints[2] = toPoint.x;
        yPoints[2] = yPoints[1];
        xPoints[3] = toPoint.x;
        yPoints[3] = toPoint.y;
        g.setColor(GraphicsConstants.IMPLEMENTS_COLOR);
        g.drawPolyline(xPoints, yPoints, 4);
        triangleStart.x = xPoints[0];
        triangleStart.y = yPoints[1] + (IMPLEMENTS_STUB_LENGTH / 2) - (INHERIT_TRIANGLE_LENGTH / 2);
        g.setColor(GraphicsConstants.BACKGROUND_IMAGE_COLOR);
        PrimitiveShapesLibrary.drawHeavyArrowhead(g, triangleStart.x, triangleStart.y, triangleStart.x, triangleStart.y + INHERIT_TRIANGLE_LENGTH, 2, true);
        g.setColor(GraphicsConstants.IMPLEMENTS_COLOR);
        PrimitiveShapesLibrary.drawHeavyArrowhead(g, triangleStart.x, triangleStart.y, triangleStart.x, triangleStart.y + INHERIT_TRIANGLE_LENGTH, 2, false);
    }

    /**
  * Creates a polygon around the connection.  The size and shape of the polygon
  * are determined by the type of connection.  This is used to determine
  * if a user has selected a connection.
  * @return   The polygon itentifying the area that can be clicked on to select
  *           this connection. 
  * @since JDK 1.2
  */
    private Polygon makeBoundingPolygon() {
        Polygon tempPoly = null;
        int stubLength = 0;
        Point toPoint = toClass.getConnectionPoint();
        Point fromPoint = fromClass.getConnectionPoint();
        int selectX[] = new int[8];
        int selectY[] = new int[8];
        int fromRadius = fromClass.getRadius();
        if ((type == DEPENDENT) || (type == ASSOCIATED) || (type == CONTAINS)) {
            if (this.isSelfRefencing()) {
                tempPoly = new Polygon();
                tempPoly.addPoint(toPoint.x, toPoint.y);
                tempPoly.addPoint(toPoint.x + toClass.getWidth(), toPoint.y);
                tempPoly.addPoint(toPoint.x + toClass.getWidth(), toPoint.y + toClass.getHeight());
                tempPoly.addPoint(toPoint.x, toPoint.y + toClass.getHeight());
            } else {
                tempPoly = PrimitiveShapesLibrary.makeBox(toPoint.x, toPoint.y, fromPoint.x, fromPoint.y, SELECTION_WIDTH);
            }
        }
        if ((type == EXTENDS) || (type == IMPLEMENTS)) {
            if (type == EXTENDS) {
                stubLength = EXTENDS_STUB_LENGTH;
                selectX[0] = fromPoint.x - fromClass.getWidth() / 3 + SELECTION_WIDTH / 2;
            } else {
                stubLength = IMPLEMENTS_STUB_LENGTH;
                selectX[0] = fromPoint.x + fromClass.getWidth() / 3 + SELECTION_WIDTH / 2;
            }
            selectY[0] = fromPoint.y - fromClass.getHeight() / 2;
            selectX[1] = selectX[0];
            selectY[1] = selectY[0] - stubLength;
            selectX[2] = toPoint.x + SELECTION_WIDTH / 2;
            selectY[2] = selectY[0] - stubLength;
            selectX[3] = selectX[2];
            selectY[3] = toPoint.y;
            selectX[4] = selectX[2] - SELECTION_WIDTH;
            selectY[4] = selectY[3];
            selectX[5] = selectX[2] - SELECTION_WIDTH;
            selectY[5] = selectY[2];
            selectX[6] = selectX[1] - SELECTION_WIDTH;
            selectY[6] = selectY[1];
            selectX[7] = selectX[0] - SELECTION_WIDTH;
            selectY[7] = selectY[0];
            tempPoly = new Polygon(selectX, selectY, 8);
        }
        return tempPoly;
    }

    /**
  * This little utility method was created to figure out the distance
  * between 2 points if it was givin the change in height and width between
  * the points.
  * It does the simple calculation for the side of a triangle.
  * a^2 + b^2 = c^2
  *
  * @param    height    the difference between two integers in height
  * @param    width     the difference between two integers in width
  * @return   the   distance in terms of a integer.
  * @since JDK 1.2
  */
    private int distance(int height, int width) {
        double tempHeight = (double) (height);
        double tempWidth = (double) (width);
        return ((int) Math.sqrt(((tempHeight * tempHeight) + (tempWidth * tempWidth))));
    }
}
