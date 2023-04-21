package com.columboid.protocol.syncml.tests.mock;

import org.jmock.Expectations;
import org.jmock.Mockery;
import com.columboid.protocol.syncml.Credential;

public class MockCredential {

    public static void mockValidUserCredential(Mockery context, final Credential credential) {
        final String userName = "columboid";
        final String nonce = "2b6efdc9-aa0d-4269-86ba-7914e45abcb3";
        final String hashPassword = "X03MO1qnZdYdgyfeuILPmQ";
        context.checking(new Expectations() {

            {
                oneOf(credential).getUserName();
                will(returnValue(userName));
                one(credential).getHashPassword();
                will(returnValue(hashPassword));
                one(credential).getNonceToken();
                will(returnValue(nonce));
            }
        });
    }

    public static void mockValidUserNameAndPassword(Mockery context, final Credential credential) {
        final String userName = "columboid";
        final String nonce = "";
        final String hashPassword = "X03MO1qnZdYdgyfeuILPmQ";
        context.checking(new Expectations() {

            {
                oneOf(credential).getUserName();
                will(returnValue(userName));
                one(credential).getHashPassword();
                will(returnValue(hashPassword));
                one(credential).getNonceToken();
                will(returnValue(nonce));
            }
        });
    }

    public static void mockInvalidValidUserNameAndPassword(Mockery context, final Credential credential) {
        final String userName = "invalid";
        final String nonce = "";
        final String hashPassword = "X03MO1qnZdYdgyfeuILPmQ";
        context.checking(new Expectations() {

            {
                oneOf(credential).getUserName();
                will(returnValue(userName));
                one(credential).getHashPassword();
                will(returnValue(hashPassword));
                one(credential).getNonceToken();
                will(returnValue(nonce));
            }
        });
    }

    public static void mockValidSHAUserCredential(Mockery context, final Credential credential) {
        final String userName = "columboid";
        final String nonce = "2b6efdc9-aa0d-4269-86ba-7914e45abcb3";
        final String hashPassword = "W6ph5Mm5Pz8GgiULbPgzG37mj9g=";
        context.checking(new Expectations() {

            {
                oneOf(credential).getUserName();
                will(returnValue(userName));
                one(credential).getHashPassword();
                will(returnValue(hashPassword));
                one(credential).getNonceToken();
                will(returnValue(nonce));
            }
        });
    }

    public static String mockValidSHANonceHashValue() {
        String matchString = "1cDSMbYmRbTHZhz4XVqPpZNFtw8=";
        return matchString;
    }

    public static void mockInValidUserCredential(Mockery context, final Credential credential) {
        final String userName = "invalid";
        final String nonce = "2b6efdc9-aa0d-4269-86ba-7914e45abcb3";
        final String hashPassword = "qnCqbWBOL4mHoGW_tH_HJA";
        context.checking(new Expectations() {

            {
                oneOf(credential).getUserName();
                will(returnValue(userName));
                one(credential).getHashPassword();
                will(returnValue(hashPassword));
                one(credential).getNonceToken();
                will(returnValue(nonce));
            }
        });
    }

    public static String mockValidNonceHashValue() {
        String matchString = "SFEOMhNr24BAdB2YhbTomg==";
        return matchString;
    }

    public static String mockValidHashPasswordValue() {
        String matchString = "jO+B2N7O2dwDv6qCe3knwQ==";
        return matchString;
    }
}
