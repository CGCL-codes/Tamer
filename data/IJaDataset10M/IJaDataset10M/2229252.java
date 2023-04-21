package org.riverock.module.factory;

import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.riverock.module.Constants;
import org.riverock.module.action.Action;
import org.riverock.module.action.ModuleActionRequest;
import org.riverock.module.exception.ActionException;
import org.riverock.module.factory.config.ActionConfig;
import org.riverock.module.factory.bean.ActionConfigurationBean;
import org.riverock.module.factory.bean.ActionBean;
import org.riverock.module.factory.bean.ForwardBean;
import org.riverock.module.web.user.ModuleUser;
import org.riverock.module.web.request.ModuleRequest;

/**
 * @author Serge Maslyukov
 *         Date: 24.04.2005
 *         Time: 21:31:15
 *         $Id: ActionFactoryImpl.java,v 1.5 2006/06/05 19:18:24 serg_main Exp $
 */
public abstract class ActionFactoryImpl implements ActionFactory {

    private static final Logger log = Logger.getLogger(WebmillPortletActionFactoryImpl.class);

    protected ActionConfig actionConfig = null;

    public String doAction(ModuleActionRequest moduleActionRequest) throws ActionException {
        ModuleUser user = moduleActionRequest.getRequest().getUser();
        if (user != null) {
            moduleActionRequest.getRequest().setAttribute(Constants.PRINCIPAL_BEAN, user);
        }
        if (log.isDebugEnabled()) {
            log.debug("user: " + user);
            log.debug("actionString: " + moduleActionRequest.getActionName());
        }
        ActionConfigurationBean actionConfigurationBean = null;
        if (moduleActionRequest.getActionName() != null) {
            actionConfigurationBean = getAction(moduleActionRequest.getActionName());
        }
        if (log.isDebugEnabled()) {
            log.debug("actionConfigurationBean: " + actionConfigurationBean);
        }
        if (actionConfigurationBean == null) {
            actionConfigurationBean = getDefaultAction();
        }
        if (log.isDebugEnabled()) {
            log.debug("result actionConfigurationBean: " + actionConfigurationBean);
        }
        ActionBean actionBean = actionConfigurationBean.getActionBean();
        String forwardPath = checkAccess(actionConfig.getConfigBean().getRoles(), actionBean, moduleActionRequest.getRequest());
        if (forwardPath != null) {
            return forwardPath;
        }
        forwardPath = checkAccess(actionBean.getRoles(), actionBean, moduleActionRequest.getRequest());
        if (forwardPath != null) {
            return forwardPath;
        }
        ForwardBean forward = null;
        String forwardName = actionConfigurationBean.getAction().execute(moduleActionRequest);
        if (log.isDebugEnabled()) {
            log.debug("forwardName after execute action: " + forwardName);
            log.debug("actionBean.isRedirect(): " + actionBean.isRedirect());
        }
        if (forwardName == null) {
            if (actionBean.isRedirect()) {
                return null;
            }
            if (actionBean.getDefaultForward() == null) {
                if (log.isDebugEnabled()) {
                    log.debug("forwardName is null defaultForward for action '" + moduleActionRequest.getActionName() + "' is null");
                }
                forward = getForwardPath(actionBean, Action.DEFAULT_FORWARD_ERROR);
                if (forward == null) {
                    throw new IllegalStateException();
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("Return defaultForward for action '" + moduleActionRequest.getActionName() + "': " + actionBean.getDefaultForward());
            }
            return actionBean.getDefaultForward();
        }
        forward = getForwardPath(actionBean, forwardName);
        if (forward == null) {
            if (actionBean.getDefaultForward() == null) {
                throw new IllegalStateException("Forward with name '" + forwardName + "' not found");
            } else {
                return actionBean.getDefaultForward();
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("Forward path: " + forward.getPath());
        }
        return forward.getPath();
    }

    public String getForwardPath(String actionName, String forwardName) throws ActionException {
        ActionConfigurationBean actionConfigurationBean = null;
        if (actionName != null) {
            actionConfigurationBean = getAction(actionName);
        }
        if (actionConfigurationBean == null) {
            actionConfigurationBean = getDefaultAction();
        }
        if (actionConfigurationBean == null) {
            throw new ActionException("actionConfigurationBean not created. actionName: " + actionName);
        }
        ActionBean actionBean = actionConfigurationBean.getActionBean();
        ForwardBean forward = getForwardPath(actionBean, forwardName);
        if (forward == null) {
            throw new IllegalStateException("Forward with name '" + forwardName + "' not found");
        }
        if (log.isDebugEnabled()) {
            log.debug("Forward path: " + forward.getPath());
        }
        return forward.getPath();
    }

    private String checkAccess(List roles, ActionBean actionBean, ModuleRequest moduleRequest) {
        boolean isDenied = true;
        ForwardBean forward = null;
        if (!roles.isEmpty()) {
            log.debug("Check roles");
            Iterator it = roles.iterator();
            while (it.hasNext()) {
                String role = (String) it.next();
                if (log.isDebugEnabled()) {
                    log.debug("   role: " + role + ", user has this role: " + moduleRequest.isUserInRole(role));
                }
                if (moduleRequest.isUserInRole(role)) {
                    return null;
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("isDenied: " + isDenied);
            }
            if (isDenied) {
                if (log.isDebugEnabled()) {
                    log.debug("Start search forward '" + Action.DEFAULT_FORWARD_ACCESS_DENIED + "'");
                }
                forward = getForwardPath(actionBean, Action.DEFAULT_FORWARD_ACCESS_DENIED);
                if (forward == null) {
                    if (log.isDebugEnabled()) {
                        log.debug("Start search forward '" + Action.DEFAULT_FORWARD_ERROR + "'");
                    }
                    forward = getForwardPath(actionBean, Action.DEFAULT_FORWARD_ERROR);
                }
                if (forward == null) {
                    throw new IllegalStateException();
                }
                if (log.isDebugEnabled()) {
                    log.debug("Result forward path: " + forward.getPath());
                }
                return forward.getPath();
            }
        }
        return null;
    }

    private ForwardBean getForwardPath(ActionBean actionBean, String name) {
        ForwardBean forward = (ForwardBean) actionBean.getForwards().get(name);
        if (log.isDebugEnabled()) {
            log.debug("ForwardBean for name '" + name + "' is " + forward);
        }
        if (forward == null) {
            forward = (ForwardBean) actionConfig.getConfigBean().getForwards().get(name);
        }
        if (log.isDebugEnabled()) {
            log.debug("Result forwardBean for name '" + name + "' is " + forward);
        }
        return forward;
    }

    public void destroy() {
        if (actionConfig != null) {
            actionConfig.destroy();
        }
    }

    public ActionConfigurationBean getAction(String actionName) {
        return actionConfig.getActionConfiguredBean(actionName);
    }

    public ActionConfigurationBean getDefaultAction() {
        return actionConfig.getDefaultAction();
    }
}
