package org.torweg.pulse.component.statistics.view.jfreecharts;

import java.awt.Paint;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.xy.XYDataset;

/**
 * A {@code XYBarRenderer} which uses {@code DataChartUtilities.DEFAULT_BLUE}
 * for positive values and {@code DataChartUtilities.DEFAULT_RED} for negative
 * values in order to colour the bar.
 * 
 * @author Daniel Dietz
 * @version $Revision$
 */
public class XYBarRendererPositiveNegative extends XYBarRenderer {

    /**
	 * The serialVersionUID.
	 */
    private static final long serialVersionUID = -2439685144988997039L;

    /**
	 * Creates a new {@code XYBarRendererPositiveNegative} with the desired
	 * margin.
	 * 
	 * @param d
	 *            the margin
	 */
    public XYBarRendererPositiveNegative(final double d) {
        super(d);
    }

    /**
	 * Returns the desired {@code Paint} for the bars, determined by the value
	 * of the current data.
	 * 
	 * @param row
	 *            the row index
	 * @param column
	 *            the column index
	 * 
	 * @return {@code Paint} the paint:
	 *         <ul>
	 *         <li>{@code DataChartUtilities.DEFAULT_BLUE} for positive values</li>
	 *         <li>{@code DataChartUtilities.DEFAULT_RED} for negative values</li>
	 *         </ul>
	 */
    @Override
    public final Paint getItemPaint(final int row, final int column) {
        XYDataset dataset = getPlot().getDataset();
        double value = dataset.getYValue(row, column);
        if (value < 0) {
            return DataChartUtilities.DEFAULT_RED;
        } else {
            return DataChartUtilities.DEFAULT_BLUE;
        }
    }
}
