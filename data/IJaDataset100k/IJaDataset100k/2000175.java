package org.ihash.processing;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;
import java.awt.Rectangle;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Extracts areas of skin from the given image.
 * 
 * @author Gergely Kiss
 */
public class SkinFeatureExtractor implements FeatureExtractor {

    @Override
    public Collection<Feature> extract(Raster image, Rectangle rect) {
        rect = rect == null ? image.getBounds() : rect;
        OverlayFeature debug = new OverlayFeature(rect);
        WritableRaster overlay = debug.getOverlay().getRaster();
        Collection<Feature> result = new ArrayList<Feature>(1);
        result.add(debug);
        int[] color = { 0, 0, 0 };
        for (int y = 0; y < rect.height; y++) {
            for (int x = 0; x < rect.width; x++) {
                image.getPixel(x, y, color);
                if (isSkin(color[0], color[1], color[2])) {
                    overlay.setPixel(x, y, new int[] { 0, 0, 255 });
                }
            }
        }
        return result;
    }

    private static final boolean isSkin(int r, int g, int b) {
        return r > 95 && g > 40 && b > 20 && r > g && r > b && abs(r - g) > 15 && (max(r, max(g, b)) - min(r, min(g, b))) > 15;
    }
}
