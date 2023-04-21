package scamsoft.squadleader.setup;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import scamsoft.squadleader.rules.Player;
import scamsoft.squadleader.rules.UnitType;

/**
 *  A Battalion represents a group of units who setup in the same area.
 *  Batallions are used during setup.
 *
 *@author     Andreas Mross
 *created    February 10, 2001
 */
public class Battalion implements Serializable {

    private int id;

    private Player player;

    private boolean hidden;

    private boolean concealed;

    private Map entries;

    static final long serialVersionUID = 5864599680158723635L;

    /**
	 * Create a new Battalion.
	 * @param id A unique number greater then 0.
	 * @param player The player controlling this Battalion.
	 * @param period The date of the scenario.
	 */
    public Battalion(int id, Player player, ScenarioDate period) {
        if (id < 0) throw new IllegalArgumentException("ID=" + id);
        if (player == null) throw new NullPointerException("Player");
        if (period == null) throw new NullPointerException("ScenarioDate");
        this.id = id;
        this.player = player;
        this.entries = Collections.synchronizedMap(new HashMap());
    }

    /**
	 *  Returns the Player who receives this battalion.
	 *
	 *@return    scamsoft.squadleader.rules.Player
	 */
    public Player getPlayer() {
        return player;
    }

    public int getID() {
        return id;
    }

    /**
	 *  Tests if the Battalion starts Concealed
	 *
	 *@return    boolean
	 */
    public boolean isConcealed() {
        return concealed;
    }

    /**
	 *  Tests if the Battalion starts the game Hidden
	 *
	 *@return    boolean
	 */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * Get the entries in this battalion. Each entry is a Unit type with a number of units.
     * @return
     */
    public Map getEntries() {
        return entries;
    }

    public void addEntry(UnitType type, int amount) {
        Integer previous = (Integer) entries.get(type);
        if (previous != null) {
            amount += previous.intValue();
        }
        entries.put(type, new Integer(amount));
    }

    protected void setConcealed(boolean concealed) {
        this.concealed = concealed;
    }

    protected void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    /**
	 * Two battalions with the same id are equal.
	 * @param o
	 * @return
	 */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Battalion)) return false;
        final Battalion battalion = (Battalion) o;
        if (id != battalion.id) return false;
        return true;
    }

    public int hashCode() {
        return id;
    }
}
