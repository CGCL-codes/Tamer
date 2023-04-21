package de.robowars.comm.impl;

import junit.framework.TestCase;
import org.apache.log4j.Category;
import de.robowars.comm.*;
import de.robowars.comm.Server;
import de.robowars.comm.transport.*;

/**
 * @author mir
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class ServerImpl extends TestCase implements Server {

    int player = 0;

    private static Category log = Category.getInstance(ServerImpl.class);

    private ClientFacade clientFacade;

    public ServerImpl(String arg0) {
        super(arg0);
    }

    public ServerImpl(ClientFacade clientFacade) {
        this("Dummy ServerImpl");
        this.clientFacade = clientFacade;
        clientFacade.setServer(this);
    }

    public void cardsSelected(CardSelection cards) {
        log.debug("cardsSelected called by " + cards.getPlayerId());
    }

    public void positionSelected(PositionSelection newPos) {
        log.debug("positionSelected called by " + newPos.getPlayerId() + ",notifying all players");
        clientFacade.notifyPosition(newPos);
    }

    public synchronized Acceptance registerPlayer(Application application) {
        if (player < 3) {
            log.debug("Accepted " + player + "...");
            return TestDataGenerator.getAcceptance(player++);
        } else {
            log.debug("Declined Acceptance of player no. " + player);
            return null;
        }
    }

    public void roundExecutionFinished(int playerId) {
        log.debug("roundExceutionFinished called by " + playerId);
    }

    public void withdrawal(int playerId) {
        log.debug("withdrawal called");
    }
}
