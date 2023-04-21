package com.android.providers.contacts;

import static com.android.providers.contacts.ContactsActor.PACKAGE_GREY;
import static com.android.providers.contacts.ContactsActor.PACKAGE_RED;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Entity;
import android.content.EntityIterator;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.LiveFolders;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.LargeTest;
import java.io.InputStream;

/**
 * Unit tests for {@link RawContacts#IS_RESTRICTED}.
 */
@LargeTest
public class RestrictionExceptionsTest extends AndroidTestCase {

    private ContactsActor mGrey;

    private ContactsActor mRed;

    private static final String PHONE_GREY = "555-1111";

    private static final String PHONE_RED = "555-2222";

    private static final String EMAIL_GREY = "user@example.com";

    private static final String EMAIL_RED = "user@example.org";

    private static final String GENERIC_STATUS = "Status update";

    private static final String GENERIC_NAME = "Smith";

    public RestrictionExceptionsTest() {
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        final Context overallContext = this.getContext();
        mGrey = new ContactsActor(overallContext, PACKAGE_GREY, SynchronousContactsProvider2.class, ContactsContract.AUTHORITY);
        mRed = new ContactsActor(overallContext, PACKAGE_RED, SynchronousContactsProvider2.class, ContactsContract.AUTHORITY);
        ((SynchronousContactsProvider2) mGrey.provider).wipeData();
    }

    /**
     * Assert that {@link Contacts#CONTACT_STATUS} matches the given value, or
     * that no rows are returned when null.
     */
    void assertStatus(ContactsActor actor, long aggId, String status) {
        final Uri aggUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, aggId);
        actor.ensureCallingPackage();
        final Cursor cursor = actor.resolver.query(aggUri, new String[] { Contacts.CONTACT_STATUS }, null, null, null);
        try {
            if (status == null) {
                assertEquals(0, cursor.getCount());
            } else {
                while (cursor.moveToNext()) {
                    final String foundStatus = cursor.getString(0);
                    assertEquals(status, foundStatus);
                }
            }
        } finally {
            cursor.close();
        }
    }

    public void testRestrictedInsertRestrictedQuery() {
        final long rawContact = mGrey.createContact(true, GENERIC_NAME);
        final int count = mGrey.getDataCountForRawContact(rawContact);
        assertEquals(1, count);
    }

    public void testRestrictedInsertGenericQuery() {
        final long rawContact = mGrey.createContact(true, GENERIC_NAME);
        final int count = mRed.getDataCountForRawContact(rawContact);
        assertEquals(0, count);
    }

    public void testGenericInsertRestrictedQuery() {
        final long rawContact = mRed.createContact(false, GENERIC_NAME);
        final int count = mGrey.getDataCountForRawContact(rawContact);
        assertEquals(1, count);
    }

    public void testGenericInsertGenericQuery() {
        final long rawContact = mRed.createContact(false, GENERIC_NAME);
        final int count = mRed.getDataCountForRawContact(rawContact);
        assertEquals(1, count);
    }

    public void testMixedAggregateRestrictedQuery() {
        final long greyContact = mGrey.createContact(true, GENERIC_NAME);
        final long greyPhone = mGrey.createPhone(greyContact, PHONE_GREY);
        final long redContact = mRed.createContact(false, GENERIC_NAME);
        final long redPhone = mRed.createPhone(redContact, PHONE_RED);
        final long greyAgg = mGrey.getContactForRawContact(greyContact);
        final long redAgg = mRed.getContactForRawContact(redContact);
        assertEquals(greyAgg, redAgg);
        final int greyCount = mGrey.getDataCountForContact(greyAgg);
        assertEquals(4, greyCount);
        final int redCount = mRed.getDataCountForContact(redAgg);
        assertEquals(2, redCount);
    }

    public void testUpdateRestricted() {
        final long greyContact = mGrey.createContact(true, GENERIC_NAME);
        final long greyPhone = mGrey.createPhone(greyContact, PHONE_GREY);
        int count = mRed.getDataCountForRawContact(greyContact);
        assertEquals(0, count);
        final Uri greyUri = ContentUris.withAppendedId(RawContacts.CONTENT_URI, greyContact);
        final ContentValues values = new ContentValues();
        values.put(RawContacts.IS_RESTRICTED, 0);
        mRed.ensureCallingPackage();
        mRed.provider.update(greyUri, values, null, null);
        count = mRed.getDataCountForRawContact(greyContact);
        assertEquals(0, count);
    }

    public void testExportVCard() throws Exception {
        final long greyContact = mGrey.createContact(true, GENERIC_NAME);
        final long greyPhone = mGrey.createPhone(greyContact, PHONE_GREY);
        final long redContact = mRed.createContact(false, GENERIC_NAME);
        final long redPhone = mRed.createPhone(redContact, PHONE_RED);
        final long greyAgg = mGrey.getContactForRawContact(greyContact);
        final long redAgg = mRed.getContactForRawContact(redContact);
        assertEquals(greyAgg, redAgg);
        mRed.ensureCallingPackage();
        final Uri aggUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, greyAgg);
        final Cursor cursor = mRed.resolver.query(aggUri, new String[] { Contacts.LOOKUP_KEY }, null, null, null);
        assertTrue(cursor.moveToFirst());
        final String lookupKey = cursor.getString(0);
        cursor.close();
        final Uri shareUri = Uri.withAppendedPath(Contacts.CONTENT_VCARD_URI, lookupKey);
        final AssetFileDescriptor file = mRed.resolver.openAssetFileDescriptor(shareUri, "r");
        final InputStream in = file.createInputStream();
        final byte[] buf = new byte[in.available()];
        in.read(buf);
        in.close();
        final String card = new String(buf);
        assertTrue(card.indexOf(PHONE_RED) != -1);
        assertTrue(card.indexOf(PHONE_GREY) == -1);
    }

    public void testContactsLiveFolder() {
        final long greyContact = mGrey.createContact(true, GENERIC_NAME);
        final long greyPhone = mGrey.createPhone(greyContact, PHONE_GREY);
        mRed.ensureCallingPackage();
        final Uri folderUri = Uri.withAppendedPath(ContactsContract.AUTHORITY_URI, "live_folders/contacts_with_phones");
        final Cursor cursor = mRed.resolver.query(folderUri, new String[] { LiveFolders._ID }, null, null, null);
        try {
            while (cursor.moveToNext()) {
                final long id = cursor.getLong(0);
                assertFalse(id == greyContact);
            }
        } finally {
            cursor.close();
        }
    }

    public void testRestrictedQueryParam() throws Exception {
        final long greyContact = mGrey.createContact(true, GENERIC_NAME);
        final long greyPhone = mGrey.createPhone(greyContact, PHONE_GREY);
        final Uri greyUri = ContentUris.withAppendedId(RawContacts.CONTENT_URI, greyContact);
        final Uri redUri = greyUri.buildUpon().appendQueryParameter(ContactsContract.REQUESTING_PACKAGE_PARAM_KEY, mRed.packageName).build();
        mGrey.ensureCallingPackage();
        EntityIterator iterator = mGrey.resolver.queryEntities(greyUri, null, null, null);
        while (iterator.hasNext()) {
            final Entity entity = iterator.next();
            final long rawContactId = entity.getEntityValues().getAsLong(RawContacts._ID);
            assertTrue(rawContactId == greyContact);
        }
        mGrey.ensureCallingPackage();
        iterator = mGrey.resolver.queryEntities(redUri, null, null, null);
        while (iterator.hasNext()) {
            final Entity entity = iterator.next();
            final long rawContactId = entity.getEntityValues().getAsLong(RawContacts._ID);
            assertTrue(rawContactId != greyContact);
        }
    }

    public void testRestrictedEmailLookupRestricted() {
        final long greyContact = mGrey.createContact(true, GENERIC_NAME);
        final long greyEmail = mGrey.createEmail(greyContact, EMAIL_GREY);
        mGrey.ensureCallingPackage();
        final Uri lookupUri = Uri.withAppendedPath(Email.CONTENT_LOOKUP_URI, EMAIL_GREY);
        final Cursor cursor = mGrey.resolver.query(lookupUri, new String[] { Data._ID }, null, null, null);
        try {
            while (cursor.moveToNext()) {
                final long dataId = cursor.getLong(0);
                assertTrue(dataId == greyEmail);
            }
        } finally {
            cursor.close();
        }
    }

    public void testRestrictedEmailLookupGeneric() {
        final long greyContact = mGrey.createContact(true, GENERIC_NAME);
        final long greyEmail = mGrey.createEmail(greyContact, EMAIL_GREY);
        mRed.ensureCallingPackage();
        final Uri lookupUri = Uri.withAppendedPath(Email.CONTENT_LOOKUP_URI, EMAIL_GREY);
        final Cursor cursor = mRed.resolver.query(lookupUri, new String[] { Data._ID }, null, null, null);
        try {
            while (cursor.moveToNext()) {
                final long dataId = cursor.getLong(0);
                assertFalse(dataId == greyEmail);
            }
        } finally {
            cursor.close();
        }
    }

    public void testStatusRestrictedInsertRestrictedQuery() {
        final long rawContactId = mGrey.createContactWithStatus(true, GENERIC_NAME, EMAIL_GREY, GENERIC_STATUS);
        final long aggId = mGrey.getContactForRawContact(rawContactId);
        assertStatus(mGrey, aggId, GENERIC_STATUS);
    }

    public void testStatusRestrictedInsertGenericQuery() {
        final long rawContactId = mGrey.createContactWithStatus(true, GENERIC_NAME, EMAIL_GREY, GENERIC_STATUS);
        final long aggId = mGrey.getContactForRawContact(rawContactId);
        assertStatus(mRed, aggId, null);
    }

    public void testStatusGenericInsertRestrictedQuery() {
        final long rawContactId = mRed.createContactWithStatus(false, GENERIC_NAME, EMAIL_RED, GENERIC_STATUS);
        final long aggId = mRed.getContactForRawContact(rawContactId);
        assertStatus(mGrey, aggId, GENERIC_STATUS);
    }

    public void testStatusGenericInsertGenericQuery() {
        final long rawContactId = mRed.createContactWithStatus(false, GENERIC_NAME, EMAIL_RED, GENERIC_STATUS);
        final long aggId = mRed.getContactForRawContact(rawContactId);
        assertStatus(mRed, aggId, GENERIC_STATUS);
    }
}
