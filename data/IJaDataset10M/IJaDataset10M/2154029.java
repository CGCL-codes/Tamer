package net.sourceforge.signal.examples.ui.mvc.lwuit;

import java.util.Enumeration;
import java.util.Vector;
import net.sourceforge.signal.examples.core.IAccount;
import com.sun.lwuit.Command;
import com.sun.lwuit.List;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.list.DefaultListModel;

public class AccountList extends com.sun.lwuit.Form {

    public static final Command NEW_ACCOUNT_CMD = new Command("New");

    public static final Command EDIT_ACCOUNT_CMD = new Command("Edit");

    public static final Command EXIT_CMD = new Command("Exit");

    private List accountList = new List();

    private DefaultListModel accountListModel = new DefaultListModel();

    public AccountList() {
        System.out.println("=== AccountList");
        setLayout(new BorderLayout());
        accountList.setModel(accountListModel);
        addComponent(BorderLayout.CENTER, accountList);
        addCommand(EXIT_CMD);
        addCommand(EDIT_ACCOUNT_CMD);
        addCommand(NEW_ACCOUNT_CMD);
    }

    public void setAccounts(Vector accounts) {
        accountListModel.removeAll();
        for (Enumeration en = accounts.elements(); en.hasMoreElements(); ) {
            IAccount account = (IAccount) en.nextElement();
            accountListModel.addItem(account);
        }
    }

    public IAccount getSelectedAccount() {
        return (IAccount) accountList.getSelectedItem();
    }
}
