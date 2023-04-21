package com.cs2340.practice.test;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import com.cs2340.practice.LoginActivity;
import com.cs2340.practice.UserDataBaseAdapter;
import com.cs2340.practice.Validator;
import junit.framework.TestCase;

public class ValidatorTest extends TestCase {

    private String username;

    private String password;

    private String wrongPassword;

    private String badUsername;

    private UserDataBaseAdapter myUSerDB;

    private Validator validate;

    public ValidatorTest() {
    }

    protected void setUp() throws Exception {
        super.setUp();
        username = "TestUser";
        password = "fakePassword";
        wrongPassword = "wrong";
        badUsername = "FakeUser";
        validate = new Validator();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testPasswordMatch() {
        String testDatabasePassword = password;
        assertEquals("The passwords should match", true, validate.passwordMatch(password, testDatabasePassword));
        assertFalse("The passwords should not match", validate.passwordMatch(wrongPassword, testDatabasePassword));
    }

    public void testUsernameExists() {
        ArrayList<String> testUserNames = new ArrayList<String>();
        testUserNames.add("Jasmine");
        testUserNames.add("John");
        testUserNames.add("TestUser");
        assertEquals("The username should exist", true, validate.usernameExists(testUserNames, username));
        assertFalse("The username should not exist", validate.usernameExists(testUserNames, badUsername));
    }

    public void testCheckPassword() {
        String testEnteredPassword = password;
        assertEquals("The passwords should match", true, validate.checkPassword(testEnteredPassword, password));
        assertFalse("The passwords should not match", validate.checkPassword(testEnteredPassword, wrongPassword));
    }
}
