package wood.model.map;

import lawu.math.Vector;

/**
 * This defines a factory method that returns a new Map
 * 
 * @author Nikolas Wolfe
 */
public interface MapFactory {

    public Map generateMap();

    public Vector getDefaultStartingPoint();

    public String getMapImageFilename();

    public String getMapDisplayName();

    public String getMapFile();
}
