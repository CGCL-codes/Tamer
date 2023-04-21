package org.greenscape.mail.imap;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import org.greenscape.core.CoreUtil;
import org.greenscape.openaccount.Account;
import org.greenscape.openaccount.AccountService;
import org.greenscape.openmail.MailAccount;
import org.greenscape.openmail.MailAccountService;
import org.greenscape.openmail.MailReaderConfig;
import org.greenscape.persistence.PersistenceService;
import org.jdesktop.swingx.JXPanel;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.NbBundle;

/**
 *
 * @author smsajid
 */
public class AddEditIMAPAccountPanel extends JPanel implements ActionListener, MailReaderConfig {

    private static AddEditIMAPAccountPanel instance;

    Account account;

    IMAPMail imap;

    /** Creates new form AddEditIMAPAccountPanel */
    public AddEditIMAPAccountPanel() {
        initComponents();
        for (Account acc : AccountService.getAccounts()) {
            accountComboBox.addItem(acc);
        }
    }

    public static synchronized AddEditIMAPAccountPanel getDefault() {
        if (instance == null) {
            instance = new AddEditIMAPAccountPanel();
        }
        return instance;
    }

    @Override
    public Account getAccount() {
        return (Account) accountComboBox.getSelectedItem();
    }

    @Override
    public void setAccount(Account account) {
        this.account = account;
        if (account != null) {
            for (Account acc : AccountService.getAccounts()) {
                if (acc.equals(account)) {
                    accountComboBox.setSelectedItem(account);
                    break;
                }
            }
        }
    }

    @Override
    public MailAccount getMailAccount() {
        return imap;
    }

    @Override
    public void setMailAccount(MailAccount mail) {
        imap = (IMAPMail) mail;
        if (mail != null) {
            hostname.setText(imap.getHost());
            port.setValue(imap.getPort());
            if (imap.getEncryptionType() == EncryptionType.NONE) {
                noneOption.setSelected(true);
            } else if (imap.getEncryptionType() == EncryptionType.SSL) {
                sslOption.setSelected(true);
            } else if (imap.getEncryptionType() == EncryptionType.TLS) {
                tlsOption.setSelected(true);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equalsIgnoreCase("OK")) {
            if (!validateData()) {
                NotifyDescriptor nd = new NotifyDescriptor.Message(NbBundle.getMessage(AddEditIMAPAccountPanel.class, "AddEditIMAPAccountPanel.errorMessage"), NotifyDescriptor.Message.ERROR_MESSAGE);
                DialogDisplayer.getDefault().notify(nd);
            } else {
                List<Account> accountList = null;
                if (imap == null) {
                    imap = new IMAPMail();
                    imap.setEnabled(true);
                    accountList = new ArrayList<Account>();
                    imap.setAccountList(accountList);
                    accountList.add((Account) this.accountComboBox.getSelectedItem());
                } else {
                    accountList = imap.getAccountList();
                    if (!account.equals((Account) this.accountComboBox.getSelectedItem())) {
                        accountList.remove(account);
                        accountList.add((Account) this.accountComboBox.getSelectedItem());
                    }
                }
                imap.setUser(CoreUtil.getUser());
                imap.setHost(hostname.getText().trim().toLowerCase());
                imap.setPort((Integer) port.getValue());
                if (encryptionButtonGroup.getSelection().equals(noneOption.getModel())) {
                    imap.setEncryptionType(EncryptionType.NONE);
                } else if (encryptionButtonGroup.getSelection().equals(sslOption.getModel())) {
                    imap.setEncryptionType(EncryptionType.SSL);
                } else if (encryptionButtonGroup.getSelection().equals(tlsOption.getModel())) {
                    imap.setEncryptionType(EncryptionType.TLS);
                }
                List<MailAccount> accounts = (List<MailAccount>) MailAccountService.getAccounts(IMAPMail.class);
                for (MailAccount mailAccount : accounts) {
                    for (Account acc : mailAccount.getAccountList()) {
                        if (acc.getUser().equals(imap.getUser())) {
                            NotifyDescriptor nd = new NotifyDescriptor.Message(NbBundle.getMessage(AddEditIMAPAccountPanel.class, "AddEditIMAPAccountPanel.notUniqueErrorMessage"), NotifyDescriptor.Message.ERROR_MESSAGE);
                            DialogDisplayer.getDefault().notify(nd);
                            return;
                        }
                    }
                }
                PersistenceService.getDefault().persist(imap);
                getTopLevelAncestor().setVisible(false);
            }
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        encryptionButtonGroup = new ButtonGroup();
        jTabbedPane1 = new JTabbedPane();
        jXPanel1 = new JXPanel();
        jLabel2 = new JLabel();
        accountComboBox = new JComboBox();
        jLabel3 = new JLabel();
        jLabel4 = new JLabel();
        hostname = new JTextField();
        port = new JSpinner();
        jXPanel2 = new JXPanel();
        jXPanel3 = new JXPanel();
        noneOption = new JRadioButton();
        sslOption = new JRadioButton();
        tlsOption = new JRadioButton();
        jSeparator1 = new JSeparator();
        jLabel1 = new JLabel();
        jSeparator2 = new JSeparator();
        jTabbedPane1.setFont(new Font("Dialog", 0, 12));
        jLabel2.setFont(new Font("Dialog", 0, 12));
        jLabel2.setText(NbBundle.getMessage(AddEditIMAPAccountPanel.class, "AddEditIMAPAccountPanel.jLabel2.text"));
        accountComboBox.setFont(new Font("Dialog", 0, 12));
        jLabel3.setFont(new Font("Dialog", 0, 12));
        jLabel3.setText(NbBundle.getMessage(AddEditIMAPAccountPanel.class, "AddEditIMAPAccountPanel.jLabel3.text"));
        jLabel4.setFont(new Font("Dialog", 0, 12));
        jLabel4.setText(NbBundle.getMessage(AddEditIMAPAccountPanel.class, "AddEditIMAPAccountPanel.jLabel4.text"));
        port.setFont(new Font("Dialog", 0, 12));
        GroupLayout jXPanel1Layout = new GroupLayout(jXPanel1);
        jXPanel1.setLayout(jXPanel1Layout);
        jXPanel1Layout.setHorizontalGroup(jXPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(jXPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jXPanel1Layout.createParallelGroup(Alignment.LEADING).addComponent(jLabel2).addComponent(jLabel3).addComponent(jLabel4)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jXPanel1Layout.createParallelGroup(Alignment.TRAILING).addComponent(accountComboBox, Alignment.LEADING, 0, 345, Short.MAX_VALUE).addComponent(port, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE).addComponent(hostname, GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE)).addContainerGap()));
        jXPanel1Layout.setVerticalGroup(jXPanel1Layout.createParallelGroup(Alignment.LEADING).addGroup(jXPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jXPanel1Layout.createParallelGroup(Alignment.BASELINE).addComponent(jLabel2).addComponent(accountComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(jXPanel1Layout.createParallelGroup(Alignment.BASELINE).addComponent(jLabel3).addComponent(hostname, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(ComponentPlacement.RELATED).addGroup(jXPanel1Layout.createParallelGroup(Alignment.BASELINE).addComponent(jLabel4).addComponent(port, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addContainerGap(67, Short.MAX_VALUE)));
        jTabbedPane1.addTab(NbBundle.getMessage(AddEditIMAPAccountPanel.class, "AddEditIMAPAccountPanel.jXPanel1.TabConstraints.tabTitle"), jXPanel1);
        jXPanel3.setBorder(BorderFactory.createTitledBorder(NbBundle.getMessage(AddEditIMAPAccountPanel.class, "AddEditIMAPAccountPanel.jXPanel3.border.title")));
        encryptionButtonGroup.add(noneOption);
        noneOption.setFont(new Font("Dialog", 0, 12));
        noneOption.setSelected(true);
        noneOption.setText(NbBundle.getMessage(AddEditIMAPAccountPanel.class, "AddEditIMAPAccountPanel.noneOption.text"));
        encryptionButtonGroup.add(sslOption);
        sslOption.setFont(new Font("Dialog", 0, 12));
        sslOption.setText(NbBundle.getMessage(AddEditIMAPAccountPanel.class, "AddEditIMAPAccountPanel.sslOption.text"));
        encryptionButtonGroup.add(tlsOption);
        tlsOption.setFont(new Font("Dialog", 0, 12));
        tlsOption.setText(NbBundle.getMessage(AddEditIMAPAccountPanel.class, "AddEditIMAPAccountPanel.tlsOption.text"));
        GroupLayout jXPanel3Layout = new GroupLayout(jXPanel3);
        jXPanel3.setLayout(jXPanel3Layout);
        jXPanel3Layout.setHorizontalGroup(jXPanel3Layout.createParallelGroup(Alignment.LEADING).addGroup(jXPanel3Layout.createSequentialGroup().addContainerGap().addGroup(jXPanel3Layout.createParallelGroup(Alignment.LEADING).addComponent(noneOption).addComponent(sslOption).addComponent(tlsOption)).addContainerGap(156, Short.MAX_VALUE)));
        jXPanel3Layout.setVerticalGroup(jXPanel3Layout.createParallelGroup(Alignment.LEADING).addGroup(jXPanel3Layout.createSequentialGroup().addContainerGap().addComponent(noneOption).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(sslOption).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(tlsOption).addContainerGap(19, Short.MAX_VALUE)));
        GroupLayout jXPanel2Layout = new GroupLayout(jXPanel2);
        jXPanel2.setLayout(jXPanel2Layout);
        jXPanel2Layout.setHorizontalGroup(jXPanel2Layout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, jXPanel2Layout.createSequentialGroup().addContainerGap().addComponent(jXPanel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));
        jXPanel2Layout.setVerticalGroup(jXPanel2Layout.createParallelGroup(Alignment.LEADING).addGroup(jXPanel2Layout.createSequentialGroup().addContainerGap().addComponent(jXPanel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jTabbedPane1.addTab(NbBundle.getMessage(AddEditIMAPAccountPanel.class, "AddEditIMAPAccountPanel.jXPanel2.TabConstraints.tabTitle"), jXPanel2);
        jLabel1.setText(NbBundle.getMessage(AddEditIMAPAccountPanel.class, "AddEditIMAPAccountPanel.jLabel1.text"));
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(Alignment.TRAILING).addComponent(jSeparator2, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE).addComponent(jTabbedPane1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE).addComponent(jSeparator1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE).addComponent(jLabel1, Alignment.LEADING)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addPreferredGap(ComponentPlacement.RELATED).addComponent(jSeparator1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(jTabbedPane1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jSeparator2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addContainerGap()));
    }

    private JComboBox accountComboBox;

    private ButtonGroup encryptionButtonGroup;

    private JTextField hostname;

    private JLabel jLabel1;

    private JLabel jLabel2;

    private JLabel jLabel3;

    private JLabel jLabel4;

    private JSeparator jSeparator1;

    private JSeparator jSeparator2;

    private JTabbedPane jTabbedPane1;

    private JXPanel jXPanel1;

    private JXPanel jXPanel2;

    private JXPanel jXPanel3;

    private JRadioButton noneOption;

    private JSpinner port;

    private JRadioButton sslOption;

    private JRadioButton tlsOption;

    private boolean validateData() {
        return true;
    }
}
