package world.action.actions;

import world.WorldConstants;
import world.WorldOverlay;
import world.action.*;
import world.unit.FriendlyUnitMask;
import world.resource.Resource;
import world.World;

/**
 * defines the actual taking of the resource by
 * the unit before being brought back to a stockpile
 * @author Jack
 *
 */
public final class LoadResource extends Action {

    WorldOverlay wo;

    World w;

    FriendlyUnitMask fum;

    public LoadResource(FriendlyUnitMask fum, WorldOverlay geo) {
        super("load resource");
        this.fum = fum;
        this.wo = geo;
    }

    /**
	 * attempts to load the closest visible resource<br /><br />
	 * 
	 * 1. gets closest visible resource<br />
	 * 2. checks if distance is less than max gather range (a world constant)<br />
	 * if yes: units resource set, resource removed from world, true returned<br />
	 * if no: resource out of range (or already picked up by other unit), action set to idle
	 */
    public boolean performAction() {
        Resource r = wo.getClosestVisibleResource(this, fum);
        if (r != null && r.getLocation().distanceTo(fum.getLocation()) <= WorldConstants.gatherRange) {
            fum.getUnit(this).setMassHolding(fum.getUnit(this).getMassHolding() + r.getHarvestMass());
            r.setDead();
            return true;
        }
        fum.setAction(this, new Idle());
        return true;
    }
}
