package org.ws4d.osgi.proxyGenerator.logging;

import java.util.Calendar;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;
import org.ws4d.java.util.Properties;
import org.ws4d.osgi.constants.GlobalConstants;
import org.ws4d.osgi.proxyGenerator.util.Constants;
import org.ws4d.osgi.proxyGenerator.util.IPropertyCallback;
import org.ws4d.osgi.proxyGenerator.util.ProxyGeneratorProperties;

public class LogServiceImpl implements LogService, IPropertyCallback {

    private static LogService log = null;

    private boolean showTimestamp = true;

    private int logLevel = LogService.LOG_WARNING;

    private boolean logConsole = true;

    protected static LogService getInstance() {
        if (LogServiceImpl.log == null) LogServiceImpl.log = new LogServiceImpl();
        return log;
    }

    protected static void shutdown() {
        log = null;
    }

    private LogServiceImpl() {
        if (ProxyGeneratorProperties.getInstance().getProperty(GlobalConstants.ProxyGeneratorProperties.PROP_LOG_LEVEL) != null) {
            logLevel = ProxyGeneratorProperties.getInstance().getIntProperty(GlobalConstants.ProxyGeneratorProperties.PROP_LOG_LEVEL);
            logConsole = ProxyGeneratorProperties.getInstance().getBooleanProperty(GlobalConstants.ProxyGeneratorProperties.PROP_LOG_CONSOLE);
        }
        ProxyGeneratorProperties.getInstance().registerPropertyListener(this);
    }

    public void log(int arg0, String arg1) {
        arg1 = "[" + Constants.MY_LOGID + "] " + arg1;
        if (arg0 > logLevel) {
            return;
        }
        switch(arg0) {
            case 1:
                msgOut("[ERROR" + showTimestamp() + "] " + arg1, 1);
                break;
            case 2:
                msgOut("[WARN " + showTimestamp() + "] " + arg1, 2);
                break;
            case 3:
                msgOut("[INFO " + showTimestamp() + "] " + arg1, 3);
                break;
            case 4:
                msgOut("[DEBUG" + showTimestamp() + "] " + arg1, 4);
                break;
            default:
                System.out.println(arg1);
                break;
        }
    }

    public void log(int arg0, String arg1, Throwable arg2) {
        arg1 = "[" + Constants.MY_LOGID + "] " + arg1;
        if (arg0 > logLevel) {
            return;
        }
        switch(arg0) {
            case 1:
                msgOut("[ERROR" + showTimestamp() + "] " + arg1, 1);
                break;
            case 2:
                msgOut("[WARN " + showTimestamp() + "] " + arg1, 2);
                break;
            case 3:
                msgOut("[INFO " + showTimestamp() + "] " + arg1, 3);
                break;
            case 4:
                msgOut("[DEBUG" + showTimestamp() + "] " + arg1, 4);
                break;
            default:
                System.out.println(arg1);
                break;
        }
    }

    public void log(ServiceReference arg0, int arg1, String arg2) {
        arg2 = "[" + Constants.MY_LOGID + "] " + arg2;
        if (arg1 > logLevel) {
            return;
        }
        switch(arg1) {
            case 1:
                msgOut("[ERROR" + showTimestamp() + "] [ServRef: " + arg0 + "] " + arg2, 1);
                break;
            case 2:
                msgOut("[WARN " + showTimestamp() + "] [ServRef: " + arg0 + "] " + arg2, 2);
                break;
            case 3:
                msgOut("[INFO " + showTimestamp() + "] [ServRef: " + arg0 + "] " + arg2, 3);
                break;
            case 4:
                msgOut("[DEBUG" + showTimestamp() + "] [ServRef: " + arg0 + "] " + arg2, 4);
                break;
            default:
                System.out.println(arg1);
                break;
        }
    }

    public void log(ServiceReference arg0, int arg1, String arg2, Throwable arg3) {
        if (arg1 > logLevel) {
            return;
        }
        switch(arg1) {
            case 1:
                msgOut("[ERROR" + showTimestamp() + "] [ServRef: " + arg0 + "] " + arg2, 1);
                break;
            case 2:
                msgOut("[WARN " + showTimestamp() + "] [ServRef: " + arg0 + "] " + arg2, 2);
                break;
            case 3:
                msgOut("[INFO " + showTimestamp() + "] [ServRef: " + arg0 + "] " + arg2, 3);
                break;
            case 4:
                msgOut("[DEBUG" + showTimestamp() + "] [ServRef: " + arg0 + "] " + arg2, 4);
                break;
            default:
                System.out.println(arg1);
                break;
        }
    }

    private String showTimestamp() {
        if (!showTimestamp) return "";
        Calendar cal = Calendar.getInstance();
        int tmp = cal.get(Calendar.HOUR_OF_DAY);
        String h = (tmp < 10 ? "0" : "") + String.valueOf(tmp);
        tmp = cal.get(Calendar.MINUTE);
        String m = (tmp < 10 ? "0" : "") + String.valueOf(tmp);
        tmp = cal.get(Calendar.SECOND);
        String s = (tmp < 10 ? "0" : "") + String.valueOf(tmp);
        tmp = cal.get(Calendar.MILLISECOND);
        String ms = (tmp < 10 ? "00" : (tmp < 100 ? "0" : "")) + String.valueOf(tmp);
        return "|" + h + ":" + m + ":" + s + "." + ms;
    }

    private void msgOut(String msg, int level) {
        if (logConsole) switch(level) {
            case LOG_ERROR:
                if (logConsole) System.err.println(msg);
                if (LogServiceTracker.osgiLog != null) LogServiceTracker.osgiLog.log(LogService.LOG_ERROR, msg);
                break;
            case LOG_WARNING:
                if (logConsole) System.err.println(msg);
                if (LogServiceTracker.osgiLog != null) LogServiceTracker.osgiLog.log(LogService.LOG_WARNING, msg);
                break;
            case LOG_INFO:
                if (logConsole) System.out.println(msg);
                if (LogServiceTracker.osgiLog != null) LogServiceTracker.osgiLog.log(LogService.LOG_INFO, msg);
                break;
            case LOG_DEBUG:
                if (logConsole) System.out.println(msg);
                if (LogServiceTracker.osgiLog != null) LogServiceTracker.osgiLog.log(LogService.LOG_DEBUG, msg);
                break;
            default:
                System.out.println(msg);
                break;
        }
    }

    public void propertyChanged(String property) {
        if (GlobalConstants.ProxyGeneratorProperties.PROP_LOG_LEVEL.equals(property)) {
            logLevel = ProxyGeneratorProperties.getInstance().getIntProperty(GlobalConstants.ProxyGeneratorProperties.PROP_LOG_LEVEL);
        } else if (GlobalConstants.ProxyGeneratorProperties.PROP_LOG_CONSOLE.equals(property)) {
            logConsole = ProxyGeneratorProperties.getInstance().getBooleanProperty(GlobalConstants.ProxyGeneratorProperties.PROP_LOG_CONSOLE);
        } else if (GlobalConstants.ProxyGeneratorProperties.PROP_DPWS_LOG_LEVEL.equals(property)) {
            Properties.getInstance().setGlobalProperty(Properties.PROP_LOG_LEVEL, ProxyGeneratorProperties.getInstance().getIntProperty(GlobalConstants.ProxyGeneratorProperties.PROP_DPWS_LOG_LEVEL));
        }
    }
}
