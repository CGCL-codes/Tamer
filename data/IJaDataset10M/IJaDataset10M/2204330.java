package com.yellowbkpk.jaytracer.world.light;

import javax.vecmath.Vector3f;
import com.yellowbkpk.jaytracer.world.shape.Primitive;

public abstract class Light extends Primitive {

    private float brightness;

    public Light(Vector3f loc, float size, float bright) {
        super(loc, size);
        brightness = bright;
    }
}
