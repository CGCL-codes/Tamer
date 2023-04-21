package logic.game;

import geom.Vector;
import logic.interfaces.Moveable;

public class MoveableChargedParticle extends ChargedParticle implements Moveable {

    Vector velocity = null;

    @Override
    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    @Override
    public Vector getVelocity() {
        return this.velocity;
    }
}
