package net.sourceforge.ondex.ovtk2.filter.significance;

import net.sourceforge.ondex.ONDEXPluginArguments;
import net.sourceforge.ondex.InvalidPluginArgumentException;
import net.sourceforge.ondex.core.*;
import net.sourceforge.ondex.core.util.ONDEXViewFunctions;
import net.sourceforge.ondex.core.util.SparseBitSet;
import net.sourceforge.ondex.filter.significance.Filter;
import net.sourceforge.ondex.ovtk2.config.Config;
import net.sourceforge.ondex.ovtk2.filter.OVTK2Filter;
import net.sourceforge.ondex.ovtk2.graph.VisibilityUndo;
import net.sourceforge.ondex.ovtk2.ui.AbstractOVTK2Viewer;
import net.sourceforge.ondex.ovtk2.ui.OVTK2Desktop;
import net.sourceforge.ondex.ovtk2.ui.stats.Statistic;
import net.sourceforge.ondex.ovtk2.util.ErrorDialog;
import net.sourceforge.ondex.ovtk2.util.SpringUtilities;
import net.sourceforge.ondex.tools.functions.StandardFunctions;
import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.statistics.SimpleHistogramBin;
import org.jfree.data.statistics.SimpleHistogramDataset;
import org.jfree.ui.RectangleInsets;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.undo.StateEdit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;
import static net.sourceforge.ondex.tools.functions.ControledVocabularyHelper.createAttName;

/**
 * Filters relations based on a significance value for GDS.
 * 
 * @author taubertj, lysenkoa
 * @version 20.03.2008
 */
public class SignificanceFilter extends OVTK2Filter implements ActionListener, ListSelectionListener, ChangeListener, ChartMouseListener {

    /**
	 * Dynamic AttributeName model.
	 * 
	 * @author lysenkoa, hindlem, taubertj
	 */
    private class AttributeNameListModel extends AbstractListModel {

        private static final long serialVersionUID = 1L;

        private List<AttributeName> al = new ArrayList<AttributeName>();

        /**
		 * Adds another AttributeName to the list.
		 * 
		 * @param an
		 *            the AttributeName to add to the list
		 */
        public void addAttributeName(AttributeName an) {
            al.add(an);
            Collections.sort(al, new Comparator<AttributeName>() {

                @Override
                public int compare(AttributeName o1, AttributeName o2) {
                    return o1.getId().compareToIgnoreCase(o2.getId());
                }
            });
        }

        /**
		 * Clears this list.
		 */
        public void clearList() {
            al.clear();
        }

        public AttributeName getAttributeNameAt(int index) {
            if (index > -1) {
                AttributeName an = al.get(index);
                return an;
            }
            return null;
        }

        public Object getElementAt(int index) {
            JLabel label = null;
            if (al.size() == 0 && index == 0) {
                label = new JLabel("no numerical attributes present!");
            } else if (index > -1) {
                AttributeName an = al.get(index);
                String name = an.getFullname();
                if (name.trim().length() == 0) name = an.getId();
                label = new JLabel(name);
                label.setName(an.getId());
                label.setToolTipText("(" + an.getId() + ") " + an.getDescription());
            }
            return label;
        }

        public int getSize() {
            int size = al.size() < 1 ? 1 : al.size();
            return size;
        }

        public boolean isEmpty() {
            return al.isEmpty();
        }

        public void refresh() {
            fireContentsChanged(this, 0, getSize());
        }
    }

    /**
	 * Customised JList to accept Component items
	 * 
	 * @author hindlem
	 */
    class CustomCellRenderer extends DefaultListCellRenderer {

        private static final long serialVersionUID = 1L;

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, hasFocus);
            if (value instanceof JLabel) {
                JLabel labelNew = (JLabel) value;
                label.setText(labelNew.getText());
                label.setToolTipText(labelNew.getToolTipText());
            }
            return label;
        }
    }

    /**
	 * Creates a dataset, might be empty.
	 * 
	 * @return The dataset.
	 */
    private class Extractor {

        double max = Double.NEGATIVE_INFINITY;

        double min = Double.POSITIVE_INFINITY;

        Collection<Number> occurrences = new ArrayList<Number>();

        public Extractor(AttributeName target) {
            if (!conceptSelected) {
                for (ONDEXRelation r : aog.getRelationsOfAttributeName(target)) {
                    GDS gds = r.getGDS(target);
                    Number number = (Number) gds.getValue();
                    if (absolute.isSelected()) {
                        number = Math.abs(number.doubleValue());
                    }
                    occurrences.add(number);
                    if (number.doubleValue() > max) max = number.doubleValue();
                    if (number.doubleValue() < min) min = number.doubleValue();
                }
            } else {
                for (ONDEXConcept r : aog.getConceptsOfAttributeName(target)) {
                    GDS gds = r.getGDS(target);
                    Number number = (Number) gds.getValue();
                    if (absolute.isSelected()) {
                        number = Math.abs(number.doubleValue());
                    }
                    occurrences.add(number);
                    if (number.doubleValue() > max) max = number.doubleValue();
                    if (number.doubleValue() < min) min = number.doubleValue();
                }
            }
        }

        public Collection<Number> getDataSet() {
            return occurrences;
        }

        public double getMaximalValue() {
            return max;
        }

        public double getMinimalValue() {
            return min;
        }
    }

    private class SubsetPanel extends JPanel {

        private static final long serialVersionUID = 4301359981277949126L;

        private JTextField subsetName = new JTextField();

        private JCheckBox useThis = new JCheckBox("Assign subsets");

        public SubsetPanel() {
            this.setLayout(new GridBagLayout());
            GridBagConstraints con = new GridBagConstraints();
            con.fill = GridBagConstraints.HORIZONTAL;
            useThis.setSelected(false);
            con.gridx = 0;
            con.gridy = 0;
            con.weighty = 1;
            con.weightx = 0;
            this.add(useThis, con);
            con.gridx = 1;
            con.weightx = 0;
            con.weightx = 1;
            this.add(subsetName, con);
        }

        public String getCategoryName() {
            return subsetName.getText();
        }

        public boolean isAssignCategories() {
            return useThis.isSelected();
        }
    }

    private static final long serialVersionUID = -7992395983692245135L;

    private JCheckBox absolute = null;

    private AttributeNameListModel anlmConcepts = null;

    private AttributeNameListModel anlmRelations = null;

    private JFreeChart chart = null;

    private ChartPanel chartPanel = null;

    private boolean conceptSelected = true;

    private JButton goButton = null;

    private JCheckBox interactive = null;

    private JCheckBox inverse = null;

    private JList listConcepts = null;

    private JList listRelations = null;

    private JCheckBox no_attribute = null;

    private JSlider resolution;

    private SubsetPanel subsets = new SubsetPanel();

    private JTabbedPane tabbedPane = null;

    private AttributeName target = null;

    private JCheckBox unconnected = null;

    public SignificanceFilter(AbstractOVTK2Viewer viewer) {
        super(viewer);
        setLayout(new SpringLayout());
        goButton = new JButton("Filter Graph");
        goButton.setEnabled(false);
        goButton.setActionCommand("GO");
        goButton.addActionListener(this);
        anlmConcepts = new AttributeNameListModel();
        anlmRelations = new AttributeNameListModel();
        listConcepts = new JList(anlmConcepts);
        listConcepts.setCellRenderer(new CustomCellRenderer());
        listRelations = new JList(anlmRelations);
        listRelations.setCellRenderer(new CustomCellRenderer());
        populateConceptList();
        populateRelationList();
        listConcepts.validate();
        listConcepts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listConcepts.addListSelectionListener(this);
        listRelations.validate();
        listRelations.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listRelations.addListSelectionListener(this);
        tabbedPane = new JTabbedPane();
        tabbedPane.addChangeListener(this);
        JPanel panelConcepts = new JPanel(new GridLayout(1, 1));
        panelConcepts.add(new JScrollPane(listConcepts));
        tabbedPane.addTab("Attributes on Concepts", null, panelConcepts, "Select attributes on concepts");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_C);
        JPanel panelRelations = new JPanel(new GridLayout(1, 1));
        panelRelations.add(new JScrollPane(listRelations));
        tabbedPane.addTab("Attributes on Relations", null, panelRelations, "Select attributes on relations");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_R);
        JPanel panelOptions = new JPanel();
        BoxLayout layout = new BoxLayout(panelOptions, BoxLayout.PAGE_AXIS);
        panelOptions.setLayout(layout);
        tabbedPane.addTab("Filter options", null, panelOptions, "Configure filter options");
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_O);
        add(tabbedPane);
        JFreeChart chart = createChart(createDataset());
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        chartPanel.setMouseZoomable(true, false);
        chartPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        chartPanel.addChartMouseListener(this);
        add(chartPanel);
        resolution = new JSlider(JSlider.HORIZONTAL, 0, 100, 25);
        resolution.setToolTipText("length of quantification interval.");
        resolution.addChangeListener(this);
        resolution.setMajorTickSpacing(25);
        resolution.setMinorTickSpacing(5);
        resolution.setPaintTicks(true);
        resolution.setPaintLabels(false);
        JPanel sliderpanel = new JPanel(new BorderLayout());
        sliderpanel.add(new JLabel("Resolution"), BorderLayout.WEST);
        sliderpanel.add(resolution, BorderLayout.CENTER);
        add(sliderpanel);
        JPanel checkBoxes = new JPanel(new GridLayout(3, 3));
        unconnected = new JCheckBox("Hide unconnected nodes");
        unconnected.setToolTipText("Hides nodes that got unconnected after applying the filter.");
        unconnected.setSelected(true);
        checkBoxes.add(unconnected);
        inverse = new JCheckBox("Inverse threshold");
        inverse.setToolTipText("Filter elements which have values smaller than selected threhold.");
        checkBoxes.add(inverse);
        no_attribute = new JCheckBox("Hide elements with no attribute");
        no_attribute.setToolTipText("Hides all elements of the graph which do not share the selected attribute.");
        no_attribute.setSelected(true);
        checkBoxes.add(no_attribute);
        absolute = new JCheckBox("Absolute Values");
        absolute.addActionListener(this);
        absolute.setToolTipText("Absolute All GDS values");
        absolute.setActionCommand("Absolute");
        checkBoxes.add(absolute);
        interactive = new JCheckBox("Interactive filtering");
        interactive.setToolTipText("Filter the graph when selecting a threshold.");
        checkBoxes.add(interactive);
        checkBoxes.add(subsets);
        panelOptions.add(checkBoxes);
        add(goButton);
        SpringUtilities.makeCompactGrid(this, this.getComponentCount(), 1, 7, 5, 5, 5);
    }

    /**
	 * Associated with go button.
	 */
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("GO")) {
            try {
                callFilter();
            } catch (InvalidPluginArgumentException e1) {
                ErrorDialog.show(e1);
            }
        } else if (e.getActionCommand().equals("Absolute")) {
            chart.getXYPlot().setDataset(createDataset());
        }
    }

    /**
	 * Calls backend filter.
	 */
    private void callFilter() throws InvalidPluginArgumentException {
        StateEdit edit = new StateEdit(new VisibilityUndo(viewer.getJUNGGraph()), this.getName());
        OVTK2Desktop desktop = OVTK2Desktop.getInstance();
        desktop.setRunningProcess(this.getName());
        Filter filter = new Filter();
        ONDEXPluginArguments fa = new ONDEXPluginArguments(filter.getArgumentDefinitions());
        fa.addOption(Filter.CONCEPTMODE_ARG, conceptSelected);
        fa.addOption(Filter.TARGETAN_ARG, target.getId());
        fa.addOption(Filter.SIG_ARG, (double) ((XYPlot) chart.getPlot()).getDomainCrosshairValue());
        fa.addOption(Filter.INVERSE_ARG, inverse.isSelected());
        fa.addOption(Filter.ABSOLUTE_ARG, absolute.isSelected());
        fa.addOption(Filter.NO_ATT_ARG, no_attribute.isSelected());
        filter.setONDEXGraph(aog);
        filter.setArguments(fa);
        filter.start();
        Set<ONDEXConcept> concepts = filter.getVisibleConcepts();
        Set<ONDEXRelation> relations = filter.getVisibleRelations();
        if (unconnected.isSelected()) {
            concepts = StandardFunctions.filteroutUnconnected(aog, concepts, relations);
        }
        if (subsets.isAssignCategories()) {
            AttributeName att = createAttName(aog, subsets.getCategoryName(), Integer.class);
            Set<ONDEXConcept> unprocessedConcepts = ONDEXViewFunctions.copy(concepts);
            Set<ONDEXRelation> unprocessedRelations = ONDEXViewFunctions.copy(relations);
            Set<Integer> takenIds = new HashSet<Integer>();
            Integer lastId = 1;
            while (unprocessedConcepts.size() > 0) {
                SparseBitSet[] group = StandardFunctions.getAllConnected(unprocessedConcepts.iterator().next(), aog, unprocessedRelations);
                Set<ONDEXConcept> concGroup = ONDEXViewFunctions.create(aog, ONDEXConcept.class, group[0]);
                unprocessedConcepts = ONDEXViewFunctions.andNot(unprocessedConcepts, concGroup);
                SortedMap<Integer, Object> prevalence = StandardFunctions.gdsRanking(aog, concGroup, att.getId());
                Integer groupID = 1;
                if (prevalence.size() != 0) groupID = Integer.valueOf(prevalence.get(prevalence.lastKey()).toString());
                if (groupID == null || takenIds.contains(groupID)) {
                    groupID = lastId + 1;
                    while (takenIds.contains(groupID)) groupID++;
                    lastId = groupID;
                }
                takenIds.add(groupID);
                for (Integer id : group[0]) {
                    ONDEXConcept c = aog.getConcept(id);
                    if (c.getGDS(att) != null) {
                        c.deleteGDS(att);
                    }
                    c.createGDS(att, groupID, false);
                }
            }
        }
        Set<ONDEXConcept> invisibleConcepts = ONDEXViewFunctions.andNot(aog.getConcepts(), concepts);
        Set<ONDEXRelation> invisibleRelations = ONDEXViewFunctions.andNot(aog.getRelations(), relations);
        for (ONDEXConcept c : concepts) {
            jung.setVisibility(jung.getNodes(c.getId()), true);
        }
        for (ONDEXRelation r : relations) {
            jung.setVisibility(jung.getEdges(r.getId()), true);
        }
        for (ONDEXRelation r : invisibleRelations) {
            jung.setVisibility(jung.getEdges(r.getId()), false);
        }
        for (ONDEXConcept c : invisibleConcepts) {
            jung.setVisibility(jung.getNodes(c.getId()), false);
        }
        viewer.getVisualizationViewer().getModel().fireStateChanged();
        edit.end();
        viewer.undoManager.addEdit(edit);
        desktop.getOVTK2Menu().updateUndoRedo(viewer);
        desktop.notifyTerminationOfProcess();
    }

    @Override
    public void chartMouseClicked(ChartMouseEvent arg0) {
        if (interactive.isSelected()) {
            Thread update = new Thread() {

                @Override
                public void run() {
                    synchronized (this) {
                        try {
                            this.wait(100);
                        } catch (InterruptedException ie) {
                        }
                        try {
                            callFilter();
                        } catch (InvalidPluginArgumentException e) {
                            ErrorDialog.show(e);
                        }
                    }
                }
            };
            update.start();
        }
    }

    @Override
    public void chartMouseMoved(ChartMouseEvent arg0) {
    }

    /**
	 * Creates a chart.
	 * 
	 * @param dataset
	 *            a dataset.
	 * @return A chart.
	 */
    private JFreeChart createChart(SimpleHistogramDataset dataset) {
        String name = "Value";
        if (target != null) name = target.getId();
        chart = ChartFactory.createHistogram(null, name, "Histogram", dataset, PlotOrientation.VERTICAL, false, true, false);
        chart.setBackgroundPaint(Color.white);
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.white);
        plot.getRenderer().setSeriesPaint(0, new Color(0x7f9f51));
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.lightGray);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        return chart;
    }

    /**
	 * performs the data extraction and quantification.
	 * 
	 * @return a histogram dataset.
	 */
    private SimpleHistogramDataset createDataset() {
        SimpleHistogramDataset set = new SimpleHistogramDataset("empty");
        if (target != null) {
            Extractor extractor = new Extractor(target);
            Statistic statistic = new Statistic(extractor.getDataSet(), getResolution(extractor));
            set = new SimpleHistogramDataset(target.getId());
            SimpleHistogramBin bin;
            double[][] his = statistic.getHistogramValues();
            double x_start, x_end, y;
            for (int i = 0; i < his.length; i++) {
                x_start = his[i][0];
                x_end = his[i][0] + getResolution(extractor);
                y = his[i][1];
                bin = new SimpleHistogramBin(x_start, x_end, true, false);
                bin.setItemCount((int) y);
                set.addBin(bin);
            }
        }
        return set;
    }

    @Override
    public String getName() {
        return Config.language.getProperty("Name.Menu.Filter.Significance");
    }

    /**
	 * @return the length of the quantification interval as chosen by the user.
	 */
    private double getResolution(Extractor extractor) {
        double selectedVal;
        if (resolution != null) selectedVal = (double) resolution.getValue(); else selectedVal = 25.0;
        double max = extractor.getMaximalValue();
        double min = extractor.getMinimalValue();
        double range = max - min;
        double pot = 2.0 * (selectedVal / 100.0);
        double perc = Math.pow(10.0, pot) - 1;
        return (perc / 100.0) * range;
    }

    private void populateConceptList() {
        anlmConcepts.clearList();
        for (AttributeName an : aog.getMetaData().getAttributeNames()) {
            Set<ONDEXConcept> concepts = aog.getConceptsOfAttributeName(an);
            if (concepts != null) {
                Class<?> cl = an.getDataType();
                if (concepts.size() > 0 && Number.class.isAssignableFrom(cl)) {
                    anlmConcepts.addAttributeName(an);
                }
            }
        }
        anlmConcepts.refresh();
        listConcepts.setEnabled(!anlmConcepts.isEmpty());
        listConcepts.repaint();
    }

    private void populateRelationList() {
        anlmRelations.clearList();
        for (AttributeName an : aog.getMetaData().getAttributeNames()) {
            Set<ONDEXRelation> relations = aog.getRelationsOfAttributeName(an);
            if (relations != null) {
                Class<?> cl = an.getDataType();
                if (relations.size() > 0 && Number.class.isAssignableFrom(cl)) {
                    anlmRelations.addAttributeName(an);
                }
            }
        }
        anlmRelations.refresh();
        listRelations.setEnabled(!anlmRelations.isEmpty());
        listRelations.repaint();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() instanceof JSlider) {
            JSlider source = (JSlider) e.getSource();
            if (!source.getValueIsAdjusting()) {
                updateChart();
            }
        } else {
            if (tabbedPane.getSelectedIndex() == 0) {
                conceptSelected = true;
            }
            if (tabbedPane.getSelectedIndex() == 1) {
                conceptSelected = false;
            }
        }
    }

    /**
	 * creates a new chart object and replaces the old one.
	 */
    private void updateChart() {
        chart = createChart(createDataset());
        chartPanel.setChart(chart);
        repaint();
    }

    /**
	 * Checks for selections in AttributeName list.
	 */
    public void valueChanged(ListSelectionEvent e) {
        JList list = ((JList) e.getSource());
        AttributeNameListModel model = (AttributeNameListModel) list.getModel();
        int index = list.getSelectedIndex();
        if (index > -1 && !model.isEmpty()) {
            goButton.setEnabled(true);
            target = model.getAttributeNameAt(index);
            chart.getXYPlot().setDataset(createDataset());
            String label = target.getFullname();
            if (label.trim().length() == 0) label = target.getId();
            chart.getXYPlot().getDomainAxis().setLabel(label);
            chart.fireChartChanged();
        }
    }
}
