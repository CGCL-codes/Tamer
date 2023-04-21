package calclipse.core.graph;

import java.awt.Dimension;
import calclipse.lib.math.util.graph.GraphPanel;

/**
 * The Initial plotter factory.
 * @author T. Sommerland
 */
public class DefaultPlotterFactory implements PlotterFactory {

    private static final int WIDTH = 300;

    private static final int HEIGHT = 300;

    public DefaultPlotterFactory() {
    }

    @Override
    public PlotterDoc createPlotter() {
        final PlotterDoc plotter = new PlotterDoc(new GraphPanel());
        init(plotter);
        return plotter;
    }

    /**
     * Initializes the plotter.
     * This method may be overridden if necessary.
     */
    protected void init(final PlotterDoc plotter) {
        final Dimension size = getSize();
        plotter.getPanel().setPreferredSize(size);
        plotter.getPanel().setSize(size);
    }

    /**
     * The initial size of the plotter.
     * This method may be overridden if necessary.
     */
    protected Dimension getSize() {
        return new Dimension(WIDTH, HEIGHT);
    }
}
