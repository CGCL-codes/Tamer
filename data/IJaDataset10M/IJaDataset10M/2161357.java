package com.semipol.PHPCheckclipse;

/**
 * A class with general constants used in the whole plugin.
 * 
 * @author Johannes Wienke
 */
public final class PHPCheckclipseConstants {

    /**
	 * Private constructor, because we don't want instances.
	 */
    private PHPCheckclipseConstants() {
    }

    /**
	 * ID of this plugin. Can be used for namespaces and so on...
	 */
    public static final String PLUGIN_ID = "com.semipol.PHPCheckclipse";

    /**
	 * Error while setting a new marker.
	 */
    public static final int E_SET_MARKER = 0;

    /**
	 * Error while visiting the full project.
	 */
    public static final int E_VISIT_RESOURCE = 1;

    /**
	 * Error while processing a resource delta.
	 */
    public static final int E_VISIT_DELTA = 2;

    /**
	 * Error while checking a file.
	 */
    public static final int E_CHECKING = 3;

    /**
	 * Error while deleting the old markers on a file.
	 */
    public static final int E_DELETE_MARKER = 4;

    /**
	 * Error while activating PHPCheckclipse for a project.
	 */
    public static final int E_TOGGLE = 5;

    /**
	 * Error while creating a registered filter.
	 */
    public static final int E_CREATE_FILTER = 6;

    /**
	 * Error while initializing defaults for filters.
	 */
    public static final int E_INIT_DEFAULTS = 7;

    /**
	 * Error while loading preferences for filters.
	 */
    public static final int E_INIT_PREFS = 8;

    /**
	 * Error while inserting values in the wrapper for the PropertyStore.
	 */
    public static final int E_PROPERTY_INSERT = 9;

    /**
	 * Error while reading a property from a resource.
	 */
    public static final int E_READ_PROPERTY = 10;

    /**
	 * Error while saving usage of specific settings for a project.
	 */
    public static final int E_SAVE_PROJECT_USE = 11;
}
