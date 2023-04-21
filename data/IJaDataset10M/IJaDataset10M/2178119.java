package br.net.woodstock.rockframework.web.struts2.util;

import java.util.Enumeration;
import java.util.Formatter;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import br.net.woodstock.rockframework.utils.ArrayUtils;
import br.net.woodstock.rockframework.web.config.WebLog;
import br.net.woodstock.rockframework.web.struts2.AbstractInterceptor;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;

public class DebugInterceptor extends AbstractInterceptor {

    private static final long serialVersionUID = 657049342110588586L;

    private static final String NEW_LINE = "\n";

    private String level;

    private boolean printParameters;

    private boolean printSession;

    public DebugInterceptor() {
        super();
        this.level = "INFO";
        this.printParameters = true;
        this.printSession = true;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public String intercept(final ActionInvocation invocation) throws Exception {
        HttpServletRequest request = this.getRequest();
        ActionProxy proxy = invocation.getProxy();
        if (this.printParameters) {
            Set set = request.getParameterMap().keySet();
            StringBuilder builder = new StringBuilder();
            builder.append(DebugInterceptor.NEW_LINE);
            builder.append("==================== Begin Parameters ====================");
            builder.append(DebugInterceptor.NEW_LINE);
            for (Object o : set) {
                String key = o.toString();
                String value = ArrayUtils.toString(request.getParameterValues(key));
                String s = new Formatter().format("%50s => %s", key, value).toString();
                builder.append(s);
                builder.append(DebugInterceptor.NEW_LINE);
            }
            builder.append("===================== End Parameters =====================");
            builder.append(DebugInterceptor.NEW_LINE);
            this.log(builder.toString());
        }
        if (this.printSession) {
            HttpSession session = request.getSession();
            Enumeration enumeration = session.getAttributeNames();
            StringBuilder builder = new StringBuilder();
            builder.append(DebugInterceptor.NEW_LINE);
            builder.append("==================== Begin Session ====================");
            builder.append(DebugInterceptor.NEW_LINE);
            while (enumeration.hasMoreElements()) {
                String key = enumeration.nextElement().toString();
                Object value = session.getAttribute(key);
                String s = new Formatter().format("%50s => %s", key, value).toString();
                builder.append(s);
                builder.append(DebugInterceptor.NEW_LINE);
            }
            builder.append("===================== End Session =====================");
            builder.append(DebugInterceptor.NEW_LINE);
            this.log(builder.toString());
        }
        this.log("Invoking " + proxy.getAction().getClass().getCanonicalName() + "." + proxy.getMethod() + "()");
        String s = invocation.invoke();
        this.log("Return of " + proxy.getAction().getClass().getCanonicalName() + "." + proxy.getMethod() + "(): '" + s + "'");
        return s;
    }

    private void log(final String message) {
        if (this.level.equals("TRACE")) {
            WebLog.getInstance().getLog().finest(message);
        } else if (this.level.equals("DEBUG")) {
            WebLog.getInstance().getLog().fine(message);
        } else if (this.level.equals("INFO")) {
            WebLog.getInstance().getLog().info(message);
        } else if (this.level.equals("WARN")) {
            WebLog.getInstance().getLog().warning(message);
        } else if (this.level.equals("ERROR")) {
            WebLog.getInstance().getLog().severe(message);
        }
    }

    public void setLevel(final String level) {
        this.level = level;
    }

    public void setPrintParameters(final String printParameters) {
        this.printParameters = Boolean.parseBoolean(printParameters);
    }

    public void setPrintSession(final String printSession) {
        this.printSession = Boolean.parseBoolean(printSession);
    }
}
