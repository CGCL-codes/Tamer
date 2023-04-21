package de.outstare.fortbattleplayer.model;

import java.util.Set;

/**
 * A Sector groups multiple {@link Area}s together and can be owned by a
 * {@link CombatantSide}.
 * 
 * @author daniel
 */
public interface Sector {

    /**
	 * @return whether a {@link Combatant} of a {@link CombatantSide} controls
	 *         this {@link Sector}.
	 */
    boolean isOccupied();

    /**
	 * @return who controls this sector? null if nobody
	 */
    CombatantSide getOccupier();

    /**
	 * @param side
	 *            which occupies this sector
	 */
    void gainControl(CombatantSide side);

    /**
	 * Frees this sector. Stating nobody controls it (anymore).
	 */
    void free();

    /**
	 * @return the height of this sector
	 */
    int getHeight();

    /**
	 * @param charClass
	 *            not <code>null</code>
	 * @return the bonus for the given class in this sector
	 */
    SectorBonus getBonus(CharacterClass charClass);

    /**
	 * @return the {@link Area}s contained in this sector
	 */
    Set<Area> getAreas();

    /**
	 * Adds a new Area to this sector.
	 * 
	 * @param area
	 */
    void _addArea(Area area);
}
