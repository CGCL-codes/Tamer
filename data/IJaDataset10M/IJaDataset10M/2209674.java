package org.signserver.rmi;

public interface ServerProperties {

    String getStartPortRMI();

    String getRegistryPortRMI();

    String getKeyFileName();

    String getKeyPassword();

    String getStorePassword();

    String getHandshakeInterval();

    String getAddress();
}
