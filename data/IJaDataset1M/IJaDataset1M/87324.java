package variation;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.table.TableColumn;
import variation.VariationActionListener.IFlagPanel;
import database.VariationDb;

public class VariationFlagsView extends JPanel {

    private static final int NO_OF_FLAGS = 6;

    private Variation m_variation;

    private VariationIndexesTableModel m_indexesTableModel;

    private int m_initialWidth;

    private int m_initialHeight;

    private int m_maxXValue;

    private VariationGraphView m_overallPanel;

    private VariationGraphView m_periodPanel;

    private VariationGraphView m_intervalPanel;

    private VariationGraphView m_durationPanel;

    private VariationGraphView m_initialIntervalPanel;

    private VariationGraphView m_zeroPanel;

    private final int m_markHeight;

    private final TableColumn m_tableCol;

    private int m_axisDetailLevel;

    /**
     * @param variation
     * @param indexesTableModel
     * @param initialWidth
     * @param initialHeight
     * @param axisDetailLevel 
     */
    public VariationFlagsView(Variation variation, VariationIndexesTableModel indexesTableModel, int initialWidth, int initialHeight, int markHeight, TableColumn tableCol, int axisDetailLevel) {
        super(new GridBagLayout());
        m_variation = variation;
        m_indexesTableModel = indexesTableModel;
        m_initialWidth = initialWidth;
        m_initialHeight = initialHeight;
        m_markHeight = markHeight;
        m_tableCol = tableCol;
        m_axisDetailLevel = axisDetailLevel;
    }

    /**
     * @param variation
     * @param initialWidth
     * @param initialHeight
     */
    public VariationFlagsView(Variation variation, int initialWidth, int initialHeight, int markHeight, TableColumn tableCol, int axisDetailLevel) {
        this(variation, null, initialWidth, initialHeight, markHeight, tableCol, axisDetailLevel);
    }

    void initialise(VariationDb params) {
        m_variation.initialise(params);
        m_indexesTableModel = m_variation.getVariationIndexesTableModel();
        m_maxXValue = params.getVariationParams().length;
        getFlagsView();
        VariationActionListener.setVariationSize(this, m_initialWidth, m_initialHeight, true);
    }

    public void setAxisDetailLevel(int axisDetailLevel) {
        m_axisDetailLevel = axisDetailLevel;
        m_overallPanel.setAxisDetailLevel(m_axisDetailLevel);
        m_periodPanel.setAxisDetailLevel(m_axisDetailLevel);
        m_intervalPanel.setAxisDetailLevel(m_axisDetailLevel);
        m_durationPanel.setAxisDetailLevel(m_axisDetailLevel);
        m_initialIntervalPanel.setAxisDetailLevel(m_axisDetailLevel);
        m_zeroPanel.setAxisDetailLevel(m_axisDetailLevel);
    }

    void setPreview(boolean on) {
        m_variation.setPreview(on);
    }

    @Override
    public void setPreferredSize(Dimension arg0) {
        super.setPreferredSize(arg0);
    }

    private VariationGraphView createFlagPanelRow(String label, final IFlagPanel iflagPanel, GridBagConstraints constraints, JPanel flagsPanel, final int row) {
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.fill = GridBagConstraints.NONE;
        flagsPanel.add(new JLabel(label), constraints);
        constraints.gridx = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.BOTH;
        final VariationGraphView graphView = new VariationBooleanGraphView(m_variation, m_axisDetailLevel) {

            @Override
            double getVariationNdxValue(int ndx) {
                return iflagPanel.getNdxVal(ndx);
            }
        };
        graphView.setMaxXValue(m_maxXValue);
        VariationActionListener.setVariationSize(graphView, m_initialWidth - 20, m_initialHeight / NO_OF_FLAGS, false);
        iflagPanel.setView(graphView);
        flagsPanel.add(graphView, constraints);
        flagsPanel.addComponentListener(new ComponentAdapter() {

            public void componentResized(ComponentEvent e) {
                Component c = e.getComponent();
                int width = c.getWidth();
                int height = c.getHeight();
                graphView.setPreferredSize(new Dimension(width - 20, height / NO_OF_FLAGS));
                graphView.revalidate();
                graphView.repaint();
            }
        });
        return graphView;
    }

    JPanel getFlagsView() {
        GridBagConstraints constraints = new GridBagConstraints();
        m_indexesTableModel.setMarkHeight(m_markHeight);
        m_overallPanel = createFlagPanelRow("OG", new IFlagPanel() {

            public double getNdxVal(int ndx) {
                return m_indexesTableModel.getOverallGraphFlag(ndx);
            }

            public void setView(VariationGraphView view) {
                m_variation.setOverallGraphFlagView(view);
            }
        }, constraints, this, 0);
        m_periodPanel = createFlagPanelRow("PG", new IFlagPanel() {

            public double getNdxVal(int ndx) {
                return m_indexesTableModel.getPeriodGraphFlag(ndx);
            }

            public void setView(VariationGraphView view) {
                m_variation.setPeriodGraphFlagView(view);
            }
        }, constraints, this, 1);
        m_intervalPanel = createFlagPanelRow("IG", new IFlagPanel() {

            public double getNdxVal(int ndx) {
                return m_indexesTableModel.getIntervalGraphFlag(ndx);
            }

            public void setView(VariationGraphView view) {
                m_variation.setIntervalGraphFlagView(view);
            }
        }, constraints, this, 2);
        m_durationPanel = createFlagPanelRow("DG", new IFlagPanel() {

            public double getNdxVal(int ndx) {
                return m_indexesTableModel.getDurationGraphFlag(ndx);
            }

            public void setView(VariationGraphView view) {
                m_variation.setDurationGraphFlagView(view);
            }
        }, constraints, this, 3);
        m_initialIntervalPanel = createFlagPanelRow("II", new IFlagPanel() {

            public double getNdxVal(int ndx) {
                return m_indexesTableModel.getInitialIntervalFlag(ndx);
            }

            public void setView(VariationGraphView view) {
                m_variation.setInitialIntervalFlagView(view);
            }
        }, constraints, this, 4);
        m_zeroPanel = createFlagPanelRow("Z", new IFlagPanel() {

            public double getNdxVal(int ndx) {
                return m_indexesTableModel.getZeroFlag(ndx);
            }

            public void setView(VariationGraphView view) {
                m_variation.setZeroFlagView(view);
            }
        }, constraints, this, 5);
        return this;
    }
}
