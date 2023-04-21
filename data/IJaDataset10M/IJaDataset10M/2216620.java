package name.emu.games.q3mapgen.mapobjects;

import name.emu.games.q3mapgen.id.Map;

/**
 * @author emu
 *
 * A building.
 */
public class Building extends AbstractMapObjectContainer {

    /**
	 * Constructor.
	 * @param parent  containing map object.
	 */
    public Building(MapObjectContainer parent) {
        super(parent);
    }

    /**
	 * Generates concrete map data from this object.
	 */
    public void generate(Map map, double x, double y, double z) {
        for (int i = 0; i < getNumMapObjects(); i++) {
            MapObject mo = this.get(i);
            mo.generate(map, x, y, z);
            z = z + mo.getSizeZ();
        }
    }
}
