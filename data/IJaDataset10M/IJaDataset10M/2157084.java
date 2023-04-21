package com.googlecode.jumpnevolve.graphics.effects;

import org.newdawn.slick.particles.Particle;
import org.newdawn.slick.particles.ParticleEmitter;
import org.newdawn.slick.particles.ParticleSystem;
import com.googlecode.jumpnevolve.util.Parameter;

/**
 * Erzeugt einen Nebeleffekt.
 * 
 * @author Niklas Fiekas
 */
public class FogEmitterFactory implements ParticleEmitterFactory {

    @Override
    public ParticleEmitter createParticleEmitter() {
        return new ParticleEmitterAdapter() {

            private static final int INTERVAL = Parameter.EFFECTS_FOG_INTERVAL;

            private int timer;

            @Override
            public void update(ParticleSystem system, int delta) {
                this.timer -= delta;
                if (this.timer <= 0) {
                    this.timer = INTERVAL;
                    Particle p = system.getNewParticle(this, 5000);
                    p.setColor(1.0f, 1.0f, 1.0f, 0.0f);
                    p.setPosition((float) ((Math.random() - 0.5f) * 200.0f), 0);
                    p.setSize(100.0f);
                }
            }

            public void updateParticle(Particle particle, int delta) {
                particle.adjustVelocity(((float) Math.random() - 0.5f) * 0.005f, ((float) Math.random() - 0.45f) * -0.003f);
                if (particle.getLife() > 2500) {
                    particle.adjustColor(0.0f, 0.0f, 0.0f, +0.00010f * delta);
                } else {
                    particle.adjustColor(0.0f, 0.0f, 0.0f, -0.00010f * delta);
                }
            }
        };
    }
}
