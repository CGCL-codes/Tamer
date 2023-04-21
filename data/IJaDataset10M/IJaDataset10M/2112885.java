package nocuffin.m5;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

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
public class TaskView extends ListActivity {

    String contents[], myList[], pielist[], tryList[];

    String collected;

    String name, entries, criteria;

    boolean special, filteractive;

    ArrayList<String> displayList;

    String[] returnarray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        String name = b.getString("name");
        special = false;
        filteractive = false;
        if (b.getString("criteria") != null) {
            criteria = b.getString("criteria");
            special = b.getBoolean("special");
            filteractive = true;
        }
        this.name = name;
        entries = findEntries();
        this.myList = new String[20];
        this.myList = createNameList(entries, special).clone();
        if (returnarray.length < 2 && !(special)) {
            this.pielist = new String[1];
            pielist[0] = "+ New Entry";
        } else if ((returnarray.length < 2) && (special)) {
            this.pielist = new String[1];
            pielist[0] = "+ Reset Filter";
        } else {
            this.pielist = new String[returnarray.length + 2];
            if (filteractive || b.getString("criteria") != null) {
                pielist[0] = "+ Reset Filter";
            } else {
                pielist[0] = "+ New Entry";
            }
            pielist[1] = "+ Apply Filter";
            pielist[2] = "+ Go Back";
            for (int x = 3; x < returnarray.length + 2; x++) {
                pielist[x] = returnarray[x - 3];
            }
        }
        setListAdapter(new ArrayAdapter<String>(TaskView.this, android.R.layout.simple_list_item_1, pielist));
    }

    String[] createNameList(String lol, boolean i) {
        String[] entriesfun;
        entriesfun = lol.split(":");
        displayList = new ArrayList<String>();
        if (entriesfun != null) {
            for (int x = 0; x < entriesfun.length; x++) {
                displayList.add(entriesfun[x]);
            }
        }
        int size = displayList.size();
        size = size / 6;
        returnarray = new String[size + 1];
        int count = 0;
        if (displayList.size() > 1) {
            for (int x = 0; x < displayList.size(); x++) {
                if ((x) % 6 == 1) {
                    returnarray[count] = displayList.get(x);
                    count = count + 1;
                }
            }
        }
        String[] wrong = { "error" };
        if (returnarray.length != 0) {
            return returnarray;
        } else {
            return wrong;
        }
    }

    @SuppressWarnings("null")
    String findEntries() {
        FileInputStream fis = null;
        try {
            fis = openFileInput("key");
            byte[] dataArray = new byte[fis.available()];
            while (fis.read(dataArray) != -1) {
                this.collected = new String(dataArray);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String[] entries = this.collected.split("\n");
        boolean hasentry = false;
        String fulltasklist = "";
        if (entries.length != 0) {
            for (int x = 0; x < entries.length; x++) {
                if (entries[x].contains(this.name) && (entries[x].length() > this.name.length()) && entries[x].contains(":")) {
                    hasentry = true;
                    if (special && (criteria.equals("work") || criteria.equals("personal") || criteria.equals("school"))) {
                        filteractive = true;
                        String[] parsed = entries[x].split(":");
                        if (parsed[5].toLowerCase().equals(criteria.toLowerCase())) {
                            fulltasklist += entries[x] + ":";
                        } else {
                        }
                    } else if (special && (criteria.equals("This task is not complete."))) {
                        filteractive = true;
                        String[] parsed = entries[x].split(":");
                        if (parsed[4].toLowerCase().contains(criteria.toLowerCase())) {
                            fulltasklist += entries[x] + ":";
                        } else {
                        }
                    } else if (special && (criteria.length() == 10)) {
                        filteractive = true;
                        String[] parsed = entries[x].split(":");
                        String[] dateparts = parsed[3].toLowerCase().split("/");
                        String[] critparts = criteria.toLowerCase().split("/");
                        boolean yearsshorter = false, monthsshorter = false, daysshorter = false;
                        if (Integer.parseInt(dateparts[2]) > Integer.parseInt(critparts[2])) {
                            fulltasklist += entries[x] + ":";
                        }
                        if (Integer.parseInt(dateparts[2]) == Integer.parseInt(critparts[2])) {
                            if (Integer.parseInt(dateparts[0]) > Integer.parseInt(critparts[0])) {
                                fulltasklist += entries[x] + ":";
                            }
                            if (Integer.parseInt(dateparts[0]) == Integer.parseInt(critparts[0])) {
                                if (Integer.parseInt(dateparts[1]) > Integer.parseInt(critparts[1])) {
                                    fulltasklist += entries[x] + ":";
                                }
                            }
                        } else {
                        }
                    } else {
                        fulltasklist += entries[x] + ":";
                    }
                }
            }
        }
        return fulltasklist;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if (position == 0) {
            if (pielist[0].equals("+ New Entry")) {
                try {
                    Class ourClass = Class.forName("nocuffin.m5." + "NewTask");
                    Intent myIntent = new Intent(v.getContext(), ourClass);
                    Bundle b = getIntent().getExtras();
                    String name = b.getString("name");
                    b.putString("name", name);
                    b.putStringArray("valofStringarray", myList);
                    myIntent.putExtras(b);
                    startActivity(myIntent);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Class ourClass = Class.forName("nocuffin.m5." + "TaskView");
                    Intent myIntent = new Intent(v.getContext(), ourClass);
                    Bundle d = new Bundle();
                    String name1 = name;
                    d.putString("name", name1);
                    myIntent.putExtras(d);
                    startActivity(myIntent);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } else if (position == 1) {
            if (pielist[1].equals("+ Apply Filter")) {
                try {
                    Class ourClass = Class.forName("nocuffin.m5." + "ListFilter");
                    Intent myIntent = new Intent(v.getContext(), ourClass);
                    Bundle b = getIntent().getExtras();
                    String name = b.getString("name");
                    b.putString("name", name);
                    myIntent.putExtras(b);
                    startActivity(myIntent);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } else if (position == 2) {
            if (pielist[2].equals("+ Go Back")) {
                try {
                    Class ourClass = Class.forName("nocuffin.m5." + "MainUI");
                    Intent myIntent = new Intent(v.getContext(), ourClass);
                    Bundle b = getIntent().getExtras();
                    String name = b.getString("name");
                    b.putString("name", name);
                    myIntent.putExtras(b);
                    startActivity(myIntent);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                Class ourClass = Class.forName("nocuffin.m5." + "TaskContents");
                Intent myIntent = new Intent(v.getContext(), ourClass);
                Bundle b = new Bundle();
                b.putString("nameoftask", displayList.get(position - 1 + (5 * (position - 2)) - 6));
                b.putString("locationoftask", displayList.get(position - 1 + (5 * (position - 2)) + (1 - 6)));
                b.putString("dateoftask", displayList.get(position - 1 + (5 * (position - 2)) + (2 - 6)));
                b.putString("detailsoftask", displayList.get(position - 1 + (5 * (position - 2)) + (3 - 6)));
                b.putString("typeoftask", displayList.get(position - 1 + (5 * (position - 2)) + (4 - 6)));
                b.putString("username", this.name);
                myIntent.putExtras(b);
                startActivity(myIntent);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
