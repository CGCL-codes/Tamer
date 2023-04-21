package org.riverock.module.factory;

import org.riverock.module.web.config.ModuleConfig;
import org.riverock.module.exception.ActionException;
import org.riverock.module.factory.config.ActionConfig;
import org.riverock.common.tools.StringTools;
import java.io.File;

/**
 * @author Serge Maslyukov
 *         Date: 24.04.2005
 *         Time: 20:54:12
 *         $Id: PortletActionFactoryImpl.java,v 1.2 2006/06/05 19:18:24 serg_main Exp $
 */
public abstract class PortletActionFactoryImpl extends ActionFactoryImpl {

    protected abstract File getConfigFile(String actionConfigFile);

    public ActionConfig getActionConfig(ModuleConfig moduleConfig, String factoryCode) throws ActionException {
        String actionConfigFile = moduleConfig.getInitParameter(factoryCode);
        if (StringTools.isEmpty(actionConfigFile)) {
            throw new ActionException("Factory code " + factoryCode + " not found in init parameter of portlet");
        }
        File configFile = getConfigFile(actionConfigFile);
        if (configFile == null || !configFile.exists()) {
            throw new ActionException("File " + actionConfigFile + " not found");
        }
        ActionConfig actionConfig = ActionConfig.getInstance(configFile);
        return actionConfig;
    }
}
