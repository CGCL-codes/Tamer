package world.unit.units;

import java.util.ArrayList;
import world.unit.BuildTree;
import world.unit.Unit;
import utilities.Location;
import world.owner.Owner;

public class Engineer extends Unit {

    public Engineer(Owner owner, Location location) {
        super(owner, location, "engineer", 7, 80, 80, 3, 10, 20);
        ArrayList<String> u = new ArrayList<String>();
        u.add("factory");
        u.add("defense turret");
        BuildTree tree = new BuildTree(u);
        bt = tree;
        gatheringRange = 15;
        isBuilder = true;
    }
}
