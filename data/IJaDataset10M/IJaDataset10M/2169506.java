package de.robowars.server;

import javax.xml.bind.StructureValidationException;
import java.util.List;
import de.robowars.comm.ClientFacade;
import de.robowars.comm.Server;
import de.robowars.comm.transport.CardSet;
import de.robowars.comm.transport.PositionSelection;
import de.robowars.comm.transport.RoundProtocol;
import de.robowars.comm.transport.StartSituation;
import de.robowars.comm.transport.PositionSelection;
import de.robowars.comm.transport.Position;
import de.robowars.comm.transport.Direction;
import de.robowars.comm.transport.DirectionTypes;
import de.robowars.comm.transport.PositionRequest;
import de.robowars.comm.transport.Point;
import de.robowars.comm.transport.CardSelection;

/**
 * A dummy for the client to test the server independently.
 * 
 * @author Kai Mueller
 **/
public class ServerTestClientFacade implements ClientFacade {

    private Server server;

    /**
	 * Constructor for ServerTestClientFacade.
	 */
    public ServerTestClientFacade(Server server) {
        this.server = server;
    }

    /**
	 * @see ClientFacade#startGame(StartSituation)
	 */
    public void startGame(StartSituation start) {
    }

    /**
	 * @see ClientFacade#requestPosition(int)
	 */
    public void requestPosition(PositionRequest arg) {
        PositionSelection pos = new PositionSelection();
        Point position = new Point();
        position.setX(((Point) arg.getPoint().get(0)).getX());
        position.setY(((Point) arg.getPoint().get(0)).getY());
        Direction dir = new Direction();
        dir.setValue(DirectionTypes.NORTH);
        pos.setDirection(dir);
        pos.setPlayerId(arg.getPlayerId());
        pos.setPoint(position);
        System.out.println("Rufe positionSelect auf dem server objekt" + arg.getPlayerId());
        server.positionSelected(pos);
        System.out.println("zur�ck von positionSelect auf Server" + arg.getPlayerId());
    }

    /**
	 * @see ClientFacade#notifyPosition(PositionSelection)
	 */
    public void notifyPosition(PositionSelection chosenPos) {
    }

    /**
	 * @see ClientFacade#issueCards(CardSet[])
	 * 
	 */
    public void issueCards(CardSet[] cards) {
        for (int i = 0; i < cards.length; i++) {
            System.out.println("Spielernr: " + cards[i].getPlayerId());
            System.out.println("Kartenanzahl" + cards[i].getCardsDealt());
            CardSelection newCards = new CardSelection();
            List tmpList = cards[i].getCard();
            List cardList = newCards.getCard();
            for (int j = 0; j < 5; j++) cardList.add(tmpList.get(j));
            newCards.setPlayerId(cards[i].getPlayerId());
            server.cardsSelected(newCards);
        }
    }

    /**
	 * @see ClientFacade#carryOut(RoundProtocol)
	 */
    public void carryOut(RoundProtocol protocol) {
        System.out.println("Starting printing protocol....\n\n\n");
        System.out.println(RoundProtocolFactory.logState());
        System.out.println("End printing protocol....");
    }

    /**
	 * @see ClientFacade#kick(int)
	 */
    public void kick(int playerId) {
    }

    /**
	 * @see ClientFacade#getServer()
	 */
    public Server getServer() {
        return null;
    }

    /**
	 * @see ClientFacade#setServer(Server)
	 */
    public void setServer(Server newServer) {
    }

    /**
	 * @see ClientFacade#gameStarted()
	 */
    public void gameStarted() {
    }

    /**
	 * @see ClientFacade#tearDown()
	 */
    public void tearDown() {
        System.exit(0);
    }
}
