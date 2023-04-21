package org.jdesktop.animation.transitions.effects;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import org.jdesktop.animation.transitions.Effect;

/**
 * Effect that performs a Fade (in or out) on the component.  This
 * is done by using an image of the component and altering the translucency (or
 * <code>AlphaComposite</code>) of the <code>Graphics2D</code> object
 * according to how far along the transition animation is.  
 * 
 * This is an abstract class that relies on the FadeIn or FadeOut subclasses
 * to set up the end (FadeIn) or start (FadeOut) states appropriately.
 * 
 * 
 * @author Chet Haase
 */
public abstract class Fade extends Effect {

    private float opacity;

    /**
     * This method is called by an animation set up by subclasses to
     * vary the opacity during the transition.
     */
    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    /**
     * This method is called prior to <code>paint()</code> during every 
     * frame of the transition animation.  It sets up 
     * an <code>AlphaComposite</code> object based on the current
     * opacity and sets that composite on the
     * <code>Graphics2D</code> object appropriately.
     */
    @Override
    public void setup(Graphics2D g2d) {
        AlphaComposite newComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity);
        g2d.setComposite(newComposite);
        super.setup(g2d);
    }
}
