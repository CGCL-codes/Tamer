package emtigi.core.event;

import emtigi.core.Player;

/**
 * @author Thomas Kamps
 *
 */
public class DrawEvent extends EmtigiEvent {

    private Player player;

    private int number;

    public DrawEvent(Player player, int number) {
        this.player = player;
        this.number = number;
    }

    /**
	 * @return the player
	 */
    public Player getPlayer() {
        return player;
    }

    /**
	 * @return the number
	 */
    public int getNumber() {
        return number;
    }
}
