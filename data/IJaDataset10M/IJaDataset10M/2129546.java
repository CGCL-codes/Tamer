package org.swtchart.examples;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.swtchart.Chart;
import org.swtchart.IAxis;
import org.swtchart.ILineSeries;
import org.swtchart.IAxis.Position;
import org.swtchart.ISeries.SeriesType;

/**
 * An example for multiple axes.
 */
public class MultipleAxesExample {

    private static final double[] ySeries1 = { 0.0, 0.38, 0.71, 0.92, 1.0, 0.92, 0.71, 0.38, 0.0, -0.38, -0.71, -0.92, -1.0, -0.92, -0.71, -0.38 };

    private static final double[] ySeries2 = { 2, 11, 19, 23, 18, 15, 18, 26, 29, 32, 47, 32, 31, 35, 30, 29 };

    /**
     * The main method.
     * 
     * @param args
     *            the arguments
     */
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("Multiple Axes");
        shell.setSize(500, 400);
        shell.setLayout(new FillLayout());
        createChart(shell);
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }

    /**
     * create the chart.
     * 
     * @param parent
     *            The parent composite
     * @return The created chart
     */
    public static Chart createChart(Composite parent) {
        Chart chart = new Chart(parent, SWT.NONE);
        chart.getTitle().setText("Multiple Axes");
        chart.getAxisSet().getXAxis(0).getTitle().setText("Data Points");
        chart.getAxisSet().getYAxis(0).getTitle().setText("Amplitude 1");
        int axisId = chart.getAxisSet().createYAxis();
        IAxis yAxis2 = chart.getAxisSet().getYAxis(axisId);
        yAxis2.setPosition(Position.Secondary);
        final Color RED = Display.getDefault().getSystemColor(SWT.COLOR_RED);
        yAxis2.getTick().setForeground(RED);
        yAxis2.getTitle().setForeground(RED);
        yAxis2.getTitle().setText("Amplitude 2");
        ILineSeries lineSeries1 = (ILineSeries) chart.getSeriesSet().createSeries(SeriesType.LINE, "line series 1");
        lineSeries1.setYSeries(ySeries1);
        ILineSeries lineSeries2 = (ILineSeries) chart.getSeriesSet().createSeries(SeriesType.LINE, "line series 2");
        lineSeries2.setYSeries(ySeries2);
        lineSeries2.setLineColor(RED);
        lineSeries2.setYAxisId(axisId);
        chart.getAxisSet().adjustRange();
        return chart;
    }
}
