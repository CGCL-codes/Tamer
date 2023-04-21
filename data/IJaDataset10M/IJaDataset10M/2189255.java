package forester.trees;

import com.jme3.math.Vector3f;
import forester.paging.GeometryPage;
import forester.paging.interfaces.PagingManager;
import forester.trees.datagrids.TreeDataBlock;

/**
 * This is the default tile-type for the treeloader.
 * 
 * @author Andreas
 */
public class TreePage extends GeometryPage {

    protected TreeDataBlock block;

    public TreePage(int x, int z, PagingManager engine) {
        super(x, z, engine);
    }

    @Override
    public TreeBlock createBlock(int x, int y, Vector3f center, PagingManager engine) {
        return new TreeBlock(x, y, center, engine);
    }

    @Override
    public void unload() {
        super.unload();
        block = null;
    }

    public TreeDataBlock getBlock() {
        return block;
    }

    public void setBlock(TreeDataBlock block) {
        this.block = block;
    }
}
