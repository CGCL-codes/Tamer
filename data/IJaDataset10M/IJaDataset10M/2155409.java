package org.statcato.dialogs.graph;

import org.statcato.graph.StatcatoChartFrame;
import org.statcato.*;
import org.statcato.spreadsheet.*;
import org.statcato.utils.HelperFunctions;
import org.statcato.graph.GraphFactory;
import org.statcato.utils.NumDataset;
import javax.swing.ButtonGroup;
import java.util.*;
import org.jfree.chart.JFreeChart;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;

/**
 * A dialog for creating a box plot.  
 * Allows the user to specify graph variables containing data 
 * values and optionally a group variable.  Provides the options
 * of providing a plot title and showing the legend.  The box plot
 * is generated using JFreeChart. 
 * 
 * @author  Margaret Yau
 * @version %I%, %G%
 * @see org.jfree.chart
 * @since 1.0
 */
public class BoxPlotDialog extends StatcatoDialog {

    /** Creates new form BoxPlotDialog */
    public BoxPlotDialog(java.awt.Frame parent, boolean modal, Statcato app) {
        super(parent, modal);
        this.app = app;
        ParentSpreadsheet = app.getSpreadsheet();
        initComponents();
        ButtonGroup group = new ButtonGroup();
        group.add(HorizontalRadioButton);
        group.add(VerticalRadioButton);
        ParentSpreadsheet.populateColumnsList(InputVarList);
        ParentSpreadsheet.populateComboBox(GroupByComboBox);
        getRootPane().setDefaultButton(OKButton);
        setHelpFile("graph-boxplot");
        name = "Box Plot";
        description = "For creating a box plot showing five-number " + "summaries of a group of data values.";
        helpStrings.add("Select the column variable(s) containing data " + "values for which the plot will be created. " + "A separate box is used with each column variable.");
        helpStrings.add("Enter the plot title and labels for the x and y axes.");
        helpStrings.add("Select the orientation of the plot (horizontal or vertical)." + "  Select whether to show a legend indicating the different groups.");
        pack();
    }

    /**
     * Updates elements on the dialog so that they have the most
     * current values.  Called by {@link #setVisible} to make
     * sure the dialog displays current values when made visible again.
     * 
     * @see #setVisible(boolean)
     */
    @Override
    public void updateElements() {
        updateComboBox(GroupByComboBox);
        updateColumnsList(InputVarList);
    }

    private void initComponents() {
        OKButton = new javax.swing.JButton();
        CancelButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        InputVarList = new javax.swing.JList();
        GroupByComboBox = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        LegendCheckBox = new javax.swing.JCheckBox();
        TitleTextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        HorizontalRadioButton = new javax.swing.JRadioButton();
        VerticalRadioButton = new javax.swing.JRadioButton();
        jLabel6 = new javax.swing.JLabel();
        xTextField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        yTextField = new javax.swing.JTextField();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Box Plot");
        setMinimumSize(new java.awt.Dimension(200, 350));
        OKButton.setText("OK");
        OKButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OKButtonActionPerformed(evt);
            }
        });
        CancelButton.setText("Cancel");
        CancelButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelButtonActionPerformed(evt);
            }
        });
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Graph Variables"));
        jLabel4.setText("Grouped By Categories in: [optional]");
        jLabel2.setText("Ctrl-click to select multiple variables");
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel1.setText("Graph Variables:");
        jScrollPane1.setViewportView(InputVarList);
        GroupByComboBox.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                GroupByComboBoxItemStateChanged(evt);
            }
        });
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jLabel1).addComponent(jLabel2).addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(GroupByComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap(39, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(jLabel4).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(GroupByComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Graph Options"));
        jLabel3.setText("Plot Title:");
        LegendCheckBox.setText("Show Legend");
        TitleTextField.setText("Box Plot");
        jLabel5.setText("Orientation:");
        HorizontalRadioButton.setText("Horizontal");
        VerticalRadioButton.setSelected(true);
        VerticalRadioButton.setText("Vertical");
        jLabel6.setText("X-axis Label:");
        jLabel7.setText("Y-axis Label:");
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(VerticalRadioButton).addComponent(HorizontalRadioButton).addGroup(jPanel2Layout.createSequentialGroup().addComponent(jLabel3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(TitleTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(jPanel2Layout.createSequentialGroup().addComponent(jLabel6).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(xTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)).addGroup(jPanel2Layout.createSequentialGroup().addComponent(jLabel7).addGap(10, 10, 10).addComponent(yTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)).addComponent(jLabel5).addComponent(LegendCheckBox)).addContainerGap()));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap(39, Short.MAX_VALUE).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel3).addComponent(TitleTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel6).addComponent(xTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel7).addComponent(yTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addComponent(jLabel5).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(HorizontalRadioButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(VerticalRadioButton).addGap(14, 14, 14).addComponent(LegendCheckBox).addContainerGap()));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addGroup(layout.createSequentialGroup().addGap(220, 220, 220).addComponent(OKButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(CancelButton))).addContainerGap()));
        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] { CancelButton, OKButton });
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(OKButton).addComponent(CancelButton)).addGap(23, 23, 23)));
        pack();
    }

    private void OKButtonActionPerformed(java.awt.event.ActionEvent evt) {
        app.compoundEdit = new DialogEdit("box plot");
        Object[] SelectedColumns = InputVarList.getSelectedValues();
        int[] columnNumbers = ParentSpreadsheet.convertColumnLabelsToNumbers(SelectedColumns);
        if (columnNumbers.length == 0) {
            app.showErrorDialog("Select at least one input variable.");
            return;
        }
        String text = "";
        String byVarLabel = GroupByComboBox.getSelectedItem().toString();
        int byVarColNum = -1;
        Vector<Cell> byVarColVector = null;
        boolean hasByVar = false;
        if (!byVarLabel.equals("")) {
            text += "Group By variable: " + byVarLabel + "<br>";
            hasByVar = true;
            byVarColNum = ParentSpreadsheet.parseColumnNumber(byVarLabel);
            byVarColVector = ParentSpreadsheet.getColumn(byVarColNum);
            byVarColVector = HelperFunctions.removeNullCells(byVarColVector);
        }
        text += "Graph variables: ";
        NumDataset dataset = new NumDataset();
        text += "Graph variables: ";
        for (int i = 0; i < columnNumbers.length; ++i) {
            Vector<Cell> StrColumnVector = ParentSpreadsheet.getColumn(columnNumbers[i]);
            String columnLabel = (String) SelectedColumns[i];
            text += columnLabel + " ";
            Vector<Double> ColumnVector = HelperFunctions.ConvertInputVectorToDoubles(StrColumnVector);
            ColumnVector = HelperFunctions.removeNullValues(ColumnVector);
            if (ColumnVector == null) {
                app.showErrorDialog("Invalid input column " + columnLabel + ": all data must be numbers.");
                return;
            }
            if (hasByVar) {
                if (ColumnVector.size() != byVarColVector.size()) {
                    app.showErrorDialog("There must be one category label for " + "each data value.");
                    return;
                }
                dataset.addDataVectorWithByVarLabels(ColumnVector, byVarColVector);
            } else dataset.add(ColumnVector, columnLabel);
        }
        DefaultBoxAndWhiskerCategoryDataset boxDataset = new DefaultBoxAndWhiskerCategoryDataset();
        Vector<Vector<Double>> data = dataset.getValues();
        Vector<String> labels = dataset.getLabels();
        for (int i = 0; i < data.size(); ++i) {
            boxDataset.add(data.elementAt(i), labels.elementAt(i), "");
        }
        JFreeChart chart = GraphFactory.createBoxPlot(boxDataset, TitleTextField.getText(), xTextField.getText(), yTextField.getText(), LegendCheckBox.isSelected(), HorizontalRadioButton.isSelected());
        app.addLogParagraph("box plot", text);
        app.compoundEdit.end();
        app.addCompoundEdit(app.compoundEdit);
        StatcatoChartFrame frame = new StatcatoChartFrame(TitleTextField.getText(), chart, app);
        frame.pack();
        frame.setVisible(true);
        setVisible(false);
    }

    private void CancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        setVisible(false);
    }

    /**
     * Selects or deselects the show legend check box depending on the state
     * of the GroupByComboBox.
     * <p>
     * If GroupByComboBox has a selected item, selects the legend checkbox.
     * Otherwise, deselects the legend checkbox.
     * 
     * @param evt
     */
    private void GroupByComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {
        if (GroupByComboBox.getSelectedItem() != null) {
            if (GroupByComboBox.getSelectedItem().toString().equals("")) {
                LegendCheckBox.setSelected(false);
            } else {
                LegendCheckBox.setSelected(true);
            }
        }
    }

    private javax.swing.JButton CancelButton;

    private javax.swing.JComboBox GroupByComboBox;

    private javax.swing.JRadioButton HorizontalRadioButton;

    private javax.swing.JList InputVarList;

    private javax.swing.JCheckBox LegendCheckBox;

    private javax.swing.JButton OKButton;

    private javax.swing.JTextField TitleTextField;

    private javax.swing.JRadioButton VerticalRadioButton;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JTextField xTextField;

    private javax.swing.JTextField yTextField;
}
