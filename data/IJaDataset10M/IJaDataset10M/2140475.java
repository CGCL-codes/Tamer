package net.xelnaga.screplay.interfaces;

import net.xelnaga.screplay.types.ColourType;
import net.xelnaga.screplay.types.PlayerType;
import net.xelnaga.screplay.types.RaceType;
import net.xelnaga.screplay.types.SlotType;

/**
 * The <code>Player</code> interface is implemented by objects representing
 * a player.
 *
 * @author Russell Wilson
 *
 */
public interface Player {

    /**
     * The colour.
     * @return the colour.
     */
    ColourType getColour();

    /**
     * The force identifier.
     * @return the force identifer.
     */
    byte getForceIdentifier();

    /**
     * The player identifier.
     * @return the player identifier.
     */
    int getIdentifier();

    /**
     * The player name.
     * @return the player name.
     */
    String getName();

    /**
     * The race.
     * @return the race.
     */
    RaceType getRaceType();

    /**
     * The slot.
     * @return the slot.
     */
    SlotType getSlotType();

    /**
     * The spot.
     * @return the spot.
     */
    byte getSpot();

    /**
     * The player type.
     * @return the player type.
     */
    PlayerType getPlayerType();
}
