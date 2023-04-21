package com.rudolfheszele.smsselector.controller;

import android.app.Application;

/**
 * This class represents the Application itself, it is the entry point.
 * The main purpose of this class is to initialize the factories
 * @author erudhes
 * @version 0.2
 */
public class SmsSelectorApplication extends Application {

    /**
	 * This method is invoked when the application launches
	 */
    @Override
    public void onCreate() {
        super.onCreate();
        SmsSelectorMainController.initialize();
    }
}
