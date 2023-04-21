package org.openthinclient.syslogd;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.apache.log4j.spi.LoggingEvent;

/**
 * @author levigo
 */
public class Log4JSyslogDaemon extends SyslogDaemon {

    private static final Logger logger = Logger.getLogger(Log4JSyslogDaemon.class);

    private Map<Facility, Logger> loggers = new HashMap<Facility, Logger>();

    private final String prefix;

    public Log4JSyslogDaemon() throws SocketException {
        super();
        this.prefix = "syslog";
    }

    public Log4JSyslogDaemon(int port) throws SocketException {
        this(port, "syslog");
    }

    public Log4JSyslogDaemon(int port, String prefix) throws SocketException {
        super(port);
        this.prefix = prefix;
    }

    @Override
    protected void handleMessage(InetAddress source, String hostname, Priority prio, Facility facility, Date timestamp, String message) {
        Logger logger = loggers.get(facility);
        if (null == logger) {
            logger = Logger.getLogger(prefix + "." + facility.getFullName());
            loggers.put(facility, logger);
        }
        LoggingEvent le = new LoggingEvent("foo", logger, timestamp.getTime(), prio.getL4jPriority(), message, null);
        MDC.put("hostname", hostname != null ? hostname : "-");
        MDC.put("peer", null != source ? source.getHostAddress() : "-");
        logger.callAppenders(le);
    }

    @Override
    protected void handleError(String message, Throwable t) {
        logger.error(message, t);
    }
}
