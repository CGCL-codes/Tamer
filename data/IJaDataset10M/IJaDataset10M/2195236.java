package org.j2eebuilder.view;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import org.j2eebuilder.BuilderHelperBean;
import org.j2eebuilder.BuilderException;
import org.j2eebuilder.ComponentDefinition;
import org.j2eebuilder.ApplicationDefinition;
import org.j2eebuilder.DefinitionException;
import org.j2eebuilder.DefinitionNotFoundException;
import org.j2eebuilder.NonManagedBeanDefinition;
import org.j2eebuilder.util.RequestParameterException;
import org.j2eebuilder.util.UtilityBean;
import org.j2eebuilder.view.Request;
import org.j2eebuilder.model.ManagedTransientObject;
import org.j2eebuilder.util.LogManager;

/**
 * @(#)DispatcherBean.java 1.350 01/12/03 A singleton Called by ViewController
 *                         (in a non-portal mode) or by PortalActionBean (in a
 *                         portal mode)
 * 
 */
public class DispatcherBean {

    private static transient LogManager log = new LogManager(DispatcherBean.class);

    private static Boolean semaphore = new Boolean(true);

    private static DispatcherBean globalDispatcherBean = null;

    public static DispatcherBean getCurrentInstance() {
        synchronized (semaphore) {
            if (globalDispatcherBean == null) {
                globalDispatcherBean = new DispatcherBean();
            }
        }
        return globalDispatcherBean;
    }

    public DispatcherBean() {
    }

    /**
	 * Authenticate the request (wrapped by Request) Retrieve
	 * ApplicationDefinition Get application's SessionBean object Authenticate
	 * and validate the mechanism and license If failure, if mechanism invalid
	 * then get component of componentType - "session" if license invalid then
	 * get componet - "License" If successful, continue Get j2eebuilder
	 * component upon which action is being performed Add component definition
	 * (found, session or License) to the request
	 * 
	 * @return component definition
	 */
    public ComponentDefinition authenticate(Request requestHelperBean) {
        org.j2eebuilder.ApplicationDefinition applicationDefinition = null;
        try {
            applicationDefinition = BuilderHelperBean.getCurrentInstance().getApplicationDefinition(requestHelperBean);
            String applicationName = applicationDefinition.getName();
            Session sessionBean = requestHelperBean.getSessionObject();
            boolean mechanismAuthentication = false;
            boolean licenseValidation = false;
            try {
                Method isMechanismValid = sessionBean.getClass().getMethod(Session.IS_MECHANISM_VALID, null);
                Method isLicenseValid = sessionBean.getClass().getMethod(Session.IS_LICENSE_VALID, null);
                Boolean _mechanismAuthentication = (Boolean) isMechanismValid.invoke(sessionBean, null);
                mechanismAuthentication = _mechanismAuthentication.booleanValue();
                boolean disableLicenseValidation = true;
                if (disableLicenseValidation) {
                    licenseValidation = true;
                } else {
                    Boolean _licenseValidation = (Boolean) isLicenseValid.invoke(sessionBean, new Object[] { requestHelperBean });
                    licenseValidation = _licenseValidation.booleanValue();
                }
            } catch (java.lang.reflect.InvocationTargetException ite) {
                log.log("User is not valid. Error occurred while executing method [" + Session.IS_MECHANISM_VALID + "]", ite, log.DEBUG);
            } catch (IllegalAccessException iae) {
                throw new org.j2eebuilder.BuilderException(iae.toString());
            } catch (NoSuchMethodException me) {
                throw new org.j2eebuilder.BuilderException(me.toString());
            } catch (Exception e) {
                throw new org.j2eebuilder.BuilderException(e.toString());
            }
            String componentName = null;
            ComponentDefinition componentDefinition = null;
            try {
                if (!mechanismAuthentication) {
                    throw new org.j2eebuilder.model.MechanismAuthenticationException("Invalid authentication.");
                }
                if (!licenseValidation) {
                    log.debug("Unable to authenticate: 6");
                    throw new org.j2eebuilder.license.LicenseViolationException("Invalid license.");
                }
                componentName = BuilderHelperBean.getCurrentInstance().getComponentName(requestHelperBean);
                componentDefinition = BuilderHelperBean.getCurrentInstance().findComponentDefinitionByName(componentName, requestHelperBean);
            } catch (org.j2eebuilder.model.MechanismAuthenticationException me) {
                componentDefinition = BuilderHelperBean.getCurrentInstance().findComponentDefinitionByName("Home", requestHelperBean);
                performDefaultLogin(componentDefinition, requestHelperBean);
            } catch (org.j2eebuilder.license.LicenseViolationException le) {
                componentDefinition = BuilderHelperBean.getCurrentInstance().findComponentDefinitionByName("License", requestHelperBean);
            } catch (DefinitionException de) {
                log.log(de.toString(), de, log.WARN);
                componentDefinition = BuilderHelperBean.getCurrentInstance().findComponentDefinitionByName("Home", requestHelperBean);
            } catch (Exception e) {
                log.log(e.toString(), e, log.WARN);
                componentDefinition = BuilderHelperBean.getCurrentInstance().findComponentDefinitionByName("Home", requestHelperBean);
            }
            if (componentDefinition == null) {
                throw new DefinitionNotFoundException("None of the minimum required definitions - License and session - were found. Application can not function and is terminated.");
            }
            this.addComponentInfoToRequest(applicationDefinition, componentDefinition, requestHelperBean);
            return componentDefinition;
        } catch (Exception be) {
            log.error(".authenticate():", be);
        }
        return null;
    }

    /**
	 * get component definition of type = session and then try default login
	 * 
	 * @param applicationDefinition
	 * @param requestHelperBean
	 * @return
	 */
    private void performDefaultLogin(ComponentDefinition componentDefinition, Request requestHelperBean) throws DefinitionException {
        try {
            String commandName = null;
            try {
                commandName = CommandManager.getCommandName(requestHelperBean);
            } catch (Exception e1) {
                commandName = Command.NOT_SUPPORTED;
            }
            String componentNameInRequestContext = null;
            try {
            } catch (Exception e1) {
                componentNameInRequestContext = (String) requestHelperBean.getAttributeFromRequest(BuilderHelperBean.getCurrentInstance().REQUEST_ATTRIBUTE_ID_OF_COMPONENT_NAME);
            }
            if (!requestHelperBean.getIsPageContext() && !requestHelperBean.isStateless() && "Home".equals(BuilderHelperBean.getCurrentInstance().getComponentName(requestHelperBean)) && !Command.LOGOFF.equals(commandName) && !Command.LOGIN.equals(commandName)) {
                log.debug("finding component definition of session type componentName from builder.componentName[" + BuilderHelperBean.getCurrentInstance().getComponentName(requestHelperBean) + "] componentNameInRequest[" + componentNameInRequestContext + "] command[" + commandName + "]");
                String defaultUsername = componentDefinition.getNonManagedBeansDefinition().findNonManagedBeanDefinitionByTypeAndScope(NonManagedBeanDefinition.TYPE_TRANSIENTOBJECT, NonManagedBeanDefinition.SCOPE_SESSION).getAttributesDefinition().findAttributeDefinitionByName("username").getAttributeValue();
                String defaultPassword = componentDefinition.getNonManagedBeansDefinition().findNonManagedBeanDefinitionByTypeAndScope(NonManagedBeanDefinition.TYPE_TRANSIENTOBJECT, NonManagedBeanDefinition.SCOPE_SESSION).getAttributesDefinition().findAttributeDefinitionByName("password").getAttributeValue();
                requestHelperBean.addAttributeToRequest("username", defaultUsername);
                requestHelperBean.addAttributeToRequest("password", defaultPassword);
                LoginCommandBean.getCurrentInstance().execute(componentDefinition, requestHelperBean);
            }
        } catch (Exception e1) {
            log.log("Failed to authenticate using default login. Error getting component definition of session type.", e1, log.DEBUG);
        }
    }

    /**
	 * 1st. applicationDefinition.findLayoutDefinitionByName(getOrganization().
	 * getLayout() == null? "default": getOrganization().getLayout())
	 * 
	 * 2nd. componentDefinition.layouts.layout(where
	 * serverName=request.servername)
	 * 
	 * 3rd. componentDefinition.layouts.layout(where name=default)
	 * 
	 * @param applicationDefinition
	 * @return
	 */
    public String getView(ApplicationDefinition applicationDefinition, Request requestHelperBean) {
        if (applicationDefinition == null) {
            return "/org/j2eebuilder/view/Error.jsp";
        }
        try {
            if (requestHelperBean != null && requestHelperBean.getSessionObject().getOrganizationVO() != null && !UtilityBean.getCurrentInstance().isNullOrEmpty(requestHelperBean.getSessionObject().getOrganizationVO().getLayout())) {
                log.debug("found org layout[" + requestHelperBean.getSessionObject().getOrganizationVO().getLayout() + "]");
                return applicationDefinition.findLayoutDefinitionByName(requestHelperBean.getSessionObject().getOrganizationVO().getLayout()).getUrl();
            }
        } catch (org.j2eebuilder.view.SessionException e) {
            log.debug("Unable to determine layout from Organization. Error [" + e.toString() + "]");
        } catch (org.j2eebuilder.DefinitionException e) {
            log.debug("Unable to determine layout from Organization. Error [" + e.toString() + "]");
        }
        try {
            if (requestHelperBean != null && !UtilityBean.getCurrentInstance().isNullOrEmpty(requestHelperBean.getServerName())) {
                return applicationDefinition.findLayoutDefinitionByServerName(requestHelperBean.getServerName()).getUrl();
            }
        } catch (org.j2eebuilder.DefinitionException e) {
            log.debug("Unable to determine layout from request server name. Error [" + e.toString() + "]");
        } catch (org.j2eebuilder.view.SessionException e) {
            log.debug("Unable to determine layout from request server name. Error [" + e.toString() + "]");
        }
        try {
            if (requestHelperBean != null) {
                return applicationDefinition.findLayoutDefinitionByName("default").getUrl();
            }
        } catch (org.j2eebuilder.DefinitionException e) {
            log.debug("Unable to determine layout by name[default]. Error [" + e.toString() + "]");
        }
        return "/org/j2eebuilder/view/Error.jsp";
    }

    public void dispatch(Request requestHelperBean, String page) throws ServletException, java.io.IOException {
        requestHelperBean.forward(page);
        return;
    }

    private void addComponentInfoToRequest(ApplicationDefinition applicationDefinition, ComponentDefinition componentDefinition, Request requestHelperBean) throws BuilderException, SessionException {
        if (componentDefinition == null) {
            throw new BuilderException("Invalid request. Unable to retrieve componentDefinition from the request. Request not processed. ");
        }
        String applicationName = applicationDefinition.getName();
        requestHelperBean.addAttributeToApplicationResponse(BuilderHelperBean.getCurrentInstance().REQUEST_ATTRIBUTE_ID_OF_APPLICATION_NAME, applicationName);
        requestHelperBean.addAttributeToApplicationResponse(BuilderHelperBean.getCurrentInstance().REQUEST_ATTRIBUTE_ID_OF_COMPONENT_NAME, componentDefinition.getName());
    }

    public void addCommandStatusToResponse(Request requestHelperBean, String commandStatus) {
        try {
            requestHelperBean.addAttributeToApplicationResponse(CommandManager.SUBMIT, CommandManager.getCommandName(requestHelperBean));
            requestHelperBean.addAttributeToApplicationResponse(CommandManager.COMMAND_STATUS, commandStatus);
            try {
                requestHelperBean.addAttributeToApplicationResponse(CommandManager.SET_LISTNAME, CommandManager.getListName(requestHelperBean));
                requestHelperBean.addAttributeToApplicationResponse(CommandManager.SET_LISTREQUESTEDBY, CommandManager.getListRequestedBy(requestHelperBean));
            } catch (RequestParameterException e) {
                log.debug(e);
            }
        } catch (InvalidCommandException e) {
            log.error(e);
        }
    }

    public String invokeCommand(ComponentDefinition componentDefinition, Request requestHelperBean) {
        String commandStatus = CommandManager.execute(componentDefinition, requestHelperBean);
        return commandStatus;
    }
}
