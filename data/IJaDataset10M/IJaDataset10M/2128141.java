package com.ao.model.worldobject;

import com.ao.model.character.Character;
import com.ao.model.worldobject.properties.TemporalStatModifyingItemProperties;

/**
 * A potion to increase strength.
 */
public class StrengthPotion extends ConsumableItem {

    /**
	 * Creates a new StrengthPotion instance.
	 * @param properties The item's properties.
	 * @param amount The item's amount.
	 */
    public StrengthPotion(TemporalStatModifyingItemProperties properties, int amount) {
        super(properties, amount);
    }

    @Override
    public Item clone() {
        return new StrengthPotion((TemporalStatModifyingItemProperties) properties, amount);
    }

    @Override
    public void use(Character character) {
        super.use(character);
        int minModifier = ((TemporalStatModifyingItemProperties) properties).getMinModifier();
        int maxModifier = ((TemporalStatModifyingItemProperties) properties).getMaxModifier();
        int time = ((TemporalStatModifyingItemProperties) properties).getEffectDuration();
        character.addToStrength((int) (Math.random() * (maxModifier - minModifier + 1)) + minModifier, time);
    }

    /**
	 * Retrieves the minimum modifier for this potion.
	 * @return The minimum modifier for this potion.
	 */
    public int getMinModifier() {
        return ((TemporalStatModifyingItemProperties) properties).getMinModifier();
    }

    /**
	 * Retrieves the maximim modifier for this potion.
	 * @return The maximim modifier for this potion.
	 */
    public int getMaxModifier() {
        return ((TemporalStatModifyingItemProperties) properties).getMaxModifier();
    }

    /**
	 * Retrieves the effect's duration.
	 * @return The effect's duration
	 */
    public int getEffectDuration() {
        return ((TemporalStatModifyingItemProperties) properties).getEffectDuration();
    }
}
