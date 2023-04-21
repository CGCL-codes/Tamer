package neon.graphics;

import neon.util.spatial.RTree;
import neon.util.spatial.SpatialIndex;

public class Layer {

    private SpatialIndex<Renderable> index;

    private int depth;

    private boolean visible = true;

    public Layer(int depth) {
        this(depth, new RTree<Renderable>(20, 9));
    }

    public Layer(int depth, SpatialIndex<? extends Renderable> index) {
        this.depth = depth;
        this.index = (SpatialIndex<Renderable>) index;
    }

    public SpatialIndex<Renderable> getElements() {
        return index;
    }

    public int getDepth() {
        return depth;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }
}
