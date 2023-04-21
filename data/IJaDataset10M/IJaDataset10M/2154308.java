package cn.com.androidforfun.finance.ui;

import java.util.List;
import android.app.Activity;
import android.content.Intent;
import cn.com.androidforfun.finance.R;
import cn.com.androidforfun.finance.context.Contexts;
import cn.com.androidforfun.finance.data.Account;
import cn.com.androidforfun.finance.data.IDataProvider;

public class AccountManageDesktop extends AbstractDesktop {

    public AccountManageDesktop(Activity activity) {
        super(activity);
    }

    @Override
    protected void init() {
        label = i18n.string(R.string.dt_wallet);
        icon = R.drawable.tab_wallet;
        Intent intent = new Intent(activity, AccountEditorActivity.class);
        Account acc = new Account("", "", 0D, 0);
        intent.putExtra(AccountEditorActivity.INTENT_MODE_CREATE, true);
        intent.putExtra(AccountEditorActivity.INTENT_ACCOUNT, acc);
        DesktopItem creator = new DesktopItem(new IntentRun(activity, intent), i18n.string(R.string.dtitem_accmgnt), R.drawable.dtitem_account);
        addItem(creator);
        IDataProvider iDataProvider = Contexts.instance().getDataProvider();
        List<Account> listAccount = iDataProvider.listAccount();
        for (Account account : listAccount) {
            intent = new Intent(activity, AccountEditorActivity.class);
            intent.putExtra(AccountEditorActivity.INTENT_MODE_CREATE, false);
            intent.putExtra(AccountEditorActivity.INTENT_ACCOUNT, account);
            DesktopItem accountItem = new DesktopItem(new IntentRun(activity, intent), account.getName(), R.drawable.dtitem_account_card);
            addItem(accountItem);
        }
    }
}
