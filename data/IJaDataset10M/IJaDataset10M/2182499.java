package com.sun.j2me.app;

/**
 * Abstraction for application model
 */
public class AppModel {

    /** Guard from 'new' operator */
    private AppModel() {
    }

    public static final int MIDLET = 0;

    public static final int XLET = 1;

    public static final int UNKNOWN_MODEL = 2;

    public static int getAppModel() {
        return MIDLET;
    }
}
