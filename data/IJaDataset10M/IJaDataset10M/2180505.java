package com.android.email;

import android.net.Uri;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

/**
 * This is a series of unit tests for the Preferences class.
 * 
 * Technically these are functional because they use the underlying preferences framework.  It
 * would be a really good idea if we could inject our own underlying preferences storage, to better
 * test cases like zero accounts behavior (right now, we have to allow for any number of accounts
 * already being on the device, and not trashing any.)
 */
@SmallTest
public class PreferencesUnitTests extends AndroidTestCase {

    private Preferences mPreferences;

    private Account mAccount;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mPreferences = Preferences.getPreferences(getContext());
    }

    /**
     * Delete any dummy accounts we set up for this test
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        if (mAccount != null && mPreferences != null) {
            mAccount.delete(mPreferences);
        }
    }

    /**
     * Test the new getAccountByContentUri() API.  This should return null if no
     * accounts are configured, or the Uri doesn't match, and it should return a desired account
     * otherwise.
     * 
     * TODO: Not actually testing the no-accounts case
     */
    public void testGetAccountByContentUri() {
        createTestAccount();
        Uri testAccountUri = mAccount.getContentUri();
        Account lookup = mPreferences.getAccountByContentUri(testAccountUri);
        assertEquals(mAccount, lookup);
        testAccountUri = Uri.parse("bogus://accounts/" + mAccount.getUuid());
        lookup = mPreferences.getAccountByContentUri(testAccountUri);
        assertNull(lookup);
        testAccountUri = Uri.parse("content://bogus/" + mAccount.getUuid());
        lookup = mPreferences.getAccountByContentUri(testAccountUri);
        assertNull(lookup);
        testAccountUri = Uri.parse("content://accounts/" + mAccount.getUuid() + "-bogus");
        lookup = mPreferences.getAccountByContentUri(testAccountUri);
        assertNull(lookup);
    }

    /**
     * Create a dummy account with minimal fields
     */
    private void createTestAccount() {
        mAccount = new Account(getContext());
        mAccount.save(mPreferences);
    }
}
