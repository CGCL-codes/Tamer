package de.hattrickorganizer.gui.model;

import de.hattrickorganizer.gui.templates.ColorLabelEntry;
import de.hattrickorganizer.gui.templates.TableEntry;
import de.hattrickorganizer.model.Spieler;

/**
 * Column shows skill of a player
 * @author Thorsten Dietz
 * @since V1.36
 *
 */
class PlayerColumn extends UserColumn {

    /**
	 * constructor
	 * @param id
	 * @param name
	 * @param tooltip
	 */
    protected PlayerColumn(int id, String name, String tooltip) {
        super(id, name, tooltip);
    }

    /**
	 * constructor
	 * @param id
	 * @param name
	 * @param minWidth
	 */
    protected PlayerColumn(int id, String name, int minWidth) {
        this(id, name, name, minWidth);
    }

    /**
	 * constructor
	 * @param id
	 * @param name
	 * @param tooltip
	 * @param minWidth
	 */
    protected PlayerColumn(int id, String name, String tooltip, int minWidth) {
        super(id, name, tooltip);
        this.minWidth = minWidth;
        preferredWidth = minWidth;
    }

    /**
	 * returns a TableEntry
	 * overwritten by all created columns
	 * @param player
	 * @param comparePlayer
	 * @return
	 */
    public TableEntry getTableEntry(Spieler player, Spieler comparePlayer) {
        return new ColorLabelEntry(getValue(player), ColorLabelEntry.BG_STANDARD, false, 0);
    }

    /**
	 * return the individual playerValue
	 * overwritten by created columns
	 * @param player
	 * @return
	 */
    public int getValue(Spieler player) {
        return player.getSpielerID();
    }
}
