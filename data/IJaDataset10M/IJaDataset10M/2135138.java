package org.argouml.uml.diagram.static_structure.ui;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.Rectangle;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigPoly;

/** 
 * Class to display graphics for a UML model in a class diagram. 
 */
public class FigModel extends FigPackage {

    private FigPoly figPoly = new FigPoly(Color.black, Color.black);

    /**
     * The Constructor.
     *
     * @param modelElement the UML model
     * @param x the x coordinate of the location
     * @param y the y coordinate of the location
     */
    public FigModel(Object modelElement, int x, int y) {
        super(modelElement, x, y);
        int[] xpoints = { 125, 130, 135, 125 };
        int[] ypoints = { 45, 40, 45, 45 };
        Polygon polygon = new Polygon(xpoints, ypoints, 4);
        figPoly.setPolygon(polygon);
        figPoly.setFilled(false);
        addFig(figPoly);
        Rectangle r = getBounds();
        setBounds(r.x, r.y, r.width, r.height);
        updateEdges();
    }

    /**
     * The constructor that hooks the Fig into the UML modelelement
     * @param gm ignored
     * @param node the UMl element
     */
    public FigModel(GraphModel gm, Object node) {
        this(node, 0, 0);
    }

    @Override
    protected void setStandardBounds(int x, int y, int w, int h) {
        if (figPoly != null) {
            Rectangle oldBounds = getBounds();
            figPoly.translate((x - oldBounds.x) + (w - oldBounds.width), y - oldBounds.y);
        }
        super.setStandardBounds(x, y, w, h);
    }
}
