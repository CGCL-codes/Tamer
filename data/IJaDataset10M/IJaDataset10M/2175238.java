package jmetest.effects;

import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingSphere;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.ZBufferState;
import com.jmex.effects.particles.ParticleFactory;
import com.jmex.effects.particles.ParticlePoints;

/**
 * @author Joshua Slack
 * @version $Id: TestPointParticles.java 4130 2009-03-19 20:04:51Z blaine.dev $
 */
public class TestPointParticles extends SimpleGame {

    private ParticlePoints pPoints;

    private Vector3f currentPos = new Vector3f(), newPos = new Vector3f();

    private float frameRate = 0;

    public static void main(String[] args) {
        TestPointParticles app = new TestPointParticles();
        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
        app.start();
    }

    protected void simpleUpdate() {
        if (tpf > 1f) tpf = 1.0f;
        if ((int) currentPos.x == (int) newPos.x && (int) currentPos.y == (int) newPos.y && (int) currentPos.z == (int) newPos.z) {
            newPos.x = (float) Math.random() * 50 - 25;
            newPos.y = (float) Math.random() * 50 - 25;
            newPos.z = (float) Math.random() * 50 - 150;
        }
        frameRate = timer.getFrameRate() / 2;
        currentPos.x -= (currentPos.x - newPos.x) / frameRate;
        currentPos.y -= (currentPos.y - newPos.y) / frameRate;
        currentPos.z -= (currentPos.z - newPos.z) / frameRate;
        pPoints.setOriginOffset(currentPos);
    }

    protected void simpleInitGame() {
        display.setTitle("Particle System - Point Particles");
        lightState.setEnabled(false);
        pPoints = ParticleFactory.buildPointParticles("particles", 300);
        pPoints.setPointSize(5);
        pPoints.setAntialiased(true);
        pPoints.setEmissionDirection(new Vector3f(0, 1, 0));
        pPoints.setOriginOffset(new Vector3f(0, 0, 0));
        pPoints.setInitialVelocity(.006f);
        pPoints.setStartSize(2.5f);
        pPoints.setEndSize(.5f);
        pPoints.setMinimumLifeTime(1200f);
        pPoints.setMaximumLifeTime(1400f);
        pPoints.setStartColor(new ColorRGBA(1, 0, 0, 1));
        pPoints.setEndColor(new ColorRGBA(0, 1, 0, 0));
        pPoints.setMaximumAngle(360f * FastMath.DEG_TO_RAD);
        pPoints.getParticleController().setControlFlow(false);
        pPoints.warmUp(120);
        BlendState as1 = display.getRenderer().createBlendState();
        as1.setBlendEnabled(true);
        as1.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
        as1.setDestinationFunction(BlendState.DestinationFunction.One);
        as1.setEnabled(true);
        rootNode.setRenderState(as1);
        ZBufferState zstate = display.getRenderer().createZBufferState();
        zstate.setEnabled(false);
        pPoints.setRenderState(zstate);
        pPoints.setModelBound(new BoundingSphere());
        pPoints.updateModelBound();
        rootNode.attachChild(pPoints);
    }
}
