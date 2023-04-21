package org.pandcorps.pandam;

public final class Panderer {

    Panderer() {
    }

    public final void render(final Panlayer layer, final Panmage image, final float x, final float y, final float z) {
        image.render(layer, x, y, z);
    }

    public final void render(final Panlayer layer, final Panmage image, final float x, final float y, final float z, final float ix, final float iy, final float iw, final float ih) {
        image.render(layer, x, y, z, ix, iy, iw, ih);
    }
}
