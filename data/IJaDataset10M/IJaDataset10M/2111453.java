package com.royfj.contactgroups;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ContactListActivity extends Activity {

    private static final String TAG = "ContactListActivity";

    private ContactMemberManager memberManager = null;

    private ContactGroupManager groupManager = null;

    private String groupId = null;

    private Cursor contacts = null;

    private List<Integer> idList = new ArrayList<Integer>();

    private ArrayList<Boolean> itemChecked = new ArrayList<Boolean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_list);
        Log.d(TAG, "ContactListActivity Start");
        groupId = getIntent().getStringExtra("groupId");
        if (groupId == null) groupId = "0";
        Log.d(TAG, "Pass in data: groupId: " + groupId);
        groupManager = ContactGroupManager.getInstance(this);
        memberManager = ContactMemberManager.getInstance(this);
        contacts = memberManager.getContacts();
        Log.d(TAG, "START:" + new Date().getTime());
        int id = -1;
        int rawId = 0;
        for (contacts.moveToFirst(); !contacts.isAfterLast(); contacts.moveToNext()) {
            id = contacts.getInt(contacts.getColumnIndex(ContactsContract.Contacts._ID));
            rawId = memberManager.getContactRawId(id);
            if (rawId == 0) {
                Log.w(TAG, "personID: " + id + " rawId: " + rawId);
            }
            itemChecked.add(memberManager.inGroup(rawId, Integer.parseInt(groupId)));
            idList.add(rawId);
        }
        Log.d(TAG, "END:" + new Date().getTime());
        ListView listView = (ListView) findViewById(R.id.contact_list);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(new SimpleCursorAdapter(this, android.R.layout.simple_list_item_multiple_choice, contacts, new String[] { ContactsContract.Contacts.DISPLAY_NAME }, new int[] { android.R.id.text1 }));
        for (int i = 0; i < itemChecked.size(); i++) {
            if (itemChecked.get(i)) {
                listView.setItemChecked(i, true);
            }
        }
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView ctView = (CheckedTextView) view;
                int rawId = idList.get(position);
                if (!ctView.isChecked()) {
                    boolean result = groupManager.addToGroup(String.valueOf(rawId), groupId);
                    Log.d(TAG, "addToGroup result:" + result);
                } else {
                    boolean result = groupManager.removeContactFromGroup(String.valueOf(rawId), groupId);
                    Log.d(TAG, "removeContactFromGroup result:" + result);
                }
            }
        });
    }
}
