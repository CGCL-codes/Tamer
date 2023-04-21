package com.android.email.activity.setup;

import com.android.email.R;
import com.android.email.provider.EmailContent;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.Button;
import android.widget.EditText;

/**
 * Tests of the basic UI logic in the Account Setup Incoming (IMAP / POP3) screen.
 */
@MediumTest
public class AccountSetupIncomingTests extends ActivityInstrumentationTestCase2<AccountSetupIncoming> {

    private static final String EXTRA_ACCOUNT = "account";

    private AccountSetupIncoming mActivity;

    private EditText mServerView;

    private Button mNextButton;

    public AccountSetupIncomingTests() {
        super("com.android.email", AccountSetupIncoming.class);
    }

    /**
     * Common setup code for all tests.  Sets up a default launch intent, which some tests
     * will use (others will override).
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Intent i = getTestIntent("imap://user:password@server.com:999");
        setActivityIntent(i);
    }

    /**
     * Test processing with a complete, good URI -> good fields
     */
    public void testGoodUri() {
        Intent i = getTestIntent("imap://user:password@server.com:999");
        setActivityIntent(i);
        getActivityAndFields();
        assertTrue(mNextButton.isEnabled());
    }

    /**
     * No user is not OK - not enabled
     */
    public void testBadUriNoUser() {
        Intent i = getTestIntent("imap://:password@server.com:999");
        setActivityIntent(i);
        getActivityAndFields();
        assertFalse(mNextButton.isEnabled());
    }

    /**
     * No password is not OK - not enabled
     */
    public void testBadUriNoPassword() {
        Intent i = getTestIntent("imap://user@server.com:999");
        setActivityIntent(i);
        getActivityAndFields();
        assertFalse(mNextButton.isEnabled());
    }

    /**
     * No port is OK - still enabled
     */
    public void testGoodUriNoPort() {
        Intent i = getTestIntent("imap://user:password@server.com");
        setActivityIntent(i);
        getActivityAndFields();
        assertTrue(mNextButton.isEnabled());
    }

    /**
     * Test for non-standard but OK server names
     */
    @UiThreadTest
    public void testGoodServerVariants() {
        getActivityAndFields();
        assertTrue(mNextButton.isEnabled());
        mServerView.setText("  server.com  ");
        assertTrue(mNextButton.isEnabled());
    }

    /**
     * Test for non-empty but non-OK server names
     */
    @UiThreadTest
    public void testBadServerVariants() {
        getActivityAndFields();
        assertTrue(mNextButton.isEnabled());
        mServerView.setText("  ");
        assertFalse(mNextButton.isEnabled());
        mServerView.setText("serv$er.com");
        assertFalse(mNextButton.isEnabled());
    }

    /**
     * Get the activity (which causes it to be started, using our intent) and get the UI fields
     */
    private void getActivityAndFields() {
        mActivity = getActivity();
        mServerView = (EditText) mActivity.findViewById(R.id.account_server);
        mNextButton = (Button) mActivity.findViewById(R.id.next);
    }

    /**
     * Create an intent with the Account in it
     */
    private Intent getTestIntent(String storeUriString) {
        EmailContent.Account account = new EmailContent.Account();
        account.setStoreUri(getInstrumentation().getTargetContext(), storeUriString);
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.putExtra(EXTRA_ACCOUNT, account);
        return i;
    }
}
