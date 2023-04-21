package de.robowars.ui;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import de.robowars.comm.transport.CardSet;
import de.robowars.comm.transport.PositionRequest;
import de.robowars.comm.transport.PositionSelection;
import de.robowars.comm.transport.RoundProtocol;
import de.robowars.comm.transport.StartSituation;
import de.robowars.ui.event.EventController;
import de.robowars.ui.event.EventFromServerListener;

/**
 * class to display status messages on the screen during the game
 * 
 * @author stefan.henze
 */
public class StatusMessages extends JLabel implements EventFromServerListener {

    private static StatusMessages _instance = null;

    private JLayeredPane mainPane;

    private StatusMessages() {
        mainPane = GameDialog.getInstance().getMainPane();
        setVerticalAlignment(JLabel.CENTER);
        setHorizontalAlignment(JLabel.LEFT);
        setOpaque(true);
        setBackground(Color.yellow);
        setForeground(Color.black);
        setBorder(BorderFactory.createLineBorder(Color.black));
        setBounds(20, 770, 984, 60);
        EventController.getInstance().addCarryOutListener(this);
        EventController.getInstance().addIssueCardsListener(this);
        EventController.getInstance().addKickedListener(this);
        EventController.getInstance().addNotifyPositionListener(this);
        EventController.getInstance().addNotifyPositionRequestListener(this);
        EventController.getInstance().addRequestPositionListener(this);
    }

    /**
	 * Method getInstance.
	 * @return StatusMessages
	 */
    public static StatusMessages getInstance() {
        if (_instance == null) {
            _instance = new StatusMessages();
        }
        return _instance;
    }

    /**
	 * Method showStatus.
	 */
    public void showStatus() {
        mainPane.add(this, new Integer(1000));
    }

    /**
	 * Method hideStatus.
	 */
    public void hideStatus() {
        mainPane.remove(this);
    }

    /**
	 * Method setMessage.
	 * @param message to be shown
	 */
    public void setMessage(String m) {
        this.setText(m);
    }

    /**
	 * Method clear to clear the message text
	 */
    public void clear() {
        setMessage("");
    }

    /**
	 * @see de.robowars.ui.event.EventFromServerListener#carryOut(RoundProtocol)
	 */
    public void carryOut(RoundProtocol p) {
        setMessage("Round is going to be carried out.");
    }

    /**
	 * @see de.robowars.ui.event.EventFromServerListener#issueCards(CardSet)
	 */
    public void issueCards(CardSet cards) {
        setMessage("issue Cards");
    }

    /**
	 * @see de.robowars.ui.event.EventFromServerListener#kicked()
	 */
    public void kicked() {
        setMessage("You were KICKED!!!");
    }

    /**
	 * @see de.robowars.ui.event.EventFromServerListener#notifyPosition(PositionSelection)
	 */
    public void notifyPosition(PositionSelection ps) {
        setMessage("Please has selected a new Position on the Board.");
    }

    /**
	 * @see de.robowars.ui.event.EventFromServerListener#notifyPositionRequest(int)
	 */
    public void notifyPositionRequest(int playerID) {
        int pid = playerID + 1;
        setMessage("Player " + pid + " currently chooses a new Position on the Board.");
    }

    /**
	 * @see de.robowars.ui.event.EventFromServerListener#startGame(StartSituation)
	 */
    public void startGame(StartSituation s) {
        setMessage("starting Game");
    }

    /**
	 * @see de.robowars.ui.event.EventFromServerListener#requestPosition(PositionRequest)
	 */
    public void requestPosition(PositionRequest r) {
        setMessage("Pleas select new Position on the Board");
    }
}
