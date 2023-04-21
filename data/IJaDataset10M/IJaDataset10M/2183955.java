package info.monitorenter.gui.chart.demos;

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.io.ADataCollector;
import info.monitorenter.gui.chart.io.RandomDataCollectorOffset;
import info.monitorenter.gui.chart.traces.Trace2DLtd;
import info.monitorenter.gui.chart.views.ChartPanel;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

/**
 * Demonstrates minimal effort to create multiple charts in one window.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * 
 */
public final class MultipleCharts {

    /**
   * Main entry.
   * <p>
   * 
   * @param args
   *          ignored
   */
    public static void main(final String[] args) {
        JFrame frame = new JFrame("MinimalDynamicChart");
        frame.setSize(500, 400);
        frame.addWindowListener(new WindowAdapter() {

            /**
       * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
       */
            @Override
            public void windowClosing(final WindowEvent e) {
                System.exit(0);
            }
        });
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new GridLayout(2, 2));
        Chart2D chart;
        ITrace2D trace;
        ADataCollector collector;
        Color[] colors = { Color.RED, Color.GREEN, Color.BLUE, Color.MAGENTA };
        Chart2D[] charts = new Chart2D[4];
        for (int i = 1; i < 5; i++) {
            chart = new Chart2D();
            charts[i - 1] = chart;
            trace = new Trace2DLtd(400);
            trace.setColor(colors[i - 1]);
            collector = new RandomDataCollectorOffset(trace, 70 * i);
            chart.addTrace(trace);
            collector.start();
            contentPane.add(new ChartPanel(chart));
        }
        frame.setVisible(true);
    }

    /** Defcon. */
    private MultipleCharts() {
    }
}
