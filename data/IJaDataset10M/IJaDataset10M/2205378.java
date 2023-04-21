package org.odlabs.wiquery.core.effects.sliding;

import org.odlabs.wiquery.core.effects.Effect;
import org.odlabs.wiquery.core.effects.EffectSpeed;

/**
 * $Id: SlideDown.java 1714 2011-09-22 20:38:30Z hielke.hoeve $
 * <p>
 * Defines the slidedown {@link Effect}. A slide down effect consists to slide a component
 * in a top-bottom direction.
 * </p>
 * 
 * @author Lionel Armanet
 * @since 0.5
 */
public class SlideDown extends Effect {

    /** Constant of serialization */
    private static final long serialVersionUID = -1708160228524287367L;

    /**
	 * Creates this effect
	 */
    public SlideDown() {
        super();
    }

    /**
	 * Creates this effect with the given {@link EffectSpeed}.
	 */
    public SlideDown(EffectSpeed effectSpeed) {
        super(effectSpeed);
    }

    public String chainLabel() {
        return "slideDown";
    }
}
