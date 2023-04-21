package newgen.presentation.circulation.ill;

import java.awt.CardLayout;
import java.sql.Timestamp;
import java.util.Hashtable;
import javax.swing.JOptionPane;
import org.jdom.Element;

/**
 *
 * @author  Verus
 */
public class CompleteFieldsDialog extends javax.swing.JDialog {

    private java.util.Hashtable enteredValues = new java.util.Hashtable();

    private newgen.presentation.component.NewGenXMLGenerator newGenXMLGenerator = null;

    private int mode = 0;

    private int mode1 = 0;

    public static final int OK_CLICK = 1;

    public static final int CANCEL_CLICK = 2;

    public static final int REC_MODE = 1;

    public static final int REJ_MODE = 2;

    String reqId = "";

    String libId = "";

    String bcode = "";

    java.sql.Timestamp dueDate = null;

    java.sql.Timestamp rDate = null;

    String inote = "";

    String reqxml = "";

    Hashtable htvalues = new Hashtable();

    String resxml = "";

    /** Creates new form completeFields */
    public CompleteFieldsDialog() {
        initComponents();
        newGenXMLGenerator = newgen.presentation.component.NewGenXMLGenerator.getInstance();
        this.setModal(true);
        this.setSize(500, 400);
        this.setLocation(200, 200);
        check();
    }

    public void check() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        java.util.Date ddate = cal.getTime();
        cal.add(java.util.Calendar.DAY_OF_MONTH, 15);
        String retStr = String.valueOf(cal.getTimeInMillis());
        duedate1.setDate(retStr);
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonsPanel = new javax.swing.JPanel();
        bnOk = new javax.swing.JButton();
        bnCancel = new javax.swing.JButton();
        dispPanel = new javax.swing.JPanel();
        receivedPanel = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        tfBarcode = new newgen.presentation.UnicodeTextField();
        jLabel7 = new javax.swing.JLabel();
        receivedDate1 = new newgen.presentation.component.DateField();
        duedate1 = new newgen.presentation.component.DateField();
        jLabel8 = new javax.swing.JLabel();
        chkBox = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        taILLNote = new javax.swing.JTextArea();
        rejectedPanel = new javax.swing.JPanel();
        receivedDate2 = new newgen.presentation.component.DateField();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        illNote = new javax.swing.JTextArea();
        jLabel9 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        rejectedDate = new newgen.presentation.component.DateField();
        radioPanel = new javax.swing.JPanel();
        rbReceived = new javax.swing.JRadioButton();
        rbRejected = new javax.swing.JRadioButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("CompleteFields"));
        buttonsPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        bnOk.setMnemonic('o');
        bnOk.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ok"));
        bnOk.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ok"));
        bnOk.setPreferredSize(new java.awt.Dimension(60, 25));
        bnOk.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnOkActionPerformed(evt);
            }
        });
        buttonsPanel.add(bnOk);
        bnCancel.setMnemonic('c');
        bnCancel.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        bnCancel.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        bnCancel.setPreferredSize(new java.awt.Dimension(80, 25));
        bnCancel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnCancelActionPerformed(evt);
            }
        });
        buttonsPanel.add(bnCancel);
        getContentPane().add(buttonsPanel, java.awt.BorderLayout.SOUTH);
        dispPanel.setLayout(new java.awt.CardLayout());
        receivedPanel.setLayout(new java.awt.GridBagLayout());
        receivedPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("ILL barcode");
        jLabel5.setAlignmentY(0.0F);
        jLabel5.setIconTextGap(0);
        jLabel5.setMaximumSize(new java.awt.Dimension(56, 156));
        jLabel5.setPreferredSize(new java.awt.Dimension(60, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        receivedPanel.add(jLabel5, gridBagConstraints);
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Due date");
        jLabel6.setAlignmentY(0.0F);
        jLabel6.setPreferredSize(new java.awt.Dimension(50, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        receivedPanel.add(jLabel6, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        receivedPanel.add(tfBarcode, gridBagConstraints);
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Received date");
        jLabel7.setAlignmentY(0.0F);
        jLabel7.setPreferredSize(new java.awt.Dimension(80, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        receivedPanel.add(jLabel7, gridBagConstraints);
        receivedDate1.setColumns(20);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 2, 5);
        receivedPanel.add(receivedDate1, gridBagConstraints);
        duedate1.setColumns(20);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 2, 5);
        receivedPanel.add(duedate1, gridBagConstraints);
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Ill note");
        jLabel8.setAlignmentY(0.0F);
        jLabel8.setIconTextGap(0);
        jLabel8.setPreferredSize(new java.awt.Dimension(45, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        receivedPanel.add(jLabel8, gridBagConstraints);
        chkBox.setText("Non-returnable ");
        chkBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chkBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        receivedPanel.add(chkBox, gridBagConstraints);
        taILLNote.setColumns(27);
        taILLNote.setRows(5);
        jScrollPane1.setViewportView(taILLNote);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        receivedPanel.add(jScrollPane1, gridBagConstraints);
        dispPanel.add(receivedPanel, "card2");
        rejectedPanel.setLayout(new java.awt.GridBagLayout());
        receivedDate2.setColumns(20);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 2, 5);
        rejectedPanel.add(receivedDate2, gridBagConstraints);
        jLabel2.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("RejectedDate"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        rejectedPanel.add(jLabel2, gridBagConstraints);
        illNote.setColumns(28);
        illNote.setRows(5);
        illNote.setText("\n");
        jScrollPane2.setViewportView(illNote);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        rejectedPanel.add(jScrollPane2, gridBagConstraints);
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Ill note");
        jLabel9.setAlignmentY(0.0F);
        jLabel9.setIconTextGap(0);
        jLabel9.setPreferredSize(new java.awt.Dimension(45, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        rejectedPanel.add(jLabel9, gridBagConstraints);
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Ill note");
        jLabel4.setAlignmentY(0.0F);
        jLabel4.setIconTextGap(0);
        jLabel4.setPreferredSize(new java.awt.Dimension(45, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        rejectedPanel.add(jLabel4, gridBagConstraints);
        jLabel1.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("RejectedDate"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        rejectedPanel.add(jLabel1, gridBagConstraints);
        rejectedDate.setColumns(20);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 2, 5);
        rejectedPanel.add(rejectedDate, gridBagConstraints);
        dispPanel.add(rejectedPanel, "card3");
        getContentPane().add(dispPanel, java.awt.BorderLayout.CENTER);
        radioPanel.setLayout(new java.awt.GridBagLayout());
        radioPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        radioPanel.setPreferredSize(new java.awt.Dimension(10, 60));
        buttonGroup1.add(rbReceived);
        rbReceived.setSelected(true);
        rbReceived.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Received"));
        rbReceived.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rbReceived.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rbReceived.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbReceivedActionPerformed(evt);
            }
        });
        radioPanel.add(rbReceived, new java.awt.GridBagConstraints());
        buttonGroup1.add(rbRejected);
        rbRejected.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Rejected"));
        rbRejected.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rbRejected.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rbRejected.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbRejectedActionPerformed(evt);
            }
        });
        radioPanel.add(rbRejected, new java.awt.GridBagConstraints());
        getContentPane().add(radioPanel, java.awt.BorderLayout.NORTH);
        pack();
    }

    private void rbRejectedActionPerformed(java.awt.event.ActionEvent evt) {
        CardLayout gbLayout = (CardLayout) dispPanel.getLayout();
        gbLayout.show(dispPanel, "card3");
    }

    private void rbReceivedActionPerformed(java.awt.event.ActionEvent evt) {
        CardLayout gbLayout = (CardLayout) dispPanel.getLayout();
        gbLayout.show(dispPanel, "card2");
    }

    String barcode = "";

    String duedate = "";

    String receivedate = "";

    String illnote = "";

    public void setDefaultValues(java.util.Hashtable htVals) {
        tfBarcode.setText(htVals.get("Barcode").toString());
        duedate1.setDate(htVals.get("Duedate").toString());
        receivedDate1.setDate(htVals.get("Receivedate").toString());
        taILLNote.setText(htVals.get("ILLNote").toString());
        if (htVals.get("RequestReturnable").toString() != "") {
            if (htVals.get("RequestReturnable").toString() == "B") {
                chkBox.setSelected(true);
            } else {
                chkBox.setSelected(false);
            }
        }
    }

    private void bnOkActionPerformed(java.awt.event.ActionEvent evt) {
        this.setMode(OK_CLICK);
        this.setVisible(false);
    }

    private void bnCancelActionPerformed(java.awt.event.ActionEvent evt) {
        this.setMode(this.CANCEL_CLICK);
        this.dispose();
    }

    private javax.swing.JButton bnCancel;

    private javax.swing.JButton bnOk;

    private javax.swing.ButtonGroup buttonGroup1;

    private javax.swing.JPanel buttonsPanel;

    private javax.swing.JCheckBox chkBox;

    private javax.swing.JPanel dispPanel;

    private newgen.presentation.component.DateField duedate1;

    private javax.swing.JTextArea illNote;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JPanel radioPanel;

    private javax.swing.JRadioButton rbReceived;

    private javax.swing.JRadioButton rbRejected;

    private newgen.presentation.component.DateField receivedDate1;

    private newgen.presentation.component.DateField receivedDate2;

    private javax.swing.JPanel receivedPanel;

    private newgen.presentation.component.DateField rejectedDate;

    private javax.swing.JPanel rejectedPanel;

    private javax.swing.JTextArea taILLNote;

    private newgen.presentation.UnicodeTextField tfBarcode;

    public java.util.Hashtable getEnteredValues() {
        return enteredValues;
    }

    public void setEnteredValues(java.util.Hashtable enteredValues) {
        this.enteredValues = enteredValues;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public Hashtable returnHashtable() {
        Hashtable htVal = new Hashtable();
        String rejecteddate = "";
        String illnote = "";
        if (rbReceived.isSelected()) {
            this.setMode1(REC_MODE);
            bcode = tfBarcode.getText();
            System.out.println("BARCODE" + bcode);
            String due = duedate1.getDate();
            String rdate = receivedDate1.getDate();
            rDate = new java.sql.Timestamp(receivedDate1.getValueAsDate().getTime());
            dueDate = new java.sql.Timestamp(duedate1.getValueAsDate().getTime());
            inote = taILLNote.getText();
            htVal.put("Barcode", tfBarcode.getText());
            htVal.put("DueDate", dueDate);
            htVal.put("ReceiveDate", rDate);
            htVal.put("ILLNote", taILLNote.getText());
            if (chkBox.isSelected()) htVal.put("RequestReturnable", "B"); else htVal.put("RequestReturnable", "A");
            System.out.println("VALUES++++++++++++++++++++++++" + htVal);
            return htVal;
        } else {
            this.setMode1(REJ_MODE);
            rDate = new java.sql.Timestamp(rejectedDate.getValueAsDate().getTime());
            illnote = illNote.getText();
            htVal.put("ReceiveDate", rDate);
            htVal.put("ILLNote", illnote);
            return htVal;
        }
    }

    public int getMode1() {
        return mode1;
    }

    public void setMode1(int mode1) {
        this.mode1 = mode1;
    }

    public void checkBox(String mattype) {
        if (mattype.equalsIgnoreCase("B")) {
            chkBox.setSelected(true);
        } else if (mattype.equalsIgnoreCase("A")) {
            chkBox.setSelected(false);
        }
    }
}
