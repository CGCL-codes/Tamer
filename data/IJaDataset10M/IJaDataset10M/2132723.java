package cn.com.androidforfun.finance.ui;

import android.app.Activity;
import android.content.Intent;
import cn.com.androidforfun.finance.R;

public class MiscDesktop extends AbstractDesktop {

    public MiscDesktop(Activity activity) {
        super(activity);
    }

    @Override
    protected void init() {
        label = i18n.string(R.string.dt_misc);
        icon = R.drawable.tab_misc;
        Intent intent = new Intent(activity, PassConfigActivity.class);
        DesktopItem passConfig = new DesktopItem(new IntentRun(activity, intent), i18n.string(R.string.password_config_title), R.drawable.dtitem_prefs);
        addItem(passConfig);
        intent = new Intent(activity, PaymentsTypeListActivity.class);
        DesktopItem ptypelist = new DesktopItem(new IntentRun(activity, intent), i18n.string(R.string.dtitem_ptypelist), R.drawable.dtitem_detail_year);
        addItem(ptypelist);
        intent = new Intent(activity, DefaultDataActivity.class);
        DesktopItem defaultData = new DesktopItem(new IntentRun(activity, intent), i18n.string(R.string.dtitem_default_data), R.drawable.dtitem_datamain);
        addItem(defaultData);
        intent = new Intent(activity, ClearDataActivity.class);
        DesktopItem clearData = new DesktopItem(new IntentRun(activity, intent), i18n.string(R.string.dtitem_clear_data), R.drawable.dtitem_datamain);
        addItem(clearData);
        intent = new Intent(activity, CancelAccountListActivity.class);
        DesktopItem cancelAccount = new DesktopItem(new IntentRun(activity, intent), i18n.string(R.string.title_canceled_account), R.drawable.dtitem_datamain);
        addItem(cancelAccount);
    }
}
