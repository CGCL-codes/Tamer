package org.xith3d.physics;

import org.jagatoo.datatypes.Enableable;
import org.xith3d.loop.Updatable;
import org.xith3d.loop.UpdatingThread.TimingMode;
import org.xith3d.physics.collision.CollisionEngine;
import org.xith3d.physics.collision.CollisionResolveListener;
import org.xith3d.physics.collision.CollisionResolver;
import org.xith3d.physics.collision.CollisionResolversManager;
import org.xith3d.physics.simulation.SimulationEngine;
import org.xith3d.physics.simulation.SurfaceParameters;

/**
 * This is a handy utility class, that holds instances of
 * CollisionEngine and SimulationEngine.<br>
 * The update() method directly invokes the update() methods of these two engines.
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public abstract class PhysicsEngine implements Updatable, Enableable {

    private final CollisionResolversManager collisionResolversManager;

    private final CollisionEngine collEngine;

    private final SimulationEngine simEngine;

    private PhysicsGFXManager gfxManager = null;

    private boolean enabled = true;

    public final CollisionResolversManager getCollisionResolversManager() {
        return (collisionResolversManager);
    }

    public final void setDefaultSurfaceParameters(SurfaceParameters parameters) {
        getCollisionResolversManager().setDefaultSurfaceParameters(parameters);
    }

    public final SurfaceParameters getDefaultSurfaceParameters() {
        return (getCollisionResolversManager().getDefaultSurfaceParameters());
    }

    public final void addCollisionResolver(CollisionResolver cr) {
        getCollisionResolversManager().addCollisionResolver(cr);
    }

    public final void removeCollisionResolver(CollisionResolver cr) {
        getCollisionResolversManager().removeCollisionResolver(cr);
    }

    public final void addCollisionResolverListener(CollisionResolveListener l) {
        getCollisionResolversManager().addCollisionResolveListener(l);
    }

    public final void removeCollisionResolverListener(CollisionResolveListener l) {
        getCollisionResolversManager().removeCollisionResolveListener(l);
    }

    public final CollisionEngine getCollisionEngine() {
        return (collEngine);
    }

    public final SimulationEngine getSimulationEngine() {
        return (simEngine);
    }

    public final void setGFXManager(PhysicsGFXManager gfxManager) {
        this.gfxManager = gfxManager;
    }

    public final PhysicsGFXManager getGFXManager() {
        return (gfxManager);
    }

    /**
     * Sets this PhysicsEngine enabled/disabled.<br>
     * If not enabled, the update() method will do nothing.
     * 
     * @param enabled
     */
    public final void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (collEngine != null) collEngine.setEnabled(enabled);
        if (simEngine != null) simEngine.setEnabled(enabled);
    }

    /**
     * @return if this PhysicsEngine is enabled.<br>
     * If not enabled, the update() method will do nothing.
     */
    public final boolean isEnabled() {
        return (enabled);
    }

    public void update(long gameTime, long frameTime, TimingMode timingMode) {
        if (simEngine != null) simEngine.update(gameTime, frameTime, timingMode);
        if (collEngine != null) collEngine.update(gameTime, frameTime, timingMode);
        if (gfxManager != null) gfxManager.update(gameTime, frameTime, timingMode);
    }

    protected PhysicsEngine(CollisionEngine collEngine, SimulationEngine simEngine) {
        this.collEngine = collEngine;
        this.simEngine = simEngine;
        this.gfxManager = new PhysicsGFXManager();
        if (simEngine != null) {
            this.collisionResolversManager = new CollisionResolversManager(collEngine, simEngine.newSurfaceParameters());
            simEngine.setCollisionResolversManager(collisionResolversManager);
        } else {
            this.collisionResolversManager = null;
        }
    }
}
