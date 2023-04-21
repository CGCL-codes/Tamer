package de.jmulti.tools;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import com.jstatcom.component.TopFrameReference;
import com.jstatcom.model.JSCNArray;
import com.jstatcom.model.JSCSArray;

/**
 * Configuration dialog for barplot settings.
 * 
 * @author <a href="mailto:mk@mk-home.de">Markus Kraetzig </a>
 */
public final class FEVDBarConfigDialog extends JDialog {

    private static class ComboBoxEditor extends DefaultCellEditor {

        public ComboBoxEditor(String[] items) {
            super(new JComboBox(items));
        }
    }

    private JSCSArray currentNames = null;

    private JPanel jPanel = null;

    private JPanel jPanel1 = null;

    private JPanel jPanel2 = null;

    private JButton okButton = null;

    private JButton cancelButton = null;

    private JButton defaultButton = null;

    private JTable jTable = null;

    private JScrollPane jScrollPane = null;

    /**
     * This method initializes
     *  
     */
    public FEVDBarConfigDialog() {
        super(TopFrameReference.getTopFrameRef());
        initialize();
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        this.setModal(true);
        this.setContentPane(getJPanel());
        this.setTitle("Configure Bar Plot Settings");
        this.setSize(500, 300);
    }

    /**
     * This method initializes jPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel() {
        if (jPanel == null) {
            jPanel = new JPanel();
            jPanel.setLayout(new BorderLayout());
            jPanel.add(getJPanel1(), java.awt.BorderLayout.CENTER);
            jPanel.add(getJPanel2(), java.awt.BorderLayout.SOUTH);
        }
        return jPanel;
    }

    /**
     * This method initializes jPanel1
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel1() {
        if (jPanel1 == null) {
            jPanel1 = new JPanel();
            jPanel1.setLayout(new BoxLayout(jPanel1, BoxLayout.X_AXIS));
            jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
            jPanel1.setEnabled(false);
            jPanel1.add(getJScrollPane(), null);
        }
        return jPanel1;
    }

    /**
     * This method initializes jPanel2
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel2() {
        if (jPanel2 == null) {
            FlowLayout flowLayout2 = new FlowLayout();
            jPanel2 = new JPanel();
            jPanel2.setLayout(flowLayout2);
            jPanel2.setPreferredSize(new java.awt.Dimension(10, 50));
            flowLayout2.setVgap(10);
            flowLayout2.setHgap(50);
            jPanel2.add(getOkButton(), null);
            jPanel2.add(getCancelButton(), null);
            jPanel2.add(getDefaultButton(), null);
        }
        return jPanel2;
    }

    /**
     * This method initializes okButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getOkButton() {
        if (okButton == null) {
            okButton = new JButton();
            okButton.setText("OK");
            okButton.setPreferredSize(new java.awt.Dimension(100, 26));
            okButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    setVisible(false);
                }
            });
        }
        return okButton;
    }

    /**
     * This method initializes jTable
     * 
     * @return javax.swing.JTable
     */
    private JTable getJTable() {
        if (jTable == null) {
            jTable = new JTable();
            jTable.setRowSelectionAllowed(false);
            jTable.setCellSelectionEnabled(false);
            jTable.setRowHeight(25);
            jTable.setColumnSelectionAllowed(false);
            int gapWidth = 4;
            int gapHeight = 4;
            jTable.setIntercellSpacing(new Dimension(gapWidth, gapHeight));
            jTable.setShowGrid(false);
        }
        return jTable;
    }

    /**
     * This method initializes jScrollPane
     * 
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setViewportView(getJTable());
        }
        return jScrollPane;
    }

    /**
     * This method initializes cancelButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getCancelButton() {
        if (cancelButton == null) {
            cancelButton = new JButton();
            cancelButton.setText("Cancel");
            cancelButton.setPreferredSize(new java.awt.Dimension(100, 26));
            cancelButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    setVisible(false);
                }
            });
        }
        return cancelButton;
    }

    /**
     * This method initializes cancelButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getDefaultButton() {
        if (defaultButton == null) {
            defaultButton = new JButton();
            defaultButton.setText("Defaults");
            defaultButton.setPreferredSize(new java.awt.Dimension(100, 26));
            defaultButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    setVariableNames(currentNames);
                }
            });
        }
        return defaultButton;
    }

    /**
     * Gets the selected bar shadings and barcolors. 
     * The order of the rows is reversed because the last
     * bar is plotted first.
     * 
     * @return n x 2 array, 1st column shades, 2nd colors
     */
    JSCNArray getSelectedBarTypes() {
        double[][] selBarTypes = new double[getJTable().getRowCount()][2];
        for (int i = 0; i < selBarTypes.length; i++) {
            String shading = getJTable().getModel().getValueAt(i, 1).toString();
            String color = getJTable().getModel().getValueAt(i, 2).toString();
            int shadingIndex = PlotControlDialog.SHADING_LIST.lastIndexOf(shading);
            int colorIndex = PlotControlDialog.COLOR_LIST.lastIndexOf(color);
            selBarTypes[selBarTypes.length - 1 - i][0] = shadingIndex;
            selBarTypes[selBarTypes.length - 1 - i][1] = colorIndex;
        }
        return new JSCNArray("selBarType", selBarTypes);
    }

    /**
     * Gets the currently used variable names.
     * 
     * @return JSCSArray
     */
    JSCSArray getVariableNames() {
        return currentNames;
    }

    /**
     * Sets the variable names eligible for selection.
     * 
     * @param names
     */
    void setVariableNames(JSCSArray names) {
        DefaultTableModel model = new DefaultTableModel() {

            public boolean isCellEditable(int row, int col) {
                return col > 0;
            }
        };
        currentNames = names;
        getJTable().setModel(model);
        if (!names.isEmpty()) {
            model.addColumn("Variable Names", names.getCol(0));
            String[] selShading = new String[names.rows()];
            for (int i = 0; i < selShading.length; i++) selShading[i] = PlotControlDialog.GAUSS_SHADINGS[6];
            String[] selColor = new String[names.rows()];
            for (int i = 0; i < selColor.length; i++) selColor[i] = PlotControlDialog.GAUSS_COLORS[(i + 9) % PlotControlDialog.GAUSS_COLORS.length];
            model.addColumn("Bar Shading", selShading);
            model.addColumn("Bar Color", selColor);
            TableColumn col = getJTable().getColumnModel().getColumn(1);
            col.setCellEditor(new ComboBoxEditor(PlotControlDialog.GAUSS_SHADINGS));
            col = getJTable().getColumnModel().getColumn(2);
            col.setCellEditor(new ComboBoxEditor(PlotControlDialog.GAUSS_COLORS));
            col.setWidth(100);
        }
    }
}
