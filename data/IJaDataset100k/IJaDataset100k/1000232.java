package mvt.graphics;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.color.*;
import java.util.*;

/**
 * The PlotPanel class provides the generic operations that
 * are required for any 2D or 3D plotter.  
 * 
 * @author Darin Gillis
 * 
 * @version 1.0
 * @since JDK1.2
 */
public class PlotPanel extends GraphicsComponent {

    private boolean axesVisible = true;

    private GraphicsList graphicsList = new GraphicsList();

    /**  paintComponent defers its painting to thte paintGraphics mthd.
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintGraphics(g);
    }

    /** The paintGraphics method iterates through its list of 
     * graphicsObjects and paints them. 
     */
    protected void paintGraphics(Graphics g) {
        for (int i = 0; i < graphicsList.size(); i++) if (graphicsList.getGraphicsObject(i) != null) graphicsList.getGraphicsObject(i).paintComponent(g);
    }

    /** Hides/displays the Axes for this PlotPanel
     */
    public void setAxesVisible(boolean vis) {
        axesVisible = vis;
    }

    /** @return boolean that decides whether or not axes are displayed.
     */
    protected boolean getAxesVisible() {
        return axesVisible;
    }

    /** The method <code>addGraphicsObject</code> adds a 
     * <code>GraphicsComponent</code> object to the current 
     * GraphicsList. 
     *
     * @param obj a <code>GraphicsComponent</code> object 
     */
    public void addGraphicsObject(GraphicsComponent obj) {
        graphicsList.addGraphicsObject(obj);
    }

    /** The method <code>clearGraphicsList</code> removes all
     * components from the GraphicsList object. 
     */
    public void clearGraphicsList() {
        graphicsList = new GraphicsList();
    }

    /** Returns the size of the graphicsList in this PlotPanel
     */
    public int getNumGraphicsObjects() {
        return graphicsList.size();
    }

    /** Return the graphics object at index i 
     */
    public GraphicsComponent getGraphicsObject(int i) {
        return graphicsList.getGraphicsObject(i);
    }
}
