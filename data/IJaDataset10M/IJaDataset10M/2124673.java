package server.universe.item;

import message.ClientMessage;
import server.universe.Creature;

/**
 * Fancy treats increase a player's maximum health by 30 points.
 *
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class FancyTreat extends Item {

    private static final long serialVersionUID = 2L;

    private int increaseAmount;

    /**
	 * Construct a potion with default characteristics.
	 */
    public FancyTreat() {
        setName("FancyTreat");
        setDescription("Use this to increase your maximum health by 30 points.");
        setPrice(30);
        setIncreaseAmount(30);
    }

    /**
	 * @return the amount a fancy treat increases a creature's health
	 */
    public int getIncreaseAmount() {
        return increaseAmount;
    }

    /**
	 * Sets the value that a creatures maximum health will be affected by.
	 * 
	 * @param increaseAmount
	 *            the amount to increase maximum health
	 */
    public void setIncreaseAmount(int increaseAmount) {
        this.increaseAmount = increaseAmount;
    }

    /**
	 * Heal player, remove potion from inventory, and possibly notify user.
	 */
    public ClientMessage use(Creature creature) {
        creature.setMaxHealth(creature.getMaxHealth() + this.getIncreaseAmount());
        creature.removeItem(this);
        return new ClientMessage("You used " + this.getName() + " to increase your maximum health by " + this.getIncreaseAmount() + " points");
    }
}
