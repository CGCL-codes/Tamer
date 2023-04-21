package KFramework30.Printing;

import KFramework30.Base.*;
import javax.swing.event.*;
import javax.swing.*;

public class printSettingDialogClass extends javax.swing.JDialog implements ListSelectionListener {

    private KConfigurationClass configuration;

    private KLogClass log;

    private selectFieldActionListenerClass selectFieldActionListener;

    private summaryActionListenerClass summaryActionListener;

    private boolean OKCloseFlag;

    /** Creates new form printSettingClass */
    public printSettingDialogClass(KConfigurationClass configurationParam, KLogClass logParam, java.awt.Window parentWindow, KPrintDataTableClass DBPrinterParam) throws KExceptionClass {
        super(parentWindow, java.awt.Dialog.ModalityType.DOCUMENT_MODAL);
        initComponents();
        pack();
        setSize(477, 380);
        setTitle("Print Setting");
        KMetaUtilsClass.centerInScreen(this);
        configuration = configurationParam;
        log = logParam;
        OKCloseFlag = false;
        selectFieldActionListener = new selectFieldActionListenerClass(configuration, log, this, sourceList, destinationList, DBPrinterParam);
        sourceList.addListSelectionListener(this);
        destinationList.addListSelectionListener(this);
        ToLeft.addActionListener(selectFieldActionListener);
        ToRight.addActionListener(selectFieldActionListener);
        upButton.addActionListener(selectFieldActionListener);
        downButton.addActionListener(selectFieldActionListener);
        clearButton.addActionListener(selectFieldActionListener);
        widthButton.addActionListener(selectFieldActionListener);
        summaryActionListener = new summaryActionListenerClass(configuration, log, this, sumFieldList, operationPanel, DBPrinterParam);
        sumFieldList.addListSelectionListener(this);
        sumFieldList.addListSelectionListener(summaryActionListener);
        sumApplyButton.addActionListener(summaryActionListener);
        setSelectFieldButtonStates();
    }

    private void setSelectFieldButtonStates() {
        if (sourceList.isSelectionEmpty()) ToRight.setEnabled(false); else ToRight.setEnabled(true);
        if (destinationList.isSelectionEmpty()) {
            ToLeft.setEnabled(false);
            upButton.setEnabled(false);
            downButton.setEnabled(false);
            widthButton.setEnabled(false);
        } else {
            ToLeft.setEnabled(true);
            upButton.setEnabled(true);
            downButton.setEnabled(true);
            widthButton.setEnabled(true);
        }
        if (((DefaultListModel) destinationList.getModel()).size() == 0) {
            clearButton.setEnabled(false);
            OKButton.setEnabled(false);
        } else {
            clearButton.setEnabled(true);
            OKButton.setEnabled(true);
        }
    }

    private void setSummaryButtonStates() {
        if (sumFieldList.isSelectionEmpty()) {
            sumApplyButton.setEnabled(false);
        } else {
            sumApplyButton.setEnabled(true);
        }
    }

    private void initComponents() {
        jTabbedPane1 = new javax.swing.JTabbedPane();
        selectFieldsPanel = new javax.swing.JPanel();
        ToRight = new javax.swing.JButton();
        ToLeft = new javax.swing.JButton();
        upButton = new javax.swing.JButton();
        downButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        sourceList = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        destinationList = new javax.swing.JList();
        widthButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        selectSummaryPanel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        sumFieldList = new javax.swing.JList();
        sumApplyButton = new javax.swing.JButton();
        operationPanel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        fieldNameLabel = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        String[] operationStrings = { "CNT", "SUM", "AVG", "" };
        operationComboBox = new javax.swing.JComboBox(operationStrings);
        operationComboBox.setSelectedIndex(0);
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        prefixTextField = new javax.swing.JTextField();
        suffixTextField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        String[] precisionStrings = { "0.", "0.0", "0.00", "0.000", "0.0000", "0.00000" };
        precisionComboBox = new javax.swing.JComboBox(precisionStrings);
        precisionComboBox.setSelectedIndex(0);
        jLabel9 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        OKButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setFont(new java.awt.Font("Dialog", 0, 10));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });
        jTabbedPane1.setFont(new java.awt.Font("Arial", 0, 10));
        selectFieldsPanel.setFont(new java.awt.Font("Arial", 0, 10));
        ToRight.setFont(new java.awt.Font("Arial", 0, 10));
        ToRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/toright1.jpg")));
        ToRight.setText("Add");
        ToRight.setActionCommand("to right");
        ToRight.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ToRight.setMargin(new java.awt.Insets(2, 6, 2, 6));
        ToRight.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        ToLeft.setFont(new java.awt.Font("Arial", 0, 10));
        ToLeft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/toleft1.jpg")));
        ToLeft.setText("Remove");
        ToLeft.setActionCommand("to left");
        ToLeft.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ToLeft.setMargin(new java.awt.Insets(2, 6, 2, 6));
        ToLeft.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        upButton.setFont(new java.awt.Font("Arial", 0, 10));
        upButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/up1.jpg")));
        upButton.setText("Move Up");
        upButton.setActionCommand("Up");
        upButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        upButton.setMargin(new java.awt.Insets(1, 1, 1, 1));
        upButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        downButton.setFont(new java.awt.Font("Arial", 0, 10));
        downButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/down1.jpg")));
        downButton.setText("Move Down");
        downButton.setActionCommand("Down");
        downButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        downButton.setMargin(new java.awt.Insets(1, 1, 1, 1));
        downButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        clearButton.setFont(new java.awt.Font("Arial", 0, 10));
        clearButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/delete1.jpg")));
        clearButton.setText("Clear");
        clearButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        clearButton.setMargin(new java.awt.Insets(2, 6, 2, 6));
        clearButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        sourceList.setFont(new java.awt.Font("Arial", 0, 10));
        jScrollPane1.setViewportView(sourceList);
        destinationList.setFont(new java.awt.Font("Arial", 0, 10));
        jScrollPane2.setViewportView(destinationList);
        widthButton.setFont(new java.awt.Font("Arial", 0, 10));
        widthButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/go1.jpg")));
        widthButton.setText("Set Width");
        widthButton.setActionCommand("Width");
        widthButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        widthButton.setMargin(new java.awt.Insets(2, 6, 2, 6));
        widthButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jLabel1.setFont(new java.awt.Font("Arial", 0, 10));
        jLabel1.setText("Available Fields");
        jLabel2.setFont(new java.awt.Font("Arial", 0, 10));
        jLabel2.setText("Selected Fields");
        org.jdesktop.layout.GroupLayout selectFieldsPanelLayout = new org.jdesktop.layout.GroupLayout(selectFieldsPanel);
        selectFieldsPanel.setLayout(selectFieldsPanelLayout);
        selectFieldsPanelLayout.setHorizontalGroup(selectFieldsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(selectFieldsPanelLayout.createSequentialGroup().add(10, 10, 10).add(selectFieldsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(selectFieldsPanelLayout.createSequentialGroup().add(jLabel1).add(144, 144, 144).add(jLabel2)).add(selectFieldsPanelLayout.createSequentialGroup().add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 130, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(selectFieldsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(ToRight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(ToLeft, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(clearButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 130, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(selectFieldsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(upButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(downButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(widthButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))));
        selectFieldsPanelLayout.setVerticalGroup(selectFieldsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(selectFieldsPanelLayout.createSequentialGroup().add(10, 10, 10).add(selectFieldsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(selectFieldsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 220, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(selectFieldsPanelLayout.createSequentialGroup().add(ToRight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(10, 10, 10).add(ToLeft, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(10, 10, 10).add(clearButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 220, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(selectFieldsPanelLayout.createSequentialGroup().add(upButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(10, 10, 10).add(downButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(10, 10, 10).add(widthButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))));
        jTabbedPane1.addTab("Select Fields", new javax.swing.ImageIcon(getClass().getResource("/resources/select1.jpg")), selectFieldsPanel, "");
        selectSummaryPanel.setFont(new java.awt.Font("Dialog", 0, 10));
        selectSummaryPanel.setName("");
        sumFieldList.setFont(new java.awt.Font("Arial", 0, 10));
        jScrollPane4.setViewportView(sumFieldList);
        sumApplyButton.setFont(new java.awt.Font("Arial", 0, 10));
        sumApplyButton.setText("Apply");
        sumApplyButton.setActionCommand("apply");
        sumApplyButton.setMargin(new java.awt.Insets(2, 6, 2, 6));
        operationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Summary", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 10)));
        operationPanel.setFont(new java.awt.Font("Arial", 0, 10));
        jLabel3.setFont(new java.awt.Font("Arial", 0, 10));
        jLabel3.setText("Field");
        fieldNameLabel.setFont(new java.awt.Font("Arial", 0, 10));
        fieldNameLabel.setText("jLabel2");
        fieldNameLabel.setName("fieldNameLabel");
        jLabel5.setFont(new java.awt.Font("Arial", 0, 10));
        jLabel5.setText("Summary");
        operationComboBox.setFont(new java.awt.Font("Arial", 0, 10));
        operationComboBox.setName("operationComboBox");
        jLabel6.setFont(new java.awt.Font("Arial", 0, 10));
        jLabel6.setText("Prefix");
        jLabel7.setFont(new java.awt.Font("Arial", 0, 10));
        jLabel7.setText("Sufix");
        prefixTextField.setFont(new java.awt.Font("Arial", 0, 10));
        prefixTextField.setName("prefixTextField");
        suffixTextField.setFont(new java.awt.Font("Arial", 0, 10));
        suffixTextField.setName("suffixTextField");
        jLabel8.setFont(new java.awt.Font("Arial", 0, 10));
        jLabel8.setText("Decimal");
        precisionComboBox.setFont(new java.awt.Font("Arial", 0, 10));
        precisionComboBox.setName("precisionComboBox");
        org.jdesktop.layout.GroupLayout operationPanelLayout = new org.jdesktop.layout.GroupLayout(operationPanel);
        operationPanel.setLayout(operationPanelLayout);
        operationPanelLayout.setHorizontalGroup(operationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(operationPanelLayout.createSequentialGroup().add(9, 9, 9).add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(15, 15, 15).add(fieldNameLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(operationPanelLayout.createSequentialGroup().add(9, 9, 9).add(jLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(25, 25, 25).add(operationComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(operationPanelLayout.createSequentialGroup().add(9, 9, 9).add(jLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(15, 15, 15).add(prefixTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(operationPanelLayout.createSequentialGroup().add(9, 9, 9).add(jLabel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(15, 15, 15).add(suffixTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(operationPanelLayout.createSequentialGroup().add(9, 9, 9).add(jLabel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(25, 25, 25).add(precisionComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)));
        operationPanelLayout.setVerticalGroup(operationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(operationPanelLayout.createSequentialGroup().add(7, 7, 7).add(operationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(fieldNameLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(15, 15, 15).add(operationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(operationComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(10, 10, 10).add(operationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(prefixTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(10, 10, 10).add(operationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(suffixTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(10, 10, 10).add(operationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(precisionComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))));
        jLabel9.setFont(new java.awt.Font("Arial", 0, 10));
        jLabel9.setText("Available Fields");
        org.jdesktop.layout.GroupLayout selectSummaryPanelLayout = new org.jdesktop.layout.GroupLayout(selectSummaryPanel);
        selectSummaryPanel.setLayout(selectSummaryPanelLayout);
        selectSummaryPanelLayout.setHorizontalGroup(selectSummaryPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(selectSummaryPanelLayout.createSequentialGroup().add(10, 10, 10).add(selectSummaryPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jScrollPane4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 130, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(50, 50, 50).add(selectSummaryPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(operationPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 220, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(selectSummaryPanelLayout.createSequentialGroup().add(150, 150, 150).add(selectSummaryPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(sumApplyButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(selectSummaryPanelLayout.createSequentialGroup().add(30, 30, 30).add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))))));
        selectSummaryPanelLayout.setVerticalGroup(selectSummaryPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(selectSummaryPanelLayout.createSequentialGroup().add(15, 15, 15).add(jLabel9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jScrollPane4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 220, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(selectSummaryPanelLayout.createSequentialGroup().add(20, 20, 20).add(operationPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 190, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(10, 10, 10).add(selectSummaryPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(sumApplyButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(selectSummaryPanelLayout.createSequentialGroup().add(10, 10, 10).add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))));
        jTabbedPane1.addTab("Add a Summary", new javax.swing.ImageIcon(getClass().getResource("/resources/recalc1.jpg")), selectSummaryPanel, "");
        OKButton.setFont(new java.awt.Font("Arial", 0, 10));
        OKButton.setText("Ok");
        OKButton.setMargin(new java.awt.Insets(2, 6, 2, 6));
        OKButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OKButtonActionPerformed(evt);
            }
        });
        cancelButton.setFont(new java.awt.Font("Arial", 0, 10));
        cancelButton.setText("Cancel");
        cancelButton.setMargin(new java.awt.Insets(2, 6, 2, 6));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(10, 10, 10).add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 460, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(layout.createSequentialGroup().add(300, 300, 300).add(OKButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(10, 10, 10).add(cancelButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(5, 5, 5).add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 285, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(10, 10, 10).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(OKButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(cancelButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))));
    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        setVisible(false);
        dispose();
    }

    private void OKButtonActionPerformed(java.awt.event.ActionEvent evt) {
        selectFieldActionListener.setPrintingFields();
        summaryActionListener.setDefaultPrintingFields();
        OKCloseFlag = true;
        setVisible(false);
        dispose();
    }

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {
        setVisible(false);
        dispose();
    }

    private javax.swing.JButton OKButton;

    private javax.swing.JButton ToLeft;

    private javax.swing.JButton ToRight;

    private javax.swing.JButton cancelButton;

    private javax.swing.JButton clearButton;

    private javax.swing.JList destinationList;

    private javax.swing.JButton downButton;

    private javax.swing.JLabel fieldNameLabel;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JScrollPane jScrollPane3;

    private javax.swing.JScrollPane jScrollPane4;

    private javax.swing.JTabbedPane jTabbedPane1;

    private javax.swing.JComboBox operationComboBox;

    private javax.swing.JPanel operationPanel;

    private javax.swing.JComboBox precisionComboBox;

    private javax.swing.JTextField prefixTextField;

    private javax.swing.JPanel selectFieldsPanel;

    private javax.swing.JPanel selectSummaryPanel;

    private javax.swing.JList sourceList;

    private javax.swing.JTextField suffixTextField;

    private javax.swing.JButton sumApplyButton;

    private javax.swing.JList sumFieldList;

    private javax.swing.JButton upButton;

    private javax.swing.JButton widthButton;

    /** Process for lists selectoin change */
    public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
        setSelectFieldButtonStates();
        setSummaryButtonStates();
    }

    public boolean dialogCloseResult() {
        show();
        return OKCloseFlag;
    }
}
