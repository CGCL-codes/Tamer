package org.pandcorps.game.core;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import org.pandcorps.core.Imtil;
import org.pandcorps.core.img.Pancolor;
import org.pandcorps.core.img.ReplacePixelFilter;

public final class ImtilX {

    public static final int DIM = 16;

    public static Pancolor outlineSrc = null;

    public static Pancolor outlineDst = null;

    private ImtilX() {
        throw new Error();
    }

    public static final BufferedImage loadImage(final String path) {
        return loadImage(path, null);
    }

    public static final BufferedImage loadImage(final String path, final ReplacePixelFilter filter) {
        return loadImage(path, DIM, filter);
    }

    public static final BufferedImage loadImage(final String path, final int dim, ReplacePixelFilter filter) {
        BufferedImage img = Imtil.load(path);
        final int h = img.getHeight();
        if (h == dim + 1) {
            img = img.getSubimage(0, 0, img.getWidth(), dim);
        } else if (h != dim) {
            throw new UnsupportedOperationException("Expected image to have height=" + dim);
        }
        final ColorModel cm = img.getColorModel();
        boolean transparency = false;
        for (int x = 0; x < dim; x++) {
            for (int y = 0; y < dim; y++) {
                final int rgb = img.getRGB(x, y);
                if (cm.getAlpha(rgb) == 0) {
                    transparency = true;
                    break;
                }
            }
        }
        if (!transparency) {
            filter = ReplacePixelFilter.putToTransparent(filter, img.getRGB(0, 0));
        }
        filter = ReplacePixelFilter.putIfValued(filter, outlineSrc, outlineDst);
        return Imtil.filter(img, filter);
    }

    public static final BufferedImage[] loadStrip(final String path) {
        return Imtil.toStrip(loadImage(path), DIM);
    }
}
