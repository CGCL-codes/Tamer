package de.grogra.imp3d.glsl.light;

import java.util.Iterator;
import javax.media.opengl.GL;
import javax.vecmath.Matrix3d;
import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import de.grogra.imp3d.glsl.GLSLDisplay;
import de.grogra.imp3d.glsl.OpenGLState;
import de.grogra.imp3d.glsl.renderpass.CopyFloatTexturePass;
import de.grogra.imp3d.glsl.renderpass.RenderPass;
import de.grogra.imp3d.objects.DirectionalLight;
import de.grogra.imp3d.objects.Sky;
import de.grogra.vecmath.Math2;

public class ProcessLightingPass extends RenderPass {

    @Override
    protected void epilogue(GLSLDisplay disp, OpenGLState glState, Object data) {
    }

    @Override
    protected void prologue(GLSLDisplay disp, OpenGLState glState, Object data) {
    }

    @Override
    protected void render(GLSLDisplay disp, OpenGLState glState, Object data) {
        DrawLights(disp, glState);
    }

    private LightingProcessPass lpp = new LightingProcessPass();

    private CopyFloatTexturePass cftp = new CopyFloatTexturePass();

    /**
	 * Iterate through all found lights, processing each by calling processLight
	 * on it.
	 * 
	 * @param disp
	 */
    public void DrawLights(GLSLDisplay disp, OpenGLState glState) {
        boolean gotOneLight = false;
        GL gl = glState.getGL();
        glState.enable(OpenGLState.STENCIL_TEST);
        gl.glStencilFunc(GL.GL_NOTEQUAL, 0x1, 0x1);
        gl.glStencilOp(GL.GL_KEEP, GL.GL_KEEP, GL.GL_KEEP);
        cftp.process(disp, glState, null);
        if (disp.isOptionLighting()) {
            Iterator<LightPos> it = glState.defLight.iterator();
            while (it.hasNext()) {
                LightPos light = it.next();
                if (disp.isOptionShowDiffuseSkyLight() || !(light.getLight() instanceof Sky)) {
                    lpp.process(disp, glState, light);
                    if (light.getLight().getLightType() != de.grogra.ray.physics.Light.NO_LIGHT) gotOneLight = true;
                }
            }
        }
        if (!gotOneLight) {
            LightPos pos = getDefaultLight(disp);
            lpp.process(disp, glState, pos);
        }
    }

    DirectionalLight defaultLight = null;

    Matrix4d defaultLightToWorld = new Matrix4d();

    Point3d p = new Point3d(-3e3, 3e3, 1e4);

    Vector3d v = new Vector3d(p);

    Matrix3d m = new Matrix3d();

    LightPos defaultLightPos = null;

    private LightPos getDefaultLight(GLSLDisplay disp) {
        Math2.makeAffine(defaultLightToWorld);
        Math2.invertAffine(disp.getCurrentGLState().getWorldToView(), defaultLightToWorld);
        p.set(-3e3, 3e3, 1e4);
        v.negate(p);
        defaultLightToWorld.transform(p);
        defaultLightToWorld.transform(v);
        Math2.getOrthogonalBasis(v, m, true);
        defaultLightToWorld.set(m);
        defaultLightToWorld.m03 = p.x;
        defaultLightToWorld.m13 = p.y;
        defaultLightToWorld.m23 = p.z;
        if (defaultLight == null) {
            defaultLight = new DirectionalLight();
            defaultLight.setShadowless(true);
            defaultLight.getColor().set(1, 1, 1);
        }
        if (defaultLightPos == null) defaultLightPos = new LightPos(defaultLight, defaultLightToWorld); else defaultLightPos.setLightPos(defaultLightToWorld);
        return defaultLightPos;
    }
}
