package phex.http;

import phex.common.log.NLogger;

/**
 * 
 */
public class HTTPRetryAfter {

    /**
     * @return
     */
    public static int parseDeltaInSeconds(HTTPHeader header) {
        String valueStr = header.getValue();
        int value;
        try {
            value = Integer.parseInt(valueStr);
            return value;
        } catch (NumberFormatException exp) {
            NLogger.warn(HTTPRetryAfter.class, "Cant parse RetryAfter header.", exp);
            return -1;
        }
    }
}
