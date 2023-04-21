package uk.ac.manchester.ac.uk.client;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.Cookies;

/**
 * @author Simon Jupp
 * @date 14/02/2012
 * Functional Genomics Group EMBL-EBI
 */
public final class Loggable {

    public static String cookie_id = "kupkb_analytics";

    public static void log(String message, String values) {
        String uid = "no_uid";
        if (Cookies.getCookie(cookie_id) != null) {
            uid = Cookies.getCookie(cookie_id);
        }
        Log.info("ACTION:" + message + ";USER:" + uid + ";VALUES:" + values);
    }
}
