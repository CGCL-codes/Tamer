package variation;

import graph.Graph;
import graph.GraphAxis;
import graph.GraphField;
import graph.GraphField.IGraphField;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ProgressMonitor;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import preferences.Preference;
import utils.ColourScheme;
import utils.DoubleCellRenderer;
import utils.DoubleEditor;
import utils.IntegerCellRenderer;
import common.Buttons;
import common.CheckBoxField;
import common.Dialogs;
import common.FillControlDouble;
import common.IntField;
import common.ItemActionListener;
import common.ItemFinder;
import common.NameField;
import common.Panels;
import common.CheckBoxField.IChangeEvent;
import common.Dialogs.SimpleDialog;
import database.DbGraph;
import database.GraphParams;
import database.DbGraph.ILoadGraphResults;

/**
 * @author Roo and Joey
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class VariationActionListener extends ItemActionListener {

    public static final int OVERALL_DURATION_PERCENT_BIT = 8;

    public static final int OVERALL_OFFSET_PERCENT_BIT = 4;

    public static final int DURATION_PERCENT_BIT = 2;

    public static final int INTERVAL_PERCENT_BIT = 1;

    private static final double MIN_DOUBLE_PARAM = 0.0;

    private static final double MAX_DOUBLE_PARAM = 1024.0;

    private static final int MAX_ROWS = 1024;

    private JFrame m_frame;

    private JTable m_indexesTable;

    private VariationIndexesTableModel m_indexesTableModel;

    private Variation m_variation;

    private NameField m_nameFld;

    private CheckBoxField m_intervalPercentCBFld;

    private CheckBoxField m_durationPercentCBFld;

    private CheckBoxField m_overallOffsetPercentCBFld;

    private CheckBoxField m_overallDurationPercentCBFld;

    private FillControlHandler m_fillControlHandler;

    /**
     * @param frame
     */
    public VariationActionListener(JFrame frame) {
        setAction(new IAction() {

            public void dialogCreateItem(boolean updateMode) {
                dialogCreateVariation(updateMode);
            }

            public ItemFinder newFinder(JFrame parentFrame, JFormattedTextField selectionFld, boolean deleteMode) {
                return new VariationFinder(parentFrame, selectionFld, deleteMode);
            }
        });
        m_frame = frame;
    }

    enum FillDest {

        INTERVAL, INITIAL_INTERVAL, DURATION, OVERALL_OFFSET, OVERALL_DURATION, FLAGS
    }

    ;

    private final class FillControlHandler implements FillControlDouble.IValue, ActionListener {

        private static final String OVERALL_DURATION = "Overall duration";

        private static final String OVERALL_OFFSET = "Overall offset";

        private static final String DURATION = "Duration";

        private static final String INTERVAL = "Interval";

        private static final String INITIAL_INTERVAL = "Initial interval";

        private static final String FLAGS = "Flags";

        private CheckBoxField m_overallGraphCBFld;

        private CheckBoxField m_periodGraphCBFld;

        private CheckBoxField m_intervalGraphCBFld;

        private CheckBoxField m_durationGraphCBFld;

        private CheckBoxField m_initialIntervalCBFld;

        private CheckBoxField m_zeroCBFld;

        private FillDest m_curDest = FillDest.INTERVAL;

        private boolean m_flagsEnable = true;

        private byte m_curMask = 0;

        /**
         * @return Returns the curDest.
         */
        public FillDest getCurDest() {
            return m_curDest;
        }

        public double getValue(int rowOffset) {
            if (m_curDest == FillDest.INTERVAL) return m_indexesTableModel.getInterval(rowOffset); else if (m_curDest == FillDest.INITIAL_INTERVAL) return m_indexesTableModel.getInitialInterval(rowOffset); else if (m_curDest == FillDest.DURATION) return m_indexesTableModel.getDuration(rowOffset); else if (m_curDest == FillDest.OVERALL_OFFSET) return m_indexesTableModel.getOverallOffset(rowOffset); else if (m_curDest == FillDest.OVERALL_DURATION) return m_indexesTableModel.getOverallDuration(rowOffset); else if (m_curDest == FillDest.FLAGS) return m_indexesTableModel.getFlags(rowOffset, m_curMask);
            return 0.0;
        }

        public double filterValue(double getValue) {
            if (m_curDest == FillDest.FLAGS) {
                if (getValue < 0.5) getValue = 0; else getValue = 63;
            }
            return getValue;
        }

        public void setValue(int rowOffset, double value) {
            if (m_curDest == FillDest.INTERVAL) m_indexesTableModel.setInterval(rowOffset, value); else if (m_curDest == FillDest.INITIAL_INTERVAL) m_indexesTableModel.setInitialInterval(rowOffset, value); else if (m_curDest == FillDest.DURATION) m_indexesTableModel.setDuration(rowOffset, value); else if (m_curDest == FillDest.OVERALL_OFFSET) m_indexesTableModel.setOverallOffset(rowOffset, value); else if (m_curDest == FillDest.OVERALL_DURATION) m_indexesTableModel.setOverallDuration(rowOffset, value); else if (m_curDest == FillDest.FLAGS) m_indexesTableModel.setFlags(rowOffset, (byte) value, m_curMask);
        }

        public void onFill() {
            m_variation.calcMaxY();
            m_variation.onPreview();
        }

        public int getNoOfValues() {
            return m_indexesTableModel.getRowCount();
        }

        public String getName() {
            return m_nameFld.getName();
        }

        public boolean saveSynchronously(String saveName, ProgressMonitor pm) {
            return m_variation.save(saveName, true, pm, false, true);
        }

        protected JRadioButton createRadioButton(String title, String tooltip) {
            JRadioButton radBtn = new JRadioButton(title);
            radBtn.setToolTipText(tooltip);
            radBtn.setActionCommand(title);
            radBtn.addActionListener(this);
            return radBtn;
        }

        private void addRadioButton(JPanel panel, JRadioButton radioButton, ButtonGroup buttonGroup) {
            buttonGroup.add(radioButton);
            panel.add(radioButton);
        }

        public JPanel getUserPanel() {
            JPanel userPanel = Panels.createBoxPanel();
            JPanel radioGrpPanel = new JPanel();
            JPanel radioGrpPanel1 = Panels.createBoxPanel();
            JPanel radioGrpPanel2 = Panels.createBoxPanel();
            radioGrpPanel.add(radioGrpPanel1);
            radioGrpPanel.add(radioGrpPanel2);
            userPanel.add(radioGrpPanel);
            JRadioButton intervalBtn = createRadioButton(INTERVAL, "Set interval");
            intervalBtn.setSelected(true);
            JRadioButton initialIntervalBtn = createRadioButton(INITIAL_INTERVAL, "Set initial interval");
            JRadioButton durationBtn = createRadioButton(DURATION, "Set Duration");
            JRadioButton overallOffsetBtn = createRadioButton(OVERALL_OFFSET, "Set overall offset");
            JRadioButton overallDurationBtn = createRadioButton(OVERALL_DURATION, "Set overall duration");
            JRadioButton flagsBtn = createRadioButton(FLAGS, "Set flags");
            ButtonGroup destGroup = new ButtonGroup();
            addRadioButton(radioGrpPanel1, intervalBtn, destGroup);
            addRadioButton(radioGrpPanel1, initialIntervalBtn, destGroup);
            addRadioButton(radioGrpPanel1, durationBtn, destGroup);
            addRadioButton(radioGrpPanel2, overallOffsetBtn, destGroup);
            addRadioButton(radioGrpPanel2, overallDurationBtn, destGroup);
            addRadioButton(radioGrpPanel2, flagsBtn, destGroup);
            JPanel flagsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            m_overallGraphCBFld = new CheckBoxField("OG", KeyEvent.VK_O, false, new IChangeEvent() {

                public void notifyChange(boolean newVal) {
                    int bit = VariationIndexesTableModel.OVERALL_GRAPH_ENABLE_BIT;
                    if (newVal) m_curMask |= bit; else m_curMask &= ~bit;
                }
            });
            m_periodGraphCBFld = new CheckBoxField("PG", KeyEvent.VK_P, false, new IChangeEvent() {

                public void notifyChange(boolean newVal) {
                    int bit = VariationIndexesTableModel.PERIOD_GRAPH_ENABLE_BIT;
                    if (newVal) m_curMask |= bit; else m_curMask &= ~bit;
                }
            });
            m_intervalGraphCBFld = new CheckBoxField("IG", KeyEvent.VK_I, false, new IChangeEvent() {

                public void notifyChange(boolean newVal) {
                    int bit = VariationIndexesTableModel.INTERVAL_GRAPH_ENABLE_BIT;
                    if (newVal) m_curMask |= bit; else m_curMask &= ~bit;
                }
            });
            m_durationGraphCBFld = new CheckBoxField("DG", KeyEvent.VK_D, false, new IChangeEvent() {

                public void notifyChange(boolean newVal) {
                    int bit = VariationIndexesTableModel.DURATION_GRAPH_ENABLE_BIT;
                    if (newVal) m_curMask |= bit; else m_curMask &= ~bit;
                }
            });
            m_initialIntervalCBFld = new CheckBoxField("II", KeyEvent.VK_I, false, new IChangeEvent() {

                public void notifyChange(boolean newVal) {
                    int bit = VariationIndexesTableModel.INITIAL_INTERVAL_BIT;
                    if (newVal) m_curMask |= bit; else m_curMask &= ~bit;
                }
            });
            m_zeroCBFld = new CheckBoxField("Z", KeyEvent.VK_Z, false, new IChangeEvent() {

                public void notifyChange(boolean newVal) {
                    int bit = VariationIndexesTableModel.ZERO_BIT;
                    if (newVal) m_curMask |= bit; else m_curMask &= ~bit;
                }
            });
            enableFlags(false);
            flagsPanel.add(m_overallGraphCBFld.getCB());
            flagsPanel.add(m_periodGraphCBFld.getCB());
            flagsPanel.add(m_intervalGraphCBFld.getCB());
            flagsPanel.add(m_durationGraphCBFld.getCB());
            flagsPanel.add(m_initialIntervalCBFld.getCB());
            flagsPanel.add(m_zeroCBFld.getCB());
            userPanel.add(flagsPanel);
            return userPanel;
        }

        void enableFlags(boolean enable) {
            if (m_flagsEnable != enable) {
                m_overallGraphCBFld.getCB().setEnabled(enable);
                m_periodGraphCBFld.getCB().setEnabled(enable);
                m_intervalGraphCBFld.getCB().setEnabled(enable);
                m_durationGraphCBFld.getCB().setEnabled(enable);
                m_initialIntervalCBFld.getCB().setEnabled(enable);
                m_zeroCBFld.getCB().setEnabled(enable);
                m_flagsEnable = enable;
            }
        }

        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals(INTERVAL)) {
                m_curDest = FillDest.INTERVAL;
                enableFlags(false);
            } else if (e.getActionCommand().equals(INITIAL_INTERVAL)) {
                m_curDest = FillDest.INITIAL_INTERVAL;
                enableFlags(false);
            } else if (e.getActionCommand().equals(DURATION)) {
                m_curDest = FillDest.DURATION;
                enableFlags(false);
            } else if (e.getActionCommand().equals(OVERALL_OFFSET)) {
                m_curDest = FillDest.OVERALL_OFFSET;
                enableFlags(false);
            } else if (e.getActionCommand().equals(OVERALL_DURATION)) {
                m_curDest = FillDest.OVERALL_DURATION;
                enableFlags(false);
            } else if (e.getActionCommand().equals(FLAGS)) {
                m_curDest = FillDest.FLAGS;
                enableFlags(true);
            }
        }
    }

    private class IndexesTableModelListener implements TableModelListener {

        public void tableChanged(TableModelEvent e) {
            m_indexesTable.revalidate();
            m_indexesTable.repaint();
        }
    }

    /**
     * @return
     */
    private JTable createIndexesTable() {
        m_indexesTableModel = new VariationIndexesTableModel();
        m_indexesTable = new JTable(m_indexesTableModel);
        m_indexesTableModel.addTableModelListener(new IndexesTableModelListener());
        setupColumnTypes(m_indexesTable);
        return m_indexesTable;
    }

    /**
     * @author Roo and Joey
     *
     * TODO To change the template for this generated type comment go to
     * Window - Preferences - Java - Code Style - Code Templates
     */
    private static class SelectColRender extends JCheckBox implements TableCellRenderer {

        private VariationIndexesTableModel m_tblModel;

        private int m_selectColumn;

        /**
         * @param tableModel
         * @param selectColumn
         */
        public SelectColRender(VariationIndexesTableModel tableModel, int selectColumn) {
            m_tblModel = tableModel;
            m_selectColumn = selectColumn;
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            boolean rowOdd = (row & 1) == 1;
            setOpaque(true);
            ColourScheme.Table.setupColours(this, rowOdd, false, isSelected);
            setSelected(((Boolean) m_tblModel.getValueAt(row, m_selectColumn)).booleanValue());
            setHorizontalAlignment(SwingConstants.CENTER);
            return this;
        }
    }

    private void setupDoubleColumn(JTable indexesTable, int colId, DoubleEditor editor, DoubleCellRenderer renderer) {
        TableColumn column = indexesTable.getColumnModel().getColumn(colId);
        column.setCellEditor(editor);
        column.setCellRenderer(renderer);
    }

    private void setupSelectColRenderer(JTable indexesTable, int colId) {
        indexesTable.getColumnModel().getColumn(colId).setCellRenderer(new SelectColRender(m_indexesTableModel, colId));
    }

    private void setupColumnTypes(JTable indexesTable) {
        TableColumn indexCol = indexesTable.getColumnModel().getColumn(VariationIndexesTableModel.INDEX_NUMBER_COLUMN);
        indexCol.setCellRenderer(IntegerCellRenderer.getRenderer(true));
        DoubleEditor editor = new DoubleEditor((double) MIN_DOUBLE_PARAM, (double) (MAX_DOUBLE_PARAM));
        DoubleCellRenderer renderer = DoubleCellRenderer.getRenderer(false);
        setupDoubleColumn(indexesTable, VariationIndexesTableModel.INTERVAL_COLUMN, editor, renderer);
        setupDoubleColumn(indexesTable, VariationIndexesTableModel.INITIAL_INTVAL_COLUMN, editor, renderer);
        setupDoubleColumn(indexesTable, VariationIndexesTableModel.DURATION_COLUMN, editor, renderer);
        setupDoubleColumn(indexesTable, VariationIndexesTableModel.OVERALL_OFFSET_COLUMN, editor, renderer);
        setupDoubleColumn(indexesTable, VariationIndexesTableModel.OVERALL_DURATION_COLUMN, editor, renderer);
        setupSelectColRenderer(indexesTable, VariationIndexesTableModel.INTERVAL_GRAPH_ENABLE_COLUMN);
        setupSelectColRenderer(indexesTable, VariationIndexesTableModel.DURATION_GRAPH_ENABLE_COLUMN);
        setupSelectColRenderer(indexesTable, VariationIndexesTableModel.OVERALL_GRAPH_ENABLE_COLUMN);
        setupSelectColRenderer(indexesTable, VariationIndexesTableModel.PERIOD_GRAPH_ENABLE_COLUMN);
        setupSelectColRenderer(indexesTable, VariationIndexesTableModel.INITIAL_INTERVAL_COLUMN);
        setupSelectColRenderer(indexesTable, VariationIndexesTableModel.ZERO_COLUMN);
    }

    private JPanel createParamsPanel(JTable indexesTable, final SimpleDialog dialog, final Variation variation, boolean updateMode) {
        JPanel boxPanel = Panels.createBoxPanel();
        m_nameFld = NameField.newInstance();
        JPanel namePanel = m_nameFld.createNamePanel("variation", updateMode, variation, dialog);
        variation.setNameField(m_nameFld);
        boxPanel.add(namePanel);
        JPanel miscParamsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        IntField noOfIndexesFld = new IntField("Number of indexes (1.." + MAX_ROWS + "):", 1, MAX_ROWS, 1, new ItemActionListener.RowCountFieldChangeListener(m_indexesTableModel, indexesTable, MAX_ROWS));
        variation.setNoOfVariationIndexesFld(noOfIndexesFld);
        m_intervalPercentCBFld = new CheckBoxField("Interval %", KeyEvent.VK_I, false);
        m_durationPercentCBFld = new CheckBoxField("Duration %", KeyEvent.VK_D, false);
        m_overallOffsetPercentCBFld = new CheckBoxField("Overall ofs %", KeyEvent.VK_O, false);
        m_overallDurationPercentCBFld = new CheckBoxField("Overall duration %", KeyEvent.VK_V, false);
        miscParamsPanel.add(noOfIndexesFld.getPanel());
        miscParamsPanel.add(m_intervalPercentCBFld.getCB());
        miscParamsPanel.add(m_durationPercentCBFld.getCB());
        miscParamsPanel.add(m_overallOffsetPercentCBFld.getCB());
        miscParamsPanel.add(m_overallDurationPercentCBFld.getCB());
        JPanel graphPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        int maxGraphFldWidth = 150;
        GraphField overallGraphField = new GraphField(dialog, new IGraphField() {

            public void notifyGraphFieldChange(String graphField) {
                if (graphField.length() == 0) {
                    variation.setOverallGraphId(0);
                    return;
                }
                DbGraph.load(m_frame, graphField, new ILoadGraphResults() {

                    public void setResults(GraphParams params) {
                        variation.setOverallGraphId(params.getGraphId());
                        dialog.getFrame().toFront();
                    }
                });
            }
        }, "Overall", true, maxGraphFldWidth);
        variation.setOverallGraphFld(overallGraphField);
        graphPanel.add(overallGraphField.getPanel());
        GraphField periodGraphField = new GraphField(dialog, new IGraphField() {

            public void notifyGraphFieldChange(String graphField) {
                if (graphField.length() == 0) {
                    variation.setPeriodGraphId(0);
                    return;
                }
                DbGraph.load(m_frame, graphField, new ILoadGraphResults() {

                    public void setResults(GraphParams params) {
                        variation.setPeriodGraphId(params.getGraphId());
                        dialog.getFrame().toFront();
                    }
                });
            }
        }, "Period", true, maxGraphFldWidth);
        variation.setPeriodGraphFld(periodGraphField);
        graphPanel.add(periodGraphField.getPanel());
        GraphField intervalGraphField = new GraphField(dialog, new IGraphField() {

            public void notifyGraphFieldChange(String graphField) {
                if (graphField.length() == 0) {
                    variation.setIntervalGraphId(0);
                    return;
                }
                DbGraph.load(m_frame, graphField, new ILoadGraphResults() {

                    public void setResults(GraphParams params) {
                        variation.setIntervalGraphId(params.getGraphId());
                        dialog.getFrame().toFront();
                    }
                });
            }
        }, "Interval", true, maxGraphFldWidth);
        variation.setIntervalGraphFld(intervalGraphField);
        graphPanel.add(intervalGraphField.getPanel());
        GraphField durationGraphField = new GraphField(dialog, new IGraphField() {

            public void notifyGraphFieldChange(String graphField) {
                if (graphField.length() == 0) {
                    variation.setDurationGraphId(0);
                    return;
                }
                DbGraph.load(m_frame, graphField, new ILoadGraphResults() {

                    public void setResults(GraphParams params) {
                        variation.setDurationGraphId(params.getGraphId());
                        dialog.getFrame().toFront();
                    }
                });
            }
        }, "Duration", true, maxGraphFldWidth);
        variation.setDurationGraphFld(durationGraphField);
        graphPanel.add(durationGraphField.getPanel());
        boxPanel.add(miscParamsPanel);
        boxPanel.add(graphPanel);
        boxPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        return boxPanel;
    }

    Component getFillControlPanel(final SimpleDialog dialog) {
        m_fillControlHandler = new FillControlHandler();
        FillControlDouble fillControl = FillControlDouble.newInstance(m_fillControlHandler, dialog);
        return fillControl.getPanel();
    }

    interface IFlagPanel {

        double getNdxVal(int ndx);

        void setView(VariationGraphView view);
    }

    class VariationDialog extends Dialogs.SimpleDialog {

        private final boolean m_updateMode;

        private JSplitPane m_splitPane;

        VariationDialog(boolean updateMode) {
            super(m_frame, false);
            m_updateMode = updateMode;
        }

        @Override
        protected JPanel getEntirePanel(SimpleDialog dialog) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setPreferredSize(new Dimension(900 + Graph.BORDER_THICKNESS * 4 + Graph.RESERVED_X, 900));
            JTable indexesTable = createIndexesTable();
            m_variation = new Variation(m_indexesTableModel, m_frame, m_updateMode, VariationActionListener.this, this);
            JPanel paramsPanel = createParamsPanel(indexesTable, dialog, m_variation, m_updateMode);
            JPanel upperPanel = Panels.createBoxPanel();
            JScrollPane paramsScrollPane = new JScrollPane(paramsPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            upperPanel.add(paramsScrollPane);
            upperPanel.add(getFillControlPanel(dialog));
            panel.add(upperPanel, BorderLayout.PAGE_START);
            JTabbedPane tabbedPane = new JTabbedPane();
            final VariationGraphView variationIntervalView = new VariationGraphView(m_variation) {

                @Override
                double getVariationNdxValue(int ndx) {
                    return m_indexesTableModel.getInterval(ndx);
                }
            };
            m_variation.setIntervalView(variationIntervalView);
            setVariationSize(variationIntervalView, 800, 400);
            tabbedPane.addTab("Interval graph", null, variationIntervalView, "Graph of interval values");
            final VariationGraphView variationInitialIntervalView = new VariationGraphView(m_variation) {

                @Override
                double getVariationNdxValue(int ndx) {
                    return m_indexesTableModel.getInitialInterval(ndx);
                }
            };
            m_variation.setInitialIntervalView(variationInitialIntervalView);
            tabbedPane.addTab("Initial Interval graph", null, variationInitialIntervalView, "Graph of initial interval values");
            final VariationGraphView variationDurationView = new VariationGraphView(m_variation) {

                @Override
                double getVariationNdxValue(int ndx) {
                    return m_indexesTableModel.getDuration(ndx);
                }
            };
            m_variation.setDurationView(variationDurationView);
            tabbedPane.addTab("Duration graph", null, variationDurationView, "Graph of duration values");
            final VariationGraphView variationOverallOffsetView = new VariationGraphView(m_variation) {

                @Override
                double getVariationNdxValue(int ndx) {
                    return m_indexesTableModel.getOverallOffset(ndx);
                }
            };
            m_variation.setOverallOffsetView(variationOverallOffsetView);
            tabbedPane.addTab("Overall offset graph", null, variationOverallOffsetView, "Graph of overall offset values");
            final VariationGraphView variationOverallDurationView = new VariationGraphView(m_variation) {

                @Override
                double getVariationNdxValue(int ndx) {
                    return m_indexesTableModel.getOverallDuration(ndx);
                }
            };
            m_variation.setOverallDurationView(variationOverallDurationView);
            tabbedPane.addTab("Overall duration graph", null, variationOverallDurationView, "Graph of overall duration values");
            JPanel flagsView = new VariationFlagsView(m_variation, m_indexesTableModel, 800, 400, 1, null, ItemFinder.DEF_AXIS_DETAIL_LEVEL).getFlagsView();
            setVariationSize(flagsView, 800, 400);
            tabbedPane.addTab("Flags graphs", null, flagsView, "Graphs of flag values");
            m_splitPane = setupSplitPane(indexesTable, tabbedPane);
            panel.add(m_splitPane, BorderLayout.CENTER);
            Buttons buttons = Buttons.newInstance();
            JPanel btnPanel = buttons.createCommonButtonsPanel(m_updateMode, new Buttons.IEvents() {

                public void onSave() {
                    m_variation.onSave();
                }

                public void onPreview() {
                    m_variation.calcMaxY();
                    m_variation.onPreview();
                }
            });
            m_variation.setButtons(buttons);
            panel.add(btnPanel, BorderLayout.LINE_END);
            return panel;
        }

        protected void handleFocus() {
            m_nameFld.setFocus();
        }

        @Override
        protected void saveProperties() {
            Preference.setDividerLocation(getClass(), m_splitPane.getDividerLocation());
        }
    }

    /**
     * @param updateMode
     */
    protected void dialogCreateVariation(boolean updateMode) {
        VariationDialog dialog = new VariationDialog(updateMode);
        dialog.showDialog(Dialogs.getTitle(updateMode, "Variation"), false);
    }

    public static void setVariationSize(Component variation, int x, int y) {
        setVariationSize(variation, x, y, true);
    }

    public static void setVariationSize(Component variation, int x, int y, boolean heightAdjust) {
        int width = x + Variation.BORDER_THICKNESS * 2 + GraphAxis.RESERVED_X;
        int height = y;
        if (heightAdjust) height += Variation.BORDER_THICKNESS * 2 + GraphAxis.RESERVED_Y;
        Dimension graphSize = new Dimension(width, height);
        variation.setPreferredSize(graphSize);
        {
        }
    }

    public byte getFlags() {
        byte flags = 0;
        if (m_intervalPercentCBFld.getSelected()) flags = INTERVAL_PERCENT_BIT;
        if (m_durationPercentCBFld.getSelected()) flags |= DURATION_PERCENT_BIT;
        if (m_overallOffsetPercentCBFld.getSelected()) flags |= OVERALL_OFFSET_PERCENT_BIT;
        if (m_overallDurationPercentCBFld.getSelected()) flags |= OVERALL_DURATION_PERCENT_BIT;
        return flags;
    }

    public void setFlags(byte flags) {
        m_intervalPercentCBFld.setSelected((flags & INTERVAL_PERCENT_BIT) != 0);
        m_durationPercentCBFld.setSelected((flags & DURATION_PERCENT_BIT) != 0);
        m_overallOffsetPercentCBFld.setSelected((flags & OVERALL_OFFSET_PERCENT_BIT) != 0);
        m_overallDurationPercentCBFld.setSelected((flags & OVERALL_DURATION_PERCENT_BIT) != 0);
    }
}
