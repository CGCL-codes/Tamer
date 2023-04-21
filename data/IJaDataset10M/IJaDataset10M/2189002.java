package calclipse.caldron.gui.graph.features;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import calclipse.caldron.gui.graph.LabeledVertexRenderer;
import calclipse.caldron.gui.graph.PlotterFeature;
import calclipse.core.graph.PlotterDoc;
import calclipse.lib.math.util.graph.Graph;
import calclipse.lib.math.util.graph.GraphPanel;
import calclipse.lib.math.util.graph.Vertex;

/**
 * Displays graph info at vertices where the user clicks.
 * @author T. Sommerland
 */
public class TraceFeature extends PlotterFeature implements MouseListener {

    private final LabeledVertexRenderer renderer;

    private final int maxDistance;

    private Graph labeledGraph;

    /**
     * Creates a trace feature with a max distance that is used to
     * sensitize the graph vertex selection.
     * @param maxDistance the maximum distance between
     * a click point and a selected vertex (in pixels)
     */
    public TraceFeature(final PlotterDoc plotter, final LabeledVertexRenderer renderer, final int maxDistance) {
        super(plotter);
        this.renderer = renderer;
        this.maxDistance = maxDistance;
    }

    public TraceFeature(final PlotterDoc plotter, final LabeledVertexRenderer renderer) {
        this(plotter, renderer, Integer.MAX_VALUE);
    }

    @Override
    protected void activate() {
        plotter.getPanel().addMouseListener(this);
    }

    @Override
    protected void deactivate() {
        plotter.getPanel().removeMouseListener(this);
    }

    @Override
    public void mouseClicked(final MouseEvent evt) {
        clear();
        final VertexSelection selection = getSelection(evt.getPoint());
        if (selection != null) {
            final GraphPanel panel = plotter.getPanel();
            renderer.setIndex(selection.index);
            labeledGraph = panel.duplicateGraph(selection.graph, renderer);
            panel.repaint();
        }
    }

    /**
     * Clears the vertex info tag.
     */
    public void clear() {
        if (labeledGraph != null) {
            plotter.getPanel().removeGraph(labeledGraph);
            labeledGraph = null;
            renderer.setIndex(-1);
        }
    }

    private VertexSelection getSelection(final Point point) {
        final GraphPanel panel = plotter.getPanel();
        Graph graph = null;
        int index = -1;
        double dist = Double.POSITIVE_INFINITY;
        for (final Graph gr : panel.getGraphs()) {
            for (int i = 0; i < gr.size(); i++) {
                final Vertex vx = gr.get(i);
                final double ds = getDistance(vx, point);
                if (ds < dist && ds <= maxDistance) {
                    graph = gr;
                    index = i;
                    dist = ds;
                }
            }
        }
        if (graph == null) {
            return null;
        }
        return new VertexSelection(graph, index);
    }

    /**
     * The distance is measured in pixels.
     */
    private double getDistance(final Vertex vertex, final Point point) {
        final Point vertexPoint = plotter.getPanel().logicalToPhysical(vertex.getX(), vertex.getY());
        final int deltaX = point.x - vertexPoint.x;
        final int deltaY = point.y - vertexPoint.y;
        return Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
    }

    private static final class VertexSelection {

        final Graph graph;

        final int index;

        public VertexSelection(final Graph graph, final int index) {
            this.graph = graph;
            this.index = index;
        }
    }

    @Override
    public void mouseEntered(final MouseEvent evt) {
    }

    @Override
    public void mouseExited(final MouseEvent evt) {
    }

    @Override
    public void mousePressed(final MouseEvent evt) {
    }

    @Override
    public void mouseReleased(final MouseEvent evt) {
    }
}
