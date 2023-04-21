package gui;

import globals.ConstantInterface;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.jfree.chart.ChartPanel;
import plot.PlotChart;
import topology.TopologyGraph;
import bottomLeftPane.CouplingTable;
import bottomLeftPane.RowHeader;
import bottomRightPane.Table;

/**
 * This is the contentPane that holds all the components.
 * 
 * @author Luca Petraglio
 * @author Michael Mattes
 */
public class ContentPane extends JPanel implements ConstantInterface {

    private static ContentPane instance = null;

    private static final long serialVersionUID = 1L;

    private JPanel jp;

    private final TopologyGraph graph;

    private final CouplingTable table;

    private final PlotChart chart;

    private final JScrollPane scrollPane;

    private boolean split = false;

    public static ContentPane getInstance() {
        if (instance == null) {
            instance = new ContentPane();
        }
        return instance;
    }

    /**
	 * The JPanel layout is set to BorderLayout.
	 */
    private ContentPane() {
        setLayout(new BorderLayout());
        graph = TopologyGraph.getInstance();
        chart = PlotChart.getObject();
        table = CouplingTable.getInstance();
        scrollPane = new JScrollPane(table);
        final RowHeader rowHeader = RowHeader.getObject(table, 35);
        scrollPane.setRowHeaderView(rowHeader.getRowHeader());
    }

    /**
	 * Build the split.
	 */
    public void buildSplits() {
        if (!split) {
            jp = new JPanel(new BorderLayout());
            final JScrollPane graphScroller = new JScrollPane(graph);
            final JScrollPane plotScroller = new JScrollPane(new ChartPanel(chart.getChart()));
            graph.setScroller(graphScroller);
            jp.add(new JTopologyBar(), BorderLayout.NORTH);
            jp.add(graphScroller, BorderLayout.CENTER);
            jp.add(new JThresholdBar(), BorderLayout.SOUTH);
            final JPanel jPlotPanel = new JPanel(new BorderLayout());
            jPlotPanel.add(new JPlotBar(), BorderLayout.NORTH);
            jPlotPanel.add(plotScroller, BorderLayout.CENTER);
            final FourSplitPane _4sp = new FourSplitPane(jp, jPlotPanel, scrollPane, new JScrollPane(Table.getInstance().getTable()));
            add(_4sp.getSplit());
            split = true;
        }
    }
}
