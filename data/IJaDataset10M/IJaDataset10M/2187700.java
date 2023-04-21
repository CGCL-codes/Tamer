package org.argoidea.argobindings;

import com.intellij.openapi.application.ApplicationManager;
import org.argoidea.ArgoIdeaApplicationIntegration;
import org.argoidea.config.GlobalConfigurationData;
import org.argouml.configuration.ConfigurationHandler;
import org.argouml.configuration.IConfigurationFactory;

/**
 * A factory object that provides configuration information.
 *
 * @author Tom Morris
 * @author Juergen Kellerer
 */
public class ArgoConfigurationFactory implements IConfigurationFactory {

    /**
	 * The active configuration handler.
	 */
    private static ArgoConfigurationHandler handler = new ArgoConfigurationHandler();

    public static GlobalConfigurationData getConfigurationData() {
        return handler.configurationData;
    }

    public static void setConfigurationData(GlobalConfigurationData configurationData) {
        handler.configurationData = configurationData;
    }

    /**
	 * {@inheritDoc}
	 */
    public ConfigurationHandler getConfigurationHandler() {
        return handler;
    }

    public ArgoConfigurationFactory() {
        setConfigurationData(ApplicationManager.getApplication().getComponent(ArgoIdeaApplicationIntegration.class).getState());
    }
}
