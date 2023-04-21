package br.gov.frameworkdemoiselle.monitoring.internal.bootstrap;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.BeforeShutdown;
import org.slf4j.Logger;
import br.gov.frameworkdemoiselle.internal.bootstrap.AbstractBootstrap;
import br.gov.frameworkdemoiselle.internal.configuration.ConfigurationLoader;
import br.gov.frameworkdemoiselle.internal.producer.LoggerProducer;
import br.gov.frameworkdemoiselle.monitoring.internal.configuration.zabbix.ZabbixAgentConfig;
import br.gov.frameworkdemoiselle.monitoring.internal.implementation.zabbix.ZabbixAgent;
import br.gov.frameworkdemoiselle.util.Beans;

/**
 * Bootstrap class intented to initialize and start the <b>Zabbix agent</b> automatically
 * on application startup. Moreover, it stops the agent before application shutdown.
 * 
 * @author SERPRO
 */
public class ZabbixAgentBootstrap extends AbstractBootstrap {

    private static Logger logger = LoggerProducer.create(ZabbixAgentBootstrap.class);

    private static final Class<ZabbixAgentConfig> ZABBIX_AGENT_CONFIG = ZabbixAgentConfig.class;

    private ZabbixAgent agent;

    private ZabbixAgentConfig config;

    private boolean started = false;

    private void readConfiguration() {
        getLogger().debug(getBundle().getString("bootstrap.configuration.processing", ZABBIX_AGENT_CONFIG.toString()));
        config = Beans.getReference(ZABBIX_AGENT_CONFIG);
        ConfigurationLoader loader = Beans.getReference(ConfigurationLoader.class);
        loader.load(config);
    }

    public void onStartup(@Observes final AfterDeploymentValidation event) {
        if (config == null) {
            readConfiguration();
        }
        if (config.isAgentEnabled()) {
            logger.info(getBundle(BootstrapConstants.BUNDLE_NAME).getString("agent-zabbix-startup"));
            agent = Beans.getReference(ZabbixAgent.class);
            agent.startup();
            started = true;
        }
    }

    public void onShutdown(@Observes final BeforeShutdown event) {
        if (started) {
            logger.info(getBundle(BootstrapConstants.BUNDLE_NAME).getString("agent-zabbix-shutdown"));
            agent.shutdown();
            agent = null;
        }
    }
}
