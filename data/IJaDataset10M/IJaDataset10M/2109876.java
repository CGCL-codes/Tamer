package org.jdesktop.animation.transitions.effects;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.transitions.ComponentState;
import org.jdesktop.animation.transitions.Effect;

/**
 * This Effect combines one or more sub-effects to create a more complex
 * and interesting effect.  For example, you could create an effect that
 * both moves and scales by creating a CompositeEffect with the Move
 * and Scale effects.
 *
 * Composite effects are created by simply adding effects in the order
 * that you want them combined.
 *
 * @author Chet Haase
 */
public class CompositeEffect extends Effect {

    /**
     * The list of effects in the CompositeEffect.
     */
    private final List<Effect> effects = new ArrayList<Effect>();

    /**
     * Creates a CompositeEffect with no sub-effects.  Additional sub-effects
     * should be added via the <code>addEffect</code> method.
     */
    public CompositeEffect() {
    }

    /**
     * Creates a CompositeEffect with the given effect as the first
     * sub-effect.  Additional sub-effects
     * should be added via the <code>addEffect</code> method.
     */
    public CompositeEffect(Effect effect) {
        addEffect(effect);
    }

    /**
     * Adds an additional effect to this CompositeEffect.  This effect is
     * added to the end of the existing list of effects, and will be processed
     * after the other effects have been processed.
     */
    public void addEffect(Effect effect) {
        effects.add(effect);
        if (effect.getRenderComponent()) {
            this.setRenderComponent(true);
        }
        if (getStart() == null) {
            setStart(effect.getStart());
        }
        if (getEnd() == null) {
            setEnd(effect.getEnd());
        }
    }

    /**
     * This method is called during the initialization process of a
     * transition and allows the effects to set up the start state for
     * each effect.
     */
    public void setStart(ComponentState start) {
        for (Effect effect : effects) {
            effect.setStart(start);
        }
        super.setStart(start);
    }

    /**
     * Initializes all child effects at the start of a transition.
     */
    @Override
    public void init(Animator animator, Effect parentEffect) {
        for (Effect effect : effects) {
            effect.init(animator, this);
        }
        super.init(animator, null);
    }

    /**
     * Calls <code>cleanup()</code> on all child effects.
     */
    @Override
    public void cleanup(Animator animator) {
        for (Effect effect : effects) {
            effect.cleanup(animator);
        }
    }

    /**
     * This method is called during the initialization process of a
     * transition and allows the effects to set up the end state for
     * each effect.
     */
    public void setEnd(ComponentState end) {
        for (Effect effect : effects) {
            effect.setEnd(end);
        }
        super.setEnd(end);
    }

    /**
     * This method is called during each frame of the transition animation
     * and allows the effect to set up the Graphics state according to the
     * various sub-effects in this CompositeEffect.
     */
    @Override
    public void setup(Graphics2D g2d) {
        for (int i = 0; i < effects.size(); ++i) {
            Effect effect = effects.get(i);
            effect.setup(g2d);
            if (!getRenderComponent() && getComponentImage() == null) {
                setComponentImage(effect.getComponentImage());
            }
        }
        super.setup(g2d);
    }
}
