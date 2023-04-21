package net.sf.gamine.effects.particles;

import net.sf.gamine.common.*;
import net.sf.gamine.control.*;
import net.sf.gamine.physics.*;
import net.sf.gamine.render.*;
import net.sf.gamine.util.*;
import java.util.*;

/**
 * ParticleEffect is useful for creating a wide range of visual effects.  It is a {@link Compound} that combines the
 * following elements:
 * <ul>
 * <li>A {@link ParticleSet} to store the locations and velocities of a set of particles</li>
 * <li>A {@link PointCloud} to render the particles</li>
 * <li>One or more {@link ParticleEmitter}s that define when and how particles should be created</li>
 * <li>A {@link Controller} to perform the work of creating and destroying particles</li>
 * <li>Optionally, one or more {@link Force}s or {@link ContactSet}s that affect the behavior of the particles</li>
 * <li>Optionally, a {@link RenderTreeNode} to automatically add the PointCloud to the render tree</li>
 * </ul>
 * When {@link #start()} is called on the ParticleEffect, it adds all of its components to the Sequence and sets all
 * particles to be invisible.  During each iteration, it then takes the following actions:
 * <ul>
 * <li>New particles are emitted from each ParticleEmitter.  Each emitter defines the rate at which particles are emitted
 * and their initial positions and velocities.  The new particles are set to be visible.</li>
 * <li>If any particles have reached the end of their lifetimes, they are set to be invisible.</li>
 * <li>The colors of all visible particles are updated based on the time since they were emitted and the specified
 * initial and final colors.</li>
 * </ul>
 * The particle effect will continue running until you call {@link #stop()}.  Alternatively, you can configure the
 * effect to stop automatically when the last visible particle is destroyed.  This is useful for one time effects,
 * such as explosions or fireworks, since it frees you from needing to keep track of when the effect finishes and
 * stop it manually.  Call {@link #setAutomaticStopEnabled(boolean)} to enable automatic stopping.
 */
public class ParticleEffect extends Compound {

    private float particleLifetime, currentTime;

    private int numAvailable;

    private boolean automaticStopEnabled;

    private final ParticleSet particleSet;

    private final PointCloud pointCloud;

    private final ArrayList<ParticleEmitter> emitters;

    private final List<ParticleEmitter> unmodifiableEmitters;

    private final Color initialColor, finalColor, tempColor;

    private final Float3 position, velocity;

    private final int availableParticles[];

    private final float particleAge[];

    private final Random random;

    /**
   * Create a new ParticleEffect.
   *
   * @param particleCount   the number of particles to include in the ParticleSet and PointCloud.  This is the largest
   *                        number of particles that can ever be visible at one time.
   * @param controlStage    the ControlStage to which the Controller should be added
   * @param physicsStage    the PhysicsStage to which the ParticleSet should be added
   */
    public ParticleEffect(int particleCount, ControlStage controlStage, PhysicsStage physicsStage) {
        super(controlStage, physicsStage);
        particleSet = new ParticleSet(particleCount);
        pointCloud = new PointCloud(particleCount);
        pointCloud.setBoundToBodies(particleSet);
        emitters = new ArrayList<ParticleEmitter>();
        unmodifiableEmitters = Collections.unmodifiableList(emitters);
        availableParticles = new int[particleCount];
        particleAge = new float[particleCount];
        initialColor = new Color(1, 1, 1);
        finalColor = new Color(1, 1, 1);
        tempColor = new Color();
        position = new Float3();
        velocity = new Float3();
        random = new Random();
        particleLifetime = 1;
        for (int i = 0; i < particleCount; i++) {
            pointCloud.setPointVisible(i, false);
            availableParticles[i] = i;
            particleAge[i] = Float.MAX_VALUE;
        }
        numAvailable = particleCount;
        addBodySet(particleSet);
        addController(new ParticleController());
    }

    /**
   * Create a new ParticleEffect.
   *
   * @param particleCount   the number of particles to include in the ParticleSet and PointCloud.  This is the largest
   *                        number of particles that can ever be visible at one time.
   * @param sequence        a DefaultSequence that the ParticleEffects's components should be added to.  The Controller will be added to
   *                        its control stage and the ParticleSet will be added to its physics stage.
   */
    public ParticleEffect(int particleCount, DefaultSequence sequence) {
        this(particleCount, sequence.getControlStage(), sequence.getPhysicsStage());
    }

    @Override
    public void start() {
        currentTime = 0;
        super.start();
    }

    /**
   * Get the ParticleSet that specifies that positions and velocities of particles.
   */
    public ParticleSet getParticleSet() {
        return particleSet;
    }

    /**
   * Get the PointCloud used to render the particles.
   */
    public PointCloud getPointCloud() {
        return pointCloud;
    }

    /**
   * Get the list of ParticleEmitters.
   */
    public List<ParticleEmitter> getParticleEmitters() {
        return unmodifiableEmitters;
    }

    /**
   * Add a ParticleEmitter
   *
   * @param emitter    the ParticleEmitter emitter to add
   */
    public void addParticleEmitter(ParticleEmitter emitter) {
        emitters.add(emitter);
    }

    /**
   * Remove a ParticleEmitter.
   *
   * @param index    the index of the ParticleEmitter to remove
   */
    public void removeParticleEmitter(int index) {
        emitters.remove(index);
    }

    /**
   * Remove a ParticleEmitter.
   *
   * @param emitter    the ParticleEmitter to remove
   */
    public void removeParticleEmitter(ParticleEmitter emitter) {
        emitters.remove(emitter);
    }

    /**
   * Get the lifetime of each particle, measured in seconds.
   */
    public float getParticleLifetime() {
        return particleLifetime;
    }

    /**
   * Set the lifetime of each particle, measured in seconds.
   */
    public void setParticleLifetime(float particleLifetime) {
        this.particleLifetime = particleLifetime;
    }

    /**
   * Get the initial color of each particle.  Over the course of a particle's lifetime, it will continuously change
   * from the initial color to the final color.
   *
   * @param color    a Color object to store the color into
   * @return the Color object that was passed as an argument.
   */
    public Color getInitialColor(Color color) {
        color.set(initialColor);
        return color;
    }

    /**
   * Set the initial color of each particle.  Over the course of a particle's lifetime, it will continuously change
   * from the initial color to the final color.
   */
    public void setInitialColor(Color color) {
        initialColor.set(color);
    }

    /**
   * Get the final color of each particle.  Over the course of a particle's lifetime, it will continuously change
   * from the initial color to the final color.
   *
   * @param color    a Color object to store the color into
   * @return the Color object that was passed as an argument.
   */
    public Color getFinalColor(Color color) {
        color.set(finalColor);
        return color;
    }

    /**
   * Set the final color of each particle.  Over the course of a particle's lifetime, it will continuously change
   * from the initial color to the final color.
   */
    public void setFinalColor(Color color) {
        finalColor.set(color);
    }

    /**
   * Get whether {@link #stop()} will be called automatically when the last visible particle is destroyed.
   */
    public boolean getAutomaticStopEnabled() {
        return automaticStopEnabled;
    }

    /**
   * Set whether {@link #stop()} will be called automatically when the last visible particle is destroyed.
   */
    public void setAutomaticStopEnabled(boolean enabled) {
        automaticStopEnabled = enabled;
    }

    /**
   * This is the Controller that creates and destroys particles.
   */
    private class ParticleController extends Controller {

        @Override
        public void execute(float time, float dt) {
            PointCloud localCloud = pointCloud;
            int localAvailable[] = availableParticles;
            float localAge[] = particleAge;
            int particleCount = localCloud.getPointCount();
            boolean colorChanges = !initialColor.equals(finalColor);
            float baseRed = initialColor.red;
            float baseGreen = initialColor.green;
            float baseBlue = initialColor.blue;
            float baseAlpha = initialColor.alpha;
            float deltaRed = finalColor.red - baseRed;
            float deltaGreen = finalColor.green - baseGreen;
            float deltaBlue = finalColor.blue - baseBlue;
            float deltaAlpha = finalColor.alpha - baseAlpha;
            for (int i = 0; i < particleCount; i++) {
                float age = localAge[i];
                if (age < particleLifetime) {
                    age += dt;
                    localAge[i] = age;
                    if (age >= particleLifetime) {
                        localCloud.setPointVisible(i, false);
                        localAvailable[numAvailable++] = i;
                        if (numAvailable == particleCount && automaticStopEnabled) {
                            stop();
                            return;
                        }
                    } else if (colorChanges) {
                        float fract = age / particleLifetime;
                        tempColor.set(baseRed + fract * deltaRed, baseGreen + fract * deltaGreen, baseBlue + fract * deltaBlue, baseAlpha + fract * deltaAlpha);
                        localCloud.setPointColor(i, tempColor);
                    }
                }
            }
            ArrayList<ParticleEmitter> localEmitters = emitters;
            for (int i = 0; i < localEmitters.size(); i++) {
                ParticleEmitter emitter = localEmitters.get(i);
                float startTime = emitter.getStartTime();
                float stopTime = emitter.getStopTime();
                float loopTime = emitter.getLoopTime();
                float emissionRate = emitter.getEmissionRate();
                float t = currentTime - startTime;
                int cycle = (int) Math.floor(t / loopTime);
                if (cycle > 0) t -= loopTime * cycle;
                if (t >= startTime && t < stopTime) {
                    float emissionInterval = stopTime - t;
                    if (emissionInterval > dt) emissionInterval = dt;
                    int numToEmit = (int) (emissionRate * emissionInterval + random.nextFloat());
                    if (numToEmit > numAvailable) numToEmit = numAvailable;
                    for (int j = 0; j < numToEmit; j++) {
                        float offset = (j * dt) / numToEmit;
                        if (!emitter.emitParticle(position, velocity, t + offset, random)) continue;
                        int particleIndex = localAvailable[--numAvailable];
                        localCloud.setPointVisible(particleIndex, true);
                        localCloud.setPointColor(particleIndex, initialColor);
                        localAge[particleIndex] = 0;
                        position.set(position.x + offset * velocity.x, position.y + offset * velocity.y, position.z + offset * velocity.z);
                        particleSet.setBodyPosition(particleIndex, position);
                        particleSet.setBodyVelocity(particleIndex, velocity);
                    }
                }
            }
            currentTime += dt;
        }
    }
}
