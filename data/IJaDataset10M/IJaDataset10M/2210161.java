package com.luzan.app.vist.gwt.login.client;

/**
 * Email  Created by Dima Dima Jan 11, 2007 7:54:16 PM
 * <p/>
 * (C) 2006 Luzan
 */
public class Email {

    private static final String EMAIL_REGEX = "(.+)@(.+)(\\.)([a-z]+)";

    public static boolean verify(String email) {
        return email.matches(EMAIL_REGEX);
    }
}
