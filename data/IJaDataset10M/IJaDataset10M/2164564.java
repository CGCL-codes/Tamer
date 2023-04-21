package com.jme3.effect;

import com.jme3.renderer.Camera;
import java.util.Comparator;

@Deprecated
class ParticleComparator implements Comparator<Particle> {

    private Camera cam;

    public void setCamera(Camera cam) {
        this.cam = cam;
    }

    public int compare(Particle p1, Particle p2) {
        return 0;
    }
}
