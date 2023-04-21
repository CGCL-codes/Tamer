package nocuffin.m5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ListFilter extends Activity {

    Button bWork, bSchool, bPersonal, bIncomplete, bAfterDate;

    EditText date;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main6);
        date = (EditText) findViewById(R.id.dateText);
        bWork = (Button) findViewById(R.id.workonlyButton);
        bSchool = (Button) findViewById(R.id.schoolonlyButton);
        bPersonal = (Button) findViewById(R.id.personalonlyButton);
        bAfterDate = (Button) findViewById(R.id.afterdateonlyButton);
        bIncomplete = (Button) findViewById(R.id.incompleteonlyButton);
        bWork.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                try {
                    Class ourClass = Class.forName("nocuffin.m5." + "TaskView");
                    Intent myIntent = new Intent(v.getContext(), ourClass);
                    Bundle b = getIntent().getExtras();
                    String name = b.getString("name");
                    b.putString("name", name);
                    b.putBoolean("special", true);
                    b.putString("criteria", "work");
                    myIntent.putExtras(b);
                    startActivity(myIntent);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        bSchool.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                try {
                    Class ourClass = Class.forName("nocuffin.m5." + "TaskView");
                    Intent myIntent = new Intent(v.getContext(), ourClass);
                    Bundle b = getIntent().getExtras();
                    String name = b.getString("name");
                    b.putString("name", name);
                    b.putBoolean("special", true);
                    b.putString("criteria", "school");
                    myIntent.putExtras(b);
                    startActivity(myIntent);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        bPersonal.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                try {
                    Class ourClass = Class.forName("nocuffin.m5." + "TaskView");
                    Intent myIntent = new Intent(v.getContext(), ourClass);
                    Bundle b = getIntent().getExtras();
                    String name = b.getString("name");
                    b.putString("name", name);
                    b.putBoolean("special", true);
                    b.putString("criteria", "personal");
                    myIntent.putExtras(b);
                    startActivity(myIntent);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        bIncomplete.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                try {
                    Class ourClass = Class.forName("nocuffin.m5." + "TaskView");
                    Intent myIntent = new Intent(v.getContext(), ourClass);
                    Bundle b = getIntent().getExtras();
                    String name = b.getString("name");
                    b.putString("name", name);
                    b.putBoolean("special", true);
                    b.putString("criteria", "This task is not complete.");
                    myIntent.putExtras(b);
                    startActivity(myIntent);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        bAfterDate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                try {
                    Class ourClass = Class.forName("nocuffin.m5." + "TaskView");
                    Intent myIntent = new Intent(v.getContext(), ourClass);
                    Bundle b = getIntent().getExtras();
                    String name = b.getString("name");
                    b.putString("name", name);
                    b.putBoolean("special", true);
                    b.putString("criteria", date.getText().toString());
                    myIntent.putExtras(b);
                    startActivity(myIntent);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
