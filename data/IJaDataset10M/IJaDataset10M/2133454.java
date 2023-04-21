package org.timothyb89.jtelirc.ircrpg.map.zone;

import org.timothyb89.jtelirc.ircrpg.Player;
import org.timothyb89.jtelirc.ircrpg.map.zone.dwelling.Inn;
import org.timothyb89.jtelirc.ircrpg.util.DirectionUtil;

/**
 *
 * @author tim
 */
public abstract class City extends Zone {

    public City() {
    }

    /**
	 * Gets the city's Inn.
	 * @return The Inn for this city, or null if there is none.
	 */
    public abstract Inn getInn();

    @Override
    public String getDescription(Player player) {
        String inn = "";
        if (getInn() != null) {
            inn = "You see the city inn, " + getInn().getName() + " to the " + DirectionUtil.getDirection(player.getLocation(), getInn().getBounds().getLocation()) + ".";
        }
        return "This is " + getName() + ".";
    }
}
