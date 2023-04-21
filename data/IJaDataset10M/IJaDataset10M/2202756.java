package meadow.server;

import meadow.common.SimulationUpdate;
import meadow.common.TerrainModel;
import meadow.common.Group;
import meadow.common.units.*;
import java.util.Iterator;

/** This class could do with being more generic, e.g. allowing user to specify
where crates may appear, and what they may contain.*/
public class CrateHandler implements SimulationModifier {

    private double crateProbability;

    public CrateHandler(int crateFrequency) {
        this.crateProbability = crateFrequency / 1000.0;
    }

    /** @return A new crate to add to the game, or null.*/
    protected Crate getCrateUpdate(MasterSimulation s) {
        final int crateDiameter = 16;
        if (Math.random() < crateProbability) {
            int x = (int) (Math.random() * s.getTerrainModel().getWidth());
            x = Math.max(crateDiameter, x);
            x = Math.min(x, s.getTerrainModel().getWidth() - crateDiameter);
            int y = (int) (Math.random() * s.getTerrainModel().getHeight());
            y = Math.max(crateDiameter, y);
            y = Math.min(y, s.getTerrainModel().getHeight() - crateDiameter);
            Crate newCrate = new Crate(x, y, s.getTerrainModel().elevationAt(0, 0), crateDiameter / 2, (int) (Math.random() * UnitFactory.NUM_TYPES), 10, s.getTerrainModel());
            return newCrate;
        } else {
            return null;
        }
    }

    /** @returns The unit that has just found the crate, or null if none.*/
    protected Unit crateFinder(MasterSimulation s, Crate crate) {
        Iterator i = s.getAllPieces().iterator();
        while (i.hasNext()) {
            GamePiece piece = (GamePiece) i.next();
            if (piece instanceof Unit) {
                Unit u = (Unit) piece;
                if (crate.getBounds().intersects(u.getBounds())) {
                    return u;
                }
            }
        }
        return null;
    }

    /** Kill off all crates that have been found (by calling 
	<code>GamePiece.die()</code> on them) and return a list of pieces that
	have emerged.
	@return A Group of new GamePieces.
	Some crates do not produce units, because:
	1. Gameplay... to add some variation, some crates are just "empty"
	2. Cheapness... it might be hard to find a place to put the new unit, so if
	it is too hard we just shan't bother making one.
	*/
    protected Group doCrateFinding(MasterSimulation s, Group crates) {
        Group result = new Group();
        for (int i = 0; i < crates.size(); i++) {
            Crate crate = (Crate) crates.get(i);
            Unit u = crateFinder(s, crate);
            if (u != null) {
                crate.die();
                int x = u.getX() + (int) ((Math.random() - 0.5f) * 80);
                int y = u.getY() + (int) ((Math.random() - 0.5f) * 80);
                int footing = s.getTerrainModel().at(x, y);
                if ((footing == TerrainModel.GRASS || footing == TerrainModel.MARSH) && !s.getAllPieces().isSolidGamePieceAt(x, y, null)) {
                    result.add(UnitFactory.createFromType(crate.getContentCode(), x, y, u.getOwner(), s.getTerrainModel()));
                }
            }
        }
        return result;
    }

    /** ISSUE: Found crates are not removed here. They will be handled by standard
	update this time (if we're called first) or next time otherwise.*/
    public SimulationUpdate getUpdate(MasterSimulation s) {
        SimulationUpdate result = new SimulationUpdate();
        Group crates = new Group(s.getAllPieces());
        crates.retainBy(new meadow.common.filters.ClassFilter("meadow.common.units.Crate"));
        result.setNewborn(doCrateFinding(s, crates));
        Crate newCrate = getCrateUpdate(s);
        if (newCrate != null) {
            result.getNewborn().add(newCrate);
        }
        return result;
    }
}
