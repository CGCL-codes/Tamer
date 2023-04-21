package org.jcrpg.threed.jme.program.impl;

import org.jcrpg.threed.J3DCore;
import org.jcrpg.threed.jme.program.EffectNode;
import com.jme.bounding.BoundingSphere;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jmex.effects.particles.ParticleFactory;
import com.jmex.effects.particles.ParticleMesh;

public class FumeCloud extends EffectNode {

    private ParticleMesh pMesh;

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public FumeCloud() {
        speed = 0.8f;
        BlendState as1 = J3DCore.getInstance().modelLoader.alphaStateParticleEffectBase;
        ZBufferState zstate = J3DCore.getInstance().modelLoader.zBufferStateOff;
        TextureState ts = J3DCore.getInstance().modelLoader.loadTextureStates(new String[] { "flaresmall.jpg" })[0];
        pMesh = cacheMesh.get(this.getClass());
        this.attachChild(getLightNode(new ColorRGBA(0.2f, 0.8f, 0.2f, 1)));
        if (pMesh == null) {
            pMesh = ParticleFactory.buildParticles("particles", 300);
            pMesh.setEmissionDirection(new Vector3f(0, 1, 0));
            pMesh.setInitialVelocity(.001f);
            pMesh.setStartSize(0.25f);
            pMesh.setEndSize(0.15f);
            pMesh.setMinimumLifeTime(1200f);
            pMesh.setMaximumLifeTime(1400f);
            pMesh.setStartColor(new ColorRGBA(0.1f, 0.6f, 0.2f, 1));
            pMesh.setEndColor(new ColorRGBA(0, 0.3f, 0.2f, 0));
            pMesh.setMaximumAngle(360f * FastMath.DEG_TO_RAD);
            pMesh.getParticleController().setControlFlow(true);
            pMesh.warmUp(20);
            pMesh.setRenderState(as1);
            pMesh.setRenderState(ts);
            pMesh.setRenderState(zstate);
            pMesh.setModelBound(new BoundingSphere());
            pMesh.updateModelBound();
            cacheMesh.put(this.getClass(), pMesh);
        } else {
            pMesh.setOriginOffset(new Vector3f(0, 0, 0));
        }
        this.attachChild(pMesh);
    }

    @Override
    public void setPosition(Vector3f newPos, Quaternion newAngle) {
        currentPos = newPos;
        if (pMesh != null) pMesh.setOriginOffset(currentPos);
        super.setPosition(newPos, newAngle);
    }

    @Override
    public void clearUp() {
        pMesh.removeFromParent();
        super.clearUp();
    }
}
