package org.openintents.historify.data.providers.internal;

import org.openintents.historify.data.providers.Events;
import org.openintents.historify.data.providers.EventsProvider;
import org.openintents.historify.data.providers.internal.Messaging.Messages;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;

/**
 * 
 * Content Provider for accessing the sms messages.
 * 
 * @author berke.andras
 */
public class MessagingProvider extends EventsProvider {

    @Override
    public boolean onCreate() {
        boolean retval = super.onCreate();
        if (retval) setEventsUri(Messages.CONTENT_URI);
        return retval;
    }

    @Override
    protected String getAuthority() {
        return Messaging.MESSAGING_AUTHORITY;
    }

    private Cursor rawQuery(String where, String lookupKey) {
        return getContext().getContentResolver().query(Messaging.Messages.CONTENT_URI, new String[] { Messaging.Messages._ID + " AS " + Events._ID, "NULL AS " + Events.EVENT_KEY, Messaging.Messages.BODY + " AS " + Events.MESSAGE, Messaging.Messages.DATE + " AS " + Events.PUBLISHED_TIME, "'" + lookupKey + "' AS " + Events.CONTACT_KEY, "REPLACE(" + "REPLACE(" + Messaging.Messages.TYPE + "," + Messaging.Messages.INCOMING_TYPE + ",'" + Events.Originator.contact + "')," + Messaging.Messages.OUTGOING_TYPE + ",'" + Events.Originator.user + "') AS " + Events.ORIGINATOR }, where, null, Messaging.Messages.DATE + " DESC");
    }

    @Override
    protected Cursor queryEventsForContact(String lookupKey) {
        String phoneSelection = Phone.LOOKUP_KEY + " = '" + lookupKey + "'";
        ContentResolver resolver = getContext().getContentResolver();
        Cursor phoneCursor = resolver.query(Phone.CONTENT_URI, new String[] { Phone.NUMBER }, phoneSelection, null, null);
        StringBuilder phoneNumbers = new StringBuilder();
        while (phoneCursor.moveToNext()) {
            phoneNumbers.append("'");
            phoneNumbers.append(phoneCursor.getString(0));
            phoneNumbers.append("'");
            phoneNumbers.append(",");
        }
        if (phoneNumbers.length() > 0) phoneNumbers.setLength(phoneNumbers.length() - 1);
        phoneCursor.close();
        String where = Messaging.Messages.ADDRESS + " IN (" + phoneNumbers.toString() + ")";
        return rawQuery(where, lookupKey);
    }

    @Override
    protected Cursor queryEvent(long eventId) {
        String where = Messaging.Messages._ID + " = " + eventId;
        return rawQuery(where, null);
    }
}
