package de.ecovations.opencms.customer;

import de.ecovations.opencms.common.CmsConstants;

/**
 * Im System verwendete Konstanten, kundenspezifisch.
 * @author konrad.wulf
 *
 */
public class CmsConstantsCustomer extends CmsConstants {

    /** the path to the system modul including a trailing slash */
    public static final String PATH_TO_SYSTEM_CUSTOMER_MODULE = "/system/modules/de.ecovations.opencms.customer/";

    /** overrides template path in parent class */
    public static final String PATH_TO_TEMPLATE = PATH_TO_SYSTEM_CUSTOMER_MODULE + "elements/template.jsp";

    /** The template to use when having a folder in the page definition. 
	 * overrrides listing templat path in parent class */
    public static final String PATH_TO_LISTING_TEMPLATE = PATH_TO_SYSTEM_CUSTOMER_MODULE + "elements/article.jsp";
}
