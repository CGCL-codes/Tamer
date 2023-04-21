package nocuffin.m5;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/** 
 * Team: No Cuffin'
 * 2/27/2012
 * 
 *
 * This will be our MainUI!!
 * 
 * 
 * @author Robert Allen, Philip Johnson, Steven Moore
 * @version 1.0 2/27/2012
 *
 */
public class NewTask extends Activity {

    Button bBack, bCreate;

    TextView label, faillabel, faillabel2, title;

    EditText type, comments, date, location, descript;

    String entry;

    final String FILENAME = "key";

    final String completeornot = "This task is not complete.";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main4);
        bBack = (Button) findViewById(R.id.backButton);
        bCreate = (Button) findViewById(R.id.createButton);
        label = (TextView) findViewById(R.id.invalidpasswordLabel);
        title = (TextView) findViewById(R.id.titleLabel);
        faillabel = (TextView) findViewById(R.id.failLabel);
        type = (EditText) findViewById(R.id.typeText);
        comments = (EditText) findViewById(R.id.commentsText);
        date = (EditText) findViewById(R.id.dateText);
        location = (EditText) findViewById(R.id.locationText);
        descript = (EditText) findViewById(R.id.descriptText);
        Bundle d = getIntent().getExtras();
        String[] name1 = d.getStringArray("valofStringArray");
        if (name1 == null) {
            label.setText("");
        } else {
            label.setText(name1[0]);
        }
        bBack.setOnClickListener(new View.OnClickListener() {

            /** 
	        	 * 
	        	 * Manipulates the text boxes upon a click of the available button. 
	        	 * 
	        	 * 
	        	 * @param v Default android param
	        	 *
	        	 */
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), TaskView.class);
                Bundle d = getIntent().getExtras();
                String name1 = d.getString("name");
                d.putString("name", name1);
                myIntent.putExtras(d);
                startActivity(myIntent);
            }
        });
        bCreate.setOnClickListener(new View.OnClickListener() {

            /** 
	        	 * 
	        	 * Manipulates the text boxes upon a click of the available button. 
	        	 * 
	        	 * 
	        	 * @param v Default android param
	        	 *
	        	 */
            public void onClick(View v) {
                String ctype, ccomments, cdate, clocation, cdescript;
                ctype = type.getText().toString();
                cdate = date.getText().toString();
                clocation = location.getText().toString();
                cdescript = descript.getText().toString();
                ccomments = comments.getText().toString();
                boolean fails1 = false;
                boolean fails2 = false;
                boolean fails3 = false;
                label.setText("");
                faillabel.setText("");
                if (ctype.contains(":")) {
                    fails1 = true;
                }
                if (cdate.contains(":")) {
                    fails1 = true;
                }
                if (clocation.contains(":")) {
                    fails1 = true;
                }
                if (cdescript.contains(":")) {
                    fails1 = true;
                }
                if (ccomments.contains(":")) {
                    fails1 = true;
                }
                if (!ctype.equalsIgnoreCase("Personal") && !ctype.equalsIgnoreCase("School") && !ctype.equalsIgnoreCase("Work")) {
                    fails2 = true;
                }
                if (cdate.length() != 10) {
                    fails3 = true;
                } else {
                    char[] datefield = cdate.toCharArray();
                    String[] stringarrayofdatefield = new String[datefield.length];
                    for (int i = 0; i < datefield.length; i++) {
                        stringarrayofdatefield[i] = "" + datefield[i];
                    }
                    if (!"01".contains(stringarrayofdatefield[0])) {
                        fails3 = true;
                    }
                    if ((!"0123456789".contains(stringarrayofdatefield[1])) || (!"0123".contains(stringarrayofdatefield[3])) || (!"0123456789".contains(stringarrayofdatefield[4])) || (!"123".contains(stringarrayofdatefield[6])) || (!"0123456789".contains(stringarrayofdatefield[7])) || (!"0123456789".contains(stringarrayofdatefield[8])) || (!"0123456789".contains(stringarrayofdatefield[9]))) {
                        fails3 = true;
                    }
                    if (!(fails3)) {
                        if (Integer.valueOf(stringarrayofdatefield[0] + stringarrayofdatefield[1]) > 12) {
                            fails3 = true;
                        }
                    }
                    if (!(fails3)) {
                        if (Integer.valueOf(stringarrayofdatefield[3] + stringarrayofdatefield[4]) > 31) {
                            fails3 = true;
                        }
                        if ((Integer.valueOf(stringarrayofdatefield[3] + stringarrayofdatefield[4]) > 29) && (Integer.valueOf(stringarrayofdatefield[0] + stringarrayofdatefield[1]) == 2)) {
                            fails3 = true;
                        }
                        if ((Integer.valueOf(stringarrayofdatefield[3] + stringarrayofdatefield[4]) > 30) && ((Integer.valueOf(stringarrayofdatefield[0] + stringarrayofdatefield[1]) == 9) || (Integer.valueOf(stringarrayofdatefield[0] + stringarrayofdatefield[1]) == 11) || (Integer.valueOf(stringarrayofdatefield[0] + stringarrayofdatefield[1]) == 4) || (Integer.valueOf(stringarrayofdatefield[0] + stringarrayofdatefield[1]) == 6))) {
                            fails3 = true;
                        }
                        if ((Integer.valueOf(stringarrayofdatefield[3] + stringarrayofdatefield[4]) == 31) && !((Integer.valueOf(stringarrayofdatefield[0] + stringarrayofdatefield[1]) == 1) || (Integer.valueOf(stringarrayofdatefield[0] + stringarrayofdatefield[1]) == 3) || (Integer.valueOf(stringarrayofdatefield[0] + stringarrayofdatefield[1]) == 5) || (Integer.valueOf(stringarrayofdatefield[0] + stringarrayofdatefield[1]) == 7) || (Integer.valueOf(stringarrayofdatefield[0] + stringarrayofdatefield[1]) == 8) || (Integer.valueOf(stringarrayofdatefield[0] + stringarrayofdatefield[1]) == 10) || (Integer.valueOf(stringarrayofdatefield[0] + stringarrayofdatefield[1]) == 12))) {
                            fails3 = true;
                        }
                    }
                    if ((!"/".contains(stringarrayofdatefield[2])) || (!"/".contains(stringarrayofdatefield[5]))) {
                        fails3 = true;
                    }
                }
                if (fails1) {
                    label.setText("No colon character allowed in any field");
                }
                if (fails2) {
                    faillabel.setText("Type must be Personal, School, or Work");
                }
                if (fails3) {
                    label.setText("Date format incorrect. Follow mm/dd/yyyy");
                }
                if (!(fails1 || fails2 || fails3)) {
                    Bundle b = getIntent().getExtras();
                    String name = b.getString("name");
                    String entry = name + ":" + cdescript + ":" + clocation + ":" + cdate + ":" + (ccomments + completeornot) + ":" + ctype + "\n";
                    FileOutputStream fos;
                    try {
                        fos = new FileOutputStream(Environment.getExternalStorageDirectory());
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    }
                    try {
                        fos = openFileOutput(FILENAME, Context.MODE_APPEND);
                        fos.write(entry.getBytes());
                        fos.flush();
                        fos.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        Class ourClass = Class.forName("nocuffin.m5." + "TaskView");
                        Intent myIntent = new Intent(v.getContext(), ourClass);
                        Bundle d = getIntent().getExtras();
                        String name1 = d.getString("name");
                        d.putString("name", name1);
                        myIntent.putExtras(d);
                        startActivity(myIntent);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
