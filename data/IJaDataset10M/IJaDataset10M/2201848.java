package factory;

import world.owner.Owner;
import utilities.Location;
import world.unit.Unit;
import world.unit.building.buildings.*;
import world.unit.units.*;

public final class UnitFactory {

    public static Unit makeUnit(Owner owner, String name, Location l) {
        if (name.equalsIgnoreCase("worker")) {
            return new Worker(owner, l);
        } else if (name.equalsIgnoreCase("fighter")) {
            return new Fighter(owner, l);
        } else if (name.equalsIgnoreCase("hq")) {
            return new HQ(owner, l);
        } else if (name.equalsIgnoreCase("barracks")) {
            return new Barracks(owner, l);
        } else if (name.equalsIgnoreCase("defense turret")) {
            return new DefenseTurret(owner, l);
        } else if (name.equalsIgnoreCase("engineer")) {
            return new Engineer(owner, l);
        } else if (name.equalsIgnoreCase("factory")) {
            return new Factory(owner, l);
        } else if (name.equalsIgnoreCase("harvester")) {
            return new Harvester(owner, l);
        }
        return null;
    }
}
