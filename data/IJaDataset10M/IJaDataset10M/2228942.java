package org.thirdstreet.google;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.google.gdata.client.GoogleService;
import com.google.gdata.client.blogger.BloggerService;
import com.google.gdata.data.DateTime;

/**
 * Some utilities for dealing with the google api
 * 
 * @author John Bramlett
 */
public final class GoogleUtils {

    private static final String kGoogleDateFormat = "yyyy-MM-dd HH:mm";

    /**
	 * Converts our google datetime to a regular java date
	 * 
	 * @param dt The date/time we are converting
	 * @return Date The java date object for this
	 */
    public static Date convertDate(DateTime dt) {
        Date result = null;
        try {
            if (dt != null) {
                SimpleDateFormat gdf = new SimpleDateFormat(kGoogleDateFormat);
                result = gdf.parse(dt.toUiString());
            }
        } catch (Exception e) {
            ;
        }
        return result;
    }

    /**
	 * Gets our google service
	 * 
	 * @return GoogleService The service
	 */
    public static GoogleService getService() {
        GoogleService myService = null;
        try {
            myService = new BloggerService("thirdstreet-bloggerexport-1");
        } catch (Exception e) {
            throw new RuntimeException("Failed to get google service!", e);
        }
        return myService;
    }

    /**
	 * Gets our google service
	 * @param user The user id
	 * @param password The password
	 * @return GoogleService The service
	 */
    public static GoogleService getService(String user, String password) {
        GoogleService myService = null;
        try {
            myService = new GoogleService("blogger", "thirdstreet-bloggerexport-1");
            myService.setUserCredentials(user, password);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get google service!", e);
        }
        return myService;
    }

    /**
	 * Constructor - private as access is via static methods
	 */
    private GoogleUtils() {
        super();
    }
}
