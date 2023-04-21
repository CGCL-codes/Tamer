package dsr.effects;

import com.jme.image.Texture;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Controller;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.scene.state.BlendState.DestinationFunction;
import com.jme.scene.state.BlendState.SourceFunction;
import com.jme.scene.state.BlendState.TestFunction;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.effects.particles.ParticleFactory;
import com.jmex.effects.particles.ParticleMesh;
import dsr.AppletMain;
import dsr.SoundEffects;
import dsrwebserver.tables.EquipmentTypesTable;

public final class Explosion extends AbstractEffect {

    private static final long serialVersionUID = 1L;

    private ParticleMesh explosion;

    private static BlendState bs;

    private static TextureState ts;

    private static ZBufferState zstate;

    private static boolean initd = false;

    private byte eqtype;

    public Explosion(AppletMain m, float x, float y, float z, float rad, byte _eqtype) {
        super(m, "Explosion", x, y, z, true, 10000);
        eqtype = _eqtype;
        if (!initd) {
            initd = true;
            InitExplosion(m.getDisplay(), 1f);
        }
        explosion = createSmallExplosion(rad);
        explosion.forceRespawn();
        this.attachChild(explosion);
        this.updateGeometricState(0, true);
        this.updateRenderState();
        main.playSound(SoundEffects.EXPLOSION);
    }

    private void InitExplosion(DisplaySystem display, float radius) {
        bs = display.getRenderer().createBlendState();
        bs.setBlendEnabled(true);
        bs.setSourceFunction(SourceFunction.SourceAlpha);
        bs.setDestinationFunction(DestinationFunction.One);
        bs.setTestEnabled(true);
        bs.setTestFunction(TestFunction.GreaterThan);
        ts = display.getRenderer().createTextureState();
        ts.setTexture(TextureManager.loadTexture(AppletMain.DATA_DIR + "textures/flaresmall.jpg", Texture.MinificationFilter.NearestNeighborLinearMipMap, Texture.MagnificationFilter.NearestNeighbor));
        zstate = display.getRenderer().createZBufferState();
        zstate.setEnabled(false);
        createSmallExplosion(radius);
    }

    private ParticleMesh createSmallExplosion(float radius) {
        ParticleMesh explosion = ParticleFactory.buildParticles("small", 20);
        explosion.setEmissionDirection(new Vector3f(0.0f, 1.0f, 0.0f));
        explosion.setMaximumAngle(FastMath.PI);
        explosion.setSpeed(0.1f);
        explosion.setMinimumLifeTime(60.0f);
        explosion.setMaximumLifeTime(100.0f * radius);
        explosion.setStartSize(radius / 20f);
        explosion.setEndSize(radius);
        if (eqtype == EquipmentTypesTable.ET_SMOKE_GRENADE) {
            explosion.setStartColor(new ColorRGBA(1.0f, 0f, 0f, 0f));
            explosion.setEndColor(new ColorRGBA(1.0f, 1f, 1f, 1f));
        } else if (eqtype == EquipmentTypesTable.ET_NERVE_GAS) {
            explosion.setStartColor(new ColorRGBA(0.0f, 1f, 0f, 0f));
            explosion.setEndColor(new ColorRGBA(0.5f, 1f, .5f, 1f));
        } else {
            explosion.setStartColor(new ColorRGBA(1.0f, 0.312f, 0.121f, 1.0f));
            explosion.setEndColor(new ColorRGBA(1.0f, 0.24313726f, 0.03137255f, 0.0f));
        }
        explosion.setControlFlow(false);
        explosion.setInitialVelocity(0.01f);
        explosion.setParticleSpinSpeed(0.0f);
        explosion.setRepeatType(Controller.RT_CLAMP);
        explosion.warmUp(200);
        explosion.setRenderState(ts);
        explosion.setRenderState(bs);
        explosion.setRenderState(zstate);
        explosion.updateRenderState();
        return explosion;
    }
}
