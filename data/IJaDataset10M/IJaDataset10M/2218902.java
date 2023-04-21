package com.android1.amarena2d.nodes.particle.effects;

import com.android1.amarena2d.commons.BlendFunction;
import com.android1.amarena2d.nodes.particle.GravityParticleEffectConfig;

public class SmokeEffect extends GravityParticleEffect {

    public SmokeEffect() {
        this(100);
    }

    public SmokeEffect(int totalParticles) {
        super(new Config(totalParticles));
    }

    private static class Config extends GravityParticleEffectConfig {

        private Config(int totalParticle) {
            totalParticles = totalParticle;
            duration = -1;
            gravity.x = 0;
            gravity.y = 0;
            angle = 90;
            angleVar = 5;
            radialAccel = 0;
            radialAccelVar = 0;
            tangentialAccel = 0;
            tangentialAccelVar = 0;
            life = 10;
            lifeVar = 2;
            speed = 25;
            speedVar = 10;
            startSize = 80.0f;
            startSizeVar = 20.0f;
            emissionRate = calcEmissionRate();
            startColor.r = 1.0f;
            startColor.g = 1.0f;
            startColor.b = 1.0f;
            startColor.a = 1.0f;
            startColorVar.r = 0.02f;
            startColorVar.g = 0.02f;
            startColorVar.b = 0.02f;
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
    public float getDefaultWidth() {
        return 300;
    }

    @Override
    public BlendFunction getBlendFunction() {
        return BlendFunction.DEFAULT;
    }
}
