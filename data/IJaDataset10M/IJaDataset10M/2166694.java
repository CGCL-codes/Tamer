package newgen.presentation.administration;

/**
 *
 * @author  Administrator
 */
public class NewBudgetHeadInternalFrame extends javax.swing.JInternalFrame implements newgen.presentation.component.NewGenLibScreen {

    private static NewBudgetHeadPanel budgetpanel = null;

    private static final NewBudgetHeadInternalFrame SINGLETON = new NewBudgetHeadInternalFrame();

    public static NewBudgetHeadInternalFrame getInstance() {
        SINGLETON.reloadLocales();
        SINGLETON.setVisible(true);
        return SINGLETON;
    }

    /** Creates new form NewBudgetHeadInternalFrame */
    private NewBudgetHeadInternalFrame() {
        initComponents();
        javax.help.HelpBroker helpbroker = newgen.presentation.NewGenMain.getAppletInstance().getHelpbroker();
        javax.help.HelpSet helpset = newgen.presentation.NewGenMain.getAppletInstance().getHelpset();
        helpbroker.enableHelp(this, "Budgetheadhelp", helpset);
        java.awt.event.ActionListener bhelpal = new javax.help.CSH.DisplayHelpFromSource(helpbroker);
        this.bHelp.addActionListener(bhelpal);
        budgetpanel = NewBudgetHeadPanel.getInstance();
        this.jPanel1.add(budgetpanel);
        this.setSize(500, 400);
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(this);
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        bOk = new javax.swing.JButton();
        bHelp = new javax.swing.JButton();
        bClose = new javax.swing.JButton();
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("BudgetHead"));
        jPanel1.setLayout(new java.awt.BorderLayout());
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        bOk.setMnemonic('o');
        bOk.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ok"));
        bOk.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ok"));
        bOk.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bOkActionPerformed(evt);
            }
        });
        jPanel2.add(bOk);
        bHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/help.gif")));
        bHelp.setMnemonic('h');
        bHelp.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Help"));
        jPanel2.add(bHelp);
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

    private void bOkActionPerformed(java.awt.event.ActionEvent evt) {
        int i = budgetpanel.insertBudgetHeadDetails();
    }

    private void bCloseActionPerformed(java.awt.event.ActionEvent evt) {
        budgetpanel.refreshScreen();
        this.dispose();
    }

    public void reloadLocales() {
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(this);
        setTitle(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("BudgetHead"));
        bOk.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ok"));
        bOk.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ok"));
        bHelp.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Help"));
        bClose.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Close"));
        bClose.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Close"));
        budgetpanel.reloadLocales();
    }

    private javax.swing.JButton bClose;

    private javax.swing.JButton bHelp;

    private javax.swing.JButton bOk;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;
}
