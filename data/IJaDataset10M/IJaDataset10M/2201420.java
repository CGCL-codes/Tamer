package org.j2eebuilder.modules.actions.portlets;

import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.lang.reflect.Method;
import org.j2eebuilder.util.RequestHelperBean;
import org.j2eebuilder.*;
import org.j2eebuilder.BuilderHelperBean;
import org.j2eebuilder.view.CommandManager;
import dori.jasper.engine.*;
import java.io.*;
import java.sql.*;
import java.awt.*;
import org.j2eebuilder.util.*;
import org.j2eebuilder.model.ValueObject;
import org.apache.log4j.Logger;
import org.apache.jetspeed.portal.Portlet;
import org.apache.jetspeed.portal.portlets.AbstractPortlet;
import org.apache.jetspeed.services.rundata.JetspeedRunData;
import org.apache.turbine.util.RunData;
import org.apache.turbine.om.security.User;
import org.apache.ecs.ConcreteElement;
import org.apache.ecs.StringElement;
import org.apache.jetspeed.modules.actions.portlets.JspPortletAction;
import org.apache.jetspeed.util.PortletSessionState;
import org.apache.turbine.util.ParameterParser;
import org.j2eebuilder.util.RequestParameterException;

/**
 * @(#)PortletActionBean.java	1.350 01/12/03
 *
 */
public class PortletActionBean extends JspPortletAction {

    private static transient Logger log = Logger.getLogger(PortletActionBean.class);

    protected static final String BUTTON = "submit";

    private static Boolean semaphore = new Boolean(true);

    private static PortletActionBean globalPortletActionBean = null;

    public static PortletActionBean getInstance() {
        synchronized (semaphore) {
            if (globalPortletActionBean == null) {
                globalPortletActionBean = new PortletActionBean();
            }
        }
        return globalPortletActionBean;
    }

    public PortletActionBean() {
    }

    public ComponentDefinition authenticate(RequestHelperBean requestHelperBean) {
        org.j2eebuilder.ApplicationDefinition applicationDefinition = null;
        try {
            log.debug("1processRequest(" + requestHelperBean + "):-> Before BuilderHelperBean.getApplicationDefinition");
            applicationDefinition = BuilderHelperBean.getInstance().getApplicationDefinition(requestHelperBean);
            String applicationName = applicationDefinition.getName();
            Object sessionBean = BuilderHelperBean.getInstance().getSessionBean(requestHelperBean);
            log.debug("2processRequest(" + requestHelperBean + "):-> After getSessionBean");
            boolean mechanismAuthentication = false;
            boolean licenseValidation = false;
            try {
                log.debug("3processRequest(" + requestHelperBean + "):-> before mechanism, license validation");
                Method isMechanismValid = sessionBean.getClass().getMethod("isMechanismValid", null);
                Method isLicenseValid = sessionBean.getClass().getMethod("isLicenseValid", null);
                Boolean _mechanismAuthentication = (Boolean) isMechanismValid.invoke(sessionBean, null);
                mechanismAuthentication = _mechanismAuthentication.booleanValue();
                Boolean _licenseValidation = (Boolean) isLicenseValid.invoke(sessionBean, null);
                licenseValidation = _licenseValidation.booleanValue();
                log.debug("4processRequest(" + requestHelperBean + "):-> after mechanism, license validation");
            } catch (IllegalAccessException iae) {
                throw new org.j2eebuilder.BuilderException(iae.toString());
            } catch (NoSuchMethodException me) {
                throw new org.j2eebuilder.BuilderException(me.toString());
            } catch (Exception e) {
                throw new org.j2eebuilder.BuilderException(e.toString());
            }
            ComponentDefinition componentDefinition = null;
            try {
                if (!mechanismAuthentication) throw new org.j2eebuilder.model.MechanismAuthenticationException("Invalid authentication.");
                if (!licenseValidation) throw new org.j2eebuilder.license.LicenseViolationException("Invalid license.");
                String componentName = BuilderHelperBean.getInstance().getComponentName(requestHelperBean);
                componentDefinition = applicationDefinition.findComponentDefinitionByName(componentName);
                if (componentDefinition == null) {
                    componentDefinition = applicationDefinition.findComponentDefinitionByType("session");
                }
            } catch (org.j2eebuilder.model.MechanismAuthenticationException me) {
                componentDefinition = applicationDefinition.findComponentDefinitionByType("session");
            } catch (org.j2eebuilder.license.LicenseViolationException le) {
                componentDefinition = applicationDefinition.findComponentDefinitionByName("License");
            } catch (Exception e) {
                componentDefinition = applicationDefinition.findComponentDefinitionByType("session");
            }
            this.addComponentInfoToRequest(applicationDefinition, componentDefinition, requestHelperBean);
            String componentType = (String) requestHelperBean.getRequest().getAttribute("componentType");
            String componentController = (String) requestHelperBean.getRequest().getAttribute("componentController");
            return componentDefinition;
        } catch (Exception be) {
            log.error(".authenticate():", be);
        }
        return null;
    }

    public String getView(ApplicationDefinition applicationDefinition) {
        return applicationDefinition == null ? "/ErrorAlias" : applicationDefinition.getLayout();
    }

    protected void dispatch(HttpServletRequest request, HttpServletResponse response, String page) throws ServletException, java.io.IOException {
        RequestDispatcher dispatcher = request.getSession().getServletContext().getRequestDispatcher(page);
        dispatcher.forward(request, response);
        return;
    }

    private void addComponentInfoToRequest(ApplicationDefinition applicationDefinition, ComponentDefinition componentDefinition, RequestHelperBean requestHelperBean) throws BuilderException {
        if (componentDefinition == null) throw new BuilderException("Invalid request. Unable to retrieve componentDefinition from the request. Request not processed. ");
        String applicationName = applicationDefinition.getName();
        String componentControllerAlias = null;
        String componentController = null;
        String componentMenu = null;
        String componentData = null;
        String componentName = null;
        String componentType = null;
        String commandStatus = null;
        String useBeanID = null;
        String className = null;
        componentName = componentDefinition.getName();
        componentControllerAlias = componentDefinition.getServletPath();
        componentController = componentDefinition.getController();
        componentType = componentDefinition.getType();
        if (componentDefinition.getMenu() != null) componentMenu = componentDefinition.getMenu();
        if (componentDefinition.getData() != null) componentData = componentDefinition.getData();
        JspDeclaration jspDeclaration = null;
        jspDeclaration = componentDefinition.getJspDeclaration();
        if (jspDeclaration != null) {
            UseBeanDefinition useBeanDefinition = jspDeclaration.findUseBeanDefinitionByScope("session");
            if (useBeanDefinition != null) {
                useBeanID = useBeanDefinition.getId();
                className = useBeanDefinition.getClassName();
            }
        }
        requestHelperBean.getRequest().setAttribute("applicationName", applicationName);
        requestHelperBean.getRequest().setAttribute("componentName", componentName);
        requestHelperBean.getRequest().setAttribute("componentControllerAlias", componentControllerAlias);
        requestHelperBean.getRequest().setAttribute("componentController", componentController);
        requestHelperBean.getRequest().setAttribute("componentMenu", componentMenu);
        requestHelperBean.getRequest().setAttribute("componentType", componentType);
        requestHelperBean.getRequest().setAttribute("componentData", componentData);
        requestHelperBean.getRequest().setAttribute("useBeanID", useBeanID);
        requestHelperBean.getRequest().setAttribute("className", className);
    }

    public void invokeCommand(ComponentDefinition componentDefinition, RequestHelperBean requestHelperBean) {
        log.debug("@@@@PortletActionBean.invokeCommand(" + componentDefinition + ", " + requestHelperBean + "):BEGIN");
        String commandStatus = CommandManager.execute(componentDefinition, requestHelperBean);
        requestHelperBean.addAttributeToRequest("commandStatus", commandStatus);
        log.debug("@@@@PortletActionBean.invokeCommand(" + componentDefinition + ", " + requestHelperBean + "):END(" + commandStatus + ")");
    }

    /**
     * This method is used when you want to short circuit an Action
     * and change the template that will be executed next.
     *
     * @param data Turbine information.
     * @param template The template that will be executed next.
     */
    public void setTemplate(RunData data, String template) {
        data.getRequest().setAttribute("template", template);
    }

    /**
     * Performs the action
     *
     * @param rundata
     * @exception Exception
     */
    public void doMaintain(RunData rundata) throws Exception {
        RequestHelperBean requestHelperBean = new RequestHelperBean(rundata);
        this.invokeCommand(authenticate(requestHelperBean), requestHelperBean);
    }

    public void doReset(RunData rundata) throws Exception {
        RequestHelperBean requestHelperBean = new RequestHelperBean(rundata);
        this.invokeCommand(authenticate(requestHelperBean), requestHelperBean);
    }

    public void doSet(RunData rundata) throws Exception {
        RequestHelperBean requestHelperBean = new RequestHelperBean(rundata);
        this.invokeCommand(authenticate(requestHelperBean), requestHelperBean);
    }

    public void doDelete(RunData rundata) throws Exception {
        RequestHelperBean requestHelperBean = new RequestHelperBean(rundata);
        this.invokeCommand(authenticate(requestHelperBean), requestHelperBean);
    }

    public void doUpdate(RunData rundata) throws Exception {
        RequestHelperBean requestHelperBean = new RequestHelperBean(rundata);
        this.invokeCommand(authenticate(requestHelperBean), requestHelperBean);
    }

    public void doSearch(RunData rundata) throws Exception {
        RequestHelperBean requestHelperBean = new RequestHelperBean(rundata);
        this.invokeCommand(authenticate(requestHelperBean), requestHelperBean);
    }

    public void doCreate(RunData rundata) throws Exception {
        RequestHelperBean requestHelperBean = new RequestHelperBean(rundata);
        this.invokeCommand(authenticate(requestHelperBean), requestHelperBean);
    }

    public void doPerform(RunData rundata) throws Exception {
        Portlet portlet = (Portlet) rundata.getRequest().getAttribute("portlet");
        JetspeedRunData jdata = (JetspeedRunData) rundata;
        if (log.isDebugEnabled()) {
            log.debug("JspPortletAction: retrieved portlet: " + portlet);
        }
        if (portlet != null) {
            if ((jdata.getMode() == jdata.CUSTOMIZE) && (portlet.getName().equals(jdata.getCustomized().getName()))) {
                if (log.isDebugEnabled()) {
                    log.debug("JspPortletAction: building customize");
                }
                buildConfigureContext(portlet, rundata);
                return;
            }
            if (jdata.getMode() == jdata.MAXIMIZE) {
                if (log.isDebugEnabled()) {
                    log.debug("JspPortletAction: building maximize");
                }
                buildMaximizedContext(portlet, rundata);
                return;
            }
            if (log.isDebugEnabled()) {
                log.debug("JspPortletAction: building normal");
            }
            buildNormalContext(portlet, rundata);
        }
    }

    /**
     * This method should be called to execute the event based system.
     *
     * @param data Turbine information.
     * @exception Exception a generic exception.
     */
    public void executeEvents(RunData data) throws Exception {
        String theButton = null;
        Object[] args = new Object[1];
        Class[] classes = new Class[1];
        classes[0] = RunData.class;
        ParameterParser pp = data.getParameters();
        String button = pp.convert(BUTTON);
        for (Enumeration e = pp.keys(); e.hasMoreElements(); ) {
            String key = (String) e.nextElement();
            if (key.startsWith(button)) {
                String value = pp.getString(key);
                theButton = METHOD_NAME_PREFIX + firstLetterCaps(value);
                break;
            }
        }
        Portlet portlet = (Portlet) data.getRequest().getAttribute("portlet");
        if (theButton == null || !PortletSessionState.isMyRequest(data, portlet)) {
            throw new NoSuchMethodException("JspActionEvent: The button was null or not my request");
        }
        log.debug("\n****** PORTLET [" + portlet + "] executeEvent (" + theButton + ") is called.\n");
        Method method = getClass().getMethod(theButton, classes);
        args[0] = data;
    }

    /**
     * Makes the first letter caps and the rest lowercase.
     *
     * @param data The input string.
     * @return A string with the described case.
     */
    private final String firstLetterCaps(String data) {
        String firstLetter = data.substring(0, 1).toUpperCase();
        String restLetters = data.substring(1).toLowerCase();
        return firstLetter + restLetters;
    }

    /**
     * Subclasses should override this method if they wish to
     * build specific content when maximized. Default behavior is
     * to do the same as normal content.
     */
    protected void buildMaximizedContext(Portlet portlet, RunData rundata) throws Exception {
        buildNormalContext(portlet, rundata);
    }

    /**
     * Subclasses should override this method if they wish to
     * provide their own customization behavior.
     * Default is to use Portal base customizer action
     */
    protected void buildConfigureContext(Portlet portlet, RunData rundata) throws Exception {
    }

    /**
	     * Build the normal state content for this portlet.
	     *
	     * @param portlet The jsp-based portlet that is being built.
	     * @param rundata The turbine rundata context for this request.
	     */
    protected void buildNormalContext(Portlet portlet, RunData rundata) {
        try {
        } catch (Exception e) {
            log.error(e);
        }
    }
}
