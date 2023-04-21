package com.atech.graphics.graphs;

import java.awt.Component;
import java.awt.Rectangle;
import javax.swing.JDialog;
import javax.swing.JFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import com.atech.help.HelpCapable;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATDataAccessAbstract;

public abstract class AbstractGraphViewAndProcessor implements GraphViewInterface, GraphViewDataProcessorInterface {

    protected ATDataAccessAbstract m_da = null;

    protected I18nControlAbstract m_ic = null;

    protected JFreeChart chart;

    protected ChartPanel chart_panel;

    protected Component parent;

    /**
     * Constructor
     * 
     * @param da
     */
    public AbstractGraphViewAndProcessor(ATDataAccessAbstract da) {
        this.m_da = da;
        this.m_ic = da.getI18nControlInstance();
    }

    /**
     * Is Help Enabled
     * 
     * @return true if help is enabled
     */
    public boolean isHelpEnabled() {
        return (getHelpId() != null);
    }

    /**
     * Get Chart
     * 
     * @return JFreeChart instance
     */
    public JFreeChart getChart() {
        if (chart == null) {
            createChart();
            setPlot(chart);
        }
        return chart;
    }

    /**
     * Get Chart Panel
     * 
     * @return ChartPanel instance
     */
    public ChartPanel getChartPanel() {
        if (chart_panel == null) createChartPanel();
        return this.chart_panel;
    }

    /**
     * Create Chart
     */
    public abstract void createChart();

    /**
     * Create Chart Panel
     */
    public abstract void createChartPanel();

    /**
     * Get Processor
     * 
     * @return GraphViewDataProcessorInterface instance (typed)
     */
    public GraphViewDataProcessorInterface getProcessor() {
        return this;
    }

    /**
     * Reload Data - This is method which should be called after setControllerData is set
     *     with new data. This method should call loadData() and preprocessData(). 
     */
    public void reloadData() {
        this.loadData();
        this.preprocessData();
    }

    /**
     * Repaint
     * 
     * @see com.atech.graphics.graphs.GraphViewInterface#repaint()
     */
    public void repaint() {
        this.chart_panel.repaint();
    }

    /**
     * Set Controler Data - used by controler to change set of data
     * 
     * @param data objects (controler can implement custom class, which is recognized by view instance)
     */
    public void setControlerData(Object data) {
        if (this.getProcessor() != null) this.getProcessor().setControllerData(data);
    }

    /**
     * Get Controler Interface instance
     * 
     * @return GraphViewControlerInterface instance or null
     */
    public GraphViewControlerInterface getControler() {
        return null;
    }

    /**
     * Get Title (used by GraphViewer)
     * 
     * @return title as string 
     */
    public String getTitle() {
        return null;
    }

    /**
     * Get Viewer Dialog Bounds (used by GraphViewer)
     * 
     * @return Rectangle object
     */
    public Rectangle getViewerDialogBounds() {
        return null;
    }

    /**
     * Set Parent
     */
    public void setParent(Component cmp) {
        this.parent = cmp;
    }

    /**
     * Get Parent
     */
    public Component getParent() {
        return this.parent;
    }

    /**
     * Close
     */
    public void close() {
        if (parent == null) {
            System.out.println("ERROR: Using close command, without setting parent is not allowed.");
            return;
        }
        if (this.parent instanceof JDialog) {
            JDialog d = (JDialog) this.parent;
            d.dispose();
        } else if (this.parent instanceof JFrame) {
            JFrame f = (JFrame) this.parent;
            f.dispose();
        } else {
            System.out.println("ERROR: Parent is of wrong type. Close command not initiated. Parent must be either JDialog or JFrame.");
        }
    }

    /**
     * Set Controller Data
     * 
     * @see com.atech.graphics.graphs.GraphViewDataProcessorInterface#setControllerData(java.lang.Object)
     */
    public void setControllerData(Object data) {
    }
}
