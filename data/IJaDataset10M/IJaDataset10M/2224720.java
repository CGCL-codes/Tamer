package gov.sns.apps.pvlogger;

/**
 * DataKeys is an interface which defines the keys used in the logger data table.
 *
 * @author  tap
 */
public interface DataKeys {

    static final String ID_KEY = "ID";

    static final String LAUNCH_TIME_KEY = "LAUNCH_TIME";

    static final String HOST_KEY = "HOST";

    static final String LAST_CHECK_KEY = "LAST_CHECK";

    /** key for status of remote service communication */
    static final String SERVICE_OKAY_KEY = "SERVICE_OKAY";
}
