package factory;

import utilities.Location;
import world.terrain.*;

/**
 * creates terrain
 * @author Jack
 *
 */
public final class TerrainFactory {

    /**
	 * creates terrain
	 * @param name name of the terrain to be created
	 * @param l location (from the center) of the terrain to be created
	 * @param width width of the terrain to be created
	 * @param height height of the terrain to be created
	 * @return Terrain
	 */
    public static Terrain makeTerrain(String name, Location l, int width, int height) {
        if (name.equalsIgnoreCase("hard rock")) {
            return new HardRock(l, width, height);
        } else if (name.equalsIgnoreCase("water")) {
            return new Water(l, width, height);
        }
        return null;
    }
}
