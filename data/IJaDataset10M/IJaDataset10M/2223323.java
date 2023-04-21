package com.android1.amarena2d.nodes.particle.effects;

import com.android1.amarena2d.commons.BlendFunction;
import com.android1.amarena2d.nodes.particle.RadialParticleEffectConfig;

public class RadialTestEffect extends RadialParticleEffect {

    public RadialTestEffect() {
        this(300);
    }

    public RadialTestEffect(int totalParticles) {
        super(new Config(totalParticles));
    }

    private static class Config extends RadialParticleEffectConfig {

        private Config(int totalParticle) {
            totalParticles = totalParticle;
            duration = -1;
            angle = 0;
            angleVar = 360;
            startRadius = 250;
            rotatePerSecond = 20;
            endRadius = 0;
            endRadiusVar = 5;
            life = 2;
            lifeVar = 0;
            startSize = 20.0f;
            startSizeVar = 10.0f;
            endSize = 5f;
            emissionRate = calcEmissionRate();
            startColor.r = 0.12f;
            startColor.g = 0.25f;
            startColor.b = 0.76f;
            startColor.a = 1.0f;
            startColorVar.r = 0.0f;
            startColorVar.g = 0.0f;
            startColorVar.b = 0.0f;
            startColorVar.a = 0.0f;
            endColor.r = 0.0f;
            endColor.g = 0.0f;
            endColor.b = 0.0f;
            endColor.a = 1.0f;
            endColorVar.r = 0.0f;
            endColorVar.g = 0.0f;
            endColorVar.b = 0.0f;
            endColorVar.a = 0.0f;
        }
    }

    @Override
    public BlendFunction getBlendFunction() {
        return BlendFunction.ADDITIVE_BLEND;
    }
}
