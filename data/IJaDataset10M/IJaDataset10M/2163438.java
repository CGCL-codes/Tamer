package gene.android;

import java.util.Calendar;
import classes.Assignment;
import classes.Course;
import classes.Reminder;
import database.AssignmentDAO;
import database.ReminderDAO;
import forms.AssignmentForm;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class AddAssignment extends AssignmentForm {

    private AlertDialog alert;

    private boolean selected;

    protected static final String COURSE_SELECTED = "COURSE_SELECTED";

    private boolean courseSeleced;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            timeChanged = savedInstanceState.getBoolean(TIME_CHANGED);
            courseSeleced = savedInstanceState.getBoolean(COURSE_SELECTED);
            remindInteger = savedInstanceState.getInt(REMINDER_INT);
            remindString = savedInstanceState.getString(REMINDER_STRING);
            updateReminderTextView();
            dueDateLong = savedInstanceState.getLong(DUE_DATE_LONG);
            dueDate = Calendar.getInstance();
            dueDate.setTimeInMillis(dueDateLong);
            dueDateText.setText(Assignment.getDueDateString(dueDate, "MM/dd/yyyy"));
            timeDueText.setText(Course.getStartTimeString(dueDate, "hh:mm a"));
        } else {
            dueDate = Calendar.getInstance();
            dueDateText.setText(Assignment.getDueDateString(dueDate, "MM/dd/yyyy"));
            dueDate.set(Calendar.HOUR_OF_DAY, ((Course) courseSpinner.getSelectedItem()).getStartTime().get(Calendar.HOUR_OF_DAY));
            dueDate.set(Calendar.MINUTE, ((Course) courseSpinner.getSelectedItem()).getStartTime().get(Calendar.MINUTE));
            timeDueText.setText(Course.getStartTimeString(dueDate, "hh:mm a"));
        }
        courseSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (!timeChanged) {
                    if (selected) {
                        dueDate.set(Calendar.HOUR_OF_DAY, courses.get(arg2).getStartTime().get(Calendar.HOUR_OF_DAY));
                        dueDate.set(Calendar.MINUTE, courses.get(arg2).getStartTime().get(Calendar.MINUTE));
                        timeDueText.setText(Course.getStartTimeString(dueDate, "hh:mm a"));
                    } else selected = true;
                }
                courseID = courses.get(arg2).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        if (courses.size() > 1 && !courseSeleced) {
            if (alert == null) alert = createCourseAlertDialog();
            alert.show();
        }
    }

    /**
	 * Creates and returns the "Choose Course" dialog
	 * @return The "Choose Course" dialog
	 */
    public AlertDialog createCourseAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Course");
        builder.setIcon(R.drawable.ic_courses);
        CharSequence[] courseStrings = new CharSequence[courses.size()];
        for (int i = 0; i < courses.size(); i++) {
            courseStrings[i] = courses.get(i).getName();
        }
        builder.setItems(courseStrings, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                courseSpinner.setSelection(which);
                courseSeleced = true;
            }
        });
        alert = builder.create();
        return alert;
    }

    @Override
    protected void onSaveButtonClicked() {
        Assignment assignment = new Assignment(courseID, assignmentNameString, descriptionString, dueDate, false);
        long assignmentId = AssignmentDAO.addAssignment(context, assignment);
        if (assignmentId == -1) {
            Toast.makeText(context, "Error adding assignment", Toast.LENGTH_SHORT).show();
        } else {
            if (reminderToggleButton.isChecked()) {
                Reminder reminder = new Reminder((int) assignmentId, remindInteger, remindString);
                if (ReminderDAO.addReminder(context, reminder) != -1) {
                    AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                    reminder.createAlarm(context, dueDate, am);
                    tracker.trackEvent(TRACKER_REMINDER, TRACKER_ADD, CLASS_TAG, 0);
                }
            }
            tracker.trackEvent(TRACKER_ASSIGNMENT, TRACKER_ADD, CLASS_TAG, 0);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(COURSE_SELECTED, courseSeleced);
        outState.putLong(DUE_DATE_LONG, dueDate.getTimeInMillis());
        outState.putInt(REMINDER_INT, remindInteger);
        outState.putString(REMINDER_STRING, remindString);
        outState.putBoolean(TIME_CHANGED, timeChanged);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        if (alert != null) alert.dismiss();
        super.onPause();
    }
}
