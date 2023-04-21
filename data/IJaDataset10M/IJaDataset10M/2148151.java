package org.apache.myfaces.trinidaddemo.email.resource;

import java.util.ListResourceBundle;

public class EmailDemoBundle extends ListResourceBundle {

    @Override
    public Object[][] getContents() {
        return _CONTENTS;
    }

    private static final Object[][] _CONTENTS = { { "TODAY_MASK", "Today, {0}" }, { "EMAIL_LIST_ERROR", "Illegal email address." }, { "EMAIL_LIST_ERROR_detail", "{0} is not a legal email address." }, { "MESSAGE_SENT", "The message was sent successfully." }, { "COULD_NOT_DELETE", "Deletion failed." }, { "COULD_NOT_DELETE_detail", "The server returned an error: {0}." }, { "EMAIL_DEMO_TITLE", "Trinidad Email Demo" } };
}
