package ai.computerAI.computerEasy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import world.resource.ResourceDeposit;
import world.unit.Unit;
import world.unit.units.Harvester;

/**
 * managest the expansion sites for the ai
 * @author Secondary
 *
 */
public class ExpansionSiteManager {

    HashSet<ExpansionSite> e = new HashSet<ExpansionSite>();

    HashMap<Unit, ExpansionSite> m = new HashMap<Unit, ExpansionSite>();

    HashSet<ResourceDeposit> used = new HashSet<ResourceDeposit>();

    /**
	 * associates a unit with the passed expansion site, associated units
	 * are then said to be managed by the site they are associated with, 
	 * units can only be associated with no more than one site
	 * @param u
	 * @param e
	 */
    public void associateUnit(Unit u, ExpansionSite e) {
        if (this.e.contains(e)) {
            m.put(u, e);
            e.associateUnit(u);
        }
    }

    public void disassociateUnit(Unit u) {
        m.get(u).disassociateUnit(u);
        m.remove(u);
    }

    public HashSet<ExpansionSite> getExpansionSites() {
        return e;
    }

    /**
	 * associates the passed unit with the expansion site that contains it
	 * @param u
	 */
    public void associateUnit(Unit u) {
        for (ExpansionSite expo : e) {
            if (expo.contains(u.getLocation()[0], u.getLocation()[1])) {
                associateUnit(u, expo);
                break;
            }
        }
    }

    /**
	 * checks to see if the passed unit is associated with one of the
	 * expansion sites, associated units are considered to be managed by the sites
	 * @param u
	 * @return returns the expansions site the passed unit is associated with
	 * and thusly managed at, null is the unit has not been associated with a
	 * specific site
	 */
    public ExpansionSite isManaged(Unit u) {
        return m.get(u);
    }

    /**
	 * gives orders to the passed unit based off which expansion site it
	 * is associated with
	 * @param u
	 */
    public void manageUnit(Unit u) {
        if (m.containsKey(u)) {
            m.get(u).manageUnit(u);
        }
    }

    public void addExpansionSite(ExpansionSite e) {
        this.e.add(e);
        used.add(e.getDeposit());
    }

    /**
	 * returns a set containing resource deposits where expansion sites
	 * where already delcared
	 * @return
	 */
    public HashSet<ResourceDeposit> getUsedDeposits() {
        return used;
    }

    /**
	 * logs the unit death
	 * @param u
	 */
    public void logUnitDestroyed(Unit u) {
        for (ExpansionSite exp : e) {
            if (exp.contains(u.getLocation()[0], u.getLocation()[1])) {
                exp.logDeath(u);
            }
        }
    }

    /**
	 * updates the expansion sites, ordering them to build various units
	 */
    public void manageSites() {
        for (ExpansionSite e : this.e) {
            e.updateSite(this);
        }
    }
}
