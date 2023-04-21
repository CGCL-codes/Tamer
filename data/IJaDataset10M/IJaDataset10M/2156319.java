package org.swtchart.examples;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.swtchart.Chart;
import org.swtchart.IBarSeries;
import org.swtchart.ISeries;
import org.swtchart.ISeries.SeriesType;

/**
 * An example to convert series data coordinate into pixel coordinate.
 */
public class CoordinateConversionExample4 {

    private static final double[] ySeries1 = { 3.0, 2.1, 1.9, 2.3, 3.2 };

    private static final double[] ySeries2 = { 2.0, 3.1, 0.9, 1.3, 2.2 };

    /**
     * The main method.
     * 
     * @param args
     *            the arguments
     */
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("Bar Bounds Example");
        shell.setSize(500, 400);
        shell.setLayout(new FillLayout());
        final Chart chart = new Chart(shell, SWT.NONE);
        IBarSeries series1 = (IBarSeries) chart.getSeriesSet().createSeries(SeriesType.BAR, "series 1");
        series1.setYSeries(ySeries1);
        IBarSeries series2 = (IBarSeries) chart.getSeriesSet().createSeries(SeriesType.BAR, "series 2");
        series2.setYSeries(ySeries2);
        series2.setBarColor(Display.getDefault().getSystemColor(SWT.COLOR_GREEN));
        chart.getAxisSet().adjustRange();
        chart.getPlotArea().addMouseMoveListener(new MouseMoveListener() {

            @Override
            public void mouseMove(MouseEvent e) {
                for (ISeries series : chart.getSeriesSet().getSeries()) {
                    Rectangle[] rs = ((IBarSeries) series).getBounds();
                    for (int i = 0; i < rs.length; i++) {
                        if (rs[i] != null) {
                            if (rs[i].x < e.x && e.x < rs[i].x + rs[i].width && rs[i].y < e.y && e.y < rs[i].y + rs[i].height) {
                                setToolTipText(series, i);
                                return;
                            }
                        }
                    }
                }
                chart.getPlotArea().setToolTipText(null);
            }

            private void setToolTipText(ISeries series, int index) {
                chart.getPlotArea().setToolTipText("Series: " + series.getId() + "\nValue: " + series.getYSeries()[index]);
            }
        });
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }
}
