package org.columba.mail.gui.config.accountlist;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.columba.api.gui.frame.IFrameMediator;
import org.columba.core.config.Config;
import org.columba.core.gui.base.ButtonWithMnemonic;
import org.columba.core.gui.base.DoubleClickListener;
import org.columba.core.gui.base.SingleSideEtchedBorder;
import org.columba.core.help.HelpManager;
import org.columba.mail.config.AccountItem;
import org.columba.mail.config.AccountList;
import org.columba.mail.config.MailConfig;
import org.columba.mail.folder.IMailFolder;
import org.columba.mail.gui.config.account.AccountDialog;
import org.columba.mail.gui.config.accountwizard.AccountWizardLauncher;
import org.columba.mail.gui.tree.FolderTreeModel;
import org.columba.mail.mailchecking.MailCheckingManager;
import org.columba.mail.pop3.POP3ServerCollection;
import org.columba.mail.util.MailResourceLoader;

/**
 * A dialog showing a list with the user's accounts.
 */
public class AccountListDialog extends JDialog implements ActionListener, ListSelectionListener {

    private AccountListTable listView;

    private AccountList accountList;

    private AccountItem accountItem;

    protected JTextField nameTextField = new JTextField();

    protected JButton addButton;

    protected JButton removeButton;

    protected JButton editButton;

    private int index;

    private IFrameMediator mediator;

    public AccountListDialog(IFrameMediator mediator) {
        super(mediator.getView().getFrame(), MailResourceLoader.getString("dialog", "account", "dialog_title"), true);
        this.mediator = mediator;
        accountList = MailConfig.getInstance().getAccountList();
        initComponents();
        getRootPane().registerKeyboardAction(this, "CLOSE", KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().registerKeyboardAction(this, "HELP", KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public AccountItem getSelected() {
        return accountItem;
    }

    public void setSelected(AccountItem item) {
        accountItem = item;
    }

    public void initComponents() {
        getContentPane().setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(5, 0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        addButton = new ButtonWithMnemonic(MailResourceLoader.getString("dialog", "account", "addaccount"));
        addButton.setActionCommand("ADD");
        addButton.addActionListener(this);
        removeButton = new ButtonWithMnemonic(MailResourceLoader.getString("dialog", "account", "removeaccount"));
        removeButton.setActionCommand("REMOVE");
        removeButton.setEnabled(false);
        removeButton.addActionListener(this);
        editButton = new ButtonWithMnemonic(MailResourceLoader.getString("dialog", "account", "editsettings"));
        editButton.setActionCommand("EDIT");
        editButton.setEnabled(false);
        editButton.addActionListener(this);
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        JPanel topBorderPanel = new JPanel();
        topBorderPanel.setLayout(new BorderLayout());
        topBorderPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        topBorderPanel.add(topPanel, BorderLayout.CENTER);
        JLabel nameLabel = new JLabel(MailResourceLoader.getString("dialog", "account", "name"));
        nameLabel.setEnabled(false);
        topPanel.add(nameLabel);
        topPanel.add(Box.createRigidArea(new java.awt.Dimension(10, 0)));
        topPanel.add(Box.createHorizontalGlue());
        nameTextField.setText(MailResourceLoader.getString("dialog", "account", "name"));
        nameTextField.setEnabled(false);
        topPanel.add(nameTextField);
        Component glue = Box.createVerticalGlue();
        c.anchor = GridBagConstraints.EAST;
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridBagLayout.setConstraints(glue, c);
        gridBagLayout = new GridBagLayout();
        c = new GridBagConstraints();
        JPanel eastPanel = new JPanel(gridBagLayout);
        mainPanel.add(eastPanel, BorderLayout.EAST);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridBagLayout.setConstraints(addButton, c);
        eastPanel.add(addButton);
        Component strut1 = Box.createRigidArea(new Dimension(30, 5));
        gridBagLayout.setConstraints(strut1, c);
        eastPanel.add(strut1);
        gridBagLayout.setConstraints(editButton, c);
        eastPanel.add(editButton);
        Component strut = Box.createRigidArea(new Dimension(30, 5));
        gridBagLayout.setConstraints(strut, c);
        eastPanel.add(strut);
        gridBagLayout.setConstraints(removeButton, c);
        eastPanel.add(removeButton);
        strut = Box.createRigidArea(new Dimension(30, 20));
        gridBagLayout.setConstraints(strut, c);
        eastPanel.add(strut);
        glue = Box.createVerticalGlue();
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1.0;
        gridBagLayout.setConstraints(glue, c);
        eastPanel.add(glue);
        listView = new AccountListTable(accountList, this);
        listView.getSelectionModel().addListSelectionListener(this);
        listView.addMouseListener(new DoubleClickListener() {

            public void doubleClick(MouseEvent ev) {
                actionPerformed(new ActionEvent(listView, 0, "EDIT"));
            }
        });
        JScrollPane scrollPane = new JScrollPane(listView);
        scrollPane.setPreferredSize(new Dimension(300, 250));
        scrollPane.getViewport().setBackground(Color.white);
        mainPanel.add(scrollPane);
        getContentPane().add(mainPanel);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(new SingleSideEtchedBorder(SwingConstants.TOP));
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 6, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        ButtonWithMnemonic closeButton = new ButtonWithMnemonic(MailResourceLoader.getString("global", "close"));
        closeButton.setActionCommand("CLOSE");
        closeButton.addActionListener(this);
        buttonPanel.add(closeButton);
        ButtonWithMnemonic helpButton = new ButtonWithMnemonic(MailResourceLoader.getString("global", "help"));
        buttonPanel.add(helpButton);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(closeButton);
        HelpManager.getInstance().enableHelpOnButton(helpButton, "configuring_columba");
        HelpManager.getInstance().enableHelpKey(getRootPane(), "configuring_columba");
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }
        DefaultListSelectionModel theList = (DefaultListSelectionModel) e.getSource();
        if (theList.isSelectionEmpty()) {
            removeButton.setEnabled(false);
            editButton.setEnabled(false);
        } else {
            removeButton.setEnabled(true);
            editButton.setEnabled(true);
            index = theList.getAnchorSelectionIndex();
            setSelected(accountList.get(index));
        }
    }

    protected void showAccountDialog() {
        AccountItem parent = getSelected();
        if (parent != null) {
            new AccountDialog(mediator, parent);
        }
    }

    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        if (action.equals("CLOSE")) {
            try {
                Config.getInstance().save();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            setVisible(false);
        } else if (action.equals("ADD")) {
            try {
                new AccountWizardLauncher().launchWizard(false);
                listView.update();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (action.equals("REMOVE")) {
            int n = JOptionPane.showConfirmDialog(this, MailResourceLoader.getString("dialog", "account", "confirmDelete.msg"), MailResourceLoader.getString("dialog", "account", "confirmDelete.title"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (n == JOptionPane.NO_OPTION) {
                return;
            }
            AccountItem item = accountList.remove(index);
            if (item.isPopAccount()) {
                POP3ServerCollection.getInstance().removePopServer(item.getUid());
            } else {
                IMailFolder folder = (IMailFolder) FolderTreeModel.getInstance().getImapFolder(item.getUid());
                try {
                    IMailFolder parentFolder = (IMailFolder) folder.getParent();
                    folder.removeFolder();
                    FolderTreeModel.getInstance().nodeStructureChanged(parentFolder);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            MailCheckingManager.getInstance().remove(item.getUid());
            MailCheckingManager.getInstance().update();
            removeButton.setEnabled(false);
            editButton.setEnabled(false);
            listView.update();
        } else if (action.equals("EDIT")) {
            showAccountDialog();
            listView.update();
        }
    }
}
