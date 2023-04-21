package net.sourceforge.jcoupling.bus.server;

import net.sourceforge.jcoupling.peer.interaction.MessageBusServerImpl;
import net.sourceforge.jcoupling.bus.dao.DaoFactoryType;
import net.sourceforge.jcoupling.bus.server.callout.RequestController;

/**
 * @author Lachlan Aldred
 */
public class JMSServer {

    MessageBusServerImpl messageServer;

    RequestController controller;

    public JMSServer() {
        messageServer = new MessageBusServerImpl();
        controller = new RequestController(DaoFactoryType.In_Memory, messageServer.getBusController());
    }
}
