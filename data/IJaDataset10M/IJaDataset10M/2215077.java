package org.swtchart.examples.advanced;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.swtchart.Chart;
import org.swtchart.ILineSeries;
import org.swtchart.ISeries;
import org.swtchart.ISeries.SeriesType;

/**
 * An example to get bounds of series symbol.
 */
public class SymbolBoundsExample {

    private static final double[] ySeries1 = { 0.26, 0.25, 0.29, 0.31, 0.32 };

    private static final double[] ySeries2 = { 0.32, 0.31, 0.27, 0.28, 0.26 };

    /**
     * The main method.
     * 
     * @param args
     *            the arguments
     */
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("Symbol Bounds");
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
        final Chart chart = new Chart(parent, SWT.NONE);
        chart.getTitle().setText("Symbol Bounds");
        ILineSeries series1 = (ILineSeries) chart.getSeriesSet().createSeries(SeriesType.LINE, "series 1");
        series1.setYSeries(ySeries1);
        ILineSeries series2 = (ILineSeries) chart.getSeriesSet().createSeries(SeriesType.LINE, "series 2");
        series2.setYSeries(ySeries2);
        series2.setLineColor(Display.getDefault().getSystemColor(SWT.COLOR_RED));
        chart.getAxisSet().adjustRange();
        chart.getPlotArea().addMouseMoveListener(new MouseMoveListener() {

            public void mouseMove(MouseEvent e) {
                for (ISeries series : chart.getSeriesSet().getSeries()) {
                    for (int i = 0; i < series.getYSeries().length; i++) {
                        Point p = series.getPixelCoordinates(i);
                        double distance = Math.sqrt(Math.pow(e.x - p.x, 2) + Math.pow(e.y - p.y, 2));
                        if (distance < ((ILineSeries) series).getSymbolSize()) {
                            setToolTipText(series, i);
                            return;
                        }
                    }
                }
                chart.getPlotArea().setToolTipText(null);
            }

            private void setToolTipText(ISeries series, int index) {
                chart.getPlotArea().setToolTipText("Series: " + series.getId() + "\nValue: " + series.getYSeries()[index]);
            }
        });
        return chart;
    }
}
