package org.zoolu.sip.provider;

import org.zoolu.tools.Configure;
import org.zoolu.tools.Parser;
import org.zoolu.tools.EventLogger;
import org.zoolu.tools.RotatingLog;
import org.zoolu.tools.Timer;
import java.io.*;
import java.util.Vector;

/** SipStack includes all static attributes used by the sip stack.
  * <p>
  * Static attributes includes the logging configuration,
  * default SIP port, deafult supported transport protocols, timeouts, etc.
  */
public class SipStack extends Configure {

    /** Whether SipStack configuration has been already loaded */
    private static boolean is_init = false;

    /** Release */
    public static final String release = "mjsip stack 1.6";

    /** Authors */
    public static final String authors = "Luca Veltri - University of Parma (Italy)";

    /** Default SIP port.
    * Note that this is not the port used by the running stack, but simply the standard default SIP port.
    * <br> Normally it sould be set to 5060 as defined by RFC 3261. Using a different value may cause
    * some problems when interacting with other unaware SIP UAs. */
    public static int default_port = 5060;

    /** Default supported transport protocols. */
    public static String[] default_transport_protocols = { SipProvider.PROTO_UDP, SipProvider.PROTO_TCP };

    /** Default max number of contemporary open transport m_hashConnections. */
    public static int default_nmax_connections = 32;

    /** Whether adding 'rport' parameter on via header fields of outgoing requests. */
    public static boolean use_rport = true;

    /** Whether adding (forcing) 'rport' parameter on via header fields of incoming requests. */
    public static boolean force_rport = false;

    /** default max-forwards value (RFC3261 recommends value 70) */
    public static int max_forwards = 70;

    /** starting retransmission timeout (milliseconds); called T1 in RFC2361; they suggest T1=500ms */
    public static long retransmission_timeout = 500;

    /** maximum retransmission timeout (milliseconds); called T2 in RFC2361; they suggest T2=4sec */
    public static long max_retransmission_timeout = 4000;

    /** transaction timeout (milliseconds); RFC2361 suggests 64*T1=32000ms */
    public static long transaction_timeout = 32000;

    /** clearing timeout (milliseconds); T4 in RFC2361; they suggest T4=5sec */
    public static long clearing_timeout = 5000;

    /** Whether using only one thread for all timer instances. */
    public static boolean single_timer = false;

    /** Whether 1xx responses create an "early dialog" for methods that create dialog. */
    public static boolean early_dialog = false;

    /** Default 'expires' value in seconds. RFC2361 suggests 3600s as default value. */
    public static int default_expires = 3600;

    /** UA info included in request messages in the 'User-Agent' header field.
     * Use "NONE" if the 'User-Agent' header filed must not be added. */
    public static String ua_info = release;

    /** Server info included in response messages in the 'Server' header field
     * Use "NONE" if the 'Server' header filed must not be added. */
    public static String server_info = release;

    /** Base level (offset) for logging Transport events */
    public static int LOG_LEVEL_TRANSPORT = 1;

    /** Base level (offset) for logging Transaction events */
    public static int LOG_LEVEL_TRANSACTION = 2;

    /** Base level (offset) for logging Dialog events */
    public static int LOG_LEVEL_DIALOG = 2;

    /** Base level (offset) for logging Call events */
    public static int LOG_LEVEL_CALL = 1;

    /** Base level (offset) for logging UA events */
    public static int LOG_LEVEL_UA = 0;

    /** EventLogger level. Only logs with a level less or equal to this are written. */
    public static int debug_level = 1;

    /** Path for the log folder where log files are written.
     * By default, it is used the "./log" folder.
     * Use ".", to store logs in the current root folder. */
    public static String log_path = "log";

    /** The size limit of the log file [kB] */
    public static int max_logsize = 2048;

    /** The number of rotations of log files. Use '0' for NO rotation, '1' for rotating a single file */
    public static int log_rotations = 0;

    /** The rotation period, in MONTHs or DAYs or HOURs or MINUTEs
     * examples: log_rotation_time=3 MONTHS, log_rotations=90 DAYS 
     * Default value: log_rotation_time=2 MONTHS */
    private static String log_rotation_time = null;

    /** The rotation time scale */
    public static int rotation_scale = RotatingLog.MONTH;

    /** The rotation time value */
    public static int rotation_time = 2;

    /** Parses a single text line (read from the config file) */
    protected void parseLine(String line) {
        String attribute;
        Parser par;
        int index = line.indexOf("=");
        if (index > 0) {
            attribute = line.substring(0, index).trim();
            par = new Parser(line, index + 1);
        } else {
            attribute = line;
            par = new Parser("");
        }
        char[] delim = { ' ', ',' };
        if (attribute.equals("default_port")) {
            default_port = par.getInt();
            return;
        }
        if (attribute.equals("default_transport_protocols")) {
            default_transport_protocols = par.getWordArray(delim);
            return;
        }
        if (attribute.equals("default_nmax_connections")) {
            default_nmax_connections = par.getInt();
            return;
        }
        if (attribute.equals("use_rport")) {
            use_rport = (par.getString().toLowerCase().startsWith("y"));
            return;
        }
        if (attribute.equals("force_rport")) {
            force_rport = (par.getString().toLowerCase().startsWith("y"));
            return;
        }
        if (attribute.equals("max_forwards")) {
            max_forwards = par.getInt();
            return;
        }
        if (attribute.equals("retransmission_timeout")) {
            retransmission_timeout = par.getInt();
            return;
        }
        if (attribute.equals("max_retransmission_timeout")) {
            max_retransmission_timeout = par.getInt();
            return;
        }
        if (attribute.equals("transaction_timeout")) {
            transaction_timeout = par.getInt();
            return;
        }
        if (attribute.equals("clearing_timeout")) {
            clearing_timeout = par.getInt();
            return;
        }
        if (attribute.equals("single_timer")) {
            single_timer = (par.getString().toLowerCase().startsWith("y"));
            return;
        }
        if (attribute.equals("early_dialog")) {
            early_dialog = (par.getString().toLowerCase().startsWith("y"));
            return;
        }
        if (attribute.equals("default_expires")) {
            default_expires = par.getInt();
            return;
        }
        if (attribute.equals("ua_info")) {
            ua_info = par.getRemainingString().trim();
            return;
        }
        if (attribute.equals("server_info")) {
            server_info = par.getRemainingString().trim();
            return;
        }
        if (attribute.equals("debug_level")) {
            debug_level = par.getInt();
            return;
        }
        if (attribute.equals("log_path")) {
            log_path = par.getString();
            return;
        }
        if (attribute.equals("max_logsize")) {
            max_logsize = par.getInt();
            return;
        }
        if (attribute.equals("log_rotations")) {
            log_rotations = par.getInt();
            return;
        }
        if (attribute.equals("log_rotation_time")) {
            log_rotation_time = par.getRemainingString();
            return;
        }
        if (attribute.equals("host_addr")) printLog("WARNING: parameter 'host_addr' is no more supported; use 'via_addr' instead.");
        if (attribute.equals("all_interfaces")) printLog("WARNING: parameter 'all_interfaces' is no more supported; use 'host_iaddr' for setting a specific interface or let it undefined.");
        if (attribute.equals("use_outbound")) printLog("WARNING: parameter 'use_outbound' is no more supported; use 'outbound_addr' for setting an outbound proxy or let it undefined.");
        if (attribute.equals("log_file")) printLog("WARNING: parameter 'log_file' is no more supported.");
    }

    /** Converts the entire object into lines (to be saved into the config file) */
    protected String toLines() {
        return "SipStack/" + release;
    }

    /** Costructs a non-static SipStack */
    private SipStack() {
    }

    public static void verifyInitialized() {
        if (!isInit()) init();
    }

    /** Inits SipStack */
    public static void init() {
        init(null);
    }

    /** Inits SipStack from the specified <i>file</i> */
    public static void init(String file) {
        (new SipStack()).loadFile(file);
        if (ua_info != null && (ua_info.length() == 0 || ua_info.equalsIgnoreCase(Configure.NONE) || ua_info.equalsIgnoreCase("NO-UA-INFO"))) ua_info = null;
        if (server_info != null && (server_info.length() == 0 || server_info.equalsIgnoreCase(Configure.NONE) || server_info.equalsIgnoreCase("NO-SERVER-INFO"))) server_info = null;
        Timer.SINGLE_THREAD = single_timer;
        if (debug_level > 0) {
            if (log_rotation_time != null) {
                SipParser par = new SipParser(log_rotation_time);
                rotation_time = par.getInt();
                String scale = par.getString();
                if (scale == null) scale = "null";
                if (scale.toUpperCase().startsWith("MONTH")) rotation_scale = RotatingLog.MONTH; else if (scale.toUpperCase().startsWith("DAY")) rotation_scale = RotatingLog.DAY; else if (scale.toUpperCase().startsWith("HOUR")) rotation_scale = RotatingLog.HOUR; else if (scale.toUpperCase().startsWith("MINUTE")) rotation_scale = RotatingLog.MINUTE; else {
                    rotation_time = 7;
                    rotation_scale = RotatingLog.DAY;
                    printLog("Error with the log rotation time. Logs will rotate every week.");
                }
            }
        }
        is_init = true;
    }

    /** Whether SipStack has been already initialized */
    public static boolean isInit() {
        return is_init;
    }

    /** Logs a string message. */
    private static void printLog(String str) {
        System.out.println("SipStack: " + str);
    }
}
