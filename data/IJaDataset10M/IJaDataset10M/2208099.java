package newgen.presentation.circulation.ill;

/**
 *
 * @author  Verus
 */
public class ILLPatronRequestFrame extends javax.swing.JInternalFrame {

    /** Creates new form ILLPatronRequestFrame */
    public ILLPatronRequestFrame() {
        initComponents();
        this.setSize(600, 500);
        this.getContentPane().add(pReqPanel);
        this.setVisible(true);
    }

    private void initComponents() {
        buttonPanel = new javax.swing.JPanel();
        bnOK = new javax.swing.JButton();
        bnHelp = new javax.swing.JButton();
        bnCancel = new javax.swing.JButton();
        bnClose = new javax.swing.JButton();
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("ILLRequest"));
        buttonPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        bnOK.setMnemonic('o');
        bnOK.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ok"));
        bnOK.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ok"));
        bnOK.setPreferredSize(new java.awt.Dimension(75, 26));
        bnOK.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnOKActionPerformed(evt);
            }
        });
        buttonPanel.add(bnOK);
        bnHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/help.gif")));
        bnHelp.setMnemonic('h');
        bnHelp.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Help"));
        bnHelp.setPreferredSize(new java.awt.Dimension(75, 26));
        bnHelp.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnHelpActionPerformed(evt);
            }
        });
        buttonPanel.add(bnHelp);
        bnCancel.setMnemonic('c');
        bnCancel.setText("Cancel");
        bnCancel.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        bnCancel.setPreferredSize(new java.awt.Dimension(75, 26));
        bnCancel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnCancelActionPerformed(evt);
            }
        });
        buttonPanel.add(bnCancel);
        bnClose.setMnemonic('e');
        bnClose.setText("Close");
        bnClose.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Close"));
        bnClose.setPreferredSize(new java.awt.Dimension(75, 26));
        bnClose.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnCloseActionPerformed(evt);
            }
        });
        buttonPanel.add(bnClose);
        getContentPane().add(buttonPanel, java.awt.BorderLayout.SOUTH);
        pack();
    }

    private void bnCloseActionPerformed(java.awt.event.ActionEvent evt) {
        this.setVisible(false);
    }

    private void bnCancelActionPerformed(java.awt.event.ActionEvent evt) {
        pReqPanel.clearFields();
    }

    private void bnHelpActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void bnOKActionPerformed(java.awt.event.ActionEvent evt) {
        pReqPanel.patronRequestForILLtoLibrarian();
    }

    private javax.swing.JButton bnCancel;

    private javax.swing.JButton bnClose;

    private javax.swing.JButton bnHelp;

    private javax.swing.JButton bnOK;

    private javax.swing.JPanel buttonPanel;

    ILLPatronRequestPanel pReqPanel = new ILLPatronRequestPanel();
}
