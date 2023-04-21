package org.statcato.dialogs.graph;

import org.statcato.graph.StatcatoChartFrame;
import org.statcato.graph.GraphFactory;
import org.statcato.*;
import org.statcato.spreadsheet.*;
import org.statcato.utils.HelperFunctions;
import java.util.*;
import javax.swing.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * A dialog for creating bar charts.  Allows the user to specify the
 * data series to be graphed, direction of bars, axis labels, title,
 * and whether a legend is displayed.
 * 
 * @author  Margaret Yau
 * @version %I%, %G%
 * @see org.jfree.chart
 * @since 1.0
 */
public class BarChartDialog extends StatcatoDialog {

    /** Creates new form BarChartDialog */
    public BarChartDialog(java.awt.Frame parent, boolean modal, Statcato app) {
        super(parent, modal);
        this.app = app;
        ParentSpreadsheet = app.getSpreadsheet();
        initComponents();
        customInitComponents();
        setHelpFile("graph-barchart");
        name = "Bar Chart";
        description = "For creating horizontal or vertical bar charts that " + "represent the frequences of a number of data series with " + "data in a number of categories.";
        helpStrings.add("To add a data series, select the column containing " + "the data values and click the Add Series button.");
        helpStrings.add("Select the column containing category labels " + "for the data values in each series.");
        helpStrings.add("Select whether to have horizontal or vertical bars " + "under Directions of bars drop-down menu.");
        helpStrings.add("Enter labels for the x and y axis and the plot title " + "in the corresponding text fields.");
        helpStrings.add("Select the Show Legend check box to show a " + "legend indicating the different groups.");
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
        updateComboBox(XComboBox);
        updateComboBox(CatComboBox);
        clearMutableColumnsList(SeriesList);
    }

    private void customInitComponents() {
        ParentSpreadsheet.populateComboBox(XComboBox);
        ParentSpreadsheet.populateComboBox(CatComboBox);
        SeriesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ButtonGroup group = new ButtonGroup();
        group.add(HorizontalRadioButton);
        group.add(VerticalRadioButton);
        getRootPane().setDefaultButton(OKButton);
    }

    private void initComponents() {
        OKButton = new javax.swing.JButton();
        CancelButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        CatComboBox = new javax.swing.JComboBox();
        XComboBox = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        AddButton = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        RemoveButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        SeriesList = new JList(new DefaultListModel());
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        VerticalRadioButton = new javax.swing.JRadioButton();
        HorizontalRadioButton = new javax.swing.JRadioButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        YTextField = new javax.swing.JTextField();
        LegendCheckBox = new javax.swing.JCheckBox();
        XTextField = new javax.swing.JTextField();
        TitleTextField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Bar Chart");
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
        XComboBox.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                XComboBoxItemStateChanged(evt);
            }
        });
        jLabel4.setText("Select the column variable containing categories:");
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel1.setText("Graph Series");
        AddButton.setText("Add Series");
        AddButton.setEnabled(false);
        AddButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddButtonActionPerformed(evt);
            }
        });
        jLabel8.setText("Select the series to be removed:");
        RemoveButton.setText("Remove Series");
        RemoveButton.setEnabled(false);
        RemoveButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RemoveButtonActionPerformed(evt);
            }
        });
        SeriesList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                SeriesListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(SeriesList);
        jLabel2.setText("Select the column variable of a new series:");
        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel3.setText("Categories");
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel1).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addComponent(XComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(AddButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)).addComponent(jLabel2).addComponent(jLabel8).addComponent(RemoveButton))).addComponent(jLabel3).addGroup(jPanel1Layout.createSequentialGroup().addGap(10, 10, 10).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(CatComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel4)))).addGap(3, 3, 3)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(XComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(AddButton)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jLabel8).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(RemoveButton)).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(26, 26, 26).addComponent(jLabel3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel4).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(CatComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Direction of Bars"));
        VerticalRadioButton.setSelected(true);
        VerticalRadioButton.setText("Vertical");
        HorizontalRadioButton.setText("Horizontal");
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(HorizontalRadioButton).addComponent(VerticalRadioButton)).addContainerGap(257, Short.MAX_VALUE)));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGap(9, 9, 9).addComponent(HorizontalRadioButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(VerticalRadioButton).addGap(47, 47, 47)));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Graph Options"));
        jLabel7.setText("X-axis Label:");
        LegendCheckBox.setSelected(true);
        LegendCheckBox.setText("Show Legend");
        TitleTextField.setText("Bar Chart");
        jLabel6.setText("Plot Title:");
        jLabel5.setText("Y-axis Label:");
        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel5).addComponent(jLabel6)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(XTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(YTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(TitleTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))).addComponent(jLabel7).addComponent(LegendCheckBox)).addContainerGap()));
        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] { TitleTextField, XTextField, YTextField });
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel7).addComponent(XTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel5).addComponent(YTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel6).addComponent(TitleTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE).addComponent(LegendCheckBox)));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addGroup(layout.createSequentialGroup().addGap(285, 285, 285).addComponent(OKButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(CancelButton))).addContainerGap()));
        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] { CancelButton, OKButton });
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(layout.createSequentialGroup().addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))).addGap(26, 26, 26).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(OKButton).addComponent(CancelButton)).addGap(22, 22, 22)));
        pack();
    }

    private void SeriesListValueChanged(javax.swing.event.ListSelectionEvent evt) {
        if (SeriesList.getSelectedIndex() != -1) RemoveButton.setEnabled(true); else RemoveButton.setEnabled(false);
    }

    private void RemoveButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (SeriesList.getSelectedValue() != null) {
            ((DefaultListModel) SeriesList.getModel()).removeElementAt(SeriesList.getSelectedIndex());
        }
    }

    private void OKButtonActionPerformed(java.awt.event.ActionEvent evt) {
        app.compoundEdit = new DialogEdit("bar chart");
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String text = "";
        if (CatComboBox.getSelectedIndex() == 0) {
            app.showErrorDialog("Select the column containing the categories.");
            return;
        }
        if (SeriesList.getModel().getSize() == 0) {
            app.showErrorDialog("Select at least one graph variable.");
            return;
        }
        int catColumn = ParentSpreadsheet.parseColumnNumber((String) CatComboBox.getSelectedItem());
        text += "Categories in " + CatComboBox.getSelectedItem() + "<br>";
        Vector<Cell> CatColumnVector = ParentSpreadsheet.getColumn(catColumn);
        CatColumnVector = HelperFunctions.removeNullCells(CatColumnVector);
        text += "Graph variables: ";
        for (int i = 0; i < SeriesList.getModel().getSize(); ++i) {
            String x = (String) SeriesList.getModel().getElementAt(i);
            text += x + " ";
            int selectedXColumn = ParentSpreadsheet.parseColumnNumber(x);
            Vector<Cell> StrColumnVector = ParentSpreadsheet.getColumn(selectedXColumn);
            Vector<Double> XColumnVector = HelperFunctions.ConvertInputVectorToDoubles(StrColumnVector);
            if (XColumnVector == null) {
                app.showErrorDialog("Invalid input column " + x + ": all data must be numbers.");
                return;
            }
            XColumnVector = HelperFunctions.removeNullValues(XColumnVector);
            if (XColumnVector.size() != CatColumnVector.size()) {
                app.showErrorDialog("The number of values must be the " + "same as the number of categories.");
                return;
            }
            for (int j = 0; j < XColumnVector.size(); ++j) {
                dataset.addValue(XColumnVector.elementAt(j).doubleValue(), x, CatColumnVector.elementAt(j).getContents());
            }
        }
        PlotOrientation orientation;
        if (HorizontalRadioButton.isSelected()) orientation = PlotOrientation.HORIZONTAL; else orientation = PlotOrientation.VERTICAL;
        app.addLogParagraph("Bar Chart", text);
        app.compoundEdit.end();
        app.addCompoundEdit(app.compoundEdit);
        StatcatoChartFrame frame = new StatcatoChartFrame(TitleTextField.getText(), GraphFactory.createBarChart(TitleTextField.getText(), XTextField.getText(), YTextField.getText(), dataset, orientation, LegendCheckBox.isSelected()), app);
        frame.pack();
        frame.setVisible(true);
        setVisible(false);
    }

    private void CancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        setVisible(false);
    }

    private void XComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {
        if (XComboBox.getSelectedIndex() == 0) AddButton.setEnabled(false); else AddButton.setEnabled(true);
    }

    private void AddButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (XComboBox.getSelectedIndex() == 0) {
            app.showErrorDialog("Select the column variable of the new series.");
            return;
        }
        String x = XComboBox.getSelectedItem().toString();
        ((DefaultListModel) SeriesList.getModel()).addElement(x);
    }

    private javax.swing.JButton AddButton;

    private javax.swing.JButton CancelButton;

    private javax.swing.JComboBox CatComboBox;

    private javax.swing.JRadioButton HorizontalRadioButton;

    private javax.swing.JCheckBox LegendCheckBox;

    private javax.swing.JButton OKButton;

    private javax.swing.JButton RemoveButton;

    private javax.swing.JList SeriesList;

    private javax.swing.JTextField TitleTextField;

    private javax.swing.JRadioButton VerticalRadioButton;

    private javax.swing.JComboBox XComboBox;

    private javax.swing.JTextField XTextField;

    private javax.swing.JTextField YTextField;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JScrollPane jScrollPane1;
}
