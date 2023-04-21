package forester.grass;

import com.jme3.math.Vector3f;
import forester.paging.GeometryPage;
import forester.paging.interfaces.PagingManager;

/**
 * Tile type used for grass.
 * 
 * @author Andreas
 */
public class GrassPage extends GeometryPage {

    public GrassPage(int x, int z, PagingManager manager) {
        super(x, z, manager);
    }

    @Override
    public GrassBlock createBlock(int x, int y, Vector3f center, PagingManager manager) {
        return new GrassBlock(x, y, center, manager);
    }
}
