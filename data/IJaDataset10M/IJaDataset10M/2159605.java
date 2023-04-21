package net.naijatek.myalumni.framework.struts;

import java.util.Date;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.naijatek.myalumni.modules.common.domain.ErrorLogVO;
import net.naijatek.myalumni.modules.common.service.IErrorLogService;
import net.naijatek.myalumni.util.BaseConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;
import org.apache.struts.util.ModuleException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Implementation of <strong>ExceptionHandler</strong> that handles any
 * Exceptions that are bundles up to_email the Action layer. This allows us
 * to_email gracelly handle any exception that Action classes do not catch.
 * 
 * @author Folashade
 * @version 1.0
 */
public final class MyAlumniExceptionHandler extends ExceptionHandler {

    private static Log logger = LogFactory.getLog(MyAlumniExceptionHandler.class);

    /**
	 * This method handles any java.lang.Exceptions that are not caught in
	 * previous classes. It will loop through and get all the causes (exception
	 * chain), create ActionErrors, add them to_email the request and then
	 * forward to_email the input.
	 * 
	 * @see org.apache.struts.action.ExceptionHandler#execute()
	 *      java.lang.Exception, org.apache.struts.config.ExceptionConfig,
	 *      org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse )
	 * 
	 * @param ex
	 *            Exception
	 * @param ae
	 *            ExceptionConfig
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @throws ServletException
	 * @return ActionForward
	 */
    public ActionForward execute(Exception ex, ExceptionConfig ae, ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException {
        ActionMessages messages = new ActionMessages();
        ActionForward forward = super.execute(ex, ae, mapping, form, request, response);
        ServletContext sCtx = request.getSession().getServletContext();
        WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(sCtx);
        IErrorLogService loggerService = (IErrorLogService) wac.getBean(BaseConstants.SERVICE_ERRORLOGGER_LOOKUP);
        String forwardKey = new String();
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        Throwable thr = (Throwable) request.getAttribute("javax.servlet.error.exception");
        StringBuffer strBuffer = new StringBuffer();
        if (thr != null) {
            StackTraceElement[] stack = thr.getStackTrace();
            for (int n = 0; n < stack.length; n++) {
                strBuffer.append(stack[n].toString());
                strBuffer.append("\n");
            }
        }
        String messg = (String) ex.getMessage();
        messg = (messg != null && messg.length() >= 4000) ? messg.substring(0, 3999) : messg;
        String trace = strBuffer.toString();
        trace = (trace != null && trace.length() >= 4000) ? trace.substring(0, 3999) : trace;
        String cause = new String();
        if (ex.getCause() != null) cause = ex.getCause().toString();
        String userName = new String();
        try {
            MyAlumniUserContainer sessionObj = null;
            HttpSession session = request.getSession(false);
            if (session != null) {
                sessionObj = (MyAlumniUserContainer) session.getAttribute(BaseConstants.USER_CONTAINER);
                userName = sessionObj.getToken().getMemberUserName();
            }
        } catch (Exception npe) {
            userName = "";
        }
        ErrorLogVO errorLog = new ErrorLogVO();
        errorLog.setErrorDate(new Date());
        errorLog.setLoggedBy(userName);
        errorLog.setLastModifiedBy(userName);
        errorLog.setErrorMessage(messg);
        errorLog.setCause(cause);
        errorLog.setTrace(trace);
        loggerService.addErrorLog(errorLog);
        if (statusCode != null) {
            int sc = ((Integer) statusCode).intValue();
            switch(sc) {
                case 700:
                    forwardKey = BaseConstants.SC_INSURFICIENT_PRIV_700;
                    messages.add(BaseConstants.FATAL_KEY, new ActionMessage("core.errorcode.00701"));
                    storeException(request, BaseConstants.FATAL_KEY, new ActionMessage("core.errorcode.00701"), forward);
                    break;
                case 701:
                    forwardKey = BaseConstants.SC_SESSION_EXPIRED_701;
                    messages.add(BaseConstants.FATAL_KEY, new ActionMessage("core.errorcode.00702"));
                    storeException(request, BaseConstants.FATAL_KEY, new ActionMessage("core.errorcode.00702"), forward);
                    break;
            }
        }
        if (forwardKey != null && forwardKey.length() == 0) {
            String requestPathInfo = request.getPathInfo();
            if (requestPathInfo != null && requestPathInfo.startsWith("/admin/")) {
                forwardKey = BaseConstants.FWD_ADMIN;
                messages.add(BaseConstants.FATAL_KEY, new ActionMessage("core.errorcode.00703"));
                storeException(request, BaseConstants.FATAL_KEY, new ActionMessage("core.errorcode.00703"), forward);
            } else if (requestPathInfo != null && requestPathInfo.startsWith("/member/")) {
                forwardKey = BaseConstants.FWD_MEMBER;
                messages.add(BaseConstants.FATAL_KEY, new ActionMessage("core.errorcode.00704"));
                storeException(request, BaseConstants.FATAL_KEY, new ActionMessage("core.errorcode.00704"), forward);
            } else {
                forwardKey = BaseConstants.FWD_MEMBER;
                messages.add(BaseConstants.FATAL_KEY, new ActionMessage("core.errorcode.00703"));
                storeException(request, BaseConstants.FATAL_KEY, new ActionMessage("core.errorcode.00703"), forward);
            }
        }
        return mapping.findForward(forwardKey);
    }

    /**
	 * This method overrides the the ExceptionHandler's storeException method in
	 * order to_email create more than one error message.
	 * 
	 * @param request -
	 *            The request we are handling
	 * @param property -
	 *            The property name to_email use for this error
	 * @param error -
	 *            The error generated from_email the exception mapping
	 * @param forward -
	 *            The forward generated from_email the input path (from_email
	 *            the form or exception mapping)
	 */
    void storeException(HttpServletRequest request, String property, ActionMessage error, ActionForward forward) {
        ActionMessages errors = (ActionMessages) request.getAttribute(Globals.ERROR_KEY);
        if (errors == null) {
            errors = new ActionMessages();
        }
        errors.add(property, error);
        request.setAttribute(Globals.ERROR_KEY, errors);
    }
}
