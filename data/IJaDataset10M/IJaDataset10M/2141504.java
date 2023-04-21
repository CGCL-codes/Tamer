package jung.refact;

import edu.uci.ics.jung.visualization.control.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import edu.uci.ics.jung.visualization.Layout;
import edu.uci.ics.jung.visualization.Renderer;
import edu.uci.ics.jung.visualization.ShapePickSupport;
import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.transform.MutableAffineTransformer;
import edu.uci.ics.jung.visualization.transform.shape.ShapeTransformer;

/**
 * A VisualizationViewer that can act as a satellite view for another
 * (master) VisualizationViewer. In this view, the full graph is always visible
 * and all mouse actions affect the graph in the master view.
 * 
 * A rectangular shape in the satellite view shows the visible bounds of
 * the master view. 
 * 
 * @author Tom Nelson - RABA Technologies
 *
 * 
 */
public class SatelliteVisualizationViewer extends VisualizationViewer {

    /**
     * the master VisualizationViewer that this is a satellite view for
     */
    protected VisualizationViewer master;

    /**
     * @param layout
     * @param renderer
     */
    public SatelliteVisualizationViewer(VisualizationViewer master, Layout layout, Renderer renderer) {
        this(master, new DefaultVisualizationModel(layout), renderer);
    }

    /**
     * @param layout
     * @param renderer
     * @param preferredSize
     */
    public SatelliteVisualizationViewer(VisualizationViewer master, Layout layout, Renderer renderer, Dimension preferredSize) {
        this(master, new DefaultVisualizationModel(layout, preferredSize), renderer, preferredSize);
    }

    /**
     * @param model
     * @param renderer
     */
    public SatelliteVisualizationViewer(VisualizationViewer master, VisualizationModel model, Renderer renderer) {
        this(master, model, renderer, new Dimension(300, 300));
    }

    /**
     * @param master the master view
     * @param model
     * @param renderer
     * @param preferredSize
     */
    public SatelliteVisualizationViewer(VisualizationViewer master, VisualizationModel model, Renderer renderer, Dimension preferredSize) {
        super(model, renderer, preferredSize);
        this.master = master;
        ModalGraphMouse gm = new ModalSatelliteGraphMouse();
        setGraphMouse(gm);
        addPreRenderPaintable(new ViewLens(this, master));
        AffineTransform modelLayoutTransform = new AffineTransform(master.getLayoutTransformer().getTransform());
        setLayoutTransformer(new MutableAffineTransformer(modelLayoutTransform));
        master.addChangeListener(this);
        setPickedState(master.getPickedState());
        setPickSupport(new ShapePickSupport());
    }

    /**
     * @return Returns the master.
     */
    public VisualizationViewer getMaster() {
        return master;
    }

    /**
     * A four-sided shape that represents the visible part of the
     * master view and is drawn in the satellite view
     * 
     * @author Tom Nelson - RABA Technologies
     *
     *
     */
    static class ViewLens implements Paintable {

        VisualizationViewer master;

        VisualizationViewer vv;

        public ViewLens(VisualizationViewer vv, VisualizationViewer master) {
            this.vv = vv;
            this.master = master;
        }

        public void paint(Graphics g) {
            ShapeTransformer masterViewTransformer = master.getViewTransformer();
            ShapeTransformer masterLayoutTransformer = master.getLayoutTransformer();
            ShapeTransformer vvLayoutTransformer = vv.getLayoutTransformer();
            Shape lens = master.getBounds();
            lens = masterViewTransformer.inverseTransform(lens);
            lens = masterLayoutTransformer.inverseTransform(lens);
            lens = vvLayoutTransformer.transform(lens);
            Graphics2D g2d = (Graphics2D) g;
            Color old = g.getColor();
            Color lensColor = master.getBackground();
            vv.setBackground(lensColor.darker());
            g.setColor(lensColor);
            g2d.fill(lens);
            g.setColor(Color.gray);
            g2d.draw(lens);
            g.setColor(old);
        }

        public boolean useTransform() {
            return true;
        }
    }
}
