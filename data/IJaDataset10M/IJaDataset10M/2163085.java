package net.ciklum.svnnotify;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

public class NotificationActivity extends TabActivity {

    private static final String INFO_KEY = "info";

    private static final String CHANGES_KEY = "changes";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
        setRepTitle();
        TabHost tabHost = getTabHost();
        TabHost.TabSpec spec;
        Intent intent;
        tabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);
        intent = new Intent().setClass(this, NotificationInfoActivity.class);
        spec = tabHost.newTabSpec(INFO_KEY).setContent(intent).setIndicator(createTabView(tabHost.getContext(), getString(R.string.info)));
        tabHost.addTab(spec);
        intent = new Intent().setClass(this, ChangedFilesActivity.class);
        spec = tabHost.newTabSpec(CHANGES_KEY).setContent(intent).setIndicator(createTabView(tabHost.getContext(), getString(R.string.changed_files)));
        tabHost.addTab(spec);
    }

    private static View createTabView(final Context context, final String text) {
        View view = LayoutInflater.from(context).inflate(R.layout.tabs, null);
        TextView tv = (TextView) view.findViewById(R.id.tabsText);
        tv.setText(text);
        return view;
    }

    private void setRepTitle() {
        TextView repTitle = (TextView) this.findViewById(R.id.rep_title);
        repTitle.setText("Rep name");
        TextView repUrl = (TextView) this.findViewById(R.id.rep_url);
        repUrl.setText("https://interns.googlecode.com/svn/trunk/");
    }
}
