package javaaxp.swingviewer.service.impl.viewer.brushes;

import java.awt.PaintContext;
import java.awt.image.ColorModel;
import java.awt.image.Raster;

public class SolidColourAWTXPSPaintContext extends AWTXPSPaintContext {

    private PaintContext fColourPaintContext;

    public SolidColourAWTXPSPaintContext(ColorModel cm, PaintContext opacityPaintContext, PaintContext colourPaintContext) {
        super(cm, opacityPaintContext);
        fColourPaintContext = colourPaintContext;
    }

    @Override
    protected Raster getSourceRaster(int x, int y, int w, int h) {
        return fColourPaintContext.getRaster(x, y, w, h);
    }
}
