package net.kashifshah.assistant;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CreateTask extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_task);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_task_menu, menu);
        return true;
    }

    public void createTaskSave(View view) {
        EditText taskTitle = (EditText) findViewById(R.id.createTaskTitle);
        String save_notification = getString(R.string.task_saved_toast, taskTitle.getText().toString());
        Toast.makeText(getApplicationContext(), save_notification, Toast.LENGTH_SHORT).show();
        finish();
    }

    public boolean onOptionsMenuItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.createTaskToFront:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
