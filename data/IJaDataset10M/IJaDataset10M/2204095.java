package uips.events.java;

import uips.events.IClient;
import uips.events.IEvent;
import uips.events.IServer;

/**
 * This interface must be implemented by every extern Java event handler.
 * <br><br>
 * Based on Miroslav Macik's C# version of UIProtocolServer
 *
 * @author Miroslav Macik (macikm1@fel.cvut.cz, CTU Prague, FEE)
 * @author Jindrich Basek, (basekjin@fel.cvut.cz, CTU Prague, FEE)
 */
public interface IJavaEventHandler {

    /**
     * Method thah starts Java event handler
     *
     * @param event <code>IEvent</code> instance
     * @param client <code>IClient</code> instance
     * @param server <code>IServer</code> instance
     */
    public void handleEvent(IEvent event, IClient client, IServer server);

    /**
     * This methods return id of event that this handler will handle
     * 
     * @return event id
     */
    public String getHandlerClass();
}
