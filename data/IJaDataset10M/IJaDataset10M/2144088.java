package com.mia.sct.view.handler;

import com.apple.eawt.Application;

/**
 * MacMenuHandler.java
 * 
 * This singleton class holds and registers the Mac OS X application events handler.
 *
 * @author Devon Bryant
 * @since Sep 22, 2007
 */
public class MacMenuHandler extends Application {

    protected MacMenuAdapter menuAdapter = null;

    private static MacMenuHandler mInstance = null;

    private MacMenuHandler() {
        addApplicationListener(new MacMenuAdapter());
    }

    /**
	 * Get an instance of the MacMenuHandler
	 * 
	 * @return MacMenuHandler instance
	 */
    public static MacMenuHandler getInstance() {
        if (mInstance == null) {
            mInstance = new MacMenuHandler();
        }
        return mInstance;
    }

    /**
	 * Get the MenuAdapter
	 * 
	 * @return MacMenuAdapter
	 */
    public MacMenuAdapter getMenuAdapter() {
        return menuAdapter;
    }
}
