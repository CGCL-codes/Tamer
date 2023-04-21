package jme3test.effect;

import com.jme3.app.SimpleApplication;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.effect.shapes.EmitterBoxShape;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

public class TestPointSprite extends SimpleApplication {

    public static void main(String[] args) {
        TestPointSprite app = new TestPointSprite();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        final ParticleEmitter emit = new ParticleEmitter("Emitter", Type.Point, 10000);
        emit.setShape(new EmitterBoxShape(new Vector3f(-1.8f, -1.8f, -1.8f), new Vector3f(1.8f, 1.8f, 1.8f)));
        emit.setGravity(0, 0, 0);
        emit.setLowLife(60);
        emit.setHighLife(60);
        emit.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 0, 0));
        emit.setImagesX(15);
        emit.setStartSize(0.05f);
        emit.setEndSize(0.05f);
        emit.setStartColor(ColorRGBA.White);
        emit.setEndColor(ColorRGBA.White);
        emit.setSelectRandomImage(true);
        emit.emitAllParticles();
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat.setBoolean("PointSprite", true);
        mat.setTexture("Texture", assetManager.loadTexture("Effects/Smoke/Smoke.png"));
        emit.setMaterial(mat);
        rootNode.attachChild(emit);
        inputManager.addListener(new ActionListener() {

            public void onAction(String name, boolean isPressed, float tpf) {
                if ("setNum".equals(name) && isPressed) {
                    emit.setNumParticles(5000);
                    emit.emitAllParticles();
                }
            }
        }, "setNum");
        inputManager.addMapping("setNum", new KeyTrigger(KeyInput.KEY_SPACE));
    }
}
