package org.openthinclient.syslogd;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.ParseException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.levigo.util.collections.IntHashtable;

public abstract class SyslogDaemon implements Runnable {

    private static final int IN_BUF_SZ = (8 * 1024);

    public static final int SYSLOG_PORT = 514;

    public enum Facility {

        LOG_KERN(0, "kernel"), LOG_USER(1, "user"), LOG_MAIL(2, "mail"), LOG_DAEMON(3, "daemon"), LOG_AUTH(4, "authentication"), LOG_SYSLOG(5, "syslog"), LOG_LPR(6, "lpr"), LOG_NEWS(7, "news"), LOG_UUCP(8, "uucp"), LOG_CRON(9, "cron"), LOG_LOCAL0(16, "local0"), LOG_LOCAL1(17, "local1"), LOG_LOCAL2(18, "local2"), LOG_LOCAL3(19, "local3"), LOG_LOCAL4(20, "local4"), LOG_LOCAL5(21, "local5"), LOG_LOCAL6(22, "local6"), LOG_LOCAL7(23, "local7");

        private static IntHashtable byValue;

        private final int code;

        private final String fullName;

        private Facility(int code, String fullName) {
            this.code = code;
            this.fullName = fullName;
        }

        public String getFullName() {
            return fullName;
        }

        private static final int LOG_FACMASK = 0x03F8;

        public static Facility fromCode(int code) {
            if (null == Facility.byValue) {
                Facility.byValue = new IntHashtable();
                for (Facility f : values()) Facility.byValue.put(f.code, f);
            }
            return (Facility) byValue.get(getCode(code));
        }

        /**
		 * @param code
		 * @return
		 */
        public static int getCode(int code) {
            return (code & LOG_FACMASK) >> 3;
        }
    }

    @SuppressWarnings("deprecation")
    public static enum Priority {

        LOG_EMERG(0, "emergency", org.apache.log4j.Priority.FATAL), LOG_ALERT(1, "alert", org.apache.log4j.Priority.FATAL), LOG_CRIT(2, "critical", org.apache.log4j.Priority.FATAL), LOG_ERR(3, "error", org.apache.log4j.Priority.ERROR), LOG_WARNING(4, "warning", org.apache.log4j.Priority.WARN), LOG_NOTICE(5, "notice", org.apache.log4j.Priority.INFO), LOG_INFO(6, "info", org.apache.log4j.Priority.INFO), LOG_DEBUG(7, "debug", org.apache.log4j.Priority.DEBUG), LOG_ALL(8, "all", org.apache.log4j.Priority.DEBUG);

        private static IntHashtable byValue;

        private final String fullName;

        private final org.apache.log4j.Priority l4jPriority;

        private final int code;

        private Priority(int code, String name, org.apache.log4j.Priority l4jPriority) {
            this.code = code;
            this.fullName = name;
            this.l4jPriority = l4jPriority;
        }

        public String getFullName() {
            return fullName;
        }

        public org.apache.log4j.Priority getL4jPriority() {
            return l4jPriority;
        }

        private static final int LOG_PRIMASK = 0x07;

        public static Priority fromCode(int code) {
            if (null == Priority.byValue) {
                Priority.byValue = new IntHashtable();
                for (Priority p : values()) Priority.byValue.put(p.code, p);
            }
            return (Priority) byValue.get(getCode(code));
        }

        /**
		 * @param code
		 * @return
		 */
        public static int getCode(int code) {
            return code & LOG_PRIMASK;
        }
    }

    private final int port;

    private final DatagramSocket socket;

    private boolean shutdownRequested;

    public SyslogDaemon() throws SocketException {
        this(SYSLOG_PORT);
    }

    public SyslogDaemon(int port) throws SocketException {
        super();
        this.port = port;
        socket = new DatagramSocket(this.port);
    }

    @Override
    public void finalize() {
        this.shutdown();
    }

    public void shutdown() {
        this.shutdownRequested = true;
        if (null != socket && !socket.isClosed()) socket.close();
    }

    public void run() {
        byte[] buffer = new byte[IN_BUF_SZ];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        while (!shutdownRequested) {
            try {
                socket.receive(packet);
            } catch (IOException e) {
                handleError("Error receiving message: ", e);
                break;
            }
            try {
                this.processMessage(packet);
            } catch (Exception e) {
                handleError("Exception processing message: ", e);
            }
        }
    }

    public void processMessage(DatagramPacket packet) {
        try {
            String message = new String(packet.getData(), 0, packet.getLength(), "UTF-8");
            try {
                Pattern messagePattern = Pattern.compile("^<(\\d{1,3})>(\\p{ASCII}{3} \\d{2} \\d{2}:\\d{2}:\\d{2})\\s+" + "(\\p{XDigit}{2}:\\p{XDigit}{2}:\\p{XDigit}{2}:\\p{XDigit}{2}:\\p{XDigit}{2}:\\p{XDigit}{2})\\s*" + "(.*)$");
                Matcher m = messagePattern.matcher(message);
                if (m.matches()) {
                    Priority priority;
                    Facility facility;
                    String priStr = m.group(1);
                    if (priStr.length() == 0) {
                        priority = Priority.LOG_INFO;
                        facility = Facility.LOG_USER;
                    } else {
                        try {
                            int code = Integer.parseInt(priStr);
                            priority = Priority.fromCode(code);
                            if (null == priority) {
                                handleError("No Priority for code " + Priority.getCode(code), null);
                                priority = Priority.LOG_INFO;
                            }
                            facility = Facility.fromCode(code);
                            if (null == facility) {
                                handleError("No Facility for code " + Facility.getCode(code), null);
                                facility = Facility.LOG_USER;
                            }
                        } catch (NumberFormatException e) {
                            priority = Priority.LOG_INFO;
                            facility = Facility.LOG_USER;
                        }
                    }
                    Date timestamp = new Date();
                    try {
                        timestamp = TimestampFormat.getInstance().parse(m.group(2));
                    } catch (ParseException e) {
                        handleError("Can't parse timestamp " + message.substring(0, 15), null);
                        timestamp = new Date();
                    }
                    handleMessage(packet.getAddress(), m.group(3), priority, facility, timestamp, m.group(4));
                } else handleMessage(packet.getAddress(), null, Priority.LOG_INFO, Facility.LOG_USER, new Date(), message);
            } catch (Throwable t) {
                handleError("Can't parse message: " + message, t);
            }
        } catch (UnsupportedEncodingException e) {
        }
    }

    /**
	 * @param message
	 * @param t
	 */
    protected abstract void handleError(String message, Throwable t);

    protected abstract void handleMessage(InetAddress source, String hostname, Priority prio, Facility facility, Date timestamp, String message);
}
