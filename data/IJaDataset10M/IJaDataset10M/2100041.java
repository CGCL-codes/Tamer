package edu.ucla.stat.SOCR.chart.gui;

import edu.ucla.stat.SOCR.chart.data.*;
import org.jfree.chart.labels.*;
import org.jfree.data.xy.*;

public class SOCRScaledBubbleSeriesLabelGenerator extends StandardXYSeriesLabelGenerator {

    /**
	 * @uml.property  name="s"
	 * @uml.associationEnd  
	 */
    Summary s = null;

    double zScale = 1;

    public SOCRScaledBubbleSeriesLabelGenerator(double scale) {
        super();
        zScale = scale;
    }

    public String generateLabel(XYDataset dataset, int series) {
        if (s == null) {
            s = new Summary((XYZDataset) dataset);
            s.setZScale(zScale);
        }
        return s.getXYZSummary((XYZDataset) dataset, series) + "\n";
    }
}
