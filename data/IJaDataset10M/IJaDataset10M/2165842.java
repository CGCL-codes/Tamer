package org.xith3d.physics.collision.joode;

import net.java.dev.joode.space.Space;
import net.java.dev.joode.util.Matrix3;
import org.openmali.vecmath2.Matrix3f;
import org.openmali.vecmath2.Tuple3f;
import org.xith3d.physics.joode.Convert;

/**
 * Sphere collideable type.
 * 
 * @author Amos Wenger (aka BlueSky)
 * @author Marvin Froehlich (aka Qudus)
 */
public class JoodeSphereCollideable extends org.xith3d.physics.collision.collideable.SphereCollideable implements JoodeCollideable {

    /**
     * Creates a new SphereCollideable.
     * 
     * @param eng
     * @param radius
     */
    public JoodeSphereCollideable(JoodeCollisionEngine eng, float radius) {
        super(eng, radius);
        this.joodeGeom = new net.java.dev.joode.geom.Sphere(null, radius);
        this.init(eng, joodeGeom);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setRadius(float radius) {
        super.setRadius(radius);
        joodeGeom.setRadius(radius);
    }

    /** The JOODE geom used for collision detection */
    private final net.java.dev.joode.geom.Sphere joodeGeom;

    /** Here is a lazily-allocated Matrix3 to avoid garbage creation */
    private Matrix3 rotMatrix3 = null;

    /**
     * Initializes a new {@link JoodeCollideable}.
     * 
     * @param engine The engine these collideables have been created by
     * @param geom The underlying JOODE geom
     */
    private final void init(JoodeCollisionEngine engine, net.java.dev.joode.geom.Geom geom) {
        if (geom instanceof Space) {
        }
        if (geom.isPlaceable()) {
            geom.setPosition(Convert.toJOODE(getWorldPos()));
        }
        if (geom instanceof Space) {
        }
        if (geom.isPlaceable()) {
            geom.setRotation(Convert.toJOODE(getWorldRotMat()));
        }
        geom.setUserData(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void finalize() {
        joodeGeom.setUserData(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JoodeCollisionEngine getEngine() {
        return (JoodeCollisionEngine) super.getEngine();
    }

    /**
     * {@inheritDoc}
     */
    public final net.java.dev.joode.geom.Sphere getJOODEGeom() {
        return joodeGeom;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setParent(org.xith3d.physics.collision.CollideableGroup parent) {
        super.setParent(parent);
    }

    /**
     * {@inheritDoc}
     */
    public final void setEnabled(boolean enabled) {
        getJOODEGeom().setEnabled(enabled);
    }

    /**
     * {@inheritDoc}
     */
    public final boolean isEnabled() {
        return getJOODEGeom().isEnabled();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void applyWorldRotation(Matrix3f worldRot) {
        if (rotMatrix3 == null) {
            rotMatrix3 = new Matrix3();
        }
        Convert.toJOODE(worldRot, rotMatrix3);
        joodeGeom.setRotation(rotMatrix3);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void applyWorldPosition(Tuple3f worldPos) {
        joodeGeom.setPosition(worldPos.getX(), worldPos.getY(), worldPos.getZ());
    }
}
