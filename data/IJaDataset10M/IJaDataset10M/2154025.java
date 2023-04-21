package au.edu.uq.itee.maenad.restlet.util;

import java.util.concurrent.atomic.AtomicLong;
import org.restlet.data.Cookie;
import org.restlet.data.CookieSetting;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.util.Series;

/**
 * The purpose of CookieUtility is to exercise current best practices for
 * certain idiomatic uses of cookies: browser identification, substitute
 * authentication tickets, etc.
 * <p>
 * This class aims to minimize the incidence of common pitfalls, e.g. forgetting
 * to set useful attributes, choosing poor values, etc. It cannot, obviously,
 * overcome inherent security or architectural flaws associated with the use of
 * cookies.
 *
 * @author Rob Heittman <rob.heittman@solertium.com>
 *
 */
public class CookieUtility {

    /**
     * The numbers that make up cookie IDs are encoded for compactness and to
     * enable efficient layout of the IDs in any related storage based on name.
     * The encoding symbols chosen exclude vowels, which reduces the likelihood
     * of the encoded value spelling an obscenity or other undesirable word.
     * Encoding symbols are chosen to be usable in filesystems and URIs without
     * any escape operations.
     */
    private static final String ENCODING_SYMBOLS = "BbCcDdFfGgHhJjKkLlMmNnPpQqRrSsTtVvWwXxZz1234567890";

    /**
     * Up to Integer.INTEGER_MAX unique IDs can be generated each second of
     * runtime (2^31; 2.1 billion).
     */
    private static AtomicLong incr = new AtomicLong();

    /**
     * J2EE servers send a JSESSIONID cookie. Microsoft servers send
     * ASPSESSIONID, among other things. This cookie name is generic and
     * sufficiently descriptive; it does not do anything other than identify the
     * current browser instance to the server.
     */
    private static final String NAME_HTTP_BROWSER_ID = "BROWSERID";

    /**
     * Returns an existing HTTPBrowserID cookie value, or creates, attempts to
     * set, and returns a new HTTPBrowserID cookie value. This method always
     * returns a valid HTTPBrowserID string, but there is no guarantee that the
     * client will use this string on subsequent requests.
     *
     * @param request
     * @param response
     * @return HTTPBrowserID cookie value
     */
    public static String associateHTTPBrowserID(final Request request, final Response response) {
        final Cookie existingID = request.getCookies().getFirst(NAME_HTTP_BROWSER_ID);
        if (existingID == null) {
            final CookieSetting newID = CookieUtility.createHTTPBrowserID();
            request.getCookies().add(newID);
            if (response != null) {
                response.getCookieSettings().add(newID);
            }
            return newID.getValue();
        }
        return existingID.getValue();
    }

    /**
     * Create and return a new HTTPBrowserID CookieSetting object containing a
     * newly assigned ID value.
     *
     * @return CookieSetting containing HTTPBrowserID
     */
    public static CookieSetting createHTTPBrowserID() {
        final CookieSetting cs = new CookieSetting();
        cs.setName(NAME_HTTP_BROWSER_ID);
        cs.setPath("/");
        cs.setValue(CookieUtility.newUniqueID());
        cs.setAccessRestricted(true);
        System.out.println("Setup new id " + cs.getValue());
        return cs;
    }

    /**
     * Returns any EXISTING HTTPBrowserID cookie value associated with the
     * current request. Does not create a new HTTPBrowserID.
     *
     * @param request
     * @return HTTPBrowserID cookie value
     */
    public static String getHTTPBrowserID(final Request request) {
        return CookieUtility.getHTTPBrowserID(request.getCookies());
    }

    /**
     * Returns any EXISTING HTTPBrowserID cookie value associated with the
     * supplied Series of Cookies. Does not create a new HTTPBrowserID.
     *
     * @param request
     * @return HTTPBrowserID cookie value
     */
    public static String getHTTPBrowserID(final Series<Cookie> cookies) {
        final Cookie existingID = cookies.getFirst(NAME_HTTP_BROWSER_ID);
        if (existingID == null) {
            return null;
        }
        return existingID.getValue();
    }

    /**
     * Converts to arbitrary radix using ENCODING_SYMBOLS.  Suggestions
     * are welcome for how to improve this.
     *
     * @param id the Number to be converted to the encoded form
     * @return the result in encoded form
     */
    private static String idToAlpha(final Number id) {
        double old = id.doubleValue();
        final double radix = ENCODING_SYMBOLS.length();
        String result = "";
        while (true) {
            final double gnu = Math.floor(old / radix);
            final double remainder = old - (radix * (gnu));
            final int digit = (int) remainder;
            result = ENCODING_SYMBOLS.charAt(digit) + result;
            old = gnu;
            if (old == 0) {
                break;
            }
        }
        return result;
    }

    /**
     * Obtains a new guaranteed unique ID that is compact, contains
     * enough randomness to make it difficult to guess, and is
     * guaranteed unique for this generator.
     *
     * @return a String with the encoded ID.
     */
    public static String newUniqueID() {
        final long csec = System.currentTimeMillis() / 1000L;
        final long lincr = incr.getAndIncrement();
        if (lincr > Integer.MAX_VALUE) {
            incr.set(0);
        }
        String rnd = "" + Math.random();
        try {
            final long lrnd = Long.parseLong(rnd.substring(2));
            rnd = CookieUtility.idToAlpha(lrnd);
            if (rnd.length() > 8) {
                rnd = rnd.substring(0, 8);
            }
        } catch (final RuntimeException ignored) {
        }
        String scsec = "" + csec;
        try {
            scsec = CookieUtility.idToAlpha(csec);
        } catch (final RuntimeException ignored) {
        }
        String sincr = "" + lincr;
        try {
            sincr = CookieUtility.idToAlpha(lincr);
        } catch (final RuntimeException ignored) {
        }
        return rnd + "-" + scsec + "-" + sincr;
    }

    /**
     * Attempts to set and return a new HTTPBrowserID cookie value, replacing
     * any current value sent by the client. This method always returns a valid
     * HTTPBrowserID string, but there is no guarantee that the client will use
     * this string on subsequent requests.
     *
     * @param request
     * @param response
     * @return HTTPBrowserID cookie value
     */
    public static String resetHTTPBrowserID(final Request request, final Response response) {
        request.getCookies().removeAll(NAME_HTTP_BROWSER_ID);
        final CookieSetting newID = CookieUtility.createHTTPBrowserID();
        response.getCookieSettings().add(newID);
        return newID.getValue();
    }
}
