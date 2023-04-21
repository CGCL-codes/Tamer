package newgen.presentation.cataloguing.authorityFiles;

import javax.help.*;

/**
 *
 * @author  administrator
 */
public class AuthoritySubTopicalTermSearchFrame extends javax.swing.JInternalFrame implements newgen.presentation.component.NewGenLibScreen {

    /** Creates new form AuthoritySubTopicalTermSearchFrame */
    private static final AuthoritySubTopicalTermSearchFrame SINGLETON = new AuthoritySubTopicalTermSearchFrame();

    public static AuthoritySubTopicalTermSearchFrame getInstance() {
        SINGLETON.reloadLocales();
        SINGLETON.setVisible(true);
        return SINGLETON;
    }

    private AuthoritySubTopicalTermSearchFrame() {
        initComponents();
        HelpBroker helpbroker = newgen.presentation.NewGenMain.getAppletInstance().getHelpbroker();
        HelpSet helpset = newgen.presentation.NewGenMain.getAppletInstance().getHelpset();
        helpbroker.enableHelp(this, "AuthoritySubTopicalTermSearchFrame", helpset);
        java.awt.event.ActionListener bhelpal = new CSH.DisplayHelpFromSource(helpbroker);
        this.bnHelp.addActionListener(bhelpal);
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(this);
        searchPanel = new newgen.presentation.cataloguing.authorityFiles.AuthoritySubTopicalTermSearchPanel();
        searchPanel.setMode(1);
        this.getContentPane().add(searchPanel, java.awt.BorderLayout.CENTER);
        this.setSize(630, 517);
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        bnHelp = new javax.swing.JButton();
        bnCancel = new javax.swing.JButton();
        bnClose = new javax.swing.JButton();
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("SearchTopicalTerm"));
        jPanel1.setLayout(new java.awt.GridBagLayout());
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setPreferredSize(new java.awt.Dimension(196, 36));
        bnHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/help.gif")));
        bnHelp.setMnemonic('h');
        bnHelp.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Help"));
        jPanel1.add(bnHelp, new java.awt.GridBagConstraints());
        bnCancel.setMnemonic('c');
        bnCancel.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        bnCancel.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        bnCancel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnCancelActionPerformed(evt);
            }
        });
        jPanel1.add(bnCancel, new java.awt.GridBagConstraints());
        bnClose.setMnemonic('e');
        bnClose.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Close"));
        bnClose.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Close"));
        bnClose.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnCloseActionPerformed(evt);
            }
        });
        jPanel1.add(bnClose, new java.awt.GridBagConstraints());
        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);
        pack();
    }

    private void bnCloseActionPerformed(java.awt.event.ActionEvent evt) {
        searchPanel.cancel();
        dispose();
        this.setVisible(false);
    }

    private void bnCancelActionPerformed(java.awt.event.ActionEvent evt) {
        searchPanel.cancel();
    }

    public void reloadLocales() {
        searchPanel.reloadLocales();
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(this);
        setTitle(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("SearchTopicalTerm"));
        bnCancel.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        bnCancel.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        bnClose.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Close"));
        bnClose.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Close"));
        bnHelp.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Help"));
    }

    private javax.swing.JButton bnCancel;

    private javax.swing.JButton bnClose;

    private javax.swing.JButton bnHelp;

    private javax.swing.JPanel jPanel1;

    private newgen.presentation.cataloguing.authorityFiles.AuthoritySubTopicalTermSearchPanel searchPanel = null;
}
