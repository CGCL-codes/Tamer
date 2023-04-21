package org.brainypdm.modules.web;

/**
 * 
 * class used for constant
 * @author <a href="mailto:nico@brainypdm.org">Nico Bagari</a>
 *
 */
public class WebConstant {

    /**
	 * the user session property name
	 */
    public static final String USER_SESSION_PROPERTY = "userprincipal";

    /**
	 * the current host page value
	 */
    public static final String HOST_PAGE_PROPERTY = "hostPageValue";

    /****
	 * the current job page value
	 */
    public static final String JOB_PAGE_PROPERTY = "jobPageValue";

    /**
	 * the user session property name
	 */
    public static final String HOST_PAGE_PRE = "hostPagePre";

    public static final String HOST_PAGE_NEXT = "hostPageNext";

    public static final String GO_HOST_PAGE = "goPage";

    public static final String GO_JOB_PAGE = "goJobPage";

    public static final String JOB_PAGE_PRE = "jobPagePre";

    public static final String JOB_PAGE_NEXT = "jobPageNext";

    /**
	 * the name for the list of host in session
	 */
    public static final String SESSION_HOST_LISTS = "hosts_page";

    /**
	 * the name for the list of service in session
	 */
    public static final String SESSION_SERVICE_LIST = "services";

    /**
	 * template name for session timeout
	 */
    public static final String TEMPLATE_SESSION_TERMINATED = "sessionfailed.vm";

    /**
	 * the login failed template
	 */
    public static final String TEMPLATE_LOGIN_FAILED = "loginFailed.vm";

    /**
	 * the name for velocity context variable for the host operation result
	 */
    public static final String VELOCITY_OPERATION_RESULT = "opResult";

    /**
	 * the name for velocity context variable for the hosts list
	 */
    public static final String VELOCITY_HOSTS = "hosts";

    /****
	 * list of jobs
	 */
    public static final String VELOCITY_JOBS = "jobs";

    /***
	 * list of prediction plugin
	 */
    public static final String VELOCITY_PREDICTION_PLUGINGS = "pPlugins";

    /****
	 * a prediction model instance
	 */
    public static final String VELOCITY_PREDICTION_MODEL = "pModelInstance";

    /****
	 * a prediction model selected
	 */
    public static final String VELOCITY_PREDICTION_MODEL_SELECTED = "pModelSelected";

    /***
	 * type of response in ajax call
	 */
    public static final String AJAX_RESPONSE_TYPE = "text/xml";

    /***
	 * id of cache control on response header
	 */
    public static final String AJAX_RESPONSE_HEADER_CACHE_CONTROL = "Cache-Control";

    /***
	 * value of cache control on response header
	 */
    public static final String AJAX_RESPONSE_HEADER_CACHE_CONTROL_VALUE = "no-cache";

    /***
	 * message
	 */
    public static final String AJAX_RESPONSE_MESSAGE = "message";

    /***
	 * size
	 */
    public static final String AJAX_RESPONSE_SIZE = "size";

    /***
	 * date
	 */
    public static final String AJAX_RESPONSE_DATE = "date";

    /***
	 * list
	 */
    public static final String AJAX_RESPONSE_NAME_VALUE_PAIR_LIST = "list";

    /***
	 * nameValuePair
	 */
    public static final String AJAX_RESPONSE_NAME_VALUE_PAIR = "nameValuePair";

    /***
	 * id
	 */
    public static final String AJAX_RESPONSE_ID = "id";

    /***
	 * value
	 */
    public static final String AJAX_RESPONSE_VALUE = "value";
}
