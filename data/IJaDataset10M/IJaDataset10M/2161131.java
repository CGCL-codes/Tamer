package org.statcato.dialogs.stat.basicstats;

import org.statcato.utils.HelperFunctions;
import org.statcato.spreadsheet.*;
import org.statcato.statistics.BasicStatistics;
import org.statcato.*;
import java.util.*;
import javax.swing.*;

/**
 * A dialog for computing descriptive statistics for a column of data values.
 * Statistics include sum, mean, standard deviation, minimum, maximum,
 * range, median, sum of squares, N total, N nonmissing, and N missing.
 * 
 * @author  Margaret Yau
 * @version %I%, %G%
 * @see org.statcato.statistics.BasicStatistics
 * @since 1.0
 */
public class ColumnStatisticsDialog extends StatcatoDialog {

    /** Creates new form ColumnStatisticsDialog */
    public ColumnStatisticsDialog(java.awt.Frame parent, boolean modal, Statcato app) {
        super(parent, modal);
        ParentSpreadsheet = app.getSpreadsheet();
        this.app = app;
        initComponents();
        ParentSpreadsheet.populateComboBox(InputVarComboBox);
        ButtonGroup group = new ButtonGroup();
        group.add(ColumnRadioButton);
        group.add(RowRadioButton);
        ColumnRadioButton.setSelected(false);
        RowRadioButton.setSelected(false);
        getRootPane().setDefaultButton(OKButton);
        setHelpFile("stat-basic-column");
        name = "Column Statistics";
        description = "For computing various statistics on the data within a " + "specific column in a Datasheet. " + "(The computed statistics are displayed in the log window and " + "can be optionally stored in the datasheet.)";
        helpStrings.add("Select the input column for which you want to " + "calculate statistics.");
        helpStrings.add("[optional] If you want to store the computed statistics " + "in a column of the Datasheet, select the Column radio button " + "and enter the column number or the variable name.");
        helpStrings.add("[optional] If you want to store the computed statistics " + "in a row of the datasheet, select the Row radio button and " + "enter the row number.");
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
        updateComboBox(InputVarComboBox);
    }

    private void initComponents() {
        OKButton = new javax.swing.JButton();
        CancelButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        InputVarComboBox = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        NNonmissingCheckBox = new javax.swing.JCheckBox();
        NMissingCheckBox = new javax.swing.JCheckBox();
        SumOfSqCheckBox = new javax.swing.JCheckBox();
        NTotalCheckBox = new javax.swing.JCheckBox();
        SumCheckBox = new javax.swing.JCheckBox();
        MeanCheckBox = new javax.swing.JCheckBox();
        RangeCheckBox = new javax.swing.JCheckBox();
        MaxCheckBox = new javax.swing.JCheckBox();
        MedianCheckBox = new javax.swing.JCheckBox();
        MinCheckBox = new javax.swing.JCheckBox();
        StdevCheckBox = new javax.swing.JCheckBox();
        SelectAllCheckBox = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        RowTextField = new javax.swing.JTextField();
        ColumnRadioButton = new javax.swing.JRadioButton();
        jLabel4 = new javax.swing.JLabel();
        ColumnTextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        RowRadioButton = new javax.swing.JRadioButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Column Statistics");
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
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Input"));
        jLabel1.setText("Column Input Variable:");
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(InputVarComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(140, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(InputVarComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap()));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Statistics"));
        NNonmissingCheckBox.setText("N nonmissing");
        NMissingCheckBox.setText("N missing");
        SumOfSqCheckBox.setText("Sum of squares");
        NTotalCheckBox.setText("N total");
        SumCheckBox.setText("Sum");
        MeanCheckBox.setText("Mean");
        RangeCheckBox.setText("Range");
        MaxCheckBox.setText("Maximum");
        MedianCheckBox.setText("Median");
        MinCheckBox.setText("Minimum");
        StdevCheckBox.setText("Standard deviation");
        SelectAllCheckBox.setText("Select All");
        SelectAllCheckBox.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                SelectAllCheckBoxStateChanged(evt);
            }
        });
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(SumCheckBox).addComponent(MeanCheckBox).addComponent(StdevCheckBox).addComponent(MinCheckBox).addComponent(MaxCheckBox)).addGap(57, 57, 57).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(NMissingCheckBox).addComponent(NNonmissingCheckBox).addComponent(NTotalCheckBox).addComponent(SumOfSqCheckBox).addComponent(MedianCheckBox))).addComponent(RangeCheckBox).addComponent(SelectAllCheckBox)).addContainerGap(71, Short.MAX_VALUE)));
        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] { MaxCheckBox, MeanCheckBox, MedianCheckBox, MinCheckBox, NMissingCheckBox, NNonmissingCheckBox, NTotalCheckBox, RangeCheckBox, StdevCheckBox, SumCheckBox, SumOfSqCheckBox });
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addComponent(SelectAllCheckBox).addGap(7, 7, 7).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(SumCheckBox).addComponent(MedianCheckBox)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(MeanCheckBox).addComponent(SumOfSqCheckBox)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(StdevCheckBox).addComponent(NTotalCheckBox)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(MinCheckBox).addComponent(NNonmissingCheckBox)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(MaxCheckBox).addComponent(NMissingCheckBox)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(RangeCheckBox).addContainerGap()));
        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] { MaxCheckBox, MeanCheckBox, MedianCheckBox, MinCheckBox, NMissingCheckBox, NNonmissingCheckBox, NTotalCheckBox, RangeCheckBox, StdevCheckBox, SumCheckBox, SumOfSqCheckBox });
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Results"));
        jLabel3.setText("Store Results in:");
        jLabel6.setText("e.g. C1 or variable name");
        ColumnRadioButton.setText("Column");
        jLabel4.setText("[optional]");
        jLabel5.setText("e.g. 11");
        RowRadioButton.setText("Row");
        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel4).addGroup(jPanel3Layout.createSequentialGroup().addComponent(jLabel3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(ColumnRadioButton).addComponent(RowRadioButton)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(RowTextField).addComponent(ColumnTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel6).addComponent(jLabel5)))).addContainerGap()));
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addComponent(jLabel4).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel3).addComponent(ColumnRadioButton).addComponent(ColumnTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel6)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(RowRadioButton).addComponent(RowTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel5)).addContainerGap()));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGap(21, 21, 21)).addGroup(layout.createSequentialGroup().addGap(123, 123, 123).addComponent(OKButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(CancelButton).addContainerGap(148, Short.MAX_VALUE)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addGap(21, 21, 21)));
        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] { CancelButton, OKButton });
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(OKButton).addComponent(CancelButton)).addContainerGap()));
        pack();
    }

    private void CancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        setVisible(false);
    }

    @SuppressWarnings("unchecked")
    private void OKButtonActionPerformed(java.awt.event.ActionEvent evt) {
        app.compoundEdit = new DialogEdit("column statistics");
        boolean storeColumnResult = false;
        boolean storeRowResult = false;
        int storeColumnNum = -1;
        int storeRowNum = -1;
        String columnLabel = InputVarComboBox.getSelectedItem().toString();
        if (columnLabel.equals("")) {
            app.showErrorDialog("Select the input variable.");
            return;
        }
        if (ColumnRadioButton.isSelected()) {
            String StoreColumn = ColumnTextField.getText();
            if (!StoreColumn.equals("")) {
                storeColumnResult = true;
                storeColumnNum = ParentSpreadsheet.getColumnNumber(StoreColumn);
                if (storeColumnNum == -1) {
                    app.showErrorDialog("Enter a valid column name (e.g. C1) or a valid variable name to store the results.");
                    return;
                }
            }
        } else if (RowRadioButton.isSelected()) {
            String StoreRow = RowTextField.getText();
            if (!StoreRow.equals("")) {
                storeRowResult = true;
                storeRowNum = ParentSpreadsheet.getRowNumber(StoreRow);
                if (storeRowNum == -1) {
                    app.showErrorDialog("Enter a valid row number.");
                    return;
                }
            }
        }
        int column = ParentSpreadsheet.parseColumnNumber(columnLabel);
        Vector<Cell> StrColumnVector = ParentSpreadsheet.getColumn(column);
        Object[] conversions = HelperFunctions.ConvertInputVectorToNumbers(StrColumnVector);
        if (conversions == null) {
            app.showErrorDialog("Invalid input column: all data must be numbers.");
            return;
        } else {
            Vector ColumnVector = (Vector) conversions[0];
            int missing = ((Integer) conversions[1]).intValue();
            int nonmissing = ((Integer) conversions[2]).intValue();
            int total = missing + nonmissing;
            Vector<String> ResultVector = new Vector<String>(0);
            app.addLogHeading("Column Statistics");
            if (MeanCheckBox.isSelected()) {
                Double mean = BasicStatistics.mean(ColumnVector);
                String m;
                if (mean == null) m = "*"; else m = HelperFunctions.formatFloat(mean.doubleValue());
                ResultVector.addElement(m);
                app.addLogHeading("Mean of " + columnLabel);
                app.addLogText("Mean of " + columnLabel + " = " + m);
            }
            if (SumCheckBox.isSelected()) {
                double sum = BasicStatistics.sum(ColumnVector);
                ResultVector.addElement(sum + "");
                app.addLogHeading("Sum of " + columnLabel);
                app.addLogText("Sum of " + columnLabel + " = " + HelperFunctions.formatFloat(sum));
            }
            if (StdevCheckBox.isSelected()) {
                Double stdev = BasicStatistics.stdev(ColumnVector);
                String s;
                if (stdev == null) s = "*"; else s = HelperFunctions.formatFloat(stdev.doubleValue());
                ResultVector.addElement(s);
                app.addLogHeading("<b>Standard Deviation of " + columnLabel);
                app.addLogText("Standard deviation of " + columnLabel + " = " + s);
            }
            if (MaxCheckBox.isSelected()) {
                Double max = BasicStatistics.max(ColumnVector);
                String m;
                if (max == null) m = "*"; else m = HelperFunctions.formatFloat(max.doubleValue());
                ResultVector.addElement(m);
                app.addLogHeading("<b>Maximum of " + columnLabel);
                app.addLogText("Maximum of " + columnLabel + " = " + m);
            }
            if (MinCheckBox.isSelected()) {
                Double min = BasicStatistics.min(ColumnVector);
                String m;
                if (min == null) m = "*"; else m = HelperFunctions.formatFloat(min.doubleValue());
                ResultVector.addElement(m);
                app.addLogHeading("Minimum of " + columnLabel);
                app.addLogText("Minimum of " + columnLabel + " = " + m);
            }
            if (RangeCheckBox.isSelected()) {
                Double range = BasicStatistics.range(ColumnVector);
                String r;
                if (range == null) r = "*"; else r = HelperFunctions.formatFloat(range.doubleValue());
                ResultVector.addElement(r);
                app.addLogHeading("Range of " + columnLabel);
                app.addLogText("Range of " + columnLabel + " = " + r);
            }
            if (MedianCheckBox.isSelected()) {
                Double median = BasicStatistics.median(ColumnVector);
                String m;
                if (median == null) m = "*"; else m = HelperFunctions.formatFloat(median.doubleValue());
                ResultVector.addElement(m);
                app.addLogHeading("Median of " + columnLabel);
                app.addLogText("Median of " + columnLabel + " = " + m);
            }
            if (SumOfSqCheckBox.isSelected()) {
                double sum = BasicStatistics.sumOfSquares(ColumnVector);
                ResultVector.addElement(sum + "");
                app.addLogHeading("Sum of Squares of " + columnLabel);
                app.addLogText("Sum of squares of " + columnLabel + " = " + sum);
            }
            if (NTotalCheckBox.isSelected()) {
                ResultVector.addElement(total + "");
                app.addLogHeading("Total Number of Observations in " + columnLabel);
                app.addLogText("Total number of observations in " + columnLabel + " = " + total);
            }
            if (NNonmissingCheckBox.isSelected()) {
                ResultVector.addElement(nonmissing + "");
                app.addLogHeading("Number of Nonmissings in " + columnLabel);
                app.addLogText("Number of nonmissings in " + columnLabel + " = " + nonmissing);
            }
            if (NMissingCheckBox.isSelected()) {
                ResultVector.addElement(missing + "");
                app.addLogHeading("Number of Missings in " + columnLabel);
                app.addLogText("Number of missings in " + columnLabel + " = " + missing);
            }
            if (storeColumnResult) {
                ParentSpreadsheet.setColumn(storeColumnNum, ResultVector);
            } else if (storeRowResult) {
                ParentSpreadsheet.setRow(storeRowNum, ResultVector);
            }
        }
        app.compoundEdit.end();
        app.addCompoundEdit(app.compoundEdit);
        setVisible(false);
    }

    private void SelectAllCheckBoxStateChanged(javax.swing.event.ChangeEvent evt) {
        if (SelectAllCheckBox.isSelected()) {
            SumCheckBox.setSelected(true);
            MeanCheckBox.setSelected(true);
            StdevCheckBox.setSelected(true);
            MinCheckBox.setSelected(true);
            MaxCheckBox.setSelected(true);
            RangeCheckBox.setSelected(true);
            MedianCheckBox.setSelected(true);
            SumOfSqCheckBox.setSelected(true);
            NTotalCheckBox.setSelected(true);
            NNonmissingCheckBox.setSelected(true);
            NMissingCheckBox.setSelected(true);
        } else {
            SumCheckBox.setSelected(false);
            MeanCheckBox.setSelected(false);
            StdevCheckBox.setSelected(false);
            MinCheckBox.setSelected(false);
            MaxCheckBox.setSelected(false);
            RangeCheckBox.setSelected(false);
            MedianCheckBox.setSelected(false);
            SumOfSqCheckBox.setSelected(false);
            NTotalCheckBox.setSelected(false);
            NNonmissingCheckBox.setSelected(false);
            NMissingCheckBox.setSelected(false);
        }
    }

    private javax.swing.JButton CancelButton;

    private javax.swing.JRadioButton ColumnRadioButton;

    private javax.swing.JTextField ColumnTextField;

    private javax.swing.JComboBox InputVarComboBox;

    private javax.swing.JCheckBox MaxCheckBox;

    private javax.swing.JCheckBox MeanCheckBox;

    private javax.swing.JCheckBox MedianCheckBox;

    private javax.swing.JCheckBox MinCheckBox;

    private javax.swing.JCheckBox NMissingCheckBox;

    private javax.swing.JCheckBox NNonmissingCheckBox;

    private javax.swing.JCheckBox NTotalCheckBox;

    private javax.swing.JButton OKButton;

    private javax.swing.JCheckBox RangeCheckBox;

    private javax.swing.JRadioButton RowRadioButton;

    private javax.swing.JTextField RowTextField;

    private javax.swing.JCheckBox SelectAllCheckBox;

    private javax.swing.JCheckBox StdevCheckBox;

    private javax.swing.JCheckBox SumCheckBox;

    private javax.swing.JCheckBox SumOfSqCheckBox;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;
}
