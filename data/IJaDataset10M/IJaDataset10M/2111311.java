package com.example.android.businesscard;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple activity that shows a "Pick Contact" button and two fields: contact's name
 * and phone number.  The user taps on the Pick Contact button to bring up
 * the contact chooser.  Once this activity receives the result from contact picker,
 * it launches an asynchronous query (queries should always be asynchronous) to load
 * contact's name and phone number. When the query completes, the activity displays
 * the loaded data.
 */
public class BusinessCardActivity extends Activity {

    private static final int PICK_CONTACT_REQUEST = 1;

    /**
     * An SDK-specific instance of {@link ContactAccessor}.  The activity does not need
     * to know what SDK it is running in: all idiosyncrasies of different SDKs are
     * encapsulated in the implementations of the ContactAccessor class.
     */
    private final ContactAccessor mContactAccessor = ContactAccessor.getInstance();

    /**
     * Called with the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_card);
        Button pickContact = (Button) findViewById(R.id.pick_contact_button);
        pickContact.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                pickContact();
            }
        });
    }

    /**
     * Click handler for the Pick Contact button.  Invokes a contact picker activity.
     * The specific intent used to bring up that activity differs between versions
     * of the SDK, which is why we delegate the creation of the intent to ContactAccessor.
     */
    protected void pickContact() {
        startActivityForResult(mContactAccessor.getPickContactIntent(), PICK_CONTACT_REQUEST);
    }

    /**
     * Invoked when the contact picker activity is finished. The {@code contactUri} parameter
     * will contain a reference to the contact selected by the user. We will treat it as
     * an opaque URI and allow the SDK-specific ContactAccessor to handle the URI accordingly.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CONTACT_REQUEST && resultCode == RESULT_OK) {
            loadContactInfo(data.getData());
        }
    }

    /**
     * Load contact information on a background thread.
     */
    private void loadContactInfo(Uri contactUri) {
        AsyncTask<Uri, Void, ContactInfo> task = new AsyncTask<Uri, Void, ContactInfo>() {

            @Override
            protected ContactInfo doInBackground(Uri... uris) {
                return mContactAccessor.loadContact(getContentResolver(), uris[0]);
            }

            @Override
            protected void onPostExecute(ContactInfo result) {
                bindView(result);
            }
        };
        task.execute(contactUri);
    }

    /**
     * Displays contact information: name and phone number.
     */
    protected void bindView(ContactInfo contactInfo) {
        TextView displayNameView = (TextView) findViewById(R.id.display_name_text_view);
        displayNameView.setText(contactInfo.getDisplayName());
        TextView phoneNumberView = (TextView) findViewById(R.id.phone_number_text_view);
        phoneNumberView.setText(contactInfo.getPhoneNumber());
    }
}
