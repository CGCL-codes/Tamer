package ch.blackspirit.graphics.particle;

import javax.vecmath.Vector2f;

/**
 * @author Markus Koller
 */
public class PhysicsUpdater<T extends Particle> implements Updater<T> {

    private Vector2f positionChange = new Vector2f();

    private Vector2f velocityChange = new Vector2f();

    public void update(T particle, long elapsedTime) {
        float time = .001f * elapsedTime;
        velocityChange.set(particle.getForce());
        velocityChange.x *= time;
        velocityChange.y *= time;
        particle.getVelocity().add(velocityChange);
        positionChange.set(particle.getVelocity());
        positionChange.x *= time;
        positionChange.y *= time;
        particle.getPosition().add(positionChange);
    }
}
