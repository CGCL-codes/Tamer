package Shapes;

import Exceptions.*;
import Reps.*;
import UI.*;
import UI.Panels.*;
import java.awt.*;
import java.util.*;

/**
 * <p>Title: ISA</p>
 * <p>Description: A class representing a visible ISA relationship (triangle)</p>
 * <p>Jaarproject Programmeren 2002-2003</p>
 * <p>Company: UIA</p>
 * @author Helsen Gert (s985177)
 * @version 20/06/2003
 */
public class ISA extends DrawableElement {

    private static ISAPanel fPanel = new ISAPanel();

    private ISARep fIsaRep;

    private Polygon fTriangle;

    /**
   * Constructor
   * @param diagram Diagram
   * @param isaRep Internal ISA relationship
   * @param x X-co�rdinate on the diagram
   * @param y Y-coordinate on the diagram
   */
    public ISA(final ERDiagram diagram, final ISARep isaRep, final int x, final int y) {
        super(diagram, Constants.kISAWidth, Constants.kISAHeight);
        fTriangle = new Polygon();
        int xPoints[] = { x - (Constants.kISAWidth / 2), x, x + (Constants.kISAWidth / 2) };
        int yPoints[] = { y + (Constants.kISAHeight / 2), y - (Constants.kISAHeight / 2), y + (Constants.kISAHeight / 2) };
        fTriangle.xpoints = xPoints;
        fTriangle.ypoints = yPoints;
        fTriangle.npoints = 3;
        fIsaRep = isaRep;
    }

    /**
   * Update the position field of the internal element (done before writing to a file)
   */
    public void updateRepPosition() {
        int difX = (int) ((getWidth() - Constants.kISAWidth) / 2);
        getRep().setPosition(getCenterX() - difX, getCenterY());
    }

    /**
   * Returns the 'fill color' for an ISA relationship (based on the colors in Constants.java)
   * @return fill color
   */
    protected Color getColor() {
        return Constants.kISAColor;
    }

    /**
   * Returns the visible shape, belonging to an ISA relationship (Triangle)
   * @return visible shape
   */
    protected Shape getShape() {
        return fTriangle;
    }

    /**
   * Returns the internal element, belonging to an ISA relationship (ISARep)
   * @return internal element
   */
    public Element getRep() {
        return fIsaRep;
    }

    /**
   * Shows the property panel for an ISA relationship
   * @param diagram Diagram
   */
    public void showPanel(final ERDiagram diagram) {
        EREditor editor = diagram.getEditor();
        fPanel.update(diagram, this);
        editor.showPanel(fPanel);
    }

    /**
   * Not used, because the name is always the same for an ISA relationship
   * @param diagram Diagram
   */
    public void adjustWidthToName(final ERDiagram diagram) {
        return;
    }

    /**
   * Connect this ISA relationship with another element
   * @param role Role of the connection line (or null if it should be automatically created)
   * @param drawableElement Element to connect with
   * @return Role of the connection
   * @throws LineException These elements cannot be connected
   */
    public Role connect(final Role role, final DrawableElement drawableElement) throws LineException {
        try {
            if (drawableElement instanceof Entity) {
                fIsaRep.addSubEntity((EntityRep) drawableElement.getRep());
            } else {
                throw new LineException("These elements cannot be connected...");
            }
            return null;
        } catch (Exception e) {
            throw new LineException(e.getMessage());
        }
    }

    /**
   * Returns if this ISA relationship can be connected with a given object
   * @param drawableElement Object to connect with
   * @return boolean to indicate if connection is allowed
   */
    public boolean canBeConnectedTo(final DrawableElement drawableElement) {
        if (drawableElement instanceof Entity) {
            return fIsaRep.canAddSubEntity((EntityRep) drawableElement.getRep());
        } else {
            return false;
        }
    }

    /**
   * Disconnect this ISA relationship with the given element
   * @param role Role belonging to the connection
   * @param drawableElement Element to disconnect with
   * @throws LineException No connection found between these elements
   */
    public void disconnect(final Role role, final DrawableElement drawableElement) throws LineException {
        try {
            if (drawableElement instanceof Entity) {
                fIsaRep.removeEntity((EntityRep) drawableElement.getRep());
            } else {
                throw new LineException("No connection found between these elements...");
            }
        } catch (Exception e) {
            throw new LineException(e.getMessage());
        }
    }

    /**
   * Move this ISA relationship to a given location (used for dragging)
   * @param newX New X-coordinate
   * @param newY New Y-co�rdinate
   */
    protected void updateLocation(final int newX, final int newY) {
        int xPoints[] = { newX - (Constants.kISAWidth / 2), newX, newX + (Constants.kISAWidth / 2) };
        int yPoints[] = { newY + (Constants.kISAHeight / 2), newY - (Constants.kISAHeight / 2), newY + (Constants.kISAHeight / 2) };
        fTriangle.xpoints = xPoints;
        fTriangle.ypoints = yPoints;
        fTriangle.invalidate();
    }

    /**
   * Returns the handles for this ISA relationship
   * @return handles for this ISA relationship
   */
    protected ArrayList getHandles() {
        ArrayList handles = new ArrayList();
        handles.add(new Handle(-(Constants.kISAWidth / 2), Constants.kISAHeight / 2));
        handles.add(new Handle(0, -(Constants.kISAHeight / 2)));
        handles.add(new Handle(Constants.kISAWidth / 2, Constants.kISAHeight / 2));
        return handles;
    }
}
