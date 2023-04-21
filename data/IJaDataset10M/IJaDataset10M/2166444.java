package com.android1.amarena2d.texture;

public class TiledTextureAtlas extends AbstractTextureAtlas {

    final int rows;

    final int cols;

    final int maxIndex;

    public TiledTextureAtlas(ManagedTexture managedTexture, int rows, int cols) {
        super(managedTexture);
        maxIndex = rows * cols;
        if (rows < 1 || cols < 1) throw new IllegalArgumentException("need at least one row and column");
        this.rows = rows;
        this.cols = cols;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                add(0, 0, 0, 0);
            }
        }
    }

    protected AtlasTextureFrame add(final int x, final int y, final int width, final int height) {
        AtlasTextureFrame frame = new AtlasTextureFrameImpl(this, nextIndex(), x, y, width, height);
        return add(frame);
    }

    @Override
    public void init() {
        if (!isInit()) {
            getManagedTexture().init();
            final int w = getManagedTexture().getWidth();
            final int h = getManagedTexture().getHeight();
            final int singleWidth = w / cols;
            final int singleHeight = h / rows;
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    AtlasTextureFrame frame = getTextureFrame(row, col);
                    frame.set(col * singleWidth, row * singleHeight, singleWidth, singleHeight);
                }
            }
            super.init();
        }
    }

    public AtlasTextureFrame getTextureFrame(int row, int col) {
        int tileIndex = col + (row * cols);
        return getTextureFrame(tileIndex);
    }
}
