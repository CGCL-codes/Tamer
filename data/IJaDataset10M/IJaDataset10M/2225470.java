package com.csc531.edit;

import java.util.ArrayList;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.csc531.R;
import com.csc531.adapters.NotesDbAdapter;

public class NotesEdit extends Activity {

    private static final String TAG = "notesedit";

    static ArrayList<String> _coursesList;

    private EditText _noteTitleText;

    private EditText _noteBodyText;

    private Long _rowId;

    private NotesDbAdapter _mDbHelper;

    static final int TIME_DIALOG_ID = 0;

    static final int DATE_DIALOG_ID = 1;

    private Button _confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _mDbHelper = new NotesDbAdapter(this);
        _mDbHelper.open();
        setContentView(R.layout.note_edit);
        _noteTitleText = (EditText) findViewById(R.id.title);
        _noteBodyText = (EditText) findViewById(R.id.body);
        _confirmButton = (Button) findViewById(R.id.confirm);
        dealWithNoteData(savedInstanceState);
    }

    /**
	 * @param confirmButton the confirm button
	 * @param savedInstanceState application saved instance
	 */
    private void dealWithNoteData(Bundle savedInstanceState) {
        _rowId = savedInstanceState != null ? savedInstanceState.getLong(NotesDbAdapter.KEY_ROWID) : null;
        if (_rowId == null) {
            Bundle extras = getIntent().getExtras();
            _rowId = extras != null ? extras.getLong(NotesDbAdapter.KEY_ROWID) : null;
        }
        populateFields();
        _confirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    /**
	 * Prints date field to the screen
	 */
    private void populateFields() {
        if (_rowId != null) {
            Cursor note = _mDbHelper.fetchNote(_rowId);
            startManagingCursor(note);
            _noteTitleText.setText(note.getString(note.getColumnIndexOrThrow(NotesDbAdapter.KEY_TITLE)));
            _noteBodyText.setText(note.getString(note.getColumnIndexOrThrow(NotesDbAdapter.KEY_BODY)));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(NotesDbAdapter.KEY_ROWID, _rowId);
    }

    /**
	 * Saves the data in case the application is suspended
	 */
    private void saveState() {
        String title = _noteTitleText.getText().toString();
        String body = _noteBodyText.getText().toString();
        if (title.length() == 0 || _confirmButton.isPressed() == false) {
            Log.v(TAG, "Nothing in here returning nothing");
            return;
        }
        if (_rowId == null) {
            Log.v(TAG, "SAVING note: " + title);
            long id = _mDbHelper.createNote(title, body);
            if (id > 0) {
                _rowId = id;
            }
        } else {
            Log.v(TAG, "UPDATING note: " + title);
            _mDbHelper.updateNote(_rowId, title, body);
        }
    }
}
