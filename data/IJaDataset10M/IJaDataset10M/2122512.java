package org.pokenet.server.battle.mechanics.statuses.items;

import org.pokenet.server.battle.Pokemon;

/**
 * An interface representing a berry item.
 * @author ben
 */
public interface Berry {

    /**
     * Execute the effects of the berry
     */
    public void executeEffects(Pokemon p);
}
