package br.gov.component.demoiselle.monitoring.web.zabbix;

import br.gov.component.demoiselle.monitoring.agent.zabbix.ZabbixAgent;
import br.gov.component.demoiselle.monitoring.config.zabbix.ZabbixConfig;
import br.gov.component.demoiselle.monitoring.trapper.zabbix.ZabbixTrapper;
import br.gov.component.demoiselle.monitoring.web.AbstractMonitoringListener;

/**
 * @author SERPRO/CETEC
 */
public class ZabbixListener extends AbstractMonitoringListener {

    private ZabbixConfig config;

    public ZabbixListener() {
        config = ZabbixConfig.getInstance();
    }

    protected void initializeSettings() {
        if (config.isAgentEnabled()) {
            this.setAgent(new ZabbixAgent());
        }
        if (config.isTrapperEnabled()) {
            this.setTrapper(new ZabbixTrapper());
        }
    }
}
