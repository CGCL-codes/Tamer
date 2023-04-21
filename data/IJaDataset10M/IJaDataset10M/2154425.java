package com.nexirius.framework.trend;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class SimpleTrend extends JPanel {

    MyCanvas canvas;

    TrendGridDate xGrid;

    TrendGridDouble yGrid;

    DateRange dateRange;

    DoubleRange yRange;

    ArrayList sampleArrays = new ArrayList();

    public SimpleTrend() {
        setLayout(new BorderLayout());
        canvas = new MyCanvas();
        add(canvas, BorderLayout.CENTER);
    }

    public void setDateRange(DateRange range) {
        xGrid = new TrendGridDate(range);
        dateRange = range;
    }

    public void setYRange(DoubleRange range) {
        yRange = range;
        yGrid = new TrendGridDouble(yRange);
    }

    public void setSamples(SampleArrayModel s) {
        sampleArrays.clear();
        addSamples(s);
    }

    public void addSamples(SampleArrayModel s) {
        sampleArrays.add(s);
        recalcYRange();
    }

    public void recalcYRange() {
        yRange = null;
        for (Iterator it = sampleArrays.iterator(); it.hasNext(); ) {
            SampleArrayModel sampleArrayModel = (SampleArrayModel) it.next();
            DoubleRange range = sampleArrayModel.getYRange(dateRange);
            if (yRange == null) {
                yRange = range;
            } else {
                yRange.extend(range.min());
                yRange.extend(range.max());
            }
        }
        yGrid = new TrendGridDouble(yRange);
    }

    public void paintSamples(Graphics g, Rectangle rect) {
        for (Iterator it = sampleArrays.iterator(); it.hasNext(); ) {
            paintSamples(g, rect, (SampleArrayModel) it.next());
        }
    }

    public void paintSamples(Graphics g, Rectangle rect, SampleArrayModel samples) {
        g.setColor(samples.getColor());
        SampleModel s1 = samples.getFirstSample(dateRange.min());
        int x1 = xGrid.getPosition(rect, s1.getTime());
        int y1 = yGrid.getPosition(rect, s1.getValue());
        SampleModel s2 = samples.getNextSample(dateRange.max());
        while (s2 != null) {
            int x2 = xGrid.getPosition(rect, s2.getTime());
            int y2 = yGrid.getPosition(rect, s2.getValue());
            g.drawLine(x1, y1, x2, y1);
            g.drawLine(x2, y1, x2, y2);
            s1 = s2;
            x1 = x2;
            y1 = y2;
            s2 = samples.getNextSample(dateRange.max());
        }
    }

    class MyCanvas extends JPanel {

        public MyCanvas() {
            setOpaque(true);
        }

        public void paint(Graphics g) {
            super.paint(g);
            int xoff = 100;
            int yoff = 30;
            int w = (int) getBounds().getWidth();
            int h = (int) getBounds().getHeight();
            Rectangle graphRect = new Rectangle(xoff, 0, w - xoff, h - yoff);
            Rectangle yRect = new Rectangle(0, 0, xoff, h - yoff);
            Rectangle xRect = new Rectangle(xoff, h - yoff, w - xoff, yoff);
            Rectangle restRect = new Rectangle(0, h - yoff, xoff, yoff);
            LabelPositionVector v = xGrid.getPositions(graphRect);
            g.setColor(Color.gray);
            xGrid.paintGrid(g, graphRect, v);
            g.setColor(Color.black);
            xGrid.paintScale(g, xRect, v);
            v = yGrid.getPositions(graphRect);
            g.setColor(Color.gray);
            yGrid.paintGrid(g, graphRect, v);
            g.setColor(Color.blue);
            yGrid.paintScale(g, yRect, v);
            paintSamples(g, graphRect);
            xGrid.paintRange(g, restRect);
        }
    }

    public static void main(String argv[]) {
        long t = new java.util.Date().getTime();
        JFrame frame = new JFrame();
        frame.setLocation(100, 100);
        frame.setSize(400, 300);
        SimpleTrend trend = new SimpleTrend();
        trend.addSamples(new SampleArrayModelImpl(Color.red, createSamples(t)));
        trend.addSamples(new SampleArrayModelImpl(Color.blue, createSamples(t)));
        trend.addSamples(new SampleArrayModelImpl(Color.magenta, createSamples(t + 100000000)));
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(trend, BorderLayout.CENTER);
        Calendar date = Calendar.getInstance();
        trend.setDateRange(new DateRange(date.get(Calendar.YEAR), date.get(Calendar.MONTH) + 1, date.get(Calendar.DAY_OF_MONTH), 10));
        frame.setVisible(true);
    }

    private static DoubleSampleModel[] createSamples(long t) {
        DoubleSampleModel samples[] = new DoubleSampleModel[1000];
        double value = 100;
        for (int i = 0; i < 1000; ++i) {
            value += Math.random() - 0.5;
            samples[i] = new DoubleSampleModel(t + 600000 * i, value);
        }
        return samples;
    }
}
