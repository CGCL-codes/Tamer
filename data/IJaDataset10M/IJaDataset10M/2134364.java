package fr.inria.zuist.engine;

import java.awt.Color;
import java.awt.Font;
import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;
import fr.inria.zvtm.engine.VirtualSpaceManager;
import fr.inria.zvtm.engine.VirtualSpace;
import fr.inria.zvtm.glyphs.Glyph;
import fr.inria.zvtm.glyphs.VText;
import fr.inria.zvtm.animation.EndAction;
import fr.inria.zvtm.animation.Animation;
import fr.inria.zvtm.animation.interpolation.IdentityInterpolator;

/** Description of text objects to be loaded/unloaded in the scene.
 *@author Emmanuel Pietriga
 */
public class TextDescription extends ObjectDescription {

    public static final String _start = "start";

    public static final String _middle = "middle";

    public static final String _end = "end";

    double vx, vy;

    float scale;

    String text;

    short anchor = VText.TEXT_ANCHOR_MIDDLE;

    Font font;

    Color fillColor;

    float alpha;

    private volatile VText glyph;

    /** Constructs the description of an image (VTextST).
        *@param id ID of object in scene
        *@param x x-coordinate in scene
        *@param y y-coordinate in scene
        *@param z z-index (layer). Feed 0 if you don't know.
        *@param s scale factor
        *@param tx text label
        *@param c text color
        *@param alpha in [0;1.0]. 0 is fully transparent, 1 is opaque
        *@param ta text alignment, one of VText.TEXT_ANCHOR_*
        *@param pr parent Region in scene
        */
    TextDescription(String id, double x, double y, int z, float s, String tx, Color c, float alpha, short ta, Region pr) {
        this.id = id;
        this.vx = x;
        this.vy = y;
        this.zindex = z;
        this.scale = s;
        this.text = tx;
        this.fillColor = c;
        this.alpha = alpha;
        this.parentRegion = pr;
        this.anchor = ta;
    }

    @Override
    public void createObject(final SceneManager sm, final VirtualSpace vs, boolean fadeIn) {
        if (glyph == null) {
            if (fadeIn) {
                glyph = new VText(vx, vy, zindex, fillColor, text, anchor, scale, 0.0f);
                if (font != null) {
                    ((VText) glyph).setFont(font);
                }
                if (!sensitive) {
                    glyph.setSensitivity(false);
                }
                Animation a = VirtualSpaceManager.INSTANCE.getAnimationManager().getAnimationFactory().createTranslucencyAnim(GlyphLoader.FADE_IN_DURATION, glyph, alpha, false, IdentityInterpolator.getInstance(), null);
                VirtualSpaceManager.INSTANCE.getAnimationManager().startAnimation(a, false);
            } else {
                glyph = new VText(vx, vy, zindex, fillColor, text, anchor, scale, alpha);
                if (font != null) {
                    ((VText) glyph).setFont(font);
                }
                if (!sensitive) {
                    glyph.setSensitivity(false);
                }
            }
            try {
                assert (!SwingUtilities.isEventDispatchThread());
                SwingUtilities.invokeAndWait(new Runnable() {

                    public void run() {
                        vs.addGlyph(glyph);
                        glyph.setOwner(TextDescription.this);
                        sm.objectCreated(TextDescription.this);
                    }
                });
            } catch (InterruptedException ie) {
            } catch (InvocationTargetException ite) {
            }
        }
    }

    @Override
    public void destroyObject(final SceneManager sm, final VirtualSpace vs, boolean fadeOut) {
        if (glyph != null) {
            if (fadeOut) {
                Animation a = VirtualSpaceManager.INSTANCE.getAnimationManager().getAnimationFactory().createTranslucencyAnim(GlyphLoader.FADE_OUT_DURATION, glyph, 0.0f, false, IdentityInterpolator.getInstance(), new TextHideAction(sm, vs));
                VirtualSpaceManager.INSTANCE.getAnimationManager().startAnimation(a, false);
                glyph = null;
            } else {
                try {
                    assert (!SwingUtilities.isEventDispatchThread());
                    SwingUtilities.invokeAndWait(new Runnable() {

                        public void run() {
                            vs.removeGlyph(glyph);
                            glyph = null;
                            sm.objectDestroyed(TextDescription.this);
                        }
                    });
                } catch (InterruptedException ie) {
                } catch (InvocationTargetException ite) {
                }
            }
        }
    }

    /** Get the AWT Font used to draw this text. */
    public void setFont(Font f) {
        this.font = f;
    }

    /** Get the AWT Font object used to draw this text. */
    public Font getFont() {
        return font;
    }

    @Override
    public Glyph getGlyph() {
        return glyph;
    }

    /** Get this text object's text string. */
    public String getText() {
        return text;
    }

    /** Get this text object's scale multiplication factor. Default is 1.0. */
    public float getScale() {
        return scale;
    }

    /** Get ZVTM constant value representing anchors "start", "midlle", "end". */
    public static short getAnchor(String anchor) {
        if (anchor.equals(_start)) {
            return VText.TEXT_ANCHOR_START;
        } else if (anchor.equals(_end)) {
            return VText.TEXT_ANCHOR_END;
        } else {
            return VText.TEXT_ANCHOR_MIDDLE;
        }
    }

    @Override
    public double getX() {
        return vx;
    }

    @Override
    public double getY() {
        return vy;
    }

    @Override
    public void moveTo(double x, double y) {
        this.vx = x;
        this.vy = y;
        if (glyph != null) {
            glyph.moveTo(vx, vy);
        }
    }

    public float getTranslucencyValue() {
        return alpha;
    }
}

class TextHideAction implements EndAction {

    VirtualSpace vs;

    SceneManager sm;

    TextHideAction(SceneManager sm, VirtualSpace vs) {
        this.sm = sm;
        this.vs = vs;
    }

    public void execute(Object subject, Animation.Dimension dimension) {
        try {
            vs.removeGlyph((Glyph) subject);
            sm.objectDestroyed((TextDescription) ((Glyph) subject).getOwner());
        } catch (ArrayIndexOutOfBoundsException ex) {
            if (SceneManager.getDebugMode()) {
                System.err.println("Warning: attempt at destroying rectangle " + ((Glyph) subject).hashCode() + " failed. Trying one more time.");
            }
            recoverFailingAnimationEnded(subject, dimension);
        }
    }

    public void recoverFailingAnimationEnded(Object subject, Animation.Dimension dimension) {
        try {
            vs.removeGlyph((Glyph) subject);
            sm.objectDestroyed((TextDescription) ((Glyph) subject).getOwner());
        } catch (ArrayIndexOutOfBoundsException ex) {
            if (SceneManager.getDebugMode()) {
                System.err.println("Warning: attempt at destroying rectangle " + ((Glyph) subject).hashCode() + " failed. Giving up.");
            }
            recoverFailingAnimationEnded(subject, dimension);
        }
    }
}
