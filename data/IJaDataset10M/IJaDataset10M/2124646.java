package org.skycastle.scratchpad.sketch.screen;

import com.jme.scene.Spatial;
import com.jme.system.DisplaySystem;

/**
 * Creates a texture with the current screen content by taking a snapshot of the rendering canvas buffer, and
 * setting it to a texture.
 * <p/>
 * Should work for graphics cards that don't have render to texture support
 *
 * @author Hans Haggstrom
 */
public final class CanvasToTextureBufferedScreen implements BufferedScreen {

    public void renderElement(final Spatial spatial) {
        throw new UnsupportedOperationException("This method has not yet been implemented.");
    }

    public void clear() {
        throw new UnsupportedOperationException("This method has not yet been implemented.");
    }

    public Spatial get3DView(final DisplaySystem displaySystem) {
        throw new UnsupportedOperationException("This method has not yet been implemented.");
    }
}
