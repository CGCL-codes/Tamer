package com.gobynote.android.activity;

import java.util.ArrayList;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.goby.util.utils.Utils;
import com.goby.util.widget.ActionBar;
import com.goby.util.widget.ActionBar.IntentAction;
import com.goby.util.widget.TouchListView;
import com.gobynote.android.CommonValues;
import com.gobynote.android.Dashboard;
import com.gobynote.android.GobyNote;
import com.gobynote.android.R;
import com.gobynote.android.adapter.NoteReorderListAdapter;
import com.gobynote.android.models.NoteRow;

public class NoteReorder extends ListActivity {

    String TAG = this.getClass().getSimpleName();

    ArrayList<NoteRow> noteList;

    NoteReorderListAdapter noteListAdapter;

    ProgressDialog progressDialog;

    static final int SORT_DIALOG_PROGRESS_ID = 1;

    boolean isDirty = false;

    Button cancelBtn;

    Button saveBtn;

    int folderId;

    int minRedorderItem = 999999999;

    int lastRedoreItem = -1;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.note_reorder);
        folderId = getIntent().getIntExtra("fld_id", 0);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        saveBtn = (Button) findViewById(R.id.saveBtn);
        final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
        actionBar.setHomeAction(new IntentAction(this, Dashboard.createIntent(this), R.drawable.goto_home));
        ;
        actionBar.setDisplayHomeAsUpEnabled(true);
        loadData();
        createListenser();
    }

    private void loadData() {
        noteList = GobyNote.getDB(NoteReorder.this).getNotesByFolderId(folderId, false);
        noteListAdapter = new NoteReorderListAdapter(this);
        noteListAdapter.setData(noteList);
        setListAdapter(noteListAdapter);
    }

    private TouchListView.DropListener onDrop = new TouchListView.DropListener() {

        @Override
        public void drop(int from, int to) {
            isDirty = true;
            NoteRow item = (NoteRow) noteListAdapter.getItem(from);
            noteList.remove(item);
            noteList.add(to, item);
            noteListAdapter.setData(noteList);
            noteListAdapter.notifyDataSetChanged();
            if (from > lastRedoreItem) {
                lastRedoreItem = from;
            }
            if (to > lastRedoreItem) {
                lastRedoreItem = to;
            }
            if (from < minRedorderItem) {
                minRedorderItem = from;
            }
            if (to < minRedorderItem) {
                minRedorderItem = to;
            }
            onDirtyAction();
        }
    };

    private TouchListView.RemoveListener onRemove = new TouchListView.RemoveListener() {

        @Override
        public void remove(int which) {
        }
    };

    private void createListenser() {
        TouchListView tlv = (TouchListView) getListView();
        tlv.setDropListener(onDrop);
        tlv.setRemoveListener(onRemove);
        cancelBtn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                finish();
            }
        });
        saveBtn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                reorderAction();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isDirty) {
            AlertDialog.Builder alert = new AlertDialog.Builder(NoteReorder.this);
            alert.setIcon(R.drawable.ic_question_white);
            alert.setTitle(R.string.save_reoredered_list);
            alert.setPositiveButton(R.string.lbl_ok, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {
                    reorderAction();
                }
            }).setNegativeButton(R.string.lbl_cancel, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {
                }
            }).create();
            alert.show();
        }
        super.onBackPressed();
    }

    private void reorderAction() {
        showDialog(SORT_DIALOG_PROGRESS_ID);
        new ReorderAsyncTask().execute();
    }

    private void onDirtyAction() {
    }

    private class ReorderAsyncTask extends AsyncTask<String, Integer, Integer> {

        private int total = 0;

        protected Integer doInBackground(String... params) {
            GobyNote.getDB(NoteReorder.this).updateNoteSeq(noteList, minRedorderItem, lastRedoreItem);
            return total;
        }

        protected void onProgressUpdate(Integer... progress) {
            progressDialog.setProgress(progress[0]);
        }

        protected void onPostExecute(Integer result) {
            dismissDialog(SORT_DIALOG_PROGRESS_ID);
            Utils.info(NoteReorder.this, getString(R.string.note_reordered));
            Intent intent = NoteReorder.this.getIntent();
            intent.putExtra("_result", "_changed");
            NoteReorder.this.setResult(RESULT_OK, intent);
            boardcastMsg();
            finish();
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog;
        Log.d(TAG, "Dialog ... " + id);
        switch(id) {
            case SORT_DIALOG_PROGRESS_ID:
                progressDialog = new ProgressDialog(NoteReorder.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage(getString(R.string.upd_in_progress));
                return progressDialog;
            default:
                dialog = null;
        }
        return dialog;
    }

    private void boardcastMsg() {
        Intent i = new Intent();
        i.setAction(CommonValues.NOTE_UPD);
        this.sendBroadcast(i);
    }
}
