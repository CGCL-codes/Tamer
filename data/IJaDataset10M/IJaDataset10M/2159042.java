package org.spantus.exp.segment.draw;

import java.awt.GridLayout;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jfree.chart.ChartPanel;
import org.jfree.ui.RefineryUtilities;
import org.spantus.core.marker.MarkerSet;
import org.spantus.core.marker.MarkerSetHolder;
import org.spantus.core.marker.MarkerSetHolder.MarkerSetHolderEnum;
import org.spantus.exp.segment.beans.ComparisionResult;
import org.spantus.exp.segment.exec.DecisionSegmentationExp;
import org.spantus.logger.Logger;

/**
 * 
 * 
 * @author Mindaugas Greibus
 * 
 * @since 0.0.1
 * 
 *  Created Oct 14, 2008
 * 
 */
public class DrawSegmentAnalysis {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private AbstractGraphGenerator graphGenerator;

    protected Logger log = Logger.getLogger(getClass());

    /**
	 * A demonstration application showing an XY series containing a null value.
	 * 
	 * @param title
	 *            the frame title.
	 */
    public DrawSegmentAnalysis(AbstractGraphGenerator graphGenerator) {
        this.graphGenerator = graphGenerator;
    }

    public void draw() {
        List<ComparisionResult> results = graphGenerator.compare();
        Map<String, Float> totals = new LinkedHashMap<String, Float>();
        Float min = Float.MAX_VALUE;
        for (ComparisionResult comparisionResult : results) {
            draw(comparisionResult);
            totals.put(comparisionResult.getName(), comparisionResult.getTotalResult());
            min = Math.min(min, Math.abs(comparisionResult.getTotalResult()));
        }
        log.debug("Min: " + min + "; Totals: " + totals);
    }

    public AbstractGraphGenerator getGraphGenerator() {
        return graphGenerator;
    }

    JFrame frame;

    protected JFrame getFrame() {
        if (frame == null) {
            frame = new JFrame("Analysis result");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(0, 2));
            frame.setContentPane(panel);
        }
        return frame;
    }

    protected void draw(ComparisionResult result) {
        JFrame frame = getFrame();
        ChartPanel chartPanel = new ChartPanel(graphGenerator.getChart(result));
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        frame.getContentPane().add(chartPanel);
        frame.pack();
        RefineryUtilities.positionFrameRandomly(frame);
        frame.setVisible(true);
    }

    protected MarkerSet getWordMarkerSet(MarkerSetHolder holder) {
        return holder.getMarkerSets().get(MarkerSetHolderEnum.word.name());
    }

    public static void main(final String[] args) {
        final DrawSegmentAnalysis drawAnlysis = new DrawSegmentAnalysis(new DecisionSegmentationExp());
        drawAnlysis.getGraphGenerator().process(null, AbstractGraphGenerator.DEFAULT_TEST_DATA_PATH);
        drawAnlysis.draw();
    }
}
