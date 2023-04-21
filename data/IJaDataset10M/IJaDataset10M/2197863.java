package com.inetmon.jn.statistic.distribution.views;

import java.awt.Color;
import java.net.URL;
import java.util.Vector;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardLegend;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.osgi.framework.Bundle;
import com.inetmon.jn.core.CorePlugin;
import com.inetmon.jn.graph.Graph;
import com.inetmon.jn.graph.IGraphConstant;
import com.inetmon.jn.graph.data.StatisticalData;
import com.inetmon.jn.graph.views.GraphViewer;
import com.inetmon.jn.statistic.IProtocolStatisticRecorder;
import com.inetmon.jn.statistic.Messages;
import com.inetmon.jn.statistic.distribution.DistributionPlugin;

/**
 * @author   Administrator
 */
public class VLANPacketSizeView extends org.eclipse.ui.part.ViewPart {

    public static final String ID_VIEW = "com.inetmon.jn.statistic.distribution.views.VLANBase2View";

    public static final int[] PACKET_SIZE = { 32, 48, 64, 96, 128, 256, 512, 1024, 2048 };

    /**
	 * Interface for accessing the used data recorder
	 */
    protected static IProtocolStatisticRecorder statRecorder;

    private static JFreeChart chart;

    static PieDataset dataset = new DefaultPieDataset();

    static DefaultPieDataset pds = (DefaultPieDataset) dataset;

    static DefaultCategoryDataset Dataset = new DefaultCategoryDataset();

    static DefaultCategoryDataset cds = (DefaultCategoryDataset) Dataset;

    private Action setVLANID;

    /**
	 * Graph Plugin graph object
	 */
    protected static Graph graph;

    /**
	 * Graph Plugin viewer object
	 */
    protected static GraphViewer graphViewer;

    protected static StatisticalData statData;

    /**
	 * Type of graph actually shown
	 * @uml.property  name="type"
	 */
    protected static int type;

    public static long[] packetCount;

    /**
	 * Boolean value to store the cumulative state
	 * @uml.property  name="cumulative"
	 */
    protected static boolean cumulative;

    /**
	 * Symbolic value representing a pie graph
	 */
    public static final int TYPE_PIE = 0;

    /**
	 * Symbolic value representing an horizontal bar graph
	 */
    public static final int TYPE_HBAR = 1;

    /**
	 * Symbolic value representing a vertical bar graph
	 */
    public static final int TYPE_VBAR = 2;

    /**
	 * Number of available kinds of graphs
	 */
    public static final int TYPE_COUNT = 3;

    /**
	 * @uml.property  name="labels"
	 */
    public static String[] labels;

    public static final String TYPE_LABELS[] = { "Pie graph", "Horizontal bar graph", "Vertical bar graph" };

    public static String VName = "others";

    static boolean found = false;

    private static boolean updateFlag = false;

    private VLANPacketSizeStat PacSizeStat;

    private String Vid;

    private static Vector Vpacket;

    private static Vector vlanID;

    private static VLANPacketSizeView baseView;

    private static int counter = 0;

    public VLANPacketSizeView() {
        super();
        final VLANPacketSizeStat PacSizeStat = new VLANPacketSizeStat();
        this.graphViewer = null;
        this.statData = new StatisticalData();
        this.PacSizeStat = PacSizeStat;
        this.type = TYPE_PIE;
        Vpacket = new Vector();
        if (CorePlugin.getDefault().getVLANFieldID() == null) {
            setVID();
        }
    }

    private void setVID() {
        Vid = "1";
        CorePlugin.getDefault().setVLANFieldID(Vid);
        System.out.println(Vid);
    }

    public static void updateView(Vector Vpacket) {
        if (!Vpacket.isEmpty()) {
            int TotalValue = 0;
            labels = new String[PACKET_SIZE.length];
            packetCount = new long[PACKET_SIZE.length];
            for (int i = 0; i < PACKET_SIZE.length; i++) {
                labels[i] = (i == 0 ? Messages.getString("Statistic.PacketSizeStat.1") : String.valueOf(PACKET_SIZE[i - 1])) + Messages.getString("Statistic.PacketSizeStat.2") + String.valueOf(PACKET_SIZE[i]);
                packetCount[i] = 0;
            }
            for (int k = 0; k < Vpacket.size(); k++) {
                for (int i = 0; i < PACKET_SIZE.length; i++) {
                    if ((Vpacket.elementAt(k) != null)) {
                        if ((Integer) Vpacket.elementAt(k) <= PACKET_SIZE[i]) {
                            packetCount[i]++;
                            break;
                        }
                    }
                }
            }
            for (int i = 0; i < packetCount.length; i++) {
                TotalValue += packetCount[i];
            }
            cds.clear();
            for (int i = 0; i < labels.length; i++) {
                Double p = (new Double(packetCount[i]) * 100 / TotalValue);
                pds.setValue(labels[i], (new Integer((int) packetCount[i])));
                cds.setValue(p, labels[i], (labels[i] + "(" + (new Integer((int) packetCount[i])) + ")" + "-" + Math.round(p) + "%"));
            }
            graphViewer.setChart(chart);
        }
    }

    public static JFreeChart CreateChart(PieDataset dataset) {
        chart = ChartFactory.createPieChart3D(Messages.getString("Statistic.OSI.VLAN3"), dataset, false, false, false);
        setChartOptions(IGraphConstant.GLOBAL_BGCOLOR, IGraphConstant.GLOBAL_BRDCOLOR, IGraphConstant.GLOBAL_SHOWBORDER);
        setPiePlotOptions(IGraphConstant.GRAPH_BGCOLOR, IGraphConstant.GRAPH_BRDCOLOR, IGraphConstant.GRAPH_BASECOLOR, IGraphConstant.GRAPH_FORCECIRCULAR, IGraphConstant.GRAPH_ALPHA);
        setLabelOptions(IGraphConstant.LABEL_BGCOLOR, IGraphConstant.LABEL_TXTCOLOR, IGraphConstant.LABEL_BRDCOLOR, IGraphConstant.LABEL_LNKCOLOR, IGraphConstant.LABEL_SHADOWCOLOR, IGraphConstant.LABEL_FORMAT, IGraphConstant.LABEL_LEGENDFORMAT);
        setTitleOptions(IGraphConstant.TITLE_BGCOLOR, IGraphConstant.TITLE_COLOR);
        setLegendOptions(IGraphConstant.LEGEND_POSITION, Messages.getString("Statistic.PacketSizeStat.4"));
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setDataset(dataset);
        if (CorePlugin.getDefault().getVLANFieldID() != null) {
            chart.addSubtitle(new TextTitle("VLAN ID :" + CorePlugin.getDefault().getVLANFieldID()));
        }
        return chart;
    }

    public static void setLabelOptions(Color bgcol, Color txtcol, Color brdcol, Color lnkcol, Color shdcol, String labformat, String legformat) {
        PiePlot plot = (PiePlot) chart.getPlot();
        if (plot != null) {
            plot.setLabelBackgroundPaint(bgcol);
            plot.setLabelPaint(txtcol);
            plot.setLabelOutlinePaint(brdcol);
            plot.setLabelLinkPaint(lnkcol);
            plot.setLabelShadowPaint(shdcol);
            plot.setLabelGenerator(new StandardPieSectionLabelGenerator(labformat));
            plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator(legformat));
        }
    }

    public static void setPiePlotOptions(Color bgcol, Color brdcol, Color basecol, boolean forcecircular, float alpha) {
        PiePlot plot = (PiePlot) chart.getPlot();
        if (plot != null) {
            plot.setCircular(forcecircular);
            plot.setBackgroundPaint(bgcol);
            plot.setOutlinePaint(brdcol);
            plot.setBaseSectionOutlinePaint(basecol);
            plot.setForegroundAlpha(alpha);
        }
    }

    public static void setChartOptions(Color bgcol, Color brdcol, boolean showbrd) {
        chart.setBackgroundPaint(bgcol);
        chart.setBorderPaint(brdcol);
        chart.setBorderVisible(showbrd);
    }

    /**
	     * Set the legends options
	     * 
	     * @param position
	     *            Position of the legend (East.South,...)
	     */
    public static void setLegendOptions(int position, String title) {
        StandardLegend legend = new StandardLegend();
        legend.setAnchor(position);
        legend.setTitle(title);
        chart.setLegend(legend);
    }

    /**
	     * Set options for title
	     * 
	     * @param bgcol
	     *            Background color
	     * @param txtcol
	     *            Text color
	     */
    public static void setTitleOptions(Color bgcol, Color txtcol) {
        TextTitle title = chart.getTitle();
        if (title != null) {
            title.setPaint(txtcol);
            title.setBackgroundPaint(bgcol);
        }
    }

    public static JFreeChart CreateVBarChart(DefaultCategoryDataset Dataset) {
        chart = ChartFactory.createStackedBarChart3D(Messages.getString("Statistic.OSI.VLAN3"), IGraphConstant.AXIS_CATLABEL, IGraphConstant.AXIS_PERCENTLABEL, (CategoryDataset) Dataset, PlotOrientation.VERTICAL, false, false, false);
        setChartOptions(IGraphConstant.GLOBAL_BGCOLOR, IGraphConstant.GLOBAL_BRDCOLOR, IGraphConstant.GLOBAL_SHOWBORDER);
        setCategoryPlotOptions(IGraphConstant.GRAPH_GRIDVISIBLE, IGraphConstant.GRAPH_ALPHA);
        setLegendOptions(IGraphConstant.LEGEND_POSITION, Messages.getString("Statistic.PacketSizeStat.4"));
        setValueAxisOptions(IGraphConstant.AXIS_PERCENTLABEL, IGraphConstant.AXIS_MINPERCENT, IGraphConstant.AXIS_MAXPERCENT, IGraphConstant.AXIS_COLOR, IGraphConstant.AXIS_LABELCOLOR, IGraphConstant.AXIS_TICKCOLOR, IGraphConstant.AXIS_TICKLABELCOLOR);
        setCategoryAxisOptions(IGraphConstant.AXIS_CATLABEL, IGraphConstant.AXIS_COLOR, IGraphConstant.AXIS_LABELCOLOR, IGraphConstant.AXIS_TICKCOLOR, IGraphConstant.AXIS_TICKLABELCOLOR);
        setTitleOptions(IGraphConstant.TITLE_BGCOLOR, IGraphConstant.TITLE_COLOR);
        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        if (CorePlugin.getDefault().getVLANFieldID() != null) {
            chart.addSubtitle(new TextTitle("VLAN ID :" + CorePlugin.getDefault().getVLANFieldID()));
        }
        plot.setDataset(Dataset);
        return chart;
    }

    public static JFreeChart CreateHBarChart(DefaultCategoryDataset Dataset) {
        chart = ChartFactory.createStackedBarChart3D(Messages.getString("Statistic.OSI.VLAN3"), IGraphConstant.AXIS_CATLABEL, IGraphConstant.AXIS_PERCENTLABEL, (CategoryDataset) Dataset, PlotOrientation.HORIZONTAL, false, false, false);
        setChartOptions(IGraphConstant.GLOBAL_BGCOLOR, IGraphConstant.GLOBAL_BRDCOLOR, IGraphConstant.GLOBAL_SHOWBORDER);
        setCategoryPlotOptions(IGraphConstant.GRAPH_GRIDVISIBLE, IGraphConstant.GRAPH_ALPHA);
        setLegendOptions(IGraphConstant.LEGEND_POSITION, Messages.getString("Statistic.PacketSizeStat.4"));
        setValueAxisOptions(IGraphConstant.AXIS_PERCENTLABEL, IGraphConstant.AXIS_MINPERCENT, IGraphConstant.AXIS_MAXPERCENT, IGraphConstant.AXIS_COLOR, IGraphConstant.AXIS_LABELCOLOR, IGraphConstant.AXIS_TICKCOLOR, IGraphConstant.AXIS_TICKLABELCOLOR);
        setCategoryAxisOptions(IGraphConstant.AXIS_CATLABEL, IGraphConstant.AXIS_COLOR, IGraphConstant.AXIS_LABELCOLOR, IGraphConstant.AXIS_TICKCOLOR, IGraphConstant.AXIS_TICKLABELCOLOR);
        setTitleOptions(IGraphConstant.TITLE_BGCOLOR, IGraphConstant.TITLE_COLOR);
        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        if (CorePlugin.getDefault().getVLANFieldID() != null) {
            chart.addSubtitle(new TextTitle("VLAN ID :" + CorePlugin.getDefault().getVLANFieldID()));
        }
        plot.setDataset(Dataset);
        return chart;
    }

    public static void setValueAxisOptions(String label, double min, double max, Color axiscol, Color labelcol, Color tickcol, Color ticklabcol) {
        ValueAxis axis = chart.getCategoryPlot().getRangeAxis();
        if (axis != null) {
            axis.setRange(min, max);
            axis.setAutoRange(false);
            axis.setLabel(label);
            axis.setAxisLinePaint(axiscol);
            axis.setLabelPaint(labelcol);
            axis.setTickMarkPaint(tickcol);
            axis.setTickLabelPaint(ticklabcol);
        }
    }

    /**
	     * Configure the category axis
	     * 
	     * @param alabel
	     *            The axis label
	     * @param axiscol
	     *            Color of the axis
	     * @param labelcol
	     *            Color of the axis label label
	     * @param tickcol
	     *            Color of ticks
	     * @param ticklabcol
	     *            Color of ticks label
	     */
    public static void setCategoryAxisOptions(String alabel, Color axiscol, Color labelcol, Color tickcol, Color ticklabcol) {
        CategoryAxis axis = chart.getCategoryPlot().getDomainAxis(0);
        axis.setLabel(alabel);
        axis.setAxisLinePaint(axiscol);
        axis.setLabelPaint(labelcol);
        axis.setTickMarkPaint(tickcol);
        axis.setTickLabelPaint(ticklabcol);
    }

    /**
	     * Configure the bar graph options
	     * 
	     * @param gvisible
	     *            Is grid visible
	     * @param alpha
	     *            The transparency of rendered thrends
	     */
    public static void setCategoryPlotOptions(boolean gvisible, float alpha) {
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setRangeGridlinesVisible(gvisible);
        plot.setForegroundAlpha(alpha);
    }

    /**
	     * Change the orientation of the graph to horizontal
	     */
    protected static void setHorizontal() {
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setOrientation(PlotOrientation.HORIZONTAL);
    }

    /**
	     * Change the orientation of the graph to vertical
	     */
    protected void setVertical() {
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setOrientation(PlotOrientation.VERTICAL);
    }

    public static long[] getValues(int index) {
        if (index == 0) return packetCount; else return null;
    }

    /**
	 * @return
	 * @uml.property  name="labels"
	 */
    public String[] getLabels() {
        this.labels = labels;
        return labels;
    }

    public void changeType(int type) {
        this.type = type;
        switch(type) {
            case TYPE_PIE:
                chart = CreateChart(pds);
                break;
            case TYPE_VBAR:
                chart = CreateVBarChart(cds);
                break;
            case TYPE_HBAR:
                chart = CreateHBarChart(cds);
                break;
        }
        setLegendOptions(IGraphConstant.LEGEND_POSITION, Messages.getString("Statistic.PacketSizeStat.4"));
        graphViewer.setChart(chart);
    }

    public void changeCumulative(boolean cumulative) {
        this.cumulative = cumulative;
        if (cumulative) {
            graph.setSubTitle("(Cumulative)");
        } else graph.setSubTitle("(Real Time)");
        graph.setCumulative(cumulative);
    }

    /**
		 * Get the statistical data of the view
		 * @return  Returns the statistical data associated.
		 * @uml.property  name="statData"
		 */
    public StatisticalData getStatData() {
        return statData;
    }

    /**
		 * Check whether the view is in cumulative or real time mode
		 * @return  Current cumulative state.
		 * @uml.property  name="cumulative"
		 */
    public boolean isCumulative() {
        return cumulative;
    }

    /**
		 * Return the type of graph that is being shown
		 * @return  Current type of graph.
		 * @uml.property  name="type"
		 */
    public int getType() {
        return type;
    }

    /**
		 * Acces the graph object of the distribution view
		 * @return  The graph object
		 * @uml.property  name="graph"
		 */
    public Graph getGraph() {
        return graph;
    }

    private static String vName = Messages.getString("Statistic.0");

    /**
		 * Method to set the formal statistic name for display.
		 * 
		 * @param name Formal name for display
		 */
    public void setName(String name) {
        this.vName = name;
    }

    /**
		 * Method to get the formal statistic name for display.
		 * 
		 * @return Returns the statistic name.
		 */
    public static String getName() {
        return vName;
    }

    public void createPartControl(Composite parent) {
        graphViewer = new GraphViewer(parent);
        graphViewer.setLayoutData(new GridData(GridData.FILL_BOTH));
        changeType(type);
        makeActions();
        IActionBars bars = getViewSite().getActionBars();
        fillLocalToolBar((ToolBarManager) bars.getToolBarManager());
    }

    public void fillLocalToolBar(ToolBarManager tbm) {
        for (int i = 0; i < baseView.TYPE_COUNT; i++) tbm.add(new VLANChangeTypeAction(this, i));
        tbm.add(new Separator());
        tbm.add(setVLANID);
        tbm.add(new Separator());
    }

    private void makeActions() {
        setVLANID = new Action() {

            public void run() {
                WizardDialog dlg = new WizardDialog(null, new SetVLANidWizard());
                dlg.open();
                chart.clearSubtitles();
                if (CorePlugin.getDefault().getVLANFieldID() != null) {
                    chart.addSubtitle(new TextTitle("VLAN ID :" + CorePlugin.getDefault().getVLANFieldID()));
                }
            }
        };
        setVLANID.setToolTipText(" Set Single Virtual LAN ID ");
        setVLANID.setImageDescriptor(DistributionPlugin.getDefault().getImageRegistry().getDescriptor(DistributionPlugin.SETIP));
        setVLANID.setEnabled(true);
    }

    /**
	 * @author   Administrator
	 */
    public class VLANChangeTypeAction extends Action {

        /**
		 * The view that is associated with this listener
		 */
        private VLANPacketSizeView view;

        /**
		 * The type of graph to set when clicked
		 */
        private int type;

        /**
		 * Construct a distribution view listener that is associated with view
		 * 
		 * @param view
		 *            The associated view
		 * 
		 * @param type
		 *            The type of graph to be set when event is fired
		 */
        public VLANChangeTypeAction(VLANPacketSizeView view, int type) {
            super();
            this.view = view;
            this.type = type;
            Bundle bundle = Platform.getBundle(DistributionPlugin.PLUGIN_ID);
            Path path = new Path("icons/" + BaseView.TYPE_LABELS[type].toLowerCase().replace(' ', '_') + ".gif");
            URL fileURL = Platform.find(bundle, path);
            this.setImageDescriptor(ImageDescriptor.createFromURL(fileURL));
            this.setToolTipText(BaseView.TYPE_LABELS[type]);
        }

        public void run() {
            view.changeType(type);
        }
    }

    public static void refreshView() {
        chart.clearSubtitles();
        if (CorePlugin.getDefault().getVLANFieldID() != null) {
            chart.addSubtitle(new TextTitle("VLAN ID :" + CorePlugin.getDefault().getVLANFieldID()));
        }
    }

    public void refresh() {
        PieDataset dataset = new DefaultPieDataset();
        pds = (DefaultPieDataset) dataset;
        cds.clear();
    }

    public void dispose() {
        CorePlugin.getDefault().setVLANFieldID(null);
        PacSizeStat.dispose();
        refresh();
        super.dispose();
    }

    public void setFocus() {
        this.graphViewer.setFocus();
    }
}
