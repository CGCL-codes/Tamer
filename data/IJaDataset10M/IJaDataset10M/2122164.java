package de.drak.Profiles;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

/**This Activity allows the user to select a task to perform when the state of a Rule changes*/
public class TaskPicker extends Activity {

    static final int TIME_DIALOG_ID = 0;

    public static final int MENU_CANCEL = Menu.FIRST;

    public static final String PARAMS_ID = "TaskPicker.id";

    public static final String TASK_DO = "TaskPicker.do";

    public static final String TASK_INK = "TaskPicker.ink";

    private final int PROF_PICKER = 1;

    private final int COLOR_DEF = Color.argb(255, 255, 255, 255);

    private final int COLOR_SEL = Color.argb(255, 0, 0, 0);

    /**What to do*/
    private int doit;

    /**do it using what*/
    private long ink;

    private boolean isCancelled = false;

    TableLayout dady;

    private AlertDialog mDialog;

    private void MarkSelected(View v) {
        v.setBackgroundResource(android.R.drawable.bottom_bar);
        ((TextView) ((ViewGroup) v).getChildAt(1)).setTextColor(COLOR_SEL);
    }

    private void UnMarkSelected(View v) {
        v.setBackgroundColor(0);
        ((TextView) ((ViewGroup) v).getChildAt(1)).setTextColor(COLOR_DEF);
    }

    public static String createTaskTitle(Resources c, Database.RULE_TASKS task) {
        switch(task) {
            case START_PROFILE:
                return c.getString(R.string.taskSTART_PROFILE);
            case END_PROFILE:
                return c.getString(R.string.taskEND_PROFILE);
            case DISCARD_ALL_PROFILES:
                return c.getString(R.string.taskDISCARD_ALL_PROFILES);
            case LAUNCH_APP:
                return c.getString(R.string.taskLAUNCH_APP);
            case NOTIFY:
                return c.getString(R.string.taskNOTIFY);
            case BIND_PROFILE:
                return c.getString(R.string.taskBIND_PROFILE);
            case DISABLE_SCANNING:
                return c.getString(R.string.taskDISABLE_SCANNING);
        }
        return null;
    }

    public static Drawable createTaskIcon(Resources c, Database.RULE_TASKS task) {
        switch(task) {
            case START_PROFILE:
                return c.getDrawable(R.drawable.start_p);
            case END_PROFILE:
                return c.getDrawable(R.drawable.end_p);
            case DISCARD_ALL_PROFILES:
                return c.getDrawable(R.drawable.wipe);
            case LAUNCH_APP:
                return c.getDrawable(R.drawable.start);
            case NOTIFY:
                return c.getDrawable(R.drawable.warn);
            case BIND_PROFILE:
                return c.getDrawable(R.drawable.icon);
            case DISABLE_SCANNING:
                return c.getDrawable(R.drawable.disable_scan);
        }
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mDialog != null) mDialog.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mDialog != null) mDialog.dismiss();
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDialog = null;
        isCancelled = true;
        int max = Database.RULE_TASKS.DISABLE_SCANNING.ordinal();
        int i = 0;
        ScrollView scroller = new ScrollView(this);
        dady = new TableLayout(this);
        dady.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT));
        dady.setColumnStretchable(1, true);
        scroller.addView(dady);
        ImageView icon = null;
        TextView text = null;
        TableRow row = null;
        while (i <= max) {
            Database.RULE_TASKS blup = Database.IntToTask(i);
            row = new TableRow(this);
            icon = new ImageView(this);
            icon.setAdjustViewBounds(true);
            icon.setScaleType(ImageView.ScaleType.FIT_CENTER);
            icon.setMaxHeight(TimedProfiles.max_icon_h);
            icon.setMinimumHeight(TimedProfiles.min_icon_h);
            icon.setImageDrawable(createTaskIcon(getResources(), blup));
            icon.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            text = new TextView(this);
            text.setText(createTaskTitle(getResources(), blup));
            text.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            text.setTextColor(COLOR_DEF);
            if (i == Database.RULE_TASKS.LAUNCH_APP.ordinal()) {
                text.setTextColor(Color.argb(200, 100, 100, 100));
            }
            row.addView(icon);
            row.addView(text);
            row.setTag(String.format("%d", i));
            row.setOnClickListener(ItemSelected);
            dady.addView(row);
            i++;
        }
        if (getIntent().hasExtra(PARAMS_ID)) {
            doit = getIntent().getIntExtra(PARAMS_ID, 0);
            MarkSelected(dady.getChildAt(doit));
            switch(Database.IntToTask(doit)) {
                case START_PROFILE:
                case BIND_PROFILE:
                case END_PROFILE:
                    Intent data = new Intent(), shortcutIntent = new Intent();
                    shortcutIntent.putExtra(ShortCutHandler.SHORTCUT_PROFILE_EXTRA_ID, getIntent().getLongExtra(TASK_INK, -1));
                    data.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
                    onActivityResult(PROF_PICKER, RESULT_OK, data);
                    break;
                case DISABLE_SCANNING:
                    ink = getIntent().getLongExtra(TASK_INK, 0);
                    TextView t = (TextView) ((ViewGroup) dady.getChildAt(doit)).getChildAt(1);
                    t.setText(String.format("%s\n%s", t.getText(), getString(R.string.taskDISABLE_SCANNING_desc, (int) (ink / 60000) / 60, (int) (ink / 60000) % 60)));
                    break;
                case LAUNCH_APP:
                    break;
            }
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        mDialog = builder.setCancelable(true).setOnCancelListener(new DialogInterface.OnCancelListener() {

            public void onCancel(DialogInterface dialog) {
                finish();
            }
        }).setView(scroller).setTitle(R.string.task_add).create();
    }

    private void startProfPicker() {
        ink = -1;
        Intent start = new Intent(this, ShortCutHandler.class);
        start.setAction("android.intent.action.CREATE_SHORTCUT");
        startActivityForResult(start, PROF_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PROF_PICKER && resultCode == RESULT_OK) {
            Intent shortcutIntent = data.getParcelableExtra(Intent.EXTRA_SHORTCUT_INTENT);
            ink = shortcutIntent.getLongExtra(ShortCutHandler.SHORTCUT_PROFILE_EXTRA_ID, 0);
            Database db = new Database(this);
            String str = "";
            db.open();
            Cursor pro = db.fetchProfile(ink);
            if (pro != null) {
                str = pro.getString(pro.getColumnIndex(Database.P_KEY_NAME));
                pro.close();
            }
            db.close();
            TextView t = (TextView) ((ViewGroup) dady.getChildAt(doit)).getChildAt(1);
            t.setText(t.getText() + "\n" + str);
        }
    }

    @Override
    public void finish() {
        if (isCancelled || ink == -1) setResult(RESULT_CANCELED); else {
            Intent result = new Intent();
            result.putExtra(TASK_INK, ink);
            result.putExtra(TASK_DO, doit);
            setResult(RESULT_OK, result);
        }
        super.finish();
    }

    private OnClickListener ItemSelected = new OnClickListener() {

        public void onClick(View v) {
            int i = Integer.parseInt((String) v.getTag());
            UnMarkSelected(dady.getChildAt(doit));
            doit = i;
            MarkSelected(dady.getChildAt(doit));
            TextView t = (TextView) ((ViewGroup) dady.getChildAt(doit)).getChildAt(1);
            String s = String.format("%s", t.getText());
            final int x = s.indexOf("\n");
            if (x != -1) {
                t.setText(s.subSequence(0, x));
            }
            isCancelled = false;
            switch(Database.IntToTask(i)) {
                case START_PROFILE:
                    startProfPicker();
                    break;
                case END_PROFILE:
                    startProfPicker();
                    break;
                case DISCARD_ALL_PROFILES:
                    ink = 0;
                    break;
                case DISABLE_SCANNING:
                    showDialog(TIME_DIALOG_ID);
                    break;
                case LAUNCH_APP:
                    Toast.makeText(v.getContext(), "BETA: Comming soon...", Toast.LENGTH_SHORT).show();
                    isCancelled = true;
                    break;
                case NOTIFY:
                    break;
                case BIND_PROFILE:
                    startProfPicker();
                    break;
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);
        final Intent helpIntent = new Intent(this, OnlineHelp.class);
        helpIntent.putExtra(OnlineHelp.HELP_URL, OnlineHelp.HELP_URL_PRE + "pick_task");
        helpIntent.putExtra(TimedProfiles.INTENT_TITLE, getTitle().toString());
        menu.add(R.string.onlinehelp).setIcon(android.R.drawable.ic_menu_help).setIntent(helpIntent);
        menu.add(0, MENU_CANCEL, 0, android.R.string.cancel).setIcon(android.R.drawable.ic_menu_close_clear_cancel).getItemId();
        return true;
    }

    /**
		 * {@inheritDoc}
		 */
    @Override
    public boolean onMenuItemSelected(final int featureId, final MenuItem item) {
        switch(item.getItemId()) {
            case MENU_CANCEL:
                {
                    isCancelled = true;
                    finish();
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    class TimePickerDlg extends TimePickerDialog {

        @Override
        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
            super.onTimeChanged(view, hourOfDay, minute);
            setTitle(R.string.taskDISABLE_SCANNING);
        }

        public TimePickerDlg(Context context, OnTimeSetListener callBack, long in) {
            super(context, callBack, (int) (in / 60000) / 60, (int) (in / 60000) % 60, true);
            setTitle(R.string.taskDISABLE_SCANNING);
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch(id) {
            case TIME_DIALOG_ID:
                return new TimePickerDlg(this, mTimeSetListener, ink);
        }
        return null;
    }

    private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            TextView t = (TextView) ((ViewGroup) dady.getChildAt(doit)).getChildAt(1);
            t.setText(String.format("%s\n%s", t.getText(), getString(R.string.taskDISABLE_SCANNING_desc, hourOfDay, minute)));
            ink = hourOfDay * 60 + minute;
            ink = ink * 60 * 1000;
        }
    };
}
