package org.xith3d.render.jsr231;

import javax.media.opengl.GL;
import javax.vecmath.Color3f;
import org.xith3d.Xith3DDefaults;
import org.xith3d.render.CanvasPeer;
import org.xith3d.render.prerender.RenderAtom;
import org.xith3d.render.shader.ColoringShader;
import org.xith3d.render.shader.Shader;
import org.xith3d.render.shader.ShaderPeer;
import org.xith3d.scenegraph.ColoringAttributes;
import org.xith3d.scenegraph.TransparencyAttributes;
import org.xith3d.utility.logs.Log;
import org.xith3d.utility.logs.LogType;
import org.xith3d.utility.profile.ProfileTimer;

/**
 * Handles the shading for coloring attributes.
 * 
 * @author Yuri Vl. Gushchin
 * @author Lilian Chamontin (jsr231 port)
 * @author Marvin Froehlich (aka Qudus) [code cleaning]
 */
public class ColoringShaderPeer implements ShaderPeer {

    public ColoringShaderPeer() {
    }

    private int glMode(int mode) {
        switch(mode) {
            case TransparencyAttributes.BLEND_ONE:
                return GL.GL_ONE;
            case TransparencyAttributes.BLEND_ZERO:
                return GL.GL_ZERO;
            case TransparencyAttributes.BLEND_ONE_MINUS_SRC_ALPHA:
                return GL.GL_ONE_MINUS_SRC_ALPHA;
            case TransparencyAttributes.BLEND_SRC_ALPHA:
                return GL.GL_SRC_ALPHA;
            default:
                throw new Error("unsupported blend mode : " + mode);
        }
    }

    public void shade(CanvasPeer canvas, RenderAtom atom, Shader shader) {
        ProfileTimer.startProfile("ColoringShaderPeer::shade");
        final GL gl = ((CanvasPeerImplBase) canvas).getGL();
        ColoringAttributes ca = ((ColoringShader) shader).getColoring();
        Color3f color = ca.getColor();
        TransparencyAttributes m = ((ColoringShader) shader).getTransparency();
        if ((m.getMode() == TransparencyAttributes.BLENDED) && m.isEnabled()) {
            if (Xith3DDefaults.getLocalDebug(CanvasPeerImplBase.DEBUG_GL)) Log.print(LogType.DEBUG, "Blending enabled");
            gl.glEnable(GL.GL_BLEND);
            gl.glBlendFunc(glMode(m.getSrcBlendFunction()), glMode(m.getDstBlendFunction()));
            gl.glColor4f(color.x, color.y, color.z, 1f - m.getTransparency());
        } else {
            if (Xith3DDefaults.getLocalDebug(CanvasPeerImplBase.DEBUG_GL)) Log.print(LogType.DEBUG, "Blending disabled");
            gl.glDisable(GL.GL_BLEND);
            gl.glColor3f(color.x, color.y, color.z);
        }
        int shadeModel = ca.getShadeModel();
        switch(shadeModel) {
            case ColoringAttributes.FASTEST:
            case ColoringAttributes.SHADE_FLAT:
                gl.glShadeModel(GL.GL_FLAT);
                break;
            case ColoringAttributes.NICEST:
            case ColoringAttributes.SHADE_GOURAUD:
                gl.glShadeModel(GL.GL_SMOOTH);
                break;
        }
        ProfileTimer.endProfile();
    }

    public void doneShading(CanvasPeer canvas, RenderAtom atom, Shader shader) {
    }
}
