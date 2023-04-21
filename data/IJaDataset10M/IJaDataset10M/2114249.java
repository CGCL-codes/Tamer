package com.gorillalogic.webapp.actions;

import com.gorillalogic.dal.AccessException;
import com.gorillalogic.dal.Txn;
import com.gorillalogic.webapp.*;
import com.gorillalogic.accounts.GXESession;
import com.gorillalogic.accounts.GXEAccount;
import com.gorillalogic.dex.Dex;
import com.gorillalogic.dex.Names;
import com.gorillalogic.dex.TxnBean;
import com.gorillalogic.gython.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import org.apache.struts.action.*;
import org.apache.log4j.*;

/** a base calss for actions. Provides logging, session management, 
 */
public abstract class WebappAction extends Action {

    static Logger logger = Logger.getLogger(WebappAction.class);

    public static final String SEPARATOR = "__,__";

    public static final String DIRECTIVE_PARAM_NAME = "webappDirectives";

    public static final String GXESESSION_KEY = "webappGSessionKey";

    public static final String GXESESSION = "webappGXESession";

    public static final String EXCEPTION_FORWARD = "exception";

    protected abstract ActionForward doExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws WebappException;

    /** with default error handling, derived classes will typically override
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException {
        ActionForward forward = null;
        HttpSession session = request.getSession();
        ScriptBean scriptBean = (ScriptBean) session.getAttribute(Webapp.SCRIPTBEAN_ATTR_NAME);
        if (scriptBean == null) {
            scriptBean = (ScriptBean) Webapp.getServletContext().getAttribute(Webapp.SCRIPTBEAN_ATTR_NAME);
            session.setAttribute(Webapp.SCRIPTBEAN_ATTR_NAME, scriptBean);
        }
        try {
            forward = executeToss(mapping, form, request, response);
        } catch (Exception e) {
            String msg = "Error in servlet processing: " + e.getMessage();
            logger.error(msg, e);
            if (e instanceof AccessException) {
                StringWriter s = new StringWriter();
                ((AccessException) e).elaborate(new PrintWriter(s));
                msg = msg + "\n" + s.toString();
            }
            forward = mapping.findForward("exception");
        }
        return forward;
    }

    /** throws exceptions to be causth by caller, derived classes will typically call from <code>execute(..)</code>
	 */
    public ActionForward executeToss(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward forward = null;
        try {
            if (logger.isDebugEnabled()) {
                logParameters(mapping, request);
            }
            try {
                resumeGXESession(request);
            } catch (SessionException e) {
                if (!isLogin()) {
                    return mapping.findForward(Names.Forwards.SESSION_INVALID);
                }
            }
            forward = doExecute(mapping, form, request, response);
            if (logger.isDebugEnabled()) {
                logAttributes(request);
            }
        } finally {
            request.setAttribute(Dex.TXN, new TxnBean(Txn.mgr.inProgress()));
            suspendGSession(request);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("returning with forward = " + forward.getName());
        }
        return forward;
    }

    protected void suspendGSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        GXESession curSession = GXESession.factory.currentSession();
        session.setAttribute(GXESESSION_KEY, curSession.getId());
        session.setAttribute(GXESESSION, curSession);
        try {
            GXEAccount acct = curSession.getAccount();
            logger.debug("suspending session " + curSession.getId() + " for " + ((acct == null) ? "an unauthenticated session" : acct.getUserId()));
        } catch (AccessException e) {
            logger.debug("suspend: error getting account " + "from current session for diagnostic");
        }
    }

    protected void resumeGXESession(HttpServletRequest request) throws SessionException {
        HttpSession session = request.getSession();
        String sessionKey = (String) session.getAttribute(GXESESSION_KEY);
        GXESession gxesession = (GXESession) session.getAttribute(GXESESSION);
        boolean resumed = resumeGXESession(gxesession);
        if (!resumed) {
            if (!isLogin()) {
                logger.error("Could not resume session");
                throw new SessionException("No current session key found");
            }
        } else {
            if (logger.isDebugEnabled()) {
                try {
                    GXEAccount acct = GXESession.factory.currentSession().getAccount();
                    logger.debug("resume OK, session is for " + ((acct == null) ? "an unauthenticated session" : acct.getUserId()));
                } catch (AccessException e) {
                    logger.debug("resume: error getting account " + "from current session for diagnostic");
                }
            }
        }
    }

    public static boolean resumeGXESession(GXESession session) throws SessionException {
        if (session == null) {
            throw new SessionException("No session key for current session.");
        }
        logger.debug("resuming GSession: " + session.getId());
        try {
            session.activate();
        } catch (AccessException e) {
            throw new SessionException("Couldn't resume session", e);
        }
        return true;
    }

    /** calls methods on the WebappAction directly, with the supplied arguments, 
	 *  by looking up the method name using reflection
	 */
    protected void execDirectives(HttpServletRequest request) throws WebappException {
        String[] directives = request.getParameterValues("webappDirective");
        if (directives == null) {
            directives = request.getParameterValues("dexDirective");
        }
        if (directives == null) {
            return;
        }
        for (int i = 0; i < directives.length; i++) {
            String[] tokens = directives[i].split(SEPARATOR);
            int nArgs = tokens.length - 1;
            String methName = tokens[0];
            if (methName.length() == 0) {
                continue;
            }
            String args[] = new String[nArgs];
            System.arraycopy(tokens, 1, args, 0, nArgs);
            Class[] types = new Class[nArgs];
            for (int arg = 0; arg < nArgs; types[arg++] = String.class) ;
            Class c = getClass();
            Method method = null;
            try {
                method = c.getMethod(methName, types);
            } catch (NoSuchMethodException e) {
                if (logger.isDebugEnabled()) {
                    logger.debug("nArgs: " + nArgs);
                }
                throw logError(Priority.ERROR, "No such method " + methName, e);
            }
            try {
                method.invoke(this, (Object[]) args);
            } catch (IllegalAccessException e) {
                throw logError(Priority.ERROR, "Error invoking method " + methName, e);
            } catch (InvocationTargetException e) {
                throw logError(Priority.ERROR, "Error invoking method " + methName, e);
            }
        }
    }

    private WebappException logError(Priority priority, String msg, Throwable e) throws WebappException {
        WebappException ex = new WebappException(msg, e);
        logger.log(priority, msg, ex);
        return ex;
    }

    public void logParameters(ActionMapping mapping, HttpServletRequest request) {
        logger.debug(mapping.getPath() + " session: " + request.getSession().getId());
        Enumeration names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            String[] values = request.getParameterValues(name);
            for (int i = 0; i < values.length; i++) {
                logger.debug("Parameter: " + name + " = " + values[i]);
            }
        }
    }

    private void logAttributes(HttpServletRequest request) {
        Enumeration names = request.getAttributeNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            Object value = request.getAttribute(name);
            logger.debug("Request attribute: " + name + " = " + value.toString());
        }
    }

    protected boolean isLogin() {
        return false;
    }
}
