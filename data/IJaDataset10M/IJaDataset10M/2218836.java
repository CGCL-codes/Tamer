package scotlandyard.engine.impl;

import scotlandyard.engine.spec.IPlayer;
import scotlandyard.engine.spec.IUser;

/**
 * player object extends User class and implements IPlayer,
 * once user join a game, the player object will be generated under the game
 * @author simon
 * @version 3.0
 */
public class Player extends User implements IPlayer {

    public boolean mrx;

    private int[] tickets = new int[5];

    public int position;

    /**
	 * constructor of player object
	 * @param user
	 * @param mrx
	 */
    public Player(IUser user, boolean mrx) {
        super(user.getIcon(), user.getHash(), user.getSID(), user.getName(), user.getEmail());
        this.mrx = mrx;
        super.updateLastActivity();
    }

    /**
	 * get current position of the player on the map,
	 * which node is the player on
	 * @return integer of node of current position of the player
	 */
    @Override
    public int getPosition() {
        return this.position;
    }

    /**
	 * gets the number of specify transport tokens
	 * @param transport
	 * @return numbers of tokens
	 */
    @Override
    public int getTickets(int transport) {
        return this.tickets[transport];
    }

    /**
	 * checks whether player is MrX
	 * @return true if player is MrX, false otherwise
	 */
    @Override
    public boolean isMrx() {
        return mrx;
    }

    /**
	 * sets the number of tickets that the player use for the move,
	 * reduce 1 token for the transport
	 * @param transport
	 */
    @Override
    public void consumeTicket(int transport) {
        this.tickets[transport]--;
    }

    /**
	 * sets new position of player
	 * @param newPosition - position that transport can move to
	 */
    @Override
    public void setPosition(int newPosition) {
        this.position = newPosition;
    }

    /**
	 * sets the number of tokens a player uses the transport type,
	 * that move from the current player position to a legal node
	 * @param transport
	 * @param value
	 */
    @Override
    public void setTickets(int transport, int value) {
        this.tickets[transport] = value;
    }
}
