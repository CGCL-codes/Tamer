package net.java.dev.joode.test;

import net.java.dev.joode.collision.SurfaceParameters;

public class SimpleWorld extends TestingWorld {

    public static final int MAX_CONTACTS = 10000;

    CollisionManager mng;

    SurfaceParameters params = new SurfaceParameters();

    public SimpleWorld(boolean gravity) {
        super(gravity);
        params.mu = 10;
        mng = new CollisionManager(world, params);
    }

    public void step() {
        mng.reset();
        space.collide(world, mng);
        mng.applyContacts();
        super.step();
    }
}
