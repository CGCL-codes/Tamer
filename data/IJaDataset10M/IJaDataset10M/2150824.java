package com.csc531.lists;

import java.util.ArrayList;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import com.csc531.R;
import com.csc531.DataTypes.Note;
import com.csc531.adapters.ManagerDbAdapter;
import com.csc531.adapters.NotesListAdapter;
import com.csc531.edit.NotesEdit;

public class NotesList extends ListActivity {

    private static final String TAG = "NOTES TAB";

    private static final int ACTIVITY_CREATE = 0;

    private static final int ACTIVITY_EDIT = 1;

    private static final int INSERT_ID = Menu.FIRST;

    private static final int EDIT_ID = Menu.FIRST + 1;

    private static final int DELETE_ID = Menu.FIRST + 2;

    private ArrayList<Integer> _notesDatabaseRowIds;

    private ManagerDbAdapter _mDbHelper;

    private NotesListAdapter _notesTabAdapter;

    private SimpleCursorAdapter _simpleNoteCursor;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        fillNoteListData();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        ((NotesListAdapter) getListAdapter()).toggle(position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, 0, R.string.notemenu_insert);
        menu.add(0, EDIT_ID, 0, R.string.notemenu_edit);
        menu.add(0, DELETE_ID, 0, R.string.notemenu_delete);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
            case INSERT_ID:
                createNote();
                return true;
            case EDIT_ID:
                editNote();
                return true;
            case DELETE_ID:
                deleteNote();
                fillNoteListData();
                return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _mDbHelper = new ManagerDbAdapter(this);
        _mDbHelper.open();
        fillNoteListData();
    }

    public void fillNoteListData() {
        initializeListAdapterAndRowIds();
        fetchNotesFromDatabase();
        try {
            printNotesToClassesTab();
        } catch (RuntimeException ex) {
            Log.v(TAG, "Error when printing notes to from note's list adapter");
        }
        setNotesListAdapter();
    }

    private void initializeListAdapterAndRowIds() {
        _notesTabAdapter = new NotesListAdapter(this);
        _notesDatabaseRowIds = new ArrayList<Integer>();
    }

    private void fetchNotesFromDatabase() {
        Cursor noteCursor = _mDbHelper.fetchAllNotes();
        startManagingCursor(noteCursor);
        String[] from = new String[] { ManagerDbAdapter.KEY_NOTE_TITLE };
        int[] to = new int[] { R.id.text1 };
        _simpleNoteCursor = new SimpleCursorAdapter(this, R.layout.notes_row, noteCursor, from, to);
    }

    private void printNotesToClassesTab() throws RuntimeException {
        _simpleNoteCursor.getCursor().moveToFirst();
        int numberOfRows = _simpleNoteCursor.getCursor().getCount();
        for (int i = 0; i < numberOfRows; i++) {
            addNotesToCourse();
            _simpleNoteCursor.getCursor().moveToNext();
        }
    }

    private void setNotesListAdapter() {
        _notesTabAdapter.createExpandedList();
        setListAdapter(_notesTabAdapter);
    }

    private void addNotesToCourse() {
        int k = 0;
        String rowId = _simpleNoteCursor.getCursor().getString(k++);
        _notesDatabaseRowIds.add(Integer.parseInt(rowId));
        Note note = new Note();
        String title = _simpleNoteCursor.getCursor().getString(k++);
        note.setTitle(title);
        String body = _simpleNoteCursor.getCursor().getString(k++);
        note.setBody(body);
        _notesTabAdapter.addNote(note);
    }

    private void createNote() {
        Intent i = new Intent(this, NotesEdit.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }

    private void editNote() {
        Intent i = new Intent(this, NotesEdit.class);
        if (checkNotesExist() == false) {
            return;
        }
        long id = _notesDatabaseRowIds.get((int) getSelectedItemId());
        i.putExtra(ManagerDbAdapter.KEY_ROWID, id);
        startActivityForResult(i, ACTIVITY_EDIT);
    }

    private boolean checkNotesExist() {
        if (_notesDatabaseRowIds.size() <= 0) {
            return false;
        } else {
            return true;
        }
    }

    private void deleteNote() {
        if (checkNotesExist() == false) {
            return;
        }
        _mDbHelper.deleteNote(_notesDatabaseRowIds.get((int) getListView().getSelectedItemId()));
    }
}
