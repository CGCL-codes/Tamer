package drk.graphics.particle;

import javax.media.opengl.GL;
import drk.DeltaTimer;
import drk.graphics.GLRenderable;

public abstract class Particle implements TimeUpdatable, GLRenderable {

    DeltaTimer dtimer;

    NewtonMotion3D motion;

    public Particle() {
        super();
    }

    public void setDeltaTimer(DeltaTimer deltat) {
        dtimer = deltat;
        motion.setDeltaTimer(deltat);
    }

    public void render(GL gl) {
    }

    public void update() {
        motion.update();
    }
}
