package org.ramadda.client.android;

import android.app.Activity;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.Button;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import java.util.ArrayList;
import java.util.List;

public class RamaddaClient extends RamaddaActivity {

    public static final String PROP_USERID = "user.id";

    public static final String PROP_PASSWORD = "user.password";

    public static final String PROP_SERVER = "server";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<View> buttons = new ArrayList<View>();
        buttons.add(GuiUtils.makeButton(this, "List", new View.OnClickListener() {

            public void onClick(View v) {
                showEntryList();
            }
        }));
        buttons.add(GuiUtils.makeButton(this, "New Note", new View.OnClickListener() {

            public void onClick(View v) {
                showNewNote();
            }
        }));
        buttons.add(GuiUtils.makeButton(this, "Configure", new View.OnClickListener() {

            public void onClick(View v) {
                showConfigure();
            }
        }));
        buttons.add(GuiUtils.makeButton(this, "About", new View.OnClickListener() {

            public void onClick(View v) {
                showAbout();
            }
        }));
        buttons.add(GuiUtils.makeButton(this, "Exit", new View.OnClickListener() {

            public void onClick(View v) {
                finish();
            }
        }));
        setContentView(GuiUtils.vbox(this, buttons));
    }

    public void showConfigure() {
        Intent i = new Intent(this, Configure.class);
        startActivity(i);
    }

    public void showEntryList() {
        Intent i = new Intent(this, EntryList.class);
        startActivity(i);
    }

    public void showNewNote() {
        Intent i = new Intent(this, NewNote.class);
        startActivity(i);
    }

    public void showAbout() {
        Intent i = new Intent(this, About.class);
        startActivity(i);
    }

    public static String getPreference(Context context, String prop, String dflt) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(prop, dflt);
    }

    public static void putPreference(Context context, String prop, String value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(prop, value).commit();
    }

    public static String getUserId(Context context) {
        return getPreference(context, PROP_USERID, "");
    }

    public static String getPassword(Context context) {
        return getPreference(context, PROP_PASSWORD, "");
    }

    public static String getServer(Context context) {
        return getPreference(context, PROP_SERVER, "");
    }

    public static void putUserId(Context context, String value) {
        putPreference(context, PROP_USERID, value);
    }

    public static void putPassword(Context context, String value) {
        putPreference(context, PROP_PASSWORD, value);
    }

    public static void putServer(Context context, String value) {
        putPreference(context, PROP_SERVER, value);
    }
}
