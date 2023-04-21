package homura.hde.ext.effects.particles;

/**
 * ParticleControllerListener
 * This interface is used to receive key events from {@link homura.hde.ext.effects.particles.ParticleController}
 */
public interface ParticleControllerListener {

    void onDead(ParticleGeometry particles);
}
