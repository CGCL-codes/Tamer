package net.sealisland.swing.util;

import java.awt.AlphaComposite;

/**
 * @author Kevin Seal
 */
public class AlphaCompositeCache {

    private CompositeControl[] composites;

    public AlphaCompositeCache(int size) {
        this(AlphaComposite.SRC_OVER, size);
    }

    public AlphaCompositeCache(int type, int size) {
        composites = new CompositeControl[size];
        for (int idx = 0; idx < size; idx++) {
            float alpha = ((float) idx) / (size - 1);
            composites[idx] = new CompositeControl(AlphaComposite.getInstance(type, alpha));
        }
    }

    public CompositeControl getComposite(int alpha) {
        int idx = (composites.length * alpha) / 255;
        idx = Math.max(0, Math.min(composites.length - 1, idx));
        return composites[idx];
    }
}
