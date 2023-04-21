package com.commandus.meetup;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.SubMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.AutoCompleteTextView;
import android.widget.ArrayAdapter;
import com.commandus.meetup.R;
import com.commandus.meetup.MeetupHelper;

public class ServerSettings extends Activity {

    public static final int CMD_HOST_CHANGED = 1;

    private static final int MENU_APPLY = Menu.FIRST + 1;

    private static final int MENU_CANCEL = Menu.FIRST + 2;

    private static final int MENU_PINGTIME = Menu.FIRST + 3;

    private static final int MENU_PINGTIME1 = Menu.FIRST + 4;

    private static final int MENU_PINGTIME2 = Menu.FIRST + 5;

    private static final int MENU_PINGTIME3 = Menu.FIRST + 6;

    private static final int MENU_UPDATELOCATIONTIME = Menu.FIRST + 7;

    private static final int MENU_UPDATELOCATIONTIME1 = Menu.FIRST + 8;

    private static final int MENU_UPDATELOCATIONTIME2 = Menu.FIRST + 9;

    private static final int MENU_UPDATELOCATIONTIME3 = Menu.FIRST + 10;

    private AutoCompleteTextView meHost;

    private MeetupSettings settings;

    private String mOldHost;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        settings = new MeetupSettings(this);
        setContentView(R.layout.serversettings);
        meHost = (AutoCompleteTextView) findViewById(R.id.ssHost);
        ArrayAdapter<String> mHostList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, MeetupHelper.getResourceStrings(this, R.string.ssh_hostlist));
        meHost.setAdapter(mHostList);
        meHost.setText(settings.Host);
        mOldHost = settings.Host;
        final Button BApply = (Button) findViewById(R.id.ssApply);
        BApply.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View v) {
                saveChanges();
            }
        });
        final Button BCancel = (Button) findViewById(R.id.ssCancel);
        BCancel.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View v) {
                cancelChanges();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem mi;
        mi = menu.add(0, MENU_APPLY, Menu.NONE, R.string.menu_cp_apply);
        mi.setIcon(R.drawable.mapply);
        mi.setShortcut('1', 'a');
        mi = menu.add(0, MENU_CANCEL, Menu.NONE, R.string.menu_cp_cancel);
        mi.setIcon(R.drawable.mcancel);
        mi.setShortcut('2', 'c');
        final SubMenu mMenuPingTime = menu.addSubMenu(1, MENU_PINGTIME, 0, R.string.menu_pingtime);
        mi = mMenuPingTime.add(1, MENU_PINGTIME1, Menu.NONE, R.string.menu_pingtime_1);
        mi.setShortcut('3', 'd');
        mi.setCheckable(true);
        mi = mMenuPingTime.add(1, MENU_PINGTIME2, Menu.NONE, R.string.menu_pingtime_2);
        mi.setShortcut('4', 'e');
        mi.setCheckable(true);
        mi = mMenuPingTime.add(1, MENU_PINGTIME3, Menu.NONE, R.string.menu_pingtime_3);
        mi.setShortcut('5', 'f');
        mi.setCheckable(true);
        final SubMenu mMenuUpdateLocationTime = menu.addSubMenu(2, MENU_UPDATELOCATIONTIME, 0, R.string.menu_updatelocation);
        mi = mMenuUpdateLocationTime.add(2, MENU_UPDATELOCATIONTIME1, Menu.NONE, R.string.menu_updatelocation_1);
        mi.setShortcut('6', 'g');
        mi.setCheckable(true);
        mi = mMenuUpdateLocationTime.add(2, MENU_UPDATELOCATIONTIME2, Menu.NONE, R.string.menu_updatelocation_2);
        mi.setShortcut('7', 'h');
        mi.setCheckable(true);
        mi = mMenuUpdateLocationTime.add(2, MENU_UPDATELOCATIONTIME3, Menu.NONE, R.string.menu_updatelocation_3);
        mi.setShortcut('8', 'i');
        mi.setCheckable(true);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(MENU_PINGTIME1).setChecked(settings.pingtime == MeetupSettings.PING_TIME_1);
        menu.findItem(MENU_PINGTIME2).setChecked(settings.pingtime == MeetupSettings.PING_TIME_2);
        menu.findItem(MENU_PINGTIME3).setChecked(settings.pingtime == MeetupSettings.PING_TIME_3);
        menu.findItem(MENU_UPDATELOCATIONTIME1).setChecked(settings.updatelocationtime == MeetupSettings.UPDATELOCATION_TIME_1);
        menu.findItem(MENU_UPDATELOCATIONTIME2).setChecked(settings.updatelocationtime == MeetupSettings.UPDATELOCATION_TIME_2);
        menu.findItem(MENU_UPDATELOCATIONTIME3).setChecked(settings.updatelocationtime == MeetupSettings.UPDATELOCATION_TIME_3);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case MENU_CANCEL:
                cancelChanges();
                break;
            case MENU_APPLY:
                saveChanges();
                break;
            case MENU_PINGTIME1:
                settings.pingtime = MeetupSettings.PING_TIME_1;
                break;
            case MENU_PINGTIME2:
                settings.pingtime = MeetupSettings.PING_TIME_2;
                break;
            case MENU_PINGTIME3:
                settings.pingtime = MeetupSettings.PING_TIME_3;
                break;
            case MENU_UPDATELOCATIONTIME1:
                settings.updatelocationtime = MeetupSettings.UPDATELOCATION_TIME_1;
                break;
            case MENU_UPDATELOCATIONTIME2:
                settings.updatelocationtime = MeetupSettings.UPDATELOCATION_TIME_2;
                break;
            case MENU_UPDATELOCATIONTIME3:
                settings.updatelocationtime = MeetupSettings.UPDATELOCATION_TIME_3;
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void saveChanges() {
        settings.Host = meHost.getText().toString();
        if (!settings.Host.equals(mOldHost)) {
            MeetupService.stop(this);
            MeetupService.start(this);
        }
        settings.save();
        setResult(RESULT_OK);
        finish();
    }

    /**
     * Take care of canceling work on. 
     */
    private final void cancelChanges() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
