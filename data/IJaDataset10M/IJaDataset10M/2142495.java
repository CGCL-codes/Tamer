package jitt64.swing.popup;

import jitt64.Shared;
import jitt64.table.InstrTable;
import jitt64.swing.Hex4InputVerifier;
import jitt64.swing.table.DataTableModelFixedPulse;
import java.awt.Toolkit;

/**
 * A dialog for popup menu table for Fixed Pulse
 * 
 * @author  ice
 */
public class JDialogFixedPulse extends javax.swing.JDialog {

    /** Instrument table to use */
    InstrTable instrTable;

    /** Actual instrument to use */
    int actInstr;

    /** The index of where popuping*/
    int index;

    /** True if hex is to use */
    private boolean isHex = false;

    /** True if input test must be clearer after an insert */
    private boolean isToClearText = false;

    /** Data model for fixed pulse */
    DataTableModelFixedPulse dataTableModelFixedPulse = new DataTableModelFixedPulse(true);

    /** Creates new form JDialogDN */
    public JDialogFixedPulse(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        Shared.framesList.add(this);
    }

    /**
   * Set the values to use for instrument and table
   * 
   * @param instrTable the instrument table to use
   * @param actInstr the actual instrument to use
   * @param index the index in table where popuping
   */
    public void setValues(InstrTable instrTable, int actInstr, int index) {
        this.instrTable = instrTable;
        this.actInstr = actInstr;
        this.index = index;
        isHex = Shared.option.isHexNumInsert();
        isToClearText = Shared.option.isToClearText();
        dataTableModelFixedPulse.setActInstr(actInstr);
        dataTableModelFixedPulse.setIsHex(isHex);
    }

    private void initComponents() {
        jScrollBar1 = new javax.swing.JScrollBar();
        jPanelMain = new javax.swing.JPanel();
        jPanelInput = new javax.swing.JPanel();
        jTextFieldInput = new javax.swing.JTextField();
        jScrollPaneTable = new javax.swing.JScrollPane();
        jTableFP = new javax.swing.JTable();
        jPanelDn = new javax.swing.JPanel();
        jButtonClose = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
        setResizable(false);
        setUndecorated(true);
        jPanelMain.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelMain.setLayout(new java.awt.BorderLayout());
        jTextFieldInput.setToolTipText("Manually enter fixed pulse to use (press return to confirm the value)");
        jTextFieldInput.setPreferredSize(new java.awt.Dimension(65, 19));
        jTextFieldInput.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldInputActionPerformed(evt);
            }
        });
        jPanelInput.add(jTextFieldInput);
        jPanelMain.add(jPanelInput, java.awt.BorderLayout.PAGE_START);
        jTableFP.setModel(dataTableModelFixedPulse);
        jTableFP.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableFPMouseClicked(evt);
            }
        });
        jScrollPaneTable.setViewportView(jTableFP);
        jPanelMain.add(jScrollPaneTable, java.awt.BorderLayout.CENTER);
        jButtonClose.setText("Dismiss");
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });
        jPanelDn.add(jButtonClose);
        jPanelMain.add(jPanelDn, java.awt.BorderLayout.SOUTH);
        getContentPane().add(jPanelMain, java.awt.BorderLayout.CENTER);
        setBounds(0, 0, 85, 235);
    }

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {
        setVisible(false);
    }

    private void jTableFPMouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() > 1) {
            int row = jTableFP.getSelectedRow();
            instrTable.setValue(index, row);
            Shared.timer.notifyChange();
            setVisible(false);
        }
    }

    private void jTextFieldInputActionPerformed(java.awt.event.ActionEvent evt) {
        int value;
        String aValue = jTextFieldInput.getText();
        if (isHex) {
            if (!Hex4InputVerifier.verifyAll((String) aValue)) {
                Toolkit.getDefaultToolkit().beep();
                return;
            }
            value = Integer.parseInt((String) aValue, 16);
        } else {
            try {
                value = new Integer((String) aValue);
            } catch (Exception e) {
                Toolkit.getDefaultToolkit().beep();
                return;
            }
        }
        if (value < 0 || value > 4095) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }
        int[] values = Shared.instruments[actInstr].getObjTableFixedPulse().getValues();
        for (int i = 0; i < values.length; i++) {
            if (values[i] == value) {
                instrTable.setValue(index, i);
                Shared.timer.notifyChange();
                if (isToClearText) jTextFieldInput.setText("");
                setVisible(false);
                return;
            }
        }
        int[] used = Shared.instruments[actInstr].getObjTableFixedPulse().getNumUsed();
        for (int i = 1; i < used.length; i++) {
            if (used[i] == 0) {
                values[i] = value;
                instrTable.setValue(index, i);
                Shared.timer.notifyChange();
                dataTableModelFixedPulse.fireTableRowsUpdated(i, i);
                if (isToClearText) jTextFieldInput.setText("");
                setVisible(false);
                return;
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                JDialogFixedPulse dialog = new JDialogFixedPulse(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    private javax.swing.JButton jButtonClose;

    private javax.swing.JPanel jPanelDn;

    private javax.swing.JPanel jPanelInput;

    private javax.swing.JPanel jPanelMain;

    private javax.swing.JScrollBar jScrollBar1;

    private javax.swing.JScrollPane jScrollPaneTable;

    private javax.swing.JTable jTableFP;

    private javax.swing.JTextField jTextFieldInput;
}
