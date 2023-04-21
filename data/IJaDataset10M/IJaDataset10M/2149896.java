package com.android.calendar;

import static android.provider.Calendar.EVENT_BEGIN_TIME;
import static android.provider.Calendar.EVENT_END_TIME;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.AsyncQueryHandler;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.content.ContentProviderOperation.Builder;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.pim.EventRecurrence;
import android.preference.PreferenceManager;
import android.provider.Calendar.Attendees;
import android.provider.Calendar.Calendars;
import android.provider.Calendar.Events;
import android.provider.Calendar.Reminders;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.text.util.Rfc822InputFilter;
import android.text.util.Rfc822Token;
import android.text.util.Rfc822Tokenizer;
import android.text.util.Rfc822Validator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ResourceCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.google.android.googlelogin.GoogleLoginServiceConstants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.TimeZone;

public class EditEvent extends Activity implements View.OnClickListener, DialogInterface.OnCancelListener, DialogInterface.OnClickListener {

    private static final String TAG = "EditEvent";

    private static final boolean DEBUG = false;

    /**
     * This is the symbolic name for the key used to pass in the boolean
     * for creating all-day events that is part of the extra data of the intent.
     * This is used only for creating new events and is set to true if
     * the default for the new event should be an all-day event.
     */
    public static final String EVENT_ALL_DAY = "allDay";

    private static final int MAX_REMINDERS = 5;

    private static final int MENU_GROUP_REMINDER = 1;

    private static final int MENU_GROUP_SHOW_OPTIONS = 2;

    private static final int MENU_GROUP_HIDE_OPTIONS = 3;

    private static final int MENU_ADD_REMINDER = 1;

    private static final int MENU_SHOW_EXTRA_OPTIONS = 2;

    private static final int MENU_HIDE_EXTRA_OPTIONS = 3;

    private static final String[] EVENT_PROJECTION = new String[] { Events._ID, Events.TITLE, Events.DESCRIPTION, Events.EVENT_LOCATION, Events.ALL_DAY, Events.HAS_ALARM, Events.CALENDAR_ID, Events.DTSTART, Events.DURATION, Events.EVENT_TIMEZONE, Events.RRULE, Events._SYNC_ID, Events.TRANSPARENCY, Events.VISIBILITY, Events.OWNER_ACCOUNT, Events.HAS_ATTENDEE_DATA };

    private static final int EVENT_INDEX_ID = 0;

    private static final int EVENT_INDEX_TITLE = 1;

    private static final int EVENT_INDEX_DESCRIPTION = 2;

    private static final int EVENT_INDEX_EVENT_LOCATION = 3;

    private static final int EVENT_INDEX_ALL_DAY = 4;

    private static final int EVENT_INDEX_HAS_ALARM = 5;

    private static final int EVENT_INDEX_CALENDAR_ID = 6;

    private static final int EVENT_INDEX_DTSTART = 7;

    private static final int EVENT_INDEX_DURATION = 8;

    private static final int EVENT_INDEX_TIMEZONE = 9;

    private static final int EVENT_INDEX_RRULE = 10;

    private static final int EVENT_INDEX_SYNC_ID = 11;

    private static final int EVENT_INDEX_TRANSPARENCY = 12;

    private static final int EVENT_INDEX_VISIBILITY = 13;

    private static final int EVENT_INDEX_OWNER_ACCOUNT = 14;

    private static final int EVENT_INDEX_HAS_ATTENDEE_DATA = 15;

    private static final String[] CALENDARS_PROJECTION = new String[] { Calendars._ID, Calendars.DISPLAY_NAME, Calendars.OWNER_ACCOUNT };

    private static final int CALENDARS_INDEX_DISPLAY_NAME = 1;

    private static final int CALENDARS_INDEX_OWNER_ACCOUNT = 2;

    private static final String CALENDARS_WHERE = Calendars.ACCESS_LEVEL + ">=" + Calendars.CONTRIBUTOR_ACCESS + " AND " + Calendars.SYNC_EVENTS + "=1";

    private static final String[] REMINDERS_PROJECTION = new String[] { Reminders._ID, Reminders.MINUTES };

    private static final int REMINDERS_INDEX_MINUTES = 1;

    private static final String REMINDERS_WHERE = Reminders.EVENT_ID + "=%d AND (" + Reminders.METHOD + "=" + Reminders.METHOD_ALERT + " OR " + Reminders.METHOD + "=" + Reminders.METHOD_DEFAULT + ")";

    private static final String[] ATTENDEES_PROJECTION = new String[] { Attendees.ATTENDEE_NAME, Attendees.ATTENDEE_EMAIL };

    private static final int ATTENDEES_INDEX_NAME = 0;

    private static final int ATTENDEES_INDEX_EMAIL = 1;

    private static final String ATTENDEES_WHERE = Attendees.EVENT_ID + "=? AND " + Attendees.ATTENDEE_RELATIONSHIP + "<>" + Attendees.RELATIONSHIP_ORGANIZER;

    private static final String ATTENDEES_DELETE_PREFIX = Attendees.EVENT_ID + "=? AND " + Attendees.ATTENDEE_EMAIL + " IN (";

    private static final int DOES_NOT_REPEAT = 0;

    private static final int REPEATS_DAILY = 1;

    private static final int REPEATS_EVERY_WEEKDAY = 2;

    private static final int REPEATS_WEEKLY_ON_DAY = 3;

    private static final int REPEATS_MONTHLY_ON_DAY_COUNT = 4;

    private static final int REPEATS_MONTHLY_ON_DAY = 5;

    private static final int REPEATS_YEARLY = 6;

    private static final int REPEATS_CUSTOM = 7;

    private static final int MODIFY_UNINITIALIZED = 0;

    private static final int MODIFY_SELECTED = 1;

    private static final int MODIFY_ALL = 2;

    private static final int MODIFY_ALL_FOLLOWING = 3;

    private static final int DAY_IN_SECONDS = 24 * 60 * 60;

    private int mFirstDayOfWeek;

    private Uri mUri;

    private Cursor mEventCursor;

    private Cursor mCalendarsCursor;

    private Button mStartDateButton;

    private Button mEndDateButton;

    private Button mStartTimeButton;

    private Button mEndTimeButton;

    private Button mSaveButton;

    private Button mDeleteButton;

    private Button mDiscardButton;

    private CheckBox mAllDayCheckBox;

    private Spinner mCalendarsSpinner;

    private Spinner mRepeatsSpinner;

    private Spinner mAvailabilitySpinner;

    private Spinner mVisibilitySpinner;

    private TextView mTitleTextView;

    private TextView mLocationTextView;

    private TextView mDescriptionTextView;

    private View mRemindersSeparator;

    private LinearLayout mRemindersContainer;

    private LinearLayout mExtraOptions;

    private ArrayList<Integer> mOriginalMinutes = new ArrayList<Integer>();

    private ArrayList<LinearLayout> mReminderItems = new ArrayList<LinearLayout>(0);

    private Rfc822Validator mEmailValidator;

    private MultiAutoCompleteTextView mAttendeesList;

    private EmailAddressAdapter mAddressAdapter;

    private String mOriginalAttendees = "";

    private boolean mHasAttendeeData = true;

    private EventRecurrence mEventRecurrence = new EventRecurrence();

    private String mRrule;

    private boolean mCalendarsQueryComplete;

    private boolean mSaveAfterQueryComplete;

    private ProgressDialog mLoadingCalendarsDialog;

    private AlertDialog mNoCalendarsDialog;

    private ContentValues mInitialValues;

    private String mOwnerAccount;

    /**
     * If the repeating event is created on the phone and it hasn't been
     * synced yet to the web server, then there is a bug where you can't
     * delete or change an instance of the repeating event.  This case
     * can be detected with mSyncId.  If mSyncId == null, then the repeating
     * event has not been synced to the phone, in which case we won't allow
     * the user to change one instance.
     */
    private String mSyncId;

    private ArrayList<Integer> mRecurrenceIndexes = new ArrayList<Integer>(0);

    private ArrayList<Integer> mReminderValues;

    private ArrayList<String> mReminderLabels;

    private Time mStartTime;

    private Time mEndTime;

    private int mModification = MODIFY_UNINITIALIZED;

    private int mDefaultReminderMinutes;

    private DeleteEventHelper mDeleteEventHelper;

    private QueryHandler mQueryHandler;

    private AccountManager mAccountManager;

    private class TimeListener implements OnTimeSetListener {

        private View mView;

        public TimeListener(View view) {
            mView = view;
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Time startTime = mStartTime;
            Time endTime = mEndTime;
            long startMillis;
            long endMillis;
            if (mView == mStartTimeButton) {
                int hourDuration = endTime.hour - startTime.hour;
                int minuteDuration = endTime.minute - startTime.minute;
                startTime.hour = hourOfDay;
                startTime.minute = minute;
                startMillis = startTime.normalize(true);
                endTime.hour = hourOfDay + hourDuration;
                endTime.minute = minute + minuteDuration;
                endMillis = endTime.normalize(true);
            } else {
                startMillis = startTime.toMillis(true);
                endTime.hour = hourOfDay;
                endTime.minute = minute;
                endMillis = endTime.normalize(true);
                if (endTime.before(startTime)) {
                    endTime.set(startTime);
                    endMillis = startMillis;
                }
            }
            setDate(mEndDateButton, endMillis);
            setTime(mStartTimeButton, startMillis);
            setTime(mEndTimeButton, endMillis);
        }
    }

    private class TimeClickListener implements View.OnClickListener {

        private Time mTime;

        public TimeClickListener(Time time) {
            mTime = time;
        }

        public void onClick(View v) {
            new TimePickerDialog(EditEvent.this, new TimeListener(v), mTime.hour, mTime.minute, DateFormat.is24HourFormat(EditEvent.this)).show();
        }
    }

    private class DateListener implements OnDateSetListener {

        View mView;

        public DateListener(View view) {
            mView = view;
        }

        public void onDateSet(DatePicker view, int year, int month, int monthDay) {
            Time startTime = mStartTime;
            Time endTime = mEndTime;
            long startMillis;
            long endMillis;
            if (mView == mStartDateButton) {
                int yearDuration = endTime.year - startTime.year;
                int monthDuration = endTime.month - startTime.month;
                int monthDayDuration = endTime.monthDay - startTime.monthDay;
                startTime.year = year;
                startTime.month = month;
                startTime.monthDay = monthDay;
                startMillis = startTime.normalize(true);
                endTime.year = year + yearDuration;
                endTime.month = month + monthDuration;
                endTime.monthDay = monthDay + monthDayDuration;
                endMillis = endTime.normalize(true);
                populateRepeats();
            } else {
                startMillis = startTime.toMillis(true);
                endTime.year = year;
                endTime.month = month;
                endTime.monthDay = monthDay;
                endMillis = endTime.normalize(true);
                if (endTime.before(startTime)) {
                    endTime.set(startTime);
                    endMillis = startMillis;
                }
            }
            setDate(mStartDateButton, startMillis);
            setDate(mEndDateButton, endMillis);
            setTime(mEndTimeButton, endMillis);
        }
    }

    private class DateClickListener implements View.OnClickListener {

        private Time mTime;

        public DateClickListener(Time time) {
            mTime = time;
        }

        public void onClick(View v) {
            new DatePickerDialog(EditEvent.this, new DateListener(v), mTime.year, mTime.month, mTime.monthDay).show();
        }
    }

    private static class CalendarsAdapter extends ResourceCursorAdapter {

        public CalendarsAdapter(Context context, Cursor c) {
            super(context, R.layout.calendars_item, c);
            setDropDownViewResource(R.layout.calendars_dropdown_item);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView name = (TextView) view.findViewById(R.id.calendar_name);
            name.setText(cursor.getString(CALENDARS_INDEX_DISPLAY_NAME));
        }
    }

    public void onClick(View v) {
        if (v == mSaveButton) {
            if (save()) {
                finish();
            }
            return;
        }
        if (v == mDeleteButton) {
            long begin = mStartTime.toMillis(false);
            long end = mEndTime.toMillis(false);
            int which = -1;
            switch(mModification) {
                case MODIFY_SELECTED:
                    which = DeleteEventHelper.DELETE_SELECTED;
                    break;
                case MODIFY_ALL_FOLLOWING:
                    which = DeleteEventHelper.DELETE_ALL_FOLLOWING;
                    break;
                case MODIFY_ALL:
                    which = DeleteEventHelper.DELETE_ALL;
                    break;
            }
            mDeleteEventHelper.delete(begin, end, mEventCursor, which);
            return;
        }
        if (v == mDiscardButton) {
            finish();
            return;
        }
        LinearLayout reminderItem = (LinearLayout) v.getParent();
        LinearLayout parent = (LinearLayout) reminderItem.getParent();
        parent.removeView(reminderItem);
        mReminderItems.remove(reminderItem);
        updateRemindersVisibility();
    }

    public void onCancel(DialogInterface dialog) {
        if (dialog == mLoadingCalendarsDialog) {
            mSaveAfterQueryComplete = false;
        } else if (dialog == mNoCalendarsDialog) {
            finish();
        }
    }

    public void onClick(DialogInterface dialog, int which) {
        if (dialog == mNoCalendarsDialog) {
            finish();
        }
    }

    private class QueryHandler extends AsyncQueryHandler {

        public QueryHandler(ContentResolver cr) {
            super(cr);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            if (isFinishing()) {
                stopManagingCursor(cursor);
                cursor.close();
            } else {
                mCalendarsCursor = cursor;
                startManagingCursor(cursor);
                getWindow().setFeatureInt(Window.FEATURE_INDETERMINATE_PROGRESS, Window.PROGRESS_VISIBILITY_OFF);
                if (cursor.getCount() == 0) {
                    if (mSaveAfterQueryComplete) {
                        mLoadingCalendarsDialog.cancel();
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditEvent.this);
                    builder.setTitle(R.string.no_syncable_calendars).setIcon(android.R.drawable.ic_dialog_alert).setMessage(R.string.no_calendars_found).setPositiveButton(android.R.string.ok, EditEvent.this).setOnCancelListener(EditEvent.this);
                    mNoCalendarsDialog = builder.show();
                    return;
                }
                int primaryCalendarPosition = findPrimaryCalendarPosition();
                CalendarsAdapter adapter = new CalendarsAdapter(EditEvent.this, mCalendarsCursor);
                mCalendarsSpinner.setAdapter(adapter);
                mCalendarsSpinner.setSelection(primaryCalendarPosition);
                mCalendarsQueryComplete = true;
                if (mSaveAfterQueryComplete) {
                    mLoadingCalendarsDialog.cancel();
                    save();
                    finish();
                }
                if (mHasAttendeeData && cursor.moveToPosition(primaryCalendarPosition)) {
                    String ownEmail = cursor.getString(CALENDARS_INDEX_OWNER_ACCOUNT);
                    if (ownEmail != null) {
                        String domain = extractDomain(ownEmail);
                        if (domain != null) {
                            mEmailValidator = new Rfc822Validator(domain);
                            mAttendeesList.setValidator(mEmailValidator);
                        }
                    }
                }
            }
        }

        private int findPrimaryCalendarPosition() {
            int primaryCalendarPosition = -1;
            try {
                Account[] accounts = mAccountManager.getAccountsByTypeAndFeatures(GoogleLoginServiceConstants.ACCOUNT_TYPE, new String[] { GoogleLoginServiceConstants.FEATURE_LEGACY_HOSTED_OR_GOOGLE }, null, null).getResult();
                if (accounts.length > 0) {
                    for (int i = 0; i < accounts.length && primaryCalendarPosition == -1; ++i) {
                        String name = accounts[i].name;
                        if (name == null) {
                            continue;
                        }
                        int position = 0;
                        mCalendarsCursor.moveToPosition(-1);
                        while (mCalendarsCursor.moveToNext()) {
                            if (name.equals(mCalendarsCursor.getString(CALENDARS_INDEX_OWNER_ACCOUNT))) {
                                primaryCalendarPosition = position;
                                break;
                            }
                            position++;
                        }
                    }
                }
            } catch (OperationCanceledException e) {
                Log.w(TAG, "Ignoring unexpected exception", e);
            } catch (IOException e) {
                Log.w(TAG, "Ignoring unexpected exception", e);
            } catch (AuthenticatorException e) {
                Log.w(TAG, "Ignoring unexpected exception", e);
            } finally {
                if (primaryCalendarPosition != -1) {
                    return primaryCalendarPosition;
                } else {
                    return 0;
                }
            }
        }
    }

    private static String extractDomain(String email) {
        int separator = email.lastIndexOf('@');
        if (separator != -1 && ++separator < email.length()) {
            return email.substring(separator);
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.edit_event);
        mAccountManager = AccountManager.get(this);
        boolean newEvent = false;
        mFirstDayOfWeek = Calendar.getInstance().getFirstDayOfWeek();
        mStartTime = new Time();
        mEndTime = new Time();
        Intent intent = getIntent();
        mUri = intent.getData();
        if (mUri != null) {
            mEventCursor = managedQuery(mUri, EVENT_PROJECTION, null, null);
            if (mEventCursor == null || mEventCursor.getCount() == 0) {
                finish();
                return;
            }
        }
        long begin = intent.getLongExtra(EVENT_BEGIN_TIME, 0);
        long end = intent.getLongExtra(EVENT_END_TIME, 0);
        String domain = "gmail.com";
        boolean allDay = false;
        if (mEventCursor != null) {
            mEventCursor.moveToFirst();
            mHasAttendeeData = mEventCursor.getInt(EVENT_INDEX_HAS_ATTENDEE_DATA) != 0;
            allDay = mEventCursor.getInt(EVENT_INDEX_ALL_DAY) != 0;
            String rrule = mEventCursor.getString(EVENT_INDEX_RRULE);
            String timezone = mEventCursor.getString(EVENT_INDEX_TIMEZONE);
            long calendarId = mEventCursor.getInt(EVENT_INDEX_CALENDAR_ID);
            mOwnerAccount = mEventCursor.getString(EVENT_INDEX_OWNER_ACCOUNT);
            if (!TextUtils.isEmpty(mOwnerAccount)) {
                String ownerDomain = extractDomain(mOwnerAccount);
                if (ownerDomain != null) {
                    domain = ownerDomain;
                }
            }
            mInitialValues = new ContentValues();
            mInitialValues.put(EVENT_BEGIN_TIME, begin);
            mInitialValues.put(EVENT_END_TIME, end);
            mInitialValues.put(Events.ALL_DAY, allDay ? 1 : 0);
            mInitialValues.put(Events.RRULE, rrule);
            mInitialValues.put(Events.EVENT_TIMEZONE, timezone);
            mInitialValues.put(Events.CALENDAR_ID, calendarId);
        } else {
            newEvent = true;
            allDay = intent.getBooleanExtra(EVENT_ALL_DAY, false);
            getWindow().setFeatureInt(Window.FEATURE_INDETERMINATE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);
            mQueryHandler = new QueryHandler(getContentResolver());
            mQueryHandler.startQuery(0, null, Calendars.CONTENT_URI, CALENDARS_PROJECTION, CALENDARS_WHERE, null, null);
        }
        if (begin != 0) {
            if (allDay) {
                String tz = mStartTime.timezone;
                mStartTime.timezone = Time.TIMEZONE_UTC;
                mStartTime.set(begin);
                mStartTime.timezone = tz;
                mStartTime.normalize(true);
            } else {
                mStartTime.set(begin);
            }
        }
        if (end != 0) {
            if (allDay) {
                String tz = mStartTime.timezone;
                mEndTime.timezone = Time.TIMEZONE_UTC;
                mEndTime.set(end);
                mEndTime.timezone = tz;
                mEndTime.normalize(true);
            } else {
                mEndTime.set(end);
            }
        }
        mTitleTextView = (TextView) findViewById(R.id.title);
        mLocationTextView = (TextView) findViewById(R.id.location);
        mDescriptionTextView = (TextView) findViewById(R.id.description);
        mStartDateButton = (Button) findViewById(R.id.start_date);
        mEndDateButton = (Button) findViewById(R.id.end_date);
        mStartTimeButton = (Button) findViewById(R.id.start_time);
        mEndTimeButton = (Button) findViewById(R.id.end_time);
        mAllDayCheckBox = (CheckBox) findViewById(R.id.is_all_day);
        mCalendarsSpinner = (Spinner) findViewById(R.id.calendars);
        mRepeatsSpinner = (Spinner) findViewById(R.id.repeats);
        mAvailabilitySpinner = (Spinner) findViewById(R.id.availability);
        mVisibilitySpinner = (Spinner) findViewById(R.id.visibility);
        mRemindersSeparator = findViewById(R.id.reminders_separator);
        mRemindersContainer = (LinearLayout) findViewById(R.id.reminder_items_container);
        mExtraOptions = (LinearLayout) findViewById(R.id.extra_options_container);
        if (mHasAttendeeData) {
            mAddressAdapter = new EmailAddressAdapter(this);
            mEmailValidator = new Rfc822Validator(domain);
            mAttendeesList = initMultiAutoCompleteTextView(R.id.attendees);
        } else {
            findViewById(R.id.attendees_group).setVisibility(View.GONE);
        }
        mAllDayCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (mEndTime.hour == 0 && mEndTime.minute == 0) {
                        mEndTime.monthDay--;
                        long endMillis = mEndTime.normalize(true);
                        if (mEndTime.before(mStartTime)) {
                            mEndTime.set(mStartTime);
                            endMillis = mEndTime.normalize(true);
                        }
                        setDate(mEndDateButton, endMillis);
                        setTime(mEndTimeButton, endMillis);
                    }
                    mStartTimeButton.setVisibility(View.GONE);
                    mEndTimeButton.setVisibility(View.GONE);
                } else {
                    if (mEndTime.hour == 0 && mEndTime.minute == 0) {
                        mEndTime.monthDay++;
                        long endMillis = mEndTime.normalize(true);
                        setDate(mEndDateButton, endMillis);
                        setTime(mEndTimeButton, endMillis);
                    }
                    mStartTimeButton.setVisibility(View.VISIBLE);
                    mEndTimeButton.setVisibility(View.VISIBLE);
                }
            }
        });
        if (allDay) {
            mAllDayCheckBox.setChecked(true);
        } else {
            mAllDayCheckBox.setChecked(false);
        }
        mSaveButton = (Button) findViewById(R.id.save);
        mSaveButton.setOnClickListener(this);
        mDeleteButton = (Button) findViewById(R.id.delete);
        mDeleteButton.setOnClickListener(this);
        mDiscardButton = (Button) findViewById(R.id.discard);
        mDiscardButton.setOnClickListener(this);
        Resources r = getResources();
        String[] strings = r.getStringArray(R.array.reminder_minutes_values);
        int size = strings.length;
        ArrayList<Integer> list = new ArrayList<Integer>(size);
        for (int i = 0; i < size; i++) {
            list.add(Integer.parseInt(strings[i]));
        }
        mReminderValues = list;
        String[] labels = r.getStringArray(R.array.reminder_minutes_labels);
        mReminderLabels = new ArrayList<String>(Arrays.asList(labels));
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String durationString = prefs.getString(CalendarPreferenceActivity.KEY_DEFAULT_REMINDER, "0");
        mDefaultReminderMinutes = Integer.parseInt(durationString);
        if (newEvent && mDefaultReminderMinutes != 0) {
            addReminder(this, this, mReminderItems, mReminderValues, mReminderLabels, mDefaultReminderMinutes);
        }
        long eventId = (mEventCursor == null) ? -1 : mEventCursor.getLong(EVENT_INDEX_ID);
        ContentResolver cr = getContentResolver();
        boolean hasAlarm = (mEventCursor != null) && (mEventCursor.getInt(EVENT_INDEX_HAS_ALARM) != 0);
        if (hasAlarm) {
            Uri uri = Reminders.CONTENT_URI;
            String where = String.format(REMINDERS_WHERE, eventId);
            Cursor reminderCursor = cr.query(uri, REMINDERS_PROJECTION, where, null, null);
            try {
                while (reminderCursor.moveToNext()) {
                    int minutes = reminderCursor.getInt(REMINDERS_INDEX_MINUTES);
                    EditEvent.addMinutesToList(this, mReminderValues, mReminderLabels, minutes);
                }
                reminderCursor.moveToPosition(-1);
                while (reminderCursor.moveToNext()) {
                    int minutes = reminderCursor.getInt(REMINDERS_INDEX_MINUTES);
                    mOriginalMinutes.add(minutes);
                    EditEvent.addReminder(this, this, mReminderItems, mReminderValues, mReminderLabels, minutes);
                }
            } finally {
                reminderCursor.close();
            }
        }
        updateRemindersVisibility();
        View.OnClickListener addReminderOnClickListener = new View.OnClickListener() {

            public void onClick(View v) {
                addReminder();
            }
        };
        ImageButton reminderRemoveButton = (ImageButton) findViewById(R.id.reminder_add);
        reminderRemoveButton.setOnClickListener(addReminderOnClickListener);
        mDeleteEventHelper = new DeleteEventHelper(this, true);
        if (mHasAttendeeData && eventId != -1) {
            Uri uri = Attendees.CONTENT_URI;
            String[] whereArgs = { Long.toString(eventId) };
            Cursor attendeeCursor = cr.query(uri, ATTENDEES_PROJECTION, ATTENDEES_WHERE, whereArgs, null);
            try {
                StringBuilder b = new StringBuilder();
                while (attendeeCursor.moveToNext()) {
                    String name = attendeeCursor.getString(ATTENDEES_INDEX_NAME);
                    String email = attendeeCursor.getString(ATTENDEES_INDEX_EMAIL);
                    if (email != null) {
                        if (name != null && name.length() > 0 && !name.equals(email)) {
                            b.append('"').append(name).append("\" ");
                        }
                        b.append('<').append(email).append(">, ");
                    }
                }
                if (b.length() > 0) {
                    mOriginalAttendees = b.toString();
                    mAttendeesList.setText(mOriginalAttendees);
                }
            } finally {
                attendeeCursor.close();
            }
        }
        if (mEventCursor == null) {
            initFromIntent(intent);
        }
    }

    private LinkedHashSet<Rfc822Token> getAddressesFromList(MultiAutoCompleteTextView list) {
        list.clearComposingText();
        LinkedHashSet<Rfc822Token> addresses = new LinkedHashSet<Rfc822Token>();
        Rfc822Tokenizer.tokenize(list.getText(), addresses);
        for (Rfc822Token address : addresses) {
            if (!mEmailValidator.isValid(address.getAddress())) {
                Log.w(TAG, "Dropping invalid attendee email address: " + address);
                addresses.remove(address);
            }
        }
        return addresses;
    }

    private MultiAutoCompleteTextView initMultiAutoCompleteTextView(int res) {
        MultiAutoCompleteTextView list = (MultiAutoCompleteTextView) findViewById(res);
        list.setAdapter(mAddressAdapter);
        list.setTokenizer(new Rfc822Tokenizer());
        list.setValidator(mEmailValidator);
        list.setFilters(sRecipientFilters);
        return list;
    }

    /**
     * From com.google.android.gm.ComposeActivity
     * Implements special address cleanup rules:
     * The first space key entry following an "@" symbol that is followed by any combination
     * of letters and symbols, including one+ dots and zero commas, should insert an extra
     * comma (followed by the space).
     */
    private static InputFilter[] sRecipientFilters = new InputFilter[] { new Rfc822InputFilter() };

    private void initFromIntent(Intent intent) {
        String title = intent.getStringExtra(Events.TITLE);
        if (title != null) {
            mTitleTextView.setText(title);
        }
        String location = intent.getStringExtra(Events.EVENT_LOCATION);
        if (location != null) {
            mLocationTextView.setText(location);
        }
        String description = intent.getStringExtra(Events.DESCRIPTION);
        if (description != null) {
            mDescriptionTextView.setText(description);
        }
        int availability = intent.getIntExtra(Events.TRANSPARENCY, -1);
        if (availability != -1) {
            mAvailabilitySpinner.setSelection(availability);
        }
        int visibility = intent.getIntExtra(Events.VISIBILITY, -1);
        if (visibility != -1) {
            mVisibilitySpinner.setSelection(visibility);
        }
        String rrule = intent.getStringExtra(Events.RRULE);
        if (rrule != null) {
            mRrule = rrule;
            mEventRecurrence.parse(rrule);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mUri != null) {
            if (mEventCursor == null || mEventCursor.getCount() == 0) {
                finish();
                return;
            }
        }
        if (mEventCursor != null) {
            Cursor cursor = mEventCursor;
            cursor.moveToFirst();
            mRrule = cursor.getString(EVENT_INDEX_RRULE);
            String title = cursor.getString(EVENT_INDEX_TITLE);
            String description = cursor.getString(EVENT_INDEX_DESCRIPTION);
            String location = cursor.getString(EVENT_INDEX_EVENT_LOCATION);
            int availability = cursor.getInt(EVENT_INDEX_TRANSPARENCY);
            int visibility = cursor.getInt(EVENT_INDEX_VISIBILITY);
            if (visibility > 0) {
                visibility--;
            }
            if (!TextUtils.isEmpty(mRrule) && mModification == MODIFY_UNINITIALIZED) {
                mSyncId = cursor.getString(EVENT_INDEX_SYNC_ID);
                mEventRecurrence.parse(mRrule);
                int itemIndex = 0;
                CharSequence[] items;
                if (mSyncId == null) {
                    items = new CharSequence[2];
                } else {
                    items = new CharSequence[3];
                    items[itemIndex++] = getText(R.string.modify_event);
                }
                items[itemIndex++] = getText(R.string.modify_all);
                items[itemIndex++] = getText(R.string.modify_all_following);
                new AlertDialog.Builder(this).setOnCancelListener(new OnCancelListener() {

                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                }).setTitle(R.string.edit_event_label).setItems(items, new OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            mModification = (mSyncId == null) ? MODIFY_ALL : MODIFY_SELECTED;
                        } else if (which == 1) {
                            mModification = (mSyncId == null) ? MODIFY_ALL_FOLLOWING : MODIFY_ALL;
                        } else if (which == 2) {
                            mModification = MODIFY_ALL_FOLLOWING;
                        }
                        if (mModification == MODIFY_ALL) {
                            mStartDateButton.setEnabled(false);
                            mEndDateButton.setEnabled(false);
                        } else if (mModification == MODIFY_SELECTED) {
                            mRepeatsSpinner.setEnabled(false);
                        }
                    }
                }).show();
            }
            mTitleTextView.setText(title);
            mLocationTextView.setText(location);
            mDescriptionTextView.setText(description);
            mAvailabilitySpinner.setSelection(availability);
            mVisibilitySpinner.setSelection(visibility);
            View calendarGroup = findViewById(R.id.calendar_group);
            calendarGroup.setVisibility(View.GONE);
        } else {
            if (Time.isEpoch(mStartTime) && Time.isEpoch(mEndTime)) {
                mStartTime.setToNow();
                mStartTime.second = 0;
                int minute = mStartTime.minute;
                if (minute > 0 && minute <= 30) {
                    mStartTime.minute = 30;
                } else {
                    mStartTime.minute = 0;
                    mStartTime.hour += 1;
                }
                long startMillis = mStartTime.normalize(true);
                mEndTime.set(startMillis + DateUtils.HOUR_IN_MILLIS);
            }
            mDeleteButton.setVisibility(View.GONE);
        }
        updateRemindersVisibility();
        populateWhen();
        populateRepeats();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item;
        item = menu.add(MENU_GROUP_REMINDER, MENU_ADD_REMINDER, 0, R.string.add_new_reminder);
        item.setIcon(R.drawable.ic_menu_reminder);
        item.setAlphabeticShortcut('r');
        item = menu.add(MENU_GROUP_SHOW_OPTIONS, MENU_SHOW_EXTRA_OPTIONS, 0, R.string.edit_event_show_extra_options);
        item.setIcon(R.drawable.ic_menu_show_list);
        item = menu.add(MENU_GROUP_HIDE_OPTIONS, MENU_HIDE_EXTRA_OPTIONS, 0, R.string.edit_event_hide_extra_options);
        item.setIcon(R.drawable.ic_menu_show_list);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mReminderItems.size() < MAX_REMINDERS) {
            menu.setGroupVisible(MENU_GROUP_REMINDER, true);
            menu.setGroupEnabled(MENU_GROUP_REMINDER, true);
        } else {
            menu.setGroupVisible(MENU_GROUP_REMINDER, false);
            menu.setGroupEnabled(MENU_GROUP_REMINDER, false);
        }
        if (mExtraOptions.getVisibility() == View.VISIBLE) {
            menu.setGroupVisible(MENU_GROUP_SHOW_OPTIONS, false);
            menu.setGroupVisible(MENU_GROUP_HIDE_OPTIONS, true);
        } else {
            menu.setGroupVisible(MENU_GROUP_SHOW_OPTIONS, true);
            menu.setGroupVisible(MENU_GROUP_HIDE_OPTIONS, false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void addReminder() {
        if (mDefaultReminderMinutes == 0) {
            addReminder(this, this, mReminderItems, mReminderValues, mReminderLabels, 10);
        } else {
            addReminder(this, this, mReminderItems, mReminderValues, mReminderLabels, mDefaultReminderMinutes);
        }
        updateRemindersVisibility();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case MENU_ADD_REMINDER:
                addReminder();
                return true;
            case MENU_SHOW_EXTRA_OPTIONS:
                mExtraOptions.setVisibility(View.VISIBLE);
                return true;
            case MENU_HIDE_EXTRA_OPTIONS:
                mExtraOptions.setVisibility(View.GONE);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mUri != null || !isEmpty()) {
            if (!save()) {
                return;
            }
        }
        finish();
    }

    private void populateWhen() {
        long startMillis = mStartTime.toMillis(false);
        long endMillis = mEndTime.toMillis(false);
        setDate(mStartDateButton, startMillis);
        setDate(mEndDateButton, endMillis);
        setTime(mStartTimeButton, startMillis);
        setTime(mEndTimeButton, endMillis);
        mStartDateButton.setOnClickListener(new DateClickListener(mStartTime));
        mEndDateButton.setOnClickListener(new DateClickListener(mEndTime));
        mStartTimeButton.setOnClickListener(new TimeClickListener(mStartTime));
        mEndTimeButton.setOnClickListener(new TimeClickListener(mEndTime));
    }

    private void populateRepeats() {
        Time time = mStartTime;
        Resources r = getResources();
        int resource = android.R.layout.simple_spinner_item;
        String[] days = new String[] { DateUtils.getDayOfWeekString(Calendar.SUNDAY, DateUtils.LENGTH_MEDIUM), DateUtils.getDayOfWeekString(Calendar.MONDAY, DateUtils.LENGTH_MEDIUM), DateUtils.getDayOfWeekString(Calendar.TUESDAY, DateUtils.LENGTH_MEDIUM), DateUtils.getDayOfWeekString(Calendar.WEDNESDAY, DateUtils.LENGTH_MEDIUM), DateUtils.getDayOfWeekString(Calendar.THURSDAY, DateUtils.LENGTH_MEDIUM), DateUtils.getDayOfWeekString(Calendar.FRIDAY, DateUtils.LENGTH_MEDIUM), DateUtils.getDayOfWeekString(Calendar.SATURDAY, DateUtils.LENGTH_MEDIUM) };
        String[] ordinals = r.getStringArray(R.array.ordinal_labels);
        boolean isCustomRecurrence = isCustomRecurrence();
        boolean isWeekdayEvent = isWeekdayEvent();
        ArrayList<String> repeatArray = new ArrayList<String>(0);
        ArrayList<Integer> recurrenceIndexes = new ArrayList<Integer>(0);
        repeatArray.add(r.getString(R.string.does_not_repeat));
        recurrenceIndexes.add(DOES_NOT_REPEAT);
        repeatArray.add(r.getString(R.string.daily));
        recurrenceIndexes.add(REPEATS_DAILY);
        if (isWeekdayEvent) {
            repeatArray.add(r.getString(R.string.every_weekday));
            recurrenceIndexes.add(REPEATS_EVERY_WEEKDAY);
        }
        String format = r.getString(R.string.weekly);
        repeatArray.add(String.format(format, time.format("%A")));
        recurrenceIndexes.add(REPEATS_WEEKLY_ON_DAY);
        int dayNumber = (time.monthDay - 1) / 7;
        format = r.getString(R.string.monthly_on_day_count);
        repeatArray.add(String.format(format, ordinals[dayNumber], days[time.weekDay]));
        recurrenceIndexes.add(REPEATS_MONTHLY_ON_DAY_COUNT);
        format = r.getString(R.string.monthly_on_day);
        repeatArray.add(String.format(format, time.monthDay));
        recurrenceIndexes.add(REPEATS_MONTHLY_ON_DAY);
        long when = time.toMillis(false);
        format = r.getString(R.string.yearly);
        int flags = 0;
        if (DateFormat.is24HourFormat(this)) {
            flags |= DateUtils.FORMAT_24HOUR;
        }
        repeatArray.add(String.format(format, DateUtils.formatDateTime(this, when, flags)));
        recurrenceIndexes.add(REPEATS_YEARLY);
        if (isCustomRecurrence) {
            repeatArray.add(r.getString(R.string.custom));
            recurrenceIndexes.add(REPEATS_CUSTOM);
        }
        mRecurrenceIndexes = recurrenceIndexes;
        int position = recurrenceIndexes.indexOf(DOES_NOT_REPEAT);
        if (mRrule != null) {
            if (isCustomRecurrence) {
                position = recurrenceIndexes.indexOf(REPEATS_CUSTOM);
            } else {
                switch(mEventRecurrence.freq) {
                    case EventRecurrence.DAILY:
                        position = recurrenceIndexes.indexOf(REPEATS_DAILY);
                        break;
                    case EventRecurrence.WEEKLY:
                        if (mEventRecurrence.repeatsOnEveryWeekDay()) {
                            position = recurrenceIndexes.indexOf(REPEATS_EVERY_WEEKDAY);
                        } else {
                            position = recurrenceIndexes.indexOf(REPEATS_WEEKLY_ON_DAY);
                        }
                        break;
                    case EventRecurrence.MONTHLY:
                        if (mEventRecurrence.repeatsMonthlyOnDayCount()) {
                            position = recurrenceIndexes.indexOf(REPEATS_MONTHLY_ON_DAY_COUNT);
                        } else {
                            position = recurrenceIndexes.indexOf(REPEATS_MONTHLY_ON_DAY);
                        }
                        break;
                    case EventRecurrence.YEARLY:
                        position = recurrenceIndexes.indexOf(REPEATS_YEARLY);
                        break;
                }
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, resource, repeatArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRepeatsSpinner.setAdapter(adapter);
        mRepeatsSpinner.setSelection(position);
    }

    static boolean addReminder(Activity activity, View.OnClickListener listener, ArrayList<LinearLayout> items, ArrayList<Integer> values, ArrayList<String> labels, int minutes) {
        if (items.size() >= MAX_REMINDERS) {
            return false;
        }
        LayoutInflater inflater = activity.getLayoutInflater();
        LinearLayout parent = (LinearLayout) activity.findViewById(R.id.reminder_items_container);
        LinearLayout reminderItem = (LinearLayout) inflater.inflate(R.layout.edit_reminder_item, null);
        parent.addView(reminderItem);
        Spinner spinner = (Spinner) reminderItem.findViewById(R.id.reminder_value);
        Resources res = activity.getResources();
        spinner.setPrompt(res.getString(R.string.reminders_label));
        int resource = android.R.layout.simple_spinner_item;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, resource, labels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        ImageButton reminderRemoveButton;
        reminderRemoveButton = (ImageButton) reminderItem.findViewById(R.id.reminder_remove);
        reminderRemoveButton.setOnClickListener(listener);
        int index = findMinutesInReminderList(values, minutes);
        spinner.setSelection(index);
        items.add(reminderItem);
        return true;
    }

    static void addMinutesToList(Context context, ArrayList<Integer> values, ArrayList<String> labels, int minutes) {
        int index = values.indexOf(minutes);
        if (index != -1) {
            return;
        }
        String label = constructReminderLabel(context, minutes, false);
        int len = values.size();
        for (int i = 0; i < len; i++) {
            if (minutes < values.get(i)) {
                values.add(i, minutes);
                labels.add(i, label);
                return;
            }
        }
        values.add(minutes);
        labels.add(len, label);
    }

    /**
     * Finds the index of the given "minutes" in the "values" list.
     *
     * @param values the list of minutes corresponding to the spinner choices
     * @param minutes the minutes to search for in the values list
     * @return the index of "minutes" in the "values" list
     */
    private static int findMinutesInReminderList(ArrayList<Integer> values, int minutes) {
        int index = values.indexOf(minutes);
        if (index == -1) {
            Log.e("Cal", "Cannot find minutes (" + minutes + ") in list");
            return 0;
        }
        return index;
    }

    static String constructReminderLabel(Context context, int minutes, boolean abbrev) {
        Resources resources = context.getResources();
        int value, resId;
        if (minutes % 60 != 0) {
            value = minutes;
            if (abbrev) {
                resId = R.plurals.Nmins;
            } else {
                resId = R.plurals.Nminutes;
            }
        } else if (minutes % (24 * 60) != 0) {
            value = minutes / 60;
            resId = R.plurals.Nhours;
        } else {
            value = minutes / (24 * 60);
            resId = R.plurals.Ndays;
        }
        String format = resources.getQuantityString(resId, value);
        return String.format(format, value);
    }

    private void updateRemindersVisibility() {
        if (mReminderItems.size() == 0) {
            mRemindersSeparator.setVisibility(View.GONE);
            mRemindersContainer.setVisibility(View.GONE);
        } else {
            mRemindersSeparator.setVisibility(View.VISIBLE);
            mRemindersContainer.setVisibility(View.VISIBLE);
        }
    }

    private void setDate(TextView view, long millis) {
        int flags = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_ABBREV_MONTH | DateUtils.FORMAT_ABBREV_WEEKDAY;
        view.setText(DateUtils.formatDateTime(this, millis, flags));
    }

    private void setTime(TextView view, long millis) {
        int flags = DateUtils.FORMAT_SHOW_TIME;
        if (DateFormat.is24HourFormat(this)) {
            flags |= DateUtils.FORMAT_24HOUR;
        }
        view.setText(DateUtils.formatDateTime(this, millis, flags));
    }

    private boolean save() {
        boolean forceSaveReminders = false;
        if (mEventCursor == null) {
            if (!mCalendarsQueryComplete) {
                if (mLoadingCalendarsDialog == null) {
                    mLoadingCalendarsDialog = ProgressDialog.show(this, getText(R.string.loading_calendars_title), getText(R.string.loading_calendars_message), true, true, this);
                    mSaveAfterQueryComplete = true;
                }
                return false;
            }
            if (mCalendarsCursor == null || mCalendarsCursor.getCount() == 0) {
                Log.w("Cal", "The calendars table does not contain any calendars." + " New event was not created.");
                return true;
            }
            Toast.makeText(this, R.string.creating_event, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.saving_event, Toast.LENGTH_SHORT).show();
        }
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        int eventIdIndex = -1;
        ContentValues values = getContentValuesFromUi();
        Uri uri = mUri;
        ArrayList<Integer> reminderMinutes = reminderItemsToMinutes(mReminderItems, mReminderValues);
        int len = reminderMinutes.size();
        values.put(Events.HAS_ALARM, (len > 0) ? 1 : 0);
        if (uri == null) {
            values.put(Events.HAS_ATTENDEE_DATA, 1);
            addRecurrenceRule(values);
            eventIdIndex = ops.size();
            Builder b = ContentProviderOperation.newInsert(Events.CONTENT_URI).withValues(values);
            ops.add(b.build());
            forceSaveReminders = true;
        } else if (mRrule == null) {
            addRecurrenceRule(values);
            checkTimeDependentFields(values);
            ops.add(ContentProviderOperation.newUpdate(uri).withValues(values).build());
        } else if (mInitialValues.getAsString(Events.RRULE) == null) {
            addRecurrenceRule(values);
            values.remove(Events.DTEND);
            ops.add(ContentProviderOperation.newUpdate(uri).withValues(values).build());
        } else if (mModification == MODIFY_SELECTED) {
            long begin = mInitialValues.getAsLong(EVENT_BEGIN_TIME);
            values.put(Events.ORIGINAL_EVENT, mEventCursor.getString(EVENT_INDEX_SYNC_ID));
            values.put(Events.ORIGINAL_INSTANCE_TIME, begin);
            boolean allDay = mInitialValues.getAsInteger(Events.ALL_DAY) != 0;
            values.put(Events.ORIGINAL_ALL_DAY, allDay ? 1 : 0);
            eventIdIndex = ops.size();
            Builder b = ContentProviderOperation.newInsert(Events.CONTENT_URI).withValues(values);
            ops.add(b.build());
            forceSaveReminders = true;
        } else if (mModification == MODIFY_ALL_FOLLOWING) {
            addRecurrenceRule(values);
            if (mRrule == null) {
                if (isFirstEventInSeries()) {
                    ops.add(ContentProviderOperation.newDelete(uri).build());
                } else {
                    updatePastEvents(ops, uri);
                }
                eventIdIndex = ops.size();
                ops.add(ContentProviderOperation.newInsert(Events.CONTENT_URI).withValues(values).build());
            } else {
                if (isFirstEventInSeries()) {
                    checkTimeDependentFields(values);
                    values.remove(Events.DTEND);
                    Builder b = ContentProviderOperation.newUpdate(uri).withValues(values);
                    ops.add(b.build());
                } else {
                    updatePastEvents(ops, uri);
                    values.remove(Events.DTEND);
                    eventIdIndex = ops.size();
                    ops.add(ContentProviderOperation.newInsert(Events.CONTENT_URI).withValues(values).build());
                }
            }
            forceSaveReminders = true;
        } else if (mModification == MODIFY_ALL) {
            addRecurrenceRule(values);
            if (mRrule == null) {
                ops.add(ContentProviderOperation.newDelete(uri).build());
                eventIdIndex = ops.size();
                ops.add(ContentProviderOperation.newInsert(Events.CONTENT_URI).withValues(values).build());
                forceSaveReminders = true;
            } else {
                checkTimeDependentFields(values);
                values.remove(Events.DTEND);
                ops.add(ContentProviderOperation.newUpdate(uri).withValues(values).build());
            }
        }
        boolean newEvent = (eventIdIndex != -1);
        if (newEvent) {
            saveRemindersWithBackRef(ops, eventIdIndex, reminderMinutes, mOriginalMinutes, forceSaveReminders);
        } else if (uri != null) {
            long eventId = ContentUris.parseId(uri);
            saveReminders(ops, eventId, reminderMinutes, mOriginalMinutes, forceSaveReminders);
        }
        Builder b;
        if (mHasAttendeeData && newEvent) {
            values.clear();
            int calendarCursorPosition = mCalendarsSpinner.getSelectedItemPosition();
            String ownerEmail = mOwnerAccount;
            if (ownerEmail == null && mCalendarsCursor != null && mCalendarsCursor.moveToPosition(calendarCursorPosition)) {
                ownerEmail = mCalendarsCursor.getString(CALENDARS_INDEX_OWNER_ACCOUNT);
            }
            if (ownerEmail != null) {
                values.put(Attendees.ATTENDEE_EMAIL, ownerEmail);
                values.put(Attendees.ATTENDEE_RELATIONSHIP, Attendees.RELATIONSHIP_ORGANIZER);
                values.put(Attendees.ATTENDEE_TYPE, Attendees.TYPE_NONE);
                values.put(Attendees.ATTENDEE_STATUS, Attendees.ATTENDEE_STATUS_ACCEPTED);
                b = ContentProviderOperation.newInsert(Attendees.CONTENT_URI).withValues(values);
                b.withValueBackReference(Reminders.EVENT_ID, eventIdIndex);
                ops.add(b.build());
            }
        }
        if (mHasAttendeeData && (newEvent || uri != null)) {
            Editable attendeesText = mAttendeesList.getText();
            if (newEvent || !mOriginalAttendees.equals(attendeesText.toString())) {
                LinkedHashSet<Rfc822Token> newAttendees = getAddressesFromList(mAttendeesList);
                long eventId = uri != null ? ContentUris.parseId(uri) : -1;
                if (!newEvent) {
                    HashSet<Rfc822Token> removedAttendees = new HashSet<Rfc822Token>();
                    HashSet<Rfc822Token> originalAttendees = new HashSet<Rfc822Token>();
                    Rfc822Tokenizer.tokenize(mOriginalAttendees, originalAttendees);
                    for (Rfc822Token originalAttendee : originalAttendees) {
                        if (newAttendees.contains(originalAttendee)) {
                            newAttendees.remove(originalAttendee);
                        } else {
                            removedAttendees.add(originalAttendee);
                        }
                    }
                    b = ContentProviderOperation.newDelete(Attendees.CONTENT_URI);
                    String[] args = new String[removedAttendees.size() + 1];
                    args[0] = Long.toString(eventId);
                    int i = 1;
                    StringBuilder deleteWhere = new StringBuilder(ATTENDEES_DELETE_PREFIX);
                    for (Rfc822Token removedAttendee : removedAttendees) {
                        if (i > 1) {
                            deleteWhere.append(",");
                        }
                        deleteWhere.append("?");
                        args[i++] = removedAttendee.getAddress();
                    }
                    deleteWhere.append(")");
                    b.withSelection(deleteWhere.toString(), args);
                    ops.add(b.build());
                }
                if (newAttendees.size() > 0) {
                    for (Rfc822Token attendee : newAttendees) {
                        values.clear();
                        values.put(Attendees.ATTENDEE_NAME, attendee.getName());
                        values.put(Attendees.ATTENDEE_EMAIL, attendee.getAddress());
                        values.put(Attendees.ATTENDEE_RELATIONSHIP, Attendees.RELATIONSHIP_ATTENDEE);
                        values.put(Attendees.ATTENDEE_TYPE, Attendees.TYPE_NONE);
                        values.put(Attendees.ATTENDEE_STATUS, Attendees.ATTENDEE_STATUS_NONE);
                        if (newEvent) {
                            b = ContentProviderOperation.newInsert(Attendees.CONTENT_URI).withValues(values);
                            b.withValueBackReference(Attendees.EVENT_ID, eventIdIndex);
                        } else {
                            values.put(Attendees.EVENT_ID, eventId);
                            b = ContentProviderOperation.newInsert(Attendees.CONTENT_URI).withValues(values);
                        }
                        ops.add(b.build());
                    }
                }
            }
        }
        try {
            ContentProviderResult[] results = getContentResolver().applyBatch(android.provider.Calendar.AUTHORITY, ops);
            if (DEBUG) {
                for (int i = 0; i < results.length; i++) {
                    Log.v(TAG, "results = " + results[i].toString());
                }
            }
        } catch (RemoteException e) {
            Log.w(TAG, "Ignoring unexpected remote exception", e);
        } catch (OperationApplicationException e) {
            Log.w(TAG, "Ignoring unexpected exception", e);
        }
        return true;
    }

    private boolean isFirstEventInSeries() {
        int dtStart = mEventCursor.getColumnIndexOrThrow(Events.DTSTART);
        long start = mEventCursor.getLong(dtStart);
        return start == mStartTime.toMillis(true);
    }

    private void updatePastEvents(ArrayList<ContentProviderOperation> ops, Uri uri) {
        long oldStartMillis = mEventCursor.getLong(EVENT_INDEX_DTSTART);
        String oldDuration = mEventCursor.getString(EVENT_INDEX_DURATION);
        boolean allDay = mEventCursor.getInt(EVENT_INDEX_ALL_DAY) != 0;
        String oldRrule = mEventCursor.getString(EVENT_INDEX_RRULE);
        mEventRecurrence.parse(oldRrule);
        Time untilTime = new Time();
        long begin = mInitialValues.getAsLong(EVENT_BEGIN_TIME);
        ContentValues oldValues = new ContentValues();
        untilTime.timezone = Time.TIMEZONE_UTC;
        untilTime.set(begin - 1000);
        if (allDay) {
            untilTime.hour = 0;
            untilTime.minute = 0;
            untilTime.second = 0;
            untilTime.allDay = true;
            untilTime.normalize(false);
            int len = oldDuration.length();
            if (oldDuration.charAt(0) == 'P' && oldDuration.charAt(len - 1) == 'S') {
                int seconds = Integer.parseInt(oldDuration.substring(1, len - 1));
                int days = (seconds + DAY_IN_SECONDS - 1) / DAY_IN_SECONDS;
                oldDuration = "P" + days + "D";
            }
        }
        mEventRecurrence.until = untilTime.format2445();
        oldValues.put(Events.DTSTART, oldStartMillis);
        oldValues.put(Events.DURATION, oldDuration);
        oldValues.put(Events.RRULE, mEventRecurrence.toString());
        Builder b = ContentProviderOperation.newUpdate(uri).withValues(oldValues);
        ops.add(b.build());
    }

    private void checkTimeDependentFields(ContentValues values) {
        long oldBegin = mInitialValues.getAsLong(EVENT_BEGIN_TIME);
        long oldEnd = mInitialValues.getAsLong(EVENT_END_TIME);
        boolean oldAllDay = mInitialValues.getAsInteger(Events.ALL_DAY) != 0;
        String oldRrule = mInitialValues.getAsString(Events.RRULE);
        String oldTimezone = mInitialValues.getAsString(Events.EVENT_TIMEZONE);
        long newBegin = values.getAsLong(Events.DTSTART);
        long newEnd = values.getAsLong(Events.DTEND);
        boolean newAllDay = values.getAsInteger(Events.ALL_DAY) != 0;
        String newRrule = values.getAsString(Events.RRULE);
        String newTimezone = values.getAsString(Events.EVENT_TIMEZONE);
        if (oldBegin == newBegin && oldEnd == newEnd && oldAllDay == newAllDay && TextUtils.equals(oldRrule, newRrule) && TextUtils.equals(oldTimezone, newTimezone)) {
            values.remove(Events.DTSTART);
            values.remove(Events.DTEND);
            values.remove(Events.DURATION);
            values.remove(Events.ALL_DAY);
            values.remove(Events.RRULE);
            values.remove(Events.EVENT_TIMEZONE);
            return;
        }
        if (oldRrule == null || newRrule == null) {
            return;
        }
        if (mModification == MODIFY_ALL) {
            long oldStartMillis = mEventCursor.getLong(EVENT_INDEX_DTSTART);
            if (oldBegin != newBegin) {
                long offset = newBegin - oldBegin;
                oldStartMillis += offset;
            }
            values.put(Events.DTSTART, oldStartMillis);
        }
    }

    static ArrayList<Integer> reminderItemsToMinutes(ArrayList<LinearLayout> reminderItems, ArrayList<Integer> reminderValues) {
        int len = reminderItems.size();
        ArrayList<Integer> reminderMinutes = new ArrayList<Integer>(len);
        for (int index = 0; index < len; index++) {
            LinearLayout layout = reminderItems.get(index);
            Spinner spinner = (Spinner) layout.findViewById(R.id.reminder_value);
            int minutes = reminderValues.get(spinner.getSelectedItemPosition());
            reminderMinutes.add(minutes);
        }
        return reminderMinutes;
    }

    /**
     * Saves the reminders, if they changed.  Returns true if the database
     * was updated.
     *
     * @param ops the array of ContentProviderOperations
     * @param eventId the id of the event whose reminders are being updated
     * @param reminderMinutes the array of reminders set by the user
     * @param originalMinutes the original array of reminders
     * @param forceSave if true, then save the reminders even if they didn't
     *   change
     * @return true if the database was updated
     */
    static boolean saveReminders(ArrayList<ContentProviderOperation> ops, long eventId, ArrayList<Integer> reminderMinutes, ArrayList<Integer> originalMinutes, boolean forceSave) {
        if (reminderMinutes.equals(originalMinutes) && !forceSave) {
            return false;
        }
        String where = Reminders.EVENT_ID + "=?";
        String[] args = new String[] { Long.toString(eventId) };
        Builder b = ContentProviderOperation.newDelete(Reminders.CONTENT_URI);
        b.withSelection(where, args);
        ops.add(b.build());
        ContentValues values = new ContentValues();
        int len = reminderMinutes.size();
        for (int i = 0; i < len; i++) {
            int minutes = reminderMinutes.get(i);
            values.clear();
            values.put(Reminders.MINUTES, minutes);
            values.put(Reminders.METHOD, Reminders.METHOD_ALERT);
            values.put(Reminders.EVENT_ID, eventId);
            b = ContentProviderOperation.newInsert(Reminders.CONTENT_URI).withValues(values);
            ops.add(b.build());
        }
        return true;
    }

    static boolean saveRemindersWithBackRef(ArrayList<ContentProviderOperation> ops, int eventIdIndex, ArrayList<Integer> reminderMinutes, ArrayList<Integer> originalMinutes, boolean forceSave) {
        if (reminderMinutes.equals(originalMinutes) && !forceSave) {
            return false;
        }
        Builder b = ContentProviderOperation.newDelete(Reminders.CONTENT_URI);
        b.withSelection(Reminders.EVENT_ID + "=?", new String[1]);
        b.withSelectionBackReference(0, eventIdIndex);
        ops.add(b.build());
        ContentValues values = new ContentValues();
        int len = reminderMinutes.size();
        for (int i = 0; i < len; i++) {
            int minutes = reminderMinutes.get(i);
            values.clear();
            values.put(Reminders.MINUTES, minutes);
            values.put(Reminders.METHOD, Reminders.METHOD_ALERT);
            b = ContentProviderOperation.newInsert(Reminders.CONTENT_URI).withValues(values);
            b.withValueBackReference(Reminders.EVENT_ID, eventIdIndex);
            ops.add(b.build());
        }
        return true;
    }

    private void addRecurrenceRule(ContentValues values) {
        updateRecurrenceRule();
        if (mRrule == null) {
            return;
        }
        values.put(Events.RRULE, mRrule);
        long end = mEndTime.toMillis(true);
        long start = mStartTime.toMillis(true);
        String duration;
        boolean isAllDay = mAllDayCheckBox.isChecked();
        if (isAllDay) {
            long days = (end - start + DateUtils.DAY_IN_MILLIS - 1) / DateUtils.DAY_IN_MILLIS;
            duration = "P" + days + "D";
        } else {
            long seconds = (end - start) / DateUtils.SECOND_IN_MILLIS;
            duration = "P" + seconds + "S";
        }
        values.put(Events.DURATION, duration);
    }

    private void updateRecurrenceRule() {
        int position = mRepeatsSpinner.getSelectedItemPosition();
        int selection = mRecurrenceIndexes.get(position);
        if (selection == DOES_NOT_REPEAT) {
            mRrule = null;
            return;
        } else if (selection == REPEATS_CUSTOM) {
            return;
        } else if (selection == REPEATS_DAILY) {
            mEventRecurrence.freq = EventRecurrence.DAILY;
        } else if (selection == REPEATS_EVERY_WEEKDAY) {
            mEventRecurrence.freq = EventRecurrence.WEEKLY;
            int dayCount = 5;
            int[] byday = new int[dayCount];
            int[] bydayNum = new int[dayCount];
            byday[0] = EventRecurrence.MO;
            byday[1] = EventRecurrence.TU;
            byday[2] = EventRecurrence.WE;
            byday[3] = EventRecurrence.TH;
            byday[4] = EventRecurrence.FR;
            for (int day = 0; day < dayCount; day++) {
                bydayNum[day] = 0;
            }
            mEventRecurrence.byday = byday;
            mEventRecurrence.bydayNum = bydayNum;
            mEventRecurrence.bydayCount = dayCount;
        } else if (selection == REPEATS_WEEKLY_ON_DAY) {
            mEventRecurrence.freq = EventRecurrence.WEEKLY;
            int[] days = new int[1];
            int dayCount = 1;
            int[] dayNum = new int[dayCount];
            days[0] = EventRecurrence.timeDay2Day(mStartTime.weekDay);
            dayNum[0] = 0;
            mEventRecurrence.byday = days;
            mEventRecurrence.bydayNum = dayNum;
            mEventRecurrence.bydayCount = dayCount;
        } else if (selection == REPEATS_MONTHLY_ON_DAY) {
            mEventRecurrence.freq = EventRecurrence.MONTHLY;
            mEventRecurrence.bydayCount = 0;
            mEventRecurrence.bymonthdayCount = 1;
            int[] bymonthday = new int[1];
            bymonthday[0] = mStartTime.monthDay;
            mEventRecurrence.bymonthday = bymonthday;
        } else if (selection == REPEATS_MONTHLY_ON_DAY_COUNT) {
            mEventRecurrence.freq = EventRecurrence.MONTHLY;
            mEventRecurrence.bydayCount = 1;
            mEventRecurrence.bymonthdayCount = 0;
            int[] byday = new int[1];
            int[] bydayNum = new int[1];
            int dayCount = 1 + ((mStartTime.monthDay - 1) / 7);
            if (dayCount == 5) {
                dayCount = -1;
            }
            bydayNum[0] = dayCount;
            byday[0] = EventRecurrence.timeDay2Day(mStartTime.weekDay);
            mEventRecurrence.byday = byday;
            mEventRecurrence.bydayNum = bydayNum;
        } else if (selection == REPEATS_YEARLY) {
            mEventRecurrence.freq = EventRecurrence.YEARLY;
        }
        mEventRecurrence.wkst = EventRecurrence.calendarDay2Day(mFirstDayOfWeek);
        mRrule = mEventRecurrence.toString();
    }

    private ContentValues getContentValuesFromUi() {
        String title = mTitleTextView.getText().toString();
        boolean isAllDay = mAllDayCheckBox.isChecked();
        String location = mLocationTextView.getText().toString();
        String description = mDescriptionTextView.getText().toString();
        ContentValues values = new ContentValues();
        String timezone = null;
        long startMillis;
        long endMillis;
        long calendarId;
        if (isAllDay) {
            timezone = Time.TIMEZONE_UTC;
            mStartTime.hour = 0;
            mStartTime.minute = 0;
            mStartTime.second = 0;
            mStartTime.timezone = timezone;
            startMillis = mStartTime.normalize(true);
            mEndTime.hour = 0;
            mEndTime.minute = 0;
            mEndTime.second = 0;
            mEndTime.monthDay++;
            mEndTime.timezone = timezone;
            endMillis = mEndTime.normalize(true);
            if (mEventCursor == null) {
                calendarId = mCalendarsSpinner.getSelectedItemId();
            } else {
                calendarId = mInitialValues.getAsLong(Events.CALENDAR_ID);
            }
        } else {
            startMillis = mStartTime.toMillis(true);
            endMillis = mEndTime.toMillis(true);
            if (mEventCursor != null) {
                timezone = mEventCursor.getString(EVENT_INDEX_TIMEZONE);
                if (TextUtils.isEmpty(timezone)) {
                    timezone = TimeZone.getDefault().getID();
                }
                calendarId = mInitialValues.getAsLong(Events.CALENDAR_ID);
            } else {
                calendarId = mCalendarsSpinner.getSelectedItemId();
                timezone = TimeZone.getDefault().getID();
            }
        }
        values.put(Events.CALENDAR_ID, calendarId);
        values.put(Events.EVENT_TIMEZONE, timezone);
        values.put(Events.TITLE, title);
        values.put(Events.ALL_DAY, isAllDay ? 1 : 0);
        values.put(Events.DTSTART, startMillis);
        values.put(Events.DTEND, endMillis);
        values.put(Events.DESCRIPTION, description);
        values.put(Events.EVENT_LOCATION, location);
        values.put(Events.TRANSPARENCY, mAvailabilitySpinner.getSelectedItemPosition());
        int visibility = mVisibilitySpinner.getSelectedItemPosition();
        if (visibility > 0) {
            visibility++;
        }
        values.put(Events.VISIBILITY, visibility);
        return values;
    }

    private boolean isEmpty() {
        String title = mTitleTextView.getText().toString();
        if (title.length() > 0) {
            return false;
        }
        String location = mLocationTextView.getText().toString();
        if (location.length() > 0) {
            return false;
        }
        String description = mDescriptionTextView.getText().toString();
        if (description.length() > 0) {
            return false;
        }
        return true;
    }

    private boolean isCustomRecurrence() {
        if (mEventRecurrence.until != null || mEventRecurrence.interval != 0) {
            return true;
        }
        if (mEventRecurrence.freq == 0) {
            return false;
        }
        switch(mEventRecurrence.freq) {
            case EventRecurrence.DAILY:
                return false;
            case EventRecurrence.WEEKLY:
                if (mEventRecurrence.repeatsOnEveryWeekDay() && isWeekdayEvent()) {
                    return false;
                } else if (mEventRecurrence.bydayCount == 1) {
                    return false;
                }
                break;
            case EventRecurrence.MONTHLY:
                if (mEventRecurrence.repeatsMonthlyOnDayCount()) {
                    return false;
                } else if (mEventRecurrence.bydayCount == 0 && mEventRecurrence.bymonthdayCount == 1) {
                    return false;
                }
                break;
            case EventRecurrence.YEARLY:
                return false;
        }
        return true;
    }

    private boolean isWeekdayEvent() {
        if (mStartTime.weekDay != Time.SUNDAY && mStartTime.weekDay != Time.SATURDAY) {
            return true;
        }
        return false;
    }
}
