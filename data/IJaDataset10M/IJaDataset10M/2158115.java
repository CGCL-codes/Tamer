package org.opengpx.lib.tools;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Martin Preishuber
 *
 */
public class ISODateTime {

    private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

    private static final Logger mLogger = LoggerFactory.getLogger(ISODateTime.class);

    /**
	 * 
	 * @param isoDateString
	 * @return
	 */
    public static Date parseString(String isoDateString) {
        Date dateTime = null;
        try {
            final DateFormat iSO8601Local = new SimpleDateFormat(DATE_FORMAT_PATTERN);
            dateTime = iSO8601Local.parse(isoDateString);
        } catch (ParseException ex) {
            mLogger.error("Unable to parse ISO8601 date '" + isoDateString + "'");
            ex.printStackTrace();
        }
        return dateTime;
    }
}
