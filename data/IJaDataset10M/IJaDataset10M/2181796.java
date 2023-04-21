package com.google.appengine.testing.cloudcover.client;

import com.google.appengine.testing.cloudcover.client.model.TestStatus;
import com.google.gwt.user.client.ui.UIObject;

/**
 * Style-related utilities.
 *
 * @author Max Ross <max.ross@gmail.com>
 */
public final class StyleHelper {

    private StyleHelper() {
    }

    public static void setStatus(TestStatus testStatus, UIObject obj) {
        switch(testStatus) {
            case SUCCESS:
                obj.setStylePrimaryName("test-passed");
                break;
            case IN_PROGRESS:
                obj.setStylePrimaryName("test-in-progress");
                break;
            case FAILURE:
                obj.setStylePrimaryName("test-failed");
                break;
            case TOO_SLOW:
                obj.setStylePrimaryName("test-too-slow");
                break;
            case NOT_STARTED:
                obj.setStylePrimaryName("test-not-started");
                break;
        }
    }
}
