package sample.todo.android;

import java.util.Date;
import sample.todo.android.model.Task;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

public class EditActivity extends Activity {

    public static String EXT_POSITION = "EXT_POSITION";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);
        Integer position = getIntent().getIntExtra(EXT_POSITION, -1);
        Log.d("", "position: " + position);
        final CheckBox checkBoxDone = (CheckBox) findViewById(R.id.Edit_CheckBoxDone);
        final EditText editTextContents = (EditText) findViewById(R.id.Edit_Contents);
        final DatePicker datePickerDueDate = (DatePicker) findViewById(R.id.Edit_DueDate);
        final Button editButton = (Button) findViewById(R.id.Edit_ButtonEdit);
        final Task t = MainActivity.store.get(position);
        checkBoxDone.setChecked(t.isDone());
        editTextContents.setText(t.getContents());
        Date dueDate = t.getDueDate();
        if (dueDate != null) {
            datePickerDueDate.init(dueDate.getYear(), dueDate.getMonth(), dueDate.getDate(), null);
        }
        editButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                t.setDone(checkBoxDone.isChecked());
                t.setContents(editTextContents.getText().toString());
                Date dueDate = new Date(datePickerDueDate.getYear(), datePickerDueDate.getMonth(), datePickerDueDate.getDayOfMonth());
                t.setDueDate(dueDate);
                setResult(0, new Intent());
                finish();
            }
        });
    }
}
