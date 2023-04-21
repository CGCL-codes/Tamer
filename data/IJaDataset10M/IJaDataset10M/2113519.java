package net.mjrz.fm.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.NumberFormat;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;
import org.apache.log4j.Logger;
import net.mjrz.fm.actions.ActionRequest;
import net.mjrz.fm.actions.ActionResponse;
import net.mjrz.fm.actions.UpdateAccountAction;
import net.mjrz.fm.entity.beans.Account;
import net.mjrz.fm.entity.beans.User;
import net.mjrz.fm.services.SessionManager;
import net.mjrz.fm.ui.FinanceManagerUI;
import net.mjrz.fm.ui.panels.AccountDetailsPanel;
import net.mjrz.fm.ui.panels.EditAccountDetailsPanel;
import net.mjrz.fm.ui.utils.SpringUtilities;
import net.mjrz.fm.ui.utils.TextComponentPopup;
import static net.mjrz.fm.utils.Messages.tr;

/**
 * @author Mjrz contact@mjrz.net
 *
 */
public class EditAccountDialog extends JDialog implements KeyListener {

    private static final long serialVersionUID = 0L;

    private JButton newAcctB, cancelB;

    private FinanceManagerUI parent = null;

    private User user = null;

    private NumberFormat numFormat = NumberFormat.getCurrencyInstance(SessionManager.getCurrencyLocale());

    private Account updateAccount = null;

    private EditAccountDetailsPanel acctDetailsPanel = null;

    public EditAccountDialog(FinanceManagerUI parent, User user, Account account) {
        super(parent, "Edit Account", true);
        this.parent = parent;
        this.user = user;
        this.updateAccount = account;
        initialize();
        try {
            acctDetailsPanel.populateFields(updateAccount);
        } catch (Exception e) {
            Logger.getLogger(EditAccountDialog.class.getName()).error(e);
            dispose();
        }
    }

    public void setDialogFocus() {
        acctDetailsPanel.setPanelFocus();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        JPanel center = new JPanel();
        center.setLayout(new BorderLayout());
        center.add(getAcctDetailsPane(), BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);
        add(getButtonPane(), BorderLayout.SOUTH);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        ActionMap am = getRootPane().getActionMap();
        InputMap im = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        Object windowCloseKey = new Object();
        KeyStroke windowCloseStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        Action windowCloseAction = new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                dispose();
            }
        };
        im.put(windowCloseStroke, windowCloseKey);
        am.put(windowCloseKey, windowCloseAction);
    }

    private JPanel getAcctDetailsPane() {
        acctDetailsPanel = new EditAccountDetailsPanel();
        return acctDetailsPanel;
    }

    private JPanel getButtonPane() {
        JPanel ret = new JPanel();
        ret.setLayout(new BoxLayout(ret, BoxLayout.LINE_AXIS));
        newAcctB = new JButton(tr("Save"));
        newAcctB.setActionCommand("Save");
        newAcctB.setMinimumSize(new Dimension(80, 20));
        newAcctB.setMnemonic(KeyEvent.VK_S);
        newAcctB.addActionListener(new ButtonHandler());
        getRootPane().setDefaultButton(newAcctB);
        cancelB = new JButton(tr("Cancel"));
        cancelB.setActionCommand("Cancel");
        cancelB.setMinimumSize(new Dimension(80, 20));
        cancelB.setMnemonic(KeyEvent.VK_C);
        cancelB.addActionListener(new ButtonHandler());
        ret.add(Box.createHorizontalGlue());
        ret.add(newAcctB);
        ret.add(Box.createRigidArea(new Dimension(20, 20)));
        ret.add(cancelB);
        ret.add(Box.createHorizontalGlue());
        return ret;
    }

    class ButtonHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();
            if (cmd.equals("Cancel")) {
                dispose();
            }
            if (cmd.equals("Save")) {
                try {
                    String result = validateFields();
                    if (result == null) {
                        updateAccount(updateAccount);
                    } else {
                        showErrorDialog(result);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return;
                }
            }
        }
    }

    private void showErrorDialog(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void updateAccount(Account selected) throws Exception {
        if (selected == null) return;
        ActionResponse resp = null;
        try {
            acctDetailsPanel.populateAccountData(selected);
            Account a = null;
            a = (Account) selected.clone();
            if (a == null) return;
            ActionRequest req = new ActionRequest();
            req.setUser(user);
            req.setActionName("updateAccount");
            req.setProperty("ACCOUNT", a);
            if (!a.getAccountName().equals(selected.getAccountName())) req.setProperty("VALIDATENAME", true);
            UpdateAccountAction action = new UpdateAccountAction();
            resp = action.executeAction(req);
            if (!resp.hasErrors()) {
                parent.reloadAccountList(a, false);
                parent.updateSummaryTab(a);
                dispose();
            } else {
                showErrorDialog(resp.getErrorMessage());
            }
            return;
        } catch (Exception ex) {
            if (resp != null) showErrorDialog(resp.getErrorMessage()); else showErrorDialog(tr("Error updating account: ") + ex.getMessage());
        }
    }

    private String validateFields() throws Exception {
        return acctDetailsPanel.validateFields();
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
        }
        if (key == KeyEvent.VK_ESCAPE) {
            dispose();
        }
        if (key == KeyEvent.VK_TAB) {
            java.awt.Component c = getFocusOwner();
            if (c != null && c.getClass().getName().equals("javax.swing.JTextArea")) {
                newAcctB.requestFocusInWindow();
            }
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }
}
