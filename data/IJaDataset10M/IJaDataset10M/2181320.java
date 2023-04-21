package com.yingyonghui.market;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

class AssetBrowser$1 extends BroadcastReceiver {

    public void onReceive(Context paramContext, Intent paramIntent) {
        String str = paramIntent.getAction();
        if ("android.intent.action.PACKAGE_ADDED".equals(str)) AssetBrowser.access$0(this.this$0);
    }
}
