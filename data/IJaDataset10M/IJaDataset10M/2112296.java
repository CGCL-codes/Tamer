package newgen.presentation.circulation;

/**
 *
 * @author  vasu praveen
 */
public class CheckinFrame extends javax.swing.JInternalFrame implements newgen.presentation.component.NewGenLibScreen {

    private static CheckinFrame instance = null;

    private static Checkin checkin = null;

    /** Creates new form CheckoutToBinderFrame */
    private CheckinFrame() {
        initComponents();
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(this);
        javax.help.HelpBroker helpbroker = newgen.presentation.NewGenMain.getAppletInstance().getHelpbroker();
        javax.help.HelpSet helpset = newgen.presentation.NewGenMain.getAppletInstance().getHelpset();
        helpbroker.enableHelp(this, "checkinhelp", helpset);
        java.awt.event.ActionListener bhelpal = new javax.help.CSH.DisplayHelpFromSource(helpbroker);
        this.bHelp.addActionListener(bhelpal);
        checkin = Checkin.getInstance();
        this.getContentPane().add(checkin);
        this.setSize(650, 450);
    }

    private void initComponents() {
        plButton = new javax.swing.JPanel();
        bOk = new javax.swing.JButton();
        bPrint = new javax.swing.JButton();
        bHelp = new javax.swing.JButton();
        bCancel = new javax.swing.JButton();
        bClose = new javax.swing.JButton();
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("CheckIn"));
        setPreferredSize(new java.awt.Dimension(600, 450));
        setVisible(true);
        plButton.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        bOk.setMnemonic('O');
        bOk.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ok"));
        bOk.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ok"));
        bOk.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bOkActionPerformed(evt);
            }
        });
        plButton.add(bOk);
        bPrint.setMnemonic('p');
        bPrint.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PrintCheckInSlip"));
        bPrint.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PrintCheckInSlip"));
        bPrint.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bPrintActionPerformed(evt);
            }
        });
        plButton.add(bPrint);
        bHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/help.gif")));
        bHelp.setMnemonic('h');
        bHelp.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Help"));
        bHelp.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bHelpActionPerformed(evt);
            }
        });
        plButton.add(bHelp);
        bCancel.setMnemonic('c');
        bCancel.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        bCancel.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        bCancel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCancelActionPerformed(evt);
            }
        });
        plButton.add(bCancel);
        bClose.setMnemonic('e');
        bClose.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Close"));
        bClose.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Close"));
        bClose.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCloseActionPerformed(evt);
            }
        });
        plButton.add(bClose);
        getContentPane().add(plButton, java.awt.BorderLayout.SOUTH);
    }

    private void bPrintActionPerformed(java.awt.event.ActionEvent evt) {
        checkin.consolidatedCheckinSlip();
    }

    private void bOkActionPerformed(java.awt.event.ActionEvent evt) {
        checkin.updatecheckindatabase();
    }

    private void bHelpActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void bCancelActionPerformed(java.awt.event.ActionEvent evt) {
        checkin.refreshScreen();
    }

    private void bCloseActionPerformed(java.awt.event.ActionEvent evt) {
        checkin.refreshScreen();
        this.setVisible(false);
    }

    public static CheckinFrame getInstance() {
        if (instance == null) instance = new CheckinFrame(); else {
        }
        checkin.refreshData();
        instance.reloadLocales();
        return instance;
    }

    public void reloadLocales() {
        checkin.reloadLocales();
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(this);
        bOk.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ok"));
        bCancel.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        bClose.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Close"));
        bPrint.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PrintCheckInSlip"));
        setTitle(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("CheckIn"));
        bOk.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ok"));
        bPrint.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PrintCheckInSlip"));
        bHelp.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Help"));
        bCancel.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        bClose.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Close"));
    }

    private javax.swing.JButton bCancel;

    private javax.swing.JButton bClose;

    private javax.swing.JButton bHelp;

    private javax.swing.JButton bOk;

    private javax.swing.JButton bPrint;

    private javax.swing.JPanel plButton;
}
