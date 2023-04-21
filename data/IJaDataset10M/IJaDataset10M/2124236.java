package progl;

import processing.core.PApplet;

/**
 * @invisible
 * @author christianr
 *
 */
public class ProGLApplet extends PApplet {

    ProGLGraphics g;

    public void size(final int i_width, final int i_height) {
        super.size(i_width, i_height, ProGLGraphics.graphics());
        g = (ProGLGraphics) super.g;
    }

    public void beginTransparent() {
        g.beginTransparent();
    }

    public void endTransparent() {
        g.endTransparent();
    }
}
