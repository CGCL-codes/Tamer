package flex.messaging.config;

import javax.servlet.ServletConfig;
import flex.messaging.log.LogCategories;

/**
 * ConfigurationManager interface
 *
 * The default implementation of the configuration manager is
 * FlexConfigurationManager.  However, this value be specified in
 * a servlet init-param &quot;services.configuration.manager&quot;
 * to the MessageBrokerServlet.
 *
 * @exclude
 */
public interface ConfigurationManager {

    String LOG_CATEGORY = LogCategories.CONFIGURATION;

    MessagingConfiguration getMessagingConfiguration(ServletConfig servletConfig);

    void reportTokens();
}
