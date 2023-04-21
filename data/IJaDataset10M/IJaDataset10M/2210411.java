package org.fulworx.core.rest.restlet;

import org.restlet.data.Request;
import org.restlet.data.Response;

/**
 * @version $Id: $
 */
public class RequestUtil {

    /**
     * Handy for translating AltParameter objects from a Request object.  If no
     * "alt" parameter is found, go to the end of the resource uri
     * <p/>
     * If the "alt" is the end of the resource uri (.xml, .json, etc) then it is
     * removed from the URI and added as a query parameter.
     *
     * @param request submited
     * @return corresponding "alt" parameter value
     */
    public static AltParameter parseAlt(Request request) {
        AltParameter altParameter = null;
        if (request != null && request.getEntity() != null) {
            altParameter = AltParameter.getModeByMedia(request.getEntity().getMediaType());
        }
        return altParameter == null ? calculate(request) : altParameter;
    }

    public static AltParameter parseAlt(Response response) {
        AltParameter altParameter = null;
        if (response != null && response.getEntity() != null) {
            altParameter = AltParameter.getModeByMedia(response.getEntity().getMediaType());
        }
        return altParameter == null && response != null ? parseAlt(response.getRequest()) : altParameter;
    }

    public static AltParameter calculate(Request request) {
        AltParameter altParameter = null;
        if (request != null) {
            String alt = retrieveAlt(request);
            if (alt != null) {
                altParameter = AltParameter.getMode(alt);
            }
        }
        if (altParameter == null) {
            altParameter = AltParameter.XML;
        }
        return altParameter;
    }

    public static String retrieveAlt(Request request) {
        String alt = null;
        if (request != null && request.getResourceRef() != null) {
            alt = request.getResourceRef().getQueryAsForm().getFirstValue("alt");
            if (alt == null) {
                alt = findAlt(request);
            }
        }
        return alt;
    }

    public static String findAlt(Request request) {
        String alt = null;
        String path = request.getResourceRef().getPath();
        int index = path.lastIndexOf(".");
        if (index > 0) {
            alt = path.substring(index + 1);
        }
        return alt;
    }

    /**
     * Convert a '.' extension into an 'alt' parameter.  So basically, somthing.xml becomes
     * something?alt=xml
     *
     * @param request to convert
     */
    public static void convertAlt(Request request) {
        String path = request.getResourceRef().getPath();
        String alt = findAlt(request);
        AltParameter altMode = AltParameter.getMode(alt);
        if (altMode != null && alt != null) {
            String newPath = path.substring(0, path.length() - 1 - alt.length());
            String newQuery = createQuery(request.getResourceRef().getQuery(), alt);
            request.getResourceRef().setQuery(newQuery);
            request.getResourceRef().setPath(newPath);
        }
    }

    private static String createQuery(String query, String alt) {
        String altQuery = "alt=" + alt;
        if (query != null && query.length() > 0) {
            return query + "&" + altQuery;
        } else {
            return altQuery;
        }
    }
}
