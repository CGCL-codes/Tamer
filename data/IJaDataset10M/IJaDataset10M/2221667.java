package com.nexirius.framework.htmlview;

import com.nexirius.framework.FWLog;
import com.nexirius.framework.Logger;
import com.nexirius.framework.datamodel.BooleanModel;
import com.nexirius.framework.datamodel.DataModel;
import com.nexirius.framework.htmlview.application.HTMLApplication;
import com.nexirius.framework.htmlview.function.HTMLFunction;
import com.nexirius.framework.htmlview.function.HTMLTransition;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;

/**
 * This servlet is connected to a state machine which holds HTMLState items. Each action holds an event parameter
 * which is used to step to the next state in the state machine. The actual model and template which is used to create
 * the next HTML page come from the HTMLState (unless the event holds specific paramters which override that information)
 */
public abstract class JnexServlet extends HttpServlet {

    public static final String SESSION_VARIABLE_PREFIX = "nexirius_";

    public static final String BOOLEAN_PARAMETER_PREFIX = "boolean__";

    private HTMLApplication htmlApplication;

    public JnexServlet() {
        FWLog.setLogger(new ServletLogger());
    }

    class ServletLogger implements Logger {

        protected boolean on = false;

        public void debug(String info) {
            log("[DEBUG] " + info);
        }

        public void debug(String info, Exception ex) {
            if (on) {
                JnexServlet.this.log("[DEBUG] " + info, ex);
            }
        }

        public void log(String info) {
            if (on) {
                JnexServlet.this.log(info);
            }
        }

        public void setDebugging(boolean on) {
            this.on = on;
        }
    }

    /**
     * This is the only method which needs to implemented in a sub-class. The servlet always uses the same instance of the
     * specified HTMLApplication (for all users).
     *
     * @return
     */
    public abstract HTMLApplication createHTMLApplication();

    public HTMLApplication getHTMLApplication() {
        if (htmlApplication == null) {
            htmlApplication = createHTMLApplication();
        }
        return htmlApplication;
    }

    /**
     * When the user session is started, then a new HTMLSessionVariable instance is generated (method:init()) and stored in the HTTP session.
     * The session variable is the only instance, which is allowed to hold user specific data.
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HTMLApplication application = getHTMLApplication();
        HttpSession session = application.getSession(request);
        String sessionVariableName = getSessionVariableName(application);
        HTMLSessionVariable sessionVariable = (HTMLSessionVariable) session.getAttribute(sessionVariableName);
        try {
            if (sessionVariable == null) {
                htmlApplication.preInit();
                sessionVariable = application.init();
                FWLog.debug("NEW SESSION CREATED");
                session.setAttribute(sessionVariableName, sessionVariable);
                htmlApplication.postInit(sessionVariable);
            }
            synchronized (sessionVariable) {
                sessionVariable.clearSelectedChildren();
                sessionVariable.init(request, response);
                HTMLState oldState = null;
                HTMLState actState = null;
                oldState = (HTMLState) sessionVariable.getActState();
                if (sessionVariable.getState() != null && sessionVariable.getState().length() > 0 && !sessionVariable.getState().equals(oldState.getName())) {
                    oldState = sessionVariable.goBackwardState();
                } else if (sessionVariable.getState() == null) {
                    sessionVariable.resetStateMachine();
                    oldState = (HTMLState) sessionVariable.getActState();
                }
                FWLog.debug("OLD STATE = " + oldState.getName());
                HTMLCommand command = application.getHTMLCommand(oldState.getName(), sessionVariable.getEvent());
                boolean doSwitchState = true;
                if (command == null) {
                    command = application.getDefaultHTMLCommand(sessionVariable.getEvent());
                }
                if (command != null) {
                    if (command.requiresMapping()) {
                        DataModel actModel = sessionVariable.getActState().getModel();
                        if (actModel != null) {
                            mapRequestParametersToModel(request, actModel);
                        }
                    }
                    try {
                        FWLog.debug("execute command " + command.getClass());
                        doSwitchState = command.execute(sessionVariable);
                    } catch (Exception e) {
                        application.handleException(sessionVariable, e);
                    }
                }
                if (doSwitchState) {
                    HTMLTransition defaultTransition = application.getDefaultHTMLTransition(sessionVariable.getEvent());
                    HTMLState targetState = defaultTransition == null ? null : defaultTransition.getTargetState();
                    if (targetState == null) {
                        sessionVariable.doStateTransition();
                    } else {
                        sessionVariable.getStateMachine().setActState(targetState);
                    }
                    String childName = sessionVariable.getChild();
                    if (childName != null) {
                        if (oldState.getModel() != null) {
                            try {
                                DataModel child = oldState.getModel().getChild(childName);
                                sessionVariable.getActState().setModel(child);
                                if (sessionVariable.getDuplicate()) {
                                    sessionVariable.getActState().startDuplicatePopup();
                                }
                            } catch (Exception e) {
                            }
                        }
                    }
                }
                actState = (HTMLState) sessionVariable.getActState();
                DataModel actModel = actState.getModel();
                FWLog.debug("ACT MODEL = " + (actModel == null ? "null" : actModel.getFieldName()));
                sessionVariable.storeActState();
                sessionVariable.getResolver().getVariableStore().setVariable(VariableStore.STATE, actState.getName());
                sessionVariable.getResolver().getVariableStore().setVariable(VariableStore.REQUEST_URL, request.getRequestURL().toString());
                sessionVariable.getResolver().setRootModel(actModel);
                String template = sessionVariable.getTemplate();
                if (template == null) {
                    template = actState.getTemplate();
                }
                byte reply[] = sessionVariable.getResolver().resolve(sessionVariable, actModel, template, actState.isEditor());
                response.getOutputStream().write(reply);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
        response.getOutputStream().close();
    }

    public static String getSessionVariableName(HTMLApplication application) {
        return SESSION_VARIABLE_PREFIX + application.getApplicationName();
    }

    /**
     * Map the request parameters to the actual DataModel which is held in the current state (sessionVariable.getActState().getModel()).
     * In general the name and values of the parameters are used like this: actModel.setChildText(parameterName, value);
     *
     * @param request
     * @param actModel
     */
    public static void mapRequestParametersToModel(HttpServletRequest request, DataModel actModel) {
        Enumeration enumVar = request.getParameterNames();
        String falseParameter = null;
        while (enumVar.hasMoreElements()) {
            String parameterName = (String) enumVar.nextElement();
            String value = request.getParameter(parameterName);
            if (falseParameter != null && !falseParameter.equals(parameterName)) {
                try {
                    ((BooleanModel) actModel.getChild(falseParameter)).setBoolean(false);
                    falseParameter = null;
                } catch (Throwable ex) {
                }
            }
            if (parameterName.startsWith(HTMLFunction.PARAMETER_BUTTON)) {
                continue;
            } else if (parameterName.equals("this")) {
                parameterName = null;
            } else if (parameterName.startsWith(BOOLEAN_PARAMETER_PREFIX)) {
                falseParameter = parameterName.substring(BOOLEAN_PARAMETER_PREFIX.length());
                continue;
            }
            try {
                DataModel child = actModel.getChild(parameterName);
                if (child instanceof BooleanModel) {
                    ((BooleanModel) child).setBoolean(true);
                } else {
                    actModel.setChildText(parameterName, value);
                }
            } catch (Exception ex) {
            }
            FWLog.debug("Setting child value " + parameterName + "=" + value);
        }
    }
}
