package uk.org.primrose;

import java.io.*;
import java.text.ParseException;
import java.util.*;

/** 
*        Logging class for the pools. 
*/
public class Logger {

    private PrintWriter logWriter = null;

    private boolean logVerbose = false;

    private boolean logInfo = false;

    private boolean logWarn = false;

    private boolean logError = false;

    private boolean logCrisis = false;

    private int syslogFacility = -1;

    private int syslogPriority = -1;

    private boolean useSyslog = false;

    private String logLevel;

    private int logDayOfMonth = -1;

    private String origLogName = null;

    private String emailEvents = null;

    private String mxServer = null;

    private String mxServerPort = null;

    private String toAddress = null;

    private String poolName = null;

    public void setLogWriter(String log) throws IOException {
        DebugLogger.log("Using log(" + log + ")");
        if (log == null) return;
        if (log.toLowerCase().startsWith("syslog")) {
            String[] parts = log.split("\\.");
            if (parts.length == 3) {
                String facility = parts[1];
                String priority = parts[2];
                try {
                    syslogPriority = SyslogDefs.getPriority(priority);
                    syslogFacility = SyslogDefs.getFacility(facility);
                    useSyslog = true;
                    Syslog.open("localhost", "primrose", 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (origLogName == null) {
                origLogName = log;
            }
            Calendar cal = Calendar.getInstance();
            int localLogDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
            boolean makeNewLog = false;
            if ((logDayOfMonth == -1) && (logWriter == null)) {
                makeNewLog = true;
            }
            if ((origLogName.indexOf("${") > -1) && (localLogDayOfMonth != logDayOfMonth)) {
                String dateFormat = origLogName.substring(origLogName.indexOf("${") + 2, origLogName.indexOf("}"));
                String logPrefix = origLogName.substring(0, origLogName.indexOf("${"));
                String logSuffix = origLogName.substring(origLogName.indexOf("}") + 1, origLogName.length());
                java.text.SimpleDateFormat sdf2 = new java.text.SimpleDateFormat(dateFormat);
                java.util.Date tmp = new java.util.Date();
                String formattedDate = sdf2.format(tmp);
                log = (logPrefix + formattedDate + logSuffix);
                logDayOfMonth = localLogDayOfMonth;
                makeNewLog = true;
            }
            if (makeNewLog) {
                if (DebugLogger.getEnabled()) {
                    DebugLogger.log("[Logger@" + poolName + "] Making new log (" + log + ")");
                }
                if (logWriter != null) {
                    logWriter.close();
                }
                logWriter = new PrintWriter(new FileOutputStream(log, true), true);
            }
        }
    }

    public void setEmailDetails(String emailEvents, String toAddress, String mxServer, String mxServerPort, String poolName) {
        if ((emailEvents != null) && (emailEvents.length() > 0) && !emailEvents.equals("null")) {
            this.emailEvents = emailEvents.toUpperCase();
            this.emailEvents += ",CRISIS";
            this.toAddress = toAddress;
            this.mxServer = mxServer;
            this.mxServerPort = mxServerPort;
            this.poolName = poolName;
        }
    }

    public void setLogLevel(String level) {
        if ((level != null) && (level.length() > 0)) {
            String[] levels = level.split(",");
            for (int i = 0; i < levels.length; i++) {
                if (levels[i].equalsIgnoreCase("verbose")) {
                    logVerbose = true;
                } else if (levels[i].equalsIgnoreCase("info")) {
                    logInfo = true;
                } else if (levels[i].equalsIgnoreCase("warn")) {
                    logWarn = true;
                } else if (levels[i].equalsIgnoreCase("error")) {
                    logError = true;
                } else if (levels[i].equalsIgnoreCase("crisis")) {
                    logCrisis = true;
                } else if (levels[i].equalsIgnoreCase("debug")) {
                    DebugLogger.setEnabled(true);
                }
            }
        }
        this.logLevel = level;
    }

    /** 
    *        Print a stack trace from an exception to the logs 
    */
    public void printStackTrace(Throwable t) {
        if (logWriter != null) {
            t.printStackTrace(logWriter);
        } else {
            t.printStackTrace(System.err);
        }
        if (DebugLogger.getEnabled()) {
            DebugLogger.printStackTrace(t);
        }
    }

    /** 
    *        Close the logger's file handle. 
    */
    public void close() {
        if (logWriter != null) {
            logWriter.close();
        }
    }

    /** 
    *        Log verbose messages 
    */
    public void verbose(String data) {
        if (logVerbose) {
            log("VERBOSE", data);
        }
        if (DebugLogger.getEnabled()) {
            DebugLogger.log(data);
        }
    }

    /** 
    *        Log an info message 
    */
    public void info(byte[] data) {
        if (logInfo) {
            log("INFO", new String(data));
        }
    }

    /** 
    *        Log an info message 
    */
    public void info(String data) {
        if (logInfo) {
            log("INFO", data);
        }
        if (DebugLogger.getEnabled()) {
            DebugLogger.log(data);
        }
    }

    /** 
    *        Log an warn message 
    */
    public void warn(String data) {
        if (logWarn) {
            log("WARN", data);
        }
        if (DebugLogger.getEnabled()) {
            DebugLogger.log(data);
        }
    }

    /** 
    *        Log an error message 
    */
    public void error(String data) {
        if (logError) {
            log("ERROR", data);
        }
        if (DebugLogger.getEnabled()) {
            DebugLogger.log(data);
        }
    }

    public void email(String eventType, String message) {
        if ((emailEvents == null) || (emailEvents.indexOf(eventType.toUpperCase()) == -1)) {
            return;
        }
        if (DebugLogger.getEnabled()) {
            DebugLogger.log("About to email event " + eventType + " :: " + message);
        }
        String host = "unknown_host";
        try {
            host = java.net.InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            printStackTrace(e);
        }
        String fromAddress = "pools@primrose.org.uk";
        String subject = eventType + " : " + poolName + "@" + host;
        info("Sending email for eventType(" + eventType + "), toAddress(" + toAddress + "), fromAddress(" + fromAddress + ") message(" + message + ")");
        try {
            new SendMail(this, mxServer, mxServerPort, toAddress, fromAddress, subject, message).send();
        } catch (Exception e) {
            printStackTrace(e);
        }
    }

    /** 
    *        Log an fatal message and email the world ! 
    */
    public void crisis(String message) {
        if (logCrisis) {
            log("CRISIS", message);
        }
        if (DebugLogger.getEnabled()) {
            DebugLogger.log(message);
        }
        if (emailEvents == null) {
            return;
        }
        email("CRISIS", message);
    }

    /** 
    *        Log an info message 
    */
    private void log(String level, String data) {
        try {
            this.setLogWriter(this.origLogName);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        Calendar now = Calendar.getInstance();
        String nowString = now.get(Calendar.DAY_OF_MONTH) + "/" + (now.get(Calendar.MONTH) + 1) + "/" + now.get(Calendar.YEAR) + " " + now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE) + ":" + now.get(Calendar.SECOND);
        String message = nowString + " : " + level + ": " + data;
        if (useSyslog) {
            try {
                Syslog.log(syslogFacility, syslogPriority, message);
            } catch (SyslogException e) {
                e.printStackTrace();
            }
        } else if (logWriter == null) {
            System.out.println(message);
        } else {
            logWriter.println(message);
        }
    }

    /** 
    *        Print a blank new line / line break to the log 
    */
    public void linebreak() {
        if (logWriter == null) {
            System.out.println("\n");
        } else {
            logWriter.println("\n");
        }
    }

    public int getLogDayOfMonth() {
        return logDayOfMonth;
    }

    public void setLogDayOfMonth(int logDayOfMonth) {
        this.logDayOfMonth = logDayOfMonth;
    }

    public String getLogLevel() {
        return logLevel;
    }
}
