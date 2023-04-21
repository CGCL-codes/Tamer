package LRAC.bundlebuilder;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import net.miginfocom.swing.MigLayout;
import net.sourceforge.jglchartutil.DataSeries;
import net.sourceforge.jglchartutil.GLChart;
import net.sourceforge.jglchartutil.StipplePattern;
import net.sourceforge.jglchartutil.chartlab.ChartLab;
import net.sourceforge.jglchartutil.chartlab.ChartLabClosingEvent;
import net.sourceforge.jglchartutil.chartlab.ChartLabClosingListener;
import net.sourceforge.jglchartutil.chartlab.ChartTestData;
import org.xml.sax.SAXException;
import AccordionLRACDrawer.bundles.Representation;
import AccordionLRACDrawer.bundles.SeriesMetadata;
import AccordionLRACDrawer.bundles.Template;
import LRAC.util.IntegerEditor;
import LRAC.util.JComponentCellEditor;
import LRAC.util.JComponentCellRenderer;

/**
 * @author Peter McLachlan <spark343@cs.ubc.ca>
 *
 */
public class TemplateEditorPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = -2548287765219577871L;

    private static final String[] COLNAMES = { "Representation", "Width", "Height", "Associated Meta-series" };

    static final long[] timeArray = ChartTestData.genTimestamps(ChartTestData.getDefaultDataPoints(GLChart.BARCHART), ChartTestData.DEFAULTBEGINTIME, ChartTestData.DEFAULTENDTIME);

    static final String TEMPLATEEDITORPANENAME = "Template editor";

    static final float FONTTITLESIZE = 16;

    TemplateManager templateManager;

    Template currentTemplate;

    JLabel templateNameLabel = new JLabel("Template name");

    JTextField templateName = new JTextField();

    JLabel typeLabel = new JLabel("Type");

    JComboBox type = new JComboBox();

    JTable repTable = new JTable();

    JButton addRep = new JButton("Add representation");

    JButton delRep = new JButton("Delete representation");

    MetaSeriesPanel metaSeriesPanel;

    /**
     * Linked to MetaSeriesPanel.metaSeriesList & model
     * @see MetaSeriesPanel#metaSeriesList 
     */
    ArrayList<DataSeries> seriesList = new ArrayList<DataSeries>();

    DefaultListModel metaSeriesModel;

    JList metaSeriesList;

    RepresentationTableModel repModel = new RepresentationTableModel();

    TableModelListener tableModelListener = new TableModelListener() {

        public void tableChanged(TableModelEvent e) {
            if (e.getType() == TableModelEvent.UPDATE) {
                repModel.removeTableModelListener(this);
                repModel.sortRows();
                repModel.addTableModelListener(this);
            }
        }
    };

    /**
     * 
     */
    public TemplateEditorPanel(TemplateManager bb) {
        this.templateManager = bb;
        setName(TEMPLATEEDITORPANENAME);
        metaSeriesModel = new DefaultListModel();
        metaSeriesList = new JList(metaSeriesModel);
        String layoutConstraints = "wrap, fill";
        String colConstraints = "[right][left, grow]";
        String rowConstraints = "[][][][][][][][][][fill,grow][]";
        MigLayout layout = new MigLayout(layoutConstraints, colConstraints, rowConstraints);
        setLayout(layout);
        setupWidgets();
        TemplateManager.addSeparator(this, "Template properties");
        add(templateNameLabel);
        add(templateName, "growx, span, wrap");
        add(typeLabel);
        add(type, "span, wrap, grow");
        TemplateManager.addSeparator(this, "Meta-series slots by priority");
        metaSeriesPanel = new MetaSeriesPanel();
        add(metaSeriesPanel, "span, growx, wrap");
        TemplateManager.addSeparator(this, "Representations");
        repTable.setBackground(Color.WHITE);
        JScrollPane tableScroll = new JScrollPane(repTable);
        add(tableScroll, "span, growx, wrap");
        add(addRep, "left");
        add(delRep);
    }

    public static final DataSeries createDataSeries(SeriesMetadata smd) {
        double[] dataArray = ChartTestData.genRandomData(ChartTestData.getDefaultDataPoints(GLChart.BARCHART), ChartTestData.DEFAULTMIN, ChartTestData.DEFAULTMAX);
        DataSeries newSeries = new DataSeries(dataArray, timeArray, smd.getColor());
        newSeries.setLabel(smd.getMetaSeriesLabel());
        newSeries.setStipplePattern(smd.getStipplePattern());
        newSeries.setShadeUnder(smd.isShadeUnder());
        newSeries.setShadeOpacity(smd.getShadeOpacity());
        return newSeries;
    }

    /**
     * Initial widget setup
     *
     */
    private void setupWidgets() {
        delRep.setEnabled(false);
        templateName.addKeyListener(new KeyAdapter() {

            public void keyReleased(KeyEvent e) {
                currentTemplate.setTemplateName(templateName.getText());
                templateManager.templateJList.repaint();
            }
        });
        type.addItem(Template.ALARMNAME);
        type.addItem(Template.METRICNAME);
        type.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int itype = Template.getTypeFromString((String) type.getSelectedItem());
                currentTemplate.setType(itype);
            }
        });
        repTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        repTable.setModel(repModel);
        addRep.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Representation newRep = currentTemplate.createRep();
                RepresentationRow r = new RepresentationRow(newRep, currentTemplate.getSeries(), getTEP());
                repModel.addRow(r);
                delRep.setEnabled(true);
            }
        });
        delRep.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                RepresentationRow r = repModel.delRow(repTable.getSelectedRow());
                currentTemplate.deleteRep(r.rep);
                if (repModel.getRowCount() == 0) delRep.setEnabled(false);
            }
        });
        repTable.setDefaultRenderer(JComponent.class, new JComponentCellRenderer());
        repTable.setDefaultEditor(JComponent.class, new JComponentCellEditor());
        repTable.getColumnModel().getColumn(0).setPreferredWidth(200);
        repTable.getColumnModel().getColumn(0).setCellEditor(new GraphicCellEditor());
        repTable.getColumnModel().getColumn(1).setPreferredWidth(10);
        repTable.getColumnModel().getColumn(1).setCellEditor(new IntegerEditor(1, Integer.MAX_VALUE));
        repTable.getColumnModel().getColumn(2).setPreferredWidth(10);
        repTable.getColumnModel().getColumn(2).setCellEditor(new IntegerEditor(1, Integer.MAX_VALUE));
        repModel.addTableModelListener(tableModelListener);
    }

    private void clearComponents() {
        templateName.setText("");
        repModel.clear();
    }

    SeriesMetadata getCurrentMetaSeries() {
        return (SeriesMetadata) metaSeriesList.getSelectedValue();
    }

    int getSeriesIndex(SeriesMetadata smd) {
        return metaSeriesModel.indexOf(smd);
    }

    /**
	 * Retrieves the dataSeries object associated with 
	 * @return
	 */
    DataSeries getDataSeries(SeriesMetadata smd) {
        final int index = getSeriesIndex(smd);
        if (index == -1) return null;
        return seriesList.get(index);
    }

    void removeDataSeries(SeriesMetadata smd) {
        final int index = getSeriesIndex(smd);
        if (index == -1) {
            System.out.println("TemplateEditorPanel: Could not find series");
            return;
        }
        seriesList.remove(index);
    }

    /**
     * Configures the widgets for the parameter of this template
     * 
     * @param template A LiveRAC template object
     */
    protected void configWidgets(Template template) {
        if (template == null) {
            clearComponents();
            enableAllComponents(false);
            return;
        }
        if (template == currentTemplate) return;
        this.currentTemplate = template;
        String templateName = template.getTemplateName();
        this.templateName.setText(templateName);
        type.setSelectedIndex(template.getType());
        metaSeriesModel.clear();
        seriesList.clear();
        ArrayList<SeriesMetadata> dl = template.getSeries();
        Iterator<SeriesMetadata> smit = dl.iterator();
        SeriesMetadata smd;
        while (smit.hasNext()) {
            smd = smit.next();
            metaSeriesModel.addElement(smd);
            seriesList.add(createDataSeries(smd));
        }
        repModel.clear();
        ArrayList<Representation> repLevels = template.getRepLevels();
        Iterator<Representation> it = repLevels.iterator();
        Representation rep;
        while (it.hasNext()) {
            rep = it.next();
            RepresentationRow rr = new RepresentationRow(rep, dl, this);
            repModel.addRow(rr);
        }
    }

    class RepresentationTableModel extends AbstractTableModel {

        final int colCount = COLNAMES.length;

        final int tableMargin = 2;

        ArrayList<RepresentationRow> mData = new ArrayList<RepresentationRow>();

        /**
	 * 
	 */
        private static final long serialVersionUID = -7625045508537451886L;

        public String getColumnName(int col) {
            if (col >= colCount) {
                System.err.println("RepresentationTableModel: Invalid column index");
                return ("INVALID");
            }
            return COLNAMES[col];
        }

        /**
	 * Clear all rows for this model
	 *
	 */
        public void clear() {
            for (int i = getRowCount(); i > 0; i--) {
                delRow(i - 1);
            }
        }

        public int getColumnCount() {
            return colCount;
        }

        public int getRowCount() {
            return mData.size();
        }

        public boolean isCellEditable(int row, int col) {
            if (col < colCount) {
                return true;
            } else {
                return false;
            }
        }

        @SuppressWarnings("unchecked")
        public Class getColumnClass(int c) {
            Object v = getValueAt(0, c);
            if (v == null) return null;
            return v.getClass();
        }

        /**
	 * Rebuilds all of the chart icons
	 *
	 */
        public void rebuildIcons() {
            for (int i = 0; i < getRowCount(); i++) {
                getRow(i).createIcon();
                fireTableCellUpdated(i, 0);
            }
        }

        public Object getValueAt(int row, int col) {
            RepresentationRow r = mData.get(row);
            return r.getColumn(col);
        }

        public void setValueAt(Object value, int row, int col) {
            RepresentationRow r = mData.get(row);
            r.setColValue(value, col);
            fireTableCellUpdated(row, col);
        }

        public RepresentationRow getRow(int row) {
            return mData.get(row);
        }

        public void tableModified() {
            fireTableDataChanged();
            for (int i = 0; i < getRowCount(); i++) {
                repTable.setRowHeight(i, PackableTable.getPreferredRowHeight(repTable, i, tableMargin));
            }
            repTable.repaint();
        }

        public void addRow(RepresentationRow r) {
            Iterator<RepresentationRow> it = mData.iterator();
            if (mData.size() == 0) {
                mData.add(r);
                fireTableRowsInserted(0, 0);
                repTable.setRowHeight(0, PackableTable.getPreferredRowHeight(repTable, 0, tableMargin));
                return;
            }
            int rowPos = 0;
            RepresentationRow cr;
            while (it.hasNext()) {
                cr = it.next();
                if (r.compareTo(cr) == -1 || r.compareTo(cr) == 0) break;
                rowPos++;
            }
            if (rowPos == mData.size()) mData.add(r); else mData.add(rowPos, r);
            fireTableRowsInserted(rowPos, rowPos);
            repTable.setRowHeight(rowPos, PackableTable.getPreferredRowHeight(repTable, rowPos, tableMargin));
        }

        public RepresentationRow delRow(int rowIndex) {
            RepresentationRow r = mData.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);
            return r;
        }

        public Representation getRowRep(int rowIndex) {
            return mData.get(rowIndex).getRep();
        }

        public void sortRows() {
            if (getRowCount() == 0) return;
            Collections.sort(mData);
            fireTableRowsUpdated(0, getRowCount());
        }
    }

    class GraphicCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

        /**
	 * 
	 */
        private static final long serialVersionUID = -6910359476195442535L;

        JButton button;

        protected static final String EDIT = "edit";

        public GraphicCellEditor() {
            button = new JButton();
            button.setActionCommand(EDIT);
            button.addActionListener(this);
            button.setBorderPainted(true);
            button.setBackground(null);
            button.setForeground(null);
        }

        public void actionPerformed(ActionEvent e) {
            if (EDIT.equals(e.getActionCommand())) {
                fireEditingStopped();
            }
        }

        public Object getCellEditorValue() {
            return null;
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            RepresentationRow rr = repModel.getRow(row);
            Representation rep = rr.getRep();
            ChartLab lab = new ChartLabSingleton(rep, rr.getChart());
            templateManager.disableAllComponents();
            lab.addChartLabClosingListener(new RowAwareChartLabClosingListener(row));
            return button;
        }
    }

    public TemplateEditorPanel getTEP() {
        return this;
    }

    public void enableAllComponents(boolean b) {
        Component[] cs = this.getComponents();
        for (int i = 0; i < cs.length; i++) {
            cs[i].setEnabled(b);
        }
        if (repTable.getRowCount() == 0) delRep.setEnabled(false);
        metaSeriesPanel.enableAllComponents(b);
    }

    class RowAwareChartLabClosingListener implements ChartLabClosingListener {

        int row = 0;

        public RowAwareChartLabClosingListener(int row) {
            this.row = row;
        }

        public void chartLabClosing(ChartLabClosingEvent e) {
            System.out.println("Chart Lab closing!");
            GLChart[] charts = e.getFinalCharts();
            if (charts == null || charts.length == 0) return;
            String chartXML = null;
            try {
                chartXML = GLChart.getXMLSettings(charts[0]);
            } catch (SAXException e1) {
                System.err.println("TemplateEditorPanel: SAX Exception");
                e1.printStackTrace();
            } catch (Exception e1) {
                System.err.println("TemplateEditorPanel: Exception");
                e1.printStackTrace();
            }
            Representation r = repModel.getRowRep(row);
            final int chartType = charts[0].getChartType();
            r.setChartXML(chartXML);
            r.setChartType(chartType);
            repModel.getRow(row).createChart();
            repModel.getRow(row).createIcon();
            templateManager.enableAllComponents();
        }
    }

    class MetaSeriesPanel extends JPanel {

        /**
	 * 
	 */
        private static final long serialVersionUID = 1L;

        JButton addButton = new JButton("Add");

        JButton delButton = new JButton("Delete");

        JCheckBox setShadeUnder = new JCheckBox();

        JLabel setShadeOpacityLabel = new JLabel("Shade opacity");

        static final double SHADEOPACITYMIN = 0d;

        static final double SHADEOPACITYMAX = 1d;

        static final double SHADEOPACITYSTEP = .05;

        static final double SHADEOPACITYDEFAULT = .5d;

        JSpinner setShadeOpacity = new JSpinner(new SpinnerNumberModel(SHADEOPACITYDEFAULT, SHADEOPACITYMIN, SHADEOPACITYMAX, SHADEOPACITYSTEP));

        JLabel setColorLabel = new JLabel("Series color");

        JButton setColor = new JButton();

        JLabel setStipplePatternLabel = new JLabel("Stipple pattern");

        JComboBox setStipplePattern = new JComboBox();

        boolean listen = true;

        public void enableAllComponents(Boolean b) {
            Component[] clist = this.getComponents();
            for (int i = 0; i < clist.length; i++) {
                clist[0].setEnabled(b);
            }
            addButton.setEnabled(b);
            delButton.setEnabled(b);
            metaSeriesList.setEnabled(b);
            enableSeriesComponents(b);
        }

        public MetaSeriesPanel() {
            final String layoutConstraints = "wrap, fill";
            final String colConstraints = "[right][fill,sizegroup]unrel[right][fill,sizegroup]";
            final String rowConstraints = "[][][][]";
            MigLayout layout = new MigLayout(layoutConstraints, colConstraints, rowConstraints);
            setLayout(layout);
            setupComponents();
            JPanel leftPanel = new JPanel();
            leftPanel.setLayout(new MigLayout(layoutConstraints, "[][]", "[][]"));
            leftPanel.add(new JScrollPane(metaSeriesList), "grow, span, wrap");
            leftPanel.add(addButton, "growx");
            leftPanel.add(delButton, "growx");
            add(leftPanel, "dock west, wmin 150, hmin 150, gapright 10px");
            TemplateManager.addSeparator(this, "Selected Meta-series settings");
            add(setColorLabel);
            add(setColor);
            add(setStipplePatternLabel);
            add(setStipplePattern, "wrap");
            add(setShadeUnder, "span 2");
            add(setShadeOpacityLabel);
            add(setShadeOpacity, "wrap");
        }

        private void enableSeriesComponents(Boolean b) {
            if (getCurrentMetaSeries() == null) {
                b = false;
            }
            setColor.setEnabled(b);
            setStipplePattern.setEnabled(b);
            setShadeUnder.setEnabled(b);
            setShadeOpacity.setEnabled(b);
        }

        void configWidgets(SeriesMetadata s) {
            if (s == null) {
                enableSeriesComponents(false);
                return;
            }
            listen = false;
            enableSeriesComponents(true);
            setColor.setBackground(s.getColor());
            setShadeUnder.setSelected(s.isShadeUnder());
            setShadeOpacity.setValue((double) s.getShadeOpacity());
            StipplePattern p = s.getStipplePattern();
            if (p == null) p = StipplePattern.solidPattern();
            setStipplePattern.setSelectedItem(StipplePattern.LINESTIPPLETYPES[p.getStippleTypeIndex()]);
            listen = true;
        }

        private void setupComponents() {
            setShadeUnder.setText("Shade under");
            metaSeriesList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
            for (int i = 0; i < StipplePattern.LINESTIPPLETYPES.length; i++) {
                setStipplePattern.addItem(StipplePattern.LINESTIPPLETYPES[i]);
            }
            metaSeriesList.addListSelectionListener(new ListSelectionListener() {

                public void valueChanged(ListSelectionEvent e) {
                    configWidgets((SeriesMetadata) metaSeriesList.getSelectedValue());
                }
            });
            addButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    String seriesName = "Slot " + metaSeriesModel.getSize();
                    SeriesMetadata smd = currentTemplate.createSeries(seriesName);
                    metaSeriesModel.addElement(smd);
                    seriesList.add(createDataSeries(smd));
                    for (int i = 0; i < repModel.getRowCount(); i++) {
                        repModel.getRow(i).addSeries(smd);
                    }
                    repTable.repaint();
                }
            });
            delButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    SeriesMetadata s = (SeriesMetadata) metaSeriesList.getSelectedValue();
                    if (s == null) return;
                    for (int i = 0; i < repModel.getRowCount(); i++) {
                        repModel.getRow(i).delSeries(s);
                    }
                    removeDataSeries(s);
                    metaSeriesModel.removeElement(s);
                    currentTemplate.deleteSeries(s);
                    repTable.repaint();
                }
            });
            setColor.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (listen == false) return;
                    JColorChooser chooser = new JColorChooser();
                    JColorChooser.createDialog(templateManager, setColorLabel.getText(), true, chooser, new ColorActionListener(chooser) {

                        public void actionPerformed(ActionEvent e) {
                            setColor.setBackground(this.chooser.getColor());
                            SeriesMetadata smd = getCurrentMetaSeries();
                            smd.setColor(this.chooser.getColor());
                            getDataSeries(smd).setColor(this.chooser.getColor());
                            repModel.rebuildIcons();
                        }
                    }, null).setVisible(true);
                }
            });
            setShadeUnder.addItemListener(new ItemListener() {

                public void itemStateChanged(ItemEvent e) {
                    if (listen == false) return;
                    SeriesMetadata smd = getCurrentMetaSeries();
                    smd.setShadeUnder(setShadeUnder.isSelected());
                    getDataSeries(smd).setShadeUnder(setShadeUnder.isSelected());
                    repModel.rebuildIcons();
                }
            });
            setShadeOpacity.getModel().addChangeListener(new ChangeListener() {

                public void stateChanged(ChangeEvent e) {
                    if (listen == false) return;
                    SeriesMetadata smd = getCurrentMetaSeries();
                    smd.setShadeOpacity(((Double) setShadeOpacity.getValue()).floatValue());
                    getDataSeries(smd).setShadeOpacity(((Double) setShadeOpacity.getValue()).floatValue());
                    repModel.rebuildIcons();
                }
            });
            setStipplePattern.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (listen == false) return;
                    SeriesMetadata smd = getCurrentMetaSeries();
                    StipplePattern p = StipplePattern.getStippleByName((String) setStipplePattern.getSelectedItem());
                    smd.setStipplePattern(p);
                    getDataSeries(smd).setStipplePattern(p);
                    repModel.rebuildIcons();
                }
            });
        }
    }

    /**
     * A simple class that defines some needed functionality for the color chooser
     * @author Peter McLachlan <spark343@cs.ubc.ca>
     *
     */
    public abstract class ColorActionListener implements ActionListener {

        protected JColorChooser chooser;

        public ColorActionListener(JColorChooser chooser) {
            this.chooser = chooser;
        }
    }
}
