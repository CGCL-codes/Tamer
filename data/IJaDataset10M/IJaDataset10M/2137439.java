package org.jes.jboss;

import org.jboss.system.ServiceMBean;

public interface JESMBean extends ServiceMBean {

    int getSmtpPort();

    void setSmtpPort(int smtpPort);
}
