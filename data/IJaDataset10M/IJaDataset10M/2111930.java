package de.plugmail.defaults.gui;

import de.plugmail.data.*;
import de.plugmail.plugins.*;
import de.plugmail.plugins.gui.WorkspacePlugin;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.border.*;
import javax.swing.tree.*;
import org.apache.log4j.Logger;
import java.util.Vector;

public class DefaultWorkspace extends WorkspacePlugin {

    private JPanel panel;

    private Dimension dim;

    private Logger log;

    private JTree accountTree;

    private DefaultMutableTreeNode root;

    protected JTable table;

    protected JScrollPane list;

    protected JSplitPane right;

    protected AccountPlugin selectedAccount;

    public DefaultWorkspace() {
        log = Logger.getLogger(this.getClass());
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        dim = panel.getToolkit().getDefaultToolkit().getScreenSize();
        JSplitPane main = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        main.setDividerLocation((int) (dim.getWidth() / 5));
        main.setOneTouchExpandable(true);
        root = new DefaultMutableTreeNode("Accounts");
        accountTree = new JTree(root);
        JScrollPane left = new JScrollPane(accountTree);
        right = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        right.setDividerLocation((int) (dim.getHeight() / 2));
        right.setOneTouchExpandable(true);
        table = new JTable(new MailListTable(new String[][] { { "a" }, { "b" }, { "c" }, { "d" }, { "e" } }, new String[] { "mailnames" }));
        list = new JScrollPane(table);
        MouseListener ml = new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                int selRow = accountTree.getRowForLocation(e.getX(), e.getY());
                TreePath selPath = accountTree.getPathForLocation(e.getX(), e.getY());
                if (selRow != -1) {
                    if (e.getClickCount() == 1) {
                        log.debug("singleklick. selpath is " + selPath + "\thave to check " + accounts.size() + " accounts\t" + selPath.getPathCount());
                        for (int i = 0; i < accounts.size(); i++) {
                            AccountPlugin account = (AccountPlugin) accounts.get(i);
                            selectedAccount = account;
                            String pn = "";
                            for (int j = 0; j < selPath.getPathCount(); j++) {
                                log.debug("path(" + j + "): " + selPath.getPathComponent(j));
                            }
                            try {
                                pn = ((DefaultMutableTreeNode) selPath.getPathComponent(selPath.getPathCount() - 1)).toString();
                            } catch (Exception ex) {
                                log.debug("error: ", ex);
                            }
                            log.debug("account(" + i + "): " + account.getName() + "\tpn is: " + pn);
                            if (account.getName().equals(pn)) {
                                log.debug("found account: " + account);
                                Vector messages = getAllMessages(account.getDefaultInbox());
                                Object[][] messageArray = new Object[messages.size()][1];
                                log.debug("account has " + messages.size() + " messages...");
                                for (int j = 0; j < messages.size(); j++) {
                                    messageArray[j][0] = (Message) messages.get(j);
                                    log.debug("id " + j + ": " + ((Message) messages.get(j)).getMessageId().toString() + "\tstate: " + ((Message) messages.get(j)).getState());
                                }
                                log.debug("now creating new table...");
                                table = new JTable(new MailListTable(messageArray, new String[] { "mailnames" }));
                                table.setDefaultRenderer(Message.class, new MailInfoRenderer());
                                MouseListener ml2 = new MouseAdapter() {

                                    public void mousePressed(MouseEvent e) {
                                        int messageNr = table.getSelectedRow();
                                        Message tableMessage = (Message) table.getModel().getValueAt(messageNr, 0);
                                        String messageName = tableMessage.getMessageId().toString();
                                        log.debug("we have to show message with name " + messageName);
                                        JTextPane text = new JTextPane();
                                        String mail = "dies koennte eine email sein....";
                                        try {
                                            log.debug("selected account is: " + selectedAccount.getName());
                                            Message message = selectedAccount.getMessageStore().getMessage(messageName);
                                            log.debug("got a message? " + (message != null));
                                            mail = new String(message.getContent());
                                            log.debug("we got it...");
                                            text.setText(mail);
                                            message.setState(message.getState() | Message.READ);
                                            tableMessage.setState(message.getState());
                                            table.doLayout();
                                            selectedAccount.getMessageStore().updateMessage(selectedAccount.getName(), message);
                                        } catch (Exception exc) {
                                            log.error("error -> ", exc);
                                        }
                                        JScrollPane sp = new JScrollPane(text);
                                        right.setBottomComponent(sp);
                                    }
                                };
                                table.addMouseListener(ml2);
                                list = new JScrollPane(table);
                                right.setTopComponent(list);
                                right.updateUI();
                                break;
                            }
                        }
                    } else if (e.getClickCount() == 2) {
                        log.debug("doubleklicked, tree: " + selRow + "\t" + selPath);
                    }
                }
            }
        };
        accountTree.addMouseListener(ml);
        JPanel preview = new JPanel();
        preview.add(new JLabel("preview"));
        preview.setBackground(Color.yellow);
        right.setTopComponent(list);
        right.setBottomComponent(preview);
        main.setLeftComponent(left);
        main.setRightComponent(right);
        panel.setBackground(Color.white);
        panel.add(main);
    }

    public Object getWorkspace() {
        return panel;
    }

    public void activate() {
    }

    public void deactivate() {
    }

    public void addAccount(BasePlugin account) {
        AccountPlugin newAcc = (AccountPlugin) account;
        log.debug("adding new account " + newAcc.getName() + " to workspace...");
        this.accounts.add(newAcc);
        log.debug("added account to vector...");
        DefaultMutableTreeNode newAccNode = new DefaultMutableTreeNode(newAcc.getName());
        root.add(newAccNode);
        log.debug("added new account " + newAcc.getName() + " to workspace!");
    }

    public void removeAccount(BasePlugin account) {
        this.accounts.remove((AccountPlugin) account);
    }

    private Vector getAllMessages(Folder root) {
        Folder[] subFolders = root.getSubFolders();
        Vector messages = root.getMessages();
        for (int i = 0; i < subFolders.length; i++) {
            messages.addAll(getAllMessages(subFolders[i]));
        }
        return messages;
    }
}
