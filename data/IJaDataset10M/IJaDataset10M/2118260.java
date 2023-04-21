package de.jmulti.arima;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import com.jstatcom.component.NumSelector;
import com.jstatcom.component.ResultField;
import com.jstatcom.component.StdMessages;
import com.jstatcom.engine.PCall;
import com.jstatcom.model.JSCInt;
import com.jstatcom.model.JSCNArray;
import com.jstatcom.model.JSCSArray;
import com.jstatcom.model.ModelPanel;
import com.jstatcom.ts.TS;
import com.jstatcom.ts.TSDateRange;
import com.jstatcom.ts.TSSel;
import com.jstatcom.util.UMatrix;
import de.jmulti.proc.ARIMAHanRissPCall;

/**
 * Variable and lags selection for ARIMA models.
 * 
 * @author <a href="mailto:mk@mk-home.de">Markus Kraetzig </a>
 */
public final class SpecPanel extends ModelPanel {

    private TSSel tSSel = null;

    private JPanel jPanel = null;

    private NumSelector arSelector = null;

    private JLabel jLabel = null;

    private JLabel jLabel1 = null;

    private NumSelector maSelector = null;

    private JComboBox dComboBox = null;

    private JLabel jLabel2 = null;

    private JCheckBox constCheckBox = null;

    private JPanel jPanel1 = null;

    private JPanel jPanel2 = null;

    private JPanel jPanel3 = null;

    private JScrollPane jScrollPane = null;

    private ResultField resultField = null;

    private NumSelector hSelector = null;

    private NumSelector pqmaxSelector = null;

    private JButton okButton = null;

    private JLabel jLabel3 = null;

    private JLabel jLabel4 = null;

    private JCheckBox seasCheckBox = null;

    private JCheckBox trendCheckBox = null;

    private JLabel jLabel5 = null;

    private JLabel jLabel6 = null;

    /**
     * This method initializes
     * 
     */
    public SpecPanel() {
        super();
        setName("SpecPanel");
        initialize();
    }

    /**
     * This method initializes this
     * 
     */
    private void initialize() {
        this.setLayout(new BorderLayout());
        this.setSize(new java.awt.Dimension(756, 488));
        this.add(getTSSel(), java.awt.BorderLayout.EAST);
        this.add(getJPanel1(), java.awt.BorderLayout.CENTER);
        getTSSel().addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equalsIgnoreCase("selectionChanged")) {
                    global().get(ARIMAConstants.EST_RESIDS).clear();
                    global().get(ARIMAConstants.EST_PARAMS).clear();
                }
            }
        });
    }

    /**
     * This method initializes tSSel
     * 
     * @return com.jstatcom.ts.TSSel
     */
    TSSel getTSSel() {
        if (tSSel == null) {
            tSSel = new TSSel();
            tSSel.setOneEndogenousOnly(true);
            tSSel.setEndogenousDataName(ARIMAConstants.END_DATA.name);
            tSSel.setEndogenousStringsName(ARIMAConstants.END_NAME.name);
            tSSel.setExogenousEnabled(false);
            tSSel.setDateRangeName(ARIMAConstants.DRANGE.name);
            tSSel.setDeterministicDataName(ARIMAConstants.DET_DATA_SEL.name);
            tSSel.setDeterministicStringsName(ARIMAConstants.DET_NAMES_SEL.name);
        }
        return tSSel;
    }

    /**
     * This method initializes jPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel() {
        if (jPanel == null) {
            GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
            gridBagConstraints31.gridx = 0;
            gridBagConstraints31.insets = new java.awt.Insets(5, 10, 0, 0);
            gridBagConstraints31.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints31.gridwidth = 2;
            gridBagConstraints31.gridy = 3;
            jLabel5 = new JLabel();
            jLabel5.setText("Fixed regressors, others may be selected as \"deterministic\" with the selector");
            jLabel5.setPreferredSize(new java.awt.Dimension(38, 20));
            GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
            gridBagConstraints21.gridx = 0;
            gridBagConstraints21.gridwidth = 2;
            gridBagConstraints21.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints21.insets = new java.awt.Insets(5, 10, 0, 0);
            gridBagConstraints21.weighty = 1.0D;
            gridBagConstraints21.anchor = java.awt.GridBagConstraints.NORTH;
            gridBagConstraints21.gridy = 6;
            GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
            gridBagConstraints12.gridx = 0;
            gridBagConstraints12.gridwidth = 2;
            gridBagConstraints12.insets = new java.awt.Insets(5, 10, 0, 0);
            gridBagConstraints12.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints12.gridy = 5;
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.insets = new java.awt.Insets(5, 10, 0, 10);
            gridBagConstraints6.gridx = 0;
            gridBagConstraints6.gridy = 4;
            gridBagConstraints6.ipadx = 0;
            gridBagConstraints6.ipady = 0;
            gridBagConstraints6.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints6.weighty = 0.0D;
            gridBagConstraints6.anchor = java.awt.GridBagConstraints.NORTH;
            gridBagConstraints6.gridwidth = 2;
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.insets = new java.awt.Insets(5, 10, 0, 10);
            gridBagConstraints5.gridy = 2;
            gridBagConstraints5.ipadx = 0;
            gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints5.weightx = 1.0D;
            gridBagConstraints5.gridx = 1;
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.fill = java.awt.GridBagConstraints.NONE;
            gridBagConstraints4.gridx = 0;
            gridBagConstraints4.gridy = 2;
            gridBagConstraints4.ipadx = 0;
            gridBagConstraints4.weightx = 0.0D;
            gridBagConstraints4.anchor = java.awt.GridBagConstraints.EAST;
            gridBagConstraints4.insets = new java.awt.Insets(5, 10, 0, 0);
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.fill = java.awt.GridBagConstraints.NONE;
            gridBagConstraints3.gridx = 0;
            gridBagConstraints3.gridy = 1;
            gridBagConstraints3.ipadx = 0;
            gridBagConstraints3.weightx = 0.0D;
            gridBagConstraints3.anchor = java.awt.GridBagConstraints.EAST;
            gridBagConstraints3.insets = new java.awt.Insets(5, 10, 0, 0);
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.insets = new java.awt.Insets(5, 10, 0, 0);
            gridBagConstraints2.gridy = 1;
            gridBagConstraints2.ipadx = 0;
            gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints2.gridx = 1;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.insets = new java.awt.Insets(5, 10, 0, 0);
            gridBagConstraints1.gridy = 0;
            gridBagConstraints1.ipadx = 0;
            gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints1.anchor = java.awt.GridBagConstraints.SOUTH;
            gridBagConstraints1.gridx = 1;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.ipadx = 0;
            gridBagConstraints.weightx = 0.0D;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
            gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 0);
            jLabel2 = new JLabel();
            jLabel2.setText("Order of differencing (d)");
            jLabel2.setPreferredSize(new java.awt.Dimension(136, 25));
            jLabel1 = new JLabel();
            jLabel1.setText("MA lags (q)");
            jLabel1.setPreferredSize(new java.awt.Dimension(63, 20));
            jLabel = new JLabel();
            jLabel.setText("AR lags (p)");
            jLabel.setPreferredSize(new java.awt.Dimension(61, 20));
            jPanel = new JPanel();
            jPanel.setLayout(new GridBagLayout());
            jPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED), "Specify ARIMA", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", java.awt.Font.PLAIN, 10), new java.awt.Color(51, 51, 51)));
            jPanel.setPreferredSize(new java.awt.Dimension(278, 230));
            jPanel.add(getArSelector(), gridBagConstraints);
            jPanel.add(jLabel, gridBagConstraints1);
            jPanel.add(jLabel1, gridBagConstraints2);
            jPanel.add(getMaSelector(), gridBagConstraints3);
            jPanel.add(getDComboBox(), gridBagConstraints4);
            jPanel.add(jLabel2, gridBagConstraints5);
            jPanel.add(getConstCheckBox(), gridBagConstraints6);
            jPanel.add(getSeasCheckBox(), gridBagConstraints12);
            jPanel.add(getTrendCheckBox(), gridBagConstraints21);
            jPanel.add(jLabel5, gridBagConstraints31);
        }
        return jPanel;
    }

    /**
     * This method initializes arSelector
     * 
     * @return com.jstatcom.component.NumSelector
     */
    private NumSelector getArSelector() {
        if (arSelector == null) {
            arSelector = new NumSelector();
            arSelector.setRangeExpr("[0, 1000]");
            arSelector.setSymbolName(ARIMAConstants.P.name);
            arSelector.setPreferredSize(new java.awt.Dimension(100, 20));
            arSelector.setIntType(true);
        }
        return arSelector;
    }

    /**
     * This method initializes maSelector
     * 
     * @return com.jstatcom.component.NumSelector
     */
    private NumSelector getMaSelector() {
        if (maSelector == null) {
            maSelector = new NumSelector();
            maSelector.setRangeExpr("[0, 1000]");
            maSelector.setSymbolName(ARIMAConstants.Q.name);
            maSelector.setPreferredSize(new java.awt.Dimension(100, 20));
            maSelector.setIntType(true);
        }
        return maSelector;
    }

    /**
     * This method initializes dComboBox
     * 
     * @return javax.swing.JComboBox
     */
    JComboBox getDComboBox() {
        if (dComboBox == null) {
            dComboBox = new JComboBox();
            dComboBox.setPreferredSize(new java.awt.Dimension(100, 25));
            dComboBox.addItem(0);
            dComboBox.addItem(1);
            dComboBox.addItem(2);
            dComboBox.setSelectedIndex(0);
        }
        return dComboBox;
    }

    private void assembleDeterministics() {
        global().get(ARIMAConstants.D).setJSCData(new JSCInt("D", dComboBox.getSelectedIndex()));
        JSCNArray selection = new JSCNArray("DET_SEL", new int[] { getConstCheckBox().isSelected() ? 1 : 0, getSeasCheckBox().isSelected() ? 1 : 0, getTrendCheckBox().isSelected() ? 1 : 0 });
        global().get(ARIMAConstants.DET_SEL).setJSCData(selection);
        TSDateRange range = global().get(ARIMAConstants.DRANGE).getJSCDRange().getTSDateRange();
        int rows = range.numOfObs();
        JSCNArray det_data_all = global().get(ARIMAConstants.DET_DATA_ALL).getJSCNArray();
        JSCSArray det_names_all = global().get(ARIMAConstants.DET_NAMES_ALL).getJSCSArray();
        det_data_all.clear();
        det_names_all.clear();
        if (getConstCheckBox().isSelected()) {
            JSCNArray cons = new JSCNArray("const", UMatrix.ones(rows, 1));
            det_data_all.setVal(cons);
            det_names_all.setVal("CONST");
        }
        if (getSeasCheckBox().isSelected() && range.subPeriodicity() > 1) {
            TS[] seasTS = range.createSeasDumTS(false, false);
            JSCNArray seasDum = new JSCNArray("seasDum");
            String[] seasNames = new String[seasTS.length];
            int index = 0;
            for (TS ts : seasTS) {
                seasDum.appendCols(new JSCNArray(ts.name(), ts.values()));
                seasNames[index++] = ts.name();
            }
            det_data_all.appendCols(seasDum);
            det_names_all.appendRows(new JSCSArray("seasNames", seasNames));
        }
        if (getTrendCheckBox().isSelected()) {
            JSCNArray trend = new JSCNArray("const", UMatrix.seqa(1, 1, rows));
            det_data_all.appendCols(trend);
            det_names_all.appendRows(new JSCSArray("trendName", "TREND"));
        }
        JSCNArray det_data_sel = global().get(ARIMAConstants.DET_DATA_SEL).getJSCNArray();
        JSCSArray detNames_sel = global().get(ARIMAConstants.DET_NAMES_SEL).getJSCSArray();
        det_data_all.appendCols(det_data_sel);
        det_names_all.appendRows(detNames_sel);
    }

    @Override
    public void shown(boolean isShown) {
        if (!isShown) assembleDeterministics();
    }

    /**
     * This method initializes jCheckBox
     * 
     * @return javax.swing.JCheckBox
     */
    JCheckBox getConstCheckBox() {
        if (constCheckBox == null) {
            constCheckBox = new JCheckBox();
            constCheckBox.setSelected(true);
            constCheckBox.setText("Add constant");
        }
        return constCheckBox;
    }

    /**
     * This method initializes jPanel1
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel1() {
        if (jPanel1 == null) {
            jPanel1 = new JPanel();
            jPanel1.setLayout(new BorderLayout());
            jPanel1.add(getJPanel(), java.awt.BorderLayout.NORTH);
            jPanel1.add(getJPanel2(), java.awt.BorderLayout.CENTER);
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
            jPanel2 = new JPanel();
            jPanel2.setLayout(new BorderLayout());
            jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED), "Hannan-Rissanen Model Selection", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", java.awt.Font.PLAIN, 10), new java.awt.Color(51, 51, 51)));
            jPanel2.add(getJPanel3(), java.awt.BorderLayout.NORTH);
            jPanel2.add(getJScrollPane(), java.awt.BorderLayout.CENTER);
        }
        return jPanel2;
    }

    /**
     * This method initializes jPanel3
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel3() {
        if (jPanel3 == null) {
            GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
            gridBagConstraints13.gridx = 0;
            gridBagConstraints13.gridwidth = 6;
            gridBagConstraints13.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints13.insets = new java.awt.Insets(0, 10, 0, 0);
            gridBagConstraints13.gridy = 0;
            jLabel6 = new JLabel();
            jLabel6.setText("Searches optimal AR and MA lags, uses settings for d and fixed regressors");
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.gridx = 1;
            gridBagConstraints11.insets = new java.awt.Insets(10, 10, 0, 0);
            gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints11.gridwidth = 2;
            gridBagConstraints11.anchor = java.awt.GridBagConstraints.NORTH;
            gridBagConstraints11.gridy = 2;
            jLabel4 = new JLabel();
            jLabel4.setText("Maximum lag order for p, q (must be < h)");
            jLabel4.setPreferredSize(new java.awt.Dimension(38, 20));
            GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
            gridBagConstraints10.gridx = 1;
            gridBagConstraints10.insets = new java.awt.Insets(10, 10, 0, 0);
            gridBagConstraints10.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints10.gridwidth = 2;
            gridBagConstraints10.gridy = 1;
            jLabel3 = new JLabel();
            jLabel3.setText("h (initial fit to compute resids)");
            jLabel3.setPreferredSize(new java.awt.Dimension(250, 20));
            GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
            gridBagConstraints9.gridx = 4;
            gridBagConstraints9.insets = new java.awt.Insets(10, 10, 0, 0);
            gridBagConstraints9.gridwidth = 2;
            gridBagConstraints9.weightx = 1.0D;
            gridBagConstraints9.weighty = 1.0D;
            gridBagConstraints9.anchor = java.awt.GridBagConstraints.NORTH;
            gridBagConstraints9.gridy = 2;
            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.fill = java.awt.GridBagConstraints.NONE;
            gridBagConstraints8.gridy = 2;
            gridBagConstraints8.weightx = 0.0D;
            gridBagConstraints8.insets = new java.awt.Insets(10, 10, 0, 0);
            gridBagConstraints8.anchor = java.awt.GridBagConstraints.NORTH;
            gridBagConstraints8.gridx = 0;
            GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
            gridBagConstraints7.fill = java.awt.GridBagConstraints.NONE;
            gridBagConstraints7.gridy = 1;
            gridBagConstraints7.weightx = 0.0D;
            gridBagConstraints7.insets = new java.awt.Insets(10, 10, 0, 0);
            gridBagConstraints7.gridx = 0;
            jPanel3 = new JPanel();
            jPanel3.setLayout(new GridBagLayout());
            jPanel3.setPreferredSize(new java.awt.Dimension(0, 100));
            jPanel3.add(getHSelector(), gridBagConstraints7);
            jPanel3.add(getPqMaxSelector(), gridBagConstraints8);
            jPanel3.add(getOkButton(), gridBagConstraints9);
            jPanel3.add(jLabel3, gridBagConstraints10);
            jPanel3.add(jLabel4, gridBagConstraints11);
            jPanel3.add(jLabel6, gridBagConstraints13);
        }
        return jPanel3;
    }

    /**
     * This method initializes jScrollPane
     * 
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setViewportView(getResultField());
        }
        return jScrollPane;
    }

    /**
     * This method initializes resultField
     * 
     * @return com.jstatcom.component.ResultField
     */
    private ResultField getResultField() {
        if (resultField == null) {
            resultField = new ResultField();
        }
        return resultField;
    }

    /**
     * This method initializes hSelector
     * 
     * @return com.jstatcom.component.NumSelector
     */
    NumSelector getHSelector() {
        if (hSelector == null) {
            hSelector = new NumSelector();
            hSelector.setPreferredSize(new java.awt.Dimension(100, 20));
            hSelector.setMinimumSize(new java.awt.Dimension(100, 20));
            hSelector.setNumber(8.0D);
            hSelector.setIntType(true);
            hSelector.setRangeExpr("[2,100]");
        }
        return hSelector;
    }

    /**
     * This method initializes pmaxSelector
     * 
     * @return com.jstatcom.component.NumSelector
     */
    NumSelector getPqMaxSelector() {
        if (pqmaxSelector == null) {
            pqmaxSelector = new NumSelector();
            pqmaxSelector.setPreferredSize(new java.awt.Dimension(100, 20));
            pqmaxSelector.setMinimumSize(new java.awt.Dimension(100, 20));
            pqmaxSelector.setNumber(3.0D);
            pqmaxSelector.setIntType(true);
            pqmaxSelector.setRangeExpr("[1,100]");
        }
        return pqmaxSelector;
    }

    /**
     * This method initializes okButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getOkButton() {
        if (okButton == null) {
            okButton = new JButton();
            okButton.setPreferredSize(new java.awt.Dimension(120, 25));
            okButton.setText("Execute");
            okButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    JSCNArray y = global().get(ARIMAConstants.END_DATA).getJSCNArray();
                    if (y.isEmpty()) {
                        StdMessages.infoNothingSelected("Please select a series first.");
                        return;
                    }
                    int h = getHSelector().getIntNumber();
                    int pqmax = getPqMaxSelector().getIntNumber();
                    if (pqmax >= h) {
                        StdMessages.errorInput("Maximum lag number must be smaller than h.");
                        return;
                    }
                    int rows = y.rows();
                    int d = getDComboBox().getSelectedIndex();
                    if (rows - d - 2 * h < 1) {
                        StdMessages.errorInput("Not enough observations. Please choose a smaller h.");
                        return;
                    }
                    if (pqmax >= (rows - d - h) / 4) {
                        StdMessages.errorInput("Not enough observations. Please reduce the maximum number of lags.");
                        return;
                    }
                    assembleDeterministics();
                    PCall job = new ARIMAHanRissPCall(h, pqmax, d, y, global().get(ARIMAConstants.DET_DATA_ALL).getJSCNArray(), global().get(ARIMAConstants.END_NAME).getJSCSArray().stringAt(0, 0), global().get(ARIMAConstants.DRANGE).getJSCDRange().getTSDateRange());
                    job.setSymbolTable(local());
                    job.setOutHolder(getResultField());
                    getResultField().clear();
                    job.execute();
                }
            });
        }
        return okButton;
    }

    /**
     * This method initializes seasCheckBox
     * 
     * @return javax.swing.JCheckBox
     */
    JCheckBox getSeasCheckBox() {
        if (seasCheckBox == null) {
            seasCheckBox = new JCheckBox();
            seasCheckBox.setText("Add seasonal dummies");
            seasCheckBox.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (seasCheckBox.isSelected()) {
                        getConstCheckBox().setSelected(true);
                        getConstCheckBox().setEnabled(false);
                    } else if (!getTrendCheckBox().isSelected()) getConstCheckBox().setEnabled(true);
                }
            });
        }
        return seasCheckBox;
    }

    /**
     * This method initializes trendCheckBox
     * 
     * @return javax.swing.JCheckBox
     */
    JCheckBox getTrendCheckBox() {
        if (trendCheckBox == null) {
            trendCheckBox = new JCheckBox();
            trendCheckBox.setText("Add linear trend");
            trendCheckBox.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (trendCheckBox.isSelected()) {
                        getConstCheckBox().setSelected(true);
                        getConstCheckBox().setEnabled(false);
                    } else if (!getSeasCheckBox().isSelected()) getConstCheckBox().setEnabled(true);
                }
            });
        }
        return trendCheckBox;
    }
}
