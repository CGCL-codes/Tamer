package texone.org.simulation.particle;

import java.util.ArrayList;
import provector.Vector3f;

public class ParticleSystem {

    ArrayList particles;

    ArrayList springs;

    ArrayList attractions;

    RungeKuttaIntegrator integrator;

    Vector3f gravity;

    Drag drag;

    public ParticleSystem(final Vector3f i_gravity, final float i_drag) {
        integrator = new RungeKuttaIntegrator(this);
        particles = new ArrayList();
        springs = new ArrayList();
        attractions = new ArrayList();
        gravity = i_gravity.cloneVector();
        drag = new Drag(i_drag);
    }

    public ParticleSystem(final float i_gravity, final float i_drag) {
        this(new Vector3f(0f, i_gravity, 0f), i_drag);
    }

    public Vector3f gravity() {
        return gravity;
    }

    public final void setGravity(float x, float y, float z) {
        gravity.set(x, y, z);
    }

    public final void setGravity(float g) {
        gravity.set(0.0F, g, 0.0F);
    }

    public final void setDrag(final float i_drag) {
        drag.drag = i_drag;
    }

    private final void cleanUp() {
        for (int i = particles.size() - 1; i >= 0; i--) {
            Particle p = (Particle) particles.get(i);
            if (p.isDead()) particles.remove(i);
        }
        for (int i = springs.size() - 1; i >= 0; i--) {
            Spring f = (Spring) springs.get(i);
            if (f.hasDead()) springs.remove(i);
        }
        for (int i = attractions.size() - 1; i >= 0; i--) {
            Attraction f = (Attraction) attractions.get(i);
            if (f.hasDead()) attractions.remove(i);
        }
    }

    public final void advanceTime(float time) {
        cleanUp();
        integrator.step(time);
    }

    public final void tick() {
        cleanUp();
        integrator.step(1.0F);
    }

    public final void tick(float t) {
        cleanUp();
        integrator.step(t);
    }

    public Particle createParticle(final Vector3f i_position, final float i_mass) {
        final Particle particle = new Particle(i_position, i_mass);
        particles.add(particle);
        integrator.allocateParticles();
        return particle;
    }

    public Particle createParticle(final float i_x, final float i_y, final float i_z, final float i_mass) {
        return createParticle(new Vector3f(i_x, i_y, i_z), i_mass);
    }

    public final Particle createParticle() {
        return createParticle(0, 0, 0, 1);
    }

    public Spring createSpring(final Particle i_particleA, final Particle i_particleB, final float i_springConstant, final float i_damping, final float i_restLength) {
        Spring s = new Spring(i_particleA, i_particleB, i_springConstant, i_damping, i_restLength);
        springs.add(s);
        return s;
    }

    public Spring createSpring(final Particle i_particleA, final Particle i_particleB, final float i_springConstant, final float i_damping) {
        final float restLength = i_particleA.position().distance(i_particleB.position());
        return createSpring(i_particleA, i_particleB, i_springConstant, i_damping, restLength);
    }

    public final Attraction makeAttraction(Particle a, Particle b, float k, float minDistance) {
        Attraction m = new Attraction(a, b, k, minDistance);
        attractions.add(m);
        return m;
    }

    public final void clear() {
        particles.clear();
        springs.clear();
        attractions.clear();
    }

    protected final void applyForces() {
        for (int i = 0; i < particles.size(); i++) {
            Particle particle = (Particle) particles.get(i);
            particle.force.setZero();
            drag.applyForceTo(particle);
        }
        for (int i = 0; i < springs.size(); i++) {
            Spring f = (Spring) springs.get(i);
            f.apply();
        }
        for (int i = 0; i < attractions.size(); i++) {
            Attraction f = (Attraction) attractions.get(i);
            f.apply();
        }
    }

    public final int numberOfParticles() {
        return particles.size();
    }

    public final int numberOfSprings() {
        return springs.size();
    }

    public final int numberOfAttractions() {
        return attractions.size();
    }

    public final Particle getParticle(int i) {
        return (Particle) particles.get(i);
    }

    public final Spring getSpring(int i) {
        return (Spring) springs.get(i);
    }

    public final Attraction getAttraction(int i) {
        return (Attraction) attractions.get(i);
    }
}
