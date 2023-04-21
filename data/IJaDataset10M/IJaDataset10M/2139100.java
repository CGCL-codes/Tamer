package cowsultants.itracker.web.actions;

import java.io.IOException;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import cowsultants.itracker.ejb.client.exceptions.SystemConfigurationException;
import cowsultants.itracker.ejb.client.interfaces.SystemConfiguration;
import cowsultants.itracker.ejb.client.interfaces.SystemConfigurationHome;
import cowsultants.itracker.ejb.client.models.ConfigurationModel;
import cowsultants.itracker.ejb.client.util.Logger;
import cowsultants.itracker.ejb.client.util.SystemConfigurationUtilities;
import cowsultants.itracker.ejb.client.util.UserUtilities;

public class OrderConfigurationItemAction extends ITrackerAction {

    public OrderConfigurationItemAction() {
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();
        if (!isLoggedIn(request, response)) {
            return mapping.findForward("login");
        }
        if (!hasPermission(UserUtilities.PERMISSION_USER_ADMIN, request, response)) {
            return mapping.findForward("unauthorized");
        }
        try {
            InitialContext ic = new InitialContext();
            Object scRef = ic.lookup("java:comp/env/" + SystemConfiguration.JNDI_NAME);
            SystemConfigurationHome scHome = (SystemConfigurationHome) PortableRemoteObject.narrow(scRef, SystemConfigurationHome.class);
            SystemConfiguration sc = scHome.create();
            Integer configId = (Integer) PropertyUtils.getSimpleProperty(form, "id");
            String action = (String) PropertyUtils.getSimpleProperty(form, "action");
            if (configId == null || configId.intValue() <= 0) {
                throw new SystemConfigurationException("Invalid configuration id.");
            }
            ConfigurationModel configItem = sc.getConfigurationItem(configId);
            if (configItem == null) {
                throw new SystemConfigurationException("Invalid configuration id.");
            }
            int configType = configItem.getType();
            ConfigurationModel[] configItems = sc.getConfigurationItemsByType(configType);
            for (int i = 0; i < configItems.length; i++) {
                if (configItems[i] != null && configId.equals(configItems[i].getId())) {
                    if ("up".equalsIgnoreCase(action) && i > 0) {
                        int tempOrder = configItems[i].getOrder();
                        configItems[i].setOrder(configItems[i - 1].getOrder());
                        configItems[i - 1].setOrder(tempOrder);
                        configItems = sc.updateConfigurationItems(configItems, configType);
                    } else if ("down".equalsIgnoreCase(action) && i < (configItems.length - 1)) {
                        int tempOrder = configItems[i].getOrder();
                        configItems[i].setOrder(configItems[i + 1].getOrder());
                        configItems[i + 1].setOrder(tempOrder);
                        configItems = sc.updateConfigurationItems(configItems, configType);
                    }
                    break;
                }
            }
            if (configType == SystemConfigurationUtilities.TYPE_RESOLUTION) {
                sc.resetConfigurationCache(SystemConfigurationUtilities.TYPE_RESOLUTION);
            } else if (configType == SystemConfigurationUtilities.TYPE_SEVERITY) {
                sc.resetConfigurationCache(SystemConfigurationUtilities.TYPE_SEVERITY);
            }
            return mapping.findForward("listconfiguration");
        } catch (SystemConfigurationException nfe) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("itracker.web.error.invalidconfiguration"));
            Logger.logDebug("Invalid configuration item id " + request.getParameter("id") + " specified.");
        } catch (NumberFormatException nfe) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("itracker.web.error.invalidconfiguration"));
            Logger.logDebug("Invalid configuration item id " + request.getParameter("id") + " specified.");
        } catch (Exception e) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("itracker.web.error.system"));
            Logger.logError("System Error.", e);
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
        }
        return mapping.findForward("error");
    }
}
