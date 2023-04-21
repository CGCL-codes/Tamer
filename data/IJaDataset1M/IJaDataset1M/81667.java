package com.android1.amarena2d.nodes.particle;

import com.android1.amarena2d.nodes.particle.effects.GravityParticleEffect;
import com.android1.amarena2d.nodes.particle.effects.ParticleEffect;
import com.android1.amarena2d.nodes.particle.effects.RadialParticleEffect;

public class EmitterFactory {

    public static Emitter create(final ParticleEffect effect, final float x, final float y, final float width, final float height) {
        int effectLevel = Emitter.InitFrequency.OnCreate;
        if (!effect.getEffectConfig().isInfiniteDuration()) effectLevel = Emitter.InitFrequency.OnReset;
        if (effect.getEmitterType() == Emitter.Type.Gravity) {
            return create((GravityParticleEffect) effect, effectLevel, x, y, width, height);
        } else if (effect.getEmitterType() == Emitter.Type.Radial) {
            return create((RadialParticleEffect) effect, effectLevel, x, y, width, height);
        }
        throw new UnsupportedOperationException("No Emitter found for: " + effect.toString());
    }

    public static Emitter create(final ParticleEffect effect, final float x, final float y) {
        return create(effect, x, y, effect.getDefaultWidth(), effect.getDefaultHeight());
    }

    public static GravityEmitter create(final GravityParticleEffect effect, final int effectLevel, final float x, final float y, final float width, final float height) {
        return new GravityEmitter(effect, effectLevel, x, y, width, height);
    }

    public static RadialEmitter create(final RadialParticleEffect effect, final int effectLevel, final float x, final float y, final float width, final float height) {
        return new RadialEmitter(effect, effectLevel, x, y, width, height);
    }
}
