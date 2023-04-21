package com.android.globalsearch.benchmarks;

import android.content.ComponentName;

public class ContactsLatency extends SourceLatency {

    private static final String[] queries = { "", "a", "s", "e", "r", "pub", "sanxjkashasrxae" };

    private static ComponentName CONTACTS_COMPONENT = new ComponentName("com.android.contacts", "com.android.contacts.ContactsListActivity");

    @Override
    protected void onResume() {
        super.onResume();
        testContacts();
    }

    private void testContacts() {
        for (String query : queries) {
            checkSource("CONTACTS", CONTACTS_COMPONENT, query);
        }
    }
}
