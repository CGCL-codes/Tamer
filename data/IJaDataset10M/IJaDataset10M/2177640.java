package game.visualizations.tools.jfree;

import java.awt.Color;
import java.awt.Rectangle;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import com.rapidminer.gui.plotter.ColorProvider;

/**
 * Scatterplot matrix renderers for datasets.
 * 
 * @author Jan Fabian
 *
 */
public class BubbleScatterplotMatrixRendererProvider implements ScatterplotMatrixRendererProvider {

    protected static final transient int BASE_SIZE = 5;

    protected class DataInstance {

        public int series;

        public int item;

        public DataInstance(int series, int item) {
            this.series = series;
            this.item = item;
        }
    }

    public ColorProvider getColorProvider() {
        return new ColorProvider();
    }

    protected DataInstance highlightedItem;

    public void highlightItem(int series, int item) {
        highlightedItem = new DataInstance(series, item);
    }

    public void nohighlight() {
        highlightedItem = null;
    }

    @Override
    public XYItemRenderer getRenderer(XYPlot plot, XYDataset dataset, Rectangle area) {
        CustomBubbleRenderer renderer = new CustomBubbleRenderer();
        renderer.autoset(dataset, BASE_SIZE);
        if (highlightedItem != null) renderer.highlightItem(highlightedItem.series, highlightedItem.item);
        if (dataset != null) {
            if (dataset.getSeriesCount() == 1) {
                renderer.setSeriesPaint(0, Color.RED);
                renderer.setSeriesFillPaint(0, Color.RED);
            } else {
                for (int j = 0; j < dataset.getSeriesCount(); j++) {
                    Color color = getColorProvider().getPointColor((double) j / (double) (dataset.getSeriesCount() - 1));
                    renderer.setSeriesPaint(j, color);
                    renderer.setSeriesFillPaint(j, color);
                }
            }
        }
        return renderer;
    }
}
