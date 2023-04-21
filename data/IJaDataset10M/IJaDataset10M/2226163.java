package name.emu.games.q3mapgen.mapobjects;

import name.emu.games.q3mapgen.id.Box;
import name.emu.games.q3mapgen.id.Map;

/**
 * @author emu
 *
 * One floor of a building.
 */
public class Floor extends AbstractMapObject {

    /**
	 * Constructor.
	 * @param parent containing parent object.
	 */
    public Floor(MapObjectContainer parent) {
        super(parent);
        sizeX = 100;
        sizeY = 100;
        sizeZ = 30;
    }

    /**
	 * Generates concrete map data from this floor.
	 */
    public void generate(Map map, double x, double y, double z) {
        map.getWorldSpawn().add(new Box(x, y, z, x + sizeX, y + sizeY, z + sizeZ, "testtexture"));
    }
}
