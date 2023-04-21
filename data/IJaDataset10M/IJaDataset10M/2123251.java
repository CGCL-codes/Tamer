package org.apache.log4j.nagios;

import java.io.FileNotFoundException;
import org.jboss.monitor.alerts.JBossAlertListenerMBean;

/**
 * MBean interface for Nagios Alerts within JBoss
 *
 * @author <a href="mailto:jarlyons@gmail.com">Jar Lyons</a>
 *
 **/
public interface NagiosAlertListenerMBean extends JBossAlertListenerMBean {

    public void setNagiosHost(String host);

    public void setNagiosPort(String port);

    public void setNagiosServiceName(String serviceName);

    public void setNagiosVirtualHost(String virtualHostname);

    public void setNscaConfigFile(String nscaConfigFile) throws FileNotFoundException;

    public void setMessageTemplate(String messageTemplate);

    public void setNagiosLevelUNKNOWN(String ignore);

    public void setNagiosLevelOK(String ignore);

    public void setNagiosLevelWARN(String ignore);

    public void setNagiosLevelCRITICAL(String ignore);

    public void setUseShortHostname(String ignore);
}
