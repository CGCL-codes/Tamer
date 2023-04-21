package com.csc531.edit;

import java.util.ArrayList;
import java.util.Calendar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.csc531.R;
import com.csc531.adapters.ManagerDbAdapter;
import com.csc531.settingsPanel.GoogleCalendar;

/**
 * @author Javier Figueroa
 * Displays the view to edit or create a new task
 *
 */
public class TasksEdit extends Activity {

    private static final String TAG = "classesedit";

    static ArrayList<String> _coursesList;

    AutoCompleteTextView _taskCourseText;

    private EditText _taskTitleText;

    private TextView _taskDueDate;

    private EditText _taskBodyText;

    private Long _rowId;

    private ManagerDbAdapter _mDbHelper;

    private int _year;

    private int _month;

    private int _day;

    private int _hour;

    private int _minute;

    static final int TIME_DIALOG_ID = 0;

    static final int DATE_DIALOG_ID = 1;

    static final int SAVE = Menu.FIRST;

    static final int GOOGLE_CALENDAR = SAVE + 1;

    private boolean _googleCalendarFailed = false;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, SAVE, 0, R.string.task_edit_menu_item1);
        menu.add(0, GOOGLE_CALENDAR, 0, R.string.task_edit_menu_item2);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
            case SAVE:
                exitActivity();
                return true;
            case GOOGLE_CALENDAR:
                saveToGoogleCalendar();
                return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    private void exitActivity() {
        setResult(RESULT_OK);
        saveState();
        finish();
    }

    private void saveToGoogleCalendar() {
        Cursor calendarSettings = _mDbHelper.FetchEmailAndPasswordAndEnable();
        startManagingCursor(calendarSettings);
        if (calendarSettings.moveToFirst() == true) {
            int enableColumnIndex = calendarSettings.getColumnIndex(ManagerDbAdapter.ENABLE_GOOGLE);
            String enabled = calendarSettings.getString(enableColumnIndex);
            if (enabled.compareTo("true") == 0 && _googleCalendarFailed == false) {
                int emailColumnIndex = calendarSettings.getColumnIndex(ManagerDbAdapter.EMAIL);
                String userName = calendarSettings.getString(emailColumnIndex);
                int passwordColumnIndex = calendarSettings.getColumnIndex(ManagerDbAdapter.PASSWORD);
                String userPassword = calendarSettings.getString(passwordColumnIndex);
                GoogleCalendar.setLogin(userName, userPassword);
                try {
                    GoogleCalendar.createEvent(_taskTitleText.getText().toString(), _taskBodyText.getText().toString(), getYear(), getMonth(), getDay());
                    Toast.makeText(TasksEdit.this, R.string.google_calendar_success, Toast.LENGTH_SHORT).show();
                    _googleCalendarFailed = false;
                } catch (Exception e) {
                    Toast.makeText(TasksEdit.this, R.string.google_calendar_failure, Toast.LENGTH_SHORT).show();
                    _googleCalendarFailed = true;
                }
            } else {
                Toast.makeText(TasksEdit.this, R.string.google_calendar_empty_creds, Toast.LENGTH_SHORT).show();
                _googleCalendarFailed = true;
            }
        }
    }

    private int getMonth() {
        String date = _taskDueDate.getText().toString();
        int month = Integer.parseInt(date.split("-", 3)[0]);
        return month - 1;
    }

    private int getDay() {
        String date = _taskDueDate.getText().toString();
        int day = Integer.parseInt(date.split("-", 3)[1]);
        return day;
    }

    private int getYear() {
        String dateText = _taskDueDate.getText().toString();
        String[] splitDate = dateText.split("-", 3);
        String date = splitDate[2].substring(0, 4);
        int year = Integer.parseInt(date);
        return year;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _mDbHelper = new ManagerDbAdapter(this);
        _mDbHelper.open();
        setContentView(R.layout.class_edit);
        populateCoursesArrayList();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, _coursesList);
        _taskCourseText = (AutoCompleteTextView) findViewById(R.id.edit);
        _taskCourseText.setAdapter(adapter);
        _taskTitleText = (EditText) findViewById(R.id.title);
        _taskDueDate = (TextView) findViewById(R.id.dateDisplay);
        displayDateField();
        _taskBodyText = (EditText) findViewById(R.id.body);
        try {
            dealWithTaskData(savedInstanceState);
        } catch (CursorIndexOutOfBoundsException ex) {
            Log.v(TAG, "Creating...nothing to show. " + ex);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(_taskCourseText.getWindowToken(), 0);
        return true;
    }

    private void dealWithTaskData(Bundle savedInstanceState) {
        _rowId = savedInstanceState != null ? savedInstanceState.getLong(ManagerDbAdapter.KEY_ROWID) : null;
        if (_rowId == null) {
            Bundle extras = getIntent().getExtras();
            _rowId = extras != null ? extras.getLong(ManagerDbAdapter.KEY_ROWID) : null;
        }
        populateFields();
    }

    private void displayDateField() {
        Button pickDate = (Button) findViewById(R.id.pickDate);
        pickDate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
        Button pickTime = (Button) findViewById(R.id.pickTime);
        pickTime.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
            }
        });
        final Calendar c = Calendar.getInstance();
        _year = c.get(Calendar.YEAR);
        _month = c.get(Calendar.MONTH);
        _day = c.get(Calendar.DAY_OF_MONTH);
        _hour = c.get(Calendar.HOUR_OF_DAY);
        _minute = c.get(Calendar.MINUTE);
        updateDisplay();
    }

    private void populateCoursesArrayList() {
        _coursesList = new ArrayList<String>();
        Cursor cursor = _mDbHelper.fetchAllCourses();
        startManagingCursor(cursor);
        for (int i = 0; i < cursor.getCount(); i++) {
            _coursesList.add(cursor.getString(0));
            cursor.moveToNext();
        }
    }

    private void populateFields() throws CursorIndexOutOfBoundsException {
        if (_rowId != null) {
            Cursor note = _mDbHelper.fetchTask(_rowId);
            startManagingCursor(note);
            note.moveToFirst();
            _taskCourseText.setText(note.getString(note.getColumnIndexOrThrow(ManagerDbAdapter.KEY_COURSE)));
            _taskTitleText.setText(note.getString(note.getColumnIndexOrThrow(ManagerDbAdapter.KEY_TITLE)));
            _taskDueDate.setText(note.getString(note.getColumnIndexOrThrow(ManagerDbAdapter.KEY_DUEDATE)));
            _taskBodyText.setText(note.getString(note.getColumnIndexOrThrow(ManagerDbAdapter.KEY_BODY)));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeConnection();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            populateFields();
        } catch (CursorIndexOutOfBoundsException ex) {
            Log.v(TAG, "Resuming... nothing to resume. " + ex);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (_rowId != null) {
            outState.putLong(ManagerDbAdapter.KEY_ROWID, _rowId);
        }
        closeConnection();
    }

    private void saveState() {
        if (isValidView() == false) {
            return;
        }
        if (_rowId == null) {
            addTaskToDatabase();
            Toast.makeText(TasksEdit.this, R.string.saved_success, Toast.LENGTH_SHORT).show();
        } else {
            updateTaskInDatabase();
            Toast.makeText(TasksEdit.this, R.string.updated_success, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidView() {
        String course = _taskCourseText.getText().toString();
        String title = _taskTitleText.getText().toString();
        String dueDate = _taskDueDate.getText().toString();
        String body = _taskBodyText.getText().toString();
        if (course.length() == 0 || title.length() == 0 || dueDate.length() == 0 || body.length() == 0) {
            Log.v(TAG, "Nothing in here returning nothing");
            return false;
        } else {
            return true;
        }
    }

    private void addTaskToDatabase() {
        String course = _taskCourseText.getText().toString();
        String title = _taskTitleText.getText().toString();
        String dueDate = _taskDueDate.getText().toString();
        String body = _taskBodyText.getText().toString();
        course = course.replaceAll(" ", "");
        Log.v(TAG, "SAVING course: " + course);
        long id = _mDbHelper.createTask(course, title, dueDate, body);
        if (id > 0) {
            _rowId = id;
        }
    }

    private void updateTaskInDatabase() {
        String course = _taskCourseText.getText().toString();
        String title = _taskTitleText.getText().toString();
        String dueDate = _taskDueDate.getText().toString();
        String body = _taskBodyText.getText().toString();
        Log.v(TAG, "UPDATING course: " + course);
        _mDbHelper.updateTask(_rowId, course, title, dueDate, body);
    }

    private void closeConnection() {
        _mDbHelper.close();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch(id) {
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this, mTimeSetListener, _hour, _minute, false);
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, mDateSetListener, _year, _month, _day);
        }
        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch(id) {
            case TIME_DIALOG_ID:
                ((TimePickerDialog) dialog).updateTime(_hour, _minute);
                break;
            case DATE_DIALOG_ID:
                ((DatePickerDialog) dialog).updateDate(_year, _month, _day);
                break;
        }
    }

    private void updateDisplay() {
        _taskDueDate.setText(new StringBuilder().append(_month + 1).append("-").append(_day).append("-").append(_year).append(" ").append(pad(_hour)).append(":").append(pad(_minute)));
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            _year = year;
            _month = monthOfYear;
            _day = dayOfMonth;
            updateDisplay();
        }
    };

    private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            _hour = hourOfDay;
            _minute = minute;
            updateDisplay();
        }
    };

    /**
	 * Convenience method used for padding date fields
	 * 
	 * @param c date integer
	 * @return
	 */
    private static String pad(int c) {
        if (c >= 10) return String.valueOf(c); else return "0" + String.valueOf(c);
    }
}
