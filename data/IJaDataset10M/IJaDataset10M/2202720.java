package newgen.presentation.administration;

import javax.help.*;

/**
 *
 * @author  vasu praveen
 */
public class CIRCoBindingFrame extends javax.swing.JInternalFrame implements newgen.presentation.component.NewGenLibScreen {

    private static CIRCoBinding panel = null;

    private static final CIRCoBindingFrame SINGLETON = new CIRCoBindingFrame();

    public static CIRCoBindingFrame getInstance() {
        SINGLETON.reloadLocales();
        SINGLETON.setVisible(true);
        return SINGLETON;
    }

    /** Creates new form CheckoutToBinderFrame */
    private CIRCoBindingFrame() {
        initComponents();
        HelpBroker helpbroker = newgen.presentation.NewGenMain.getAppletInstance().getHelpbroker();
        HelpSet helpset = newgen.presentation.NewGenMain.getAppletInstance().getHelpset();
        java.awt.event.ActionListener bhelpal = new CSH.DisplayHelpFromSource(helpbroker);
        helpbroker.enableHelp(this, "Bindingtypeandprice", helpset);
        this.bHelp.addActionListener(bhelpal);
        panel = CIRCoBinding.getInstance();
        this.getContentPane().add(panel);
        this.setSize(635, 540);
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(this);
    }

    private void initComponents() {
        plButton = new javax.swing.JPanel();
        bHelp = new javax.swing.JButton();
        bClose = new javax.swing.JButton();
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Bindtypeconfiguration"));
        plButton.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        bHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/help.gif")));
        bHelp.setMnemonic('h');
        bHelp.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Help"));
        bHelp.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bHelpActionPerformed(evt);
            }
        });
        plButton.add(bHelp);
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

    private void bHelpActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void bCloseActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
    }

    public void reloadLocales() {
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(this);
        setTitle(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Bindtypeconfiguration"));
        bHelp.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Help"));
        bClose.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Close"));
        bClose.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Close"));
        panel.reloadLocales();
    }

    private javax.swing.JButton bClose;

    private javax.swing.JButton bHelp;

    private javax.swing.JPanel plButton;
}
