package com.yingyonghui.market;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;

class FilteredAppListActivity$1 extends BroadcastReceiver {

    public void onReceive(Context paramContext, Intent paramIntent) {
        if ((paramIntent.getAction().equals("broadcast_search_request")) && (FilteredAppListActivity.access$0(this.this$0) == 56)) {
            FilteredAppListActivity localFilteredAppListActivity1 = this.this$0;
            String str1 = paramIntent.getStringExtra("keyword");
            FilteredAppListActivity.access$1(localFilteredAppListActivity1, str1);
            FilteredAppListActivity localFilteredAppListActivity2 = this.this$0;
            String str2 = paramIntent.getStringExtra("title");
            FilteredAppListActivity.access$2(localFilteredAppListActivity2, str2);
            FilteredAppListActivity localFilteredAppListActivity3 = this.this$0;
            int i = paramIntent.getIntExtra("searchType", 3);
            FilteredAppListActivity.access$3(localFilteredAppListActivity3, i);
            FilteredAppListActivity localFilteredAppListActivity4 = this.this$0;
            int j = paramIntent.getIntExtra("source", -1);
            FilteredAppListActivity.access$4(localFilteredAppListActivity4, j);
            FilteredAppListActivity.access$5(this.this$0, 0);
            FilteredAppListActivity.access$6(this.this$0, -1);
            FilteredAppListActivity.access$7(this.this$0, 0);
            if (FilteredAppListActivity.access$8(this.this$0) != null) FilteredAppListActivity.access$8(this.this$0).clear();
            FilteredAppListActivity.access$9(this.this$0).setVisibility(0);
            FilteredAppListActivity.access$10(this.this$0);
            Intent localIntent = new Intent("show_search_result");
            this.this$0.sendBroadcast(localIntent);
        }
        while (true) {
            return;
            if (FilteredAppListActivity.access$8(this.this$0) != null) {
                FilteredAppListActivity.access$8(this.this$0).notifyDataSetChanged();
                continue;
            }
        }
    }
}
