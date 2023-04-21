package org.rjam.collectors.debug;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.rjam.Collector;
import org.rjam.Event;
import org.rjam.admin.AdminProcessor;
import org.rjam.collectors.ServletCollector;
import org.rjam.net.command.server.api.ICommand;
import org.rjam.net.command.server.api.ICommandProcessor;
import org.rjam.net.command.server.api.IRequestContext;
import org.rjam.xml.Token;

/**
 * This collector can be used to debug an application when
 * you don't know what to instrument.
 * 
 * 
 * @author Tony Bringardner
 *
 */
public class DebugCollector2 extends Collector implements ICommand {

    private static final long serialVersionUID = 1L;

    private static final String DEFULT_REPORT_ALL = "false";

    private static final String PROP_REPORT_ALL = "ReportAll";

    protected class ArgsHolder {

        private Object[] args;

        public void setArgs(Object[] args) {
            this.args = args;
        }

        public Object[] getArgs() {
            return args;
        }
    }

    private ThreadLocal<ArgsHolder> args = new ThreadLocal<ArgsHolder>() {

        protected synchronized ArgsHolder initialValue() {
            return new ArgsHolder();
        }
    };

    public class ThreadLocalObject {

        long time = 0L;
    }

    private ThreadLocal<ThreadLocalObject> notified = new ThreadLocal<ThreadLocalObject>() {

        protected synchronized ThreadLocalObject initialValue() {
            return new ThreadLocalObject();
        }
    };

    private boolean reportAll;

    public DebugCollector2() {
        super();
        setRequiresData(true);
    }

    public DebugCollector2(String monitorName) {
        super(monitorName);
        setRequiresData(true);
    }

    public boolean isReportAll() {
        return reportAll;
    }

    public void setReportAll(boolean reportAll) {
        this.reportAll = reportAll;
    }

    public void configure(Token configToken) {
        super.configure(configToken);
        reportAll = getProperty(PROP_REPORT_ALL, DEFULT_REPORT_ALL).toLowerCase().startsWith("t");
        AdminProcessor.registerCommand("Debug2", this);
    }

    public Object[] getArgsServlet(String method, Object[] args, boolean injector) {
        List<String> list = new ArrayList<String>();
        if (args != null) {
            if (args.length == 2) {
                try {
                    Class<? extends Object> cls = args[0].getClass();
                    if (method.equals(ServletCollector.SERVICE)) {
                        Object obj = executeGetter(cls, "getMethod", args[0]);
                        if (obj != null) {
                            String tmp = obj.toString();
                            if (tmp.length() > 0) {
                                list.add(tmp);
                            }
                        }
                    }
                    String url = executeGetterAsString(cls, "getRequestURI", args[0]);
                    if (url != null) {
                        list.add(url);
                    }
                } catch (Throwable e) {
                    logError("Error occured during processing of HttpRequest", e);
                }
            }
        }
        if (list.size() == 0) {
            args = null;
        } else {
            args = list.toArray();
        }
        return args;
    }

    public Event startEvent(long startTime, Object object, String method, Object[] args, boolean injector) {
        ((ArgsHolder) this.args.get()).setArgs(args);
        setNotified(startTime);
        Event ret = super.startEvent(startTime, object, method, args, injector);
        return ret;
    }

    public Object endEvent(Event event, Object inResult) {
        String method = event.getMethodName();
        if (method.startsWith("prepare")) {
            String[] args = event.getArgs();
            if (args != null && args.length >= 1) {
                setObjectData(inResult, args[0]);
                if (isDebug()) {
                    logDebug(event.getMethodName() + " toString=" + inResult);
                }
            } else {
                String tmp = "noArgs";
                if (args != null) {
                    tmp = "args=" + args.length;
                    for (int i = 0; i < args.length; i++) {
                        tmp += " args[" + i + "]=" + args[i];
                    }
                }
                logDebug("Hmmm, endEvent for prepare has no args. method=" + event.getMethodName() + " args=" + args);
            }
        }
        Object ret = super.endEvent(event, inResult);
        return ret;
    }

    public Object fireEvent(long startTime, Object object, String methodName, Object[] args, Object retVal) {
        Object ret = retVal;
        boolean enabled = isEnabled();
        if (enabled) {
            try {
                if (reportAll || !isNotified(startTime)) {
                    ret = super.fireEvent(startTime, object, methodName, args, retVal);
                }
            } catch (Throwable e) {
                logError("Ignoring error in fireEvent", e);
            }
        }
        return ret;
    }

    private void setNotified(long startTime) {
        ((ThreadLocalObject) notified.get()).time = startTime;
    }

    private boolean isNotified(long startTime) {
        long time = ((ThreadLocalObject) notified.get()).time;
        boolean ret = time > startTime;
        return ret;
    }

    public Object[] getArgsJdbc(Object object, String method, Object[] args) {
        Object[] ret = null;
        if (method.startsWith("execute")) {
            ret = args;
            if (args == null || args.length == 0) {
                Object oid = getObjectData(object);
                if (oid != null) {
                    ret = new Object[] { oid };
                } else {
                    String tmp = "noArgs";
                    if (args != null) {
                        tmp = "args=" + args.length;
                        for (int i = 0; i < args.length; i++) {
                            tmp += " args[" + i + "]=" + args[i];
                        }
                    }
                    logDebug("Hmmm, no startData?  method=" + method + " args=" + tmp);
                }
            }
        }
        return ret;
    }

    public void execute(ICommandProcessor processor, IRequestContext context) throws IOException {
        String cmd = context.getNextToken();
        if (cmd == null || PROP_REPORT_ALL.equalsIgnoreCase(cmd)) {
            if (context.hasNext()) {
                setReportAll(context.getNextToken());
            }
            processor.reply(REPLY_200_OK, "reportAll=" + reportAll);
        } else if (cmd.equalsIgnoreCase(PROP_THRESHOLD)) {
            if (context.hasNext()) {
                setThreashold(Long.parseLong(context.getNextToken()));
            }
            processor.reply(REPLY_200_OK, "threshold=" + getThreashold());
        } else {
            processor.reply(REPLY_500_ERROR, "Invalid debug command =" + cmd);
        }
    }

    private void setReportAll(String val) {
        logDebug("Set reportAll=" + val);
        reportAll = val.toLowerCase().startsWith("t");
    }

    public boolean isAutherized(ICommandProcessor processor, IRequestContext context) {
        return true;
    }
}
