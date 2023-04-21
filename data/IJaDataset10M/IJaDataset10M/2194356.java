package de.fuh.xpairtise.tests.common.network.activemq;

import de.fuh.xpairtise.common.network.INetworkFactory;
import de.fuh.xpairtise.common.network.imp.activemq.ActiveMQNetworkFactory;
import de.fuh.xpairtise.tests.common.network.ServerCommandTransmissionTestBase;

public class ActiveMQServerCommandTransmissionTest extends ServerCommandTransmissionTestBase {

    @Override
    protected INetworkFactory getNetworkFactory() throws Exception {
        return new ActiveMQNetworkFactory();
    }
}
