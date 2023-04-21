package de.jmulti.str;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import org.apache.log4j.Logger;
import com.jstatcom.component.NumSelector;
import com.jstatcom.component.ResultField;
import com.jstatcom.component.StdMessages;
import com.jstatcom.component.TopFrameReference;
import com.jstatcom.engine.PCall;
import com.jstatcom.engine.PCallAdapter;
import com.jstatcom.model.JSCInt;
import com.jstatcom.model.JSCNArray;
import com.jstatcom.model.JSCString;
import com.jstatcom.model.ModelPanel;
import com.jstatcom.table.JSCDataTableScrollPane;
import com.jstatcom.table.JSCSArrayTable;
import com.jstatcom.ts.TSDateRange;
import com.jstatcom.util.UString;
import de.jmulti.proc.STREstimationCall;

/**
 * Panel for estimating an STR model.
 * 
 * @author <a href="mailto:mk@mk-home.de">Markus Kraetzig</a>
 */
final class STR_Estimate extends ModelPanel {

    private static final Logger log = Logger.getLogger(STR_Estimate.class);

    private JPanel ivjControlPanel = null;

    IvjEventHandler ivjEventHandler = new IvjEventHandler();

    private JButton ivjExecute = null;

    private ResultField ivjResultField = null;

    private JSCSArrayTable ivjDataTable_Trans = null;

    private JSCDataTableScrollPane ivjScrollPaneTrans = null;

    private JRadioButton ivjLSTR1 = null;

    private JRadioButton ivjLSTR2 = null;

    private NumSelector ivjGamma = null;

    private NumSelector ivjC1 = null;

    private NumSelector ivjC2 = null;

    private JLabel ivjJLabel1 = null;

    private JLabel ivjJLabel2 = null;

    private JLabel ivjJLabel3 = null;

    private JLabel ivjJLabel = null;

    private JComboBox ivjMaxLoops = null;

    private JButton ivjRestrictions = null;

    private STR_RestrictChooser strRestrictionChooser = null;

    class IvjEventHandler implements java.awt.event.ActionListener, java.awt.event.ItemListener {

        public void actionPerformed(java.awt.event.ActionEvent e) {
            if (e.getSource() == STR_Estimate.this.getExecute()) connEtoC1();
            if (e.getSource() == STR_Estimate.this.getRestrictions()) connEtoC3();
        }

        ;

        public void itemStateChanged(java.awt.event.ItemEvent e) {
            if (e.getSource() == STR_Estimate.this.getLSTR2()) connEtoC2(e);
        }

        ;
    }

    ;

    /**
     * EstimateStar constructor comment.
     */
    public STR_Estimate() {
        super();
        initialize();
    }

    private void connEtoC1() {
        try {
            this.execute_ActionEvents();
        } catch (Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    private void connEtoC2(java.awt.event.ItemEvent arg1) {
        try {
            this.lSTR2_ItemStateChanged();
        } catch (Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    private void connEtoC3() {
        try {
            this.restrictions_ActionEvents();
        } catch (Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
     * Prepares and performs STR estimation.
     */
    private void execute_ActionEvents() {
        JSCNArray selConst = getResChooser().getThet0Sel();
        JSCNArray selPhi0 = getResChooser().getPhi0Sel();
        JSCNArray selThetMinPhi = getResChooser().getThetMinusPhiSel();
        final int selTransIndex = getDataTable_Trans().getSelectedRow();
        if (selTransIndex == -1) {
            String msg = "Please select one transition variable.";
            StdMessages.infoNothingSelected(msg);
            return;
        }
        int loops = 0;
        Double loopD = UString.parseToNumber(getMaxLoops().getSelectedItem().toString());
        if (loopD == null) loops = 0; else loops = loopD.intValue();
        int k = 1;
        if (getLSTR2().isSelected()) k = 2;
        TSDateRange sampleRange = global().get(STR_Constants.STR_drange).getJSCDRange().getTSDateRange();
        int py = global().get(STR_Constants.STR_py).getJSCInt().intVal();
        int pz = global().get(STR_Constants.STR_pz).getJSCInt().intVal();
        JSCNArray startGammaC1C2 = new JSCNArray("gammaC1C2", new double[] { getGamma().getNumber(), getC1().getNumber(), getC2().getNumber() });
        global().get(STR_Constants.STR_CONST_EST).setJSCData(selConst);
        global().get(STR_Constants.STR_PHIRES_EST).setJSCData(selPhi0);
        PCall job = new STREstimationCall(global().get(STR_Constants.STR_Y).getJSCNArray(), global().get(STR_Constants.STR_X).getJSCNArray(), global().get(STR_Constants.STR_D).getJSCNArray(), py, pz, sampleRange, selTransIndex, global().get(STR_Constants.STR_transNames).getJSCSArray(), selConst, selPhi0, selThetMinPhi, global().get(STR_Constants.STR_thet0Names).getJSCSArray(), global().get(STR_Constants.STR_allRes).getJSCNArray(), startGammaC1C2, loops, k);
        job.setSymbolTable(global());
        job.setOutHolder(getResultField());
        job.addPCallListener(new PCallAdapter() {

            public void success() {
                global().get(STR_Constants.STR_TRANSINDEX).setJSCData(new JSCInt("toSet", selTransIndex));
                String transName = global().get(STR_Constants.STR_transNames).getJSCSArray().stringAt(selTransIndex, 0);
                global().get(STR_Constants.STR_TRANSNAME).setJSCData(new JSCString("toSet", transName));
            }
        });
        job.execute();
    }

    private NumSelector getC1() {
        if (ivjC1 == null) {
            try {
                ivjC1 = new NumSelector();
                ivjC1.setName("C1");
                ivjC1.setNumber(0.02);
                ivjC1.setPrecision(5);
                ivjC1.setSymbolName(STR_Constants.STR_C1.name);
                ivjC1.setPreferredSize(new java.awt.Dimension(100, 21));
                ivjC1.setMinimumSize(new java.awt.Dimension(100, 21));
            } catch (Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjC1;
    }

    private NumSelector getC2() {
        if (ivjC2 == null) {
            try {
                ivjC2 = new NumSelector();
                ivjC2.setName("C2");
                ivjC2.setNumber(0.02);
                ivjC2.setPrecision(5);
                ivjC2.setSymbolName(STR_Constants.STR_C2.name);
                ivjC2.setPreferredSize(new java.awt.Dimension(100, 21));
                ivjC2.setEnabled(false);
                ivjC2.setMinimumSize(new java.awt.Dimension(100, 21));
            } catch (Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjC2;
    }

    private javax.swing.JPanel getControlPanel() {
        if (ivjControlPanel == null) {
            try {
                ivjControlPanel = new javax.swing.JPanel();
                ivjControlPanel.setName("ControlPanel");
                ivjControlPanel.setPreferredSize(new java.awt.Dimension(300, 160));
                ivjControlPanel.setLayout(new java.awt.GridBagLayout());
                ivjControlPanel.setMinimumSize(new java.awt.Dimension(300, 100));
                java.awt.GridBagConstraints constraintsExecute = new java.awt.GridBagConstraints();
                constraintsExecute.gridx = 4;
                constraintsExecute.gridy = 3;
                constraintsExecute.gridwidth = 2;
                constraintsExecute.anchor = java.awt.GridBagConstraints.SOUTHWEST;
                constraintsExecute.insets = new java.awt.Insets(5, 20, 5, 0);
                java.awt.GridBagConstraints constraintsScrollPaneTrans = new java.awt.GridBagConstraints();
                constraintsScrollPaneTrans.gridx = 0;
                constraintsScrollPaneTrans.gridy = 0;
                constraintsScrollPaneTrans.gridheight = 5;
                constraintsScrollPaneTrans.fill = java.awt.GridBagConstraints.BOTH;
                constraintsScrollPaneTrans.weighty = 1.0;
                constraintsScrollPaneTrans.insets = new java.awt.Insets(0, 10, 0, 10);
                getControlPanel().add(getScrollPaneTrans(), constraintsScrollPaneTrans);
                java.awt.GridBagConstraints constraintsLSTR1 = new java.awt.GridBagConstraints();
                constraintsLSTR1.gridx = 1;
                constraintsLSTR1.gridy = 0;
                constraintsLSTR1.anchor = java.awt.GridBagConstraints.WEST;
                constraintsLSTR1.insets = new java.awt.Insets(5, 0, 0, 0);
                getControlPanel().add(getLSTR1(), constraintsLSTR1);
                java.awt.GridBagConstraints constraintsLSTR2 = new java.awt.GridBagConstraints();
                constraintsLSTR2.gridx = 1;
                constraintsLSTR2.gridy = 1;
                constraintsLSTR2.anchor = java.awt.GridBagConstraints.WEST;
                constraintsLSTR2.insets = new java.awt.Insets(5, 0, 0, 0);
                getControlPanel().add(getLSTR2(), constraintsLSTR2);
                java.awt.GridBagConstraints constraintsGamma = new java.awt.GridBagConstraints();
                constraintsGamma.gridx = 3;
                constraintsGamma.gridy = 0;
                constraintsGamma.anchor = java.awt.GridBagConstraints.WEST;
                constraintsGamma.insets = new java.awt.Insets(5, 10, 0, 0);
                java.awt.GridBagConstraints constraintsC1 = new java.awt.GridBagConstraints();
                constraintsC1.gridx = 3;
                constraintsC1.gridy = 1;
                constraintsC1.anchor = java.awt.GridBagConstraints.WEST;
                constraintsC1.insets = new java.awt.Insets(5, 10, 0, 0);
                java.awt.GridBagConstraints constraintsC2 = new java.awt.GridBagConstraints();
                constraintsC2.gridx = 3;
                constraintsC2.gridy = 2;
                constraintsC2.anchor = java.awt.GridBagConstraints.WEST;
                constraintsC2.insets = new java.awt.Insets(5, 10, 0, 0);
                java.awt.GridBagConstraints constraintsJLabel1 = new java.awt.GridBagConstraints();
                constraintsJLabel1.gridx = 2;
                constraintsJLabel1.gridy = 0;
                constraintsJLabel1.insets = new java.awt.Insets(5, 5, 0, 0);
                java.awt.GridBagConstraints constraintsJLabel2 = new java.awt.GridBagConstraints();
                constraintsJLabel2.gridx = 2;
                constraintsJLabel2.gridy = 1;
                constraintsJLabel2.insets = new java.awt.Insets(5, 5, 0, 0);
                java.awt.GridBagConstraints constraintsJLabel3 = new java.awt.GridBagConstraints();
                constraintsJLabel3.gridx = 2;
                constraintsJLabel3.gridy = 2;
                constraintsJLabel3.insets = new java.awt.Insets(5, 5, 0, 0);
                java.awt.GridBagConstraints constraintsMaxLoops = new java.awt.GridBagConstraints();
                constraintsMaxLoops.gridx = 3;
                constraintsMaxLoops.gridy = 3;
                constraintsMaxLoops.anchor = java.awt.GridBagConstraints.NORTHWEST;
                constraintsMaxLoops.insets = new java.awt.Insets(5, 10, 0, 0);
                java.awt.GridBagConstraints constraintsJLabel = new java.awt.GridBagConstraints();
                constraintsJLabel.gridx = 2;
                constraintsJLabel.gridy = 3;
                constraintsJLabel.anchor = java.awt.GridBagConstraints.NORTHWEST;
                constraintsJLabel.insets = new java.awt.Insets(5, 5, 0, 0);
                java.awt.GridBagConstraints constraintsRestrictions = new java.awt.GridBagConstraints();
                constraintsJLabel2.fill = java.awt.GridBagConstraints.HORIZONTAL;
                constraintsJLabel1.anchor = java.awt.GridBagConstraints.CENTER;
                constraintsJLabel1.fill = java.awt.GridBagConstraints.HORIZONTAL;
                constraintsJLabel.fill = java.awt.GridBagConstraints.HORIZONTAL;
                constraintsJLabel3.fill = java.awt.GridBagConstraints.HORIZONTAL;
                constraintsExecute.fill = java.awt.GridBagConstraints.NONE;
                constraintsRestrictions.gridx = 4;
                constraintsRestrictions.gridy = 2;
                constraintsRestrictions.gridwidth = 2;
                constraintsRestrictions.anchor = java.awt.GridBagConstraints.SOUTHWEST;
                constraintsRestrictions.insets = new java.awt.Insets(0, 20, 0, 0);
                constraintsRestrictions.fill = java.awt.GridBagConstraints.NONE;
                constraintsRestrictions.weightx = 1.0D;
                ivjControlPanel.add(getExecute(), constraintsExecute);
                ivjControlPanel.add(getGamma(), constraintsGamma);
                ivjControlPanel.add(getC1(), constraintsC1);
                ivjControlPanel.add(getC2(), constraintsC2);
                ivjControlPanel.add(getMaxLoops(), constraintsMaxLoops);
                ivjControlPanel.add(getJLabel1(), constraintsJLabel1);
                ivjControlPanel.add(getJLabel2(), constraintsJLabel2);
                ivjControlPanel.add(getJLabel3(), constraintsJLabel3);
                ivjControlPanel.add(getJLabel(), constraintsJLabel);
                ivjControlPanel.add(getRestrictions(), constraintsRestrictions);
                TitledBorder title = new TitledBorder(new BevelBorder(BevelBorder.LOWERED), "Estimate STR Model", TitledBorder.RIGHT, TitledBorder.TOP);
                getControlPanel().setBorder(title);
            } catch (Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjControlPanel;
    }

    private javax.swing.JButton getExecute() {
        if (ivjExecute == null) {
            try {
                ivjExecute = new javax.swing.JButton();
                ivjExecute.setName("Execute");
                ivjExecute.setText("Execute");
                ivjExecute.setPreferredSize(new java.awt.Dimension(120, 26));
                ivjExecute.setMinimumSize(new java.awt.Dimension(120, 26));
            } catch (Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjExecute;
    }

    private NumSelector getGamma() {
        if (ivjGamma == null) {
            try {
                ivjGamma = new NumSelector();
                ivjGamma.setName("Gamma");
                ivjGamma.setNumber(0.05);
                ivjGamma.setPrecision(5);
                ivjGamma.setRangeExpr("(0,1000]");
                ivjGamma.setSymbolName(STR_Constants.STR_GAMMA.name);
                ivjGamma.setPreferredSize(new java.awt.Dimension(100, 21));
                ivjGamma.setMinimumSize(new java.awt.Dimension(100, 21));
            } catch (Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjGamma;
    }

    private JSCSArrayTable getDataTable_Trans() {
        if (ivjDataTable_Trans == null) {
            try {
                ivjDataTable_Trans = new JSCSArrayTable();
                ivjDataTable_Trans.setName("DataTable_Trans");
                getScrollPaneTrans().setColumnHeaderView(ivjDataTable_Trans.getTableHeader());
                ivjDataTable_Trans.setColumnWidth(120);
                ivjDataTable_Trans.setSymbolName(STR_Constants.STR_transNames.name);
                ivjDataTable_Trans.setRowSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                ivjDataTable_Trans.setBounds(0, 0, 144, 104);
                ivjDataTable_Trans.setRowSelectionAllowed(true);
                ivjDataTable_Trans.setEditable(false);
            } catch (Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjDataTable_Trans;
    }

    private javax.swing.JLabel getJLabel() {
        if (ivjJLabel == null) {
            try {
                ivjJLabel = new javax.swing.JLabel();
                ivjJLabel.setName("JLabel");
                ivjJLabel.setPreferredSize(new java.awt.Dimension(65, 21));
                ivjJLabel.setText("Max loops");
                ivjJLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            } catch (Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJLabel;
    }

    private javax.swing.JLabel getJLabel1() {
        if (ivjJLabel1 == null) {
            try {
                ivjJLabel1 = new javax.swing.JLabel();
                ivjJLabel1.setName("JLabel1");
                ivjJLabel1.setText("Gamma start");
                ivjJLabel1.setPreferredSize(new java.awt.Dimension(80, 21));
                ivjJLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            } catch (Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJLabel1;
    }

    private javax.swing.JLabel getJLabel2() {
        if (ivjJLabel2 == null) {
            try {
                ivjJLabel2 = new javax.swing.JLabel();
                ivjJLabel2.setName("JLabel2");
                ivjJLabel2.setPreferredSize(new java.awt.Dimension(65, 21));
                ivjJLabel2.setText("C1 start");
                ivjJLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            } catch (Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJLabel2;
    }

    private javax.swing.JLabel getJLabel3() {
        if (ivjJLabel3 == null) {
            try {
                ivjJLabel3 = new javax.swing.JLabel();
                ivjJLabel3.setName("JLabel3");
                ivjJLabel3.setPreferredSize(new java.awt.Dimension(65, 21));
                ivjJLabel3.setText("C2 start");
                ivjJLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            } catch (Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJLabel3;
    }

    private javax.swing.JRadioButton getLSTR1() {
        if (ivjLSTR1 == null) {
            try {
                ivjLSTR1 = new javax.swing.JRadioButton();
                ivjLSTR1.setName("LSTR1");
                ivjLSTR1.setSelected(true);
                ivjLSTR1.setPreferredSize(new java.awt.Dimension(62, 21));
                ivjLSTR1.setText("LSTR1");
                ivjLSTR1.setMinimumSize(new java.awt.Dimension(62, 21));
            } catch (Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjLSTR1;
    }

    private javax.swing.JRadioButton getLSTR2() {
        if (ivjLSTR2 == null) {
            try {
                ivjLSTR2 = new javax.swing.JRadioButton();
                ivjLSTR2.setName("LSTR2");
                ivjLSTR2.setPreferredSize(new java.awt.Dimension(62, 21));
                ivjLSTR2.setText("LSTR2");
                ivjLSTR2.setMinimumSize(new java.awt.Dimension(62, 21));
            } catch (Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjLSTR2;
    }

    private javax.swing.JComboBox getMaxLoops() {
        if (ivjMaxLoops == null) {
            try {
                ivjMaxLoops = new javax.swing.JComboBox();
                ivjMaxLoops.setName("MaxLoops");
                ivjMaxLoops.setPreferredSize(new java.awt.Dimension(100, 21));
                ivjMaxLoops.setMinimumSize(new java.awt.Dimension(85, 21));
                ivjMaxLoops.addItem("100");
                ivjMaxLoops.addItem("250");
                ivjMaxLoops.addItem("500");
                ivjMaxLoops.addItem("1000");
                ivjMaxLoops.addItem("5000");
                ivjMaxLoops.addItem("10000");
                ivjMaxLoops.addItem("no limit");
                ivjMaxLoops.setSelectedIndex(6);
            } catch (Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjMaxLoops;
    }

    private javax.swing.JButton getRestrictions() {
        if (ivjRestrictions == null) {
            try {
                ivjRestrictions = new javax.swing.JButton();
                ivjRestrictions.setName("Restrictions");
                ivjRestrictions.setText("Set Restrictions");
                ivjRestrictions.setPreferredSize(new java.awt.Dimension(120, 26));
                ivjRestrictions.setMinimumSize(new java.awt.Dimension(120, 26));
            } catch (Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjRestrictions;
    }

    private com.jstatcom.component.ResultField getResultField() {
        if (ivjResultField == null) {
            try {
                ivjResultField = new com.jstatcom.component.ResultField();
                ivjResultField.setName("ResultField");
                ivjResultField.setBorder(new BevelBorder(BevelBorder.LOWERED));
            } catch (Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjResultField;
    }

    private JSCDataTableScrollPane getScrollPaneTrans() {
        if (ivjScrollPaneTrans == null) {
            try {
                ivjScrollPaneTrans = new JSCDataTableScrollPane();
                ivjScrollPaneTrans.setName("ScrollPaneTrans");
                ivjScrollPaneTrans.setMaximumVisibleColumns(1);
                String ivjLocal45columnHeaderStringData[] = { "transition variable" };
                ivjScrollPaneTrans.setColumnHeaderStringData(ivjLocal45columnHeaderStringData);
                ivjScrollPaneTrans.setColumnHeaderShowing(true);
                ivjScrollPaneTrans.setMaximumVisibleRows(10);
                getScrollPaneTrans().setViewportView(getDataTable_Trans());
            } catch (Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjScrollPaneTrans;
    }

    /**
     * Called whenever the part throws an exception.
     * 
     * @param exception
     *            Exception
     */
    private void handleException(Throwable exception) {
        log.error("Unhandled Exception", exception);
    }

    private void initConnections() throws Exception {
        getExecute().addActionListener(ivjEventHandler);
        getLSTR2().addItemListener(ivjEventHandler);
        getRestrictions().addActionListener(ivjEventHandler);
    }

    private void initialize() {
        try {
            ButtonGroup selectGroup = new ButtonGroup();
            selectGroup.add(getLSTR1());
            selectGroup.add(getLSTR2());
            setName("EstimateStar");
            setLayout(new java.awt.BorderLayout());
            setSize(570, 329);
            add(getControlPanel(), "North");
            add(getResultField(), "Center");
            initConnections();
        } catch (Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
     * Comment
     */
    private void lSTR2_ItemStateChanged() {
        getC2().setEnabled(getLSTR2().isSelected());
        return;
    }

    /**
     * Lazily initializes the restriction dialog.
     * 
     * @return STR_RestrictChooser
     */
    private STR_RestrictChooser getResChooser() {
        if (strRestrictionChooser == null) {
            strRestrictionChooser = new STR_RestrictChooser(TopFrameReference.getTopFrameRef(), true, global());
        }
        return strRestrictionChooser;
    }

    /**
     * Comment
     */
    private void restrictions_ActionEvents() {
        getResChooser().setLocationRelativeTo(TopFrameReference.getTopFrameRef());
        getResChooser().setVisible(true);
    }
}
