package org.alcibiade.eternity.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JComponent;

public class GridScoreGraph extends JComponent implements GridScoreRecorderObserver {

    private static final long serialVersionUID = 1L;

    private static final Color col_back = Color.GRAY.darker();

    private static final Color col_score = Color.GREEN.brighter();

    private GridScoreRecorder scorerecorder;

    public GridScoreGraph(GridScoreRecorder recorder) {
        scorerecorder = recorder;
        scorerecorder.addObserver(this);
        setBorder(BorderFactory.createEtchedBorder());
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Dimension size = getSize();
        GradientPaint grad_back = new GradientPaint(new Point2D.Double(0, 0), col_back.brighter(), new Point2D.Double(0, size.height), col_back.darker());
        GradientPaint grad_score = new GradientPaint(new Point2D.Double(0, 0), col_score.brighter(), new Point2D.Double(0, size.height), col_score.darker());
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(grad_back);
        g2.fill(new Rectangle2D.Double(0, 0, size.getWidth(), size.getHeight()));
        Map<Long, Integer> graphdata = scorerecorder.getHistory();
        int maxval = scorerecorder.getMaxValue();
        long firsttimestamp = Long.MAX_VALUE;
        long lasttimestamp = Long.MIN_VALUE;
        Iterator<Long> it_data = graphdata.keySet().iterator();
        while (it_data.hasNext()) {
            long ts = it_data.next();
            if (firsttimestamp > ts) {
                firsttimestamp = ts;
            }
            if (lasttimestamp < ts) {
                lasttimestamp = ts;
            }
        }
        if (firsttimestamp < lasttimestamp) {
            Point2D lastpoint = null;
            g2.setPaint(grad_score);
            Iterator<Long> it_time = graphdata.keySet().iterator();
            while (it_time.hasNext()) {
                long ts = it_time.next();
                int val = graphdata.get(ts);
                double p_x = (ts - firsttimestamp) * size.width / (lasttimestamp - firsttimestamp);
                double p_y = size.height - val * size.height / maxval;
                Point2D point = new Point2D.Double(p_x, p_y);
                if (lastpoint != null) {
                    Line2D segment = new Line2D.Double(lastpoint, point);
                    g2.draw(segment);
                }
                lastpoint = point;
            }
        }
    }

    public void notifyRecorderUpdated() {
        repaint();
    }
}
