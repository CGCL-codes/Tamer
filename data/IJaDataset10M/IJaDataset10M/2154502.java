package org.xith3d.render.jsr231;

import javax.media.opengl.GL;
import org.jagatoo.logging.ProfileTimer;
import org.openmali.vecmath2.Colorf;
import org.xith3d.render.CanvasPeer;
import org.xith3d.render.OpenGLCapabilities;
import org.xith3d.render.OpenGLStatesCache;
import org.xith3d.render.RenderOptions;
import org.xith3d.render.RenderPeer;
import org.xith3d.render.RenderPeer.RenderMode;
import org.xith3d.render.preprocessing.RenderAtom;
import org.xith3d.render.states.StateUnit;
import org.xith3d.render.states.units.ColoringStateUnit;
import org.xith3d.render.states.units.StateUnitPeer;
import org.xith3d.scenegraph.ColoringAttributes;
import org.xith3d.scenegraph.TransparencyAttributes;
import org.xith3d.scenegraph.View;
import org.xith3d.utility.logging.X3DLog;

/**
 * Handles the shading for coloring attributes.
 * 
 * @author Yuri Vl. Gushchin
 * @author Lilian Chamontin [jsr231 port]
 * @author Marvin Froehlich (aka Qudus)
 * @author Julien Gouesse [JOGL 2.0 port]
 */
public class ColoringStateUnitPeer implements StateUnitPeer {

    public void apply(RenderAtom<?> atom, StateUnit stateUnit, Object glObj, CanvasPeer canvasPeer, RenderPeer renderPeer, OpenGLCapabilities glCaps, View view, OpenGLStatesCache statesCache, RenderOptions options, long nanoTime, long nanoStep, RenderMode renderMode, long frameId) {
        if (renderMode != RenderMode.NORMAL) return;
        ProfileTimer.startProfile(X3DLog.LOG_CHANNEL, "ColoringStateUnitPeer::apply()");
        final GL gl = (GL) glObj;
        final ColoringAttributes ca = ((ColoringStateUnit) stateUnit).getColoringAttributes();
        final Colorf color = ca.getColor();
        final TransparencyAttributes m = ((ColoringStateUnit) stateUnit).getTransparencyAttributes();
        if ((m.getMode() == TransparencyAttributes.BLENDED) && m.isEnabled()) {
            if (!statesCache.enabled || !statesCache.blendingEnabled) {
                X3DLog.debug("Blending enabled");
                gl.glEnable(GL.GL_BLEND);
                statesCache.blendingEnabled = true;
            }
            gl.glBlendFunc(m.getSrcBlendFunction().toOpenGL(), m.getDstBlendFunction().toOpenGL());
            gl.getGL2().glColor4f(color.getRed(), color.getGreen(), color.getBlue(), 1f - m.getTransparency());
            statesCache.color.set(color);
        } else {
            if (!statesCache.enabled || statesCache.blendingEnabled) {
                X3DLog.debug("Blending disabled");
                gl.glDisable(GL.GL_BLEND);
                statesCache.blendingEnabled = false;
            }
            gl.getGL2().glColor3f(color.getRed(), color.getGreen(), color.getBlue());
            statesCache.color.set(color.getRed(), color.getGreen(), color.getBlue());
        }
        gl.getGL2().glShadeModel(ca.getShadeModel().toOpenGL());
        ProfileTimer.endProfile();
    }
}
