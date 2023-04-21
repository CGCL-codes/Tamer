package newgen.presentation.acquisitions;

/**
 *
 * @author  Administrator
 */
public class ApproveRequestsDialog extends javax.swing.JDialog {

    private static ApproveRequestsDialog instance = null;

    private static ApproveRequestsPanel panel = null;

    public static ApproveRequestsDialog getInstance(int BUTTON_MODE) {
        if (instance == null) {
            instance = new ApproveRequestsDialog();
            panel.setBUTTON_MODE(BUTTON_MODE);
        }
        return instance;
    }

    public ApproveRequestsDialog() {
        initComponents();
        panel = new ApproveRequestsPanel();
        this.jPanel1.add(panel);
        this.setSize(650, 450);
        this.setLocation(newgen.presentation.NewGenMain.getAppletInstance().getLocation(650, 450));
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(this);
    }

    public boolean getPendingRequestForBudgetApproval(String requestID, String libraryID) {
        return panel.getPendingRequestForBudgetApproval(requestID, libraryID);
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        bOk = new javax.swing.JButton();
        bReject = new javax.swing.JButton();
        bHelp = new javax.swing.JButton();
        bCancel = new javax.swing.JButton();
        bClose = new javax.swing.JButton();
        setTitle(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("ApproveRequests"));
        setModal(true);
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });
        jPanel1.setLayout(new java.awt.BorderLayout());
        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        bOk.setMnemonic('o');
        bOk.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ok"));
        bOk.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bOkActionPerformed(evt);
            }
        });
        jPanel2.add(bOk);
        bReject.setMnemonic('r');
        bReject.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Reject"));
        bReject.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bRejectActionPerformed(evt);
            }
        });
        jPanel2.add(bReject);
        bHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/help.gif")));
        jPanel2.add(bHelp);
        bCancel.setMnemonic('c');
        bCancel.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        bCancel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCancelActionPerformed(evt);
            }
        });
        jPanel2.add(bCancel);
        bClose.setMnemonic('e');
        bClose.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Close"));
        bClose.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCloseActionPerformed(evt);
            }
        });
        jPanel2.add(bClose);
        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);
        pack();
    }

    private void bOkActionPerformed(java.awt.event.ActionEvent evt) {
        panel.updateApproveBudgetDatabase();
    }

    private void bRejectActionPerformed(java.awt.event.ActionEvent evt) {
        panel.rejectRequest();
    }

    private void bCancelActionPerformed(java.awt.event.ActionEvent evt) {
        panel.refreshScreen();
    }

    private void bCloseActionPerformed(java.awt.event.ActionEvent evt) {
        panel.refreshScreen();
        this.dispose();
    }

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {
        setVisible(false);
        dispose();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new ApproveRequestsDialog().show();
    }

    private javax.swing.JButton bCancel;

    private javax.swing.JButton bClose;

    private javax.swing.JButton bHelp;

    private javax.swing.JButton bOk;

    private javax.swing.JButton bReject;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;
}
