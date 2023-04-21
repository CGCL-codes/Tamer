package org.darkimport.omeglespy_z;

import java.net.URL;

/**
 * Requests for communication with an external service are passed through this
 * class. The most basic implementation will simply use http to pass
 * communication requests to the service. However, more complicated
 * implementations might employ a proxy or some kind of browser emulation
 * pattern.
 * 
 * Note that this API will probably change in the future to be more transport
 * agnostic.
 * 
 * @author user
 * @version $Id: $
 */
public abstract class CommunicationHelper {

    private static CommunicationHelper _instance = new DefaultCommunicationHelper();

    /**
	 * Initializes the CommunicationHelper with the given implementation.
	 * 
	 * @param communicationHelper
	 *            a {@link org.darkimport.omeglespy_z.CommunicationHelper}
	 *            object.
	 */
    public static void initialize(final CommunicationHelper communicationHelper) {
        _instance = communicationHelper;
    }

    /**
	 * Make a request.
	 * 
	 * 
	 * @param url
	 *            a {@link java.net.URL} object. The requested URL.
	 * @param post
	 *            a boolean. Is the request HTTP POST?
	 * @param post_data
	 *            a {@link java.lang.String} object. The data to be posted. key,
	 *            value, key, value, etc.
	 * @return a {@link java.lang.String} object. The response.
	 * @throws java.lang.Exception
	 *             if any communication errors have occurred.
	 */
    public static String wget(final URL url, final boolean post, final String... post_data) throws Exception {
        return wget(url, post, false, post_data);
    }

    /**
	 * Make a request.
	 * 
	 * @param url
	 *            a {@link java.net.URL} object. The requested URL.
	 * @param post
	 *            a boolean. Is the request HTTP POST?
	 * @param ignore
	 *            a boolean. Do we ignore the response?
	 * @param post_data
	 *            a {@link java.lang.String} object. The data to be posted. key,
	 *            value, key, value, etc.
	 * @return a {@link java.lang.String} object.
	 * @throws java.lang.Exception
	 *             if any communication errors have occurred.
	 */
    public static String wget(final URL url, final boolean post, final boolean ignore, final String... post_data) throws Exception {
        return _instance.doWget(url, post, ignore, post_data);
    }

    /**
	 * Sub classes make the request here.
	 * 
	 * @param url
	 *            a {@link java.net.URL} object. The requested URL.
	 * @param post
	 *            a boolean. Is the request HTTP POST?
	 * @param ignore
	 *            a boolean. Do we ignore the response?
	 * @param post_data
	 *            a {@link java.lang.String} object.The data to be posted. key,
	 *            value, key, value, etc.
	 * @return a {@link java.lang.String} object.
	 * @throws java.lang.Exception
	 *             if any communication errors have occurred.
	 */
    protected abstract String doWget(final URL url, final boolean post, final boolean ignore, final String... post_data) throws Exception;
}
