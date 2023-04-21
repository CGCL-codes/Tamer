package aurora.hwc.gui;

import java.awt.*;
import java.awt.Point;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.xy.*;
import org.jfree.data.time.*;
import org.jfree.data.general.*;
import aurora.*;
import aurora.hwc.*;
import aurora.hwc.control.*;
import aurora.util.*;

/**
 * Implementation of Node detail window.
 * @author Alex Kurzhanskiy
 * @version $Id: $
 */
public class WindowNodeP extends JInternalFrame implements ActionListener {

    private static final long serialVersionUID = -6387965880979812735L;

    private AbstractContainer mySystem;

    private AbstractNodeHWC myNode;

    private TreePane treePane;

    protected JTabbedPane tabbedPane = new JTabbedPane();

    protected JTabbedPane tabbedConfig = new JTabbedPane();

    private int nIn;

    private int nOut;

    private String[] ctrlTypes;

    private String[] ctrlClasses;

    private Box bcPanel = Box.createVerticalBox();

    private TimeSeriesCollection[] bcDataSets = new TimeSeriesCollection[2];

    private JFreeChart bcChart;

    private Box wcPanel = Box.createVerticalBox();

    private TimeSeriesCollection[] wcDataSets = new TimeSeriesCollection[2];

    private JFreeChart wcChart;

    protected Box confPanel = Box.createVerticalBox();

    private JComboBox listEvents;

    private JComboBox vtList = new JComboBox();

    private static final String nmVTList = "TypeList";

    private JButton buttonEvents = new JButton("Generate");

    private srmROTableModel srmTM = new srmROTableModel();

    private wfmROTableModel wfmTM = new wfmROTableModel();

    private ctrlROTableModel ctrlTM = new ctrlROTableModel();

    public WindowNodeP() {
    }

    public WindowNodeP(AbstractContainer ctnr, AbstractNodeHWC nd, TreePane tp) {
        super("Node " + nd.toString(), true, true, true, true);
        mySystem = ctnr;
        myNode = nd;
        treePane = tp;
        nIn = nd.getInputs().size();
        nOut = nd.getOutputs().size();
        ctrlTypes = myNode.getSimpleControllerTypes();
        ctrlClasses = myNode.getSimpleControllerClasses();
        setSize(320, 450);
        int n = treePane.getInternalFrameCount();
        setLocation(20 * n, 20 * n);
        AdapterWindowNode listener = new AdapterWindowNode();
        addInternalFrameListener(listener);
        addComponentListener(listener);
        fillSimPanel();
        fillConfPanel();
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.add("Best Case", new JScrollPane(bcPanel));
        tabbedPane.add("Worst Case", new JScrollPane(wcPanel));
        tabbedPane.add("Configuration", new JScrollPane(confPanel));
        setContentPane(tabbedPane);
    }

    /**
	 * Updates simulation data.
	 */
    private void updateSimSeries() {
        double bcTotal = 0;
        double wcTotal = 0;
        Second cts = Util.time2second(myNode.getTS() * myNode.getTop().getTP());
        try {
            for (int i = 1; i <= nIn; i++) {
                AbstractLinkHWC il = (AbstractLinkHWC) myNode.getPredecessors().get(i - 1);
                AuroraInterval ifl = il.getAverageOutFlow().sum();
                double lb, ub;
                if (il.isOutputUpperBoundFirst()) {
                    lb = ifl.getUpperBound();
                    ub = ifl.getLowerBound();
                } else {
                    lb = ifl.getLowerBound();
                    ub = ifl.getUpperBound();
                }
                bcDataSets[0].getSeries(i).add(cts, lb);
                wcDataSets[0].getSeries(i).add(cts, ub);
                bcTotal += lb;
                wcTotal += ub;
            }
            bcDataSets[0].getSeries(0).add(cts, bcTotal);
            wcDataSets[0].getSeries(0).add(cts, wcTotal);
            bcTotal = 0;
            wcTotal = 0;
            for (int i = 1; i <= nOut; i++) {
                AbstractLinkHWC ol = (AbstractLinkHWC) myNode.getSuccessors().get(i - 1);
                AuroraInterval ofl = ol.getAverageInFlow().sum();
                double lb, ub;
                if (ol.isOutputUpperBoundFirst()) {
                    lb = ofl.getUpperBound();
                    ub = ofl.getLowerBound();
                } else {
                    lb = ofl.getLowerBound();
                    ub = ofl.getUpperBound();
                }
                bcDataSets[1].getSeries(i).add(cts, lb);
                wcDataSets[1].getSeries(i).add(cts, ub);
                bcTotal += lb;
                wcTotal += ub;
            }
            bcDataSets[1].getSeries(0).add(cts, bcTotal);
            wcDataSets[1].getSeries(0).add(cts, wcTotal);
        } catch (SeriesException e) {
        }
        return;
    }

    /**
	 * Resets simulation data.
	 */
    private void resetSimSeries() {
        int i;
        for (i = 0; i <= nIn; i++) {
            bcDataSets[0].getSeries(i).clear();
            wcDataSets[0].getSeries(i).clear();
        }
        for (i = 0; i <= nOut; i++) {
            bcDataSets[1].getSeries(i).clear();
            wcDataSets[1].getSeries(i).clear();
        }
        return;
    }

    /**
	 * Generates Best Case and Worst Case tabs.
	 */
    private void fillSimPanel() {
        int i;
        TimeSeriesCollection dataset;
        NumberAxis rangeAxis;
        XYPlot subplot;
        CombinedDomainXYPlot bcPlot = new CombinedDomainXYPlot(new DateAxis("Time"));
        dataset = new TimeSeriesCollection();
        dataset.addSeries(new TimeSeries("Total In-Flow", Second.class));
        for (i = 0; i < nIn; i++) {
            AbstractNetworkElement ne = myNode.getPredecessors().get(i);
            dataset.addSeries(new TimeSeries("From " + TypesHWC.typeString(ne.getType()) + " " + ne.getId(), Second.class));
        }
        bcDataSets[0] = dataset;
        rangeAxis = new NumberAxis("In-Flow(vph)");
        rangeAxis.setAutoRangeIncludesZero(false);
        subplot = new XYPlot(bcDataSets[0], null, rangeAxis, new StandardXYItemRenderer());
        bcPlot.add(subplot);
        dataset = new TimeSeriesCollection();
        dataset.addSeries(new TimeSeries("Total Out-Flow", Second.class));
        for (i = 0; i < nOut; i++) {
            AbstractNetworkElement ne = myNode.getSuccessors().get(i);
            dataset.addSeries(new TimeSeries("To " + TypesHWC.typeString(ne.getType()) + " " + ne.getId(), Second.class));
        }
        bcDataSets[1] = dataset;
        rangeAxis = new NumberAxis("Out-Flow(vph)");
        rangeAxis.setAutoRangeIncludesZero(false);
        subplot = new XYPlot(bcDataSets[1], null, rangeAxis, new StandardXYItemRenderer());
        bcPlot.add(subplot);
        ValueAxis axis = bcPlot.getDomainAxis();
        axis.setAutoRange(true);
        bcChart = new JFreeChart(null, bcPlot);
        ChartPanel cp = new ChartPanel(bcChart);
        cp.setPreferredSize(new Dimension(200, 300));
        bcPanel.add(cp);
        CombinedDomainXYPlot wcPlot = new CombinedDomainXYPlot(new DateAxis("Time"));
        dataset = new TimeSeriesCollection();
        dataset.addSeries(new TimeSeries("Total In-Flow", Second.class));
        for (i = 0; i < nIn; i++) {
            AbstractNetworkElement ne = myNode.getPredecessors().get(i);
            dataset.addSeries(new TimeSeries("From " + TypesHWC.typeString(ne.getType()) + " " + ne.getId(), Second.class));
        }
        wcDataSets[0] = dataset;
        rangeAxis = new NumberAxis("In-Flow(vph)");
        rangeAxis.setAutoRangeIncludesZero(false);
        subplot = new XYPlot(wcDataSets[0], null, rangeAxis, new StandardXYItemRenderer());
        wcPlot.add(subplot);
        dataset = new TimeSeriesCollection();
        dataset.addSeries(new TimeSeries("Total Out-Flow", Second.class));
        for (i = 0; i < nOut; i++) {
            AbstractNetworkElement ne = myNode.getSuccessors().get(i);
            dataset.addSeries(new TimeSeries("To " + TypesHWC.typeString(ne.getType()) + " " + ne.getId(), Second.class));
        }
        wcDataSets[1] = dataset;
        rangeAxis = new NumberAxis("Out-Flow(vph)");
        rangeAxis.setAutoRangeIncludesZero(false);
        subplot = new XYPlot(wcDataSets[1], null, rangeAxis, new StandardXYItemRenderer());
        wcPlot.add(subplot);
        axis = wcPlot.getDomainAxis();
        axis.setAutoRange(true);
        wcChart = new JFreeChart(null, wcPlot);
        cp = new ChartPanel(wcChart);
        cp.setPreferredSize(new Dimension(200, 300));
        wcPanel.add(cp);
        return;
    }

    /**
	 * Generates Configuration tab.
	 */
    private void fillConfPanel() {
        final Vector<AbstractControllerSimple> controllers = myNode.getSimpleControllers();
        JPanel desc = new JPanel(new GridLayout(1, 0));
        desc.setBorder(BorderFactory.createTitledBorder("Description"));
        desc.add(new JScrollPane(new JLabel("<html><pre><font color=\"blue\">" + myNode.getDescription() + "</font></pre></html>")));
        confPanel.add(desc);
        Vector<String> vts = ((SimulationSettingsHWC) mySystem.getMySettings()).getVehicleTypes();
        vtList.addItem("All vehicle types");
        for (int i = 0; i < vts.size(); i++) vtList.addItem(vts.get(i));
        vtList.setSelectedIndex(0);
        vtList.setActionCommand(nmVTList);
        vtList.addActionListener(this);
        Box srm = Box.createVerticalBox();
        srm.add(vtList);
        final JTable srmtab = new JTable(srmTM);
        srmtab.setPreferredScrollableViewportSize(new Dimension(250, 70));
        srm.add(new JScrollPane(srmtab));
        tabbedConfig.add("Split Ratios", srm);
        srmtab.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = srmtab.rowAtPoint(new Point(e.getX(), e.getY()));
                    int clmn = srmtab.columnAtPoint(new Point(e.getX(), e.getY()));
                    AbstractLinkHWC lnk = null;
                    if ((row > 0) && (clmn == 0)) lnk = (AbstractLinkHWC) myNode.getPredecessors().get(row - 1); else if ((clmn > 0) && (row == 0)) lnk = (AbstractLinkHWC) myNode.getSuccessors().get(clmn - 1); else return;
                    treePane.actionSelected(lnk, true);
                }
                return;
            }
        });
        JPanel wfm = new JPanel(new GridLayout(1, 0));
        final JTable wfmtab = new JTable(wfmTM);
        wfmtab.setPreferredScrollableViewportSize(new Dimension(250, 70));
        wfm.add(new JScrollPane(wfmtab));
        tabbedConfig.add("Weaving", wfm);
        wfmtab.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = wfmtab.rowAtPoint(new Point(e.getX(), e.getY()));
                    int clmn = wfmtab.columnAtPoint(new Point(e.getX(), e.getY()));
                    AbstractLinkHWC lnk = null;
                    if ((row > 0) && (clmn == 0)) lnk = (AbstractLinkHWC) myNode.getPredecessors().get(row - 1); else if ((clmn > 0) && (row == 0)) lnk = (AbstractLinkHWC) myNode.getSuccessors().get(clmn - 1); else return;
                    treePane.actionSelected(lnk, true);
                }
                return;
            }
        });
        JPanel ctrl = new JPanel(new GridLayout(1, 0));
        final JTable ctrltab = new JTable(ctrlTM);
        ctrltab.setPreferredScrollableViewportSize(new Dimension(250, 70));
        ctrl.add(new JScrollPane(ctrltab));
        tabbedConfig.add("Control", ctrl);
        ctrltab.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (!mySystem.getMyStatus().isStopped()) return;
                if (e.getClickCount() == 2) {
                    int row = ctrltab.rowAtPoint(new Point(e.getX(), e.getY()));
                    if (controllers.get(row) == null) return;
                    try {
                        Class c = Class.forName("aurora.hwc.control.Panel" + controllers.get(row).getClass().getSimpleName());
                        AbstractPanelSimpleController cp = (AbstractPanelSimpleController) c.newInstance();
                        cp.initialize((AbstractControllerSimpleHWC) controllers.get(row), null, -1, myNode);
                    } catch (Exception xpt) {
                    }
                }
                return;
            }
        });
        setUpControllerColumn(ctrltab, ctrltab.getColumnModel().getColumn(1));
        confPanel.add(tabbedConfig);
        JPanel events = new JPanel(new GridLayout(1, 0));
        Box events1 = Box.createHorizontalBox();
        events.setBorder(BorderFactory.createTitledBorder("Events"));
        listEvents = new JComboBox();
        listEvents.addItem("Split Ratio Matrix");
        listEvents.addItem("Weaving Factors");
        listEvents.addItem("Controller");
        events1.add(listEvents);
        buttonEvents.addActionListener(new ButtonEventsListener());
        events1.add(buttonEvents);
        events.add(events1);
        confPanel.add(events);
        return;
    }

    /**
	 * Establishes combo box editor for controller column.
	 * @param table 
	 * @param ctrlColumn
	 */
    private void setUpControllerColumn(JTable table, TableColumn ctrlColumn) {
        JComboBox combo = new JComboBox();
        combo.addItem("None");
        for (int i = 0; i < ctrlTypes.length; i++) combo.addItem(ctrlTypes[i]);
        ctrlColumn.setCellEditor(new DefaultCellEditor(combo));
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        ctrlColumn.setCellRenderer(renderer);
        return;
    }

    /**
	 * Updates displayed data.
	 */
    public void updateView() {
        srmTM.fireTableRowsUpdated(1, nIn);
        if (mySystem.getMyStatus().isStopped()) return;
        updateSimSeries();
        return;
    }

    /**
	 * Resets displayed data.
	 */
    public void resetView() {
        resetSimSeries();
        srmTM.fireTableRowsUpdated(1, nIn);
        return;
    }

    /**
	 * Action performed before closing the frame.
	 */
    private void close() {
        treePane.removeFrame(this);
        return;
    }

    /**
	 * Reaction to button and combo box events.
	 */
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (nmVTList.equals(cmd)) {
            srmTM.fireTableDataChanged();
        }
        return;
    }

    /**
	 * Class needed for displaying table of controllers.
	 */
    private class ctrlROTableModel extends AbstractTableModel {

        private static final long serialVersionUID = -8310974555648802182L;

        public String getColumnName(int col) {
            String buf = null;
            switch(col) {
                case 0:
                    buf = "In-Link";
                    break;
                case 1:
                    buf = "Controller";
                    break;
                case 2:
                    buf = "Queue Controller";
                    break;
            }
            return buf;
        }

        public int getColumnCount() {
            return 3;
        }

        public int getRowCount() {
            return nIn;
        }

        public Object getValueAt(int row, int column) {
            if ((row < 0) || (row >= nIn) || (column < 0) || (column > 2)) return null;
            if (column == 0) return (TypesHWC.typeString(myNode.getPredecessors().get(row).getType()) + " " + myNode.getPredecessors().get(row).toString());
            AbstractControllerSimpleHWC ctrl = (AbstractControllerSimpleHWC) myNode.getSimpleControllers().get(row);
            if (ctrl == null) return "None";
            if (column == 1) return ctrl.getDescription();
            AbstractQueueController qc = ctrl.getQController();
            if (qc == null) return "None";
            return qc.getDescription();
        }

        public boolean isCellEditable(int row, int column) {
            if ((mySystem.getMyStatus().isStopped()) && (column == 1) && (row >= 0) && (row < nIn)) {
                if ((myNode.getSimpleControllers().get(row) == null) || (!myNode.getSimpleControllers().get(row).isDependent())) return true;
            }
            return false;
        }

        public void setValueAt(Object value, int row, int column) {
            if ((row < 0) || (row >= nIn) || (column != 1) || (value == null)) return;
            int idx = -1;
            for (int i = 0; i < ctrlTypes.length; i++) if (ctrlTypes[i].compareTo((String) value) == 0) idx = i;
            if (idx < 0) myNode.setSimpleController(null, row); else try {
                Class c = Class.forName(ctrlClasses[idx]);
                myNode.setSimpleController((AbstractControllerSimpleHWC) c.newInstance(), row);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Cannot create Controller of type '" + ctrlClasses[idx] + "'.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            fireTableRowsUpdated(row, row);
            return;
        }
    }

    /**
	 * Class needed for displaying split ratio matrix in a table.
	 */
    private class srmROTableModel extends AbstractTableModel {

        private static final long serialVersionUID = 7602085793295670923L;

        public String getColumnName(int col) {
            return " ";
        }

        public int getColumnCount() {
            return (nOut + 1);
        }

        public int getRowCount() {
            return (nIn + 1);
        }

        public Object getValueAt(int row, int column) {
            AuroraIntervalVector[][] srm = myNode.getSplitRatioMatrix();
            if (row == 0) {
                if ((column < 1) || (column > srm[0].length)) return null;
                AbstractNetworkElement ne = myNode.getSuccessors().get(column - 1);
                return "To " + TypesHWC.typeString(ne.getType()) + " " + ne;
            }
            if (column == 0) {
                if ((row < 1) || (row > srm.length)) return null;
                AbstractNetworkElement ne = myNode.getPredecessors().get(row - 1);
                return "From " + TypesHWC.typeString(ne.getType()) + " " + ne;
            }
            if ((row < 1) || (row > srm.length) || (column < 1) || (column > srm[0].length)) return null;
            int idx = vtList.getSelectedIndex() - 1;
            String buf;
            if (idx < 0) buf = srm[row - 1][column - 1].toString2(); else buf = srm[row - 1][column - 1].get(idx).toString2();
            return buf;
        }

        public boolean isCellEditable(int row, int column) {
            if (vtList.getSelectedIndex() > 0) return false;
            if ((!mySystem.getMyStatus().isStopped()) || (row <= 0) || (column <= 0)) return false;
            return true;
        }

        public void setValueAt(Object value, int row, int column) {
            AuroraIntervalVector[][] srm = myNode.getSplitRatioMatrix();
            String buf = (String) value;
            int i = row - 1;
            int j = column - 1;
            if ((i < 0) || (i >= nIn) || (j < 0) || (j >= nOut)) return;
            srm[i][j].setIntervalVectorFromString(buf);
            if (myNode.setSplitRatioMatrix(srm)) fireTableRowsUpdated(row, row);
            return;
        }
    }

    /**
	 * Class needed for displaying weaving factor matrix in a table.
	 */
    private class wfmROTableModel extends AbstractTableModel {

        private static final long serialVersionUID = -8187950364203691237L;

        public String getColumnName(int col) {
            return " ";
        }

        public int getColumnCount() {
            return (nOut + 1);
        }

        public int getRowCount() {
            return (nIn + 1);
        }

        public Object getValueAt(int row, int column) {
            double[][] wfm = myNode.getWeavingFactorMatrix();
            if (row == 0) {
                if ((column < 1) || (column > wfm[0].length)) return null;
                AbstractNetworkElement ne = myNode.getSuccessors().get(column - 1);
                return "To " + TypesHWC.typeString(ne.getType()) + " " + ne;
            }
            if (column == 0) {
                if ((row < 1) || (row > wfm.length)) return null;
                AbstractNetworkElement ne = myNode.getPredecessors().get(row - 1);
                return "From " + TypesHWC.typeString(ne.getType()) + " " + ne;
            }
            if ((row < 1) || (row > wfm.length) || (column < 1) || (column > wfm[0].length)) return null;
            NumberFormat form = NumberFormat.getInstance();
            form.setMinimumFractionDigits(0);
            form.setMaximumFractionDigits(2);
            form.setGroupingUsed(false);
            return form.format(wfm[row - 1][column - 1]);
        }

        public boolean isCellEditable(int row, int column) {
            if ((!mySystem.getMyStatus().isStopped()) || (row <= 0) || (column <= 0)) return false;
            return true;
        }

        public void setValueAt(Object value, int row, int column) {
            double[][] wfm = myNode.getWeavingFactorMatrix();
            String buf = (String) value;
            int i = row - 1;
            int j = column - 1;
            if ((i < 0) || (i >= nIn) || (j < 0) || (j >= nOut)) return;
            try {
                wfm[i][j] = Double.parseDouble(buf);
                if (wfm[i][j] > -1) wfm[i][j] = Math.max(1.0, wfm[i][j]);
            } catch (Exception e) {
                wfm[i][j] = 1;
            }
            if (myNode.setWeavingFactorMatrix(wfm)) fireTableRowsUpdated(row, row);
            return;
        }
    }

    /**
	 * This class is needed to react to "Generate" button pressed.
	 */
    private class ButtonEventsListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            EventTableModel etm = (EventTableModel) ((TableSorter) treePane.getActionPane().getEventsTable().getModel()).getTableModel();
            AbstractEventPanel ep;
            switch(listEvents.getSelectedIndex()) {
                case 0:
                    ep = new PanelEventSRM();
                    break;
                case 1:
                    ep = new PanelEventWFM();
                    break;
                default:
                    ep = new PanelEventControllerSimple();
            }
            ep.initialize(myNode, mySystem.getMyEventManager(), etm);
            return;
        }
    }

    /**
	 * Class needed for proper closing of internal node windows.
	 */
    private class AdapterWindowNode extends InternalFrameAdapter implements ComponentListener {

        /**
		 * Function that is called when user closes the window.
		 * @param e internal frame event.
		 */
        public void internalFrameClosing(InternalFrameEvent e) {
            close();
            return;
        }

        public void componentHidden(ComponentEvent e) {
            return;
        }

        public void componentMoved(ComponentEvent e) {
            return;
        }

        public void componentResized(ComponentEvent e) {
            return;
        }

        public void componentShown(ComponentEvent e) {
            return;
        }
    }
}
