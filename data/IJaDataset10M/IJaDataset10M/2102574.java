package com.cnc.mediaconnect1;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.cnc.mediaconnect.common.Common;
import com.cnc.mediaconnect.common.CommonView;
import com.cnc.mediaconnect.common.XMLfunctions;
import com.cnc.mediaconnect.data.SearchItem;
import com.cnc.mediaconnect.views.LoadingView;

public class AddNoteScreen extends Activity implements OnClickListener {

    private Handler handler = new Handler();

    private EditText etNote;

    private String id;

    private int type;

    private LoadingView loadingView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_note);
        etNote = (EditText) findViewById(R.id.editText1);
        ((Button) findViewById(R.id.button1)).setOnClickListener(this);
        ((Button) findViewById(R.id.button2)).setOnClickListener(this);
        loadingView = (LoadingView) findViewById(R.id.loadingView1);
        loadingView.setLoadingMessage("Adding ...");
        showLoadding(false);
        type = getIntent().getExtras().getInt(RecordScreen.TYPE);
        id = getIntent().getExtras().getString(Common.ARG0);
    }

    private void showLoadding(final boolean check) {
        handler.post(new Runnable() {

            public void run() {
                loadingView.setVisibility(check ? View.VISIBLE : View.GONE);
            }
        });
    }

    public void onClick(View v) {
        if (v.getId() == R.id.button2) {
            finish();
            return;
        }
        saveDataAsyn = new SaveDataAsyn();
        saveDataAsyn.execute("");
    }

    SaveDataAsyn saveDataAsyn;

    public class SaveDataAsyn extends AsyncTask<String, String, String> {

        SearchItem item;

        public SaveDataAsyn() {
            super();
        }

        protected String doInBackground(String... params) {
            isRun = true;
            showLoadding(true);
            item = XMLfunctions.addNote(etNote.getText().toString(), id, type == Common._CONTACT);
            return null;
        }

        protected void onPostExecute(String result) {
            isRun = false;
            showLoadding(false);
            if ("true".equals(item.get(0))) {
                Bundle data = new Bundle();
                data.putString(Common.ARG0, etNote.getText().toString());
                data.putString(Common.ARG1, "");
                Intent intent = new Intent();
                intent.putExtras(data);
                setResult(RESULT_OK, intent);
                CommonView.makerText(AddNoteScreen.this, "New note added successfully");
                finish();
            } else {
                CommonView.makerText(getBaseContext(), item.get(1));
            }
        }

        boolean isRun = false;

        public boolean isRun() {
            return isRun;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (saveDataAsyn != null && saveDataAsyn.isRun()) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
