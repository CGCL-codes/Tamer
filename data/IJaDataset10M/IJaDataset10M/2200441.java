package net.jabbra.gui;

import net.jabbra.core.JabbraConnection;
import net.jabbra.core.JabbraConnectionContainer;
import net.jabbra.core.JabbraController;
import net.jabbra.core.JabbraDatabase;
import net.jabbra.core.account.JabbraAccount;
import net.jabbra.core.roster.JabbraRoster;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 08.02.2008
 * Time: 14:06:02
 */
public class JabbraAccountsManager {

    private JFrame frame;

    private JButton addButton;

    private JList accountsListField;

    private JButton button2;

    private JButton button3;

    private JButton okButton;

    private JPanel mainPane;

    private DefaultListModel accountsListModel;

    public JabbraAccountsManager() {
        frame = new JFrame("JabbraAccountsManager");
        frame.setContentPane(mainPane);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.pack();
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
            }
        });
        final JabbraAccountsManager accountsManager = this;
        addButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new Thread() {

                    public void run() {
                        new JabbraAccountSettings(accountsManager, null);
                    }
                }.start();
            }
        });
        accountsListModel = new DefaultListModel();
        accountsListField.setModel(accountsListModel);
        loadAccounts();
        frame.setVisible(true);
    }

    private void loadAccounts() {
        for (JabbraConnection connection : JabbraConnectionContainer.getConnections()) {
            accountsListModel.addElement(connection.getAccount());
        }
    }

    public void addAccount(JabbraAccount account) {
        try {
            JabbraDatabase.getInstance().insertAccount(account);
            new JabbraConnection(account, new JabbraRoster(account.getJID(), account.getId())).connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        accountsListModel.addElement(account);
    }
}
