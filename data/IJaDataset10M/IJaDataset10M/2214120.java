package org.j2eebuilder.view;

import org.j2eebuilder.BuilderException;
import org.j2eebuilder.BuilderHelperBean;
import org.j2eebuilder.ComponentDefinition;
import org.j2eebuilder.DefinitionNotFoundException;
import org.j2eebuilder.model.ManagedTransientObject;
import org.j2eebuilder.util.LogManager;
import org.j2eebuilder.util.PrimaryKeyFactoryException;
import java.util.Map;
import java.util.Iterator;
import org.j2eebuilder.util.RequestParameterException;

/**
 *
 * @author sdixit
 */
public class EmbeddedCreateCommand extends CreateCommand {

    private static transient LogManager log = new LogManager(EmbeddedCreateCommand.class);

    private static Boolean semaphore = new Boolean(true);

    private static EmbeddedCreateCommand globalEmbeddedCreateCommand = null;

    public static EmbeddedCreateCommand getCurrentInstance() {
        synchronized (semaphore) {
            if (globalEmbeddedCreateCommand == null) {
                globalEmbeddedCreateCommand = new EmbeddedCreateCommand();
            }
        }
        return globalEmbeddedCreateCommand;
    }

    public String getCommandName() {
        return CommandImpl.EMBEDDED_CREATE;
    }

    public void postExecute(ComponentDefinition componentDefinition, BusinessDelegate businessDelegate, ManagedTransientObject valueObject, Object postValueObject, Request requestHelperBean, String scope) throws BeanRegistrationException, DefinitionNotFoundException, BuilderException, SessionException, PrimaryKeyFactoryException, BusinessDelegateException {
        scope = componentDefinition.getNonManagedBeansDefinition().getComponentObjectStateManagement();
        super.postExecute(componentDefinition, businessDelegate, valueObject, (ManagedTransientObject) postValueObject, requestHelperBean, scope);
        try {
            log.debug("EMBEDDED_CREATE:listRequestedByAttribute:" + requestHelperBean.getStringParameter("listRequestedByAttribute")[0]);
        } catch (Exception e) {
            log.debug(e);
        }
        try {
            log.debug("EMBEDDED_CREATE:listName:" + requestHelperBean.getStringParameter("listName")[0]);
        } catch (Exception e) {
            log.debug(e);
        }
        String listRequestedBy = null;
        try {
            listRequestedBy = requestHelperBean.getStringParameter(CommandManager.SET_LISTREQUESTEDBY)[0];
            try {
                log.debug("EMBEDDED_CREATE:valueObject:" + (ManagedTransientObject) postValueObject);
                if (valueObject != null) {
                    Map primaryKeySet = ((ManagedTransientObject) postValueObject).getPrimaryKeyVO();
                    if (primaryKeySet != null) {
                        for (Iterator itr = primaryKeySet.entrySet().iterator(); itr.hasNext(); ) {
                            Map.Entry mapEntry = (Map.Entry) itr.next();
                            requestHelperBean.removeAttributeFromRequest((String) mapEntry.getKey());
                            requestHelperBean.addAttributeToRequest((String) mapEntry.getKey(), mapEntry.getValue());
                        }
                    }
                }
            } catch (Exception e) {
                log.printStackTrace(e, LogManager.ERROR);
            }
            postValueObject = SetCustomAttributeCommandBean.getCurrentInstance().execute(BuilderHelperBean.getCurrentInstance().findComponentDefinitionByName(listRequestedBy), requestHelperBean);
        } catch (Exception e) {
            log.debug(e);
            throw new BuilderException(e);
        }
    }

    /**
     * navigate-to after the command is completed - either successfully or in error
     * added in the finally of the execute
     */
    public void navigateTo(ComponentDefinition componentDefinition, Request requestHelperBean) {
        requestHelperBean.removeAttributeFromRequest(BuilderHelperBean.getCurrentInstance().REQUEST_ATTRIBUTE_ID_OF_COMPONENT_NAME);
        try {
            requestHelperBean.addAttributeToRequest(BuilderHelperBean.getCurrentInstance().REQUEST_ATTRIBUTE_ID_OF_COMPONENT_NAME, requestHelperBean.getStringParameter(CommandManager.SET_LISTREQUESTEDBY)[0]);
        } catch (RequestParameterException e) {
            super.navigateTo(componentDefinition, requestHelperBean);
        }
    }
}
