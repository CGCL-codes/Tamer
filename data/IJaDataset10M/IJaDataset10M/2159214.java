package org.ddth.mypluto.driver;

public class AttributeKeys {

    /**
	 * Attribute Key used to bind the application's driver config to the
	 * ServletContext.
	 */
    public static final String DRIVER_CONFIG = "driverConfig";

    /**
	 * Attribute Key used to bind the application's driver admin config to the
	 * ServletContext.
	 */
    public static final String DRIVER_ADMIN_CONFIG = "driverAdminConfig";

    /**
	 * Attribute Key used to bind the application's portlet container to the
	 * ServletContext.
	 */
    public static final String PORTLET_CONTAINER = "portletContainer";

    /** Attribute key used to bind the current page to servlet request. */
    public static final String CURRENT_PAGE = "currentPage";

    /** Attribute key used to bind the portlet title to servlet request. */
    public static final String PORTLET_TITLE = "org.apache.pluto.driver.DynamicPortletTitle";

    public static final String PORTAL_URL_PARSER = "PORTAL_URL_PARSER";

    /**
	 * Private constructor that prevents external instantiation.
	 */
    private AttributeKeys() {
    }
}
