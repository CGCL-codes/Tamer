package com.the_eventhorizon.actionQueue.testApp;

import com.the_eventhorizon.actionQueue.testApp.mainView.MainViewController;

/**
 * @author <a href="mailto:pkrupets@yahoo.com">Pavel Krupets</a>
 */
public class EntryPoint {

    public static void main(String[] args) {
        try {
            MainViewController.createOpenMainViewAction().schedule();
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
