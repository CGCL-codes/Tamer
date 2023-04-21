package info.monitorenter.gui.chart.demos;

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.IErrorBarPainter;
import info.monitorenter.gui.chart.IErrorBarPolicy;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.errorbars.ErrorBarPainter;
import info.monitorenter.gui.chart.errorbars.ErrorBarPolicyAbsoluteSummation;
import info.monitorenter.gui.chart.pointpainters.PointPainterDisc;
import info.monitorenter.gui.chart.pointpainters.PointPainterLine;
import info.monitorenter.gui.chart.traces.Trace2DSimple;
import info.monitorenter.gui.chart.views.ChartPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Title: StaticChartErrorBarLineDisc
 * <p>
 * 
 * Description: A demonstration of the minimal code to set up a chart with
 * static data and an relative error bar policy that paints relative error bars
 * by line segments with a disc at the end in y dimension (positive and
 * negative).
 * <p>
 * 
 * @author Achim Westermann
 * 
 * @version $Revision: 1.13 $
 */
public final class StaticChartErrorBarLineDisc extends JPanel {

    /**
   * Generated for <code>serialVersionUID</code>.
   */
    private static final long serialVersionUID = 3257009847668192306L;

    /**
   * Main entry.
   * <p>
   * 
   * @param args
   *          ignored.
   */
    public static void main(final String[] args) {
        for (int i = 0; i < 1; i++) {
            JFrame frame = new JFrame(StaticChartErrorBarLineDisc.class.getName());
            frame.getContentPane().add(new StaticChartErrorBarLineDisc());
            frame.addWindowListener(new WindowAdapter() {

                /**
         * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
         */
                @Override
                public void windowClosing(final WindowEvent e) {
                    System.exit(0);
                }
            });
            frame.setSize(600, 600);
            frame.setLocation(i % 3 * 200, i / 3 * 100);
            frame.setVisible(true);
        }
    }

    /**
   * Defcon.
   */
    private StaticChartErrorBarLineDisc() {
        this.setLayout(new BorderLayout());
        Chart2D chart = new Chart2D();
        ITrace2D trace = new Trace2DSimple();
        chart.addTrace(trace);
        trace.setColor(Color.RED);
        IErrorBarPolicy<?> errorBarPolicy = new ErrorBarPolicyAbsoluteSummation(10, 10);
        errorBarPolicy.setShowNegativeYErrors(true);
        errorBarPolicy.setShowPositiveYErrors(true);
        trace.setErrorBarPolicy(errorBarPolicy);
        IErrorBarPainter errorBarPainter = new ErrorBarPainter();
        errorBarPainter.setEndPointPainter(new PointPainterDisc());
        errorBarPainter.setEndPointColor(Color.GRAY);
        errorBarPainter.setConnectionPainter(new PointPainterLine());
        errorBarPainter.setConnectionColor(Color.LIGHT_GRAY);
        errorBarPolicy.setErrorBarPainter(errorBarPainter);
        for (double i = 2; i < 40; i++) {
            trace.addPoint(i * 100, Math.random() * i + i * 10);
        }
        this.add(new ChartPanel(chart), BorderLayout.CENTER);
    }
}
