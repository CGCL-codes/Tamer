package net.xelnaga.screplay.interfaces;

import java.util.Map;
import net.xelnaga.screplay.types.ObjectType;

/**
 * The <code>UnitCounter</code> interface is implemented by classes that
 * determine the number of units built during a replay.
 *
 * @author Russell Wilson
 */
public interface UnitCounter {

    /**
     * The counts for each type of unit.
     * 
     * @return the counts.
     */
    Map<ObjectType, Integer> getUnitCount();

    /**
     * The count for each type of unit for a player.
     *
     * @param player the player.
     * @return the counts.
     */
    Map<ObjectType, Integer> getUnitCountForPlayer(Player player);
}
