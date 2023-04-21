package de.beas.explicanto.distribution.reports.scriptlets;

import java.awt.Color;
import java.awt.Paint;
import java.io.Serializable;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import de.beas.explicanto.distribution.config.Constants;

/**
 * @author paulh
 */
public class ScriptletUtils {

    /**
	 * Creates a pie chart, the procent is considered to be the part completed of the course (or something like that)
	 * @param label_1 the label which will be seen on the completed part of the chart (Currently labels are OFF)
	 * @param label_2 the label which will be seen on the uncompleted part of the chart
	 * @param procent the procent of the completed course (task...)
	 * @return
	 */
    public static JFreeChart buildChart(String label_1, String label_2, int procent) {
        DefaultPieDataset dps = new DefaultPieDataset();
        if (procent < 100) {
            dps.setValue(label_1, 100 - procent);
            dps.setValue(label_2, procent);
        } else if (procent == 100) {
            dps.setValue(label_1, 1);
            dps.setValue(label_2, 99);
        }
        JFreeChart chart = ChartFactory.createPieChart(null, dps, false, false, false);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setInteriorGap(0);
        plot.setLabelGenerator(new LabelGenerator());
        plot.setLabelPaint(Color.WHITE);
        plot.setDrawingSupplier(new DrawingCustom());
        chart.setBackgroundPaint(Color.WHITE);
        chart.setBorderVisible(false);
        return chart;
    }
}

/**
 * Label generator used for not generating labels :D 
 */
class LabelGenerator implements PieSectionLabelGenerator, Serializable {

    public String generateSectionLabel(PieDataset ds, Comparable key) {
        return null;
    }
}

;

/**
 * Drawing suplier, used for alternating the colours between the section of the pies
 */
class DrawingCustom extends DefaultDrawingSupplier implements Serializable {

    private boolean flag = false;

    public Paint getNextPaint() {
        Paint value = Constants.CHART_UNCOMPLETED;
        if (flag) {
            value = Constants.CHART_COMPLETED;
        }
        flag = !flag;
        return value;
    }
}
