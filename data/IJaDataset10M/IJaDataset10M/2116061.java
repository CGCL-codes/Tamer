package server.universe.item;

import message.ClientMessage;
import server.universe.Creature;

/**
 * Potion class outlines everything a potion does.
 * 
 * All potions are healing potions.
 */
public class SmallPotion extends Item {

    private static final long serialVersionUID = 2L;

    private int healingPower;

    /**
	 * Construct a potion with default characteristics.
	 */
    public SmallPotion() {
        setName("small potion");
        setDescription("Use potions to recover health points");
        setPrice(10);
        setHealingPower(10);
    }

    /**
	 * Get the healing power of a potion.
	 */
    public int getHealingPower() {
        return this.healingPower;
    }

    /**
	 * Set the healing power of a potion.
	 */
    public void setHealingPower(int power) {
        this.healingPower = power;
    }

    /**
	 * Heal player, remove potion from inventory, and possibly notify user.
	 */
    public ClientMessage use(Creature creature) {
        creature.increaseHealth(this.healingPower);
        creature.removeItem(this);
        return new ClientMessage("You use " + this.getName() + " and heal " + this.healingPower + " points");
    }
}
