package org.orbeon.oxf.util;

import org.apache.log4j.Logger;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class HttpUtils {

    private static Logger logger = LoggerFactory.createLogger(HttpUtils.class);

    private static final SimpleDateFormat dateHeaderFormats[] = { new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US), new SimpleDateFormat("EEEEEE, dd-MMM-yy HH:mm:ss zzz", Locale.US), new SimpleDateFormat("EEE MMMM d HH:mm:ss yyyy", Locale.US) };

    private static final TimeZone gmtZone = TimeZone.getTimeZone("GMT");

    static {
        for (int i = 0; i < dateHeaderFormats.length; i++) dateHeaderFormats[i].setTimeZone(gmtZone);
    }

    public static boolean checkIfModifiedSince(HttpServletRequest request, long lastModified) {
        String ifModifiedHeader = request.getHeader("If-Modified-Since");
        if (logger.isDebugEnabled()) logger.debug("Found If-Modified-Since header");
        if (ifModifiedHeader != null) {
            for (int i = 0; i < dateHeaderFormats.length; i++) {
                try {
                    Date date = dateHeaderFormats[i].parse(ifModifiedHeader);
                    if (date != null) {
                        if (lastModified <= (date.getTime() + 1000)) {
                            if (logger.isDebugEnabled()) logger.debug("Sending SC_NOT_MODIFIED response");
                            return true;
                        } else {
                            break;
                        }
                    }
                } catch (ParseException e) {
                }
            }
        }
        return false;
    }
}
