package org.xith3d.loop;

/**
 * A ConsciousFPSListener is aware of the RenderLoop it gets its FPS count from.
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public interface ConsciousFPSListener extends FPSListener {

    /**
     * Tells the ConsciousFPSListener, which RenderLoop it is linked with.
     * 
     * @param renderLoop
     */
    public void setRenderLoop(RenderLoop renderLoop);
}
