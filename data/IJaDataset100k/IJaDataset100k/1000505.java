package edu.columbia.hypercontent.util.codec;

import java.awt.image.Raster;
import java.awt.image.ColorModel;
import java.awt.image.SampleModel;

/**
 * A simple class that provides RenderedImage functionality
 * given a Raster and a ColorModel.
 */
public class SingleTileRenderedImage extends SimpleRenderedImage {

    Raster ras;

    /**
     * Constructs a SingleTileRenderedImage based on a Raster
     * and a ColorModel.
     *
     * @param ras A Raster that will define tile (0, 0) of the image.
     * @param cm A ColorModel that will serve as the image's
     *           ColorModel.
     */
    public SingleTileRenderedImage(Raster ras, ColorModel colorModel) {
        this.ras = ras;
        this.tileGridXOffset = this.minX = ras.getMinX();
        this.tileGridYOffset = this.minY = ras.getMinY();
        this.tileWidth = this.width = ras.getWidth();
        this.tileHeight = this.height = ras.getHeight();
        this.sampleModel = ras.getSampleModel();
        this.colorModel = colorModel;
    }

    /**
     * Returns the image's Raster as tile (0, 0).
     */
    public Raster getTile(int tileX, int tileY) {
        if (tileX != 0 || tileY != 0) {
            throw new IllegalArgumentException(JaiI18N.getString("SingleTileRenderedImage0"));
        }
        return ras;
    }
}
