package com.azaleait.asterion.action;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import com.azaleait.asterion.exception.ActionNotFoundException;
import com.azaleait.asterion.exception.ApplicationException;
import com.azaleait.asterion.exception.AttributeNotFoundException;
import com.azaleait.asterion.exception.InvalidActionException;
import com.azaleait.asterion.exception.InvalidCommandNameException;
import com.azaleait.asterion.utils.Utils;

public final class Edecan {

    private static Logger logger = Logger.getLogger(Edecan.class);

    private Edecan() {
    }

    public static void addParameters(final ActionForm form, final Map inline) throws AttributeNotFoundException {
        for (Iterator it = inline.keySet().iterator(); it.hasNext(); ) {
            String name = (String) it.next();
            Object value = inline.get(name);
            try {
                PropertyUtils.setSimpleProperty(form, name, value);
            } catch (IllegalAccessException e) {
                String msg = "Attribute '" + name + "' not found in action form '" + form.getClass().getName() + "'.";
                logger.error(msg, e);
                throw new AttributeNotFoundException(msg, e);
            } catch (InvocationTargetException e) {
                String msg = "Attribute '" + name + "' not found in action form '" + form.getClass().getName() + "'.";
                logger.error(msg, e);
                throw new AttributeNotFoundException(msg, e);
            } catch (NoSuchMethodException e) {
                String msg = "Attribute '" + name + "' not found in action form '" + form.getClass().getName() + "'.";
                logger.error(msg, e);
                throw new AttributeNotFoundException(msg, e);
            }
        }
    }

    public static String getCommandName(final HttpServletRequest request) throws InvalidCommandNameException {
        AsterionApplicationState aas = AsterionState.getAsterionAppState(request.getSession().getServletContext());
        String command = (String) request.getParameter(aas.getFieldcommandname());
        logger.debug("command=" + command);
        if (command != null) {
            command = command.trim();
            if (command.length() == 0) {
                command = null;
            }
        }
        if ("execute".equals(command) || "perform".equals(command) || "allowInitiateSequence".equals(command) || "navigateIn".equals(command) || "preRender".equals(command) || "navigateOut".equals(command) || "allowPage".equals(command) || "registerAuthentication".equals(command) || "invalidateAuthentication".equals(command)) {
            String message = "The '" + command + "' command name " + "is reserved and cannot be used.";
            logger.error(message);
            throw new InvalidCommandNameException(message);
        }
        return command;
    }

    public static Method findCommandMethod(final String command, final ViewController action) {
        Method method = null;
        try {
            synchronized (action.methods) {
                method = (Method) action.methods.get(command);
                if (method == null) {
                    logger.debug("search method in class.");
                    method = action.getClass().getMethod(command, ViewControllerProxy.COMMAND_METHOD_TYPES);
                    logger.debug("method found.");
                    action.methods.put(command, method);
                }
            }
        } catch (NoSuchMethodException e) {
            return null;
        }
        return method;
    }

    public static AsterionSessionState getAsterionSessionState(final HttpServletRequest request) {
        logger.debug("getAsterionSessionState.");
        HttpSession session = request.getSession();
        if (session == null) {
            return null;
        }
        AsterionSessionState as = (AsterionSessionState) session.getAttribute(Constants.ASTERION_SESSION_STATE);
        return as;
    }

    public static AsterionRequestState getAsterionRequestState(final HttpServletRequest request) {
        AsterionRequestState ar = (AsterionRequestState) request.getAttribute(Constants.ASTERION_REQUEST_STATE);
        return ar;
    }

    public static final int ALLOW_NULL_ACTION = 1;

    public static final int REJECT_NULL_ACTION = 2;

    public static void checkNextAction(final RequestInfo ri, final String method, final Class nextAction, final int allowNullValue) throws InvalidActionException {
        if (nextAction == null) {
            if (allowNullValue != ALLOW_NULL_ACTION) {
                String message = method + " of class '" + ri.getActionClassName() + "' has returned a null value.";
                throw new InvalidActionException(message);
            }
            return;
        }
        if (!ViewController.class.isAssignableFrom(nextAction)) {
            String message = "Returned class '" + nextAction.getName() + "' from " + method + " of action '" + ri.getActionClassName() + "' is not subclass of '" + ViewController.class.getName() + "' class.";
            logger.error(message);
            throw new InvalidActionException(message);
        }
    }

    public static final boolean PREVENT_VALIDATE = true;

    public static final boolean ALLOW_VALIDATE = false;

    private static ActionForward findForward(final Class nextAction, final boolean avoidValidate) throws ActionNotFoundException {
        logger.debug("class=" + nextAction.getName());
        String url = AsterionClassMapper.getInstance(null).getPath(nextAction.getName());
        if (url == null) {
            String message = "Action '" + nextAction.getName() + "' not found in struts-config.xml.";
            throw new ActionNotFoundException(message);
        }
        logger.debug("1) url=" + url);
        if (!url.endsWith(".do")) {
            url = url + ".do";
        }
        if (avoidValidate) {
            url = Utils.addUrlParameter(url, org.apache.struts.taglib.html.Constants.CANCEL_PROPERTY, "a");
        }
        logger.debug("2) url=" + url);
        ActionForward af = new ActionForward("a", url, true);
        return af;
    }

    public static QualifiedForward getForwardToNextPage(final RequestInfo ri, final Class nextAction) throws ActionNotFoundException, ApplicationException {
        AsterionSessionState as = ri.getAsterionSessionState();
        as.setNavigating(true);
        ri.setCurrentAction(nextAction);
        logger.debug("nextAction=" + nextAction.getName());
        ActionForward af = findForward(nextAction, Edecan.PREVENT_VALIDATE);
        return new QualifiedForward(af, QualifiedForward.KEEP_TOKEN);
    }
}
