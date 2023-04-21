package com.replica.replicaisland;

public class TiledBackgroundVertexGrid extends ScrollableBitmap {

    private TiledVertexGrid mGrid;

    public TiledBackgroundVertexGrid() {
        super(null, 0, 0);
    }

    @Override
    public void reset() {
        super.reset();
        mGrid = null;
    }

    public void setGrid(TiledVertexGrid grid) {
        mGrid = grid;
    }

    @Override
    public void draw(float x, float y, float scaleX, float scaleY) {
        if (mGrid != null) {
            mGrid.draw(x, y, getScrollOriginX(), getScrollOriginY());
        }
    }
}
