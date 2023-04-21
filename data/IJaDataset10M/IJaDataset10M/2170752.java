package es.aeat.eett.workbench.core;

public interface PropertiesKeys {

    public static final String LOOK_AND_FEEL = "LookAndFeel";

    public static final String THEME = "Theme";

    /**
	 * App Title 
	 */
    public static final String APP_TITLE = "App.title";

    /**
	 * App Splash Image
	 */
    public static final String SPLAS_HIMAGE = "splashImage";

    /**
	 * Folder where to store application data and configuration files
	 */
    public static final String DATA_FOLDER = "dataFolder";

    /**
	 * Folder where to store shared resources 
	 */
    public static final String SHARED_FOLDER = "sharedFolder";

    /**
	 *  Path relative to SHARED_FOLDER
	 */
    public static final String CATALOGS_FOLDER = "catalogs";

    /**
	 * Save and restore layaout on exit app
	 */
    public static final String SAVE_LAYAOUT_ON_EXIT = "App.save.layaout";
}
