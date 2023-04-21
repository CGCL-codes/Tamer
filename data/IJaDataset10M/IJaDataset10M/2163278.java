package org.openbandy.example.test;

import org.openbandy.service.NotificatorService;
import org.openbandy.test.Test;

/**
 * A very simple test for the NotificatorService, just create an info alert and
 * show it to the user. The test will pass if the alert could be shown.
 * 
 * <br>
 * <br>
 * (c) Copyright Philipp Bolliger 2007, ALL RIGHTS RESERVED.
 * 
 * @author Philipp Bolliger (philipp@bolliger.name)
 * @version 0.1
 * 
 */
public class NotificatorTest extends Test {

    /**
	 * Create a new NotificatorTest
	 */
    public NotificatorTest() {
        super("Notificator Test");
    }

    public void run() {
        testStarted();
        if (!NotificatorService.infoAlert("This is an 'info' notification.\nYou should see this for 3 seconds..", 3)) {
            testsScreen.setTestFailed(this);
        } else {
            testsScreen.setTestPassed(this);
        }
        testFinished();
    }
}
