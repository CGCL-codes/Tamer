package emtigi.core.event;

import emtigi.core.*;

/**
 * A Event that gets fired, when a Player got mana
 * @author Thomas Kamps
 */
public class GotManaEvent extends EmtigiEvent {

    private Player player;

    private ManaColor color;

    private int n;

    public GotManaEvent(Player player, ManaColor color, int n) {
        this.player = player;
        this.color = color;
        this.n = n;
    }

    /**
	 * @return The player that got the mana
	 */
    public Player getPlayer() {
        return player;
    }

    /**
	 * @return The color of that mana
	 */
    public ManaColor getColor() {
        return color;
    }

    /**
	 * @return the Number of the mana
	 */
    public int getNum() {
        return n;
    }
}
