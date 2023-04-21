package org.starobjects.restful.viewer.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.ws.rs.WebApplicationException;
import org.starobjects.restful.viewer.Constants;

public final class UrlDecoderUtils {

    private UrlDecoderUtils() {
    }

    public static String urlDecode(final String oidStr) {
        try {
            return URLDecoder.decode(oidStr, Constants.URL_ENCODING_CHAR_SET);
        } catch (UnsupportedEncodingException e) {
            throw new WebApplicationException(e);
        }
    }
}
