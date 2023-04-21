package org.jes.core.services;

import org.jes.core.services.Service;

/**
 * Defines a common interface that all services that listen for
 * incoming connections must implement.  Each
 * service is run as a seperate thread.  Before the thread is started,
 * each attribute must be set using the setXXX() methods defined.
 *
 * @author Eric Daugherty
 */
public interface ListenerService extends Service {

    int getListenPort();

    void setListenPort(int listenPort);

    String getListenAddress();

    void setListenAddress(String listenAddress);

    String getProtocolProcessorName();

    void setProtocolProcessorName(String protocolProcessorName);
}
