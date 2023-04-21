package info.monitorenter.gui.chart.demos;

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.IAxisScalePolicy;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.ITracePoint2D;
import info.monitorenter.gui.chart.ITracePointProvider;
import info.monitorenter.gui.chart.axis.AAxis;
import info.monitorenter.gui.chart.axis.AxisLinear;
import info.monitorenter.gui.chart.traces.Trace2DSimple;
import info.monitorenter.gui.chart.views.ChartPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Iterator;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Title: MultipleAxesStaticChart
 * <p>
 * Description: A minimal example for rendering a static chart with multiple
 * axes.
 * <p>
 * 
 * @author Achim Westermann
 * @version $Revision: 1.15 $
 */
public final class MultiAxesStaticChart extends JPanel {

    /** Generated <code>serialVersionUID</code>. */
    private static final long serialVersionUID = 3476998470009995195L;

    /**
   * Main entry.
   * <p>
   * 
   * @param args
   *          ignored.
   */
    public static void main(final String[] args) {
        for (int i = 0; i < 1; i++) {
            JFrame frame = new JFrame(MultiAxesStaticChart.class.getName());
            frame.getContentPane().add(new MultiAxesStaticChart());
            frame.addWindowListener(new WindowAdapter() {

                /**
         * 
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
    private MultiAxesStaticChart() {
        this.setLayout(new BorderLayout());
        Chart2D chart = new Chart2D();
        chart.setToolTipType(Chart2D.ToolTipType.VALUE_SNAP_TO_TRACEPOINTS);
        ITracePointProvider pointCreator = chart.getTracePointProvider();
        ITrace2D apples = new Trace2DSimple();
        apples.setColor(Color.RED);
        apples.setName("Apples");
        ITrace2D pears = new Trace2DSimple();
        pears.setColor(Color.BLUE);
        pears.setName("Pears");
        ITrace2D carrots = new Trace2DSimple();
        carrots.setColor(Color.MAGENTA);
        carrots.setName("Carrots");
        AAxis<?> yAxisApples = new AxisLinear<IAxisScalePolicy>();
        yAxisApples.getAxisTitle().setTitle("Y-Apples");
        yAxisApples.getAxisTitle().setTitleColor(Color.RED);
        AAxis<?> yAxisPears = new AxisLinear<IAxisScalePolicy>();
        yAxisPears.getAxisTitle().setTitle("Y-Pears");
        yAxisPears.getAxisTitle().setTitleColor(Color.BLUE);
        AAxis<?> yAxisCarrots = new AxisLinear<IAxisScalePolicy>();
        yAxisCarrots.getAxisTitle().setTitle("Y-Carrots");
        yAxisCarrots.getAxisTitle().setTitleColor(Color.MAGENTA);
        chart.setAxisYLeft(yAxisApples, 0);
        chart.setAxisYRight(yAxisPears, 0);
        chart.addAxisYLeft(yAxisCarrots);
        AAxis<?> xAxisApples = new AxisLinear<IAxisScalePolicy>();
        xAxisApples.getAxisTitle().setTitle("X-Apples");
        xAxisApples.getAxisTitle().setTitleColor(Color.RED);
        AAxis<?> xAxisPears = new AxisLinear<IAxisScalePolicy>();
        xAxisPears.getAxisTitle().setTitle("X-Pears");
        xAxisPears.getAxisTitle().setTitleColor(Color.BLUE);
        AAxis<?> xAxisCarrots = new AxisLinear<IAxisScalePolicy>();
        xAxisCarrots.getAxisTitle().setTitle("X-Carrots");
        xAxisCarrots.getAxisTitle().setTitleColor(Color.MAGENTA);
        chart.setAxisXBottom(xAxisApples, 0);
        chart.setAxisXTop(xAxisPears, 0);
        chart.addAxisXBottom(xAxisCarrots);
        chart.addTrace(apples, xAxisApples, yAxisApples);
        chart.addTrace(pears, xAxisPears, yAxisPears);
        chart.addTrace(carrots, xAxisCarrots, yAxisCarrots);
        double time = System.currentTimeMillis();
        int i;
        for (i = 0; i < 120; i++) {
            apples.addPoint(pointCreator.createTracePoint(time + i * 10000, (10.0 + Math.random()) * i, apples));
        }
        ITracePoint2D copyPoint;
        Iterator<ITracePoint2D> it = apples.iterator();
        i = 0;
        while (it.hasNext()) {
            copyPoint = it.next();
            pears.addPoint(pointCreator.createTracePoint(i * 0.001, copyPoint.getY() + (Math.random() * 1000), pears));
            i++;
        }
        it = apples.iterator();
        i = 0;
        while (it.hasNext()) {
            copyPoint = it.next();
            carrots.addPoint(pointCreator.createTracePoint(i * 100, 100 - copyPoint.getY(), carrots));
            i++;
        }
        this.add(new ChartPanel(chart), BorderLayout.CENTER);
    }
}
