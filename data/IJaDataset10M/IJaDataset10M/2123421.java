package com.jme3.post;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.texture.FrameBuffer;

/**
 * Processor that lays depth first, this can improve performance in complex
 * scenes.
 */
public class PreDepthProcessor implements SceneProcessor {

    private RenderManager rm;

    private ViewPort vp;

    private AssetManager assetManager;

    private Material preDepth;

    private RenderState forcedRS;

    public PreDepthProcessor(AssetManager assetManager) {
        this.assetManager = assetManager;
        preDepth = new Material(assetManager, "Common/MatDefs/Shadow/PreShadow.j3md");
        preDepth.getAdditionalRenderState().setPolyOffset(0, 0);
        preDepth.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Back);
        forcedRS = new RenderState();
        forcedRS.setDepthTest(true);
        forcedRS.setDepthWrite(false);
    }

    public void initialize(RenderManager rm, ViewPort vp) {
        this.rm = rm;
        this.vp = vp;
    }

    public void reshape(ViewPort vp, int w, int h) {
        this.vp = vp;
    }

    public boolean isInitialized() {
        return vp != null;
    }

    public void preFrame(float tpf) {
    }

    public void postQueue(RenderQueue rq) {
        rm.setForcedMaterial(preDepth);
        rq.renderQueue(RenderQueue.Bucket.Opaque, rm, vp.getCamera(), false);
        rm.setForcedMaterial(null);
        rm.setForcedRenderState(forcedRS);
    }

    public void postFrame(FrameBuffer out) {
        rm.setForcedRenderState(null);
    }

    public void cleanup() {
        vp = null;
    }
}
