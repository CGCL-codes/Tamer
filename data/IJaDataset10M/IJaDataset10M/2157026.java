package RFID;

import com.phidgets.RFIDPhidget;
import com.phidgets.PhidgetException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import listeners.*;

/**
 *
 * @author  Owner
 */
public class RFID extends javax.swing.JFrame {

    private static String runArgs[];

    private RFIDPhidget rfid;

    private RFIDAttachListener attach_listener;

    private RFIDDetachListener detach_listener;

    private RFIDErrorListener error_listener;

    private RFIDTagGainListener tagGain_listener;

    private RFIDTagLossListener tagLoss_listener;

    /** Creates new form RFID */
    public RFID() {
        initComponents();
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        attachedTxt = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        nameTxt = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        serialTxt = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        versionTxt = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        numOutputsTxt = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        antennaChk = new javax.swing.JCheckBox();
        out0Chk = new javax.swing.JCheckBox();
        ledChk = new javax.swing.JCheckBox();
        out1Chk = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        tagTxt = new javax.swing.JTextField();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("RFID - full");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }

            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("RFID Info"));
        attachedTxt.setEditable(false);
        jLabel1.setText("Attached:");
        nameTxt.setBackground(javax.swing.UIManager.getDefaults().getColor("TextField.disabledBackground"));
        nameTxt.setColumns(20);
        nameTxt.setEditable(false);
        nameTxt.setFont(new java.awt.Font("Tahoma", 0, 11));
        nameTxt.setLineWrap(true);
        nameTxt.setRows(3);
        nameTxt.setTabSize(2);
        nameTxt.setWrapStyleWord(true);
        jScrollPane1.setViewportView(nameTxt);
        jLabel2.setText("Name:");
        serialTxt.setEditable(false);
        jLabel3.setText("Serial No.:");
        versionTxt.setEditable(false);
        jLabel4.setText("Version:");
        numOutputsTxt.setEditable(false);
        jLabel5.setText("Outputs:");
        antennaChk.setText("Antenna Enabled");
        antennaChk.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        antennaChk.setMargin(new java.awt.Insets(0, 0, 0, 0));
        antennaChk.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                antennaChkActionPerformed(evt);
            }
        });
        out0Chk.setText("Output 0");
        out0Chk.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        out0Chk.setMargin(new java.awt.Insets(0, 0, 0, 0));
        out0Chk.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                out0ChkActionPerformed(evt);
            }
        });
        ledChk.setText("Led Enabled");
        ledChk.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        ledChk.setMargin(new java.awt.Insets(0, 0, 0, 0));
        ledChk.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ledChkActionPerformed(evt);
            }
        });
        out1Chk.setText("Output 1");
        out1Chk.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        out1Chk.setMargin(new java.awt.Insets(0, 0, 0, 0));
        out1Chk.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                out1ChkActionPerformed(evt);
            }
        });
        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1Layout.createSequentialGroup().add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1Layout.createSequentialGroup().add(35, 35, 35).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(jLabel1).add(jLabel2).add(jLabel3).add(jLabel4).add(jLabel5)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false).add(org.jdesktop.layout.GroupLayout.LEADING, numOutputsTxt).add(org.jdesktop.layout.GroupLayout.LEADING, versionTxt).add(org.jdesktop.layout.GroupLayout.LEADING, serialTxt).add(org.jdesktop.layout.GroupLayout.LEADING, attachedTxt).add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane1))).add(jPanel1Layout.createSequentialGroup().addContainerGap().add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(antennaChk).add(ledChk)).add(25, 25, 25).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(out1Chk).add(out0Chk)))).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1Layout.createSequentialGroup().addContainerGap().add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(attachedTxt, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel1)).add(15, 15, 15).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel2)).add(16, 16, 16).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(serialTxt, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel3)).add(16, 16, 16).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(versionTxt, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel4)).add(15, 15, 15).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(numOutputsTxt, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel5)).add(31, 31, 31).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(antennaChk).add(out0Chk)).add(18, 18, 18).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(ledChk).add(out1Chk)).addContainerGap(20, Short.MAX_VALUE)));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Tag Info"));
        jLabel6.setText("Tag Data:");
        tagTxt.setEditable(false);
        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel2Layout.createSequentialGroup().add(40, 40, 40).add(jLabel6).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(tagTxt, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE).addContainerGap()));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel2Layout.createSequentialGroup().addContainerGap().add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(tagTxt, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel6)).addContainerGap(22, Short.MAX_VALUE)));
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        pack();
    }

    private void formWindowOpened(java.awt.event.WindowEvent evt) {
        antennaChk.setEnabled(false);
        ledChk.setEnabled(false);
        out0Chk.setEnabled(false);
        out1Chk.setEnabled(false);
        try {
            rfid = new RFIDPhidget();
            attach_listener = new RFIDAttachListener(this, this.attachedTxt, this.nameTxt, this.serialTxt, this.versionTxt, this.numOutputsTxt, this.antennaChk, this.ledChk, this.out0Chk, this.out1Chk);
            detach_listener = new RFIDDetachListener(this, this.attachedTxt, this.nameTxt, this.serialTxt, this.versionTxt, this.numOutputsTxt, this.antennaChk, this.ledChk, this.out0Chk, this.out1Chk);
            error_listener = new RFIDErrorListener(this);
            tagGain_listener = new RFIDTagGainListener(this.tagTxt);
            tagLoss_listener = new RFIDTagLossListener(this.tagTxt);
            rfid.addAttachListener(attach_listener);
            rfid.addDetachListener(detach_listener);
            rfid.addErrorListener(error_listener);
            rfid.addTagGainListener(tagGain_listener);
            rfid.addTagLossListener(tagLoss_listener);
            if ((runArgs.length > 1) && (runArgs[1].equals("remote"))) {
                rfid.open(Integer.parseInt(runArgs[0]), null);
            } else if (runArgs.length > 0) {
                rfid.open(Integer.parseInt(runArgs[0]));
            } else {
                rfid.openAny();
            }
        } catch (PhidgetException ex) {
            JOptionPane.showMessageDialog(this, ex.getDescription(), "Phidget Error" + ex.getErrorNumber(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void antennaChkActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            rfid.setAntennaOn(antennaChk.isSelected());
        } catch (PhidgetException ex) {
            JOptionPane.showMessageDialog(this, ex.getDescription(), "Phidget Error" + ex.getErrorNumber(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ledChkActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            rfid.setLEDOn(ledChk.isSelected());
        } catch (PhidgetException ex) {
            JOptionPane.showMessageDialog(this, ex.getDescription(), "Phidget Error" + ex.getErrorNumber(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void out0ChkActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            rfid.setOutputState(0, out0Chk.isSelected());
        } catch (PhidgetException ex) {
            JOptionPane.showMessageDialog(this, ex.getDescription(), "Phidget Error" + ex.getErrorNumber(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void out1ChkActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            rfid.setOutputState(1, out1Chk.isSelected());
        } catch (PhidgetException ex) {
            JOptionPane.showMessageDialog(this, ex.getDescription(), "Phidget Error" + ex.getErrorNumber(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void formWindowClosed(java.awt.event.WindowEvent evt) {
        try {
            rfid.removeTagLossListener(tagLoss_listener);
            rfid.removeTagGainListener(tagGain_listener);
            rfid.removeErrorListener(error_listener);
            rfid.removeDetachListener(detach_listener);
            rfid.removeAttachListener(attach_listener);
            rfid.close();
            rfid = null;
            dispose();
            System.exit(0);
        } catch (PhidgetException ex) {
            JOptionPane.showMessageDialog(this, ex.getDescription(), "Phidget Error" + ex.getErrorNumber(), JOptionPane.ERROR_MESSAGE);
            dispose();
            System.exit(0);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        runArgs = args;
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new RFID().setVisible(true);
            }
        });
    }

    private javax.swing.JCheckBox antennaChk;

    private javax.swing.JTextField attachedTxt;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JCheckBox ledChk;

    private javax.swing.JTextArea nameTxt;

    private javax.swing.JTextField numOutputsTxt;

    private javax.swing.JCheckBox out0Chk;

    private javax.swing.JCheckBox out1Chk;

    private javax.swing.JTextField serialTxt;

    private javax.swing.JTextField tagTxt;

    private javax.swing.JTextField versionTxt;
}
