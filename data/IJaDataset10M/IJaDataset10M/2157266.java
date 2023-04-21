package de.jrpgcore.rpg.characterClasses;

import de.jrpgcore.rpg.foundation.Item;
import de.jrpgcore.rpg.foundation.Wearable;

/**
 * This is the base class for all NPCs and PCs.
 * 
 * @author wolfenlord
 * @since Oct 16, 2010 2:06:08 AM
 */
public class Character extends Creature {

    {
        this.ini = 10;
        this.lifeEnergy = 50;
        this.armourClass = 3;
        this.numberOfAttacks = 1;
    }

    Equipment equipment = new Equipment();

    Luggage luggage = new Luggage();

    public void wear(Wearable w) {
        if (!equipment.mount(w)) {
            throw new IllegalStateException();
        }
    }

    public void take(Item item) {
        luggage.add(item);
    }
}
