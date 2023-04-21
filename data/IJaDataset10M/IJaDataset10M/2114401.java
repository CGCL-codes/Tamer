package fr.inria.zuist.engine;

import java.io.IOException;
import java.net.URL;
import fr.inria.zvtm.engine.VirtualSpace;
import fr.inria.zvtm.glyphs.FitsImage;
import fr.inria.zvtm.glyphs.Glyph;

/**
 * Describes a FITS images and creates / releases the corresponding
 * ZVTM glyph on demand.
 */
public class FitsImageDescription extends ResourceDescription {

    private float scaleFactor = 1;

    private FitsImage.ScaleMethod scaleMethod;

    private FitsImage.ColorFilter colorFilter;

    transient FitsImage glyph;

    public FitsImageDescription(String id, long x, long y, int z, URL src, Region parentRegion, float scaleFactor, FitsImage.ScaleMethod scaleMethod, FitsImage.ColorFilter colorFilter) {
        this.id = id;
        this.vx = x;
        this.vy = y;
        this.zindex = z;
        this.src = src;
        this.scaleFactor = scaleFactor;
        this.scaleMethod = scaleMethod;
        this.colorFilter = colorFilter;
    }

    public String getType() {
        return FitsResourceHandler.RESOURCE_TYPE_FITS;
    }

    public void setColorFilter(FitsImage.ColorFilter colorFilter) {
        this.colorFilter = colorFilter;
        if (glyph != null) {
            glyph.setColorFilter(colorFilter);
        }
    }

    public void setScaleMethod(FitsImage.ScaleMethod scaleMethod) {
        this.scaleMethod = scaleMethod;
        if (glyph != null) {
            glyph.setScaleMethod(scaleMethod);
        }
    }

    public void rescale() {
    }

    public void createObject(final VirtualSpace vs, final boolean fadeIn) {
        try {
            glyph = new FitsImage(vx, vy, zindex, src, scaleFactor, true);
        } catch (IOException ioe) {
            throw new Error("Could not create FitsImage");
        }
        glyph.setScaleMethod(scaleMethod);
        glyph.setColorFilter(colorFilter);
        vs.addGlyph(glyph);
    }

    public void destroyObject(final VirtualSpace vs, boolean fadeOut) {
        vs.removeGlyph(glyph);
        glyph = null;
    }

    public Glyph getGlyph() {
        return glyph;
    }
}
