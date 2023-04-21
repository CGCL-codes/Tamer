package org.test;

import javax.swing.JFrame;
import org.chart.Chart;
import org.chart.ChartTypes;
import org.data.ChartDataset;
import org.data.ChartRowDataset;
import org.data.model.BarChart.BarChartBar;
import org.data.model.BarChart.BarChartData;
import org.data.model.GanttChart.GanttChartData;
import org.data.model.GanttChart.GanttChartInterval;
import org.data.model.LineChart.LineChartData;
import org.data.model.LineChart.LineChartPoint;

/**
 * 
 * @author vimartin
 */
public class BarChartExample {

    public static ChartDataset createDataset() {
        ChartRowDataset dataRow = new ChartRowDataset("Bar Chart 0");
        BarChartData bars1 = new BarChartData("Graphics " + 1);
        bars1.addData(new BarChartBar("Bar 1.1", 5));
        bars1.addData(new BarChartBar("Bar 1.2", 3));
        bars1.addData(new BarChartBar("Bar 1.3", 9));
        BarChartData bars2 = new BarChartData("Graphics " + 2);
        bars2.addData(new BarChartBar("Bar 2.1", 1));
        bars2.addData(new BarChartBar("Bar 2.2", 2));
        bars2.addData(new BarChartBar("Bar 2.3", 5));
        dataRow.addElement(bars1);
        dataRow.addElement(bars2);
        ChartRowDataset dataRow1 = new ChartRowDataset("Bar Chart 1");
        BarChartData bars3 = new BarChartData("Graphics " + 3);
        bars3.addData(new BarChartBar("Bar 3.1", 7));
        bars3.addData(new BarChartBar("Bar 3.2", 2));
        bars3.addData(new BarChartBar("Bar 3.3", 19));
        BarChartData bars4 = new BarChartData("Graphics " + 4);
        bars4.addData(new BarChartBar("Bar 4.1", 3));
        bars4.addData(new BarChartBar("Bar 4.2", 12));
        bars4.addData(new BarChartBar("Bar 4.3", 17));
        dataRow1.addElement(bars3);
        dataRow1.addElement(bars4);
        ChartDataset dataset = new ChartDataset();
        dataset.addRow(dataRow);
        dataset.addRow(dataRow1);
        return dataset;
    }

    public static void main(String args[]) {
        ChartDataset dataset = createDataset();
        JFrame frame = new JFrame();
        Chart chart = new Chart("Bar Chart: Example", "Bar Chart: Example", ChartTypes.Bars, dataset);
        frame.add(chart.getChartPanel());
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }
}
