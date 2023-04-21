package com.stericson.permissions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.RootToolsException;

public class MasterPermissions extends ListActivity {

    private ArrayList<Permissions_Master> list = new ArrayList<Permissions_Master>();

    private ToggleButton toggle;

    private TextView toggletext;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.master_list);
        toggle = (ToggleButton) findViewById(R.id.toggle);
        toggletext = (TextView) findViewById(R.id.toggletext);
        toggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean checked) {
                if (checked) {
                    toggletext.setText("Disabling Permissions");
                    toggletext.setTextColor(Color.RED);
                } else {
                    toggletext.setText("Enabling Permissions");
                    toggletext.setTextColor(Color.GREEN);
                }
            }
        });
        buildList();
        warn();
    }

    public void warn() {
        new AlertDialog.Builder(MasterPermissions.this).setCancelable(false).setTitle("WARNING!!").setMessage("Making changes here will affect ALL permissions that we can change. \n\n " + "For example, If you disable the internet permission here then all apps shown on the main screen, after the splash, will be denied this permission. \n\n" + "If you install new apps, or update apps, then those apps will have that permission if they request it.").setPositiveButton("ok", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {
            }
        }).show();
    }

    public void buildList() {
        StaticThings.patienceShowing = true;
        StaticThings.patience = ProgressDialog.show(MasterPermissions.this, getString(R.string.working), getString(R.string.loadingPermissions), true);
        new LoadPermissions().execute();
    }

    private void showList() {
        setListAdapter(new PermissionsAdapter(this, R.layout.master_permissions_row, list));
        StaticThings.patience();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        if (!RootTools.isAccessGiven()) {
            new AlertDialog.Builder(MasterPermissions.this).setCancelable(false).setTitle("OOOOPS!").setMessage("We could not get root access, therefore we cannot make the needed changes to adjust this apps permissions." + "\n\n Check that you have root, that you have given the app root access via SuperUser, and try again.").setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {
                    MasterPermissions.this.finish();
                }
            }).show();
        } else {
            StaticThings.patienceShowing = true;
            StaticThings.patience = ProgressDialog.show(MasterPermissions.this, getString(R.string.working), getString(R.string.changingPermission), true);
            new ChangePermissions().execute(list.get(position).Permission);
        }
    }

    private class PermissionsAdapter extends ArrayAdapter<Permissions_Master> {

        private int[] colors = new int[] { 0xff303030, 0xff404040 };

        private View v;

        private Context context;

        int index = 0;

        public PermissionsAdapter(Context context, int textViewResourceId, ArrayList<Permissions_Master> packages) {
            super(context, textViewResourceId, packages);
            this.context = context;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            v = convertView;
            final Permissions_Master o = list.get(position);
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.master_permissions_row, null);
            }
            if (o != null) {
                TextView permission = (TextView) v.findViewById(R.id.permission);
                TextView permissionDescription = (TextView) v.findViewById(R.id.permissionDescription);
                TextView owner = (TextView) v.findViewById(R.id.Owner2);
                ImageView icon = (ImageView) v.findViewById(R.id.packageicon);
                LinearLayout row = (LinearLayout) v.findViewById(R.id.rowMain);
                if (permission != null) {
                    permission.setText(o.Permission);
                }
                if (permissionDescription != null) {
                    permissionDescription.setText(o.PermissionDescription);
                }
                if (owner != null) {
                    owner.setText(o.Owner);
                }
                if (icon != null) {
                    icon.setImageDrawable(o.icon);
                }
                if (position % 2 == 0) {
                    row.setBackgroundColor(colors[position % 2]);
                } else {
                    row.setBackgroundColor(colors[position % 2]);
                }
            }
            return (v);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Reboot");
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("Reboot")) {
            Reboot();
        }
        return true;
    }

    private void Reboot() {
        new AlertDialog.Builder(MasterPermissions.this).setCancelable(false).setTitle("You sure?").setMessage("Are you sure you want to reboot?").setPositiveButton("Yes", new OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                try {
                    RootTools.sendShell("reboot");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (RootToolsException e) {
                    e.printStackTrace();
                }
            }
        }).setNegativeButton("No", null).show();
    }

    public void genericError() {
        final SpannableString s = new SpannableString("We could not perform the requested operation! \n\n Please email me about this: StericDroid@gmail.com");
        Linkify.addLinks(s, Linkify.ALL);
        new AlertDialog.Builder(MasterPermissions.this).setCancelable(false).setTitle("OOOOPS!").setMessage(s).setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {
                MasterPermissions.this.finish();
            }
        }).show();
    }

    private class LoadPermissions extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            PackageManager pm = getPackageManager();
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(new FileReader(StaticThings.path()));
                int eventType = xpp.getEventType();
                List<String> tmp = new ArrayList<String>();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("perms")) {
                        while (eventType != XmlPullParser.END_DOCUMENT) {
                            if (eventType == XmlPullParser.END_TAG && xpp.getName().equals("perms")) {
                                break;
                            }
                            if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("item")) {
                                String description;
                                if (pm.getPermissionInfo(xpp.getAttributeValue(0).replace("stericson.disabled.", ""), 0).loadDescription(pm) == null) {
                                    description = "No description available for this permission";
                                } else {
                                    description = pm.getPermissionInfo(xpp.getAttributeValue(0).replace("stericson.disabled.", ""), 0).loadDescription(pm).toString();
                                }
                                if (!tmp.contains(xpp.getAttributeValue(0).replace("stericson.disabled.", ""))) {
                                    tmp.add(xpp.getAttributeValue(0).replace("stericson.disabled.", ""));
                                    list.add(new Permissions_Master(xpp.getAttributeValue(0).replace("stericson.disabled.", ""), description, pm.getPermissionInfo(xpp.getAttributeValue(0).replace("stericson.disabled.", ""), 0).packageName, pm.getApplicationInfo(pm.getPermissionInfo(xpp.getAttributeValue(0).replace("stericson.disabled.", ""), 0).packageName, 0).loadIcon(getPackageManager())));
                                }
                            }
                            eventType = xpp.next();
                        }
                    }
                    eventType = xpp.next();
                }
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
            return "done";
        }

        protected void onPostExecute(String result) {
            showList();
        }
    }

    private class ChangePermissions extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... permission) {
            PackageManager pm = getPackageManager();
            try {
                RootTools.sendShell("dd if=" + StaticThings.path() + " of=/data/local/packages1.xml");
                RootTools.sendShell("dd if=" + StaticThings.path() + " of=/data/local/packages.xml");
                RootTools.sendShell("chmod 0777 /data/local/packages1.xml");
                RootTools.sendShell("chmod 0777 /data/local/packages.xml");
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (new File("/data/local/packages1.xml").exists()) {
                try {
                    String readTarget = "/data/local/packages1.xml";
                    String writeTarget = "/data/local/packages.xml";
                    LineNumberReader lnr = new LineNumberReader(new FileReader(readTarget));
                    FileWriter fw = new FileWriter(writeTarget);
                    String line;
                    boolean done = false;
                    while ((line = lnr.readLine()) != null) {
                        if (line.contains("<shared-user")) {
                            done = true;
                        }
                        if (line.contains("<perms>") && !done) {
                            RootTools.debugMode = true;
                            fw.write(line + "\n");
                            while ((line = lnr.readLine()) != null) {
                                if (line.contains("</perms>")) {
                                    break;
                                }
                                if (line.contains(permission[0])) {
                                    String tmp;
                                    if (line.contains("stericson.disabled.")) {
                                        if (!toggle.isChecked()) {
                                            tmp = line.replace("stericson.disabled.", "");
                                        } else {
                                            tmp = line;
                                        }
                                    } else {
                                        if (toggle.isChecked()) {
                                            tmp = line.replace("name=\"", "name=\"stericson.disabled.");
                                        } else {
                                            tmp = line;
                                        }
                                    }
                                    fw.write(tmp + "\n");
                                } else {
                                    fw.write(line + "\n");
                                }
                            }
                        }
                        fw.write(line + "\n");
                    }
                    fw.close();
                    lnr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return 1;
                }
                try {
                    RootTools.sendShell("dd if=/data/local/packages.xml of=" + StaticThings.path());
                    RootTools.sendShell("rm /data/local/packages1.xml");
                    RootTools.sendShell("rm /data/local/packages.xml");
                    return 2;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                return 1;
            }
            return 2;
        }

        protected void onPostExecute(Integer result) {
            switch(result) {
                case 1:
                    genericError();
                    break;
                case 2:
                    StaticThings.patience();
                    break;
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
