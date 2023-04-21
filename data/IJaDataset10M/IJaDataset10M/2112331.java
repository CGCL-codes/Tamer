package forester.grass.datagrids;

import forester.grass.GrassPage;
import forester.image.DensityMap;
import java.util.HashMap;

/**
 * An object that provides density maps and color maps to 
 * a grassloader must implement this.
 * 
 * @author Andreas
 */
public interface MapProvider {

    public HashMap<Integer, DensityMap> getMaps(GrassPage tile);
}
