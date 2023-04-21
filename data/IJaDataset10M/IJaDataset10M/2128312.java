package dinamica.charts;

import dinamica.*;
import org.jfree.chart.*;
import org.jfree.data.general.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.labels.*;

/**
 * Chart plugin for simple Pie
 * Last update: 24/july/2005
 * @author Martin Cordova (dinamica@martincordova.com)
 */
public class PieChart extends AbstractChartPlugin {

    public JFreeChart getChart(Recordset chartInfo, Recordset data) throws Throwable {
        DefaultPieDataset chartdata = new DefaultPieDataset();
        data.top();
        while (data.next()) {
            String colx = (String) chartInfo.getValue("column-x");
            String coly = (String) chartInfo.getValue("column-y");
            Double value = new Double(String.valueOf(data.getValue(coly)));
            String label = String.valueOf(data.getValue(colx));
            if (value == null) value = new Double(0);
            chartdata.setValue(label, value.doubleValue());
        }
        String title = (String) chartInfo.getValue("title");
        JFreeChart chart = ChartFactory.createPieChart(title, chartdata, true, false, false);
        configurePlot(chart.getPlot());
        return chart;
    }

    /**
	 * Configure chart decorations
	 */
    public void configurePlot(Plot p) {
        PiePlot plot = (PiePlot) p;
        StandardPieSectionLabelGenerator splg = new StandardPieSectionLabelGenerator("{2}");
        splg.getPercentFormat().setMinimumFractionDigits(2);
        plot.setLabelGenerator(splg);
        plot.setForegroundAlpha(0.7F);
    }
}
