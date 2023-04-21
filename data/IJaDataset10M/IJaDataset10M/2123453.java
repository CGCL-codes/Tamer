package com.jme.app;

import com.jme.image.Texture;
import com.jme.renderer.Renderer;
import com.jme.util.geom.Debugger;

/**
 * Extends {@link BaseSimpleGame} to automatically update and render the root node.
 *
 * @author Joshua Slack
 * @version $Revision: 4131 $, $Date: 2009-03-19 13:15:28 -0700 (Thu, 19 Mar 2009) $
 */
public abstract class SimpleGame extends BaseSimpleGame {

    /**
     * Called every frame to update scene information.
     * 
     * @param interpolation
     *            unused in this implementation
     * @see BaseSimpleGame#update(float interpolation)
     */
    protected final void update(float interpolation) {
        super.update(interpolation);
        if (!pause) {
            simpleUpdate();
            rootNode.updateGeometricState(tpf, true);
            statNode.updateGeometricState(tpf, true);
        }
    }

    /**
     * This is called every frame in BaseGame.start(), after update()
     * 
     * @param interpolation
     *            unused in this implementation
     * @see AbstractGame#render(float interpolation)
     */
    protected final void render(float interpolation) {
        super.render(interpolation);
        Renderer r = display.getRenderer();
        r.draw(rootNode);
        simpleRender();
        r.draw(statNode);
        doDebug(r);
    }

    @Override
    protected void doDebug(Renderer r) {
        super.doDebug(r);
        if (showDepth) {
            r.renderQueue();
            Debugger.drawBuffer(Texture.RenderToTextureType.Depth, Debugger.NORTHEAST, r);
        }
    }
}
