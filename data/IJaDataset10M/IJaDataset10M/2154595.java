package ai.humanAI.basicHumanAI;

import graphics.GLCamera;
import sgEngine.SGEngine;
import utilities.Location;
import world.World;
import world.owner.Owner;
import world.unit.Unit;

public class TestHumanAI extends BasicHumanAI3 {

    public TestHumanAI(Owner o, World w, SGEngine sge, GLCamera c) {
        super(o, w, sge, c);
    }

    protected void orderUnit(Unit u, Location l) {
        if (u.isSelected()) {
            if (l != null) {
                Location location = new Location(l.x, l.y + u.getRestingHeight(), l.z);
                forceMoveUnit(u, location);
            }
        }
    }

    private void buildRandomly(Unit u) {
        Location l = getRandomMapLocation();
        this.buildAt("factory", u, l);
        this.buildAt("worker", u, u.getLocation());
        moveUnit(u, l);
    }

    /**
	 * gets a random location within the map bounds, the height does not matter
	 * because the AI class automatically sends units to the proper resting height
	 * @return
	 */
    private Location getRandomMapLocation() {
        return new Location(Math.random() * w.getWidth() - w.getWidth() / 2, 0, Math.random() * w.getDepth() - w.getDepth() / 2);
    }
}
