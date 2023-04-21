package net.mogray.infinitypfm.ui.view.dialogs;

import java.sql.SQLException;
import net.mogray.infinitypfm.client.InfinityPfm;
import net.mogray.infinitypfm.core.conf.MM;
import net.mogray.infinitypfm.core.data.Account;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class AccountSelectorDialog extends BaseDialog {

    private Label lblAccount = null;

    private Combo cmbAccount = null;

    private Button cmdSelect = null;

    private Button cmdCancel = null;

    private String accountName = null;

    public AccountSelectorDialog() {
        super();
    }

    protected void LoadUI(Shell sh) {
        lblAccount = new Label(sh, SWT.NONE);
        lblAccount.setText(MM.PHRASES.getPhrase("93"));
        cmdSelect = new Button(sh, SWT.PUSH);
        cmdSelect.setText(MM.PHRASES.getPhrase("5"));
        cmdSelect.addSelectionListener(cmdSelect_OnClick);
        cmdCancel = new Button(sh, SWT.PUSH);
        cmdCancel.setText(MM.PHRASES.getPhrase("4"));
        cmdCancel.addSelectionListener(cmdCancel_OnClick);
        cmbAccount = new Combo(sh, SWT.DROP_DOWN | SWT.READ_ONLY);
        try {
            Account act = null;
            java.util.List list = MM.sqlMap.queryForList("getAccountsForType", MM.ACT_TYPE_EXPENSE);
            cmbAccount.removeAll();
            for (int i = 0; i < list.size(); i++) {
                act = (Account) list.get(i);
                cmbAccount.add(act.getActName());
            }
            list = MM.sqlMap.queryForList("getAccountsForType", MM.ACT_TYPE_LIABILITY);
            for (int i = 0; i < list.size(); i++) {
                act = (Account) list.get(i);
                cmbAccount.add(act.getActName());
            }
            cmbAccount.select(0);
        } catch (SQLException se) {
            InfinityPfm.LogMessage(se.getMessage());
        }
    }

    protected void LoadLayout() {
        FormData lblaccountdata = new FormData();
        lblaccountdata.top = new FormAttachment(0, 40);
        lblaccountdata.left = new FormAttachment(20, 0);
        lblAccount.setLayoutData(lblaccountdata);
        FormData cmbaccountdata = new FormData();
        cmbaccountdata.top = new FormAttachment(0, 40);
        cmbaccountdata.left = new FormAttachment(lblAccount, 10);
        cmbaccountdata.right = new FormAttachment(100, -30);
        cmbAccount.setLayoutData(cmbaccountdata);
        FormData cmdcanceldata = new FormData();
        cmdcanceldata.top = new FormAttachment(lblAccount, 40);
        cmdcanceldata.right = new FormAttachment(40, 10);
        cmdCancel.setLayoutData(cmdcanceldata);
        FormData cmdadddata = new FormData();
        cmdadddata.top = new FormAttachment(lblAccount, 40);
        cmdadddata.left = new FormAttachment(cmdCancel, 10);
        cmdadddata.right = new FormAttachment(60, 10);
        cmdSelect.setLayoutData(cmdadddata);
    }

    public int Open() {
        super.Open();
        shell.setText(MM.PHRASES.getPhrase("139"));
        shell.setSize(400, 200);
        this.CenterWindow();
        shell.open();
        Display display = parent.getDisplay();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        return 1;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    SelectionAdapter cmdSelect_OnClick = new SelectionAdapter() {

        public void widgetSelected(SelectionEvent e) {
            accountName = cmbAccount.getText();
            shell.dispose();
        }
    };

    SelectionAdapter cmdCancel_OnClick = new SelectionAdapter() {

        public void widgetSelected(SelectionEvent e) {
            shell.dispose();
        }
    };
}
