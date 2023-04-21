package newgen.presentation.acquisitions;

/**
 *
 * @author  Administrator
 */
public class OnApprovalReceivingDialog extends javax.swing.JDialog {

    private static OnApprovalReceivingDialog instance = null;

    private static newgen.presentation.administration.SearchPatronPanel panel = null;

    java.util.Vector vec = new java.util.Vector();

    public int RETURN_CODE = 0;

    /** Creates new form OnApprovalReceivingDialog */
    public OnApprovalReceivingDialog() {
        initComponents();
        panel = new newgen.presentation.administration.SearchPatronPanel();
        this.jPanel1.add(panel);
        this.setSize(650, 450);
        this.setLocation(newgen.presentation.NewGenMain.getAppletInstance().getLocation(650, 450));
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(this);
    }

    public static OnApprovalReceivingDialog getInstance(int SCREEN_MODE) {
        if (instance == null) instance = new OnApprovalReceivingDialog();
        System.out.println("This is buttonMode:" + SCREEN_MODE);
        panel.setSCREEN_MODE(SCREEN_MODE);
        return instance;
    }

    public java.util.Vector getData() {
        return vec;
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        bOk = new javax.swing.JButton();
        bHelp = new javax.swing.JButton();
        bCancel = new javax.swing.JButton();
        setTitle(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("SearchPatron"));
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
        bHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/help.gif")));
        bHelp.setMnemonic('h');
        jPanel2.add(bHelp);
        bCancel.setMnemonic('c');
        bCancel.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        bCancel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCancelActionPerformed(evt);
            }
        });
        jPanel2.add(bCancel);
        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);
        pack();
    }

    private void bCancelActionPerformed(java.awt.event.ActionEvent evt) {
        RETURN_CODE = 1;
        panel.clear();
        this.dispose();
    }

    private void bOkActionPerformed(java.awt.event.ActionEvent evt) {
        RETURN_CODE = 0;
        vec = panel.getData();
        if (vec.size() > 0) {
            this.dispose();
        }
    }

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {
        setVisible(false);
        dispose();
    }

    public int getReturn_Code() {
        return RETURN_CODE;
    }

    public void setReturn_Code(int RETURN_CODE) {
        this.RETURN_CODE = RETURN_CODE;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new OnApprovalReceivingDialog().show();
    }

    private javax.swing.JButton bCancel;

    private javax.swing.JButton bHelp;

    private javax.swing.JButton bOk;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;
}
