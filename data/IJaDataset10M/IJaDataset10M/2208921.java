package org.paccman.ui.accountselector;

import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Vector;
import org.paccman.controller.AccountController;
import org.paccman.controller.Controller;
import org.paccman.controller.ControllerManager;
import org.paccman.controller.PaccmanView;
import org.paccman.paccman.Account;
import org.paccman.ui.selector.ControllerSelectionButton;
import org.paccman.ui.selector.ControllerSelectionListener;
import static org.paccman.ui.main.ContextMain.*;

/**
 *
 * @author  joao
 */
public class AccountSelectorPanel extends javax.swing.JPanel implements PaccmanView, ActionListener {

    AccountController accountCtrl;

    Vector<ControllerSelectionButton> selButtons = new Vector<ControllerSelectionButton>();

    Vector<ControllerSelectionListener> accountSelectionListener = new Vector<ControllerSelectionListener>();

    public void setAccountCtrl(AccountController accountCtrl) {
        if (this.accountCtrl == accountCtrl) {
            return;
        }
        if (this.accountCtrl != null) {
            this.accountCtrl.unregisterView(this);
        }
        this.accountCtrl = accountCtrl;
        accountCtrl.registerView(this);
        selectedAccountLbl.setText(accountCtrl.getAccount().getName());
    }

    public void addListener(ControllerSelectionListener listener) {
        accountSelectionListener.add(listener);
    }

    /** Creates new form AccountSelectorPanel */
    public AccountSelectorPanel() {
        initComponents();
    }

    public void registerToDocumentCtrl() {
        getDocumentController().registerView(this);
    }

    public void onChange(Controller controller) {
        if (controller == getDocumentController()) {
            for (ControllerSelectionButton acb : selButtons) {
                acb.getController().unregisterView(acb);
                accountButtonsPanel.remove(acb);
            }
            selButtons.clear();
            Collection<Account> accounts = getDocumentController().getDocument().getAccounts();
            for (Account acc : accounts) {
                ControllerSelectionButton asb = new ControllerSelectionButton((AccountController) ControllerManager.getController(acc));
                asb.addActionListener(this);
                accountButtonsPanel.add(asb);
                selButtons.add(asb);
            }
            validate();
        } else if (controller == accountCtrl) {
            selectedAccountLbl.setText(accountCtrl.getAccount().getName());
        }
    }

    public void actionPerformed(java.awt.event.ActionEvent e) {
        ControllerSelectionButton asb = (ControllerSelectionButton) e.getSource();
        if (e != null) {
            if (selectAccount((AccountController) asb.getController())) {
                setAccountCtrl((AccountController) asb.getController());
            }
        }
    }

    private boolean selectionEnabled() {
        for (ControllerSelectionListener asl : accountSelectionListener) {
            if (!asl.selectionEnabled()) {
                return false;
            }
        }
        return true;
    }

    public boolean selectAccount(AccountController account) {
        if (selectionEnabled()) {
            for (ControllerSelectionListener asl : accountSelectionListener) {
                asl.controllerSelected(account);
            }
            setAccountCtrl(account);
            return true;
        } else {
            return false;
        }
    }

    private void initComponents() {
        accountButtonsPanel = new javax.swing.JPanel();
        selectedAccountLbl = new javax.swing.JLabel();
        setLayout(new java.awt.BorderLayout());
        accountButtonsPanel.setLayout(new java.awt.GridLayout(0, 1));
        selectedAccountLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        selectedAccountLbl.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED, null, java.awt.Color.white, null, null), new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED)));
        accountButtonsPanel.add(selectedAccountLbl);
        add(accountButtonsPanel, java.awt.BorderLayout.NORTH);
    }

    private javax.swing.JPanel accountButtonsPanel;

    private javax.swing.JLabel selectedAccountLbl;
}
