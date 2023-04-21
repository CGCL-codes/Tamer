package net.playbesiege.entities;

import net.playbesiege.MainInit;
import net.playbesiege.Settings;
import net.playbesiege.gui.Options;
import net.playbesiege.utils.Textures;
import paulscode.sound.SoundSystemConfig;
import com.ardor3d.bounding.BoundingBox;
import com.ardor3d.extension.effect.particle.ParticleControllerListener;
import com.ardor3d.extension.effect.particle.ParticleFactory;
import com.ardor3d.extension.effect.particle.ParticleSystem;
import com.ardor3d.image.Texture.WrapMode;
import com.ardor3d.math.ColorRGBA;
import com.ardor3d.math.MathUtils;
import com.ardor3d.math.Vector3;
import com.ardor3d.math.type.ReadOnlyVector3;
import com.ardor3d.renderer.state.BlendState;
import com.ardor3d.renderer.state.TextureState;
import com.ardor3d.renderer.state.ZBufferState;
import com.ardor3d.scenegraph.Node;
import com.ardor3d.scenegraph.controller.ComplexSpatialController.RepeatType;

public class Explosion {

    public static void addParticles(Node root, ReadOnlyVector3 zero) {
        final ParticleSystem explosion = ParticleFactory.buildParticles("big", 80);
        explosion.setEmissionDirection(new Vector3(0.0f, 1.0f, 0.0f));
        explosion.setMaximumAngle(MathUtils.PI);
        explosion.setSpeed(0.09f);
        explosion.setMinimumLifeTime(20.0f);
        explosion.setMaximumLifeTime(20.0f);
        explosion.setStartSize(.2f);
        explosion.setEndSize(.5f);
        explosion.setStartColor(new ColorRGBA(1.0f, 0.312f, 0.121f, 1.0f));
        explosion.setEndColor(new ColorRGBA(1.0f, 0.24313726f, 0.03137255f, 0.0f));
        explosion.setControlFlow(false);
        explosion.setInitialVelocity(0.04f);
        explosion.setParticleSpinSpeed(0.0f);
        explosion.setRepeatType(RepeatType.CLAMP);
        explosion.setTranslation(zero);
        explosion.warmUp(1);
        root.attachChild(explosion);
        root.updateWorldTransform(true);
        explosion.getParticleController().addListener(new ParticleControllerListener() {

            public void onDead(ParticleSystem particles) {
                explosion.removeFromParent();
            }
        });
        explosion.forceRespawn();
        final BlendState blend = new BlendState();
        blend.setBlendEnabled(true);
        blend.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
        blend.setDestinationFunction(BlendState.DestinationFunction.One);
        explosion.setRenderState(blend);
        final TextureState ts = new TextureState();
        ts.setTexture(Textures.flare.get());
        ts.getTexture().setWrap(WrapMode.BorderClamp);
        ts.setEnabled(true);
        explosion.setRenderState(ts);
        final ZBufferState zstate = new ZBufferState();
        zstate.setWritable(false);
        explosion.setRenderState(zstate);
        if (Settings.sound && Options.getInstance().sound.isSelected()) MainInit.soundSystem.quickPlay(false, Explosion.class.getResource("explosion.wav"), "explosion.wav", false, zero.getXf(), zero.getYf(), zero.getZf(), SoundSystemConfig.ATTENUATION_LINEAR, 140f);
    }

    public static void testname(Node root) {
        ParticleSystem particles = ParticleFactory.buildParticles("particles", 300);
        particles.setEmissionDirection(new Vector3(0, 1, 0));
        particles.setInitialVelocity(.006);
        particles.setStartSize(2.5);
        particles.setEndSize(.5);
        particles.setMinimumLifeTime(1200);
        particles.setMaximumLifeTime(1400);
        particles.setStartColor(new ColorRGBA(1, 0, 0, 1));
        particles.setEndColor(new ColorRGBA(0, 1, 0, 0));
        particles.setMaximumAngle(360 * MathUtils.DEG_TO_RAD);
        particles.getParticleController().setControlFlow(false);
        particles.setParticlesInWorldCoords(true);
        final BlendState blend = new BlendState();
        blend.setBlendEnabled(true);
        blend.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
        blend.setDestinationFunction(BlendState.DestinationFunction.One);
        particles.setRenderState(blend);
        final TextureState ts = new TextureState();
        ts.setTexture(Textures.flare.get());
        ts.getTexture().setWrap(WrapMode.BorderClamp);
        ts.setEnabled(true);
        particles.setRenderState(ts);
        final ZBufferState zstate = new ZBufferState();
        zstate.setWritable(false);
        particles.setRenderState(zstate);
        particles.getParticleGeometry().setModelBound(new BoundingBox());
        root.attachChild(particles);
        root.updateWorldTransform(true);
        particles.warmUp(60);
    }
}
