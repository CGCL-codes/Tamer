package org.velma.plots.frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;
import org.velma.plots.InterFrameByPosPlotPanel;

/**
 * The cover panel for plot by position
 * It contains the plot panel and the button panel
 * 
 * @author Hyun Kyu Shim
 *
 */
public class PlotByPosCoverPanel extends JPanel {

    private static final long serialVersionUID = 5179679894829357086L;

    private final JPanel buttonPanel;

    private final InterFrameByPosPlotPanel plotPanel;

    private PlotByPosFrame frame;

    private boolean isPlotShown;

    private int index;

    public PlotByPosCoverPanel(PlotByPosButtonPanel buttonPanel, InterFrameByPosPlotPanel plotPanel) {
        super();
        this.buttonPanel = buttonPanel;
        this.plotPanel = plotPanel;
        isPlotShown = true;
        setLayout(new BorderLayout());
        buttonPanel.setCoverPanel(this);
        add(buttonPanel, BorderLayout.PAGE_START);
        add(plotPanel, BorderLayout.LINE_START);
    }

    public InterFrameByPosPlotPanel getPlot() {
        return plotPanel;
    }

    /**
	 * Called by its child button panel to zoom in X
	 */
    public void zoomXPlus() {
        frame.zoomXPlus();
    }

    /**
	 * Called by its child button panel to zoom out X
	 */
    public void zoomXMinus() {
        frame.zoomXMinus();
    }

    /**
	 * Called by its child button panel to zoom in Y
	 */
    public void zoomYPlus() {
        int graphHeight = plotPanel.getHeight();
        graphHeight += 20;
        plotPanel.setPreferredSize(new Dimension(frame.graphPosWidth * frame.positions + 40, graphHeight));
        plotPanel.setSize(plotPanel.getPreferredSize());
        frame.resize();
    }

    /**
	 * Called by its child button panel to zoom out Y
	 */
    public void zoomYMinus() {
        int graphHeight = plotPanel.getHeight();
        graphHeight = Math.max(80, graphHeight - 20);
        plotPanel.setPreferredSize(new Dimension(frame.graphPosWidth * frame.positions + 40, graphHeight));
        plotPanel.setSize(plotPanel.getPreferredSize());
        frame.resize();
    }

    /**
	 * Called by its child button panel to move this plot up
	 */
    public void moveUp() {
        frame.moveUp(index);
    }

    /**
	 * Called by its child button panel to move this plot down
	 */
    public void moveDown() {
        frame.moveDown(index);
    }

    /**
	 * Called by its child button panel to show the plot
	 */
    public void showPlot() {
        if (!isPlotShown) {
            plotPanel.setVisible(true);
            isPlotShown = true;
            frame.resize();
        }
    }

    /**
	 * Called by its child button panel to hide the plot
	 */
    public void hidePlot() {
        if (isPlotShown) {
            plotPanel.setPreferredSize(plotPanel.getSize());
            plotPanel.setVisible(false);
            isPlotShown = false;
            frame.resize();
        }
    }

    /**
	 * Called by its child button panel to close the plot
	 */
    public void close() {
        frame.close(index);
    }

    /**
	 * @return true if graph is showing
	 */
    public boolean isPlotShowing() {
        return isPlotShown;
    }

    /**
	 * 
	 * @return
	 */
    public int getPrefferedHeight() {
        int h = buttonPanel.getPreferredSize().height;
        if (isPlotShown) h += plotPanel.getPreferredSize().height;
        return h;
    }

    /**
	 * sets the frame this cover belongs to
	 * @param frame
	 */
    public void setParentFrame(PlotByPosFrame frame) {
        this.frame = frame;
    }

    /**
	 * sets the index of the cover in the frame
	 * @param index
	 */
    public void setIndex(int index) {
        this.index = index;
    }
}
