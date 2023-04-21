package newgen.presentation.acquisitions.acqAdv;

/**
 *
 * @author  Administrator
 */
public class CancelAndCancelReorderForm extends javax.swing.JInternalFrame implements newgen.presentation.component.NewGenLibScreen {

    /** Creates new form CancelAndCancelReorderForm */
    public CancelAndCancelReorderForm() {
        initComponents();
        panelOrder = new newgen.presentation.acquisitions.acqAdv.CancelAndCancelReorderPanel();
        this.getContentPane().add(panelOrder, java.awt.BorderLayout.CENTER);
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(this);
        reloadLocales();
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        bnHelp = new javax.swing.JButton();
        bnCancel = new javax.swing.JButton();
        bnClose = new javax.swing.JButton();
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("CancelandReorder"));
        setPreferredSize(new java.awt.Dimension(803, 577));
        bnHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/help.gif")));
        bnHelp.setMnemonic('h');
        bnHelp.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Help"));
        jPanel1.add(bnHelp);
        bnCancel.setMnemonic('c');
        bnCancel.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        bnCancel.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        bnCancel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnCancelActionPerformed(evt);
            }
        });
        jPanel1.add(bnCancel);
        bnClose.setMnemonic('e');
        bnClose.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Close"));
        bnClose.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Close"));
        bnClose.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnCloseActionPerformed(evt);
            }
        });
        jPanel1.add(bnClose);
        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);
        pack();
    }

    private void bnCancelActionPerformed(java.awt.event.ActionEvent evt) {
        panelOrder.onClickcancel();
    }

    private void bnCloseActionPerformed(java.awt.event.ActionEvent evt) {
        this.setVisible(false);
        this.dispose();
    }

    public void reloadLocales() {
        try {
            this.setTitle(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("CancelandReorder"));
            bnHelp.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Help"));
            bnCancel.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
            bnCancel.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
            bnClose.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Close"));
            bnClose.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Close"));
            newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(jPanel1);
            panelOrder.reloadLocales();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private javax.swing.JButton bnCancel;

    private javax.swing.JButton bnClose;

    private javax.swing.JButton bnHelp;

    private javax.swing.JPanel jPanel1;

    private newgen.presentation.acquisitions.acqAdv.CancelAndCancelReorderPanel panelOrder = null;
}
