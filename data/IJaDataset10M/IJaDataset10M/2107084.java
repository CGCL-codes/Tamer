package org.mbari.observatory.perfeval;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.jms.JMSException;
import javax.jms.Message;
import org.apache.log4j.Logger;
import org.doomdark.uuid.UUID;
import org.doomdark.uuid.UUIDGenerator;

/**
 * Some utilities related with performance evaluation (instrument proxy side).
 * Note that there is a similar (but not exact) utility class in OMFRouter.
 * These two utility classes should keep aligned wrt the names of the headers
 * used to capture timings.
 * 
 * <p>
 * TODO some of the exact replicate elements (eg., StopWatch) could only be
 * defined on the instrument proxy side and imported by OMFRouter.
 * 
 * @author carueda
 */
public class IpPerfUtils {

    public interface IpObserver {

        public void aboutToSend(String correlationId);

        public void justReceived(String correlationId);

        public void timingsCompleted(Map<String, Object> res);
    }

    private static IpObserver ipObserver = new IpObserver() {

        @Override
        public void aboutToSend(String correlationId) {
        }

        @Override
        public void justReceived(String correlationId) {
        }

        @Override
        public void timingsCompleted(Map<String, Object> res) {
        }
    };

    /**
     * Sets the IpObserver
     */
    public static void setIpObserver(IpObserver ipo) {
        if (ipo == null) {
            throw new IllegalArgumentException();
        }
        ipObserver = ipo;
    }

    private static final Logger log = Logger.getLogger(IpPerfUtils.class);

    /**
     * Indicates whether security related operations should be skipped, which is
     * the case if the environment variable IGNORE_SECURITY is defined with
     * value "y" (IGNORE_SECURITY=y).
     */
    public static final boolean IGNORE_SECURITY = "y".equals(System.getenv("IGNORE_SECURITY"));

    private static final String OMF_PREFIX = "Omf";

    private static final String NEXT_TIMING_INDEX = "OmfNti";

    private static final String NAME_FORMAT = "Omf%03dname";

    private static final String VALUE_FORMAT = "Omf%03dvalue";

    private static volatile int aboutToSend = 0;

    private static volatile int justReceived = 0;

    /**
     * Resets the given headers map for subsequent record of timing of
     * operations. The only property set upon return is "JMSCorrelationID" with
     * a newly generated UUID.
     * 
     * @param headers
     *            The Map to reset.
     */
    public static void resetHeaders(Map<String, Object> headers) {
        headers.clear();
        UUID uuid = UUIDGenerator.getInstance().generateTimeBasedUUID();
        String correlationId = uuid.toString();
        headers.put("JMSCorrelationID", correlationId);
    }

    /**
     * Transfers JMSCorrelationID, and OMF-related properties.
     * 
     * @param headers
     *            the map to read the properties from
     * @param message
     *            the affected message
     */
    public static void transferHeaders(Map<String, Object> headers, Message message) {
        if (log.isDebugEnabled()) {
            log.debug("transferHeaders " + headers);
        }
        Object corrId = headers.get("JMSCorrelationID");
        if (corrId == null) {
            throw new AssertionError("JMSCorrelationID expected in headers");
        }
        String correlationId = String.valueOf(corrId);
        try {
            message.setJMSCorrelationID(correlationId);
            for (Entry<String, Object> e : headers.entrySet()) {
                if (e.getKey().startsWith(OMF_PREFIX)) {
                    message.setStringProperty(e.getKey(), String.valueOf(e.getValue()));
                }
            }
        } catch (JMSException e) {
            log.warn("Error transfering headers", e);
        }
    }

    /**
     * Sets a timing property to the given headers map.
     * 
     * @param headers
     * @param timingName
     * @param timingValue
     */
    public static void setTimingHeader(Map<String, Object> headers, String timingName, long timingValue) {
        Object nextTimeIndex = headers.get(NEXT_TIMING_INDEX);
        int nti = nextTimeIndex == null ? 0 : Integer.parseInt(String.valueOf(nextTimeIndex));
        headers.put(String.format(NAME_FORMAT, nti), timingName);
        headers.put(String.format(VALUE_FORMAT, nti), timingValue);
        headers.put(NEXT_TIMING_INDEX, ++nti);
    }

    public static void setSentHeader(Map<String, Object> headers, String timingName) {
        _notifyAboutToSend(headers);
    }

    private static void _notifyAboutToSend(Map<String, Object> headers) {
        Object corrId = headers.get("JMSCorrelationID");
        if (corrId == null) {
            throw new AssertionError("JMSCorrelationID expected in headers");
        }
        String correlationId = String.valueOf(corrId);
        log.info("********** _notifyAboutToSend (" + aboutToSend + ") ==> correlationId = " + correlationId);
        ipObserver.aboutToSend(correlationId);
        aboutToSend++;
    }

    public static void setReceivedHeader(Map<String, Object> headers, String timingName) {
        _notifyJustReceived(headers);
    }

    private static void _notifyJustReceived(Map<String, Object> headers) {
        Object corrId = headers.get("JMSCorrelationID");
        if (corrId == null) {
            throw new AssertionError("JMSCorrelationID expected in headers");
        }
        String correlationId = String.valueOf(corrId);
        log.info("********  _notifyJustReceived (" + justReceived + ") <== correlationId = " + correlationId);
        ipObserver.justReceived(correlationId);
        justReceived++;
    }

    /**
     * To be called when the timing collection session has completed.
     * 
     * @param headers
     */
    public static void timingsCompleted(Map<String, Object> headers) {
        if (log.isDebugEnabled()) {
            log.debug("timingsCompleted: headers:");
            List<String> keys = new ArrayList<String>(headers.keySet());
            Collections.sort(keys);
            for (String key : keys) {
                log.debug(String.format("%10s %-20s = %s", "", key, headers.get(key)));
            }
        }
        Map<String, Object> res = new LinkedHashMap<String, Object>();
        _structureTimings(headers, res);
        _notifyTimingsCompleted(res);
    }

    private static void _notifyTimingsCompleted(Map<String, Object> res) {
        ipObserver.timingsCompleted(res);
    }

    /**
     * Adds a structured version of the timings to the given res map
     */
    private static void _structureTimings(Map<String, Object> headers, Map<String, Object> res) {
        res.put("JMSCorrelationID", headers.get("JMSCorrelationID"));
        Object nti = headers.get("OmfNti");
        final int counter = nti == null ? 0 : Integer.parseInt(nti.toString());
        double totalMs = 0;
        for (int i = 0; i < counter; i++) {
            String timingName = String.valueOf(headers.get(String.format(NAME_FORMAT, i)));
            long timingValue = Long.parseLong(String.valueOf(headers.get(String.format(VALUE_FORMAT, i))));
            double timingMs = (double) timingValue / 1e6;
            String key = String.format("%02d_%s", i, timingName);
            res.put(key, timingMs);
            totalMs += timingMs;
        }
        res.put("total", totalMs);
    }

    /**
     * A simple stop watch for execution timing. Usage:
     * 
     * <pre>
     * sw = new StopWatch().start();
     * ... code to time ...
     * nanosec1 = sw.stop();
     * ...
     * sw.start();
     * ... other code to time ...
     * nanosec2 = sw.stop();
     * </pre>
     * 
     * @author carueda
     */
    public static final class StopWatch {

        private long start;

        /**
         * Starts the stop watch
         * 
         * @return this
         */
        public StopWatch start() {
            start = System.nanoTime();
            return this;
        }

        /**
         * @return the elapse time (nanosec precision) from the latest call to
         *         {@link #start()}.
         */
        public long stop() {
            return System.nanoTime() - start;
        }
    }

    private IpPerfUtils() {
    }
}
