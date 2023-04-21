package newgen.presentation.sm.smadv;

/**
 *
 * @author  Administrator
 */
public class FirmOrderSubscriptionFrame extends javax.swing.JInternalFrame implements newgen.presentation.component.NewGenLibScreen {

    private static FirmOrderSubscriptionFrame instance = null;

    private static FirmOrderSubscriptionPanel firmOrderPanel = null;

    public static FirmOrderSubscriptionFrame getInstance() {
        if (instance == null) {
            instance = new FirmOrderSubscriptionFrame();
        }
        return instance;
    }

    /** Creates new form FirmOrderSubscriptionFrame */
    public FirmOrderSubscriptionFrame() {
        initComponents();
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(this);
        firmOrderPanel = new FirmOrderSubscriptionPanel();
        this.jPanel1.add(firmOrderPanel);
        this.setSize(750, 550);
        javax.help.HelpBroker helpbroker = newgen.presentation.NewGenMain.getAppletInstance().getHelpbroker();
        javax.help.HelpSet helpset = newgen.presentation.NewGenMain.getAppletInstance().getHelpset();
        helpbroker.enableHelp(this, "PlaceFirmSubscriptionOrder", helpset);
        java.awt.event.ActionListener bhelpal = new javax.help.CSH.DisplayHelpFromSource(helpbroker);
        this.bHelp.addActionListener(bhelpal);
    }

    public void reloadLocales() {
        firmOrderPanel.reloadLocales();
        setTitle(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Placefirmorders"));
        bClose.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Close"));
        bCancel.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        bHelp.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Help"));
        bRefresh.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Refresh"));
        bCancel.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        bClose.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Close"));
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        bHelp = new javax.swing.JButton();
        bRefresh = new javax.swing.JButton();
        bCancel = new javax.swing.JButton();
        bClose = new javax.swing.JButton();
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Placefirmorders"));
        jPanel1.setLayout(new java.awt.BorderLayout());
        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        bHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/help.gif")));
        bHelp.setMnemonic('h');
        bHelp.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Help"));
        jPanel2.add(bHelp);
        bRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/general/Refresh16.gif")));
        bRefresh.setMnemonic('r');
        bRefresh.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Refresh"));
        bRefresh.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bRefreshActionPerformed(evt);
            }
        });
        jPanel2.add(bRefresh);
        bCancel.setMnemonic('c');
        bCancel.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        bCancel.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        bCancel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCancelActionPerformed(evt);
            }
        });
        jPanel2.add(bCancel);
        bClose.setMnemonic('e');
        bClose.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Close"));
        bClose.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Close"));
        bClose.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCloseActionPerformed(evt);
            }
        });
        jPanel2.add(bClose);
        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);
        pack();
    }

    private void bCancelActionPerformed(java.awt.event.ActionEvent evt) {
        firmOrderPanel.refreshScreen();
    }

    private void bRefreshActionPerformed(java.awt.event.ActionEvent evt) {
        firmOrderPanel.refreshScreen();
    }

    private void bCloseActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
    }

    private javax.swing.JButton bCancel;

    private javax.swing.JButton bClose;

    private javax.swing.JButton bHelp;

    private javax.swing.JButton bRefresh;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;
}
