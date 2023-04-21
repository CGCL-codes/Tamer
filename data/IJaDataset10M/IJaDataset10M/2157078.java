package playground.thibautd.utils;

import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.JFreeChart;

/**
 * provides static function to perform usual XYPlot manipulation.
 * @author thibautd
 */
public class XYChartUtils {

    /**
	 * Sets the X axis of an XYPlot chart to a {@link NumberAxis}
	 * with integer values.
	 */
    public static void integerXAxis(final JFreeChart chart) {
        ValueAxis axis = (chart.getXYPlot()).getDomainAxis();
        NumberAxis numberAxis;
        if (axis instanceof NumberAxis) {
            numberAxis = (NumberAxis) axis;
        } else {
            numberAxis = new NumberAxis();
            (chart.getXYPlot()).setDomainAxis(numberAxis);
        }
        numberAxis.setTickUnit((NumberTickUnit) NumberAxis.createIntegerTickUnits().getCeilingTickUnit(1d));
        numberAxis.setAutoRangeIncludesZero(false);
    }

    /**
	 * Sets the X axis of an XYPlot chart to a {@link NumberAxis}
	 * with integer values.
	 */
    public static void integerYAxis(final JFreeChart chart) {
        ValueAxis axis = (chart.getXYPlot()).getRangeAxis();
        NumberAxis numberAxis;
        if (axis instanceof NumberAxis) {
            numberAxis = (NumberAxis) axis;
        } else {
            numberAxis = new NumberAxis();
            (chart.getXYPlot()).setRangeAxis(numberAxis);
        }
        numberAxis.setTickUnit((NumberTickUnit) NumberAxis.createIntegerTickUnits().getCeilingTickUnit(1d));
        numberAxis.setAutoRangeIncludesZero(false);
    }

    /**
	 * Sets the X and the Y axes of an XYPlot to use integer values.
	 */
    public static void integerAxes(final JFreeChart chart) {
        integerXAxis(chart);
        integerYAxis(chart);
    }
}
