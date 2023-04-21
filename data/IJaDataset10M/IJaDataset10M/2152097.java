package de.grogra.pf.ui.swing;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.*;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StatisticalBarRenderer;
import org.jfree.data.statistics.StatisticalCategoryDataset;
import de.grogra.util.*;
import de.grogra.pf.data.*;
import de.grogra.pf.registry.*;
import de.grogra.pf.ui.*;

class ChartSupport extends PanelSupport implements de.grogra.pf.ui.ChartPanel, ModifiableMap.Producer {

    private final CPanel panel;

    private Dataset dataset;

    private int plot;

    static class CPanel extends org.jfree.chart.ChartPanel implements Command {

        private boolean pending;

        private boolean pendingScale;

        private Rectangle2D pendingBounds;

        private Object lock = new Object();

        CPanel() {
            super(null, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_MINIMUM_DRAW_WIDTH, DEFAULT_MINIMUM_DRAW_HEIGHT, DEFAULT_MAXIMUM_DRAW_WIDTH, DEFAULT_MAXIMUM_DRAW_HEIGHT, true, true, false, true, true, true);
        }

        public String getCommandName() {
            return null;
        }

        public void run(Object info, Context ctx) {
            boolean scale;
            Rectangle2D bounds;
            synchronized (lock) {
                if (pendingBounds != info) {
                    return;
                }
                scale = pendingScale;
                bounds = pendingBounds;
                pending = false;
            }
            super.drawChart(scale, bounds);
            repaint();
        }

        @Override
        protected void drawChart(boolean scale, Rectangle2D bounds) {
            PanelSupport p = PanelSupport.get(this);
            if (p == null) {
                super.drawChart(scale, bounds);
            } else {
                synchronized (lock) {
                    if (pending) {
                        if ((pendingScale == scale) && pendingBounds.equals(bounds)) {
                            return;
                        }
                    }
                    pending = true;
                    pendingScale = scale;
                    pendingBounds = (Rectangle2D) bounds.clone();
                }
                UI.getJobManager(p).runLater(100, this, pendingBounds, p);
            }
        }
    }

    public ChartSupport() {
        super(new SwingPanel(null));
        Container c = ((SwingPanel) getComponent()).getContentPane();
        c.setLayout(new GridLayout(1, 1));
        c.add(panel = new CPanel());
        mapProducer = this;
    }

    @Override
    protected void configure(Map params) {
        super.configure(params);
        String s = (String) params.get("dataset", null);
        if (s != null) {
            Item i = Item.resolveItem(getWorkbench(), s);
            if (i instanceof ObjectItem) {
                Object o = ((ObjectItem) i).getObject();
                if (o instanceof Dataset) {
                    setChart((Dataset) o, Utils.getInt(params, "plot", LINE_PLOT), params);
                }
            }
        }
    }

    @Override
    protected void disposeImpl() {
        super.disposeImpl();
        disposeChart();
    }

    private void disposeChart() {
        JFreeChart c = panel.getChart();
        if (c != null) {
            panel.setChart(null);
            c.getPlot().dispose();
        }
    }

    public void addMappings(ModifiableMap out) {
        if (dataset.getProvider() instanceof ObjectItem) {
            out.put("dataset", ((ObjectItem) dataset.getProvider()).getAbsoluteName());
        }
        out.put("plot", Integer.valueOf(plot));
    }

    public void setChart(Dataset dataset, int plot, Map options) {
        this.dataset = dataset;
        if (plot != PREVIOUS_PLOT) {
            this.plot = plot;
        }
        disposeChart();
        JFreeChart jfc;
        String t = dataset.getTitle(), r = dataset.getCategoryLabel(), c = dataset.getValueLabel();
        DatasetAdapter ds = dataset.asDatasetAdapter();
        StatisticalCategoryDataset sds = ds;
        switch(plot & PLOT_TYPE_MASK) {
            case AREA_PLOT:
                jfc = ChartFactory.createAreaChart(t, r, c, ds, PlotOrientation.VERTICAL, true, false, false);
                break;
            case STACKED_AREA_PLOT:
                jfc = ChartFactory.createStackedAreaChart(t, r, c, ds, PlotOrientation.VERTICAL, true, false, false);
                break;
            case BAR_PLOT:
                jfc = ChartFactory.createBarChart(t, r, c, ds, PlotOrientation.VERTICAL, true, false, false);
                break;
            case STACKED_BAR_PLOT:
                jfc = ChartFactory.createStackedBarChart(t, r, c, ds, PlotOrientation.VERTICAL, true, false, false);
                break;
            case LINE_PLOT:
                jfc = ChartFactory.createLineChart(t, r, c, ds, PlotOrientation.VERTICAL, true, false, false);
                break;
            case PIE_PLOT:
                jfc = ChartFactory.createPieChart(t, ds, true, false, false);
                break;
            case SCATTER_PLOT:
                jfc = ChartFactory.createScatterPlot(t, r, c, ds, PlotOrientation.VERTICAL, true, false, false);
                break;
            case XY_PLOT:
                jfc = ChartFactory.createXYLineChart(t, r, c, ds, PlotOrientation.VERTICAL, true, false, false);
                break;
            case HISTOGRAM:
                jfc = ChartFactory.createHistogram(t, r, c, dataset.toHistogram(), PlotOrientation.VERTICAL, true, false, false);
                break;
            case STATISTICS:
                sds = dataset.toStatistics();
            case STATISTICAL_BAR_PLOT:
                jfc = ChartFactory.createBarChart(t, r, c, sds, PlotOrientation.VERTICAL, true, false, false);
                BarRenderer old = (BarRenderer) ((CategoryPlot) jfc.getPlot()).getRenderer();
                StatisticalBarRenderer renderer = new StatisticalBarRenderer();
                renderer.setNegativeItemLabelPosition(old.getNegativeItemLabelPosition());
                renderer.setPositiveItemLabelPosition(old.getPositiveItemLabelPosition());
                ((CategoryPlot) jfc.getPlot()).setRenderer(renderer);
                break;
            default:
                throw new IllegalArgumentException();
        }
        Plot p = jfc.getPlot();
        if ((plot & X_LOG) != 0) {
            if (p instanceof XYPlot) {
                XYPlot xy = (XYPlot) p;
                for (int i = 0; i < xy.getDomainAxisCount(); i++) {
                    xy.setDomainAxis(i, toLogAxis(xy.getDomainAxis(i)));
                }
            }
        }
        if ((plot & Y_LOG) != 0) {
            if (p instanceof XYPlot) {
                XYPlot xy = (XYPlot) p;
                for (int i = 0; i < xy.getRangeAxisCount(); i++) {
                    xy.setRangeAxis(i, toLogAxis(xy.getRangeAxis(i)));
                }
            } else if (p instanceof CategoryPlot) {
                CategoryPlot cp = (CategoryPlot) p;
                for (int i = 0; i < cp.getRangeAxisCount(); i++) {
                    cp.setRangeAxis(i, toLogAxis(cp.getRangeAxis(i)));
                }
            }
        }
        panel.setChart(jfc);
    }

    private static LogarithmicAxis toLogAxis(ValueAxis v) {
        LogarithmicAxis a = new LogarithmicAxis(v.getLabel());
        a.setAllowNegativesFlag(true);
        a.setStrictValuesFlag(false);
        return a;
    }
}
