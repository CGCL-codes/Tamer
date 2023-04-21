package core.effects;

import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 * 
 * Used for displaying particle effects.
 * 
 * @author Jason
 * 
 */
public class ParticleEngine {

    protected final ArrayList<Effect> effects;

    public ParticleEngine() {
        effects = new ArrayList<Effect>();
    }

    public synchronized void tick(double seconds) {
        for (int i = effects.size() - 1; i >= 0; i--) {
            Effect effect = effects.get(i);
            if (!effect.tick(seconds)) {
                effects.remove(i);
                if (effect instanceof Recyclable) {
                    ParticleFactory.recycle(effect);
                }
            }
        }
    }

    public synchronized void render(Graphics2D g) {
        for (Effect effect : effects) {
            effect.render(g);
        }
    }

    public synchronized void add(Effect effect) {
        effects.add(effect);
    }

    public synchronized void remove(Effect effect) {
        effects.remove(effect);
        if (effect instanceof Recyclable) {
            ParticleFactory.recycle(effect);
        }
    }
}
