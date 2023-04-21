package org.jCharts.demo.swing;

import org.jCharts.chartData.ChartDataException;
import org.jCharts.chartData.PieChartDataSet;
import org.jCharts.nonAxisChart.PieChart2D;
import org.jCharts.properties.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class SwingDemo extends JFrame {

    private JPanel panel;

    /*******************************************************************************
	 *
	 ********************************************************************************/
    public SwingDemo() throws ChartDataException, PropertyException {
        initComponents();
    }

    /*******************************************************************************
	 *
	 ********************************************************************************/
    private void initComponents() throws ChartDataException, PropertyException {
        this.setSize(500, 500);
        this.panel = new JPanel(true);
        this.panel.setSize(500, 500);
        this.getContentPane().add(this.panel);
        this.setVisible(true);
        String[] labels = { "BMW", "Audi", "Lexus" };
        String title = "Cars that Own";
        Paint[] paints = { Color.blue, Color.gray, Color.red };
        double[] data = { 50d, 30d, 20d };
        PieChart2DProperties pieChart2DProperties = new PieChart2DProperties();
        PieChartDataSet pieChartDataSet = new PieChartDataSet(title, data, labels, paints, pieChart2DProperties);
        PieChart2D pieChart2D = new PieChart2D(pieChartDataSet, new LegendProperties(), new ChartProperties(), 450, 450);
        pieChart2D.setGraphics2D((Graphics2D) this.panel.getGraphics());
        pieChart2D.render();
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(WindowEvent windowEvent) {
                exitForm(windowEvent);
            }
        });
    }

    /*********************************************************************************
	 * Exit the Application
	 *
	 * @param windowEvent
	 ***********************************************************************************/
    private void exitForm(WindowEvent windowEvent) {
        System.exit(0);
    }

    /*********************************************************************************
	 *
	 *
	 ***********************************************************************************/
    public static void main(String args[]) throws ChartDataException, PropertyException {
        new SwingDemo();
    }
}
