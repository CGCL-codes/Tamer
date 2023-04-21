package com.android1.amarena2d.nodes.particle;

import com.badlogic.gdx.math.Vector2;

public class RadialParticle extends EffectParticle {

    public RadialParticle(int id) {
        super(id);
    }

    public final Vector2 startPos = new Vector2();

    public float angle;

    public float degreesPerSecond;

    public float startRadius;

    public float radius;

    public float deltaRadius;
}
