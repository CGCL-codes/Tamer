package dsr.models.scenery;

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.util.TextureManager;
import com.jmex.effects.particles.ParticleFactory;
import com.jmex.effects.particles.ParticleMesh;
import dsr.AppletMain;
import dsr.data.AppletMapSquare;

public class SmokeScenery extends AbstractSceneryModel {

    private static final long serialVersionUID = 1L;

    public SmokeScenery(AppletMain m, AppletMapSquare sq, Vector3f dir) {
        super(m, "Smoke", sq.x + 0.5f, sq.y + 0.5f, 0f);
        BlendState as1 = main.getDisplay().getRenderer().createBlendState();
        as1.setBlendEnabled(true);
        as1.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
        as1.setDestinationFunction(BlendState.DestinationFunction.One);
        as1.setTestEnabled(true);
        as1.setTestFunction(BlendState.TestFunction.GreaterThan);
        as1.setEnabled(true);
        TextureState ts = main.getDisplay().getRenderer().createTextureState();
        Texture t = TextureManager.loadTexture("./data/textures/flaresmall.jpg", Texture.MinificationFilter.NearestNeighborLinearMipMap, Texture.MagnificationFilter.NearestNeighbor);
        ts.setTexture(t);
        ts.setEnabled(true);
        ParticleMesh mesh = ParticleFactory.buildParticles("particles", 50);
        mesh.setEmissionDirection(dir);
        mesh.setMaximumAngle(0.3f);
        mesh.setSpeed(0.05f);
        mesh.setMinimumLifeTime(30.0f);
        mesh.setMaximumLifeTime(130.0f);
        mesh.setStartSize(0.01f);
        mesh.setEndSize(0.6f);
        mesh.setStartColor(new ColorRGBA(0.204f, 0.255f, 0.355f, 0.3f));
        mesh.setEndColor(new ColorRGBA(0.204f, 0.255f, 0.355f, 0.0f));
        mesh.setInitialVelocity(0.01f);
        mesh.setRotateWithScene(true);
        mesh.setReleaseRate(1);
        mesh.setReleaseVariance(0.6f);
        mesh.setParticleSpinSpeed(0.08f);
        mesh.forceRespawn();
        mesh.warmUp(10);
        mesh.setModelBound(new BoundingBox());
        mesh.updateModelBound();
        mesh.setIsCollidable(false);
        ZBufferState zbuf = main.getDisplay().getRenderer().createZBufferState();
        zbuf.setWritable(false);
        zbuf.setEnabled(true);
        zbuf.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);
        mesh.setRenderState(ts);
        mesh.setRenderState(as1);
        mesh.setRenderState(zbuf);
        this.attachChild(mesh);
        sq.threeds.add(this);
    }
}
